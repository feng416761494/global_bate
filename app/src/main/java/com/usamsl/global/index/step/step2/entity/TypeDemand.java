package com.usamsl.global.index.step.step2.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/24.
 * 根据人员类型返回要求
 */
public class TypeDemand {

    /**
     * result : [{"visa_datum_name":"户口本（原件）","visa_id":1,"cust_type":"自由职业者",
     * "explain_desc":"1.户口本整本信息#2.如有丢失请到派出所证明","visa_datum_id":13,"VISA_DATUM_TYPE":4}]
     * reason : 获取数据成功
     * error_code : 0
     */

    private String reason;
    private int error_code;
    private List<ResultBean> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * visa_datum_name : 户口本（原件）
         * visa_id : 1
         * cust_type : 自由职业者
         * explain_desc : 1.户口本整本信息#2.如有丢失请到派出所证明
         * visa_datum_id : 13
         * VISA_DATUM_TYPE : 4
         */

        private String visa_datum_name;
        private int visa_id;
        private String cust_type;
        private String explain_desc;
        private int visa_datum_id;
        private int VISA_DATUM_TYPE;

        public String getVisa_datum_name() {
            return visa_datum_name;
        }

        public void setVisa_datum_name(String visa_datum_name) {
            this.visa_datum_name = visa_datum_name;
        }

        public int getVisa_id() {
            return visa_id;
        }

        public void setVisa_id(int visa_id) {
            this.visa_id = visa_id;
        }

        public String getCust_type() {
            return cust_type;
        }

        public void setCust_type(String cust_type) {
            this.cust_type = cust_type;
        }

        public String getExplain_desc() {
            return explain_desc;
        }

        public void setExplain_desc(String explain_desc) {
            this.explain_desc = explain_desc;
        }

        public int getVisa_datum_id() {
            return visa_datum_id;
        }

        public void setVisa_datum_id(int visa_datum_id) {
            this.visa_datum_id = visa_datum_id;
        }

        public int getVISA_DATUM_TYPE() {
            return VISA_DATUM_TYPE;
        }

        public void setVISA_DATUM_TYPE(int VISA_DATUM_TYPE) {
            this.VISA_DATUM_TYPE = VISA_DATUM_TYPE;
        }
    }
}
