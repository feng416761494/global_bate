package com.usamsl.global.order.entity;

/**
 * Created by Administrator on 2017/4/27.
 */

public class FormSchedule {

    private Result result;

    private String reason;

    private int error_code;

    public void setResult(Result result){
        this.result = result;
    }
    public Result getResult(){
        return this.result;
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


    /**
     * {"result":{"form_url":"http://139.196.88.32:8080/GlobalVisa/app/uservisainfo/mobileAdd.do",
     * "request_url":"http://139.196.88.32:8080/GlobalVisa/app/uservisainfo/usaSave.do?ORDERID=67722",
     * "is_show":"GlobalVisa/app/uservisainfo/usaSave.do?ORDERID=67722"},
     * "reason":"获取数据成功","error_code":0}
     */

    public class Result {
        private String form_url;

        private String request_url;

        private String is_show;

        public void setForm_url(String form_url){
            this.form_url = form_url;
        }
        public String getForm_url(){
            return this.form_url;
        }
        public void setRequest_url(String request_url){
            this.request_url = request_url;
        }
        public String getRequest_url(){
            return this.request_url;
        }
        public void setIs_show(String is_show){
            this.is_show = is_show;
        }
        public String getIs_show(){
            return this.is_show;
        }

    }


}
