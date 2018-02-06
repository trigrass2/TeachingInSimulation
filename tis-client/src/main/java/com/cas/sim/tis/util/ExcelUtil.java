package com.cas.sim.tis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cas.util.Util;

/**
 * 表格文件处理类
 * @author sco_pra
 */
public final class ExcelUtil {

	static final String DB_NULL_STR = "null";

	static final String DB_NULL_NUMBER = "-9999";
	public static int COLNUM = 1; // 共读取1列
	public static int START_ROW = 1 - 1; // 从编号为1的开始

	static int CELL_TYPE_DATE = 99;
	static int CELL_TYPE_NULL = 3;
	static int CELL_TYPE_NUM = 0;
	static int CEll_TYPE_STRING = 1;
	static int CEll_TYPE_EXPRESSION = 2;
	static int CELL_TYPE_BOOLEAN = 4;
	public static String EXCEL_TYPE_2007 = "2007";
	public static String EXCEL_TYPE_2003 = "2003";
	static int RETURN_SUCCESS = 0;
	static int RETURN_FAILE = 1;
	static int RETURN_ERROR = -1;
	static int FIND_SHEET_RESULT_LENGTH = 10;
	public static String[] SHEET_NAMES_DEFAULT = { "sheet1", "sheet2", "sheet3" };

	/**
	 * 在指定路径创建Excel文件，内附3个Sheet
	 * @param version 生成的Excel文件版本/2007?2003?
	 * @param filePath 文件绝对路径
	 * @param overWriteFlag 是否將原有的覆蓋
	 * @return RETURN_SUCCESS:创建成功 1:文件已经存在
	 * @throws IOException
	 */
	public static int newExcel(String version, String filePath, Boolean overWriteFlag) {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isFile()) {
				System.out.println("文件已存在！");
				if (overWriteFlag) {
					file.delete();
					System.out.println("刪除成功");
				} else {
					return RETURN_FAILE;
				}
			}
		}

		File folder = new File(file.getParent());
		if (!folder.exists()) {
			folder.mkdirs();
		}
		Workbook wb = null;
		FileOutputStream fo = null;

		if (version.equals(EXCEL_TYPE_2007)) {
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}

		try {
			fo = new FileOutputStream(filePath);
			wb.write(fo);
			fo.flush();

			newSheet(EXCEL_TYPE_2003, filePath, true, SHEET_NAMES_DEFAULT);
			System.out.println("新的Excel文件生成成功");

			return RETURN_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return RETURN_ERROR;
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
				if (wb != null) {
					wb.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 在指定Excel中创建Sheet,文件必须存在
	 * @param version Excel文件版本
	 * @param filePath 目标文件路径（含文件名）
	 * @param sheetNames 希望作成的sheetName可是是String数组
	 * @param overWriteFlag 是否覆盖
	 * @return RETURN_SUCCESS:创建成功 1:文件已经存在
	 * @throws IOException
	 */
	public static int newSheet(String version, String filePath, boolean overWriteFlag, String... sheetNames) {
		// 检查sheet名是否已经存在，根据overWirteFlag进行处理
		FileInputStream fi = null;
		FileOutputStream foDel = null;
		Workbook wbIn = null;
		try {
			fi = new FileInputStream(filePath);
			if (version.equals(EXCEL_TYPE_2007)) {
				wbIn = new XSSFWorkbook(fi);
			} else {
				wbIn = new HSSFWorkbook(fi);
			}
			for (int i = 0; i < sheetNames.length; i++) {
				int sheetIndex = wbIn.getSheetIndex(sheetNames[i]);
				if (sheetIndex != -1) {
					System.out.println("工作簿已经存在！位于第" + (sheetIndex + 1) + "张");
					if (overWriteFlag) {
						wbIn.removeSheetAt(sheetIndex);
						System.out.println("删除成功!");
					} else {
						return RETURN_FAILE;
					}
				}
			}
			foDel = new FileOutputStream(filePath);
			wbIn.write(foDel);
			foDel.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (foDel != null) {
					foDel.close();
				}
				if (wbIn != null) {
					wbIn.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 插入新的sheet
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(filePath);
			for (int i = 0; i < sheetNames.length; i++) {
				wbIn.createSheet(WorkbookUtil.createSafeSheetName(sheetNames[i]));
				// wbOut.setSheetName(totalSheetsNum,
				// WorkbookUtil.createSafeSheetName(sheetNames[i]));
			}
			wbIn.write(fo);
			fo.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return RETURN_ERROR;
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
				if (fi != null) {
					fi.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return RETURN_SUCCESS;
	}

	private static Object[][] readExcelCell07(Workbook wb, String sheetName, int rowNum, int columnNum) throws Exception {
		Object[][] result = null;

		int cellType = 0;
		XSSFRow row = null;
		XSSFCell cell = null;
		result = new Object[rowNum][columnNum];
		XSSFSheet sheet = (XSSFSheet) wb.getSheet(sheetName);

		// 开始循环遍历单元格，取出数据放入result的二维数组中
		for (int i = START_ROW; i < rowNum; i++) {
			row = sheet.getRow(i);
			cellType = -1;
			Object cellValue = null;
			// 确保此行有数据
			if (row == null) {
				result[i] = null;
				continue;
			}
			boolean rowEmptyFlg = true;
			for (int j = 0; j < columnNum; j++) {
				cell = row.getCell(j);
				if (cell != null) {
					rowEmptyFlg = false;
					// 判断单元格内数据类型，
					try {
						// 數字型必須要先檢測，他既不會走if也不會走catch
						cellType = cell.getCellType();
						if (DateUtil.isCellDateFormatted(cell)) {
							// 日期格式需要这样来判断，下面的方法判断不了
							cellType = CELL_TYPE_DATE;
						}
					} catch (IllegalStateException e) {
						cellType = cell.getCellType();
					}

					if (cellType == CELL_TYPE_NULL) {
						// 空值型
						result[i][j] = null;
						continue;
					} else if (cellType == CELL_TYPE_NUM) {
						// 数值型
						cellValue = cell.getNumericCellValue();
					} else if (cellType == CEll_TYPE_STRING) {
						// 字符串型
						cellValue = cell.getStringCellValue();
					} else if (cellType == CELL_TYPE_BOOLEAN) {
						// boolean型
						cellValue = new Boolean(cell.getBooleanCellValue());
					} else if (cellType == CELL_TYPE_DATE) {
						// 日期类型
						double value = cell.getNumericCellValue();
						cellValue = DateUtil.getJavaDate(value);
					} else if (cellType == CEll_TYPE_EXPRESSION) {
						cellValue = cell.getCTCell().getV();
//							cellValue = cell.getNumericCellValue();
					}
					result[i][j] = cellValue;
				} else {
					result[i][j] = null;
				}
			}
			// 如何该行每一列都没有数据，则该行为空
			if (rowEmptyFlg) {
				result[i] = null;
			}
		}
		return result;
	}

	private static Object[][] readExcelCell03(Workbook wb, String sheetName, int rowNum, int columnNum) throws Exception {
		Object[][] result = null;

		int cellType = 0;
		HSSFRow row = null;
		HSSFCell cell = null;
		result = new Object[rowNum][columnNum];

		HSSFSheet sheet = (HSSFSheet) wb.getSheet(sheetName);

		// 开始循环遍历单元格，取出数据放入result的二维数组中
		for (int i = START_ROW; i < rowNum; i++) {
			row = sheet.getRow(i);
			cellType = -1;
			Object cellValue = null;
			// 确保此行有数据
			if (row == null) {
				result[i] = null;
				continue;
			}
			boolean rowEmptyFlg = true;
			for (int j = 0; j < columnNum; j++) {
				cell = row.getCell(j);
				if (cell != null) {
					rowEmptyFlg = false;
					// 判断单元格内数据类型，
					try {
						// 數字型必須要先檢測，他既不會走if也不會走catch
						cellType = cell.getCellType();
						if (DateUtil.isCellDateFormatted(cell)) {
							// 日期格式需要这样来判断，下面的方法判断不了
							cellType = CELL_TYPE_DATE;
						}
					} catch (IllegalStateException e) {
						cellType = cell.getCellType();
					}

					if (cellType == CELL_TYPE_NULL) {
						// 空值型
						result[i][j] = null;
						continue;
					} else if (cellType == CELL_TYPE_NUM) {
						// 数值型
						cellValue = cell.getNumericCellValue();
					} else if (cellType == CEll_TYPE_STRING) {
						// 字符串型
						cellValue = cell.getStringCellValue();
					} else if (cellType == CELL_TYPE_BOOLEAN) {
						// boolean型
						cellValue = new Boolean(cell.getBooleanCellValue());
					} else if (cellType == CELL_TYPE_DATE) {
						// 日期类型
						double value = cell.getNumericCellValue();
						cellValue = DateUtil.getJavaDate(value);
					} else if (cellType == CEll_TYPE_EXPRESSION) {
						cellValue = String.valueOf(cell.getNumericCellValue());
						if ("NaN".equals(cellValue)) {
							cellValue = String.valueOf(cell.getRichStringCellValue());
						}
					}
					result[i][j] = cellValue;
				} else {
					result[i][j] = null;
				}
			}
			// 如何该行每一列都没有数据，则该行为空
			if (rowEmptyFlg) {
				result[i] = null;
			}
		}
		return result;
	}

	/**
	 * 目前支持读取的单元格格式为：boolean,字符串,数字,日期 如果某一行中一点数据都没有，比如第五行，那么返回的二维表中result[4]就会为null(数组从0开始计数，所以这里是4)
	 * @param version OFFICE版本
	 * @param fiPath 文件路径
	 * @param sheetName 目标sheet名
	 * @param rowNum 行数-从1开始计数
	 * @param columnNum 列数-从1开始计数
	 * @throws IOException
	 */
	public static Object[][] readExcelSheet(String fiPath, String sheetName, int rowNum, int columnNum) {
		FileInputStream fo = null;
		Object[][] result = null;

		try {
			fo = new FileInputStream(fiPath);
			Workbook wb = null;
			try {
				wb = new HSSFWorkbook(fo);
			} catch (Exception e) {
				try {
					fo = new FileInputStream(fiPath);
					wb = new XSSFWorkbook(fo);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

			try {
				if (wb instanceof XSSFWorkbook) {
					result = readExcelCell07(wb, sheetName, rowNum, columnNum);
				} else if (wb instanceof HSSFWorkbook) {
					result = readExcelCell03(wb, sheetName, rowNum, columnNum);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Object[][] readExcelSheet(String fiPath, String sheetName, int columnNum) {
		Workbook wb = null;
		FileInputStream fo = null;
		try {
			int rowNum = 1;

			fo = new FileInputStream(fiPath);

			try {
				wb = new HSSFWorkbook(fo);
			} catch (Exception e) {
				try {
					fo = new FileInputStream(fiPath);
					wb = new XSSFWorkbook(fo);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

			Sheet sheet = wb.getSheet(sheetName);

			if (sheet == null) {
				System.out.println("工作簿为空！");
				return null;
			}

			while (sheet.getRow(rowNum) != null) {
				rowNum++;
			}
			return readExcelSheet(fiPath, sheetName, rowNum, columnNum);
			// -------------------------------------------------------------
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fo = null;
			}
		}
	}

	/**
	 * 读取excel文件某个sheet中的所有单元格数据
	 * @param version
	 * @param fiPath
	 * @param sheetName
	 */
	public static Object[][] readExcelSheet(String fiPath, String sheetName) {
		Workbook wb = null;
		FileInputStream fo = null;
		try {
			int rowNum = 1, columnNum = 1;

			fo = new FileInputStream(fiPath);

			try {
				wb = new HSSFWorkbook(fo);
			} catch (Exception e) {
				try {
					fo = new FileInputStream(fiPath);
					wb = new XSSFWorkbook(fo);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

			Sheet sheet = wb.getSheet(sheetName);

			if (sheet == null) {
				System.out.println("工作簿为空！");
				return null;
			}

			while (sheet.getRow(rowNum) != null) {
				rowNum++;
			}
			while (!"".equals(sheet.getRow(0).getCell(columnNum).toString())) {
				columnNum++;
			}
			return readExcelSheet(fiPath, sheetName, rowNum, columnNum);
			// -------------------------------------------------------------
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fo = null;
			}
		}
	}

	/**
	 * 获取excel文件下所有的sheet名称
	 * @param version 文件版本
	 * @param fiPath 文件絕對路徑
	 * @param num 結果數組的長度
	 */
	public static String[] readAllSheet(String version, String fiPath, int num) {
		Workbook wb = null;
		FileInputStream fo = null;
		String[] result = new String[num];
		try {
			fo = new FileInputStream(fiPath);
			if (version.equals(EXCEL_TYPE_2007)) {
				wb = new XSSFWorkbook(fo);
			} else {
				wb = new HSSFWorkbook(fo);
			}

			int i = 0;
			try {
				for (; i < num; i++) {
					result[i] = wb.getSheetAt(i).getSheetName();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			try {
				wb.getSheetAt(i);
				System.out.println("輸入的結果集長度過小，不能讀取全部的sheet!");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
				if (wb != null) {
					wb.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param inPath
	 * @param outPath
	 * @param datas
	 * @param version
	 */
	public static void writeExcelByTemplate(String inPath, String outPath, String version, Map<String, Object[][]> datas, int startRow) {
		Workbook wb = null;
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			// 读取模版
			in = new FileInputStream(inPath);
			if (version.equals(EXCEL_TYPE_2007)) {
				wb = new XSSFWorkbook(in);
			} else {
				wb = new HSSFWorkbook(in);
			}
			in.close();
			// 写入数据
			for (Entry<String, Object[][]> data : datas.entrySet()) {
				Object[][] objs = data.getValue();
				Sheet sheet = wb.getSheet(data.getKey());
				for (int i = 0; i < objs.length; i++) {
					Row row = sheet.getRow(i + startRow);
					if (row == null) {
						row = sheet.createRow(i + startRow);
					}
					for (int j = 0; j < objs[i].length; j++) {
						Object obj = objs[i][j];
						if (obj == null) {
							continue;
						}
						Cell cell = row.getCell(j);
						if (cell == null) {
							cell = row.createCell(j);
						}
						String value = String.valueOf(obj);
						if (obj instanceof String) {
							cell.setCellValue(value);
						} else if (Util.isNumeric(value)) {
							cell.setCellValue(Double.parseDouble(value));
						}
					}
				}
			}
			// 保存到本地
			out = new FileOutputStream(outPath);
			wb.write(out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					in = null;
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					out = null;
				}
			}
		}
	}

	public static void main(String[] args) {
		ExcelUtil.readExcelSheet("C:/Users/Administrator/Desktop/教学大纲_计划模板.xlsx", "Sheet1");
	}
}
