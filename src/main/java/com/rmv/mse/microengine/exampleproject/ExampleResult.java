package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.model.ActivityResult;

public  class ExampleResult extends ActivityResult {
        private String messageid;

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
    }