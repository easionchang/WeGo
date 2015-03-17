/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2012-12-22 上午10:54:29
 */
package com.msn.support.network.util;

import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.msn.support.network.XHttpClient;
import com.msn.support.network.base.Request;
import com.msn.support.network.base.Response;


/**
 * 类功能描述：</br>
 *
 * @author zms
 * @version 1.0
 * </p>
 * 修改时间：</br>
 * 修改备注：</br>
 */
public class FileUploadHandler<T extends Response> extends ModelHandler<T>{
    private static final int PROGRESS_MESSAGE = 4;

    
    public void onProgress(int progress) {}
    

    
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch(msg.what) {
            case PROGRESS_MESSAGE:
                onProgress((Integer)msg.obj);
                break;
        }
    }
    
    public void onUploadSuccess(String rsp,Request rquestContent) {
        try {
            JSONObject properties = new JSONObject(rsp);
            String iqContent = properties.toString();
            Log.d("dd", "上传附件结果---> " + iqContent);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
//            ResponseContent rsponseObj = gson.fromJson(iqContent, ResponseContent.class);
            AttachmentUpdateResponse rsponseObj = gson.fromJson(iqContent, AttachmentUpdateResponse.class);
            if("0".equals(rsponseObj.getErrorCode())){
            	if(rsponseObj.getUrl()!=null){
                	onUrlSuccess(rsponseObj.getUrl());
            	}
                if(rquestContent == null){
                    sendSuccessMessage(rsp);
                }else{
                    XHttpClient.post(rquestContent, this);
                }
            }else{
                Exception e = new Exception(rsponseObj.getErrorMessage());
                sendFailureMessage(e, rsponseObj.getErrorMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendFailureMessage(e,e.getMessage());
        }
    }
    
    public void onUrlSuccess(String url){
    	
    }
    
    public void sendProgressMessage(int progress) {
        sendMessage(obtainMessage(PROGRESS_MESSAGE, progress));
    }
    
    public void sendSuccessMessage(String responseBody) {
        super.sendSuccessMessage(responseBody);
    }

    public void sendFailureMessage(Throwable e, String responseBody) {
        super.sendFailureMessage(e, responseBody);
    }
}
