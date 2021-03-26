package com.landis.eoswallet.net.manage;

import com.blankj.utilcode.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class HttpInterceptor {

     public static Interceptor requestBuilderInterceptor(){
         return new Interceptor() {
             @Override
             public Response intercept(Chain chain) throws IOException {
                 Request request = chain.request();
                 if (request.method().equals("POST")) {
                     if (request.body() instanceof FormBody) {
                         JSONObject jsonObject = new JSONObject();
                         FormBody.Builder bodyBuilder = new FormBody.Builder();
                         FormBody formBody = (FormBody) request.body();
                         for (int i = 0; i < formBody.size(); i++) {
                             bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                             try {
                                 jsonObject.put(formBody.encodedName(i), formBody.encodedValue(i));
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }
                         }
                         LogUtils.d("请求地址+" + request.url() + "请求参数:" + jsonObject);
                         RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                         request = request.newBuilder().post(body).build();
                     }
                 }
                 Request.Builder builder = request.newBuilder().url(request.url());
                 builder.addHeader("Content-Type", "application/json");
//            builder.addHeader("Accept", "*/*");
                 return chain.proceed(builder.build());
             }
         };
     }
}
