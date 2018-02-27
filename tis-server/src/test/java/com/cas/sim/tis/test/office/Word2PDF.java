package com.cas.sim.tis.test.office;

/***
 * 0.jacod-1.17.jar和jacod-1.17-x64.dll
 * 1.本地安装Office程序<br>
 * 2.Excel转化需要Adobe Acrobat虚拟打印机<br>
 * 3.启用Print Spooler服务
 */
public class Word2PDF {
//	private static final int wdFormatPDF = 17;
//	private static final int xlTypePDF = 0;
//	private static final int ppSaveAsPDF = 32;
//
//	public static void main(String[] args) {
//
////        int time = convert2PDF("F:/3D电工仿真项目方案书2.docx", "F:/3D电工仿真项目方案书2.pdf");
//		int time = convert2PDF("F:\\魔方墙.xlsx", "F:\\魔方墙.pdf");
////		int time = convert2PDF("F:\\工厂虚拟仿真操作说明书.docx", "F:\\test-time2.pdf");
////        int time = convert2PDF("F:\\(2017-09-11 16-22-50)引导文教学方法.ppt", "F:\\引导文教学方法.pdf");
//
//		if (time == -4) {
//			System.out.println("转化失败，未知错误...");
//		} else if (time == -3) {
//			System.out.println("原文件就是PDF文件,无需转化...");
//		} else if (time == -2) {
//			System.out.println("转化失败，文件不存在...");
//		} else if (time == -1) {
//			System.out.println("转化失败，请重新尝试...");
//		} else if (time < -4) {
//			System.out.println("转化失败，请重新尝试...");
//		} else {
//			System.out.println("转化成功，用时：  " + time + "s...");
//		}
//
//	}
//
//	/***
//	 * 判断需要转化文件的类型（Excel、Word、ppt）
//	 * @param inputFile
//	 * @param pdfFile
//	 */
//	private static int convert2PDF(String inputFile, String pdfFile) {
//		String kind = getFileSufix(inputFile);
//		File file = new File(inputFile);
//		if (!file.exists()) {
//			return -2;// 文件不存在
//		}
//		if (kind.equals("pdf")) {
//			return -3;// 原文件就是PDF文件
//		}
//		if (kind.equals("doc") || kind.equals("docx") || kind.equals("txt")) {
//			return Word2PDF.word2PDF(inputFile, pdfFile);
//		} else if (kind.equals("ppt") || kind.equals("pptx")) {
//			return Word2PDF.ppt2PDF(inputFile, pdfFile);
//		} else if (kind.equals("xls") || kind.equals("xlsx")) {
//			return Word2PDF.Ex2PDF(inputFile, pdfFile);
//		} else {
//			return -4;
//		}
//	}
//
//	/***
//	 * 判断文件类型
//	 * @param fileName
//	 * @return
//	 */
//	public static String getFileSufix(String fileName) {
//		int splitIndex = fileName.lastIndexOf(".");
//		return fileName.substring(splitIndex + 1);
//	}
//
//	/***
//	 * Word转PDF
//	 * @param inputFile
//	 * @param pdfFile
//	 * @return
//	 */
//
//	private static int word2PDF(String inputFile, String pdfFile) {
//		// TODO Auto-generated method stub
//		try {
//			// 打开Word应用程序
//			ActiveXComponent app = new ActiveXComponent("KWPS.Application");
////			ActiveXComponent app = new ActiveXComponent("Word.Application");
//			System.out.println("开始转化Word为PDF...");
//			long date = new Date().getTime();
//			// 设置Word不可见
//			app.setProperty("Visible", new Variant(false));
//			// 禁用宏
//			app.setProperty("AutomationSecurity", new Variant(3));
//			// 获得Word中所有打开的文档，返回documents对象
//			Dispatch docs = app.getProperty("Documents").toDispatch();
//			// 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
//			Dispatch doc = Dispatch.call(docs, "Open", inputFile, false, true).toDispatch();
//			/***
//			 * 调用Document对象的SaveAs方法，将文档保存为pdf格式 Dispatch.call(doc, "SaveAs", pdfFile, wdFormatPDF word保存为pdf格式宏，值为17 )
//			 */
//			Dispatch.call(doc, "ExportAsFixedFormat", pdfFile, wdFormatPDF);// word保存为pdf格式宏，值为17
//			System.out.println(doc);
//			// 关闭文档
//			long date2 = new Date().getTime();
//			int time = (int) ((date2 - date) / 1000);
//
//			Dispatch.call(doc, "Close", false);
//			// 关闭Word应用程序
//			app.invoke("Quit", 0);
//			return time;
//		} catch (Exception e) {
//			// TODO: handle exception
//			return -1;
//		}
//
//	}
//
//	/***
//	 * Excel转化成PDF
//	 * @param inputFile
//	 * @param pdfFile
//	 * @return
//	 */
//	private static int Ex2PDF(String inputFile, String pdfFile) {
//		ComThread.InitSTA(true);
////		ActiveXComponent ax = new ActiveXComponent("Excel.Application");
//		ActiveXComponent ax = new ActiveXComponent("KET.Application");
//		try {
//			System.out.println("开始转化Excel为PDF...");
//			long date = new Date().getTime();
//			ax.setProperty("Visible", false);
//			ax.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
//			Dispatch excels = ax.getProperty("Workbooks").toDispatch();
//
//			Dispatch excel = Dispatch.invoke(excels, "Open", Dispatch.Method, new Object[] { inputFile, new Variant(false), new Variant(false) }, new int[9]).toDispatch();
//			// 转换格式
//			Dispatch.invoke(excel, "ExportAsFixedFormat", Dispatch.Method, new Object[] { new Variant(0), // PDF格式=0
//					pdfFile, new Variant(xlTypePDF) // 0=标准 (生成的PDF图片不会变模糊) 1=最小文件
//					// (生成的PDF图片糊的一塌糊涂)
//			}, new int[1]);
//
//			// 这里放弃使用SaveAs
//			/*
//			 * Dispatch.invoke(excel,"SaveAs",Dispatch.Method,new Object[]{ outFile, new Variant(57), new Variant(false), new Variant(57), new Variant(57), new Variant(false), new Variant(true), new Variant(57), new Variant(true), new Variant(true), new Variant(true) },new int[1]);
//			 */
//			long date2 = new Date().getTime();
//			int time = (int) ((date2 - date) / 1000);
//			Dispatch.call(excel, "Close", new Variant(false));
//
//			return time;
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return -1;
//		} finally {
//			if (ax != null) {
//				ax.invoke("Quit", new Variant[] {});
//				ax = null;
//			}
//			ComThread.Release();
//		}
//	}
//
//	/***
//	 * ppt转化成PDF
//	 * @param inputFile
//	 * @param pdfFile
//	 * @return
//	 */
//	private static int ppt2PDF(String inputFile, String pdfFile) {
//		try {
//			ComThread.InitSTA(true);
//			ActiveXComponent app = new ActiveXComponent("PowerPoint.Application");
////            app.setProperty("Visible", false);
//			System.out.println("开始转化PPT为PDF...");
//			long date = new Date().getTime();
//			Dispatch ppts = app.getProperty("Presentations").toDispatch();
//			Dispatch ppt = Dispatch.call(ppts, "Open", inputFile, true, // ReadOnly
//					// false, // Untitled指定文件是否有标题
//					false// WithWindow指定文件是否可见
//			).toDispatch();
//			Dispatch.invoke(ppt, "SaveAs", Dispatch.Method, new Object[] { pdfFile, new Variant(ppSaveAsPDF) }, new int[1]);
//			System.out.println("PPT");
//			Dispatch.call(ppt, "Close");
//			long date2 = new Date().getTime();
//			int time = (int) ((date2 - date) / 1000);
//			app.invoke("Quit");
//			return time;
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.getStackTrace();
//			return -1;
//		}
//	}
}