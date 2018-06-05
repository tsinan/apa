package com.x.apa.webportal.webpage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.x.apa.common.Constant;
import com.x.apa.webportal.service.DashboardService;

/**
 * @author liumeng
 */
@Controller
@RequestMapping(value = "/webpage/apa/dashboard")
public class DashboardWebpageController implements Constant {

	@Autowired
	private DashboardService dashboardService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView toDashboardPage() {

		Model model = new ExtendedModelMap();

		model.addAttribute("suspectUrlPage", "");

		return new ModelAndView("page/dashboard", model.asMap());
	}

	@RequestMapping(value = "/data-count", method = RequestMethod.GET)
	public ModelAndView toDataCountPage() {

		Model model = new ExtendedModelMap();

		List<Map<String, String>> dataCountList = dashboardService.queryDataCountSummary();
		model.addAttribute("dataCountList", dataCountList);

		return new ModelAndView("page/dashboard/data-count", model.asMap());
	}

	@RequestMapping(value = "/crawler-run-status", method = RequestMethod.GET)
	public ModelAndView toCrawlerRunStatusPage() {

		Model model = new ExtendedModelMap();

		List<Map<String, String>> runStatusList = dashboardService.queryCrawlerRunStatus();
		model.addAttribute("runStatusList", runStatusList);

		return new ModelAndView("page/dashboard/crawler-run-status", model.asMap());
	}

	@RequestMapping(value = "/raw-domain-count", method = RequestMethod.GET)
	public ModelAndView toRawDomainCountPage() {

		Model model = new ExtendedModelMap();

		List<Map<String, String>> rawDomainCountList = dashboardService.queryRawDomainCountSummary();
		model.addAttribute("rawDomainCountList", rawDomainCountList);

		return new ModelAndView("page/dashboard/raw-domain-count", model.asMap());
	}

	@RequestMapping(value = "/crawler-latest-run-status", method = RequestMethod.GET)
	public ModelAndView toCrawlerLatestRunStatusPage(@RequestParam String day) {

		Model model = new ExtendedModelMap();

		Map<String, String> runStatusMap = dashboardService.queryCrawlerLatestRunStatus(day);
		model.addAttribute("runStatusMap", runStatusMap);

		return new ModelAndView("page/dashboard/crawler-latest-run-status", model.asMap());
	}

	@RequestMapping(value = "/recent-found-suspect-url", method = RequestMethod.GET)
	public ModelAndView toRecentFoundSuspectUrl() {

		Model model = new ExtendedModelMap();

		int fetchNum = 5;
		List<Map<String, String>> recentFoundList = dashboardService.queryRecentFoundSuspectUrl(fetchNum);
		model.addAttribute("recentFoundList", recentFoundList);

		return new ModelAndView("page/dashboard/recent-found-suspect-url", model.asMap());
	}

	@RequestMapping(value = "/recent-found-suspect-domain", method = RequestMethod.GET)
	public ModelAndView toRecentFoundSuspectDomain() {

		Model model = new ExtendedModelMap();

		int fetchNum = 5;
		List<Map<String, String>> recentFoundList = dashboardService.queryRecentFoundSuspectDomain(fetchNum);
		model.addAttribute("recentFoundList", recentFoundList);

		return new ModelAndView("page/dashboard/recent-found-suspect-domain", model.asMap());
	}

	@RequestMapping(value = "/phishtank-run-status", method = RequestMethod.GET)
	public ModelAndView toPhishtankRunStatusPage() {

		Model model = new ExtendedModelMap();

		List<Map<String, String>> runStatusList = dashboardService.queryPhishtankRunStatus();
		model.addAttribute("runStatusList", runStatusList);

		return new ModelAndView("page/dashboard/phishtank-run-status", model.asMap());
	}

}
