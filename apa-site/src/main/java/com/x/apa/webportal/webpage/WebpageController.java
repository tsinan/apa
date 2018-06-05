package com.x.apa.webportal.webpage;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.x.apa.common.Constant;
import com.x.apa.common.data.RawDomain;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.pageable.Sort;
import com.x.apa.common.util.ExportUtils;
import com.x.apa.inspecturl.data.Brand;
import com.x.apa.inspecturl.service.BrandService;
import com.x.apa.loader.data.SensitiveDomain;
import com.x.apa.loader.service.RawDomainService;
import com.x.apa.loader.service.SensitiveDomainService;
import com.x.apa.phishtankurl.data.PhishtankUrl;
import com.x.apa.phishtankurl.service.PhishtankUrlService;
import com.x.apa.suspecturl.data.SuspectUrl;
import com.x.apa.suspecturl.data.Tag;
import com.x.apa.suspecturl.service.SuspectUrlService;
import com.x.apa.suspecturl.service.TagService;

/**
 * @author liumeng
 */
@Controller
@RequestMapping(value = "/webpage/apa")
public class WebpageController implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BrandService brandService;

	@Autowired
	private SuspectUrlService suspectUrlService;

	@Autowired
	private TagService tagService;

	@Autowired
	private RawDomainService rawDomainService;

	@Autowired
	private SensitiveDomainService sensitiveDomainService;

	@Autowired
	private PhishtankUrlService phishtankUrlService;

	@RequestMapping(value = "/suspect-url", method = RequestMethod.GET)
	public ModelAndView toSuspectUrlPage(@RequestParam(required = false, defaultValue = "") String tagId,
			@RequestParam(required = false, defaultValue = "") String urlLike,
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		List<Tag> tagList = tagService.queryTags();
		model.addAttribute("tagList", tagList);

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.DESC,
				"create_time");
		Page<SuspectUrl> suspectUrlPage = suspectUrlService.querySuspectUrls(page, tagId, urlLike);
		model.addAttribute("suspectUrlPage", suspectUrlPage);

		model.addAttribute("tagId", tagId);
		model.addAttribute("urlLike", urlLike);
		return new ModelAndView("page/suspect_url", model.asMap());
	}

	@RequestMapping(value = "/suspect-url/{suspectUrlId}/confirm", method = RequestMethod.GET)
	public ModelAndView toSuspectUrlConfirmPage(@PathVariable String suspectUrlId) {

		Model model = new ExtendedModelMap();

		List<Brand> brandList = brandService.queryBrands();
		model.addAttribute("brandList", brandList);

		SuspectUrl suspectUrl = suspectUrlService.querySuspectUrl(suspectUrlId);
		model.addAttribute("suspectUrl", suspectUrl);

		model.addAttribute("suspectUrlId", suspectUrlId);
		return new ModelAndView("page/suspect_url_confirm", model.asMap());
	}

	@RequestMapping(value = "/sensitive-domain", method = RequestMethod.GET)
	public ModelAndView toSensitiveDomainPage(@RequestParam(required = false, defaultValue = "") String domainLike,
			@RequestParam(required = false, defaultValue = "") String startTime,
			@RequestParam(required = false, defaultValue = "") String endTime,
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.DESC,
				"create_time");
		Page<SensitiveDomain> sensitiveDomainPage = sensitiveDomainService.querySensitiveDomains(page, startTime,
				endTime, domainLike);
		model.addAttribute("sensitiveDomainPage", sensitiveDomainPage);

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("domainLike", domainLike);
		return new ModelAndView("page/sensitive_domain", model.asMap());
	}

	@RequestMapping(value = "/sensitive-domain/export", method = RequestMethod.GET)
	public void exportSensitiveDomainPage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "") String startTime,
			@RequestParam(required = false, defaultValue = "") String endTime,
			@RequestParam(required = false, defaultValue = "") String domainLike) {

		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 设置导出文件格式
		response.setHeader("Content-Disposition",
				"attachment; filename=" + ExportUtils.fileNameToUtf8String(request, "敏感域名清单.xls"));
		response.resetBuffer();
		HSSFWorkbook workbook = sensitiveDomainService.exportSensitiveDomain(startTime, endTime, domainLike);

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			workbook.write(out);
			response.flushBuffer();
			out.close();
		} catch (Exception e) {
			logger.warn("export sensitive domain failed.", e);
		}
	}

	@RequestMapping(value = "/newly-domain", method = RequestMethod.GET)
	public ModelAndView toNewlyDomainPage(@RequestParam(required = false, defaultValue = "") String domainLike,
			@RequestParam(required = false, defaultValue = "") String startTime,
			@RequestParam(required = false, defaultValue = "") String endTime,
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.DESC,
				"create_time");
		Page<RawDomain> rawDomainPage = rawDomainService.queryRawDomains(page, startTime, endTime, domainLike);
		model.addAttribute("rawDomainPage", rawDomainPage);

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("domainLike", domainLike);
		return new ModelAndView("page/newly_domain", model.asMap());
	}

	@RequestMapping(value = "/newly-domain/export", method = RequestMethod.GET)
	public void exportNewlyDomainPage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "") String startTime,
			@RequestParam(required = false, defaultValue = "") String endTime,
			@RequestParam(required = false, defaultValue = "") String domainLike) {

		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 设置导出文件格式
		response.setHeader("Content-Disposition",
				"attachment; filename=" + ExportUtils.fileNameToUtf8String(request, "域名清单.xls"));
		response.resetBuffer();
		HSSFWorkbook workbook = rawDomainService.exportRawDomain(startTime, endTime, domainLike);

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			workbook.write(out);
			response.flushBuffer();
			out.close();
		} catch (Exception e) {
			logger.warn("export raw domain failed.", e);
		}
	}

	@RequestMapping(value = "/phishtank", method = RequestMethod.GET)
	public ModelAndView toPhishtankPage(
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		Sort sort = new Sort(Sort.Direction.DESC, "submission_time")
				.and(new Sort(Sort.Direction.DESC, "verification_time"));
		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, sort);
		Page<PhishtankUrl> phishtankUrlPage = phishtankUrlService.queryPhishtankUrls(page);
		model.addAttribute("phishtankUrlPage", phishtankUrlPage);

		return new ModelAndView("page/other/phishtank", model.asMap());
	}

}
