package com.x.apa.common.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liumeng
 */
public class ExportUtils {

	private static Logger logger = LoggerFactory.getLogger(ExportUtils.class);

	/**
	 * 根据不同浏览器将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 * 
	 * @param fileName
	 *            原文件名
	 * @return 重新编码后的文件名
	 */
	public static String fileNameToUtf8String(HttpServletRequest request, String fileName) {
		String agent = request.getHeader("User-Agent");
		try {
			boolean isFireFox = agent != null && agent.toLowerCase().indexOf("firefox") != -1;
			if (isFireFox) {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			} else {
				fileName = StringUtils.toUtf8String(fileName);
				if (agent != null && agent.indexOf("MSIE") != -1 && fileName.length() > 150) {
					// see http://support.microsoft.com/default.aspx?kbid=816868

					// 根据request的locale 得出可能的编码
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("fileNameToUtf8String faild, fileName is " + fileName, e);
		}
		return fileName;
	}

	public static CellStyle buildTitleStyle(HSSFWorkbook workbook) {
		CellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setAlignment(HorizontalAlignment.CENTER);
		HSSFFont bodyFont = workbook.createFont();
		bodyFont.setFontHeightInPoints((short) 15);// 设置字体大小
		dataStyle.setFont(bodyFont);
		dataStyle.setWrapText(true);// 自动换行

		return dataStyle;
	}

	public static CellStyle buildHeaderStyle(HSSFWorkbook workbook) {
		CellStyle headStyle = workbook.createCellStyle();
		headStyle.setAlignment(HorizontalAlignment.LEFT);
		headStyle.setBorderBottom(BorderStyle.THIN); // 下边框
		headStyle.setBorderLeft(BorderStyle.THIN);// 左边框
		headStyle.setBorderTop(BorderStyle.THIN);// 上边框
		headStyle.setBorderRight(BorderStyle.THIN);// 右边框
		headStyle.setFillForegroundColor((short) 13);// 设置背景色
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		HSSFFont headFont = workbook.createFont();
		headFont.setFontName("黑体");
		headFont.setFontHeightInPoints((short) 14);// 设置字体大小
		headStyle.setFont(headFont);
		return headStyle;
	}

	public static CellStyle buildDataStyle(HSSFWorkbook workbook) {
		CellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setAlignment(HorizontalAlignment.LEFT);
		dataStyle.setBorderBottom(BorderStyle.THIN); // 下边框
		dataStyle.setBorderLeft(BorderStyle.THIN);// 左边框
		dataStyle.setBorderTop(BorderStyle.THIN);// 上边框
		dataStyle.setBorderRight(BorderStyle.THIN);// 右边框
		HSSFFont bodyFont = workbook.createFont();
		bodyFont.setFontHeightInPoints((short) 12);// 设置字体大小
		dataStyle.setFont(bodyFont);
		dataStyle.setWrapText(true);// 自动换行

		return dataStyle;
	}
}
