/**
 * Email: 1273482124@qq.com
 * @author Created by qiyei2015 on 2017/8/19.
 * Version: 1.0
 * Description: 所有图片的操作
 */

package com.qiyei.image;

import android.app.Notification;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.io.File;

public interface IImageOperator {

    /**
     * 加载图片
     * @param imageView
     * @param url
     */
    void load(ImageView imageView, String url);

    /**
     *
     * @param imageView
     * @param url
     * @param placeResId
     * @param errResId
     */
    void load(ImageView imageView, String url, int placeResId, int errResId);

    /**
     *
     * @param imageView
     * @param file
     */
    void load(ImageView imageView, File file);

    /**
     *
     * @param imageView
     * @param resId
     */
    void load(ImageView imageView, int resId);

    /**
     *
     * @param imageView
     * @param url
     */
    void loadForCircle(ImageView imageView, String url);

    /**
     *
     * @param group
     * @param url
     */
    void loadForViewGroup(final ViewGroup group, String url);

    /**
     *
     * @param context
     * @param rv
     * @param id
     * @param notification
     * @param NOTIFICATION_ID
     * @param url
     */
    void loadForNotification(Context context, RemoteViews rv, int id,
                             Notification notification, int NOTIFICATION_ID, String url);


}
