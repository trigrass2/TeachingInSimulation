package com.cas.sim.tis.test.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jodconverter.JodConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.LocalOfficeUtils;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lbh
 */

public class OfficeConverter {

	private static Logger logger = LoggerFactory.getLogger(OfficeConverter.class);

	/**
	 * 打开libreOffice服务的方法
	 * @return
	 */
	public String getLibreOfficeHome() {
		String osName = System.getProperty("os.name");

		if (Pattern.matches("Linux.*", osName)) {
			// 获取linux系统下libreoffice主程序的位置
			logger.info("获取Linux系统LibreOffice路径");
			return "/opt/libreoffice 5/program/soffice";
		} else if (Pattern.matches("Windows.*", osName)) {
			// 获取windows系统下libreoffice主程序的位置
			logger.info("获取windows系统LibreOffice路径");
			return "D:\\Program Files\\LibreOffice 5";
		}
		return null;
	}

	/**
	 * 转换libreoffice支持的文件为pdf
	 * @param inputfile
	 * @param outputfile
	 */
//    public void libreOffice2PDF(File inputfile, File outputfile) {  
//        String LibreOffice_HOME = getLibreOfficeHome();  
//        String fileName = inputfile.getName();  
//        logger.info(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + "文件" + inputfile.getName());  
//        System.out.println(fileName.substring(fileName.lastIndexOf(".")));  
//        if (fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".txt")) {  
//            System.out.println("处理txt文件");  
//            new Test3().TXTHandler(inputfile);  
//        }  
//        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();  
//        // libreOffice的安装目录  
//        configuration.setOfficeHome(new File(LibreOffice_HOME));  
//        // 端口号  
//        configuration.setPortNumber(8100);  
//        configuration.setTaskExecutionTimeout(1000 * 60 * 25L);  
////         设置任务执行超时为10分钟  
//        configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);  
////         设置任务队列超时为24小时  
//        OfficeManager officeManager = configuration.buildOfficeManager();  
//        officeManager.start();  
//        logger.info(new Date().toString() + "开始转换......");  
//        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);  
//        converter.getFormatRegistry();  
//        try {  
//            converter.convert(inputfile, outputfile);  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//            logger.info("转换失败");  
//        } finally {  
//            officeManager.stop();  
//        }  
//  
//  
//        logger.info(new Date().toString() + "转换结束....");  
//    }  

	// 将word格式的文件转换为pdf格式
	public static void Word2Pdf(String srcPath, String desPath) throws IOException {
		// 源文件目录
		File inputFile = new File(srcPath);
		if (!inputFile.exists()) {
			System.out.println("源文件不存在！");
			return;
		}
		// 输出文件目录
		File outputFile = new File(desPath);
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().exists();
		}
		// 连接openoffice服务
		OfficeManager officeManager = LocalOfficeManager.builder().officeHome("D:\\Program Files\\OpenOffice 4").install().build();
		try {
			officeManager.start();
			// 转换文档到pdf
			long time = System.currentTimeMillis();
			JodConverter.convert(inputFile).to(outputFile).execute();
			logger.info("文件：{}转换PDF：{}完成，用时{}毫秒！", srcPath, desPath, System.currentTimeMillis() - time);
		} catch (OfficeException e) {
			e.printStackTrace();
			logger.warn("文件：{}转换PDF：{}失败！", srcPath, desPath);
		} finally {
			// 关闭连接
			LocalOfficeUtils.stopQuietly(officeManager);
		}
	}

	/**
	 * 测试的方法
	 * @param args
	 */
	public static void main(String[] args) {
//		System.setProperty("java.library.path", "D:\\Program Files\\OpenOffice 4\\program");
		try {
			Word2Pdf("F:\\工厂虚拟仿真操作说明书.docx", "F:\\test-time.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转换txt文件编码的方法
	 * @param file
	 * @return
	 */
	public File TXTHandler(File file) {
		// 或GBK
		String code = "gb2312";
		byte[] head = new byte[3];
		try {
			InputStream inputStream = new FileInputStream(file);
			inputStream.read(head);
			if (head[0] == -1 && head[1] == -2) {
				code = "UTF-16";
			} else if (head[0] == -2 && head[1] == -1) {
				code = "Unicode";
			} else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
				code = "UTF-8";
			}
			inputStream.close();

			System.out.println(code);
			if (code.equals("UTF-8")) {
				return file;
			}
			String str = FileUtils.readFileToString(file, code);
			FileUtils.writeStringToFile(file, str, "UTF-8");
			System.out.println("转码结束");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}
}