package com.trinary.validation;

import java.lang.reflect.Field;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class ParameterObjectFactoryImpl implements ParameterObjectFactory {
	@SuppressWarnings("unchecked")
	@Override
	public ParameterObject create(String className, Map<String, String[]> params)
			throws OWASPValidationException {
		Class<ParameterObject> clazz;
		try {
			clazz = (Class<ParameterObject>)Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new OWASPValidationException(e);
		}
		return create(clazz, params);
	}
	
	@Override
	public ParameterObject create(Class<ParameterObject> clazz, Map<String, String[]> params) throws OWASPValidationException {
		ParameterObject object = null;
		try {
			object = clazz.newInstance();
			object = marshallParameterObject(object, params);
		} catch (InstantiationException e) {
			throw new OWASPValidationException("Unable to create new instance of object type " + clazz.getCanonicalName() + ".", e);
		} catch (IllegalAccessException e) {
			throw new OWASPValidationException("Unable to create new instance of object type " + clazz.getCanonicalName() + ".", e);
		}
		
		return object;
	}
	
	protected ParameterObject marshallParameterObject(ParameterObject object, Map<String, String[]> params) throws OWASPValidationException {
		// Transfer all parameters into this object
		for (Field field : object.getClass().getDeclaredFields()) {
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
						field.set(object, value[0]);
					}
				} else if (field.getType().isAssignableFrom(String[].class)) {
					if (value != null && value.length > 0) {
						field.set(object, value);
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
		
		return object;
	}
}