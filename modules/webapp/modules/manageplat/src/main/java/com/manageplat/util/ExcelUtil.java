package com.manageplat.util;

import net.sf.jxls.transformer.Configuration;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * @param title        标题行 例：String[]{"名称","地址"}
     * @param key          从查询结果List取得的MAP的KEY顺序，需要和title顺序匹配，例：String[]{"name","address"}
     * @param values       结果集
     * @param outputStream
     * @throws java.io.IOException
     */
    public static void download(String[] title, String[] key, List<Map> values, OutputStream outputStream) throws IOException {

        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = null;
        HSSFCell cell = null;
        row = sheet.createRow((short) 0);
        for (int i = 0; title != null && i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(title[i]));
        }
        Map map = null;
        for (int i = 0; values != null && i < values.size(); i++) {
            row = sheet.createRow((short) (i + 1));
            map = values.get(i);
            for (int j = 0; j < key.length; j++) {
                cell = row.createCell(j);
                if (map.get(key[j]) == null) {
                    cell.setCellValue(new HSSFRichTextString(""));
                } else {
                    try {
                        setCellValue(workbook, sheet, cell, map.get(key[j]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        workbook.write(outputStream);
        outputStream.flush();
    }

    public static File generateXls(String[] title, String[] key, List<Map> values, String filepath) throws IOException {
        File xlsfile = new File(filepath);
        File path = new File(xlsfile.getParent());
        if(!path.exists()){
            if(!path.mkdirs()){
                return null;
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(xlsfile);
        try {

            download(title, key, values, fileOutputStream);

        }finally
        {
            if(fileOutputStream!=null)
            {
                fileOutputStream.close();
            }
        }
        return xlsfile;
    }

    /**
     * @param title        标题行 例：String[]{"名称","地址"}
     * @param key          从查询结果List取得的MAP的KEY顺序，需要和title顺序匹配，例：String[]{"name","address"}
     * @param values       结果集
     * @param outputStream
     * @throws java.io.IOException
     */
    public static void downloadCsv(String[] title, String[] key, List<Map> values, OutputStream outputStream) throws IOException {

        for (String head : title) {
            outputStream.write((head + ",").getBytes());
        }

        outputStream.write(("\r\n").getBytes());

        Map map = null;
        for (int i = 0; values != null && i < values.size(); i++) {
            map = values.get(i);
            for (int j = 0; j < key.length; j++) {
                outputStream.write((map.get(key[j]) + ",").getBytes());
            }
            outputStream.write(("\r\n").getBytes());
        }
        outputStream.flush();
    }

    public static void setCellValue(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCell cell, Object obj) throws Exception {
        String type = obj.getClass().getName();
        if (type.endsWith("String")) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue((String) obj);
        } else if (("int".equals(type)) || (type.equals("java.lang.Integer"))) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Integer) obj).intValue());
        } else if (("double".equals(type)) || (type.equals("java.lang.Double"))) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            BigDecimal bigDecimal = new BigDecimal((Double) obj, MathContext.DECIMAL32);
            cell.setCellValue(bigDecimal.doubleValue());
        } else if (("boolean".equals(type)) || (type.equals("java.lang.Boolean"))) {
            cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
            cell.setCellValue(((Boolean) obj));
        } else if (("float".equals(type)) || (type.equals("java.lang.Float"))) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            BigDecimal bigDecimal = new BigDecimal((Float) obj, MathContext.DECIMAL32);
            cell.setCellValue(bigDecimal.doubleValue());
        } else if (("char".equals(type)) || (type.equals("java.lang.Character"))) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(obj.toString());
        } else if (("long".equals(type)) || (type.equals("java.lang.Long"))) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Long) obj).longValue());
        } else if (("short".equals(type)) || (type.equals("java.lang.Short"))) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Short) obj).shortValue());
        } else if (type.equals("java.math.BigDecimal")) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            BigDecimal bigDecimal = (BigDecimal) obj;
            bigDecimal = new BigDecimal(bigDecimal.doubleValue(), MathContext.DECIMAL32);
            cell.setCellValue(bigDecimal.doubleValue());
        } else if ((type.equals("java.util.Date")) || (type.endsWith("Date"))) {
            HSSFCellStyle cellStyle = workbook.createCellStyle();

            short df = workbook.createDataFormat().getFormat("yyyy/m/d h:mm:ss");
            cellStyle.setDataFormat(df);
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(cell.getColumnIndex(), 18 * 256);
            cell.setCellValue(org.apache.poi.ss.usermodel.DateUtil.getExcelDate((Date) obj));
        } else {
            throw new Exception("data type errored!");
        }
    }
	
	public static void buildExcelByTemplate(String template, Map data, OutputStream outputStream) {
        Workbook workbook = null;
        try {
            workbook = getWorkBook(template);
        } catch (InvalidFormatException e) {
           // LOGGER.warn("template format error", e);
        	System.out.println("template format error\r\n");
        	e.printStackTrace(System.out);
        } catch (IOException e) {
           // LOGGER.warn("template io error", e);
        	System.out.println("template io error\r\n");
        	e.printStackTrace(System.out);
        }
        if (null == workbook) {
            return;
        }
        //使用 jxls 导出excel，导出的字段内容中有一个字段内容是 url 地址（http://.....），导出发现，
        // url地址被截断了：只剩下 http: 了，后面的网站没有了
        //jxls的官方论坛上找到了答案：原来 // 是jxls 内置的 metainfotoken符号
        //下面2句代码是正解，能处理此问题
        Configuration config = new Configuration();
        config.setMetaInfoToken("\\\\");
        XLSTransformer transformer = new XLSTransformer(config);

        transformer.transformWorkbook(workbook, data);
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
           // LOGGER.warn("out excel io error", e);
            System.out.println("out excel io error\r\n");
        	e.printStackTrace(System.out);
        }
    }

    public static Workbook getWorkBook(String template) throws IOException, InvalidFormatException {
        URL url = ExcelUtil.class.getResource("/" + template);
        if (null == url) {
            //LOGGER.info("not found excel template");
            System.out.println("not found excel template\r\n");
            return null;
        }
        File templateFile = new File(url.getPath());
        if (!templateFile.exists()) {
           // LOGGER.info("jxls  template not exist:" + templateFile.getPath());
            
            System.out.println("jxls  template not exist:" + templateFile.getPath());
            return null;
        }
        InputStream is = new BufferedInputStream(new FileInputStream(templateFile));
        Workbook wb;
        try {
            wb = WorkbookFactory.create(is);
        } catch (InvalidFormatException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return wb;
    }


}
