package com.qiyei.network.dialog;


import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author Created by qiyei2015 on 2017/10/25.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class LoadingManager {
    public static final String TAG = "LoadingManager";

    /**
     * 显示对话框
     * @param manager
     * @param tag
     */
    public static void showDialog(Object manager, String tag){
        if (manager == null){
            return;
        }
        if (manager instanceof FragmentManager){
            LoadingDialog dialog = new LoadingDialog();
            dialog.setCancelable(false);
            FragmentManager fragmentManager = (FragmentManager) manager;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(dialog, tag);
            fragmentTransaction.commitNowAllowingStateLoss();//立即执行
            Log.i(TAG,"showDialog tag:" + tag);
        }
    }

    /**
     * 取消对话框显示
     * @param manager
     * @param tag
     */
    public static void dismissDialog(Object manager,String tag){

        if (manager == null){
            return;
        }
        if (manager instanceof FragmentManager){
            FragmentManager fragmentManager = (FragmentManager) manager;
            LoadingDialog dialog = (LoadingDialog) fragmentManager.findFragmentByTag(tag);
            if(dialog != null){
                dialog.dismissAllowingStateLoss();
                Log.i(TAG,"dismissDialog tag:" + tag);
            }
        }
    }

    /**
     * 显示对话框
     * @param manager
     * @param tag
     */
    public static void showProgressDialog(Object manager, String tag){
        if (manager == null){
            return;
        }

        if (manager instanceof FragmentManager){
            ProgressDialog dialog = new ProgressDialog();
            dialog.setCancelable(false);
            FragmentManager fragmentManager = (FragmentManager) manager;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(dialog, tag);
            fragmentTransaction.commitNowAllowingStateLoss();
            Log.i(TAG,"showProgressDialog tag:" + tag);
        }
    }

    /**
     * 取消对话框显示
     * @param manager
     * @param tag
     */
    public static void dismissProgressDialog(Object manager,String tag){
        if (manager == null){
            return;
        }
        if (manager instanceof FragmentManager){
            FragmentManager fragmentManager = (FragmentManager) manager;
            ProgressDialog dialog = (ProgressDialog) fragmentManager.findFragmentByTag(tag);
            if(dialog != null){
                dialog.dismissAllowingStateLoss();
                Log.i(TAG,"dismissProgressDialog tag:" + tag);
            }
        }
    }

    /**
     * 取消对话框显示
     * @param manager
     * @param tag
     * @param progress
     */
    public static void setProgress(Object manager,String tag,int progress){
        if (manager == null){
            return;
        }
        if (manager instanceof FragmentManager){
            FragmentManager fragmentManager = (FragmentManager) manager;
            ProgressDialog dialog = (ProgressDialog) fragmentManager.findFragmentByTag(tag);
            if(dialog != null){
                dialog.setProgress(progress);
            }
        }
    }

}
