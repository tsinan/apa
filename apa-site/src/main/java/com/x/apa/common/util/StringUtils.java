/**
 * 
 */
package com.x.apa.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

public class StringUtils {

	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	public static String toMD5String(String s) throws NoSuchAlgorithmException {
		MessageDigest alg = MessageDigest.getInstance("MD5");
		alg.update(s.getBytes());
		return byte2Hex(alg.digest());
	}

	public static String byte2Hex(byte[] paramArrayOfByte) {
		StringBuffer localStringBuffer = new StringBuffer();
		String str = "";
		for (int i = 0; i < paramArrayOfByte.length; ++i) {
			str = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
			if (str.length() == 1)
				localStringBuffer.append("0");
			localStringBuffer.append(str);
		}
		return localStringBuffer.toString().toUpperCase();
	}

	public static String bbcode(String text) {
		String html = text;

		Map<String, String> bbMap = new HashMap<String, String>();

		bbMap.put("(\r\n|\r|\n|\n\r)", "<br/>");
		bbMap.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
		bbMap.put("\\[i\\](.+?)\\[/i\\]", "<span style=\"font-style:italic;\">$1</span>");
		bbMap.put("\\[u\\](.+?)\\[/u\\]", "<span style=\"text-decoration:underline;\">$1</span>");
		bbMap.put("\\[h1\\](.+?)\\[/h1\\]", "<h1>$1</h1>");
		bbMap.put("\\[h2\\](.+?)\\[/h2\\]", "<h2>$1</h2>");
		bbMap.put("\\[h3\\](.+?)\\[/h3\\]", "<h3>$1</h3>");
		bbMap.put("\\[h4\\](.+?)\\[/h4\\]", "<h4>$1</h4>");
		bbMap.put("\\[h5\\](.+?)\\[/h5\\]", "<h5>$1</h5>");
		bbMap.put("\\[h6\\](.+?)\\[/h6\\]", "<h6>$1</h6>");
		bbMap.put("\\[quote\\](.+?)\\[/quote\\]", "<blockquote>$1</blockquote>");
		bbMap.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
		bbMap.put("\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]", "<p style=\"text-indent:$1px;line-height:$2%;\">$3</p>");
		bbMap.put("\\[center\\](.+?)\\[/center\\]", "<div align=\"center\">$1");
		bbMap.put("\\[align=(.+?)\\](.+?)\\[/align\\]", "<div align=\"$1\">$2");
		bbMap.put("\\[color=(.+?)\\](.+?)\\[/color\\]", "<span style=\"color:$1;\">$2</span>");
		bbMap.put("\\[size=(.+?)\\](.+?)\\[/size\\]", "<span style=\"font-size:$1;\">$2</span>");
		bbMap.put("\\[img\\](.+?)\\[/img\\]", "<img src=\"$1\" />");
		bbMap.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]", "<img width=\"$1\" height=\"$2\" src=\"$3\" />");
		bbMap.put("\\[email\\](.+?)\\[/email\\]", "<a href=\"mailto:$1\">$1</a>");
		bbMap.put("\\[email=(.+?)\\](.+?)\\[/email\\]", "<a href=\"mailto:$1\">$2</a>");
		bbMap.put("\\[url\\](.+?)\\[/url\\]", "<a href=\"$1\">$1</a>");
		bbMap.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a href=\"$1\">$2</a>");
		bbMap.put("\\[youtube\\](.+?)\\[/youtube\\]",
				"<object width=\"640\" height=\"380\"><param name=\"movie\" value=\"http://www.youtube.com/v/$1\"></param><embed src=\"http://www.youtube.com/v/$1\" type=\"application/x-shockwave-flash\" width=\"640\" height=\"380\"></embed></object>");
		bbMap.put("\\[video\\](.+?)\\[/video\\]", "<video src=\"$1\" />");

		for (Map.Entry<String, String> entry : bbMap.entrySet()) {
			html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString());
		}

		return html;
	}

	public static String toHtml(String s) {
		StringBuilder builder = new StringBuilder();
		boolean previousWasASpace = false;
		for (char c : s.toCharArray()) {
			if (c == ' ') {
				if (previousWasASpace) {
					builder.append("&nbsp;");
					previousWasASpace = false;
					continue;
				}
				previousWasASpace = true;
			} else {
				previousWasASpace = false;
			}
			switch (c) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '"':
				builder.append("&quot;");
				break;
			case '\n':
				builder.append("<br>");
				break;
			case '\t':
				builder.append("&nbsp;&nbsp;&nbsp;&nbsp;");
				break;
			default:
				if (c < 128) {
					builder.append(c);
				} else {
					builder.append("&#").append((int) c).append(";");
				}
			}
		}
		return builder.toString();
	}

	public static Set<Character> toCharSet(String s1) {
		Set<Character> set1 = Sets.newHashSetWithExpectedSize(50);
		char[] cs1 = s1.toCharArray();
		for (char c : cs1) {
			set1.add(c);
		}
		return set1;
	}

	public static int computeCharDifferSize(String s1, String s2) {
		Set<Character> set1 = toCharSet(s1);
		Set<Character> set2 = toCharSet(s2);

		if (set1.size() > set2.size()) {
			set1.removeAll(set2);
			return set1.size();
		} else {
			set2.removeAll(set1);
			return set2.size();
		}
	}
}
