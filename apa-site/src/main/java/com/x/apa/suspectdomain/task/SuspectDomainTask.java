package com.x.apa.suspectdomain.task;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.data.SuspectDomain;
import com.x.apa.common.data.RawDomain;
import com.x.apa.common.queue.RawDomainQueue;
import com.x.apa.common.queue.SuspectDomainQueue;
import com.x.apa.suspectdomain.service.SuspectDomainService;
import com.x.apa.suspectdomain.strategy.DomainStrategyChain;

/**
 * @author liumeng
 */
@Component
public class SuspectDomainTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RawDomainQueue rawDomainQueue;

	@Autowired
	private SuspectDomainQueue suspectDomainQueue;

	@Autowired
	private DomainStrategyChain domainStrategyChain;

	@Autowired
	private SuspectDomainService suspectDomainService;

	@Value("${sd.match.switch}")
	private String domainMatchSwitch;

	@PostConstruct
	public void init() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				doProcess();
			}
		}).start();
	}

	public void doProcess() {

		logger.info("start domain match task...");

		RawDomain rawDomain = null;
		boolean goon = true;
		while (goon && (rawDomain = rawDomainQueue.take()) != null) {

			// 开关为关闭，所有域名丢弃
			if (StringUtils.equals(domainMatchSwitch, "reject")) {
				continue;
			}

			// 开关为放行，所有域名送往下一步队列
			if (StringUtils.equals(domainMatchSwitch, "bypass")) {
				doDomainMatch(rawDomain, null);
				continue;
			}

			// 调用分析链，检查是否疑似Domain
			Map<String, String> matchResult = null;
			if ((matchResult = domainStrategyChain.match(rawDomain)) != null) {
				doDomainMatch(rawDomain, matchResult);
			} else {
				// TODO 非疑似TA，发送到非疑似TA队列
			}

		}

		logger.info("end domain match task...");
	}

	private void doDomainMatch(RawDomain rawDomain, Map<String, String> matchResult) {
		// 疑似TA，记录数据
		SuspectDomain suspectDomain = new SuspectDomain();
		BeanUtils.copyProperties(rawDomain, suspectDomain);
		suspectDomain.setRawDomainId(rawDomain.getId());

		if (matchResult != null) {
			suspectDomain.setSuspectMatch(matchResult.get("match"));
			suspectDomain.setSuspectMatchObjId(matchResult.get("matchObjId"));
			suspectDomain.setSuspectMatchObjText(matchResult.get("matchObjText"));

			// 保存到数据库
			suspectDomainService.createSuspectDomain(suspectDomain);
		} else {
			suspectDomain.setId("");
			suspectDomain.setSuspectMatch(SUSPECT_DOMAIN_MATCH_NONE);
			suspectDomain.setSuspectMatchObjId("");
			suspectDomain.setSuspectMatchObjText("");
		}

		// 发送到疑似域名队列
		suspectDomainQueue.put(suspectDomain);
	}
}
