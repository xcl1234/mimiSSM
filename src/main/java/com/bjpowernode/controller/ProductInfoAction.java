package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    private static final int PAGE_SIZE=5;
    private String saveFileName=" ";
    @Autowired
    ProductInfoService productInfoService;

    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list",list);
        return "product";
    }

    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo pageInfo = productInfoService.splitPage(1, PAGE_SIZE);
        request.setAttribute("info",pageInfo);
        return "product";
    }

    @ResponseBody
    @RequestMapping("/ajaxsplit")
    public void ajaxSplit(int page, HttpSession session){
        PageInfo pageInfo = productInfoService.splitPage(page, PAGE_SIZE);
        session.setAttribute("info",pageInfo);
    }

    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        saveFileName= FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());
        String path=request.getServletContext().getRealPath("/image_big");
        try {
            pimage.transferTo(new File(path+File.separator+saveFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject object=new JSONObject();
        object.put("imgurl",saveFileName);
        return object.toString();

    }

    @RequestMapping("/save")
    public String save(ProductInfo info, HttpServletRequest request){
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        info.setTypeId(1);
        int num=-1;
        try {
            num= productInfoService.save(info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (num>0){
            request.setAttribute("msg","增加成功！");
        }else {
            request.setAttribute("msg","增加失败！");
        }
        saveFileName="";
        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid, Model model){
        ProductInfo info = productInfoService.getByID(pid);
        model.addAttribute("prod",info);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        if(!saveFileName.equals("")){
            info.setpImage(saveFileName);
        }
        int num=-1;
        try {
            num= productInfoService.update(info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (num>0){
            request.setAttribute("msg","更新成功！");
        }else {
            request.setAttribute("msg","更新失败!");
        }
        return "forward:/prod/split.action";

    }

    @RequestMapping("/delete")
    public String delete(int pid,HttpServletRequest request){
        int num=-1;
        try {
            num= productInfoService.delete(pid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (num>0){
            request.setAttribute("msg","删除成功！");
        }else {
            request.setAttribute("msg","删除失败!");
        }
        return "forward:/prod/split.action";
    }

    @RequestMapping("/deletebatch")
    public String deletebatch(String str,HttpServletRequest request){
        String[] pids=str.split(",");
        System.out.println(pids);
        int num=-1;
        try {
            num= productInfoService.deleteBatch(pids);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (num>0){
            request.setAttribute("msg","删除成功！");
        }else {
            request.setAttribute("msg","删除失败!");
        }
        return "forward:/prod/split.action";
    }



}
