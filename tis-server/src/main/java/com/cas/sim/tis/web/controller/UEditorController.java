package com.cas.sim.tis.web.controller;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@Transactional
@RequestMapping("ueditor")
public class UEditorController {
	@Value("${ueditor.upload.image.url}")
	private String imgPath;
	
    /**
	 * ueditor配置
	 */
	@RequestMapping("ueconfig")
	public void getUEConfig(HttpServletRequest request, HttpServletResponse response){
	   org.springframework.core.io.Resource res = new ClassPathResource("config.json");
	   InputStream is = null;
	   response.setHeader("Content-Type" , "text/html");
	   try {
	      is = new BufferedInputStream(res.getInputStream());
	      StringBuffer sb = new StringBuffer();
	      byte[] b = new byte[1024];
	      int length=0;
	      while(-1!=(length=is.read(b))){
	         sb.append(new String(b,0,length,"utf-8"));
	      }
	      String result = sb.toString().replaceAll("/\\*(.|[\\r\\n])*?\\*/","");
	      JSONObject json = JSON.parseObject(result);
	      PrintWriter out = response.getWriter();
	      out.print(json.toString());
	   } catch (IOException e) {
	      e.printStackTrace();
	   }finally {
	      try {
	         is.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
	}
	
	/**
	 * 保存图片
	 * @param file
	 * @return
	 * @author 倪金涛
	 * @date:   2018年3月27日 上午11:26:57
	 */
	@RequestMapping(value = "/imgUpdate")
	@ResponseBody
	public Object imgUpdate(MultipartFile file) {
	    if (file.isEmpty()) {
	        return "error";
	    }
	    // 获取文件名
	    String fileName = file.getOriginalFilename();
	    // 获取文件的后缀名
	    String suffixName = fileName.substring(fileName.lastIndexOf("."));
	    // 这里我使用随机字符串来重新命名图片
	    fileName = Calendar.getInstance().getTimeInMillis() + suffixName;
	    // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
	    File dest = new File(imgPath + fileName);
	    // 检测是否存在目录
	    if (!dest.getParentFile().exists()) {
	        dest.getParentFile().mkdirs();
	    }
	    try {
	        file.transferTo(dest);
	        //url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
	        String config = "{\"state\": \"SUCCESS\"," +
	                "\"url\": \"" + "" + fileName + "\"," +
	                "\"title\": \"" + fileName + "\"," +
	                "\"original\": \"" + fileName + "\"}";
	        JSONObject jobj = JSONObject.parseObject(config);
	        return jobj;
	    } catch (IllegalStateException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return "error";
	}
	
	/**
	 * 显示图片
	 * @param imgUrl
	 * @return
	 * @author 倪金涛
	 * @throws Exception 
	 * @date:   2018年3月27日 上午11:26:49
	 */
	@RequestMapping(value = "/showImg")
	@ResponseBody
	public void showImg(HttpServletResponse response, @ModelAttribute("imgUrl") String imgUrl) throws Exception {
        if (imgUrl != null && imgUrl != "") {
            // 不使用
        	String url = imgPath + imgUrl;
        	OutputStream os = response.getOutputStream();
            File file = new File(url);
            FileInputStream fips = new FileInputStream(file);
            ByteArrayOutputStream bops = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024*2];
            int len ;
			while ((len = fips.read(bytes)) != -1) {
				bops.write(bytes, 0 , len);
			}
            byte[] btImg = bops.toByteArray();
            os.write(btImg);
            os.flush();
        }
	}
}
