package com.trinary.validation;

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