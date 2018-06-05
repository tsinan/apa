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
import com.x.apa.inspecturl.data.Brand;
import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.inspecturl.service.BrandService;
import com.x.apa.inspecturl.service.InspectUrlService;

/**
 * @author liumeng
 */
@RestController
@RequestMapping(value = "/webapi/apa/v1")
public class InspectUrlWebApiController implements Constant {

	@Autowired
	private BrandService brandService;

	@Autowired
	private InspectUrlService inspectUrlService;

	@RequestMapping(value = "/brands/{brandId}", method = RequestMethod.GET)
	public ResponseEntity<Brand> queryBrand(@PathVariable String brandId) {
		Brand brand = brandService.queryBrand(brandId);
		return new ResponseEntity<Brand>(brand, HttpStatus.OK);
	}

	@RequestMapping(value = "/brands", method = RequestMethod.POST)
	public ResponseEntity<String> createBrand(Brand brand) {
		brand.setCreatorId("");
		brandService.createBrand(brand);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/brands/{brandId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateBrand(@PathVariable String brandId, Brand brand) {
		brand.setId(brandId);
		brand.setCreatorId("");
		brandService.updateBrand(brand);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/brands/{brandId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteBrand(@PathVariable String brandId) {
		brandService.deleteBrand(brandId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/inspect-urls/{inspectUrlId}", method = RequestMethod.GET)
	public ResponseEntity<InspectUrl> queryInspectUrl(@PathVariable String inspectUrlId) {
		InspectUrl inspectUrl = inspectUrlService.queryInspectUrl(inspectUrlId);
		return new ResponseEntity<InspectUrl>(inspectUrl, HttpStatus.OK);
	}

	@RequestMapping(value = "/inspect-urls", method = RequestMethod.POST)
	public ResponseEntity<String> createInspectUrl(InspectUrl inspectUrl) {
		inspectUrl.setUrl(inspectUrl.getUrl().toLowerCase());
		inspectUrl.setCreatorId("");
		inspectUrlService.createInspectUrl(inspectUrl);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/inspect-urls/{inspectUrlId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateInspectUrl(@PathVariable String inspectUrlId, InspectUrl inspectUrl) {
		inspectUrl.setId(inspectUrlId);
		inspectUrl.setCreatorId("");
		inspectUrlService.updateInspectUrl(inspectUrl);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/inspect-urls/{inspectUrlId}/inspect-level", method = RequestMethod.PUT)
	public ResponseEntity<String> changeInspectUrlLevel(@PathVariable String inspectUrlId,
			@RequestParam int inspectLevel) {
		inspectUrlService.changeInspectUrlLevel(inspectUrlId, inspectLevel);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/inspect-urls/{inspectUrlId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteInspectUrl(@PathVariable String inspectUrlId) {
		inspectUrlService.deleteInspectUrl(inspectUrlId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
