package com.x.apa.webportal.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.x.apa.common.Constant;
import com.x.apa.suspecturl.data.ClueUrlTemplate;
import com.x.apa.suspecturl.data.TrustDomain;
import com.x.apa.suspecturl.service.ClueUrlTemplateService;
import com.x.apa.suspecturl.service.TrustDomainService;

/**
 * @author liumeng
 */
@RestController
@RequestMapping(value = "/webapi/apa/v1")
public class UrlCrawlingWebApiController implements Constant {

	@Autowired
	private ClueUrlTemplateService clueUrlTemplateService;

	@Autowired
	private TrustDomainService trustDomainService;

	@RequestMapping(value = "/clue-url-templates", method = RequestMethod.PUT)
	public ResponseEntity<String> updateUrlTemplate(ClueUrlTemplate clueUrlTemplate) {
		clueUrlTemplate.setId(CLUE_URL_TEMPLATE_ID);
		clueUrlTemplateService.updateClueUrlTemplate(clueUrlTemplate);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/trust-domains", method = RequestMethod.POST)
	public ResponseEntity<String> createTrustDomain(TrustDomain trustDomain) {
		trustDomain.setDomainName(trustDomain.getDomainName().toLowerCase());
		trustDomain.setCreatorId("");
		trustDomainService.createTrustDomain(trustDomain);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/trust-domains/{trustDomainId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateTrustDomain(@PathVariable String trustDomainId, TrustDomain trustDomain) {
		trustDomain.setId(trustDomainId);
		trustDomain.setCreatorId("");
		trustDomainService.updateTrustDomain(trustDomain);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/trust-domains/{trustDomainId}/status", method = RequestMethod.PUT)
	public ResponseEntity<String> changeTrustDomainStatus(@PathVariable String trustDomainId,
			@RequestParam int status) {
		trustDomainService.changeTrustDomainStatus(trustDomainId, status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/trust-domains/{trustDomainId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTrustDomain(@PathVariable String trustDomainId) {
		trustDomainService.deleteTrustDomain(trustDomainId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
