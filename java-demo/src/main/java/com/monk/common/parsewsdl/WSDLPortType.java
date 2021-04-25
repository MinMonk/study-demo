package com.monk.common.parsewsdl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

/**
 * WSDL端口类型实体类
 * 
 * @author houyongchuan
 *
 */
public class WSDLPortType {
	private String name;
	private QName qname;
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
		buf.append("WSDLPortType:").append("\n");
		buf.append("name=").append(name).append("\n");
		buf.append("QName=").append(qname).append("\n");
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
		}else{
			buf.append("operations=null").append("\n");
		}
		return buf.toString();
	}

}
