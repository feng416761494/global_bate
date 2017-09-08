package com.usamsl.global.index.step.step5.entity;

/**
 * Created by Administrator on 2017/2/17.
 * 在职证明
 */
public class JobProve {

    /**
     * result : {"address":"d","is_oneself":"0","sex":"d","birth_date":"2001-01-01","passport_no":" ","telephone":"4","travel_date":" ","contact_id":"65","salary":"d","travel_desc":" ","entry_date":"2001-02-01","date_number":" ","e_mail":"4","surname":"q","company_name":"f","name":"q","pay_type":"1","position":"d","return_date":" "}
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
         * address : d
         * is_oneself : 0
         * sex : d
         * birth_date : 2001-01-01
         * passport_no :
         * telephone : 4
         * travel_date :
         * contact_id : 65
         * salary : d
         * travel_desc :
         * entry_date : 2001-02-01
         * date_number :
         * e_mail : 4
         * surname : q
         * company_name : f
         * name : q
         * pay_type : 1
         * position : d
         * return_date :
         */

        private String address;
        private String is_oneself;
        private String sex;
        private String birth_date;
        private String passport_no;
        private String telephone;
        private String travel_date;
        private String contact_id;
        private String salary;
        private String travel_desc;
        private String entry_date;
        private String date_number;
        private String e_mail;
        private String surname;
        private String company_name;
        private String name;
        private String pay_type;
        private String position;
        private String return_date;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIs_oneself() {
            return is_oneself;
        }

        public void setIs_oneself(String is_oneself) {
            this.is_oneself = is_oneself;
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

        public String getPassport_no() {
            return passport_no;
        }

        public void setPassport_no(String passport_no) {
            this.passport_no = passport_no;
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

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public String getTravel_desc() {
            return travel_desc;
        }

        public void setTravel_desc(String travel_desc) {
            this.travel_desc = travel_desc;
        }

        public String getEntry_date() {
            return entry_date;
        }

        public void setEntry_date(String entry_date) {
            this.entry_date = entry_date;
        }

        public String getDate_number() {
            return date_number;
        }

        public void setDate_number(String date_number) {
            this.date_number = date_number;
        }

        public String getE_mail() {
            return e_mail;
        }

        public void setE_mail(String e_mail) {
            this.e_mail = e_mail;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getReturn_date() {
            return return_date;
        }

        public void setReturn_date(String return_date) {
            this.return_date = return_date;
        }
    }
}
