package com.sap;

import org.springframework.web.servlet.view.InternalResourceViewResolver;

public final class TestViewResolver {

        private TestViewResolver(){}

        public static InternalResourceViewResolver viewResolver() {
            InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
            viewResolver.setPrefix("/WEB-INF/views/");
            viewResolver.setSuffix(".jsp");
            return viewResolver;
        }
}

