package com.bjpowernode.test;

import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.service.ProductTypeService;
import com.bjpowernode.utils.MD5Util;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import java.util.Arrays;
import java.util.List;

public class MyTest {

    @Autowired
    ProductInfoService productInfoService;
    @Test
    public void testMd5(){
        String mi= MD5Util.getMD5("000000");
        System.out.println(mi);
    }

    @Test
    public void contextInitialized() {
        ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext_service.xml","applicationContext_dao.xml");
        ProductTypeService productInfoServiceImpl = (ProductTypeService) context.getBean("ProductTypeServiceImpl");
        List<ProductType> list = productInfoServiceImpl.getAll();
        System.out.println(list.get(0).getTypeId());

    }

    @Test
    public void TestStr(){

        String str="15,16,17";
        String[] pids=str.split(",");
        for (int i=0;i<pids.length;i++)
            System.out.println(pids[i]);
        int num=-1;
        try {
            num= productInfoService.deleteBatch(pids);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
