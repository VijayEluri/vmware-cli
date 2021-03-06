package com.octo.vmware.utils;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;

import com.octo.vmware.entities.Converter;
import com.octo.vmware.entities.EsxServer;
import com.octo.vmware.services.Configuration;

public final class PropertiesUtils {

	PropertiesUtils() {
	}

	public static final Configuration loadProperties(InputStream inputStream) throws Exception {
		Properties defaultProps = new java.util.Properties();
		defaultProps.load(inputStream);
		inputStream.close();
		PropertiesUtils pu = new PropertiesUtils();
		return pu.loadConfiguration(defaultProps);
	}

	Configuration loadConfiguration(Properties properties) throws Exception {
		Configuration conf = new Configuration();
		conf.setConverter(new Converter());
		Object bean = null;

		for (Enumeration<Object> keysEnum = properties.keys(); keysEnum.hasMoreElements();) {
			String key = (String) keysEnum.nextElement();
			String ketToSet = null;
			if (key.toLowerCase().startsWith("esx.")) {
				int firstPoint = key.indexOf('.');
				int secondPoint = key.indexOf('.', firstPoint + 1);
				String esxKey = key.substring(firstPoint + 1, secondPoint).toLowerCase();
				if (!conf.getEsxServers().containsKey(esxKey)) {
					EsxServer esxServer = new EsxServer();
					esxServer.setName(esxKey);
					conf.getEsxServers().put(esxKey, esxServer);
				}
				bean = conf.getEsxServers().get(esxKey);
				ketToSet = key.substring(secondPoint + 1);
			} else {
				bean = conf;
				ketToSet = key;
			}
			String value = properties.getProperty(key);
			if (value != null) {
				PropertyUtils.setNestedProperty(bean, ketToSet, value);
			}
		}
		if (conf.getConverter().getHostname() == null) {
			conf.setConverter(null);
		}
		return conf;
	}
}
