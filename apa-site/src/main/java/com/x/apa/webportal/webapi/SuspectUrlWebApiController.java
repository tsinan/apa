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
import com.x.apa.common.util.CsvUtils;
import com.x.apa.suspecturl.data.PoisonDomain;
import com.x.apa.suspecturl.data.Tag;
import com.x.apa.suspecturl.service.PoisonDomainService;
import com.x.apa.suspecturl.service.SuspectUrlService;
import com.x.apa.suspecturl.service.TagService;

/**
 * @author liumeng
 */
@RestController
@RequestMapping(value = "/webapi/apa/v1")
public class SuspectUrlWebApiController implements Constant {

	@Autowired
	private SuspectUrlService suspectUrlService;

	@Autowired
	private PoisonDomainService poisonDomainService;

	@Autowired
	private TagService tagService;

	@RequestMapping(value = "/suspect-urls/batch-verify", method = RequestMethod.PUT)
	public ResponseEntity<String> changeSuspectUrlVerifyBatch(@RequestParam String suspectUrlIds,
			@RequestParam int verify) {

		for (String suspectUrlId : CsvUtils.convertCSVToList(suspectUrlIds)) {
			suspectUrlService.changeSuspectUrlVerify(suspectUrlId, verify);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/suspect-urls/{suspectId}/verify", method = RequestMethod.PUT)
	public ResponseEntity<String> changeSuspectUrlVerify(@PathVariable String suspectId, @RequestParam int verify) {

		suspectUrlService.changeSuspectUrlVerify(suspectId, verify);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/poison-domains", method = RequestMethod.POST)
	public ResponseEntity<String> createPoisonDomain(PoisonDomain poisonDomain) {
		poisonDomain.setDomainName(poisonDomain.getDomainName().toLowerCase());
		poisonDomain.setCreatorId("");
		poisonDomainService.createPoisonDomain(poisonDomain);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/poison-domains/{poisonDomainId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updatePoisonDomain(@PathVariable String poisonDomainId, PoisonDomain poisonDomain) {
		poisonDomain.setId(poisonDomainId);
		poisonDomain.setCreatorId("");
		poisonDomainService.updatePoisonDomain(poisonDomain);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/poison-domains/{poisonDomainId}/status", method = RequestMethod.PUT)
	public ResponseEntity<String> changePoisonDomainStatus(@PathVariable String poisonDomainId,
			@RequestParam int status) {
		poisonDomainService.changePoisonDomainStatus(poisonDomainId, status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/poison-domains/{poisonDomainId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deletePoisonDomain(@PathVariable String poisonDomainId) {
		poisonDomainService.deletePoisonDomain(poisonDomainId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tags", method = RequestMethod.POST)
	public ResponseEntity<String> createTag(Tag tag) {
		tag.setCreatorId("");
		tagService.createTag(tag);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tags/{tagId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateTag(@PathVariable String tagId, Tag tag) {
		tag.setId(tagId);
		tag.setCreatorId("");
		tagService.updateTag(tag);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tags/{tagId}/status", method = RequestMethod.PUT)
	public ResponseEntity<String> changeTagStatus(@PathVariable String tagId, @RequestParam int status) {
		tagService.changeTagStatus(tagId, status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tags/{tagId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTag(@PathVariable String tagId) {
		tagService.deleteTag(tagId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
