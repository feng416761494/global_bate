package com.usamsl.global.index.step.step6.entity;

/**
 * Created by Administrator on 2017/2/21.
 * 支付结果
 */
public class AliPayResult {

    /**
     * alipay_trade_app_pay_response : {"code":"10000","msg":"Success","app_id":"2016073100133101","auth_app_id":"2016073100133101","charset":"utf-8","timestamp":"2017-02-21 12:02:19","total_amount":"8888.00","trade_no":"2017022121001004130200079294","seller_id":"2088102169295908","out_trade_no":"0221120210-5693"}
     * sign : IvhgjuYNrB6lRvqSae5aF5deo44njmJ96/SRxYyrYtZLMeiicatUxYfRdsuDtFtVsKuu3ZVwMWKnIvo09m6c+WubqYN6XL4Jc8qPaCHge+csMgh5TDfJUDj4zIFgeUTy9MnpLYVfoRZfSCICQoFmyoDXEj+X84bM0vaesBJ6S6dzroQsj/7AG0O5tLeFN73mywDe4Ub6OzQbTctybi/TsbobFX1RL0ilxTEQBG0biLRCR7TLL6U6lfFHrxMJ1wbcJns5X2I4SaQns8hbj9wBMszY21U+KyYPZsNRrqQFZNZ+Q3ne+bhZrCp2oLTQHk1OsEerhTArMXSDqLGe1rhA2w==
     * sign_type : RSA2
     */

    private AlipayTradeAppPayResponseBean alipay_trade_app_pay_response;
    private String sign;
    private String sign_type;

    public AlipayTradeAppPayResponseBean getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(AlipayTradeAppPayResponseBean alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class AlipayTradeAppPayResponseBean {
        /**
         * code : 10000
         * msg : Success
         * app_id : 2016073100133101
         * auth_app_id : 2016073100133101
         * charset : utf-8
         * timestamp : 2017-02-21 12:02:19
         * total_amount : 8888.00
         * trade_no : 2017022121001004130200079294
         * seller_id : 2088102169295908
         * out_trade_no : 0221120210-5693
         */

        private String code;
        private String msg;
        private String app_id;
        private String auth_app_id;
        private String charset;
        private String timestamp;
        private String total_amount;
        private String trade_no;
        private String seller_id;
        private String out_trade_no;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getAuth_app_id() {
            return auth_app_id;
        }

        public void setAuth_app_id(String auth_app_id) {
            this.auth_app_id = auth_app_id;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }
    }
}
