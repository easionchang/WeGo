/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2012-12-31 ����04:32:07
 */
package com.msn.support.network.util;


import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.msn.support.network.base.Response;


public class ModelHandler<T extends Response> extends AsyncHttpResponseHandler{
    protected Class<T>  entityClass;  
    
 
    @SuppressWarnings("unchecked")
	public ModelHandler() {
        entityClass =(Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
    @SuppressWarnings("unchecked")
    public void onSuccess(String content) {
        super.onSuccess(content);
        Log.d("dd", "响应协议---> " + content);
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Response rsp = gson.fromJson(content,entityClass);
            showTip(rsp);
            if("-99".equals(rsp.getErrorCode())){
                //TODO 处理错误信息
            	//XLToast.showMessage(rsp.getErrorMessage());
            }
            onSuccess((T)rsp); 
            
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(e,e.getMessage());
            return;
        }
    }
    
    /**
     * 通用性的提示,两种情况直接提示
     * 1.通用响应
     * 2.非通用响应出错时
     * @param rsp
     */
    public void showTip(Response rsp){
        if("CommonResponse".equals(rsp.getNamespace()) || !Response.OK_CODE.equals(rsp.getErrorCode())){
            if(rsp.getErrorMessage() != null && !"".equals(rsp.getErrorMessage())){
//            	XLToast.showMessage(rsp.getErrorMessage());
            }
        }
    }
   
    
    /**
     * 格式化JSON内容
     */
    private String formatJsonString(JSONObject iq) throws Exception{
        Response instance = entityClass.newInstance();
        Method method= entityClass.getMethod("handle",JSONObject.class);
        return (String)method.invoke(instance, iq);
    }
   
    public void onSuccess(T rsp){}

    /** 
     *  用于中间转换的接口，由于服务器端的JSON有时候会不标准
     *  所以GSON会出现转换失败的情况，
     *  使用该回调接口可以将不标准的JSON内容转换为标准的JSON内容
     */
    public interface ITransformHandler{
        /**
         * @param content   服务器端返回的JSON对象
         * @return 转换后，GSON可以处理的JSON内容
         */
        String handle(JSONObject content);
    }
}
