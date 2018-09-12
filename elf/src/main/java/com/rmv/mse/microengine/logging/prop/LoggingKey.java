package com.rmv.mse.microengine.logging.prop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class LoggingKey {
    private final static Logger logger = LoggerFactory.getLogger(LoggingKey.class);
    public static final String MSISDN="msisdn";
    public static final String IMSI="imsi";
    public static final String SERVICE="service";
    public static final String REQUEST_ID = "request_id";
    public static final String TRANSACTIONID ="transaction_id" ;
    public static final String TYPE = "type";
    public static final String ACTIVITY = "activity";
    public static final Object TRANSACTION = "transaction";
    public static final String PROCESS_TIME = "process_time_ms";
    public static final String BEGIN = "begin";
    public static final String PROCESS = "function";
    public static final String PARTENT_TRANSACTION_ID = "parent_transaction_id";
    public static final String METHOD = "method";
    public static final String MESSAGE = "message";
    public static final String ACTIVITY_ID = "activity_id";
    public static final String EXCEPTION = "exception";
    public static final String EXCEPTIONCLASS = "exception_class";
    public static final String EXCEPTIONMESSAGE = "exception_message";
    public static final String STRACKTRACE = "stracktrace";
    public static final String HOSTNAME = "hostname";
    public static final String ELFVERSION_KEY ="elf_version" ;
    public static   Object ELFVERSION_VALUE = "unset";


    static{

        InputStream resourceAsStream =
                LoggingKey.class.getResourceAsStream(
                        "/version.properties"
                );
        Properties prop = new Properties();
        try
        {
             prop.load( resourceAsStream );
            ELFVERSION_VALUE=prop.getOrDefault("version","N/A");
        }catch (Exception e){
           // logger.error("load file error",e);
        }

    }
}
