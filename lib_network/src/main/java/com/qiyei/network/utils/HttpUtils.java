package com.qiyei.network.utils;



import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.qiyei.network.api.HttpManager;
import com.qiyei.network.api.HttpRequest;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by qiyei2015 on 2017/10/24.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 功能工具类
 */
public class HttpUtils {

    private static final String TAG = HttpUtils.class.getSimpleName();

    /**
     * 将json解析为字符串
     * @param json
     * @param clazz
     * @param <T>
     */
    public static <T> T parseJson(String json,Class<?> clazz,boolean isInterface){
        if (json == null && clazz == null){
            return null;
        }
        Log.i(TAG,"clazz :" + clazz.getName());
        Type genType = null;

        if (isInterface){
            //获取type类型数组的第0个
            genType = clazz.getGenericInterfaces()[0];
        }else {
            genType = clazz.getGenericSuperclass();
        }
        Log.d(TAG,"genType:" + genType.toString());

        //判断是不是参数化类型
        if (genType instanceof ParameterizedType){
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            Gson gson = new Gson();
            T obj = gson.fromJson(json,params[0]);
            return obj;
        }
        return null;
    }

    /**
     * 获取泛型的参数clazz
     * @param clazz
     * @param isInterface 是否是接口
     */
    public static Class<?>  getParamsClazz(Class<?> clazz,boolean isInterface){

        Type genType = null;

        Log.d(TAG,"clazz :" + clazz.getName());

        if (isInterface && clazz.getInterfaces().length > 0){
            //获取type类型数组的第0个
            genType = clazz.getGenericInterfaces()[0];
        }else {
            genType = clazz.getGenericSuperclass();
        }
        Log.d(TAG,"genType:" + genType.toString());

        //判断是不是参数化类型
        if (genType instanceof ParameterizedType){
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            //利用Gson的TypeToken来获取
            TypeToken<?> typeToken = TypeToken.get(params[0]);
            Class<?> cla = typeToken.getRawType();
            Log.d(TAG,"Class<?>:" + cla);
            return cla;
        }
        return null;
    }


    /**
     * 获取泛型的参数clazz
     * @param clazz
     */
    public static Class<?>  getParamsClazz(Class<?> clazz) {
        Type type = getSuperclassTypeParameter(clazz);
        Class<?> cla = $Gson$Types.getRawType(type);
        if (cla != null){
            Log.i(TAG,"getParamsClazz cla: " + cla.getName());
        }else {
            Log.i(TAG,"getParamsClazz cla: null ");
        }
        return cla;
    }

    /**
     * 获取父类的ypeParameter
     * @param subclass
     * @return
     */
    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 将 httpGet请求进行参数的序列化为map对象
     * @param request
     * @param <T>
     * @return
     */
    public static <T> Map<String,String> gsonToGetParams(HttpRequest<T> request){

        //将对象序列化成字符串
        String gsonStr = new Gson().toJson(request.getBody());

        Map<String,String> map = new Gson().fromJson(gsonStr, new TypeToken<HashMap<String,String>>(){}.getType());
        return map;
    }


    private void printClazzMethodInfo(Class<?> clazz){
        for (Method method : clazz.getDeclaredMethods()){
            Log.i(HttpManager.TAG,"method: " + method.getName());
            printMethodParameterTypes(method.getParameterTypes());
        }
    }

    private void printMethodParameterTypes(Class<?>[] classes){
        for (Class<?> clazz : classes){
            Log.i(HttpManager.TAG,"params clazz : " + clazz.getName());
        }
    }


}
