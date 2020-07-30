package com.exportpdf.demo.util;

import com.exportpdf.demo.pojo.Goods;
import com.exportpdf.demo.service.PdfTableService;
import com.exportpdf.demo.service.impl.PdfTableServiceImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class ViewPdfUtil extends AbstractITextPdfView {

    //文件名
    private String fileName;
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //指定一个类型，方便知道调用哪个类处理
    private String pdfType;
    public String getPdfType() {
        return this.pdfType;
    }
    public void setPdfType(String pdfType) {
        this.pdfType = pdfType;
    }

    //生成pdf的document并显示出来
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf");

            response.setHeader("Content-Disposition","filename=" + URLEncoder.encode(this.fileName, "UTF-8"));
            List<Goods> products = (List<Goods>) model.get("sheet");
            if (this.pdfType.equals("goods")) {
                PdfTableService pdfTableService = new PdfTableServiceImpl();
                //不保存成文件，直接显示，所以不指定保存路径
                pdfTableService.createPDF(document, products,"");
            }
    }
}
