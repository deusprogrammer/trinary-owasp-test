package com.trinary.validation;

import java.util.List;
import java.util.Map;

import com.trinary.security.owasp.annotations.OWASPValidation;
import com.trinary.security.owasp.annotations.OWASPValidationType;
import com.trinary.security.owasp.annotations.Parameter;

public class TestParameterObject {
	@Parameter(name="guid")
	@OWASPValidation(storedPattern="guid", type=OWASPValidationType.WHITELIST)
	protected String guid;
	
	@Parameter(name="postId")
	@OWASPValidation(pattern="[0-9]+", type=OWASPValidationType.WHITELIST)
	protected String postId;
	
	@Parameter(name="referringUrl")
	@OWASPValidation(storedPattern="url", type=OWASPValidationType.WHITELIST)
	protected String referringUrl;
	
	@Parameter(match="^awsGenerateAutograph(.*)IsChanged$")
	@OWASPValidation(pattern="true|false")
	protected String autographBlockIsChanged;
	
	@Parameter(match="^awsGenerateAutograph(.*)Type$")
	@OWASPValidation(pattern="^raw|vectorial$")
	protected List<String> autographBlockType;
	
	@Parameter(match="^awsGenerateAutograph(.*)Content$")
	@OWASPValidation(pattern="^[a-zA-Z]+$")
	protected Map<String, String> autographBlockContent;

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

	/**
	 * @return the autographBlockIsChanged
	 */
	public String getAutographBlockIsChanged() {
		return autographBlockIsChanged;
	}

	/**
	 * @param autographBlockIsChanged the autographBlockIsChanged to set
	 */
	public void setAutographBlockIsChanged(String autographBlockIsChanged) {
		this.autographBlockIsChanged = autographBlockIsChanged;
	}

	/**
	 * @return the autographBlockType
	 */
	public List<String> getAutographBlockType() {
		return autographBlockType;
	}

	/**
	 * @param autographBlockType the autographBlockType to set
	 */
	public void setAutographBlockType(List<String> autographBlockType) {
		this.autographBlockType = autographBlockType;
	}

	/**
	 * @return the autographBlockContent
	 */
	public Map<String, String> getAutographBlockContent() {
		return autographBlockContent;
	}

	/**
	 * @param autographBlockContent the autographBlockContent to set
	 */
	public void setAutographBlockContent(Map<String, String> autographBlockContent) {
		this.autographBlockContent = autographBlockContent;
	}
}