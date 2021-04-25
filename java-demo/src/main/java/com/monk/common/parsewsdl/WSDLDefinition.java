package com.monk.common.parsewsdl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.wsdl.Definition;
import javax.xml.namespace.QName;

/**
 * WSDL定义实体类
 * @author houyongchuan
 *
 */
public class WSDLDefinition {
	private String documentBaseURI;
	private QName qName;
	private String targetNamespace;
	private Definition definition;

	private Map<QName, WSDLService> services;
	private Map<QName, WSDLBinding> bindings;
	private Map<QName, WSDLPort> ports;
	private Map<QName, WSDLPortType> portTypes;

	public String getDocumentBaseURI() {
		return documentBaseURI;
	}

	public void setDocumentBaseURI(String documentBaseURI) {
		this.documentBaseURI = documentBaseURI;
	}

	public QName getqName() {
		return qName;
	}

	public void setqName(QName qName) {
		this.qName = qName;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public Definition getDefinition() {
		return definition;
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	public WSDLService getService(QName qname) {
		return services == null ? null : services.get(qname);
	}

	public Collection<WSDLService> getServices() {
		return services == null ? null : services.values();
	}

	public void setServices(Map<QName, WSDLService> services) {
		this.services = services;
	}

	public void addService(QName qname, WSDLService service) {
		if (this.services == null) {
			services = new HashMap<QName, WSDLService>();
		}
		services.put(qname, service);
	}

	public WSDLBinding getBinding(QName qname) {
		return bindings == null ? null : bindings.get(qname);
	}

	public Collection<WSDLBinding> getBindings() {
		return bindings == null ? null : bindings.values();
	}

	public void setBindings(Map<QName, WSDLBinding> bindings) {
		this.bindings = bindings;
	}

	public void addBinding(QName qname, WSDLBinding binding) {
		if (this.bindings == null) {
			bindings = new HashMap<QName, WSDLBinding>();
		}
		bindings.put(qname, binding);
	}

	public WSDLPort getPort(QName qname) {
		return ports == null ? null : ports.get(qname);
	}

	public Collection<WSDLPort> getPorts() {
		return ports == null ? null : ports.values();
	}

	public void setPorts(Map<QName, WSDLPort> ports) {
		this.ports = ports;
	}

	public void addPort(QName qname, WSDLPort port) {
		if (this.ports == null) {
			ports = new HashMap<QName, WSDLPort>();
		}
		ports.put(qname, port);
	}

	public WSDLPortType getPortType(QName qname) {
		return portTypes == null ? null : portTypes.get(qname);
	}

	public Collection<WSDLPortType> getPortTypes() {
		return portTypes == null ? null : portTypes.values();
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

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("WSDLDefinition:").append("\n");
		buf.append("documentBaseURI=").append(documentBaseURI).append(",\n");
		buf.append("QName=").append(qName).append(",\n");
		buf.append("targetNamespace=").append(targetNamespace).append(",\n");
		if(services!=null) {
			Iterator<Entry<QName, WSDLService>> it = services.entrySet().iterator();
			buf.append("services=").append("\n");
			buf.append("[");
			while(it.hasNext()) {
				Entry<QName, WSDLService> entry = it.next();
				WSDLService service = entry.getValue();
				buf.append(service.toString());
			}
			buf.append("]").append("\n");
		}else{
			buf.append("services=null").append("\n");
		}
		return buf.toString();
	}

}
