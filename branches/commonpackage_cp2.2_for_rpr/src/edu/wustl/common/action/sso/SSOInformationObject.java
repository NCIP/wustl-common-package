package edu.wustl.common.action.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessages;

public class SSOInformationObject {
	
	private String loginName;
	
	private HttpServletRequest request;
private HttpServletResponse response;
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	private ActionMessages actionMessages;
	
	private ActionErrors actionErrors;
	
	private String forwardTo;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ActionMessages getActionMessages() {
		return actionMessages;
	}

	public void setActionMessages(ActionMessages actionMessages) {
		this.actionMessages = actionMessages;
	}

	public ActionErrors getActionErrors() {
		return actionErrors;
	}

	public void setActionErrors(ActionErrors actionErrors) {
		this.actionErrors = actionErrors;
	}

	public String getForwardTo() {
		return forwardTo;
	}

	public void setForwardTo(String forwardTo) {
		this.forwardTo = forwardTo;
	}

}
