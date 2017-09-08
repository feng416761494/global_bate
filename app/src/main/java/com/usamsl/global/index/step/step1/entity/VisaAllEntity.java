package com.usamsl.global.index.step.step1.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */
public class VisaAllEntity implements Serializable{
    private List<Result> result ;

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

    public static class Result implements Serializable{

        private int visa_area_id;

        private String visa_area_name;

        private int bs_company_id;

        private List<ListVisa> listVisa ;

        private int is_default;

        public void setVisa_area_id(int visa_area_id){
            this.visa_area_id = visa_area_id;
        }
        public int getVisa_area_id(){
            return this.visa_area_id;
        }
        public void setVisa_area_name(String visa_area_name){
            this.visa_area_name = visa_area_name;
        }
        public String getVisa_area_name(){
            return this.visa_area_name;
        }
        public void setBs_company_id(int bs_company_id){
            this.bs_company_id = bs_company_id;
        }
        public int getBs_company_id(){
            return this.bs_company_id;
        }
        public void setListVisa(List<ListVisa> listVisa){
            this.listVisa = listVisa;
        }
        public List<ListVisa> getListVisa(){
            return this.listVisa;
        }
        public void setIs_default(int is_default){
            this.is_default = is_default;
        }
        public int getIs_default(){
            return this.is_default;
        }
    }

}
