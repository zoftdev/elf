package com.rmv.mse.microengine.exampleproject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rmv.mse.microengine.logging.model.ActivityResult;

public  class ExampleResult extends ActivityResult {
        @JsonProperty
        private String messageid;
        private String new_password;
        public ExampleResult(String code, String namespace, String description, String messageid) {
            super(code, namespace, description);
            this.messageid=messageid;

        }


        public String getMessageid() {
            return messageid;
        }

        public void setMessageid(String messageid) {
            this.messageid = messageid;
        }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}