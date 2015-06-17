package com.trinary.validation;

import javax.ejb.Local;

@Local
public interface OWASPValidator {
	public void validate(Object object) throws OWASPValidationException;
}
