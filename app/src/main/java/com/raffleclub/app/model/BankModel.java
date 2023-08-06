package com.raffleclub.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankModel {

    @SerializedName("result")
    private List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result{

        @SerializedName("bank_name")
        private String bank_name;
        @SerializedName("acc_name")
        private String acc_name;

        @SerializedName("success")
        private int success;

        @SerializedName("acc_no")
        private String acc_no;

        @SerializedName("ifsc_code")
        private String ifsc_code;

        @SerializedName("pan_no")
        private String pan_no;

        @SerializedName("proof_copy")
        private String proof_copy;

        @SerializedName("pan_card")
        private String pan_card;

        @SerializedName("acc_status")
        private String acc_status;

        @SerializedName("reject_reason")
        private String reject_reason;


        public String getReject_reason() {
            return reject_reason;
        }

        public void setReject_reason(String reject_reason) {
            this.reject_reason = reject_reason;
        }

        public String getPan_card() {
            return pan_card;
        }

        public void setPan_card(String pan_card) {
            this.pan_card = pan_card;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getAcc_name() {
            return acc_name;
        }

        public void setAcc_name(String acc_name) {
            this.acc_name = acc_name;
        }

        public int getSuccess() {
            return success;
        }

        public void setSuccess(int success) {
            this.success = success;
        }

        public String getAcc_no() {
            return acc_no;
        }

        public void setAcc_no(String acc_no) {
            this.acc_no = acc_no;
        }

        public String getIfsc_code() {
            return ifsc_code;
        }

        public void setIfsc_code(String ifsc_code) {
            this.ifsc_code = ifsc_code;
        }

        public String getPan_no() {
            return pan_no;
        }

        public void setPan_no(String pan_no) {
            this.pan_no = pan_no;
        }

        public String getProof_copy() {
            return proof_copy;
        }

        public void setProof_copy(String proof_copy) {
            this.proof_copy = proof_copy;
        }

        public String getAcc_status() {
            return acc_status;
        }

        public void setAcc_status(String acc_status) {
            this.acc_status = acc_status;
        }
    }

}
