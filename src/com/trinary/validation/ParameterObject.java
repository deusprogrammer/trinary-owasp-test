package com.trinary.validation;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class ParameterObject {
	public ParameterObject(Map<String, String[]> params) throws OWASPValidationException {
		mapParamsToObject(params);
	}
	
	protected void mapParamsToObject(Map<String, String[]> params) throws OWASPValidationException {
		// Transfer all parameters into this object
		for (Field field : this.getClass().getDeclaredFields()) {
			Parameter param = field.getAnnotation(Parameter.class);
			
			String name;
			if (param == null) {
				name = field.getName();
			} else {
				name = param.name();
			}
			
			try {
				field.setAccessible(true);
				String[] value = params.get(name);
				
				if (field.getType().isAssignableFrom(String.class)) {
					if (value != null && value.length == 1) {
						field.set(this, value[0]);
					}
				} else if (field.getType().isAssignableFrom(String[].class)) {
					if (value != null && value.length > 0) {
						field.set(this, value);
					}
				}
			} catch (IllegalArgumentException e) {
				throw new OWASPValidationException("Unable to set parameter " + field.getName(), e);
			} catch (IllegalAccessException e) {
				throw new OWASPValidationException("Unable to set parameter " + field.getName(), e);
			}
			
			params.remove(name);
		}
		
		// Check for left over parameters
		if (params.size() != 0) {
			throw new OWASPValidationException("Unexpected parameters: " + params.keySet());
		}
	}
}
