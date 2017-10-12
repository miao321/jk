package jk28_web;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;



public class POI01Test {
	@Test
	public void testPoi() throws Exception{
		//1.创建工作簿
		Workbook wb = new HSSFWorkbook();
		
		//2.创建工作表Sheet
		Sheet sheet = wb.createSheet();
		
		//3.创建行对象Row
		Row row = sheet.createRow(3);//下标从0开始
		
		//4.创建单元格对象  从0计数
		Cell cell = row.createCell(3);
		
		//5.设置单元格内容
		cell.setCellValue("您好");
		
		//6.设置单元格的格式
		CellStyle cellStyle = wb.createCellStyle();
		
		Font font = wb.createFont();//创建字体对象
		font.setFontName("黑体");//设置字体名称
		font.setFontHeightInPoints((short) 48);//设置字体大小
		
		cellStyle.setFont(font);  //样式中添加一个字体样式
		
		cell.setCellStyle(cellStyle);
		
		//7.保存,关闭流
		OutputStream os = new FileOutputStream("E:/abc.xls");//创建一个输出流
		
		wb.write(os);
		os.close();
		
		//8.下载
		
	}

}
