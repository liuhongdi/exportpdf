package com.exportpdf.demo.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class PdfUtil {
     //read  a exist pdf file,not download
     public static void readPdfFile(String filePath) {
         ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
         HttpServletResponse response = servletRequestAttributes.getResponse();
         File f = new File(filePath);
         if (!f.exists()) {
             return;
         }
         try {
             BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
             byte[] bs = new byte[1024];
             int len = 0;
             response.reset();
             response.setContentType("application/pdf");//
             response.setHeader("Content-Disposition", "inline;filename="
                     + URLEncoder.encode(f.getName(), "UTF-8"));
             // 文件名应该编码成utf-8
             OutputStream out = response.getOutputStream();
             while ((len = br.read(bs)) > 0) {
                 out.write(bs, 0, len);
             }
             out.flush();
             out.close();
             br.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

    //download a pdf file
    public static void downPdfFile(String filePath) {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        File f = new File(filePath);
        if (!f.exists()) {
            return;
        }
        try {
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
            byte[] bs = new byte[1024];
            int len = 0;
            response.reset();
                // 纯下载方式
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(f.getName(), "UTF-8"));
            OutputStream out = response.getOutputStream();
            while ((len = br.read(bs)) > 0) {
                out.write(bs, 0, len);
            }
            out.flush();
            out.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
