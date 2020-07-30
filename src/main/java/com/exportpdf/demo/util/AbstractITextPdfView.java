package com.exportpdf.demo.util;

import com.itextpdf.text.Document;
        import com.itextpdf.text.DocumentException;
        import com.itextpdf.text.PageSize;
        import com.itextpdf.text.pdf.PdfWriter;
        import org.springframework.web.servlet.view.AbstractView;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.ByteArrayOutputStream;
        import java.io.OutputStream;
        import java.util.Map;

/**
新建一个pdfview,主要是为了避免AbstractPdfView中使用的pdf库太旧的问题
 AbstractPdfView只支持到 com.lowagie.itext的2.1.7版本，
 版本太旧，文档也缺少
 修改后可以支持itextpdf库的类，
 新增AbstractITextPdfView后此问题完美解决
 by liuhongdi
*/
public abstract class AbstractITextPdfView extends AbstractView {

    public AbstractITextPdfView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model,
                                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 获得流
        ByteArrayOutputStream baos = createTemporaryOutputStream();
        Document document = newDocument();
        PdfWriter writer = newWriter(document, baos);
        prepareWriter(model, writer, request);
        buildPdfMetadata(model, document, request);
        buildPdfDocument(model, document, writer, request, response);
        writeToResponse(response, baos);
    }

    protected Document newDocument() {
        return new Document(PageSize.A4);
    }

    protected PdfWriter newWriter(Document document, OutputStream os)
            throws DocumentException {
        return PdfWriter.getInstance(document, os);
    }

    protected void prepareWriter(Map<String, Object> model, PdfWriter writer,
                                 HttpServletRequest request) throws DocumentException {

        writer.setViewerPreferences(getViewerPreferences());
    }

    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }

    protected void buildPdfMetadata(Map<String, Object> model,
                                    Document document, HttpServletRequest request) {
    }

    protected abstract void buildPdfDocument(Map<String, Object> model,
                                             Document document, PdfWriter writer, HttpServletRequest request,
                                             HttpServletResponse response) throws Exception;
}