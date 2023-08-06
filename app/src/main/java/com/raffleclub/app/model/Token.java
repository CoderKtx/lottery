package com.raffleclub.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {


    @SerializedName("head")
    @Expose
    private Head head;

    @SerializedName("body")
    @Expose
    private Body body;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class ResultInfo {

        @SerializedName("resultStatus")
        @Expose
        private String resultStatus;

        @SerializedName("resultCode")
        @Expose
        private String resultCode;

        @SerializedName("resultMsg")
        @Expose
        private String resultMsg;

        public String getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(String resultStatus) {
            this.resultStatus = resultStatus;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

    }

    public static class Head {

        @SerializedName("responseTimestamp")
        @Expose
        private String responseTimestamp;
        @SerializedName("version")
        @Expose
        private String version;
        @SerializedName("requestId")
        @Expose
        private String requestId;
        @SerializedName("tokenType")
        @Expose
        private String tokenType;

        public String getResponseTimestamp() {
            return responseTimestamp;
        }

        public void setResponseTimestamp(String responseTimestamp) {
            this.responseTimestamp = responseTimestamp;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }

    public static class Body {

        @SerializedName("resultInfo")
        @Expose
        private ResultInfo resultInfo;

        @SerializedName("extraParamsMap")
        @Expose
        private String extraParamsMap;

        @SerializedName("txnToken")
        @Expose
        private String txnToken;

        @SerializedName("isPromoCodeValid")
        @Expose
        private boolean isPromoCodeValid;

        @SerializedName("authenticated")
        @Expose
        private boolean authenticated;

        public String getExtraParamsMap() {
            return extraParamsMap;
        }

        public void setExtraParamsMap(String extraParamsMap) {
            this.extraParamsMap = extraParamsMap;
        }

        public String getTxnToken() {
            return txnToken;
        }

        public void setTxnToken(String txnToken) {
            this.txnToken = txnToken;
        }

        public boolean isPromoCodeValid() {
            return isPromoCodeValid;
        }

        public void setPromoCodeValid(boolean promoCodeValid) {
            isPromoCodeValid = promoCodeValid;
        }

        public boolean isAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(boolean authenticated) {
            this.authenticated = authenticated;
        }

        public ResultInfo getResultInfo() {
            return resultInfo;
        }

        public void setResultInfo(ResultInfo resultInfo) {
            this.resultInfo = resultInfo;
        }


    }
}