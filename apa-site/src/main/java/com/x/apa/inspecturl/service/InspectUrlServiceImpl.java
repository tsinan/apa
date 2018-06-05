package com.x.apa.inspecturl.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.util.DateUtils;
import com.x.apa.common.util.ExportUtils;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.common.util.JsonHelper;
import com.x.apa.common.util.LuceneUtils;
import com.x.apa.common.util.UrlUtils;
import com.x.apa.inspecturl.dao.BrandDao;
import com.x.apa.inspecturl.dao.InspectEventDao;
import com.x.apa.inspecturl.dao.InspectUrlDao;
import com.x.apa.inspecturl.data.Brand;
import com.x.apa.inspecturl.data.InspectEvent;
import com.x.apa.inspecturl.data.InspectTrace;
import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.inspecturl.vo.InspectUrlQuery;

import edu.uci.ics.crawler4j.parser.HtmlParseData;

/**
 * @author liumeng
 */
@Component
public class InspectUrlServiceImpl implements InspectUrlService, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InspectTraceService inspectTraceService;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private InspectUrlDao inspectUrlDao;

	@Autowired
	private InspectEventDao inspectEventDao;

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#queryInspectUrls(java.util.Date)
	 */
	@Override
	public List<InspectUrl> queryInspectUrlsByNextTime(Date datetime) {
		return inspectUrlDao.queryInspectUrlsByNextTime(datetime);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#queryInspectUrls(boolean)
	 */
	@Override
	public List<InspectUrl> queryInspectUrls(boolean onlyLocked) {
		return inspectUrlDao.queryInspectUrls(onlyLocked);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#queryInspectUrlDomains()
	 */
	@Override
	public Set<String> queryInspectUrlDomains() {
		Set<String> domainSet = Sets.newLinkedHashSet();
		List<InspectUrl> inspectUrlList = inspectUrlDao.queryInspectUrls(new InspectUrlQuery());
		for (InspectUrl inspectUrl : inspectUrlList) {

			// 根据URL计算域名
			String domain = UrlUtils.parseDomain(inspectUrl.getUrl());
			domainSet.add(domain);
		}
		return domainSet;
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#queryInspectUrls(com.x.apa.common.pageable.PageRequest,
	 *      com.x.apa.inspecturl.vo.InspectUrlQuery)
	 */
	@Override
	public Page<InspectUrl> queryInspectUrls(PageRequest page, InspectUrlQuery inspectUrlQuery) {
		Map<String, Brand> brandMap = Maps.newHashMap();

		Page<InspectUrl> inspectUrlPage = inspectUrlDao.queryInspectUrls(page, inspectUrlQuery);
		for (InspectUrl inspectUrl : inspectUrlPage) {
			Brand brand = brandMap.get(inspectUrl.getBrandId());
			if (brand == null) {
				brand = brandDao.queryBrand(inspectUrl.getBrandId());
				brandMap.put(inspectUrl.getBrandId(), brand);
			}
			inspectUrl.setBrandName(brand.getName());
		}
		return inspectUrlPage;
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#exportInpsectUrl(com.x.apa.inspecturl.vo.InspectUrlQuery)
	 */
	@Override
	public HSSFWorkbook exportInpsectUrl(InspectUrlQuery inspectUrlQuery) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("URL");
		sheet.setColumnWidth(0, 50 * 256); // URL
		sheet.setColumnWidth(1, 30 * 256); // brand
		sheet.setColumnWidth(2, 30 * 256); // registrar
		sheet.setColumnWidth(3, 30 * 256); // registrant email
		sheet.setColumnWidth(4, 20 * 256); // createTime

		// 获取标题/表头/数据style
		CellStyle headStyle = ExportUtils.buildHeaderStyle(workbook);
		CellStyle dataStyle = ExportUtils.buildDataStyle(workbook);

		Row headRow = sheet.createRow(0);
		headRow.setRowStyle(headStyle);

		Cell cell0 = headRow.createCell(0);
		cell0.setCellValue("URL");
		cell0.setCellStyle(headStyle);

		Cell cell1 = headRow.createCell(1);
		cell1.setCellValue("BRAND");
		cell1.setCellStyle(headStyle);

		Cell cell2 = headRow.createCell(2);
		cell2.setCellValue("REGISTRAR");
		cell2.setCellStyle(headStyle);

		Cell cell3 = headRow.createCell(3);
		cell3.setCellValue("Registrant-Email");
		cell3.setCellStyle(headStyle);

		Cell cell4 = headRow.createCell(4);
		cell4.setCellValue("CREATE-TIME");
		cell4.setCellStyle(headStyle);

		List<InspectUrl> inspectUrlList = inspectUrlDao.queryInspectUrls(inspectUrlQuery);
		Map<String, Brand> brandMap = Maps.newHashMap();
		int i = 1;
		for (InspectUrl inspectUrl : inspectUrlList) {
			// 设置第一行汇总数据
			Row numRow = sheet.createRow(i);

			cell0 = numRow.createCell(0);
			cell0.setCellValue(inspectUrl.getUrl());
			cell0.setCellStyle(dataStyle);

			Brand brand = brandMap.get(inspectUrl.getBrandId());
			if (brand == null) {
				brand = brandDao.queryBrand(inspectUrl.getBrandId());
				brandMap.put(inspectUrl.getBrandId(), brand);
			}
			cell1 = numRow.createCell(1);
			cell1.setCellValue(brand.getName());
			cell1.setCellStyle(dataStyle);


			cell2 = numRow.createCell(2);
			cell2.setCellValue(inspectUrl.getWhoisRegName());
			cell2.setCellStyle(dataStyle);

			cell3 = numRow.createCell(3);
			cell3.setCellValue(inspectUrl.getWhoisRegEmail());
			cell3.setCellStyle(dataStyle);
			
			cell4 = numRow.createCell(4);
			cell4.setCellValue(DateUtils.toString_YYYY_MM_DD(inspectUrl.getCreateTime()));
			cell4.setCellStyle(dataStyle);

			i++;
		}
		return workbook;
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#queryInspectUrl(java.lang.String)
	 */
	@Override
	public InspectUrl queryInspectUrl(String id) {
		return inspectUrlDao.queryInspectUrl(id);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#changeInspectUrlLock(java.lang.String)
	 */
	@Override
	public int changeInspectUrlLock(String id, boolean isLock) {
		return inspectUrlDao.changeInspectUrlLock(id, isLock);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#createInspectUrl(com.x.apa.inspecturl.data.InspectUrl)
	 */
	@Override
	public InspectUrl createInspectUrl(InspectUrl inspectUrl) {

		// 检查是否已存在
		InspectUrl exsitInspectUrl = inspectUrlDao.queryInspectUrlByUrl(inspectUrl.getUrl());
		if (StringUtils.isNotBlank(exsitInspectUrl.getId())) {
			return exsitInspectUrl;
		}

		// 添加新巡检URL，默认进行一次巡检
		inspectUrl.setIpAddress("");
		inspectUrl.setInspectNextTime(new Date());
		inspectUrl.setInspectKeywordScore(0f);
		inspectUrl.setInspectMessage("");
		return inspectUrlDao.saveInspectUrl(inspectUrl);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#updateInspectUrl(com.x.apa.inspecturl.data.InspectUrl)
	 */
	@Override
	public int updateInspectUrl(InspectUrl inspectUrl) {

		// 从库内查询最新状态
		InspectUrl existUrl = inspectUrlDao.queryInspectUrl(inspectUrl.getId());
		if (StringUtils.isBlank(existUrl.getId())) {
			return 0;
		}

		// 如果关键词和客户发生变化，设置当前时间为下次执行时间
		if (!StringUtils.equals(inspectUrl.getInspectKeyword(), existUrl.getInspectKeyword())) {
			inspectUrl.setInspectNextTime(new Date());
		} else if (!StringUtils.equals(inspectUrl.getBrandId(), existUrl.getBrandId())) {
			inspectUrl.setInspectNextTime(new Date());
		} else {
			inspectUrl.setInspectNextTime(existUrl.getInspectNextTime());
		}

		// 如果从非例行巡检改变为例行巡检，将状态设置为INIT
		if (inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_LEVEL_ROUTINE
				&& existUrl.getInspectStatus() != INSPECT_URL_INSPECT_LEVEL_ROUTINE) {
			inspectUrl.setInspectStatus(INSPECT_URL_INSPECT_STATUS_INIT);
		}
		return inspectUrlDao.updateInspectUrl(inspectUrl);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#updateInpsectUrlWhois(com.x.apa.inspecturl.data.InspectUrl)
	 */
	@Override
	public int updateInpsectUrlWhois(InspectUrl inspectUrl) {
		return inspectUrlDao.updateInpsectUrlWhois(inspectUrl);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#updateInspectUrlStatus(com.x.apa.inspecturl.data.InspectUrl,
	 *      int, org.apache.http.Header[], java.util.List)
	 */
	@Override
	public void updateInspectUrlStatus(InspectUrl inspectUrl, int statusCode, Header[] headers,
			List<HtmlParseData> htmlDataList) {

		String fullHtml = "";
		String fullText = "";

		int inspectStatus = INSPECT_URL_INSPECT_STATUS_INIT;
		String keyword = inspectUrl.getInspectKeyword();
		float score = 0.0f;
		Date inspectTime = new Date();

		// 内容关键字识别
		String message = "";
		if (statusCode == -1) {
			inspectStatus = INSPECT_URL_INSPECT_STATUS_INACTIVE;
			message = "NO DNS FOUND";
		} else if (statusCode < 200 || statusCode > 299) {
			// 内容没有，inactive
			inspectStatus = INSPECT_URL_INSPECT_STATUS_INACTIVE;
			message = "NO CONTENT, STATUS=" + statusCode;
		} else {
			// 解析页面结构
			StringBuilder htmlBuilder = new StringBuilder();
			StringBuilder textBuilder = new StringBuilder();
			for (HtmlParseData htmlParseData : htmlDataList) {
				htmlBuilder.append(htmlParseData.getHtml());

				String text = htmlParseData.getText();
				text = text.replaceAll("[^\\u4e00-\\u9fa5\\n\\r]", ""); // 去掉非汉字和空格
				text = text.replaceAll("[\\r\\n]", " ");// 换行替换为空格
				textBuilder.append(text).append("\n");
			}
			fullHtml = htmlBuilder.toString();
			fullText = textBuilder.toString();

			if (StringUtils.isBlank(keyword)) {
				// 提取关键字
				keyword = LuceneUtils.getKeyword(fullText);
				if (StringUtils.isNotBlank(keyword)) {
					inspectStatus = INSPECT_URL_INSPECT_STATUS_ACTIVE;
					message = "EXTRACT KEYWORD: " + keyword;

					// 内容、关键词都有，进行内容匹配
					MemoryIndex index = new MemoryIndex();
					Analyzer analyzer = LuceneUtils.getAnalyzer();
					index.addField("content", fullText, analyzer);

					// 计算初次score
					QueryParser parser = new QueryParser("content", analyzer);
					try {
						score = index.search(parser.parse(keyword));
					} catch (ParseException e) {
						logger.warn("inspect match failed for url " + inspectUrl.getUrl() + " keyword "
								+ inspectUrl.getInspectKeyword() + ".", e);
					}

				} else {
					inspectStatus = INSPECT_URL_INSPECT_STATUS_INACTIVE;
					message = "EXTRACT KEYWORD FAILED";
				}
			} else {
				// 内容、关键词都有，进行内容匹配
				MemoryIndex index = new MemoryIndex();
				Analyzer analyzer = LuceneUtils.getAnalyzer();
				index.addField("content", fullText, analyzer);

				QueryParser parser = new QueryParser("content", analyzer);
				try {
					score = index.search(parser.parse(inspectUrl.getInspectKeyword()));
				} catch (ParseException e) {
					logger.warn("inspect match failed for url " + inspectUrl.getUrl() + " keyword "
							+ inspectUrl.getInspectKeyword() + ".", e);
				}
				if (score > 0.0f) {
					switch (inspectUrl.getInspectStatus()) {
					case INSPECT_URL_INSPECT_STATUS_INIT:
					case INSPECT_URL_INSPECT_STATUS_INACTIVE:
						inspectStatus = INSPECT_URL_INSPECT_STATUS_ACTIVE;
						message = "ACTIVE";
						break;
					case INSPECT_URL_INSPECT_STATUS_ACTIVE:
					case INSPECT_URL_INSPECT_STATUS_CHANGED:
						if (score != inspectUrl.getInspectKeywordScore()) {
							inspectStatus = INSPECT_URL_INSPECT_STATUS_CHANGED;
							message = "CONTENT CHANGED";
						} else {
							inspectStatus = INSPECT_URL_INSPECT_STATUS_ACTIVE;
							message = "NO CHANGED";
						}
						break;
					}
				} else {
					inspectStatus = INSPECT_URL_INSPECT_STATUS_INACTIVE;
					message = "KEYWORD NO SCORE";
				}
			}
		}

		// 判断前次状态是否与当前一致
		if (needSendEvent(inspectUrl, inspectStatus)) {

			// 发出告警
			InspectEvent inspectEvent = new InspectEvent();
			if (inspectStatus == INSPECT_URL_INSPECT_STATUS_CHANGED) {
				inspectEvent.setCategory(INSPECT_EVENT_CATEGORY_CONTENTCHANGE);
			} else {
				inspectEvent.setCategory(INSPECT_EVENT_CATEGORY_STATUSCHANGE);
			}
			inspectEvent.setUrlId(inspectUrl.getId());
			inspectEvent.setUrl(inspectUrl.getUrl());
			inspectEvent.setKeywordFrom(inspectUrl.getInspectKeyword());
			inspectEvent.setKeywordTo(keyword);
			inspectEvent.setKeywordScoreFrom(inspectUrl.getInspectKeywordScore());
			inspectEvent.setKeywordScoreTo(score);
			inspectEvent.setMessageFrom(inspectUrl.getInspectMessage());
			inspectEvent.setMessageTo(message);
			inspectEvent.setStatusFrom(inspectUrl.getInspectStatus());
			inspectEvent.setStatusTo(inspectStatus);
			inspectEvent.setTimeFrom(inspectUrl.getInspectTime());
			inspectEvent.setTimeTo(inspectTime);
			inspectEvent.setProgress(INSPECT_EVENT_PROGRESS_INIT);
			inspectEventDao.saveInspectEvent(inspectEvent);
		}

		// 计算在线时长
		if (inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_STATUS_ACTIVE
				&& inspectStatus == INSPECT_URL_INSPECT_STATUS_ACTIVE) {
			if (inspectUrl.getInspectTime() != null) {
				int interval = (int) (inspectTime.getTime() - inspectUrl.getInspectTime().getTime()) / 1000;
				if (interval >= 0) {
					inspectUrl.setActiveDuration(inspectUrl.getActiveDuration() + interval);
				}
			} else {
				inspectUrl.setActiveDuration(0);
			}
		}
		inspectUrl.setInspectStatus(inspectStatus);
		inspectUrl.setInspectKeyword(keyword);
		inspectUrl.setInspectKeywordScore(score);
		inspectUrl.setInspectMessage(message);
		inspectUrl.setInspectTime(inspectTime);

		// 计算下次巡检时间，如果不是DNS问题，也不是内容关键字不匹配，那就加速巡检
		int delta = computeInspectNextTime(inspectUrl, statusCode);
		inspectUrl.setInspectNextTime(new Date(inspectUrl.getInspectTime().getTime() + delta));
		inspectUrl.setInspectTimes(inspectUrl.getInspectTimes() + 1);

		inspectUrlDao.updateInspectUrlStatus(inspectUrl);

		// 记录跟踪信息
		InspectTrace inspectTrace = new InspectTrace();
		BeanUtils.copyProperties(inspectUrl, inspectTrace);
		inspectTrace.setUrlId(inspectUrl.getId());
		if (CollectionUtils.isNotEmpty(htmlDataList)) {

			inspectTrace.setHttpStatusCode(statusCode);
			if (headers != null) {
				String httpHeaders = JsonHelper.serialize(headers);
				inspectTrace.setHttpHeaders(httpHeaders);
			}
			inspectTrace.setHtml(fullHtml);
			inspectTrace.setText(fullText);
			inspectTrace.setTextTokens(LuceneUtils.getTokens(fullText, false));

			HtmlParseData htmlParseData = htmlDataList.get(0);
			Map<String, Object> textOtherMap = Maps.newLinkedHashMap();
			textOtherMap.put("title", htmlParseData.getTitle());
			textOtherMap.put("metaTags", htmlParseData.getMetaTags());
			textOtherMap.put("outgoings", htmlParseData.getOutgoingUrls());
			String textOther = JsonHelper.serialize(textOtherMap);
			inspectTrace.setTextOther(textOther);
		}
		inspectTraceService.createInspectTrace(inspectTrace);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#changeInspectUrlLevel(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeInspectUrlLevel(String inspectUrlId, int inspectLevel) {
		// 从库内查询最新状态
		InspectUrl existUrl = inspectUrlDao.queryInspectUrl(inspectUrlId);
		if (StringUtils.isBlank(existUrl.getId())) {
			return 0;
		}

		// 如果从非例行巡检改变为例行巡检，将状态设置为INIT
		int inspectStatus = existUrl.getInspectStatus();
		if (inspectLevel == INSPECT_URL_INSPECT_LEVEL_ROUTINE
				&& existUrl.getInspectStatus() != INSPECT_URL_INSPECT_LEVEL_ROUTINE) {
			inspectStatus = INSPECT_URL_INSPECT_STATUS_INIT;
		}
		return inspectUrlDao.changeInspectUrlLevel(inspectUrlId, inspectLevel, inspectStatus);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectUrlService#deleteInspectUrl(java.lang.String)
	 */
	@Override
	public int deleteInspectUrl(String id) {
		return inspectUrlDao.deleteInspectUrl(id);
	}

	private boolean needSendEvent(InspectUrl inspectUrl, int inspectStatus) {
		if (inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_STATUS_ACTIVE
				&& (inspectStatus == INSPECT_URL_INSPECT_STATUS_CHANGED
						|| inspectStatus == INSPECT_URL_INSPECT_STATUS_INACTIVE)) {
			return true;
		} else if (inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_STATUS_CHANGED
				&& inspectStatus == INSPECT_URL_INSPECT_STATUS_INACTIVE) {
			return true;
		} else if (inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_STATUS_INACTIVE
				&& inspectStatus == INSPECT_URL_INSPECT_STATUS_ACTIVE) {
			return true;
		} else {
			return false;
		}
	}

	private int computeInspectNextTime(InspectUrl inspectUrl, int statusCode) {
		int delta = 0;

		// 基本时间单位
		int interval = Integer.parseInt(HotPropertiesAccessor.getProperty("pu.inspect.next.interval.minute"));

		// 如果要求时间加速，则范围基本时间单位的1/3
		if (statusCode != -1 && (statusCode < 200 || statusCode > 299)) {
			return (interval / 3) * 60 * 1000;
		}

		// 根据修改后的客户类型，当前url状态和上次巡检时间计算下次巡检时间
		int brandCategory = INSPECT_URL_BRAND_CATEGORY_NO_CLIENT;
		Brand brand = brandDao.queryBrand(inspectUrl.getBrandId());
		if (StringUtils.isNotBlank(brand.getId())) {
			brandCategory = brand.getCategory();
		}

		// 客户或者潜在客户
		if ((brandCategory == INSPECT_URL_BRAND_CATEGORY_CLIENT
				|| brandCategory == INSPECT_URL_BRAND_CATEGORY_POC_CLIENT)) {

			// 在线
			if (inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_STATUS_ACTIVE
					|| inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_STATUS_CHANGED) {
				// 基本单位一次
				delta = interval * 60 * 1000;
			} else {
				// 三倍基本单位一次
				delta = 3 * interval * 60 * 1000;
			}

		} else {
			// 非客户，在线
			if (inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_STATUS_ACTIVE
					|| inspectUrl.getInspectStatus() == INSPECT_URL_INSPECT_STATUS_CHANGED) {
				// 六倍基本单位一次
				delta = 6 * interval * 60 * 1000;
			} else {
				// 十二倍基本单位一次
				delta = 12 * interval * 60 * 1000;
			}
		}
		return delta;
	}

}
