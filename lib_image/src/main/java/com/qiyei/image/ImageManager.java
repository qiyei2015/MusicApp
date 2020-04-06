package com.qiyei.image;

import android.app.Notification;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.io.File;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * Email: 1273482124@qq.com
 * @author Created by qiyei2015 on 2017/8/19.
 * Version: 1.0
 * Description: 图片管理器,管理图片的加载，压缩，切割等
 */
public class ImageManager implements IImageOperator{

    /**
     * 图片操作管理者
     */
    private IImageOperator mImageImpl;

    private static class SingleHolder{
        static final ImageManager sInstance = new ImageManager();
    }

    /**
     * 构造方法私有化
     */
    private ImageManager(){
        mImageImpl = new GlideImpl();
    }

    /**
     * 内部类方式单例
     * @return
     */
    public static ImageManager getInstance(){
        return SingleHolder.sInstance;
    }

    public void setImageOper(IImageOperator oper){
        mImageImpl = oper;
    }


    /**
     * 加载图片
     * @param url
     * @param imageView
     */
    @Override
    public void load(ImageView imageView, String url){
        mImageImpl.load(imageView,url);
    }

    /**
     * 加载图片
     * @param imageView
     * @param url
     * @param placeResId
     * @param errResId
     */
    @Override
    public void load(ImageView imageView, String url,int placeResId, int errResId){
        mImageImpl.load(imageView,url,placeResId,errResId);
    }

    @Override
    public void load(ImageView imageView, File file) {
        mImageImpl.load(imageView,file);
    }

    @Override
    public void load(ImageView imageView, int resId) {
        mImageImpl.load(imageView,resId);
    }

    @Override
    public void loadForCircle(ImageView imageView, String url) {
        mImageImpl.loadForCircle(imageView,url);
    }

    @Override
    public void loadForViewGroup(ViewGroup group, String url) {
        mImageImpl.loadForViewGroup(group,url);
    }

    @Override
    public void loadForNotification(Context context, RemoteViews rv, int id, Notification notification, int NOTIFICATION_ID, String url) {
        mImageImpl.loadForNotification(context,rv,id,notification,NOTIFICATION_ID,url);
    }

}
