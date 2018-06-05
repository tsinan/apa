package com.x.apa.loader.task;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;

import com.google.common.io.Files;
import com.x.apa.common.Constant;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author liumeng
 */
public class NewlyDomainCrawler extends WebCrawler implements Constant {

	private static final Pattern ZIP_EXTENSIONS = Pattern.compile(".*\\.zip/nrd$");

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#shouldVisit(edu.uci.ics.crawler4j.crawler.Page,
	 *      edu.uci.ics.crawler4j.url.WebURL)
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		if (href.contains("?")) {
			href = href.substring(0, href.lastIndexOf("?"));
		}

		if (!ZIP_EXTENSIONS.matcher(href).matches()) {
			return false;
		}

		return true;
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#visit(edu.uci.ics.crawler4j.crawler.Page)
	 */
	@Override
	public void visit(Page page) {

		String url = page.getWebURL().getURL();

		logger.debug("crawling data {} from {} success.", url, page.getWebURL().getParentUrl());

		// 判断是否是csv文件
		if (url.contains("?")) {
			url = url.substring(0, url.lastIndexOf("?"));
		}
		if (!ZIP_EXTENSIONS.matcher(url).matches() || !(page.getParseData() instanceof BinaryParseData)) {
			return;
		}
		if (ArrayUtils.isEmpty(page.getContentData())) {
			return;
		}

		ZipInputStream zin = null;
		FileOutputStream fos = null;
		try {

			// 解压目标文件名
			String unzipFileName = (String) getMyController().getCustomData();
			// 先解压到.bak临时文件
			File unzipFile = FileUtils.getFile(unzipFileName + ".bak");
			FileUtils.forceMkdirParent(unzipFile);
			if (!unzipFile.exists()) {
				unzipFile.createNewFile();
			}
			// 最终解压后的文件
			File finalFile = FileUtils.getFile(unzipFileName);

			// 压缩文件byte流
			zin = new ZipInputStream(new ByteArrayInputStream(page.getContentData()));
			fos = new FileOutputStream(unzipFile);

			// 循环处理每个被压缩文件
			boolean hasUnzip = false;
			ZipEntry entry = null;
			while ((entry = zin.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					// 不处理目录
					continue;
				} else if (hasUnzip) {
					// 只处理第一个文件
					logger.warn("when unzip newly domain file from {}, find more than one file in zip.", url);
					break;
				} else {
					hasUnzip = true;

					int b = 0;
					while ((b = zin.read()) != -1) {
						fos.write(b);
					}
					fos.close();
				}
			}

			// 解压成功后将.bak文件改名
			if (hasUnzip) {
				Files.move(unzipFile, finalFile);
			}
		} catch (IOException e) {
			logger.warn("store newly domain file from " + url + " failed.", e);
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
					// do nothing
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}

	}

}
