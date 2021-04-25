package com.monk.common.parsewsdl;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

/**
 * WSDL端口实体类
 * 
 * @author houyongchuan
 *
 */
public class WSDLPort {

	private String name;
	private Map<QName, WSDLBinding> bindings;
	private String soapAdress;
	private String soapVersion;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WSDLBinding getBinding(QName qname) {
		return bindings.get(qname);
	}

	public Collection<WSDLBinding> getBindings() {
		return bindings.values();
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

	public String getSoapAdress() {
		return soapAdress;
	}

	public void setSoapAdress(String soapAdress) {
		this.soapAdress = soapAdress;
	}

	public String getSoapVersion() {
		return soapVersion;
	}

	public void setSoapVersion(String soapVersion) {
		this.soapVersion = soapVersion;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("WSDLPort:").append("\n");
		buf.append("name=").append(name).append("\n");
		buf.append("soapAdress=").append(soapAdress).append("\n");
		buf.append("soapVersion=").append(soapVersion).append("\n");
		if(bindings!=null) {
			Iterator<Entry<QName, WSDLBinding>> it = bindings.entrySet().iterator();
			buf.append("bindings=").append("\n");
			buf.append("[");
			while(it.hasNext()) {
				Entry<QName, WSDLBinding> entry = it.next();
				WSDLBinding binding = entry.getValue();
				buf.append(binding.toString());
			}
			buf.append("]").append("\n");
		}else{
			buf.append("bindings=null").append("\n");
		}
		return buf.toString();
	}
}
