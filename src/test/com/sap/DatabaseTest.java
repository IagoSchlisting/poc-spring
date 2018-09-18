package com.sap;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration({
        "file:WebContent/WEB-INF/applicationContext.xml",
        "file:WebContent/WEB-INF/security.xml",
        "classpath:/resources/testApplicationContext.xml",
        "classpath:/resources/datasource-config.xml"
})

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public @interface DatabaseTest {
}
