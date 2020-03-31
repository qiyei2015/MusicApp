package com.qiyei.network.server.okhttp;



import android.util.Log;

import com.qiyei.network.api.HttpManager;
import com.qiyei.network.server.HttpTask;
import com.qiyei.network.utils.TimeUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Created by qiyei2015 on 2017/11/3.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class MyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();
        //取出okHttp tag保存的数据
        HttpTask task = (HttpTask) original.tag();
        if (task == null){
            return chain.proceed(original);
        }

        //获取taskId
        String taskId = task.getTaskId();

        //新的request
        Request newRequest = handleDns(task,original).build();

        long requestTime = System.currentTimeMillis();

        switch (task.getRequest().getMethod()){
            case HttpManager.POST:
                RequestBody rb = newRequest.body();
                //只有普通的post请求打印 body UPLOAD 请求不打印
                if (rb != null) {
                    okio.Buffer buffer = new okio.Buffer();
                    rb.writeTo(buffer);
                    Log.i(HttpManager.TAG, getRequestInfo(newRequest,taskId,System.currentTimeMillis()) + "\nbody = " + buffer.readUtf8());
                    buffer.clear();
                } else {
                    Log.i(HttpManager.TAG, getRequestInfo(newRequest,taskId,System.currentTimeMillis()) + "\nbody = null");
                }
                break;
            case HttpManager.GET:
            case HttpManager.DOWNLOAD:
            case HttpManager.UPLOAD:
                Log.i(HttpManager.TAG, getRequestInfo(newRequest,taskId,System.currentTimeMillis()));
                break;
            default:
                break;
        }

        // TODO: 2017/11/4 后续再优化一下，保存缓存的响应等
        //获取到响应
        okhttp3.Response response = chain.proceed(newRequest);

        //下载请求 直接返回 防止崩溃OOM
        if (task.getRequest().getMethod().equals(HttpManager.DOWNLOAD)){
            Log.i(HttpManager.TAG, getResponseInfo(taskId,requestTime,System.currentTimeMillis()));
            return response;
        }

        //打印response信息
        okhttp3.MediaType mediaType = response.body().contentType();
        String body = response.body().string();
        Log.i(HttpManager.TAG, getResponseInfo(taskId,requestTime,System.currentTimeMillis()) + "\nbody: " +  body);
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, body)).build();
    }

    /**
     * 添加DNS和Header
     * @param task
     * @param request
     * @return
     */
    private Request.Builder handleDns(HttpTask task, Request request){

        Request.Builder requestBuilder = request.newBuilder();

        Map<String,String> headerMap = task.getRequest().getHeaderMap();
        for (String key : headerMap.keySet()) {
            requestBuilder.addHeader(key, headerMap.get(key));
        }
//        String ip = DnsManager.getInstance().findIpByHost(original.url().host());
//        if (!TextUtils.isEmpty(ip)) {
//            requestBuilder.addHeader("host", original.url().host());
//            requestBuilder.url(original.url().toString().replace(original.url().host(), ip));
//        }
        return requestBuilder;
    }



    /**
     * 请求信息整理
     * @param request
     * @param taskId
     * @param time
     * @return
     */
    private String getRequestInfo(Request request, String taskId, long time){
        String requestInfo = "Request ---> time: " + TimeUtils.formatTime(time,TimeUtils.FORMAT_1)
                + " id: " + taskId  + " url = " + request.url();

        return requestInfo;
    }

    /**
     * 响应信息格式
     * @param taskId
     * @param requestTime
     * @param time
     * @return
     */
    private String getResponseInfo(String taskId,long requestTime,long time){
        String responseInfo = "Response --> time: " + TimeUtils.formatTime(time,TimeUtils.FORMAT_1)
                + " id: "  + taskId + " " + (System.currentTimeMillis() - requestTime) + "ms";
        return responseInfo;
    }

}
