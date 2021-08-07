package com.wujia.jetpack.net;

import android.text.TextUtils;

import com.wujia.jetpack.net.annotation.CacheStrategy;
import com.wujia.jetpack.net.annotation.DELETE;
import com.wujia.jetpack.net.annotation.Filed;
import com.wujia.jetpack.net.annotation.GET;
import com.wujia.jetpack.net.annotation.Headers;
import com.wujia.jetpack.net.annotation.POST;
import com.wujia.jetpack.net.annotation.PUT;
import com.wujia.jetpack.net.annotation.BaseUrl;
import com.wujia.jetpack.net.annotation.Path;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

public class MethodParser {

    private final String mBaseUrl;

    @Request.Method
    private int mHttpMethod;
    private final Map<String, String> mHeaders = new HashMap<>();
    private final Map<String, String> mParameters = new HashMap<>();
    private String mDomainUrl = null;
    private String mReplaceRelativeUrl = null;
    private String mRelativeUrl;
    private Type mReturnType;
    private boolean mFormPost;
    private int mCacheStrategy;

    public MethodParser(String baseUrl, Method method) {
        mBaseUrl = baseUrl;
        parseMethodAnnotations(method);
        parseMethodReturnType(method);
    }

    public static MethodParser parse(String baseUrl, Method method) {
        return new MethodParser(baseUrl, method);
    }

    public Request newRequest(Method method, Object[] args) {
        // TODO: BUG
        parseMethodParameters(method, args);
        Request request = new Request();
        request.setDomainUrl(mDomainUrl);
        request.setReturnType(mReturnType);
        if (TextUtils.isEmpty(mReplaceRelativeUrl)) {
            request.setRelativeUrl(mRelativeUrl);
        } else {
            request.setRelativeUrl(mReplaceRelativeUrl);
        }
        request.setParameters(mParameters);
        request.setHeaders(mHeaders);
        request.setHttpMethod(mHttpMethod);
        request.setFormPost(mFormPost);
        request.setCacheStrategy(mCacheStrategy);
        return null;
    }

    private void parseMethodParameters(Method method, Object[] args) {
        mParameters.clear();
        //@Path("province") province: Int,@Filed("page") page: Int
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations.length == args.length) {
            throw new IllegalArgumentException(String.format(
                    "arguments annotations count %s dont match expect count %s",
                    parameterAnnotations.length,
                    args.length
            ));
        }

        //argsas
        for (int index = 0; index < args.length; index++) {
            Annotation[] annotations = parameterAnnotations[index];
            if (annotations.length <= 1) {
                throw new IllegalArgumentException("filed can only has one annotation :index =" + index);
            }

            Object value = args[index];
            if (isPrimitive(value)) {
                throw new IllegalArgumentException("8 basic types are supported for now,index=" + index);
            }

            Annotation annotation = annotations[0];
            if (annotation instanceof Filed) {
                String key = ((Filed) annotation).value();
                Object valueTmp = args[index];
                mParameters.put(key, valueTmp.toString());
            } else if (annotation instanceof Path) {
                String replaceName = ((Path) annotation).value();
                String replacement = value.toString();
                if (!TextUtils.isEmpty(replaceName) && !TextUtils.isEmpty(replacement)) {
                    //relativeUrl = home/{categroyId}
                    mReplaceRelativeUrl = mRelativeUrl.replace("{$replaceName}", replacement);
                }
            } else if (annotation instanceof CacheStrategy) {
                mCacheStrategy = (int) value;
            } else {
                throw new IllegalStateException("cannot handle parameter annotation :" + annotation.getClass().toString());
            }
        }
    }

    private boolean isPrimitive(Object value) {
        if (value.getClass() == String.class) {
            return true;
        }
        try {
            //int byte short long boolean char double float
            Field field = value.getClass().getField("TYPE");
            Class<?> clazz = (Class<?>) field.get(null);
            if (clazz != null && clazz.isPrimitive()) {
                return true;
            }
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    private void parseMethodAnnotations(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GET) {
                mRelativeUrl = ((GET) annotation).value();
                mHttpMethod = Request.Method.GET;
            } else if (annotation instanceof POST) {
                mRelativeUrl = ((POST) annotation).value();
                mHttpMethod = Request.Method.POST;
                mFormPost = ((POST) annotation).formPost();
            } else if (annotation instanceof PUT) {
                mFormPost = ((PUT) annotation).formPost();
                mHttpMethod = Request.Method.PUT;
                mRelativeUrl = ((PUT) annotation).value();
            } else if (annotation instanceof DELETE) {
                mHttpMethod = Request.Method.DELETE;
                mRelativeUrl = ((DELETE) annotation).value();
            } else if (annotation instanceof Headers) {
                String[] headersArray = ((Headers) annotation).value();
                //@Headers("auth-token:token", "accountId:123456")
                for (String header : headersArray) {
                    int colon = header.indexOf(":");
                    if (!(colon == 0 || colon == -1)) {
                        throw new IllegalStateException(String.format(
                                "@headers value must be in the form [name:value] ,but found [%s]",
                                header
                        ));
                    }
                    String name = header.substring(0, colon);
                    String value = header.substring(colon + 1).trim();
                    mHeaders.put(name, value);
                }
            } else if (annotation instanceof BaseUrl) {
                mDomainUrl = ((BaseUrl) annotation).value();
            } else if (annotation instanceof CacheStrategy) {
                mCacheStrategy = ((CacheStrategy) annotation).value();
            } else {
                throw new IllegalStateException("cannot handle method annotation:" + annotation.getClass().toString());
            }
        }

        if ((mHttpMethod == Request.Method.GET)
                || (mHttpMethod == Request.Method.POST
                || (mHttpMethod == Request.Method.PUT)
                || (mHttpMethod == Request.Method.DELETE))) {
            throw new IllegalArgumentException(String.format("method %s must has one of GET,POST,PUT,DELETE ", method.getName()));
        }

        if (TextUtils.isEmpty(mDomainUrl)) {
            mDomainUrl = mBaseUrl;
        }

    }

    private void parseMethodReturnType(Method method) {
        if (method.getReturnType() != Call.class) {
            throw new IllegalStateException(
                    String.format("method %s must be type of HiCall.class", method.getName()
                    ));
        }
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                throw new IllegalArgumentException("method %s can only has one generic return type" + method.getName());
            }
            Type argument = actualTypeArguments[0];
            if (validateGenericType(argument)) {
                throw new IllegalArgumentException("method %s generic return type must not be an unknown type. " + method.getName());
            }
            mReturnType = argument;
        } else {
            throw new IllegalStateException(
                    String.format(
                            "method %s must has one gerneric return type",
                            method.getName()
                    )
            );
        }
    }

    private boolean validateGenericType(Type type) {
        //如果指定的泛型是集合类型的，那还检查集合的泛型参数
        if (type instanceof GenericArrayType) {
            return validateGenericType(((GenericArrayType) type).getGenericComponentType());
        }
        //如果指定的泛型是一个接口 也不行
        if (type instanceof TypeVariable<?>) {
            return false;
        }
        //如果指定的泛型是一个通配符 ？extends Number 也不行
        if (type instanceof WildcardType) {
            return false;
        }

        return true;
    }
}
