package com.neurocom.crud;

import com.neurocom.crud.config.RestDataConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by c.karalis on 4/23/2015.
 */
public class CustomConfigDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { RestDataConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/rest/*" };
    }

}
