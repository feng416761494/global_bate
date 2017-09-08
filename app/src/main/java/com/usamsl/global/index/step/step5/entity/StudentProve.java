package com.usamsl.global.index.step.step5.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/17.
 * 在校证明大学
 */
public class StudentProve {

    /**
     * result : {"college":" ","specialty":" ","is_college":"0","sex":"e","birth_date":"2001-01-01","telephone":"5525","travel_date":"2001-01-01","contact_id":"65","system_year":" ","student_name":"drt","date_number":"20","student_no":" ","surname":"q","name":"q","class":"1","age":"2","start_date":" "}
     * reason : 获取数据成功
     * error_code : 0
     */

    private ResultBean result;
    private String reason;
    private int error_code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

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

    public static class ResultBean {
        /**
         * college :
         * specialty :
         * is_college : 0
         * sex : e
         * birth_date : 2001-01-01
         * telephone : 5525
         * travel_date : 2001-01-01
         * contact_id : 65
         * system_year :
         * student_name : drt
         * date_number : 20
         * student_no :
         * surname : q
         * name : q
         * class : 1
         * age : 2
         * start_date :
         */

        private String college;
        private String specialty;
        private String is_college;
        private String sex;
        private String birth_date;
        private String telephone;
        private String travel_date;
        private String contact_id;
        private String system_year;
        private String student_name;
        private String date_number;
        private String student_no;
        private String surname;
        private String name;
        @SerializedName("class")
        private String classX;
        private String age;
        private String start_date;
        private String return_date;
        private String pay_promoter;

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public String getIs_college() {
            return is_college;
        }

        public void setIs_college(String is_college) {
            this.is_college = is_college;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirth_date() {
            return birth_date;
        }

        public void setBirth_date(String birth_date) {
            this.birth_date = birth_date;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getTravel_date() {
            return travel_date;
        }

        public void setTravel_date(String travel_date) {
            this.travel_date = travel_date;
        }

        public String getContact_id() {
            return contact_id;
        }

        public void setContact_id(String contact_id) {
            this.contact_id = contact_id;
        }

        public String getSystem_year() {
            return system_year;
        }

        public void setSystem_year(String system_year) {
            this.system_year = system_year;
        }

        public String getStudent_name() {
            return student_name;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public String getDate_number() {
            return date_number;
        }

        public void setDate_number(String date_number) {
            this.date_number = date_number;
        }

        public String getStudent_no() {
            return student_no;
        }

        public void setStudent_no(String student_no) {
            this.student_no = student_no;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getReturn_date() {
            return return_date;
        }

        public void setReturn_date(String return_date) {
            this.return_date = return_date;
        }

        public String getPay_promoter() {
            return pay_promoter;
        }

        public void setPay_promoter(String pay_promoter) {
            this.pay_promoter = pay_promoter;
        }
    }
}
