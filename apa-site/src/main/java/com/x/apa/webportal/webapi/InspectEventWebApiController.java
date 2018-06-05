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
import com.x.apa.inspecturl.data.InspectTrace;
import com.x.apa.inspecturl.service.InspectEventService;
import com.x.apa.inspecturl.service.InspectTraceService;

/**
 * @author liumeng
 */
@RestController
@RequestMapping(value = "/webapi/apa/v1")
public class InspectEventWebApiController implements Constant {

	@Autowired
	private InspectEventService inspectEventService;

	@Autowired
	private InspectTraceService inspectTraceService;

	@RequestMapping(value = "/inspect-traces/{inspectTraceId}", method = RequestMethod.GET)
	public ResponseEntity<InspectTrace> queryInspectTrace(@PathVariable String inspectTraceId) {
		InspectTrace inspectTrace = inspectTraceService.queryInspectTrace(inspectTraceId);
		return new ResponseEntity<InspectTrace>(inspectTrace, HttpStatus.OK);
	}

	@RequestMapping(value = "/inspect-events/{inspectEventId}/progress", method = RequestMethod.PUT)
	public ResponseEntity<String> changeInspectEventProgress(@PathVariable String inspectEventId,
			@RequestParam int progress) {
		inspectEventService.changeInspectEventProgress(inspectEventId, progress);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/inspect-events/{inspectEventId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteInspectEvent(@PathVariable String inspectEventId) {
		inspectEventService.deleteInspectEvent(inspectEventId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
