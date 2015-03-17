/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2013-3-18 涓嬪崍4:50:00
 */
package com.msn.support.network.base;

import java.util.List;

/**
 * 类功能描述：</br>
 *
 * @author zms
 * @version 1.0
 * </p>
 * 修改时间：</br>
 * 修改备注：</br>
 */
public class FileRequestContent {
    private List<String> files;
    private String openKey;
    private String type;
    private String isCompress;
//    private String attachmentGUID;
//    private List<String> deleteFileIds;
    
    public boolean isEmpty(){
        boolean empty = true;
        if(files != null && files.size() > 0){
            empty = false;
        }
        
//        if(deleteFileIds != null && deleteFileIds.size() > 0){
//            empty = false;
//        }
        return empty;
    }
    
    

//    public String getAttachmentGUID() {
//        return attachmentGUID;
//    }
//
//    public void setAttachmentGUID(String attachmentGUID) {
//        this.attachmentGUID = attachmentGUID;
//    }
//
//    public List<String> getDeleteFileIds() {
//        return deleteFileIds;
//    }
//
//    public void setDeleteFileIds(List<String> attachmentItemIds) {
//        this.deleteFileIds = attachmentItemIds;
//    }

    public String getIsCompress() {
		return isCompress;
	}



	public void setIsCompress(String isCompress) {
		this.isCompress = isCompress;
	}



	public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

	public String getOpenKey() {
		return openKey;
	}

	public void setOpenKey(String openKey) {
		this.openKey = openKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    

}
