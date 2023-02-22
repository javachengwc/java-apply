package com.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * excel文件操作器
 */

public class ExcelOperator {
	
	//private static Logger m_logger  = null;
	
	int cursor;
	
	/**
	 * 根据filePath获取Workbook
	 * @param filePath
	 * @return Workbook
	 */
	public static Workbook getWorkbook(String filePath){
		File file=new File(filePath);
		Workbook book = null;
		InputStream in;
		try {
			//m_logger.debug("read an existed workbook from file ["+filePath+"]");
			in = new FileInputStream(file);
			book = new XSSFWorkbook(in);
			//return book;
		} catch (FileNotFoundException e) {
			//m_logger.error("file ["+filePath+"] not found");
		} catch (IOException e) {
			//m_logger.error("IOException: "+e.getMessage());
		}
			
		return book;	
	}
	
	/**
	 * 创建一个新的Workbook
	 * @return Workbook
	 */
	public static Workbook createWorkbook(){
		//m_logger.debug("create a new workbook ");
		Workbook book = new XSSFWorkbook();
		return book;
	}
	
	
	/**
	 * 根据指定的Workbook及表名获取Sheet
	 * @param workbook
	 * @param sheetName
	 * @return Sheet
	 */
	public static  Sheet getSheet(Workbook workbook, String sheetName){
		Sheet sheet = null;
		//Sheet sheet;
		try{
			sheet = workbook.getSheet(sheetName);
		}catch(Exception e){
			//m_logger.error("the sheet doesn't exist");
		}
		return sheet;
	}
	
	/**
	 * 在Workbook中创建一个新的Sheet，名称为sheetName
	 * @param workbook
	 * @param sheetName
	 * @return Sheet
	 */
	public static Sheet createSheet(Workbook workbook,String sheetName){
		return workbook.createSheet(sheetName);
	}
	
	/**
	 * 获取指定sheet中第rowNum行的Row
	 * @param sheet
	 * @param rowNum
	 * @return Row
	 */
	public static Row getRow(Sheet sheet,int rowNum){
		Row row=null;
		try{
			row = sheet.getRow(rowNum);
		}catch(Exception e){
			//m_logger.error("can not get row ["+rowNum+"] , return null,e:"+e.getMessage());
		}
		return row;
	}
	
	/**
	 * 在sheet中创建一个新的第rowNum行的row
	 * @param sheet
	 * @param rowNum
	 * @return Row
	 */
	public static Row createRow(Sheet sheet,int rowNum){
		return sheet.createRow(rowNum);
	}
	
	/**
	 * 获取行、列指定的单元格
	 * @param row
	 * @param colNum
	 * @return Cell
	 */
	public static Cell getCell(Row row, int colNum){
		Cell cell=null;
		try{
			cell = row.getCell(colNum);
		}catch(Exception e){
			//m_logger.error("can not get cell in col ["+colNum+"] , return null,e:"+e.getMessage());
		}
		return cell;
	}
	
	/**
	 * 在指定的workbook的sheet表中创建一个位于rowNum行，colNum列的值为value的单元格，其格式由style指定
	 * @param workbook
	 * @param sheet
	 * @param rowNum
	 * @param colNum
	 * @param value
	 * @param style
	 */
	public static void createCell(Workbook workbook,Sheet sheet,int rowNum,int colNum, String value, String style){
		//int rowN = sheet.getLastRowNum();
		Row row;
		Cell cell;
		//int rowNums = sheet.getLastRowNum();
		row = sheet.getRow(rowNum);
		if(row==null){
			row = sheet.createRow(rowNum);
		}
	
		cell = row.createCell(colNum);
		cell.setCellValue(value);
		setCellStyle(workbook,cell,style);
		
		//m_logger.debug("new cell's value is ["+sheet.getRow(rowNum).getCell(colNum).getStringCellValue()+"]");
	}
	
	/**
	 * 设置指定单元格的样式
	 * @param workbook
	 * @param cell
	 * @param style
	 */
	public static void setCellStyle(Workbook workbook,Cell cell,String style){
		CellStyle cellStyle=workbook.createCellStyle();
		XSSFFont font = (XSSFFont) workbook.createFont();
		font.setFontName("微软雅黑");
		if(style.contains("title")){
			font.setBold(true);
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			
		}else if(style.contains("result")){
			font.setBold(false);
			cell.getSheet().setColumnWidth(cell.getColumnIndex(), 7*256);
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		}else if(style.contains("common")){
			cell.getSheet().setColumnWidth(cell.getColumnIndex(), 20*256);
			font.setBold(false);
		}else if(style.contains("summary")){
			font.setBold(false);
			int col=cell.getColumnIndex();
			cell.getSheet().setColumnWidth(col, 50*256);
		}
		//设置边框
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		
		//设置方向
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);
		cell.setCellStyle(cellStyle);
		cell.setCellType(Cell.CELL_TYPE_STRING);
	}
	
	/**
	 * 获取指定单元格的值
	 * @param sheet
	 * @param rowNum
	 * @return List<String>
	 */
	public static List<String> getRowValue(Sheet sheet,int rowNum){
		List<String> value = new LinkedList<String>();
		Row row = sheet.getRow(rowNum);
		int colNums=row.getPhysicalNumberOfCells();
		for(int i=0;i<colNums;i++){
			try{
			row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			value.add(row.getCell(i).getStringCellValue());
			}catch(NullPointerException e){
				value.add("");
				colNums++;
			}
		}
		return value;
	}
	
	
	/**
	 * 返回指定table指定sheet中的所有数据，每一行数据存储在List<String>中，所有数据存储在List<List<String>>中
	 * @param sheetName
	 * @param bookName
	 * @return List<List<String>>
	 */
	public static List<List<String>> getSheetValue(String bookName,String sheetName){
		List<List<String>> dataList = new LinkedList<List<String>>();
		List<String> dataInfo;
		
		Workbook workbook = getWorkbook(bookName);
		Sheet sheet = getSheet(workbook,sheetName);
		
		int rowCount = sheet.getPhysicalNumberOfRows();
		
		for(int i=0;i<rowCount;i++){
			dataInfo = getRowValue(sheet,i);
			dataList.add(dataInfo);
		}
		
		return dataList;
	}
	
	/**
	 * 获取指定行的值，以<列标题：值>的形式给出
	 * @param colTitle
	 * @param sheet
	 * @param rowNum
	 * @return Map<String,String>
	 */
	public static Map<String,String> getKeyValueSet(List<String> colTitle,Sheet sheet,int rowNum){
		Map<String,String> set = new HashMap<String,String>();
		Row row = getRow(sheet, rowNum);
		int colNums = row.getPhysicalNumberOfCells();
		for(int i=0;i<colNums;i++){
			row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			String title=colTitle.get(i);
			String value=row.getCell(i).getStringCellValue();
			set.put(title, value);
		}
		return set;
	}

	/**
	 * 将workbook保存到指定的文件中
	 * @param workbook
	 * @param reportFullPath
	 */
	public static void writeToFile(Workbook workbook,String reportFullPath) {
		//m_logger.debug("write workbook to file ["+reportFullPath+"]");
		checkWorkbookData(workbook);
		
		try {
			workbook.write(new FileOutputStream(reportFullPath));
		} catch (FileNotFoundException e) {
			//m_logger.error("report file [" + reportFullPath+"] not found");
			//m_logger.error(e.getMessage());
		} catch (IOException e) {
			//m_logger.error("IO exception "+e.getMessage());
		}
		
	}
	
	
	public static void checkWorkbookData(Workbook workbook){
		//m_logger.debug("sheet number is ["+workbook.getNumberOfSheets()+"]");
		for(int i=0;i<workbook.getNumberOfSheets();i++){
			Sheet sheet = workbook.getSheetAt(i);
			//m_logger.debug("content of sheet ["+workbook.getSheetName(i)+"] is:");
			int rowNums = sheet.getLastRowNum();
			for(int j=0;j<=rowNums;j++){
				try{
					Row row=sheet.getRow(j);
			
					//m_logger.debug("row: "+j);
					int colNums = row.getPhysicalNumberOfCells();
					for(int k=0;k<colNums;k++){
						Cell cell = row.getCell(k);
						try{
							//m_logger.debug(cell.getStringCellValue());
						}catch(Exception e){
							//m_logger.debug("cell of "+j+","+k+" in sheet"+workbook.getSheetName(i)+" is null");
						}//try-catch
					}//for
				}catch(Exception e){
					//m_logger.debug("row "+j+"is null");
				}
				
			}
		}
	}
}
