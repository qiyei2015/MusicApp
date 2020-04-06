package com.qiyei.network.server;


import android.text.TextUtils;

import androidx.fragment.app.FragmentManager;

import com.qiyei.network.api.HttpManager;
import com.qiyei.network.api.HttpRequest;
import com.qiyei.network.api.IHttpListener;
import com.qiyei.network.api.IHttpTransferListener;
import com.qiyei.network.utils.HttpUtils;


/**
 * @author Created by qiyei2015 on 2017/10/21.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: httpServer的代理
 */
public class HttpServerProxy {

    /**
     * httpServer 服务
     */
    private HttpServer mHttpServer;

    /**
     * 构造方法
     */
    public HttpServerProxy(){
        mHttpServer = HttpServer.getDefault();
    }

    /**
     * 执行https请求
     * @param request 请求参数
     * @param listener 回调listener
     * @param <T> 请求泛型参数
     * @param <R> 响应泛型参数
     * @return 该任务的taskId
     */
    public <T,R> String execute(HttpRequest<T> request, IHttpListener<R> listener) {
        checkParams(request,listener);
        return mHttpServer.execute(request,listener);
    }

    /**
     * 执行https请求
     * @param fragmentManager 显示对话框的fragment
     * @param request 请求参数
     * @param listener 回调listener
     * @param <T> 请求泛型参数
     * @param <R> 响应泛型参数
     * @return 该任务的taskId
     */
    public <T,R> String execute(FragmentManager fragmentManager, HttpRequest<T> request, IHttpListener<R> listener) {
        checkParams(request,listener);
        return mHttpServer.execute(fragmentManager,request,listener);
    }

    /**
     * 取消网络请求
     * @param taskId 需要取消的 taskId
     */
    public void cancel(String taskId) {
        if (TextUtils.isEmpty(taskId)){
            throw new NullPointerException(" the params taskId is null");
        }
        mHttpServer.cancel(taskId);
    }


    /**
     * 检查参数
     * @param request
     * @param listener
     */
    private <T,R> void checkParams(HttpRequest<T> request, IHttpListener<R> listener){

        if (request == null){
            throw new NullPointerException(" the params request is null");
        }

        //baseUrl不能为null
        if (TextUtils.isEmpty(request.getBaseUrl())){
            throw new NullPointerException(" the mBaseUrl of request is null");
        }

        //pathUrl不能为null
        if (TextUtils.isEmpty(request.getPathUrl())){
            throw new NullPointerException(" the mBaseUrl of request is null");
        }

        if (listener == null){
            throw new NullPointerException(" the params listener is null");
        }

        //IHttpsListener<R> listener 必须实例化
        if (HttpUtils.getParamsClazz(listener.getClass(),true) == null){
            throw new IllegalArgumentException(" the prams listener of IHttpsListener<R> must be Instantiated !");
        }

        //检查listener的类型
        if (request.getMethod().equals(HttpManager.DOWNLOAD) || request.getMethod().equals(HttpManager.UPLOAD)){
            if (!(listener instanceof IHttpTransferListener)){
                throw new IllegalArgumentException("the listener must be Instantiated of IHttpTransferListener");
            }
        }
    }

}
