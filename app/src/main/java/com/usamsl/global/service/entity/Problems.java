package com.usamsl.global.service.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 * 问题库
 */
public class Problems implements Serializable{

    /**
     * result : [{"problem_tag":"护照已经到期","answer":"不需要，如果您的签证仍然有效，则您可以使用新旧两本护照到美国旅行，前提是签证未损坏，且旅行主要目的与签证类型相符 （例如： 如果主要旅行目的为旅游，则签证必须为旅游签证）。另外，两本护照上的姓名与其他个人信息必须一致，且两本护照必须是同一国家签发的同一类型护照（即均为旅游护照或外交护照）。","problem":"我的护照已经到期，但是其中的美国签证仍然有效。我是否需要申请新的签证？","country_name":"美国"},{"problem_tag":"旧护照过期了","answer":"不需要。如果您的签证仍然有效，您可以同时使用旧护照和新护照去美国。这两本护照（一本有效一本过期但含有有效签证）必须是同一个国家签发并且同一个护照类型（比如，他们必须都是因私护照或者都是公务护照）。","problem":"我的旧护照过期了，但我旧护照里的赴美签证仍然有效。我需要用新护照重新申请新的签证吗？","country_name":"美国"}]
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

    public static class ResultBean implements Serializable{
        /**
         * problem_tag : 护照已经到期
         * answer : 不需要，如果您的签证仍然有效，则您可以使用新旧两本护照到美国旅行，前提是签证未损坏，且旅行主要目的与签证类型相符 （例如： 如果主要旅行目的为旅游，则签证必须为旅游签证）。另外，两本护照上的姓名与其他个人信息必须一致，且两本护照必须是同一国家签发的同一类型护照（即均为旅游护照或外交护照）。
         * problem : 我的护照已经到期，但是其中的美国签证仍然有效。我是否需要申请新的签证？
         * country_name : 美国
         */

        private String problem_tag;
        private String answer;
        private String problem;
        private String country_name;

        public String getProblem_tag() {
            return problem_tag;
        }

        public void setProblem_tag(String problem_tag) {
            this.problem_tag = problem_tag;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getProblem() {
            return problem;
        }

        public void setProblem(String problem) {
            this.problem = problem;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }
    }
}
