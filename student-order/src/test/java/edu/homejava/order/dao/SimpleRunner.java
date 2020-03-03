package edu.homejava.order.dao;

import org.junit.Test;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SimpleRunner {
    public static void main(String[] args) {
        SimpleRunner sr = new SimpleRunner();

        sr.runTests();
    }

    private void runTests() {
        try {
            Class cl = Class.forName("edu.homejava.order.dao.DbAccessObj_ImplTest");

            Constructor cst = cl.getConstructor();
            Object entity = cst.newInstance();

            Method[] methods = cl.getMethods();
            // Передаем методу объект, для которого этот метод нужно вызвать.
            for (Method m : methods) {
                Test ann = m.getAnnotation(Test.class);
                if (ann != null) {
                    m.invoke(entity);
                }
            }


        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
