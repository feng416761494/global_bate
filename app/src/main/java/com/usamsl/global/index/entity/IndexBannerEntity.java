package com.usamsl.global.index.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */
public class IndexBannerEntity implements Serializable{
    private List<Result> result;

    private String reason;

    private int error_code;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public void setReason(String reason){
        this.reason = reason;
    }
    public String getReason(){
        return this.reason;
    }
    public void setError_code(int error_code){
        this.error_code = error_code;
    }
    public int getError_code(){
        return this.error_code;
    }

    public static class Result implements Serializable{
        private String ban_no;

        private String img_url;

        private String title;

        private String hyperlinks;

        public void setBan_no(String ban_no){
            this.ban_no = ban_no;
        }
        public String getBan_no(){
            return this.ban_no;
        }
        public void setImg_url(String img_url){
            this.img_url = img_url;
        }
        public String getImg_url(){
            return this.img_url;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setHyperlinks(String hyperlinks){
            this.hyperlinks = hyperlinks;
        }
        public String getHyperlinks(){
            return this.hyperlinks;
        }
    }
}
