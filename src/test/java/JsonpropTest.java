import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rmv.mse.microengine.exampleproject.ExampleResult;
import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.argument.StructuredArguments;
import net.logstash.logback.marker.Markers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;

import java.util.HashMap;

/**
 * Created by zoftdev on 8/9/2017.
 */
public class JsonpropTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger loggerStash = LoggerFactory.getLogger("stash");

    @Test
    public void testJson() throws JsonProcessingException {
//        ObjectMapper om=new ObjectMapper();
//        om.disable(MapperFeature.AUTO_DETECT_CREATORS,
//                MapperFeature.AUTO_DETECT_FIELDS,
//                MapperFeature.AUTO_DETECT_GETTERS,
//                MapperFeature.AUTO_DETECT_IS_GETTERS);
//        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        ExampleResult r=new ExampleResult("0","SBM","okkkkk","11101");
        r.setNew_password("z");
//        String s = om.writeValueAsString(r);
//        logger.info("json convert @Jsonprop:{}",s);
        MDC.put("test","ja");
//        Marker ipArgument = Markers.append("exampleResult", r);
        Marker ipArgument = Markers.appendFields(r);
        StructuredArgument sa= StructuredArguments.value("sa",r);
        loggerStash.info(ipArgument,"json convert of sa",sa);

    }

    @Test
    public void testJson1Levl() throws JsonProcessingException {
        ExampleResult r=new ExampleResult("0","SBM","okkkkk","11101");
        r.setNew_password("z");

        HashMap<String,String>map=new HashMap<>();
        map.put("k1","v1");

        Marker marker=Markers.appendFields(r);
        marker.add(Markers.appendEntries(map));
        loggerStash.info(marker,"json convert of sa");


    }

}
