package com.monk.common.parsewsdl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

/**
 * WSDL服务定义实体类
 * 
 * @author houyongchuan
 *
 */
public class WSDLService {
	private String name;
	private QName qname;
	private Map<String, WSDLPort> ports;

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

	public WSDLPort getPort(QName qname) {
		return ports.get(qname);
	}

	public Collection<WSDLPort> getPorts() {
		return ports.values();
	}

	public void setPorts(Map<String, WSDLPort> ports) {
		this.ports = ports;
	}

	public void addPort(String name, WSDLPort port) {
		if (this.ports == null) {
			ports = new HashMap<String, WSDLPort>();
		}
		ports.put(name, port);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("WSDLService:").append("\n");
		buf.append("name=").append(name).append(",\n");
		buf.append("QName=").append(qname).append(",\n");
		if(ports!=null) {
			Iterator<Entry<String, WSDLPort>> it = ports.entrySet().iterator();
			buf.append("ports=").append("\n");
			buf.append("[");
			while(it.hasNext()) {
				Entry<String, WSDLPort> entry = it.next();
				WSDLPort port = entry.getValue();
				buf.append(port.toString());
			}
			buf.append("]").append("\n");
		}else{
			buf.append("ports=null").append("\n");
		}
		return buf.toString();
	}
}
