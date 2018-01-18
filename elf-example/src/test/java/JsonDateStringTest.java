import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

public class JsonDateStringTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void testToString() throws JsonProcessingException {
        Obj o=new Obj();
        ObjectMapper om=new ObjectMapper();
        String s = om.writeValueAsString(o);
        logger.info("to string:{}",s);
    }



    public static class Obj implements Serializable{
        Date d=new Date();

        public Date getD() {
            return d;
        }

        public void setD(Date d) {
            this.d = d;
        }
    }
}
