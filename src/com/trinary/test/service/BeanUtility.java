package com.trinary.test.service;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BeanUtility {
	public static Object getBean(String jndi) {
		try {
			Context ctx = new InitialContext();
			System.out.println("Available beans: ");
			return ctx.lookup(jndi);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}
}