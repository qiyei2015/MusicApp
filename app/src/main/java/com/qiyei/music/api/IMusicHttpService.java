/**
 * @author Created by qiyei2015 on 2020/4/17.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: IMusicHttpService
 */
package com.qiyei.music.api;

import com.qiyei.music.common.Env;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface IMusicHttpService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET(Env.LOGIN)
    Call<Object> userLogin(@QueryMap Map<String, String> params);

}

