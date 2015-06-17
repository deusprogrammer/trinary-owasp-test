package com.trinary.validation;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface ParameterObjectFactory {
	public ParameterObject create(String className, Map<String, String[]> params) throws OWASPValidationException;
	public ParameterObject create(Class<ParameterObject> clazz, Map<String, String[]> params) throws OWASPValidationException;
}