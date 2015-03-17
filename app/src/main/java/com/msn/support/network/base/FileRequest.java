/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2013-3-18 下午4:50:00
 */
package com.msn.support.network.base;


/**
 * 类功能描述：</br>
 *
 * @author zms
 * @version 1.0
 * </p>
 * 修改时间：</br>
 * 修改备注：</br>
 */
public class FileRequest {
    private FileRequestContent fileContent;
    private Request requestContent;
    
    /**
     * 判断该请求中是否有附件需要上传
     * @return
     */
    public boolean isFileEmpty(){
        if(fileContent == null){
            return true;
        }
        
        return fileContent.isEmpty();
    }
    

    public FileRequestContent getFileContent() {
        return fileContent;
    }

    public void setFileContent(FileRequestContent fileContent) {
        this.fileContent = fileContent;
    }

    public Request getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(Request requestContent) {
        this.requestContent = requestContent;
    }
    
    
}
