package com.qiyei.network.api;


import androidx.fragment.app.FragmentManager;

import com.qiyei.network.server.HttpServerProxy;


/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class HttpManager implements IHttpExecutor {


    /**
     * 调试标志
     */
    public static final String TAG = "HTTPS";
    /**
     * GET请求
     */
    public static final String GET = "GET";
    /**
     * POST请求
     */
    public static final String POST = "POST";
    /**
     * DOWNLOAD请求
     */
    public static final String DOWNLOAD = "DOWNLOAD";
    /**
     * UPLOAD请求
     */
    public static final String UPLOAD = "UPLOAD";


    /**
     * HttpServer 服务代理
     */
    private HttpServerProxy mProxy;

    /**
     * 构造方法
     */
    public HttpManager(){
        mProxy = new HttpServerProxy();
    }

    /**
     * 执行https请求
     * @param request 请求参数
     * @param listener 回调listener
     * @param <T> 泛型参数
     * @return 该任务的taskId
     */
    @Override
    public <T,R> String execute(HttpRequest<T> request, IHttpListener<R> listener){
        return mProxy.execute(request,listener);
    }

    /**
     * 执行https请求
     * @param fragmentManager 显示对话框的fragment
     * @param request 请求参数
     * @param listener 回调listener
     * @param <T> 泛型参数
     * @return 该任务的taskId
     */
    @Override
    public <T,R> String execute(FragmentManager fragmentManager, HttpRequest<T> request, IHttpListener<R> listener){
        return mProxy.execute(fragmentManager,request,listener);
    }

    /**
     * 取消网络请求
     * @param taskId 需要取消的 taskId
     */
    @Override
    public void cancel(String taskId){
        mProxy.cancel(taskId);
    }

}
