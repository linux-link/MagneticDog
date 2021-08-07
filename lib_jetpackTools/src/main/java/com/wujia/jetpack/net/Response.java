package com.wujia.jetpack.net;

public class Response<Data> {

    public static final int SUCCESS = 0;
    public static final int CACHE_SUCCESS = 304;
    public static final int RC_HAS_ERROR = 5000;               //有错误
    public static final int RC_ACCOUNT_INVALID = 5001;         //账号不存在
    public static final int RC_PWD_INVALID = 5002;             //密码错误
    public static final int RC_NEED_LOGIN = 5003;             //请先登录
    public static final int RC_NOT_PURCHASED = 5004;         //用户ID有误
    public static final int RC_CHECK_SERVER_ERROR = 5005;    //校验服务报错
    public static final int RC_USER_NAME_EXISTS = 5006;     //此用户名被占用
    public static final int RC_HTML_INVALID = 8001;      //请输入HTML
    public static final int RC_CONFIG_INVALID = 8002;    //请输入配置
    public static final int RC_USER_FORBID = 6001;       //用户身份非法，如有疑问可进入课程官方群联系管理员
    public static final int RC_AUTH_TOKEN_EXPIRED = 4030;   //访问Token过期，请重新设置
    public static final int RC_AUTH_TOKEN_INVALID = 4031;  //访问Token不正确，请重新设置

    private String mOriginData;
    private int mCode;
    private Data mData;
    private String mMsg;

    public String getOriginData() {
        return mOriginData;
    }

    public int getCode() {
        return mCode;
    }

    public Data getData() {
        return mData;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setOriginData(String originData) {
        mOriginData = originData;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public void setData(Data data) {
        mData = data;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }
}
