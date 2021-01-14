package com.example.webloader.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author liyiruo
 * @Description
 * @Date 2021/1/8 下午7:10
 */

@Slf4j
@RestController
@RequestMapping("/filecontroller")
public class WebLoaderController {
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile files, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=new JSONObject();
        response.setCharacterEncoding("utf-8");
        String msg = "添加成功";
        long start = System.currentTimeMillis();
        log.info("-------------------开始调用上传文件upload接口----{}---------------",System.currentTimeMillis());
        try{
            String name = files.getOriginalFilename();
            log.info("name==>{}",name);
            //String path = this.getClass().getClassLoader().getResource("/").getPath();
            ClassLoader loader = this.getClass().getClassLoader();
            log.info("loader=>{}",loader);

            URL resource = loader.getResource("");//""里不能有东西啊

            log.info("resource.toString()==>{}",resource.toString());

           // String path1 = resource.getPath();
            String path = loader.getResource("").getPath();


            log.info("path=====>{}",path);


            path = path + File.separator + name;
            File uploadFile = new File(path);
            files.transferTo(uploadFile);
            //files.transferTo(new File(name));

            log.info("path==>{}",path);

            log.info("耗时=====>{}",System.currentTimeMillis()-start);
        }catch(Exception e){
            e.printStackTrace();
            msg="添加失败";
        }
        log.info("-------------------结束调用上传文件upload接口-------------------");
        json.put("msg", msg);
        log.info("msg===>{}",msg);
        return json.toString();
    }

    @RequestMapping(value = "/uploadservlet", method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    protected String uploadServlet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json=new JSONObject();
        response.setCharacterEncoding("utf-8");
        String msg = "添加成功";
        log.info("-------------------开始调用上传文件uploadservlet接口-------------------");
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
                List<MultipartFile> multipartFile = mr.getFiles("myfile");
                if (null != multipartFile && !multipartFile.isEmpty()) {
                    MultipartFile file = multipartFile.get(0);
                    String name = file.getOriginalFilename();
                    String path = this.getClass().getClassLoader().getResource("/").getPath();
                    int index = path.indexOf("Shopping");
                    path = path.substring(0, index + "Shopping".length()) + "/WebContent/resources/upload/";
                    path = path + File.separator + name;
                    System.out.println("path==>"+path);
                    File uploadFile = new File(path);
                    if(FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile))>0)
                    {
                        json.put("path",path);
                    }

                }
            }
        } catch (Exception e) {
            msg = "上传失败";
        }
        log.info("-------------------结束调用上传文件uploadservlet接口-------------------");
        json.put("msg", msg);
        return json.toString();
    }
}
