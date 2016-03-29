package com.excel;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * excel操作工具
 */
public class JxlUtil {
    private static final int EXCEL_SHEET_MAX_ROW_SIZE = 65535;

    /**
     * 输出内容到流中
     *
     * @param os
     * @param fieldName
     * @param data
     * @throws java.io.IOException
     * @throws jxl.write.WriteException
     */
    public static void makeExcelWorkBook(OutputStream os,
                                         List<String> fieldName, List<List<String>> data)
            throws IOException, WriteException {
        makeExcelWorkBook(os, null, fieldName, data);
    }

    /**
     * 输出内容到流中
     *
     * @param os
     * @param sheetName
     * @param fieldName
     * @param data
     * @throws java.io.IOException
     * @throws jxl.write.WriteException
     */
    public static void makeExcelWorkBook(OutputStream os, String sheetName,
                                         List<String> fieldName, List<List<String>> data)
            throws IOException, WriteException {
        sheetName = sheetName == null ? "sheet" : sheetName;

        // 文件对象
        WritableWorkbook wwb = Workbook.createWorkbook(os);

        int sheetNo = 0;
        // 工作簿对象
        WritableSheet sheet = null;
        for (int i = 0; i < data.size(); i++) {
            // int sheetSize = sheet.getRows();
            int r = i % EXCEL_SHEET_MAX_ROW_SIZE;
            if (r == 0) {
                sheet = wwb.createSheet(sheetName + " - " + sheetNo, sheetNo);
                sheetNo++;
                // 写表头
                writeExcelRow(fieldName, sheet, 0);
            }

            // 写数据
            writeExcelRow(data.get(i), sheet, r + 1);
        }

        // 没有意义但是 但是不写wwb.write()就会报错
        if (data.size() == 0) {
            sheet = wwb.createSheet(sheetName + " - " + sheetNo, sheetNo);
            writeExcelRow(fieldName, sheet, 0);
        }
        wwb.write();
        wwb.close();
    }

    /**
     * 输出内容到流中
     *
     * @param os
     * @param sheetName
     * @param fieldName
     * @param data
     * @throws java.io.IOException
     * @throws jxl.write.WriteException
     */
    public static void makeExcelWorkBook(OutputStream os, String sheetName,
                                         String[] fieldName, List<String[]> data)
            throws IOException, WriteException {
        sheetName = sheetName == null ? "sheet" : sheetName;

        // 文件对象
        WritableWorkbook wwb = Workbook.createWorkbook(os);

        int sheetNo = 0;
        // 工作簿对象
        WritableSheet sheet = null;
        for (int i = 0; i < data.size(); i++) {
            // int sheetSize = sheet.getRows();
            int r = i % EXCEL_SHEET_MAX_ROW_SIZE;
            if (r == 0) {
                sheet = wwb.createSheet(sheetName + " - " + sheetNo, sheetNo);
                sheetNo++;
                // 写表头
                writeExcelRow(fieldName, sheet, 0);
            }

            // 写数据
            writeExcelRow(data.get(i), sheet, r + 1);
        }

        // 没有意义但是 但是不写wwb.write()就会报错
        if (data.size() == 0) {
            sheet = wwb.createSheet(sheetName + " - " + sheetNo, sheetNo);
            writeExcelRow(fieldName, sheet, 0);
        }
        wwb.write();
        wwb.close();
    }

    public static void makeExcelWorkBookToSheet(OutputStream os,
                                         String[] fieldName, Map<String, List<String[]>> dataMap)
            throws IOException, WriteException {
        String sheetName;
        // 文件对象
        WritableWorkbook wwb = Workbook.createWorkbook(os);

        int sheetNo = 0;
        // 工作簿对象
        WritableSheet sheet = null;
        for (Map.Entry<String, List<String[]>> sheetData : dataMap.entrySet()){
            sheetName = sheetData.getKey();
            List<String[]> data = sheetData.getValue();

            for (int i = 0; i < data.size(); i++) {
                // int sheetSize = sheet.getRows();
                int r = i % EXCEL_SHEET_MAX_ROW_SIZE;
                if (r == 0) {
                    sheet = wwb.createSheet(sheetName, sheetNo);
                    sheetNo++;
                    // 写表头
                    writeExcelRow(fieldName, sheet, 0);
                }

                // 写数据
                writeExcelRow(data.get(i), sheet, r + 1);
            }

            // 没有意义但是 但是不写wwb.write()就会报错
            if (data.size() == 0) {
                sheet = wwb.createSheet(sheetName + " - " + sheetNo, sheetNo);
                writeExcelRow(fieldName, sheet, 0);
            }

        }

        wwb.write();
        wwb.close();
    }

    /**
     * 输出内容到流中
     *
     * @param os
     * @param sheetName
     * @param fieldName
     * @param data
     * @throws java.io.IOException
     * @throws jxl.write.WriteException
     */
    public static void makeExcelWorkBook(OutputStream os, String sheetName,
                                         String[] fieldName, List<String[]> data,  String sheetName2,
                                         String[] fieldName2, List<String[]> data2)
            throws IOException, WriteException {
        sheetName = sheetName == null ? "sheet" : sheetName;

        // 文件对象
        WritableWorkbook wwb = Workbook.createWorkbook(os);

        int sheetNo = 0;
        // 工作簿对象
        WritableSheet sheet = null;
        for (int i = 0; i < data.size(); i++) {
            // int sheetSize = sheet.getRows();
            int r = i % EXCEL_SHEET_MAX_ROW_SIZE;
            if (r == 0) {
                sheet = wwb.createSheet(sheetName + " - " + sheetNo, sheetNo);
                sheetNo++;
                // 写表头
                writeExcelRow(fieldName, sheet, 0);
            }

            // 写数据
            writeExcelRow(data.get(i), sheet, r + 1);
        }

        // 没有意义但是 但是不写wwb.write()就会报错
        if (data.size() == 0) {
            sheet = wwb.createSheet(sheetName + " - " + sheetNo, sheetNo);
            writeExcelRow(fieldName, sheet, 0);
        }

        sheet = null;
        for (int i = 0; i < data2.size(); i++) {
            // int sheetSize = sheet.getRows();
            int r = i % EXCEL_SHEET_MAX_ROW_SIZE;
            if (r == 0) {
                sheet = wwb.createSheet(sheetName2 + " - " + sheetNo, sheetNo);
                sheetNo++;
                // 写表头
                writeExcelRow(fieldName2, sheet, 0);
            }

            // 写数据
            writeExcelRow(data2.get(i), sheet, r + 1);
        }

        // 没有意义但是 但是不写wwb.write()就会报错
        if (data2.size() == 0) {
            sheet = wwb.createSheet(sheetName2 + " - " + sheetNo, sheetNo);
            writeExcelRow(fieldName2, sheet, 0);
        }

        wwb.write();
        wwb.close();
    }

    /**
     * 向工作表中写入一行数据
     *
     * @param data
     * @param sheet
     * @param row
     * @throws jxl.write.WriteException
     */
    private static void writeExcelRow(List<String> data, WritableSheet sheet,
                                      int row) throws WriteException {
        for (int i = 0; i < data.size(); i++) {
            String d = data.get(i) == null ? "" : data.get(i);

            WritableCell cell;
            if (isDigit(d)) {
                cell = new Number(i, row, Double.valueOf(d));
            } else {
                cell = new Label(i, row, d);
            }

            sheet.addCell(cell);
        }
    }

    /**
     * 向工作表中写入一行数据
     *
     * @param data
     * @param sheet
     * @param row
     * @throws jxl.write.WriteException
     */
    private static void writeExcelRow(String[] data, WritableSheet sheet,
                                      int row) throws WriteException {
        for (int i = 0; i < data.length; i++) {
            String d = data[i] == null ? "" : data[i];

            WritableCell cell;
/*            if (isDigit(d)) {
                cell = new Number(i, row, Double.valueOf(d));
            } else {*/
                cell = new Label(i, row, d);
  //          }

            sheet.addCell(cell);
        }
    }

    /**
     * 是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isDigit(String str) {
        if (str == null) {
            return false;
        }

        str = str.trim();
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }

    /**
     * 输出内容到流中
     * 只适用于输出不超过‘EXCEL_SHEET_MAX_ROW_SIZE’的数据
     * @param os
     * @param sheetNameList
     * @param fieldNameList
     * @param dataList
     * @throws java.io.IOException
     * @throws jxl.write.WriteException
     */
    public static void makeExcelWorkBook(OutputStream os, List<String> sheetNameList,
                                         List<String[]> fieldNameList, List<List<String[]>> dataList,List<List<int[]>> mergeList)
            throws IOException, WriteException {
        // 文件对象
        WritableWorkbook wwb = Workbook.createWorkbook(os);
        for(int j=0;j<sheetNameList.size();j++){
            String sheetName = sheetNameList.get(j);
            sheetName = sheetName == null ? "sheet" : sheetName;
            String[] fieldName = fieldNameList.get(j);
            List<String[]> data = dataList.get(j);
            List<int[]> merge = mergeList.get(j);
            // 工作簿对象
            WritableSheet sheet = null;

            // 没有意义但是 但是不写wwb.write()就会报错
            if (data.size() == 0 || data.size() > EXCEL_SHEET_MAX_ROW_SIZE) {
                sheet = wwb.createSheet(sheetName, j);
                writeExcelRow(fieldName, sheet, 0);
            } else {
                for (int i = 0; i < data.size(); i++) {
                    if (i == 0) {
                        sheet = wwb.createSheet(sheetName, j);
                        // 写表头
                        writeExcelRowNoNull(fieldName, sheet, 0);
                    }

                    // 写数据
                    writeExcelRowNoNull(data.get(i), sheet, i + 1);
                }
                for (int[] integers : merge) {
                    if (sheet != null && integers.length == 4) {
                        sheet.mergeCells(integers[0], integers[1], integers[2], integers[3]);
                    }
                }
            }
        }
        wwb.write();
        wwb.close();
    }

    /**
     * 向工作表中写入一行数据(为空则不填)
     *
     * @param data
     * @param sheet
     * @param row
     * @throws jxl.write.WriteException
     */
    private static void writeExcelRowNoNull(String[] data, WritableSheet sheet,
                                      int row) throws WriteException {
        for (int i = 0; i < data.length; i++) {
            if(data[i] != null){
                String d = data[i];
                WritableCell cell;
                cell = new Label(i, row, d);
                WritableCellFormat cellFormat = new WritableCellFormat();
                cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
                cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                cell.setCellFormat(cellFormat);
                sheet.addCell(cell);
            }
        }
    }
}
