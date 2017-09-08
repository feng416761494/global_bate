package com.usamsl.global.index.step.step7.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/2/10.
 * 我的订单上传资料返回值
 */
public class OrderDatum {

    /**
     * result : [{"explain_desc":"1.原件#2.正反面#3.复印件3张","visa_datum_id":1,
     * "approval_status":0,"is_must":1,"attachment_id":0,"use_status":"1",
     * "visa_datum_name":"身份证","show_order":1,"order_id":101,"visa_datum_type":"1",
     * "path_name":"http://139.196.88.32/oss/b5f7438a-ca73-4f45-bb91-cf982bfd2c23.png",
     * "order_datum_id":551},{"explain_desc":" 原件","visa_datum_id":16,"approval_status":0,
     * "is_must":1,"attachment_id":0,"use_status":"1","visa_datum_name":"护照","show_order":2,
     * "order_id":101,"visa_datum_type":"2","path_name":"http://139.196.88.32/oss/b5f7438a-ca73-4f45-bb91-cf982bfd2c23.png",
     * "order_datum_id":553},{"explain_desc":"1.户口本整本信息#2.如有丢失请到派出所证明","visa_datum_id":2,"approval_status":0,"is_must":1,"attachment_id":0,"use_status":"1","visa_datum_name":"户口本（原件）","show_order":3,"order_id":101,"visa_datum_type":"4","path_name":"http://139.196.88.32/oss/b5f7438a-ca73-4f45-bb91-cf982bfd2c23.png","order_datum_id":552},{"explain_desc":"拍照","visa_datum_id":17,"approval_status":0,"is_must":1,"attachment_id":0,"use_status":"1","visa_datum_name":"1寸照片","show_order":4,"order_id":101,"visa_datum_type":"3","path_name":"http://139.196.88.32/oss/b5f7438a-ca73-4f45-bb91-cf982bfd2c23.png","order_datum_id":554}]
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
         * explain_desc : 1.原件#2.正反面#3.复印件3张
         * visa_datum_id : 1
         * approval_status : 0
         * is_must : 1
         * attachment_id : 0
         * use_status : 1
         * visa_datum_name : 身份证
         * show_order : 1
         * order_id : 101
         * visa_datum_type : 1
         * path_name : http://139.196.88.32/oss/b5f7438a-ca73-4f45-bb91-cf982bfd2c23.png
         * order_datum_id : 551
         */

        private String explain_desc;
        private int visa_datum_id;
        private int approval_status;
        private int is_must;
        private int attachment_id;
        private String use_status;
        private String visa_datum_name;
        private int show_order;
        private int order_id;
        private String visa_datum_type;
        private String path_name;
        private int order_datum_id;

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

        public int getApproval_status() {
            return approval_status;
        }

        public void setApproval_status(int approval_status) {
            this.approval_status = approval_status;
        }

        public int getIs_must() {
            return is_must;
        }

        public void setIs_must(int is_must) {
            this.is_must = is_must;
        }

        public int getAttachment_id() {
            return attachment_id;
        }

        public void setAttachment_id(int attachment_id) {
            this.attachment_id = attachment_id;
        }

        public String getUse_status() {
            return use_status;
        }

        public void setUse_status(String use_status) {
            this.use_status = use_status;
        }

        public String getVisa_datum_name() {
            return visa_datum_name;
        }

        public void setVisa_datum_name(String visa_datum_name) {
            this.visa_datum_name = visa_datum_name;
        }

        public int getShow_order() {
            return show_order;
        }

        public void setShow_order(int show_order) {
            this.show_order = show_order;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getVisa_datum_type() {
            return visa_datum_type;
        }

        public void setVisa_datum_type(String visa_datum_type) {
            this.visa_datum_type = visa_datum_type;
        }

        public String getPath_name() {
            return path_name;
        }

        public void setPath_name(String path_name) {
            this.path_name = path_name;
        }

        public int getOrder_datum_id() {
            return order_datum_id;
        }

        public void setOrder_datum_id(int order_datum_id) {
            this.order_datum_id = order_datum_id;
        }
    }
}
