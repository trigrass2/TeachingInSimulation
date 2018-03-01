package com.cas.sim.tis.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.cas.sim.tis.consts.TemplateConsts;

/**
 * @功能 word内容转到EXCEL中
 * @作者 CaoWJ
 * @创建日期 2017年1月9日
 * @修改人 CaoWJ
 */
public class ExcelConverter {

	private static Row row;

	public static void excelConverter1() throws Exception {
		File dir = new File("G:\\Workspace\\TeachingInSimulation\\TeachingInSimulation\\tis-client\\src\\test\\resources\\维修电工（初级）单选题");
		File dir2 = new File("G:\\Workspace\\TeachingInSimulation\\TeachingInSimulation\\tis-client\\src\\test\\resources\\维修电工（初级）多选题");

		writeExcel1(dir);
		writeExcel1(dir2);

	}

	/**
	 * @param data
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void writeExcel1(File dir) throws FileNotFoundException, IOException {
		File[] files = dir.listFiles();
		for (File file : files) {
			String data = loadDoc(file);
			System.out.println("读取" + file.getName() + "完成");

			File excel = new File(TemplateConsts.QUESTION_TEMPLATE);
			File target = new File("C:\\Users\\Administrator\\Desktop\\Excel\\" + file.getName().replaceAll("docx", "xls"));

			// 读取模版
			FileInputStream in = new FileInputStream(excel);
			Workbook wb = new HSSFWorkbook(in);
			in.close();

			removeOther(wb, 1);
			removeOther(wb, 2);
			removeOther(wb, 3);

			Sheet sheet = wb.getSheetAt(0);
			int index = 2;
			for (String d : data.split("\r\n")) {
				for (String string : d.split("\n")) {
					System.out.println(index);
					if (string.startsWith("维修电工（初级）") || string.matches("[1-9]\\d*")) {
						continue;
					}
					if (string.matches("[0-9]+.*")) {
						row = sheet.createRow(index);
						Cell cell = row.createCell(0);
						int i = string.indexOf("．");
						cell.setCellValue(string.substring(i + 1));
					} else if (string.startsWith("（")) {
						Cell cell = row.getCell(1);
						if (cell == null) {
							cell = row.createCell(1);
							cell.setCellValue(string.replaceAll("（A）", "A、"));
						} else {
							cell.setCellValue(cell.getStringCellValue() + "\r\n" + string.replaceAll("（B）", "B、").replaceAll("（C）", "C、").replaceAll("（D）", "D、").replaceAll("（E）", "E、").replaceAll("（F）", "F、"));
						}
					} else if (string.startsWith("答案：")) {
						Cell cell2 = row.createCell(2);
						cell2.setCellValue(string.replaceAll("答案：", ""));
						Cell cell3 = row.createCell(3);
						cell3.setCellValue("5");
					} else if (string.startsWith("难度：")) {
						Cell cell = row.getCell(4);
						if (cell == null) {
							cell = row.createCell(4);
						}
						cell.setCellValue(string);
					} else if (string.startsWith("知识点：")) {
						Cell cell = row.getCell(4);
						if (cell == null) {
							cell = row.createCell(4);
						}
						cell.setCellValue(cell.getStringCellValue() + "\r\n" + string);
						index++;
					}
				}
			}
			// 保存到本地
			FileOutputStream out = new FileOutputStream(target);
			wb.write(out);
			out.flush();
			System.out.println("写入" + target.getAbsolutePath() + "完成");
		}
	}

	/**
	 * @param wb
	 * @param sheetIndex
	 */
	private static void removeOther(Workbook wb, int sheetIndex) {
		Sheet sheet = wb.getSheetAt(sheetIndex);
		Row row = sheet.getRow(2);
		sheet.removeRow(row);
	}

	public static void excelConverter2() throws Exception {
		File dir = new File("G:\\Workspace\\TeachingInSimulation\\TeachingInSimulation\\tis-client\\src\\test\\resources\\维修电工（初级）判断题");
		File[] files = dir.listFiles();
		for (File file : files) {
			String data = loadDoc(file);
			System.out.println("读取" + file.getName() + "完成");

			File excel = new File(TemplateConsts.QUESTION_TEMPLATE);
			File target = new File("C:\\Users\\Administrator\\Desktop\\Excel\\" + file.getName().replaceAll("docx", "xls"));

			// 读取模版
			FileInputStream in = new FileInputStream(excel);
			Workbook wb = new HSSFWorkbook(in);
			in.close();

			removeOther(wb, 0);
			removeOther(wb, 2);
			removeOther(wb, 3);

			Sheet sheet = wb.getSheetAt(1);
			int index = 2;
			for (String d : data.split("\r\n")) {
				for (String string : d.split("\n")) {
					System.out.println(index);
					if (string.startsWith("维修电工（初级）") || string.matches("[1-9]\\d*")) {
						continue;
					}
					if (string.matches("[0-9]+.*")) {
						row = sheet.createRow(index);
						Cell cell = row.createCell(0);
						int i = string.indexOf("．");
						cell.setCellValue(string.substring(i + 1));
					} else if (string.startsWith("答案：")) {
						Cell cell2 = row.createCell(1);
						cell2.setCellValue(string.replaceAll("答案：", "").replaceAll("×", "错误").replaceAll("√", "正确"));
						Cell cell3 = row.createCell(2);
						cell3.setCellValue("5");
					} else if (string.startsWith("难度：")) {
						Cell cell = row.getCell(3);
						if (cell == null) {
							cell = row.createCell(3);
						}
						cell.setCellValue(string);
					} else if (string.startsWith("知识点：")) {
						Cell cell = row.getCell(3);
						if (cell == null) {
							cell = row.createCell(3);
						}
						cell.setCellValue(cell.getStringCellValue() + "\r\n" + string);
						index++;
					}
				}
			}
			// 保存到本地
			FileOutputStream out = new FileOutputStream(target);
			wb.write(out);
			out.flush();
			System.out.println("写入" + target.getAbsolutePath() + "完成");
		}
	}

	/**
	 * @param dir
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String loadDoc(File file) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(file);
		XWPFDocument xdoc = new XWPFDocument(fis);
		XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
		return extractor.getText();
	}

	public static void main(String[] args) {
		try {
			ExcelConverter.excelConverter1();
			ExcelConverter.excelConverter2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
