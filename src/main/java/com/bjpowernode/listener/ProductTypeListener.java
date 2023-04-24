package com.bjpowernode.listener;

import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

class ProductTypeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext_service.xml","applicationContext_dao.xml");
        ProductTypeService productInfoServiceImpl = (ProductTypeService) context.getBean("ProductTypeServiceImpl");
        List<ProductType> list = productInfoServiceImpl.getAll();
        servletContextEvent.getServletContext().setAttribute("typeList",list);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
