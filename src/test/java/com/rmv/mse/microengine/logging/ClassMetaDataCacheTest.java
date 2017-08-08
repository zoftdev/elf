package com.rmv.mse.microengine.logging;

import com.rmv.mse.microengine.logging.annotation.Activity;
import com.rmv.mse.microengine.logging.annotation.LogMDC;
import com.rmv.mse.microengine.logging.annotation.LogParam;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class ClassMetaDataCacheTest {


    ClassMetaDataCache classMetaDataCache = new ClassMetaDataCache();

    class TestOverload {

        @Activity
        public void test() {

        }


        @Activity
        public void test(String name) {

        }
    }

    class TestNotOverload {
        @Activity
        public void test() {

        }

        public void notAnnotatedMethod(String sameNameButNotAnnotated) {

        }


        public void test(String sameNameButNotAnnotated) {

        }

        @Activity
        public void test2(@LogParam("n") String name, int a, @LogParam("b") boolean b) {

        }
    }

    class ClassWithParam {
        @Activity
        public void pos0(@LogMDC Map map,String test) {

        }
        @Activity
        public void pos01(@LogMDC Map map,@LogMDC  String test) {

        }
        @Activity
        public void pos1( Map map,@LogMDC  String test) {

        }

        @Activity
        public void methodWithOutTranContext(String username) {

        }
    }




    @Test
    public void findTranContextPositionTest() throws Exception {

        classMetaDataCache.initialize(ClassWithParam.class);
        int pos = classMetaDataCache.getCachedClass().get(ClassWithParam.class).getActivtyMethod().get("pos0")
                .getPosOfLogMDC();
        assertEquals(0,pos);

        pos = classMetaDataCache.getCachedClass().get(ClassWithParam.class).getActivtyMethod().get("pos01")
                .getPosOfLogMDC();
        assertEquals(0,pos);

        pos = classMetaDataCache.getCachedClass().get(ClassWithParam.class).getActivtyMethod().get("pos1")
                .getPosOfLogMDC();
        assertEquals(1,pos);

    }

    @Test
    public void findTranContextPositionTest_whenNoContext_getNegative() throws Exception {

        classMetaDataCache.initialize(ClassWithParam.class);
        int pos = classMetaDataCache.getCachedClass().get(ClassWithParam.class).getActivtyMethod().get("methodWithOutTranContext")
                .getPosOfLogMDC();
        assertEquals(-1,pos);

    }
    @Test
    public void mapLogParam() throws Exception {

        Map<Integer, String> test = classMetaDataCache.mapLogParam(TestNotOverload.class.getMethod("test"));
        Assert.assertEquals(0, test.size());

        Map<Integer, String> test2 = classMetaDataCache.mapLogParam(TestNotOverload.class.getMethod("test2", String.class, int.class, boolean.class));
        Assert.assertEquals(2, test2.size());
        Assert.assertEquals("n", test2.get(0));
        Assert.assertEquals("b", test2.get(2));

    }

    @Test
    public void duplicates() throws Exception {


        assertThat(classMetaDataCache.isOverloadActivityAnnotation(TestOverload.class.getMethods()), is(true));
        assertThat(classMetaDataCache.isOverloadActivityAnnotation(TestNotOverload.class.getMethods()), is(false));


    }

    @Test
    public void whengetActivityMethod_thenGetMethod() throws Exception {
        classMetaDataCache.initialize(TestNotOverload.class);
        Method test = classMetaDataCache.getActivtyMethod(TestNotOverload.class, "test");
        assertThat(test.getName().equals("test"), is(true));
    }

    @Test
    public void whengetActivityMethod_withNotAnnotatedMethodName_thenError() throws Exception {
        classMetaDataCache.initialize(TestNotOverload.class);
        try {
            classMetaDataCache.getActivtyMethod(TestNotOverload.class, "notAnnotatedMethod");

            fail();
        } catch (AssertionError assertionError) {

        }

        try {
            classMetaDataCache.getActivtyMethod(TestNotOverload.class, "zzz");

            fail();
        } catch (AssertionError assertionError) {

        }


    }
}