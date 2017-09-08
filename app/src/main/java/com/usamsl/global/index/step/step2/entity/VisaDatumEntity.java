package com.usamsl.global.index.step.step2.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */
public class VisaDatumEntity implements Serializable{
    private List<Result> result ;

    private Visa visa;


    private String reason;

    private int error_code;

    public void setResult(List<Result> result){
        this.result = result;
    }
    public List<Result> getResult(){
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
    public Visa getVisa() {
        return visa;
    }
    public void setVisa(Visa visa) {
        this.visa = visa;
    }



    public static class Result implements Serializable{
        private List<DatumList> datumList ;

        private int cust_type;

        public void setDatumList(List<DatumList> datumList){
            this.datumList = datumList;
        }
        public List<DatumList> getDatumList(){
            return this.datumList;
        }
        public void setCust_type(int cust_type){
            this.cust_type = cust_type;
        }
        public int getCust_type(){
            return this.cust_type;
        }
    }

    public static class Visa implements Serializable{
        private String service_desc;

        private String range_desc;

        public void setService_desc(String service_desc){
            this.service_desc = service_desc;
        }
        public String getService_desc(){
            return this.service_desc;
        }
        public void setRange_desc(String range_desc){
            this.range_desc = range_desc;
        }
        public String getRange_desc(){
            return this.range_desc;
        }

    }




}
