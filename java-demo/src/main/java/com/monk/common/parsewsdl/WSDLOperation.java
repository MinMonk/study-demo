package com.monk.common.parsewsdl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * WSDL操作实体类
 * 
 * @author houyongchuan
 *
 */
public class WSDLOperation {

	private String name;
	private WSDLParameter input;
	private WSDLParameter output;
	private QName inputMessage;
	private QName outputMessage;
	private String soapAction;
	private String style;
	private Boolean soapActionRequired;
	private QName inputElement;
	private QName outputElement;
	//参数是多个
	private List<WSDLParameter> inputs = new ArrayList<WSDLParameter>();
	private List<WSDLParameter> outputs = new ArrayList<WSDLParameter>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WSDLParameter getInput() {
		return input;
	}

	public void setInput(WSDLParameter input) {
		this.input = input;
	}

	public WSDLParameter getOutput() {
		return output;
	}

	public void setOutput(WSDLParameter output) {
		this.output = output;
	}

	public QName getInputMessage() {
		return inputMessage;
	}

	public void setInputMessage(QName inputMessage) {
		this.inputMessage = inputMessage;
	}

	public QName getOutputMessage() {
		return outputMessage;
	}

	public void setOutputMessage(QName outputMessage) {
		this.outputMessage = outputMessage;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Boolean getSoapActionRequired() {
		return soapActionRequired;
	}

	public void setSoapActionRequired(Boolean soapActionRequired) {
		this.soapActionRequired = soapActionRequired;
	}

	public QName getInputElement() {
		return inputElement;
	}

	public void setInputElement(QName inputElement) {
		this.inputElement = inputElement;
	}

	public QName getOutputElement() {
		return outputElement;
	}

	public void setOutputElement(QName outputElement) {
		this.outputElement = outputElement;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("WSDLOperation:").append("\n");
		buf.append("name=").append(name).append("\n");
		buf.append("soapAction=").append(soapAction).append("\n");
		buf.append("inputMessage=").append(inputMessage).append("\n");
		buf.append("outputMessage=").append(outputMessage).append("\n");
		if(input!=null) {
			buf.append("input={\n").append(input.toString()).append("}\n");
		}else{
			buf.append("input=null\n");
		}
		if(output!=null) {
			buf.append("output={\n").append(output.toString()).append("}\n");
		}else{
			buf.append("output=null\n");
		}
		return buf.toString();
	}

	public List<WSDLParameter> getInputs() {
		return inputs;
	}

	public void setInputs(List<WSDLParameter> inputs) {
		this.inputs = inputs;
	}

	public List<WSDLParameter> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<WSDLParameter> outputs) {
		this.outputs = outputs;
	}

}
