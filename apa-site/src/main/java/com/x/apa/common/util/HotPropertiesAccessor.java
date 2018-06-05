package com.x.apa.common.util;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * 读取一个属性文件里的配置，且属性文件改变时，不用重启应用，自动读取新的值
 */
@Component
public class HotPropertiesAccessor implements ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(HotPropertiesAccessor.class);

	private static PropertiesConfiguration configuration;
	private static Resource propertyFile;

	@Value("${spring.config.location:classpath:application.properties}")
	private String propertyFileLocation;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public synchronized void initConfigurationIfnessary() throws ConfigurationException, IOException {
		if (configuration == null) {

			Resource propertyFile = null;
			if (propertyFileLocation.startsWith("classpath")) {
				propertyFile = applicationContext.getResource(propertyFileLocation);
			} else {
				propertyFile = applicationContext.getResource("file://" + propertyFileLocation);
			}

			logger.info("Initialize apache common PropertiesConfiguration with resource {}", propertyFile);
			configuration = new PropertiesConfiguration(propertyFile.getURL());
			configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
		}
	}

	/**
	 * 得到一个key对应的值 如果key在properties文件中配置，那么返回null
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		if (configuration == null) {
			return "";
		}
		return configuration.getString(key);
	}

	public static void setProperty(String key, String value) {
		configuration.setProperty(key, value);

		try {
			configuration.save(propertyFile.getURL());
		} catch (ConfigurationException | IOException e) {
			logger.warn("Initialize apache common PropertiesConfiguration occurs error.", e);
		}
	}

}