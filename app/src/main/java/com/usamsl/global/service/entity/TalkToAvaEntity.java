package com.usamsl.global.service.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 * 问题库
 */
public class TalkToAvaEntity implements Serializable {


    private String reason;
    private int error_code;
    private int listSum;
    private int pageSum;
    private int page;
    private int listAllSum;
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

    public int getListSum() {
        return listSum;
    }

    public void setListSum(int listSum) {
        this.listSum = listSum;
    }

    public int getPageSum() {
        return pageSum;
    }

    public void setPageSum(int pageSum) {
        this.pageSum = pageSum;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getListAllSum() {
        return listAllSum;
    }

    public void setListAllSum(int listAllSum) {
        this.listAllSum = listAllSum;
    }

    public static class ResultBean implements Serializable {
        /**
         * problem_tag : 护照已经到期
         * answer : 不需要，如果您的签证仍然有效，则您可以使用新旧两本护照到美国旅行，前提是签证未损坏，且旅行主要目的与签证类型相符 （例如： 如果主要旅行目的为旅游，则签证必须为旅游签证）。另外，两本护照上的姓名与其他个人信息必须一致，且两本护照必须是同一国家签发的同一类型护照（即均为旅游护照或外交护照）。
         * problem : 我的护照已经到期，但是其中的美国签证仍然有效。我是否需要申请新的签证？
         * country_name : 美国
         */

        private String project;

        private String answer_service;

        private String answer_ava;

        public void setProject(String project) {
            this.project = project;
        }

        public String getProject() {
            return this.project;
        }

        public void setAnswer_service(String answer_service) {
            this.answer_service = answer_service;
        }

        public String getAnswer_service() {
            return this.answer_service;
        }

        public void setAnswer_ava(String answer_ava) {
            this.answer_ava = answer_ava;
        }

        public String getAnswer_ava() {
            return this.answer_ava;
        }

    }
}