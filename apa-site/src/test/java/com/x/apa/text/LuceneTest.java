package com.x.apa.text;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.util.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlMapper;
import org.apache.tika.parser.html.HtmlParser;
import org.assertj.core.util.Sets;
import org.xml.sax.SAXException;

import com.x.apa.common.util.LuceneUtils;

import edu.uci.ics.crawler4j.parser.AllTagMapper;
import edu.uci.ics.crawler4j.parser.HtmlContentHandler;

/**
 * @author liumeng
 */
public class LuceneTest {

	private static final Set<String> PHISHING_WORD_SET = Sets.newLinkedHashSet();
	private static final String FILE_COMMENT = "//";

	static {

		try {
			BufferedReader br = null;
			try {
				br = (BufferedReader) IOUtils.getDecodingReader(LuceneUtils.class, "/lucene/ext_phishing.dic",
						StandardCharsets.UTF_8);
				String word = null;
				while ((word = br.readLine()) != null) {
					if (word.startsWith(FILE_COMMENT) == false) {
						PHISHING_WORD_SET.add(word.trim());
					}
				}
			} finally {
				IOUtils.close(br);
			}
		} catch (IOException e) {

		}

	}

	public static void main(String args[]) throws Exception {

		// StandardAnalyzer analyzer = new StandardAnalyzer();
		// SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		// SmartChineseAnalyzer analyzer = new
		// SmartChineseAnalyzer(DEFAULT_STOP_SET);
		// WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
		// IKAnalyzer analyzer = new IKAnalyzer(true);

		File file = FileUtils.getFile("/Users/liumeng/Desktop/html.txt");
		String text = FileUtils.readFileToString(file, "UTF-8");

		text = parseHtml(text);
		System.out.println(text);

		String tokens = LuceneUtils.getTokens(text, false);
		System.out.println(tokens);

		String keyword = LuceneUtils.getKeyword(text);
		System.out.println(keyword);
	}

	private static String parseHtml(String text)
			throws InstantiationException, IllegalAccessException, IOException, SAXException, TikaException {

		HtmlParser htmlParser = new HtmlParser();
		ParseContext parseContext = new ParseContext();
		parseContext.set(HtmlMapper.class, AllTagMapper.class.newInstance());

		Metadata metadata = new Metadata();
		HtmlContentHandler contentHandler = new HtmlContentHandler();
		InputStream inputStream = new ByteArrayInputStream(text.getBytes());
		htmlParser.parse(inputStream, contentHandler, metadata, parseContext);

		text = contentHandler.getBodyText().trim();
		//text = text.replaceAll("[\\s\\u00A0]+", ""); // 去掉空格和制表符
		text = text.replaceAll("[^\\u4e00-\\u9fa5\\n\\r]", "");
		text = text.replaceAll("[\\r\\n]", " ");

		return text;
	}

}
