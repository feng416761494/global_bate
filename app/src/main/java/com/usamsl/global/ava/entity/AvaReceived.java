package com.usamsl.global.ava.entity;


/**
 * Created by Administrator on 2017/1/5.
 * 发送文字到后台后，返回的数据 ssa
 */
public class AvaReceived {

    /**
     * user_id : test_user
     * input_text : {"status":"return","text":"我是杜贤贤。"}
     * output_text : {"control":{"input":"NAME","value":"杜贤贤"},"text":"您叫杜贤贤。您的生日？","video":null}
     */

    private String user_id;
    private InputTextBean input_text;
    private OutputTextBean output_text;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public InputTextBean getInput_text() {
        return input_text;
    }

    public void setInput_text(InputTextBean input_text) {
        this.input_text = input_text;
    }

    public OutputTextBean getOutput_text() {
        return output_text;
    }

    public void setOutput_text(OutputTextBean output_text) {
        this.output_text = output_text;
    }

    public static class InputTextBean {
        /**
         * status : return
         * text : 我是杜贤贤。
         */

        private String status;
        private String text;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class OutputTextBean {
        /**
         * control : {"input":"NAME","value":"杜贤贤"}
         * text : 您叫杜贤贤。您的生日？
         * video : null
         */

        private ControlBean control;
        private String text;
        private Object video;

        public ControlBean getControl() {
            return control;
        }

        public void setControl(ControlBean control) {
            this.control = control;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Object getVideo() {
            return video;
        }

        public void setVideo(Object video) {
            this.video = video;
        }

        public static class ControlBean {
            /**
             * input : NAME
             * value : 杜贤贤
             */

            private String input;
            private String value;

            public String getInput() {
                return input;
            }

            public void setInput(String input) {
                this.input = input;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
