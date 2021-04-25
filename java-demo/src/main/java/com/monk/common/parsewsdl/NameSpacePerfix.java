package com.monk.common.parsewsdl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

public class NameSpacePerfix {

	private Map<String,String> prefixMap = new HashMap<String,String>();
	private Map<String,String> qNameUriMap = new HashMap<String,String>();
	public String getQNamePrefix(QName qname) {
		if(qname!=null) {
			String prefix = qNameUriMap.get(qname.getNamespaceURI());
			if(prefix==null) {
				prefix = buildPrefix(qname);
				qNameUriMap.put(qname.getNamespaceURI(), prefix);
				return prefix;
			}else {
				return prefix;
			}
		}
		return null;
	}
	
	private String buildPrefix(QName qname) {
		String prefix = qname.getPrefix();
		if(prefix==null||"".equals(prefix)) {
			//取URI最后一段，三个字符
			String qsUri = qname.getNamespaceURI();
			String[] tokens = qsUri.split("/");
			for(int i=tokens.length;i>0;i--) {
				String token = tokens[i-1];
				if(token!=null&&!"".equals(token)) {
					token = token.replaceAll("\\.", "");
					prefix = token.toLowerCase().substring(0, token.length()>2?3:token.length()-1);
					break;
				}
			}
			if(prefix==null) {
				prefix = "ns";
			}
			
		}
		
		String uri = prefixMap.get(prefix);
		if(uri==null) {
			prefixMap.put(prefix, qname.getNamespaceURI());
		}else{
			for(int i=1;i<100;i++) {
				String namespaceURI = prefixMap.get(prefix+i);
				if(namespaceURI==null) {
					prefixMap.put(prefix+i, qname.getNamespaceURI());
					prefix = prefix+i;
					break;
				}
			}
		}
		return prefix;
	}

	public Map<String, String> getPrefixMap() {
		return prefixMap;
	}

	public void setPrefixMap(Map<String, String> prefixMap) {
		this.prefixMap = prefixMap;
	}

	public Map<String, String> getqNameUriMap() {
		return qNameUriMap;
	}

	public void setqNameUriMap(Map<String, String> qNameUriMap) {
		this.qNameUriMap = qNameUriMap;
	}
}
