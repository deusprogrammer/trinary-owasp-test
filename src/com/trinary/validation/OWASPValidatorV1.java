package com.trinary.validation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.ServletException;

@Startup
@Singleton
public class OWASPValidatorV1 implements OWASPValidator {
	protected Map<String, Pattern> whitelist = new HashMap<String, Pattern>();
	protected Map<String, Pattern> blacklist = new HashMap<String, Pattern>();
	
	@PostConstruct
	void init() throws ServletException {
		System.out.println("INITIALIZING OWASPValidatorV1");
		Properties whitelistProps = new Properties();
		Properties blacklistProps  = new Properties();
		try {
			InputStream is = OWASPValidatorV1.class.getResourceAsStream("/whitelist.properties");
			whitelistProps.load(is);
			is = OWASPValidatorV1.class.getResourceAsStream("/blacklist.properties");
			blacklistProps.load(is);
		} catch (IOException e) {
			throw new ServletException("Failed to find whitelist and blacklist files.");
		}
		
		for (Entry<Object, Object> entry : whitelistProps.entrySet()) {
			whitelist.put((String)entry.getKey(), Pattern.compile((String)entry.getValue()));
		}
		
		for (Entry<Object, Object> entry : blacklistProps.entrySet()) {
			blacklist.put((String)entry.getKey(), Pattern.compile((String)entry.getValue()));
		}
	}
	
	public void validate(Object object) throws OWASPValidationException {
		for (Field field : object.getClass().getDeclaredFields()) {
			OWASPValidation validation = field.getAnnotation(OWASPValidation.class);
			
			if (validation == null) {
				continue;
			}
			
			try {
				if (!validation.storedPattern().isEmpty()) {
					validateStoredPattern((String)field.get(object), validation.storedPattern(), validation.type());
				} else if (!validation.pattern().isEmpty()) {
					validatePattern((String)field.get(object), validation.pattern(), validation.type());
				} else {
					continue;
				}
			} catch (IllegalArgumentException e) {
				throw new OWASPValidationException("Unable to get field value to validate.", e);
			} catch (IllegalAccessException e) {
				throw new OWASPValidationException("Unable to get field value to validate.", e);
			}
		}
	}
	
	protected void validatePattern(String value, String pattern, OWASPValidationType type) throws OWASPValidationException {
		validatePattern(value, Pattern.compile(pattern), type);
	}
	
	protected void validatePattern(String value, Pattern pattern, OWASPValidationType type) throws OWASPValidationException {
		switch(type) {
		case WHITELIST:
			validateWhiteList(value, pattern);
			break;
		case BLACKLIST:
			validateBlackList(value, pattern);
			break;
		}
	}
	
	protected void validateStoredPattern(String value, String patternName, OWASPValidationType type) throws OWASPValidationException {
		Pattern pattern = this.getStoredPattern(patternName, type);
		validatePattern(value, pattern, type);
	}
	
	protected void validateBlackList(String value, Pattern pattern) throws OWASPValidationException {
		if (value == null) {
			return;
		}

		Matcher m = pattern.matcher(value);
		if (m.matches()) {
			throw new OWASPValidationException("Value \"" + value + "\" failed validation against pattern \"" + pattern + "\"");
		}
	}
	
	protected void validateWhiteList(String value, Pattern pattern) throws OWASPValidationException {
		if (value == null) {
			return;
		}
		
		Matcher m = pattern.matcher(value);
		if (!m.matches()) {
			throw new OWASPValidationException("Value \"" + value + "\" failed validation against pattern \"" + pattern + "\"");
		}
	}
	
	protected Pattern getStoredPattern(String patternName, OWASPValidationType type) {
		switch(type) {
		case BLACKLIST:
			return blacklist.get(patternName);
		case WHITELIST:
		default:
			return whitelist.get(patternName);
		}
	}
}