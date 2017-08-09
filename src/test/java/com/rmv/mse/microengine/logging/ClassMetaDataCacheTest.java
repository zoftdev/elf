package com.rmv.mse.microengine.logging;

import com.rmv.mse.microengine.logging.annotation.ActivityLogging;
import com.rmv.mse.microengine.logging.model.TransactionLoggingContext;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class ClassMetaDataCacheTest {



    ClassMetaDataCache classMetaDataCache = new ClassMetaDataCache();

    class TestOverload {

        @ActivityLogging
        public void test() {

        }


        @ActivityLogging
        public void test(String name) {

        }
    }

    class TestNotOverload {
        @ActivityLogging
        public void test() {

        }

        public void notAnnotatedMethod(String sameNameButNotAnnotated) {

        }


        public void test(String sameNameButNotAnnotated) {

        }

           }

    public class ClassWithParam{
        @ActivityLogging
        public void pos0(TransactionLoggingContext c){

        }
        @ActivityLogging
        public void noParam(){

        }


        @ActivityLogging
        public void noContext(String a){

        }

        @ActivityLogging
        public void pos1(String a,TransactionLoggingContext c){

        }
    }

    @Test
    public void findTranContextPositionTest() throws Exception {

        classMetaDataCache.initialize(ClassWithParam.class);
        int pos = classMetaDataCache.getCachedClass().get(ClassWithParam.class).getActivtyMethod().get("pos0")
                .getPosOfTransactionLogging();
        assertEquals(0,pos);
        classMetaDataCache.initialize(ClassWithParam.class);

        pos = classMetaDataCache.getCachedClass().get(ClassWithParam.class).getActivtyMethod().get("noContext")
                .getPosOfTransactionLogging();
        assertEquals(-1,pos);

        pos = classMetaDataCache.getCachedClass().get(ClassWithParam.class).getActivtyMethod().get("noParam")
                .getPosOfTransactionLogging();
        assertEquals(-1,pos);

        pos = classMetaDataCache.getCachedClass().get(ClassWithParam.class).getActivtyMethod().get("pos1")
                .getPosOfTransactionLogging();
        assertEquals(1,pos);

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