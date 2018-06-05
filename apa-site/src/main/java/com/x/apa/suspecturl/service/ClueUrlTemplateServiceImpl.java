package com.x.apa.suspecturl.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.suspecturl.dao.ClueUrlTemplateDao;
import com.x.apa.suspecturl.data.ClueUrlTemplate;

import freemarker.cache.TemplateLoader;

/**
 * @author liumeng
 */
@Component
public class ClueUrlTemplateServiceImpl implements ClueUrlTemplateService, TemplateLoader, Constant {

	@Autowired
	private ClueUrlTemplateDao clueUrlTemplateDao;

	/**
	 * @see com.x.apa.suspecturl.service.ClueUrlTemplateService#queryClueUrlTemplate()
	 */
	@Override
	public ClueUrlTemplate queryClueUrlTemplate() {
		return clueUrlTemplateDao.queryClueUrlTemplate(CLUE_URL_TEMPLATE_ID);
	}

	/**
	 * @see com.x.apa.suspecturl.service.ClueUrlTemplateService#updateClueUrlTemplate(com.x.apa.suspecturl.data.ClueUrlTemplate)
	 */
	@Override
	public int updateClueUrlTemplate(ClueUrlTemplate clueUrlTemplate) {
		return clueUrlTemplateDao.updateClueUrlTemplate(clueUrlTemplate);
	}

	/**
	 * @see freemarker.cache.TemplateLoader#findTemplateSource(java.lang.String)
	 */
	@Override
	public Object findTemplateSource(String name) throws IOException {
		Locale locale = Locale.getDefault();
		String languageAndRegion = "_" + locale.getLanguage() + "_" + locale.getCountry();

		String tempalteId = name;
		if (name.endsWith(languageAndRegion)) {
			tempalteId = name.substring(0, name.indexOf(languageAndRegion));
		}
		ClueUrlTemplate template = clueUrlTemplateDao.queryClueUrlTemplate(tempalteId);

		return new StringTemplateSource(name, template.getContent(), template.getUpdateTime().getTime());
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getLastModified(java.lang.Object)
	 */
	@Override
	public long getLastModified(Object templateSource) {
		return ((StringTemplateSource) templateSource).lastModified;
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getReader(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return new StringReader(((StringTemplateSource) templateSource).source);
	}

	/**
	 * @see freemarker.cache.TemplateLoader#closeTemplateSource(java.lang.Object)
	 */
	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		// do nothing
	}

	private static class StringTemplateSource {
		private final String name;
		private final String source;
		private final long lastModified;

		StringTemplateSource(String name, String source, long lastModified) {
			this.name = name;
			this.source = source;
			this.lastModified = lastModified;
		}

		public boolean equals(Object obj) {
			if (obj instanceof StringTemplateSource) {
				return name.equals(((StringTemplateSource) obj).name);
			}
			return false;
		}

		public int hashCode() {
			return name.hashCode();
		}
	}

}
