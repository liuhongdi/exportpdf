package com.exportpdf.demo.controller;

import com.exportpdf.demo.mapper.GoodsMapper;
import com.exportpdf.demo.pojo.Goods;
import com.exportpdf.demo.service.PdfTableService;
import com.exportpdf.demo.util.PdfUtil;
import com.exportpdf.demo.util.ViewPdfUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    PdfTableService pdfTableService;

    //把数据保存到pdf文件
    @GetMapping("/savepdf")
    @ResponseBody
    public String savepdf() {
        List<Goods> goodsList = goodsMapper.selectAllGoods();
        String savePath = "/data/springboot2/goodslist.pdf";
        pdfTableService.createPDF(new Document(PageSize.A4), goodsList,savePath);
        return "pdf saveed";
    }

    //从浏览器直接显示pdf
    @GetMapping("/viewpdf")
    public ModelAndView viewpdf() {
        List<Goods> goodsList = goodsMapper.selectAllGoods();
        Map<String, Object> model = new HashMap<>();
        model.put("sheet", goodsList);
        ViewPdfUtil viewPdf = new ViewPdfUtil();
        viewPdf.setFileName("测试.pdf");
        viewPdf.setPdfType("goods");
        return new ModelAndView(viewPdf, model);
    }

    //下载pdf文件
    @GetMapping("/downpdf")
    public void downpdf() {
          String filepath = "/data/springboot2/hello.pdf";
          PdfUtil.downPdfFile(filepath);
     }
}