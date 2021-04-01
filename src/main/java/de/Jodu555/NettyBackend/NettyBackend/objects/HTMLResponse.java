package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.io.File;
import java.util.HashMap;

import de.Jodu555.NettyBackend.NettyBackend.abstracts.AbstractResponse;
import de.Jodu555.NettyBackend.NettyBackend.utils.JsonUtils;

public class HTMLResponse extends AbstractResponse {

	public enum HTMLResponseType {
		TEXT, FILE;
	}

	private HTMLResponseType htmlResponseType = HTMLResponseType.TEXT;
	private StringBuilder responseString = new StringBuilder();
	private File responseFile;
	private boolean CodeInterpreter = false;
	private HashMap<String, Object> variables;

	public HTMLResponseType getHtmlResponseType() {
		return htmlResponseType;
	}

	public void setHtmlResponseType(HTMLResponseType htmlResponseType) {
		this.htmlResponseType = htmlResponseType;
	}

	public File getResponseFile() {
		return responseFile;
	}

	public void setResponseFile(File responseFile) {
		if(!responseFile.exists()) {
			System.out.println("Error Response File Dont Exists");
			return;
		}
		this.responseFile = responseFile;
		this.htmlResponseType = HTMLResponseType.FILE;
	}

	public StringBuilder getResponseString() {
		return responseString;
	}

	public void setResponseString(StringBuilder response) {
		this.responseString = response;
		this.htmlResponseType = HTMLResponseType.TEXT;
	}
	
	public HashMap<String, Object> getVariables() {
		return variables;
	}
	
	public void setVariables(HashMap<String, Object> variables) {
		this.variables = variables;
	}
	
	public boolean isCodeInterpreter() {
		return CodeInterpreter;
	}
	
	public void setCodeInterpreter(boolean codeInterpreter) {
		CodeInterpreter = codeInterpreter;
	}

}
