package com.excel;

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
import java.util.*;

/**
  *HSSFWorkbook:是操作Excel2003以前（包括2003）的版本，扩展名是.xls
  *XSSFWorkbook:是操作Excel2007的版本，扩展名是.xlsx
  *对于不同版本的EXCEL文档要使用不同的工具类，如果使用错了，会报错
  *
  * sheet.createRow()参数如果是short会限制excel的行数最多3万多行，
  * 所以最好用int参数来创建excel的行对象
  */
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
        row = sheet.createRow((int) 0);
        for (int i = 0; title != null && i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(title[i]));
        }
        Map map = null;
        for (int i = 0; values != null && i < values.size(); i++) {
            row = sheet.createRow((int) (i + 1));
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

    public static <K,V> File generate(String[] title, String[] key, List<Map<K,V>> values, String filepath) throws IOException {

        logger.info("-------------generate start----------");

        File csvfile = new File(filepath);
        File path = new File(csvfile.getParent());
        if(!path.exists()){
            if(!path.mkdirs()){
                return null;
            }
        }
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream = new FileOutputStream(csvfile);
            HSSFWorkbook workbook = null;
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row = null;
            HSSFCell cell = null;
            row = sheet.createRow((int) 0);
            for (int i = 0; title != null && i < title.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(new HSSFRichTextString(title[i]));
            }
            logger.info("-------------generate invoke data----------");
            Map<K,V> map = null;
            for (int i = 0; values != null && i < values.size(); i++) {
                if(i%2000==0)
                {
                    logger.info("----------setCellValue count"+i);
                }
                row = sheet.createRow((int) (i + 1));
                map = values.get(i);
                for (int j = 0; j < key.length; j++) {
                    cell = row.createCell(j);

                    try {
                        if (map.get(key[j]) == null) {
                            cell.setCellValue(new HSSFRichTextString(""));
                        } else {

                            setCellValue(workbook, sheet, cell, map.get(key[j]));
                        }
                    }catch (Exception e) {
                        cell.setCellValue(new HSSFRichTextString(""));
                        logger.error("generate is error,",e);
                    }
                }
            }
            logger.info("-------------generate invoke data end ----------");

            //当数据量过大时候，此处很容易导致oom,
            //一般2万数据量，通过poi生成excel需要内存开到768m
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            logger.info("-------------workbook write to file end----------");
        }catch (Exception e){
            logger.error("generate is error,",e);
        }finally {
            logger.info("-------------generate finally----------");
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }

        logger.info("-------------generate end----------");

        return csvfile;
    }

    public static <K,V> File generate1(String[] title, String[] key, List<Map<K,V>> values, String filepath) throws IOException {

        logger.info("-------------generate1 start----------");

        File csvfile = new File(filepath);
        File path = new File(csvfile.getParent());
        if(!path.exists()){
            if(!path.mkdirs()){
                return null;
            }
        }
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream = new FileOutputStream(csvfile);
            HSSFWorkbook workbook = null;
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row = null;
            HSSFCell cell = null;
            row = sheet.createRow((int) 0);
            for (int i = 0; title != null && i < title.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(new HSSFRichTextString(title[i]));
            }
            logger.info("-------------generate1 invoke data----------");
            Map<K,V> map = null;
            for (int i = 0; values != null && i < values.size(); i++) {
                if(i%2000==0)
                {
                    logger.info("----------setCellValue count"+i);
                }
                row = sheet.createRow((int) (i + 1));
                map = values.get(i);
                for (int j = 0; j < key.length; j++) {
                    cell = row.createCell(j);

                    try {
                        if (map.get(key[j]) == null) {
                            cell.setCellValue(new HSSFRichTextString(""));
                        } else {

                            setCellValue(workbook, sheet, cell, map.get(key[j]));
                        }
                    }catch (Exception e) {
                        cell.setCellValue(new HSSFRichTextString(""));
                        logger.error("generate is error,",e);
                    }
                }
            }
            logger.info("-------------generate1 invoke data end ----------");
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            logger.info("-------------workbook write to file end----------");
        }catch (Exception e){
            logger.error("generate is error,",e);
        }finally {
            logger.info("-------------generate1 finally----------");
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }

        logger.info("-------------generate1 end----------");

        return csvfile;
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

    public static void download(List<MakeExcelUnit> list, OutputStream outputStream) throws IOException {

        if(list==null)
        {
            return;
        }
        if(!LinkedList.class.isAssignableFrom(list.getClass()))
        {
            Collections.sort(list, new Comparator<MakeExcelUnit>() {
                public int compare(MakeExcelUnit unit1, MakeExcelUnit unit2) {
                    int s1 = unit1.getSheetSort();
                    int s2 = unit2.getSheetSort();
                    if (s1 < s2) {
                        return -1;
                    }
                    if (s1 > s2) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        HSSFWorkbook workbook =new HSSFWorkbook();
        for(MakeExcelUnit unit:list) {
            HSSFSheet sheet = workbook.createSheet(unit.getSheetname());
            String [] title = unit.getFidleNames();
            String [] key =unit.getKeys();
            List<Map> values =unit.getDataList();
            HSSFRow row = null;
            HSSFCell cell = null;
            //title
            row = sheet.createRow((int) 0);
            for (int i = 0; title != null && i < title.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(new HSSFRichTextString(title[i]));
            }
            if(values!=null) {
                for (int i = 0; values != null && i < values.size(); i++) {
                    row = sheet.createRow((int) (i + 1));
                    Map map = values.get(i);
                    for (int j = 0; j < key.length; j++) {
                        cell = row.createCell(j);
                        if (map.get(key[j]) == null) {
                            cell.setCellValue(new HSSFRichTextString(""));
                        } else {
                            try {
                                setCellValue(workbook, sheet, cell, map.get(key[j]));
                            } catch (Exception e) {
                                System.out.println("download  mult sheet exception");
                            }
                        }
                    }
                }
            }
        }
        workbook.write(outputStream);
        outputStream.flush();
    }

    public static class MakeExcelUnit
    {
        private List<Map> dataList;

        private String[] fidleNames;

        private String[] keys;

        private String sheetname;

        private int sheetSort;

        private String fileName;

        public MakeExcelUnit()
        {

        }

        public MakeExcelUnit(List<Map> dataList,String[] fidleNames,String keys[],String fileName)
        {
            this.dataList=dataList;
            this.fidleNames=fidleNames;
            this.keys=keys;
            this.fileName=fileName;
        }

        public List<Map> getDataList() {
            return dataList;
        }

        public void setDataList(List<Map> dataList) {
            this.dataList = dataList;
        }

        public String[] getFidleNames() {
            return fidleNames;
        }

        public void setFidleNames(String[] fidleNames) {
            this.fidleNames = fidleNames;
        }

        public String[] getKeys() {
            return keys;
        }

        public void setKeys(String[] keys) {
            this.keys = keys;
        }

        public String getSheetname() {
            return sheetname;
        }

        public void setSheetname(String sheetname) {
            this.sheetname = sheetname;
        }

        public int getSheetSort() {
            return sheetSort;
        }

        public void setSheetSort(int sheetSort) {
            this.sheetSort = sheetSort;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
