package com.x.apa.webportal.webpage;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.pageable.Sort;
import com.x.apa.common.util.ExportUtils;
import com.x.apa.inspecturl.data.Brand;
import com.x.apa.inspecturl.data.InspectEvent;
import com.x.apa.inspecturl.data.InspectTrace;
import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.inspecturl.service.BrandService;
import com.x.apa.inspecturl.service.InspectEventService;
import com.x.apa.inspecturl.service.InspectTraceService;
import com.x.apa.inspecturl.service.InspectUrlService;
import com.x.apa.inspecturl.vo.InspectUrlQuery;

/**
 * @author liumeng
 */
@Controller
@RequestMapping(value = "/webpage/apa")
public class InspectWebpageController implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BrandService brandService;

	@Autowired
	private InspectEventService inspectEventService;

	@Autowired
	private InspectTraceService inspectTraceService;

	@Autowired
	private InspectUrlService inspectUrlService;

	@RequestMapping(value = "/inspect-event", method = RequestMethod.GET)
	public ModelAndView toInspectEventPage(@RequestParam(required = false, defaultValue = "") String progress,
			@RequestParam(required = false, defaultValue = "") String urlLike,
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		Sort sort = new Sort(Sort.Direction.DESC, "create_time").and(new Sort(Sort.Direction.ASC, "url"));
		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, sort);
		Page<InspectEvent> inspectEventPage = inspectEventService.queryInspectEvents(page, progress, urlLike);
		model.addAttribute("inspectEventPage", inspectEventPage);

		model.addAttribute("progress", progress);
		model.addAttribute("urlLike", urlLike);
		return new ModelAndView("page/inspect_event", model.asMap());
	}

	@RequestMapping(value = "/inspect-trace", method = RequestMethod.GET)
	public ModelAndView toInspectTracePage(@RequestParam String inspectUrlId,
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		InspectUrl inspectUrl = inspectUrlService.queryInspectUrl(inspectUrlId);
		model.addAttribute("inspectUrl", inspectUrl);

		Sort sort = new Sort(Sort.Direction.DESC, "create_time");
		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, sort);
		Page<InspectTrace> inspectTracePage = inspectTraceService.queryInspectTraces(page, inspectUrlId);
		model.addAttribute("inspectTracePage", inspectTracePage);

		model.addAttribute("inspectUrlId", inspectUrlId);
		return new ModelAndView("page/inspect_trace", model.asMap());
	}

	@RequestMapping(value = "/inspect-url", method = RequestMethod.GET)
	public ModelAndView toInspectUrlPage(InspectUrlQuery inspectUrlQuery,
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		List<Brand> brandList = brandService.queryBrands();
		model.addAttribute("brandList", brandList);

		Sort sort = new Sort(Sort.Direction.DESC, "inspect_level").and(new Sort(Sort.Direction.DESC, "create_time"))
				.and(new Sort(Sort.Direction.ASC, "url"));
		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, sort);
		Page<InspectUrl> inspectUrlPage = inspectUrlService.queryInspectUrls(page, inspectUrlQuery);
		model.addAttribute("inspectUrlPage", inspectUrlPage);

		model.addAttribute("inspectLevel", StringUtils.defaultIfBlank(inspectUrlQuery.getInspectLevel(), ""));
		model.addAttribute("inspectStatus", StringUtils.defaultIfBlank(inspectUrlQuery.getInspectStatus(), ""));
		model.addAttribute("category", StringUtils.defaultIfBlank(inspectUrlQuery.getCategory(), ""));
		model.addAttribute("brandId", StringUtils.defaultIfBlank(inspectUrlQuery.getBrandId(), ""));
		model.addAttribute("urlLike", StringUtils.defaultIfBlank(inspectUrlQuery.getUrlLike(), ""));
		model.addAttribute("startTime", StringUtils.defaultIfBlank(inspectUrlQuery.getStartTime(), ""));
		model.addAttribute("endTime", StringUtils.defaultIfBlank(inspectUrlQuery.getEndTime(), ""));
		return new ModelAndView("page/inspect_url", model.asMap());
	}

	@RequestMapping(value = "/inspect-url/export", method = RequestMethod.GET)
	public void exportInspectUrlPage(HttpServletRequest request, HttpServletResponse response,
			InspectUrlQuery inspectUrlQuery) {

		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 设置导出文件格式
		response.setHeader("Content-Disposition",
				"attachment; filename=" + ExportUtils.fileNameToUtf8String(request, "巡检URL清单.xls"));
		response.resetBuffer();
		HSSFWorkbook workbook = inspectUrlService.exportInpsectUrl(inspectUrlQuery);

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			workbook.write(out);
			response.flushBuffer();
			out.close();
		} catch (Exception e) {
			logger.warn("export inspect url failed.", e);
		}
	}

}
