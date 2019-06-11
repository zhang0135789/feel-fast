/**
 * Copyright (C) 2013 北京通宝科技有限公司
 *
 * @version:v1.0.0
 * @author: Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2013-6-25                              v1.0.0        create
 */
package com.feel.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public final class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = context;
        }
    }

    public static ApplicationContext getAppContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getAppContext().getBean(name);
    }
}
