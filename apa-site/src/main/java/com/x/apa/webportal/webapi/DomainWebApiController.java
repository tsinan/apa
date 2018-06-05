package com.x.apa.webportal.webapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.x.apa.common.util.CsvUtils;
import com.x.apa.loader.data.DomainSensitiveWord;
import com.x.apa.loader.data.SensitiveDomain;
import com.x.apa.loader.data.ThreatActor;
import com.x.apa.loader.service.DomainSensitiveWordService;
import com.x.apa.loader.service.SensitiveDomainService;
import com.x.apa.loader.service.ThreatActorService;
import com.x.apa.suspectdomain.data.DomainRegular;
import com.x.apa.suspectdomain.service.DomainRegularService;

/**
 * @author liumeng
 */
@RestController
@RequestMapping(value = "/webapi/apa/v1")
public class DomainWebApiController {

	@Autowired
	private DomainRegularService domainRegularService;

	@Autowired
	private DomainSensitiveWordService domainSensitiveWordService;

	@Autowired
	private ThreatActorService threatActorService;

	@Autowired
	private SensitiveDomainService sensitiveDomainService;

	@RequestMapping(value = "/domain-regulars", method = RequestMethod.POST)
	public ResponseEntity<String> createDomainRegular(DomainRegular domainRegular) {
		domainRegular.setCreatorId("");
		domainRegular.setDescription("");
		domainRegularService.createDomainRegular(domainRegular);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-regulars/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateDomainRegular(@PathVariable String id, DomainRegular domainRegular) {
		domainRegular.setId(id);
		domainRegular.setCreatorId("");
		domainRegular.setDescription("");
		domainRegularService.updateDomainRegular(domainRegular);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-regulars/{id}/status", method = RequestMethod.PUT)
	public ResponseEntity<String> changeDomainRegularStatus(@PathVariable String id, @RequestParam int status) {
		domainRegularService.changeDomainRegularStatus(id, status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-regulars/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteDomainRegular(@PathVariable String id) {
		domainRegularService.deleteDomainRegular(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-sensitive-words", method = RequestMethod.POST)
	public ResponseEntity<String> createDomainSensitiveWord(DomainSensitiveWord domainSensitiveWord) {
		domainSensitiveWord.setCreatorId("");
		domainSensitiveWord.setDescription("");
		domainSensitiveWordService.createDomainSensitiveWord(domainSensitiveWord);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-sensitive-words/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateDomainSensitiveWord(@PathVariable String id,
			DomainSensitiveWord domainSensitiveWord) {
		domainSensitiveWord.setId(id);
		domainSensitiveWord.setCreatorId("");
		domainSensitiveWord.setDescription("");
		domainSensitiveWordService.updateDomainSensitiveWord(domainSensitiveWord);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-sensitive-words/{id}/status", method = RequestMethod.PUT)
	public ResponseEntity<String> changeDomainSensitiveWordStatus(@PathVariable String id, @RequestParam int status) {
		domainSensitiveWordService.changeDomainSensitiveWordStatus(id, status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-sensitive-words/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteDomainSensitiveWord(@PathVariable String id) {
		domainSensitiveWordService.deleteDomainSensitiveWord(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-threat-actors", method = RequestMethod.POST)
	public ResponseEntity<String> createThreatActor(ThreatActor threatActor) {
		threatActor.setCreatorId("");
		threatActorService.createThreatActor(threatActor);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-threat-actors/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateThreatActor(@PathVariable String id, ThreatActor threatActor) {
		threatActor.setId(id);
		threatActor.setCreatorId("");
		threatActorService.updateThreatActor(threatActor);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-threat-actors/{id}/status", method = RequestMethod.PUT)
	public ResponseEntity<String> changeThreatActorStatus(@PathVariable String id, @RequestParam int status) {
		threatActorService.changeThreatActorStatus(id, status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/domain-threat-actors/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteThreatActor(@PathVariable String id) {
		threatActorService.deleteThreatActor(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/sensitive-domains/{id}", method = RequestMethod.GET)
	public ResponseEntity<SensitiveDomain> querySensitiveDomain(@PathVariable String id) {

		SensitiveDomain sensitiveDomain = sensitiveDomainService.querySensitiveDomain(id);
		return new ResponseEntity<SensitiveDomain>(sensitiveDomain, HttpStatus.OK);
	}

	@RequestMapping(value = "/sensitive-domains", method = RequestMethod.POST)
	public ResponseEntity<String> createSensitiveDomain(@RequestParam String category,
			@RequestParam String domainNames) {
		List<String> domainNameList = CsvUtils.convertCSVToList(domainNames);
		for (String domainName : domainNameList) {
			SensitiveDomain sensitiveDomain = new SensitiveDomain();
			sensitiveDomain.setDomainName(domainName.toLowerCase().trim());
			sensitiveDomain.setSensitiveWordName("");
			sensitiveDomain.setSensitiveWord("");
			sensitiveDomainService.createSensitiveDomain(sensitiveDomain);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/sensitive-domains/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateSensitiveDomain(@PathVariable String id, SensitiveDomain sensitiveDomaind) {
		sensitiveDomaind.setId(id);

		sensitiveDomainService.updateSensitiveDomain(sensitiveDomaind);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/sensitive-domains/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteSensitiveDomain(@PathVariable String id) {
		sensitiveDomainService.deleteSensitiveDomain(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
