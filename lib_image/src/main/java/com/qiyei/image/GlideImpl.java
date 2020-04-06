/**
 * Email: 1273482124@qq.com
 * @author Created by qiyei2015 on 2017/8/20.
 * Version: 1.0
 * Description: 采用Glide操作的Image图片管理实现
 */
package com.qiyei.image;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiyei.image.utils.ImageUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class GlideImpl implements IImageOperator {

    private RequestOptions mDefault;


    public GlideImpl() {
        mDefault = new RequestOptions();
        mDefault.placeholder(R.mipmap.image_loading_placeholder)
                .error(R.mipmap.image_error_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(mDefault)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @Override
    public void load(ImageView imageView, String url, int placeResId, int errResId) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(mDefault)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(placeResId)
                .error(errResId)
                .into(imageView);
    }

    @Override
    public void load(ImageView imageView, File file) {
        Glide.with(imageView.getContext())
                .load(file)
                .apply(mDefault)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @Override
    public void load(ImageView imageView, int resId) {
        Glide.with(imageView.getContext())
                .load(resId)
                .apply(mDefault)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @Override
    public void loadForCircle(final ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(mDefault)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(final Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    @Override
    public void loadForViewGroup(final ViewGroup group, String url) {
        Glide.with(group.getContext())
                .asBitmap()
                .load(url)
                .apply(mDefault).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                final Bitmap bitmap = resource;
                Observable.just(bitmap)
                        .map(new Function<Bitmap, Drawable>() {
                            @Override
                            public Drawable apply(Bitmap bitmap) throws Exception {
                                Drawable drawable = new BitmapDrawable(null,ImageUtils.doBlur(bitmap,100,true));
                                return drawable;
                            }
                        }).observeOn(Schedulers.computation())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Drawable>() {
                            @Override
                            public void accept(Drawable drawable) throws Exception {
                                group.setBackground(drawable);
                            }
                        });
            }
        });
    }

    @Override
    public void loadForNotification(Context context, RemoteViews rv, int id, Notification notification, int NOTIFICATION_ID, String url) {
        loadForNotificationInner(context,rv,id,notification,NOTIFICATION_ID,url);
    }

    private void loadForNotificationInner(Context context, RemoteViews rv, int id, Notification notification, int notification_id, String url) {

    }

//
//    /**
//     * 为非view加载图片
//     */
//    private void displayImageForTarget(Context context, Target target, String url) {
//        this.displayImageForTarget(context, target, url, null);
//    }
//
//    /**
//     * 为非view加载图片
//     */
//    private void displayImageForTarget(Context context, Target target, String url,
//                                       CustomRequestListener requestListener) {
//        Glide.with(context)
//                .asBitmap()
//                .load(url)
//                .apply(initCommonRequestOption())
//                .transition(withCrossFade())
//                .fitCenter()
//                .listener(requestListener)
//                .into(target);
//    }
//
//    /*
//     * 初始化Notification Target
//     */
//    private NotificationTarget initNotificationTarget(Context context, int id, RemoteViews rv,
//                                                      Notification notification, int NOTIFICATION_ID) {
//        NotificationTarget notificationTarget =
//                new NotificationTarget(context, id, rv, notification, NOTIFICATION_ID);
//        return notificationTarget;
//    }

}
