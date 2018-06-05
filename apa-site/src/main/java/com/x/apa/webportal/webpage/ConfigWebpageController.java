package com.x.apa.webportal.webpage;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.x.apa.common.Constant;
import com.x.apa.common.metric.MetricCenter;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.pageable.Sort;
import com.x.apa.inspecturl.data.Brand;
import com.x.apa.inspecturl.service.BrandService;
import com.x.apa.loader.data.DomainSensitiveWord;
import com.x.apa.loader.data.ThreatActor;
import com.x.apa.loader.service.DomainSensitiveWordService;
import com.x.apa.loader.service.ThreatActorService;
import com.x.apa.suspectdomain.data.DomainRegular;
import com.x.apa.suspectdomain.service.DomainRegularService;
import com.x.apa.suspecturl.data.ClueUrlTemplate;
import com.x.apa.suspecturl.data.PoisonDomain;
import com.x.apa.suspecturl.data.Tag;
import com.x.apa.suspecturl.data.TrustDomain;
import com.x.apa.suspecturl.service.ClueUrlTemplateService;
import com.x.apa.suspecturl.service.PoisonDomainService;
import com.x.apa.suspecturl.service.TagService;
import com.x.apa.suspecturl.service.TrustDomainService;

/**
 * @author liumeng
 */
@Controller
@RequestMapping(value = "/webpage/apa")
public class ConfigWebpageController implements Constant {

	@Autowired
	private BrandService brandService;

	@Autowired
	private TagService tagService;

	@Autowired
	private ClueUrlTemplateService clueUrlTemplateService;

	@Autowired
	private TrustDomainService trustDomainService;

	@Autowired
	private PoisonDomainService poisonDomainService;

	@Autowired
	private DomainRegularService domainRegularService;

	@Autowired
	private DomainSensitiveWordService domainSensitiveWordService;
	
	@Autowired
	private ThreatActorService threatActorService;

	@RequestMapping(value = "/brand", method = RequestMethod.GET)
	public ModelAndView toBrandPage(
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.ASC, "name");
		Page<Brand> brandPage = brandService.queryBrands(page);
		model.addAttribute("brandPage", brandPage);

		return new ModelAndView("page/config/brand", model.asMap());
	}

	@RequestMapping(value = "/url-crawling", method = RequestMethod.GET)
	public ModelAndView toUrlCrawlingPage(
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		ClueUrlTemplate urlTemplate = clueUrlTemplateService.queryClueUrlTemplate();
		model.addAttribute("urlTemplate", urlTemplate);

		List<TypedTuple<String>> urlVisitMetric = MetricCenter.topUrlVisit();
		model.addAttribute("urlVisitMetric", urlVisitMetric);

		Set<TypedTuple<String>> urlVisitSizeMetric = MetricCenter.topUrlVisitSize();
		model.addAttribute("urlVisitSizeMetric", urlVisitSizeMetric);

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.ASC,
				"domain_name");
		Page<TrustDomain> trustDomainPage = trustDomainService.queryTrustDomains(page);
		model.addAttribute("trustDomainPage", trustDomainPage);

		return new ModelAndView("page/config/url_crawling", model.asMap());
	}

	@RequestMapping(value = "/content-keyword", method = RequestMethod.GET)
	public ModelAndView toContentKeywordPage(
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		Map<Object, Object> metric = MetricCenter.getSuspectUrlMatchMetric();
		model.addAttribute("metric", metric);

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.DESC,
				"create_time");
		Page<Tag> tagPage = tagService.queryTags(page);
		model.addAttribute("tagPage", tagPage);

		return new ModelAndView("page/config/content_keyword", model.asMap());
	}

	@RequestMapping(value = "/content-poison", method = RequestMethod.GET)
	public ModelAndView toContentPoisonPage(
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

		Model model = new ExtendedModelMap();

		Map<Object, Object> metric = MetricCenter.getSuspectUrlMatchMetric();
		model.addAttribute("metric", metric);

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.DESC,
				"create_time");
		Page<PoisonDomain> poisonDomainPage = poisonDomainService.queryPoisonDomains(page);
		model.addAttribute("poisonDomainPage", poisonDomainPage);

		return new ModelAndView("page/config/content_poison", model.asMap());
	}

	@RequestMapping(value = "/domain-regular", method = RequestMethod.GET)
	public ModelAndView toDomainRegularPage(
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		Model model = new ExtendedModelMap();

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.DESC,
				"create_time");
		Page<DomainRegular> domainRegularPage = domainRegularService.queryDomainRegulars(page);
		model.addAttribute("domainRegularPage", domainRegularPage);

		return new ModelAndView("page/config/domain_regular", model.asMap());
	}

	@RequestMapping(value = "/domain-sensitive-word", method = RequestMethod.GET)
	public ModelAndView toDomainSensitiveWordPage(
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		Model model = new ExtendedModelMap();

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.DESC,
				"create_time");
		Page<DomainSensitiveWord> domainSensitiveWordPage = domainSensitiveWordService.queryDomainSensitiveWords(page);
		model.addAttribute("domainSensitiveWordPage", domainSensitiveWordPage);

		return new ModelAndView("page/config/domain_sensitive_word", model.asMap());
	}

	@RequestMapping(value = "/domain-threat-actor", method = RequestMethod.GET)
	public ModelAndView toWhoisPage(
			@RequestParam(value = "page.page", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		Model model = new ExtendedModelMap();

		PageRequest page = new PageRequest(Integer.valueOf(pageNumber) - 1, pageSize, Sort.Direction.DESC,
				"create_time");
		Page<ThreatActor> threatActorPage = threatActorService.queryThreatActors(page);
		model.addAttribute("threatActorPage", threatActorPage);

		return new ModelAndView("page/config/domain_threat_actor", model.asMap());
	}

}
