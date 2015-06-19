package com.trinary.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	@SuppressWarnings({ "unchecked" })
	protected ParameterObject marshallParameterObject(ParameterObject object, Map<String, String[]> params) throws OWASPValidationException {
		Map<String, String[]> backup = new HashMap<String, String[]>(params);
		
		// Transfer all parameters into this object
		for (Field field : object.getClass().getDeclaredFields()) {
			Parameter param = field.getAnnotation(Parameter.class);
			String name = null, match = null;
			if (param == null) {
				name = field.getName();
			} else {
				if (!param.name().isEmpty()) {
					name = param.name();
				} else if (!param.match().isEmpty()) {
					match = param.match();
				}
			}
			
			try {
				// If object not initialized, initialize it.
				if (List.class.isAssignableFrom(field.getType()) && field.get(object) == null) {
					field.set(object, new ArrayList<String>());
				} else if (Map.class.isAssignableFrom(field.getType()) && field.get(object) == null) {
					field.set(object,  new HashMap<String, String>());
				}
				
				field.setAccessible(true);
				
				if (name != null) {
					String[] value = params.get(name);
					
					if (value == null) {
						continue;
					}
					
					if (field.getType().isAssignableFrom(String.class)) {
						if (value != null && value.length == 1) {
							field.set(object, value[0]);
						}
					} else if (field.getType().isAssignableFrom(String[].class)) {
						if (value != null && value.length > 0) {
							field.set(object, value);
						}
					}
				} else if (match != null) {
					Pattern pattern = Pattern.compile(match);
					for (Entry<String, String[]> entry : params.entrySet()) {
						Matcher matcher = pattern.matcher(entry.getKey());
						if (matcher.matches()) {
							if (String.class.isAssignableFrom(field.getType())) {
								field.set(object, entry.getValue()[0]);
							} else if (String[].class.isAssignableFrom(field.getType())) {
								field.set(object, entry.getValue());
							} else if (List.class.isAssignableFrom(field.getType())) {
								List<String> list = (List<String>)field.get(object);
								list.add(entry.getValue()[0]);
							} else if (Map.class.isAssignableFrom(field.getType())) {
								Map<String, String> map = (Map<String, String>)field.get(object);
								map.put(entry.getKey(), entry.getValue()[0]);
							}
							
							backup.remove(entry.getKey());
						}
 					}
				}
			} catch (IllegalArgumentException e) {
				throw new OWASPValidationException("Unable to set parameter " + field.getName(), e);
			} catch (IllegalAccessException e) {
				throw new OWASPValidationException("Unable to set parameter " + field.getName(), e);
			}
			
			backup.remove(name);
		}
		
		// Check for left over parameters
		if (backup.size() != 0) {
			throw new OWASPValidationException("Unexpected parameters: " + backup.keySet());
		}
		
		return object;
	}
}