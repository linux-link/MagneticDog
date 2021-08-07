package com.wujia.jetpack.net.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@IntDef({CacheStrategy.CACHE_FIRST, CacheStrategy.NET_FIRST, CacheStrategy.NET_ONLY})
public @interface CacheStrategy {
    int value() default NET_ONLY;

    int CACHE_FIRST = 1;
    int NET_FIRST = 2; // 先经过网络，然后更新本地缓存
    int NET_ONLY = 3;
}
