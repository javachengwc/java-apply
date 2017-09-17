package com.other.file;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.LinkProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class PdfUtil {

    public static ByteArrayInputStream html2Pdf(String html,String cssClassPath) throws Exception {

        byte[] bs = html.toString().getBytes("utf-8");
        InputStream input = new ByteArrayInputStream(bs);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        document.setMargins(0, 0, 0, 0);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        //initialized pdf xml worker config for iText
        //FontFactory.registerDirectories();
        //FontFactory.getFont("STSong-Light", "UniGB-UCS2-H");

        XMLWorkerFontProvider xMLWorkerFontProvider = new XMLWorkerFontProvider("");
        CssAppliers cssAppliers = new CssAppliersImpl(xMLWorkerFontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setCssAppliers(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.setImageProvider(new AbstractImageProvider() {
            public String getImageRootPath() {
                return null;
            }
        });
        htmlContext.setLinkProvider(new LinkProvider() {
            public String getLinkRoot() {
                return null;
            }
        });

        CSSResolver cssResolver = new StyleAttrCSSResolver();
        InputStream cssStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(cssClassPath);
        CssFile cssFile = XMLWorkerHelper.getCSS(cssStream);
        cssResolver.addCss(cssFile);

        // pipeline
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline pipeline = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, pipeline);

        //xml worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(input, Charset.forName("UTF-8"));
        document.close();

        byte[] pdfBytes = out.toByteArray();
        ByteArrayInputStream rtStream = new ByteArrayInputStream(pdfBytes);
        return rtStream;
    }
}
