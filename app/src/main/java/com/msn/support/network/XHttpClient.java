package com.msn.support.network;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.msn.support.network.base.FileRequest;
import com.msn.support.network.base.Request;
import com.msn.support.network.base.Response;
import com.msn.support.network.util.FileUploadHandler;
import com.msn.support.network.util.HttpMultipartUpload;
import com.msn.support.network.util.ModelHandler;


public class XHttpClient {

    
    private static String BASE_URL = "http://10.62.13.231:9092"; 



	
    private static final String BASE_PATH = "/servlet/oncampus";
    public static final String UPLOAD_PATH = "/servlet/uploadAttachmentServlet?";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static Gson mGson = new Gson();

    
    /**
     * 发起POST请求
     * @param reqContent
     * @param handler
     */
    public static <K extends Request,T extends Response> void post(
            K reqContent, ModelHandler<T> handler) {
            RequestParams params = new RequestParams();

            Log.d("dd", "请求内容---》 " + mGson.toJson(reqContent));

            params.put("json", mGson.toJson(reqContent));
            client.post(BASE_URL + BASE_PATH, params, handler);
    }
    
    
    /**
     * 提交带有附件的请求
     * @param fileRequest
     * @param handler
     * @return  HttpMultipartUpload实例可以取消附件的上传动作
     */
    public static <T extends Response> HttpMultipartUpload postFile(FileRequest fileRequest,FileUploadHandler<T> handler){
        HttpMultipartUpload upload = new HttpMultipartUpload();
        if(fileRequest.getFileContent() == null){
            post(fileRequest.getRequestContent(), handler);
        }else{
            upload.upload(fileRequest, handler);
        }
        return upload; 
    }
    
    
    
    /**
     * SET/GET
     * 
     * @return
     */
    public static String getBASE_URL() {
        return BASE_URL;
    }

}
