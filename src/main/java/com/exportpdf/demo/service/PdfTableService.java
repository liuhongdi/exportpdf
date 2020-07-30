package com.exportpdf.demo.service;

import com.exportpdf.demo.pojo.Goods;
import com.itextpdf.text.Document;

import java.util.List;

public interface PdfTableService {
    public void createPDF(Document document,List<Goods> products,String savePath);
}
