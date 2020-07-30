package com.exportpdf.demo.service.impl;

import com.exportpdf.demo.pojo.Goods;
import com.exportpdf.demo.service.PdfTableService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfTableServiceImpl implements PdfTableService {

    //创建pdf文件，
    // savePath是保存路径，如果是空字串，则直接输出到document
    //document:pdf内容
    //goods:写入到pdf表格中的数据
    @Override
    public void createPDF(Document document, List<Goods> goods,String savePath)  {
        try {
            if (!savePath.equals("")) {
                PdfWriter.getInstance(document, new FileOutputStream(savePath));
            }
            document.addTitle("商品库存统计表");
            document.addAuthor("老刘");
            document.addSubject("2020年商品库存统计");
            document.addKeywords("商品库存");
            document.open();
            Paragraph para = getParagraphText("整个白酒行业从2012年开始，都迅速下滑，销量和利润都是大跌。2014年和2015年，茅台的股价涨得不错，但也没有超过同期的白马股太多，加上利润增速一直没有恢复塑化剂之前的状态，我就一直没有再买入");
            document.add(para);
            String imagePath = "/data/springboot2/logo.jpg";      // 图片的绝对路径
            Image image = Image.getInstance(imagePath);       // 取得图片对象
            //计算得到目标宽高
            File gifFile = new File(imagePath);
            int origWidth = 0;
            int origHeight = 0;
            try {
                BufferedImage imageBuffer = ImageIO.read(gifFile);
                if (imageBuffer != null) {//如果image=null 表示上传的不是图片格式
                    origWidth = imageBuffer.getWidth();
                    origHeight = imageBuffer.getHeight();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("width:"+document.getPageSize().getWidth());
            System.out.println("margin:"+document.leftMargin());
            //得到新的高度和新的宽度
            float newwidth = document.getPageSize().getWidth()-document.leftMargin()-document.rightMargin();
            float newHeight = (newwidth*origHeight) / origWidth;

            image.scaleAbsolute(newwidth, newHeight);

            document.add(image);

            PdfPTable table = createTable(goods);
            document.add(table);

        } catch ( IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    //从text得到可以添加到document的Paragraph
    public static Paragraph getParagraphText(String text)  {

        try {
            Font font = new Font(BaseFont.createFont(new ClassPathResource("/font/FZLTHK.TTF").getPath(),BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED));
            font.setColor(BaseColor.GRAY);

            Paragraph para = new Paragraph(text,font);
            return para;
        } catch ( IOException | DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    //创建PdfTable
    public static PdfPTable createTable(List<Goods> products) throws IOException, DocumentException {
        PdfPTable table = new PdfPTable(4);//生成一个4列的表格

        int widths[] = { 10,40,40,10 };//指定各列的宽度百分比
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(widths);

        PdfPCell cell;
        int size = 20;

        Font font = new Font(BaseFont.createFont(new ClassPathResource("/font/FZLTHK.TTF").getPath(),BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED));
        font.setColor(BaseColor.BLACK);

        Font font_head = new Font(BaseFont.createFont(new ClassPathResource("/font/FZLTHK.TTF").getPath(),BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED));
        font_head.setColor(BaseColor.BLUE);

        Font font_title = new Font(BaseFont.createFont(new ClassPathResource("/font/FZLTHK.TTF").getPath(),BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED));
        font_title.setColor(BaseColor.GREEN);
        font_title.setSize(36);

        cell = new PdfPCell(new Phrase("商品库存信息表",font_title));
        cell.setColspan(4);//设置所占列数
        cell.setFixedHeight(50);//设置高度
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("ID",font_head));//商品编号
        cell.setFixedHeight(size);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("商品名称",font_head));//商品名称
        cell.setFixedHeight(size);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("描述",font_head));//描述
        cell.setFixedHeight(size);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("价格",font_head));//商品价格
        cell.setFixedHeight(size);
        table.addCell(cell);

        for(int i = 0;i<products.size();i++) {
            cell = new PdfPCell(new Phrase(String.valueOf(products.get(i).getGoodsId()),font));//商品编号
            cell.setFixedHeight(size);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(products.get(i).getGoodsName(),font));//商品名称
            cell.setFixedHeight(size);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(products.get(i).getSubject(),font));//描述
            cell.setFixedHeight(size);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(products.get(i).getPrice()+"",font));//商品价格
            cell.setFixedHeight(size);
            table.addCell(cell);
        }
        return table;
    }

}
