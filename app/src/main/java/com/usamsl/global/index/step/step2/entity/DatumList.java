package com.usamsl.global.index.step.step2.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/24.
 */
public class DatumList implements Serializable{
        private String explain_desc;

        private String cust_type;

        private int visa_datum_id;

        private int visa_id;

        private String visa_datum_name;

        private int VISA_DATUM_TYPE;

        public void setExplain_desc(String explain_desc){
            this.explain_desc = explain_desc;
        }
        public String getExplain_desc(){
            return this.explain_desc;
        }
        public void setCust_type(String cust_type){
            this.cust_type = cust_type;
        }
        public String getCust_type(){
            return this.cust_type;
        }
        public void setVisa_datum_id(int visa_datum_id){
            this.visa_datum_id = visa_datum_id;
        }
        public int getVisa_datum_id(){
            return this.visa_datum_id;
        }
        public void setVisa_id(int visa_id){
            this.visa_id = visa_id;
        }
        public int getVisa_id(){
            return this.visa_id;
        }
        public void setVisa_datum_name(String visa_datum_name){
            this.visa_datum_name = visa_datum_name;
        }
        public String getVisa_datum_name(){
            return this.visa_datum_name;
        }
        public void setVISA_DATUM_TYPE(int VISA_DATUM_TYPE){
            this.VISA_DATUM_TYPE = VISA_DATUM_TYPE;
        }
        public int getVISA_DATUM_TYPE(){
            return this.VISA_DATUM_TYPE;
        }
}
