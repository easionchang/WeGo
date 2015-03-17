/*
 * Copyright 2012 flyrise. All rights reserved.
 * Create at 2012-12-30 ����10:24:19
 */
package com.msn.support.network.base;

import org.json.JSONObject;


public class Response {
    public static final String OK_CODE = "0";
    public static final String ERROR_CODE = "1";
    
    private String namespace;
	private String errorCode;
    private String errorMessage;
    private String channelCommentId;
    
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getChannelCommentId() {
		return channelCommentId;
	}

	public void setChannelCommentId(String channelCommentId) {
		this.channelCommentId = channelCommentId;
	}
}
