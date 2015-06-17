# OWASP-Filter

## What is it?

OWASP Filter is a Java EE web filter that takes as an argument a class that extends type ParameterObject.  Parameter object is a specially annotated object that includes all possible parameter map entries a servlet should be expecting from the http request.  Each field is annotated with a @Parameter annotation to map the field to it's parameter map key name and annotated with a @OWASPValidation annotation to specify a one off white list pattern or a stored pattern (stored in the whitelist.properties file).  The filter will load the ParameterObject and will throw an OWASPValidationException when the http request contains parameters not enumerated in the ParameterObject.  The object will be passed to the OWASPValidator (which can be overriden and injected with a specialized validator if the user so desires), which based on he annotations will be validated in one pass.  The first failed validation will throw an OWASPValidationException.

## What's to come?

Next I will refactor the filter to allow any JAX-B binded object to be marshalled into a Parameter Object.  The object will still be able to be marked up in the same fashion we are doing with our simple ParameterObject.  We will also add validation annotations for checking things other non-security validations (like field length for example).

## How to use

The following is an example web.xml

	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" version="3.1">
	  <display-name>owasp-filter</display-name>
	  <servlet>
		<display-name>Test Servlet</display-name>
		<servlet-name>TestServlet</servlet-name>
		<servlet-class>com.trinary.test.servlet.TestServlet</servlet-class>
	  </servlet>
	  <servlet-mapping>
		<servlet-name>TestServlet</servlet-name>
		<url-pattern>/test/*</url-pattern>
	  </servlet-mapping>
	  <filter>
		<filter-name>owasp-filter</filter-name>
		<filter-class>com.trinary.validation.OWASPFilter</filter-class>
		<init-param>
			<param-name>owasp.parameterObjectType</param-name>
			<param-value>com.trinary.validation.TestParameterObject</param-value>
		</init-param>
	  </filter>
	  <filter-mapping>
		<filter-name>owasp-filter</filter-name>
		<servlet-name>TestServlet</servlet-name>
	  </filter-mapping>
	</web-app>

Notice that the OWASPFilter takes one parameter "owasp.parameterObjectType".  This tells the filter that all parameter maps passed in should attempt to be unmarshalled into this object.  The object type MUST EXTEND type ParameterObject.

Now let's look at an example ParameterObject.

	package com.trinary.validation;

	import java.util.Map;

	public class TestParameterObject extends ParameterObject {
		@Parameter(name="guid")
		@OWASPValidation(storedPattern="guid", type=OWASPValidationType.WHITELIST)
		protected String guid;
	
		@Parameter(name="postId")
		@OWASPValidation(pattern="[0-9]+", type=OWASPValidationType.WHITELIST)
		protected String postId;
	
		@Parameter(name="referringUrl")
		@OWASPValidation(storedPattern="url", type=OWASPValidationType.WHITELIST)
		protected String referringUrl;

		public TestParameterObject(Map<String, String[]> params) throws OWASPValidationException {
			super(params);
		}

		/**
		 * @return the guid
		 */
		public String getGuid() {
			return guid;
		}

		/**
		 * @param guid the guid to set
		 */
		public void setGuid(String guid) {
			this.guid = guid;
		}

		/**
		 * @return the postId
		 */
		public String getPostId() {
			return postId;
		}

		/**
		 * @param postId the postId to set
		 */
		public void setPostId(String postId) {
			this.postId = postId;
		}

		/**
		 * @return the referringUrl
		 */
		public String getReferringUrl() {
			return referringUrl;
		}

		/**
		 * @param referringUrl the referringUrl to set
		 */
		public void setReferringUrl(String referringUrl) {
			this.referringUrl = referringUrl;
		}
	}

The @Parameter annotations are not required, but if the parameter map key is different from the name of the field in your parameter object you must map them together.  Otherwise the ParameterObject will assume that the key should match the field name.  I also just wanted to note that I will be moving the marshalling of a parameter map into a ParameterObject into a factory design pattern rather than the constructor of said object.

The @OWASPValidation annotation takes at minimum a storedPattern name or a one off regex pattern.  The type will automatically be set to WHITELIST if you don't put anything else.  The BLACKLIST type will currently only do a negative match (meaning that matching the regex pattern will cause a failed validation).  However in the near future I will allow the BLACKLIST to take a list of stored regex patterns as well as a name for a stored list of stored patterns.

As for the whitelist and blacklist.  These are just simple properties files using key value pairs.  Here is an example whitelist:

	guid=^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}$
	url=^((((https?|ftps?|gopher|telnet|nntp)\://)|(mailto\:|news\:))(%[0-9A-Fa-f]{2}|[-()_.\!~*';/?\:@&\=+$,A-Za-z0-9])+)([).\!';/?\:,][[\:blank\:]])?$

## Versions

### 0.1a

* Implemented basic parameter map validation for typical http requests iniated by a browser.
* Implemented basic whitelist validation and naive blacklist validation
* Implemented first version of OWASPValidationFilter.
* Implemented first version of ParameterObject.

## TODO

* Refactor OWASPValidationFilter to handle all content-types that have a JAX-B binding.
* Create a factory design pattern service to handle conversion of parameter map to ParameterObject.
* Think about a way to get the ParameterObject to the servlet insted of the parameter map.
* Think about JAX-WS and JAX-RS web services and how we might filter these...the validation might not be able to be done at the filter since we have no idea what object an XML/JSON blob maps to.
