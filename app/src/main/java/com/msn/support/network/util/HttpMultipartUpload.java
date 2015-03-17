/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2012-12-21 上午11:34:43
 */

package com.msn.support.network.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.msn.support.network.XHttpClient;
import com.msn.support.network.base.FileRequest;
import com.msn.support.network.base.FileRequestContent;
import com.msn.support.network.base.Request;
import com.msn.support.network.base.Response;

/**
 * 类功能描述：</br>
 * 附件上传类，支持进度的获取，取消上传
 * @author zms
 * @version 1.0
 * </p>
 * 修改时间：</br>
 * 修改备注：</br>
 */
public class HttpMultipartUpload implements Runnable {
    public static final String TAG = "HttpMultipartUpload";
    public static final int TRY_TIMES = 3;//上传尝试次数
    public static final String UPLOAD_URL = XHttpClient.getBASE_URL() + XHttpClient.UPLOAD_PATH;
    public static final String HTTP_LINE_END = "\r\n";
    private static String twoHyphens = "--";
    private static String boundary = "AaB03x87yxdkjnxvi7";
    private static int maxBufferSize = 8 * 1024;
    private static String charSet = "UTF-8";

    private List<String> fileList;
    private Request requestContent;
    private FileRequest fileRequest;
    private HashMap<String, String> parameters;
    private FileUploadHandler<? extends Response> uploadHandler;
    private long filesSize;
    private long uploadedSize;
    private Thread uploadThread;
    private boolean isStop = Boolean.FALSE;
    private int progress = 0;//上传进度

    /**
     * 上传附件
     * @param  fileRequest
     * @param uploadHandler 上传回调接口
     */
    public HttpMultipartUpload upload(FileRequest fileRequest,FileUploadHandler<? extends Response> uploadHandler) {
        this.fileRequest = fileRequest;
        this.fileList = fileRequest.getFileContent().getFiles();
        this.requestContent = fileRequest.getRequestContent();
        this.uploadHandler = uploadHandler;
        uploadThread = new Thread(this);
        uploadThread.setName("HttpMultipartUpload--Thread");
        uploadThread.start();
        return this;
    }

    /**
     * 取消上传附件
     */
    public void cancel() {
        isStop = Boolean.TRUE;
    }

    @Override
    public void run() {
        for(int i=1;i<=TRY_TIMES;i++){
            if(!startUpload(i == TRY_TIMES)){ //不需要重新上传，上传成功，或者上传文件不存在
                break;
            }
        }
    }
    
    /**
     * 开始上传
     * @return 是否需要重新上传
     */
    private boolean startUpload(boolean isLastTry){
        boolean needReload = false;
        try {
            parameters = getUploadParams(fileRequest.getFileContent());
            post();
        } catch (FileNotFoundException e) {
            if (!isStop) {
                uploadHandler.sendFailureMessage(e, "上传文件不存在");
                Log.e(TAG, e.getMessage(), e);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            if (!isStop) {
                if(isLastTry){ //最后一次尝试失败，提示用户
                    uploadHandler.sendFailureMessage(e, "上传失败");
                }
                needReload = true;
            }
        }
        return needReload;
    }


    /**
     * 
     * 提交HTTP POST请求
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    private void post() throws IOException, FileNotFoundException,
            UnsupportedEncodingException {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        BufferedReader bin = null;

        try {
            filesSize = FileHelper.getSizeUnitByte(fileList);
            uploadedSize = 0;
            conn = getConnection(new URL(UPLOAD_URL));
            dos = new DataOutputStream(conn.getOutputStream());

            postFileParams(dos);
            postTextplainParams(dos);
            postEnd(dos);

            bin = new BufferedReader(new InputStreamReader(conn.getInputStream(), charSet));
            String rsp = getResponse(bin);
            if (!isStop) {
                uploadHandler.onUploadSuccess(rsp,requestContent);
                //uploadHandler.sendSuccessMessage();
            }
        } finally {
            release(conn, dos, bin);
        }
    }

    private HttpURLConnection getConnection(URL url) throws ProtocolException,
            IOException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setReadTimeout(15 * 1000);
        conn.setConnectTimeout(15 * 1000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);

        //HttpURLConnection默认会缓存所有的输入直到write完毕(造成OOM)，
        //设置setChunkedStreamingMode可以改变该属性，实行按块发送
        
        //由于信源项目中集群有问题所以取消了按块上传的机制，
        //改为客户端限制附件的总体大小，防止内存溢出（2013年8月28日）
        conn.setChunkedStreamingMode(maxBufferSize);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                + boundary);
        return conn;
    }

    /**
     * 
     * 提交附件域
     * @param dos
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.IOException
     */
    private void postFileParams(DataOutputStream dos)
            throws FileNotFoundException, UnsupportedEncodingException,
            IOException {
        for (int i = 0; i < fileList.size(); i++) {
            postFile(new File(fileList.get(i)), "file[" + i + "]", dos);
        }
    }

    private void postFile(File file, String fileFieldName, DataOutputStream dos)
            throws FileNotFoundException, UnsupportedEncodingException,
            IOException {
        if (isStop) {
            return;
        }
        postFileHeader(fileFieldName, file.getName(), dos);
        postFileContent(file, dos);
    }

    /**
     * 提交附件域的头信息
     * @param fileFieldName
     * @param fileName
     * @param dos
     * @throws java.io.IOException
     */
    private void postFileHeader(String fileFieldName, String fileName,
            DataOutputStream dos) throws IOException {
        dos.writeBytes(twoHyphens + boundary + HTTP_LINE_END);
        dos.write(encode("Content-Disposition: form-data; name=\""
                + fileFieldName + "\"; filename=\"" + fileName + "\""
                + HTTP_LINE_END));
        dos.writeBytes("Content-Type: application/octet-stream"
                        + HTTP_LINE_END);
        dos.writeBytes(HTTP_LINE_END);
    }

    /**
     * 提交附件域内容
     * @param file
     * @param dos
     * @throws java.io.IOException
     */
    private void postFileContent(File file, DataOutputStream dos)
            throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[Math.min((int)file.length(), maxBufferSize)];
            int length;

            if (file.length() != 0) { //文件为空直接写结束位就好
                // read file and write it into form...
                while ((length = fileInputStream.read(buffer)) != -1) {
                    dos.write(buffer, 0, length);
                    uploadedSize += length;
                    if (isStop) {
                        break;
                    }
                    publishProgress();
                }
            }

            dos.writeBytes(HTTP_LINE_END);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    /**
     * 提交文本域 
     * @param dos
     * @throws java.io.IOException
     */
    private void postTextplainParams(DataOutputStream dos) throws IOException {
        for (String name : parameters.keySet()) {
            dos.writeBytes(HTTP_LINE_END);
            dos.writeBytes(twoHyphens + boundary + HTTP_LINE_END);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"" + HTTP_LINE_END);
            dos.writeBytes(HTTP_LINE_END);
            dos.writeBytes(parameters.get(name));
        }
    }

    /**
     * 提交HTTP结束位
     * @param dos
     * @throws java.io.IOException
     */
    private void postEnd(DataOutputStream dos) throws IOException {
        dos.writeBytes(HTTP_LINE_END);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + HTTP_LINE_END);
        dos.flush();
    }

    /**
     * 对包含中文的字符串进行转码，此为UTF-8。
     */
    private byte[] encode(String value) throws UnsupportedEncodingException {
        return value.getBytes(charSet);
    }

    /**
     * 获取HTTP响应信息
     * @param bin
     * @return
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private String getResponse(BufferedReader bin) throws IOException {
        if (isStop) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = bin.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    private void release(HttpURLConnection conn, DataOutputStream dos,
            BufferedReader bin) {
        try {
            if (dos != null) {
                dos.close();
            }
            if (bin != null) {
                bin.close();
            }
        } catch (IOException e) {}
        
        if (conn != null) {
            conn.disconnect();
        }
    }

    /**
     * 进度变了，发布
     */
    private void publishProgress() {
        int cProgress = (int)(uploadedSize * 100 / filesSize);
        if(cProgress > 100){    //防止上传出现大于100的情况
            return;
        }
        
        if(cProgress > progress){   //防止重新上传的时候出现进度变小的情况
            progress = cProgress;
            uploadHandler.sendProgressMessage(progress);
        }
    }

    private HashMap<String, String> getUploadParams(FileRequestContent fileContent)
            throws Exception{
//            JSONObject properties = new JSONObject();
//            properties.put("iq", new JSONObject());
//            JSONObject parent = properties.getJSONObject("iq");
            
            JSONObject queryContent = new JSONObject();
//            if (fileContent.getAttachmentGUID() != null) {
//                queryContent.put("attachmentGUID", fileContent.getAttachmentGUID());
//            }
            
            if (fileContent.getOpenKey() != null) {
                queryContent.put("openKey", fileContent.getOpenKey());
            }
            
            if (fileContent.getType() != null) {
                queryContent.put("type", fileContent.getType());
            }
            
            if (fileContent.getIsCompress() != null) {
                queryContent.put("isCompress", fileContent.getIsCompress());
            }

//            if (fileContent.getDeleteFileIds() != null 
//                    && fileContent.getDeleteFileIds().size() > 0) {
//                JSONArray attachmentsProperty = new JSONArray();
//                queryContent.put("attachmentsDelete", attachmentsProperty);
//                for (String id : fileContent.getDeleteFileIds()) {
//                    JSONObject attachmentItem = new JSONObject();
//                    attachmentItem.put("id", id);
//                    attachmentsProperty.put(attachmentItem);
//                }
//            }
//            properties.put("query", queryContent);
            queryContent.put("namespace", "AttachmentUpdateRequest");
            
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("json", queryContent.toString());
            Log.d("dd", "上传附件协议---> " + queryContent );
            return params;
        }
}
