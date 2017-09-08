package com.usamsl.global.index.step.step6.entity;

/**
 * Created by Administrator on 2017/3/29.
 * 获取签名字符串
 */
public class GetPay {

    /**
     * reason : 获取秘钥成功
     * error_code : 0
     * payCode : charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%22100.0%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E7%BE%8E%E5%9B%BD%E7%AD%BE%E8%AF%81EVUS%22%2C%22out_trade_no%22%3A%22032915140868360%22%7D&method=alipay.trade.app.pay&app_id=2016073100133101&sign_type=RSA2&version=1.0.1&timestamp=2017-03-29+15%3A14%3A08&sign=bSvpsUfwfa%2B4GRGXm6dINrPcYMqEBMzXkYnqNJ%2FT%2FHkmwKNtBCeZ2wy2atObNV8o%2B2wf1GIRtVvYnrgNm6D%2BiX9aHGFoBV65Dk6M0BPrlZnS%2FiqdKKsmTnlf5EAIxzEjpeXkLtUOhsr9zhqIPY2Kx15q8u6UEeUozU2LUktxyhCuWu9HkV%2FQIt9vhATjQPJLr3AcIaAA6dpIfnVaZFEk%2BGrHpDF288NR3tY97PRihFnEKUGKBqMLkFObIrrdsMkm%2Fn7Ikx0OCUKVF%2BAECpiK5KBT7%2FO8wIKnvaBua%2F1b4mCE8uIgbac18Df9hpYaa6ADY6sOM517eMT1yiPx%2FOb8oQ%3D%3D
     */

    private String reason;
    private int error_code;
    private String payCode;

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

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
}
