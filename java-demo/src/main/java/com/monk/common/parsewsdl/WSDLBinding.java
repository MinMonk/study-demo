package com.monk.common.parsewsdl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

/**
 * WSDL绑定实体类
 * 
 * @author houyongchuan
 *
 */
public class WSDLBinding {
	private String name;
	private QName qname;
	private Map<QName, WSDLPortType> portTypes;
	private Map<String, WSDLOperation> operations;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QName getQname() {
		return qname;
	}

	public void setQname(QName qname) {
		this.qname = qname;
	}

	public WSDLPortType getPortType(QName qname) {
		return portTypes.get(qname);
	}

	public Collection<WSDLPortType> getPortTypes() {
		return portTypes.values();
	}

	public void setPortTypes(Map<QName, WSDLPortType> portTypes) {
		this.portTypes = portTypes;
	}

	public void addPortType(QName qname, WSDLPortType portType) {
		if (this.portTypes == null) {
			portTypes = new HashMap<QName, WSDLPortType>();
		}
		portTypes.put(qname, portType);
	}

	public WSDLOperation getOperation(String name) {
		return operations.get(name);
	}

	public Collection<WSDLOperation> getOperations() {
		return operations.values();
	}

	public void setOperations(Map<String, WSDLOperation> operations) {
		this.operations = operations;
	}

	public void addOperation(String name, WSDLOperation operation) {
		if (this.operations == null) {
			operations = new HashMap<String, WSDLOperation>();
		}
		operations.put(name, operation);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("WSDLBinding:").append("\n");
		buf.append("name=").append(name).append("\n");
		buf.append("QName=").append(qname).append("\n");
		if(portTypes!=null) {
			Iterator<Entry<QName, WSDLPortType>> it = portTypes.entrySet().iterator();
			buf.append("portTypes=").append("\n");
			buf.append("[");
			while(it.hasNext()) {
				Entry<QName, WSDLPortType> entry = it.next();
				WSDLPortType portTypes = entry.getValue();
				buf.append(portTypes.toString());
			}
			buf.append("]").append("\n");
		}
		if(operations!=null) {
			Iterator<Entry<String, WSDLOperation>> it = operations.entrySet().iterator();
			buf.append("operations=").append("\n");
			buf.append("[");
			while(it.hasNext()) {
				Entry<String, WSDLOperation> entry = it.next();
				WSDLOperation operation = entry.getValue();
				buf.append(operation.toString());
			}
			buf.append("]").append("\n");
		}
		return buf.toString();
	}

}
