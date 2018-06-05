package com.x.apa.inspecturl.task;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.x.apa.common.util.HotPropertiesAccessor;

/**
 * @author liumeng
 */
@Component
public class InspectUrlLatch {

	private static final String INSPECT_URL_LATCH_KEY = "PU_INSPECT_URL_LATCH";
	@Autowired
	private static StringRedisTemplate redisTemplate;

	@Resource(name = "stringRedisTemplate")
	public void setRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		InspectUrlLatch.redisTemplate = stringRedisTemplate;
	}

	public static boolean tryLock(String ipAddress, String hostName) {
		boolean success = redisTemplate.opsForHash().putIfAbsent(INSPECT_URL_LATCH_KEY, ipAddress, hostName);
		if (redisTemplate.getExpire(INSPECT_URL_LATCH_KEY) == -1) {
			int timeout = Integer.parseInt(HotPropertiesAccessor.getProperty("pu.inspect.ip.latch.minute"));
			redisTemplate.expire(INSPECT_URL_LATCH_KEY, timeout, TimeUnit.MINUTES);
		}
		return success;
	}

}
