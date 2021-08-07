package com.wujia.jetpack.pic;

public class ImageLoader {

    public static final ImageLoader IMAGE_LOADER = new ImageLoader();

    private ILoadStrategy mLoadStrategy;

    private ImageLoader() {
        mLoadStrategy = new GlideStrategy();
    }

    public static ImageLoader load(String url) {
        return IMAGE_LOADER;
    }

//    public static ImageLoader skipMemoryCache(boolean skip){
//        return IMAGE_LOADER;
//        Glide.with()
//    }
//
//    public static ImageLoader placeholder(@DrawableRes int drawableId){
//
//    }
//
//    public static ImageLoader centerCrop(boolean center){
//
//    }
//
//    public static ImageLoader animate(ViewPropertyAnimator animator){
//
//    }
//
//    public static ImageLoader into(ImageView view){
//
//    }
//
//    public void use(ILoadStrategy loadStrategy) {
//
//    }

}
