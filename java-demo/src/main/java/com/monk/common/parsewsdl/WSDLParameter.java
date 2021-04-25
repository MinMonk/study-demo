package com.monk.common.parsewsdl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * WSDL参数实体类
 * 
 * @author houyongchuan
 *
 */
public class WSDLParameter {

	private String id;
	private String pId;
	private boolean open = true;
	private boolean isLeaf = false;
	private boolean isSimpleType = false;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 值，仅基本类型会被填入
	 */
	private String value;

	/**
	 * 类型，类型未空时表示自己定义的complexType
	 */
	private String type;

	/**
	 * 子类型，用于识别array类型下的type，应该是不用担心array嵌套array的，目前我测试的java
	 * webservice框架无法自动生成对应的wsdl文档
	 */
	private String childType;

	/**
	 * 子结点
	 */
	private List<WSDLParameter> children = new ArrayList<WSDLParameter>();

	private QName typeQName;
	private long maxOccurs;
	private long minOccurs;
	private boolean isNillable;
	private QName elementQName;
	//当前节点路径
	private String xpath;
	private NameSpacePerfix spacePerfix;

	public WSDLParameter() {
	}

	public WSDLParameter(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public WSDLParameter(String name, String value, String type, String childType, List<WSDLParameter> children) {
		this.name = name;
		this.value = value;
		this.type = type;
		this.childType = childType;
		this.children = children;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<WSDLParameter> getChildren() {
		return this.children;
	}

	public void setChildren(List<WSDLParameter> children) {
		this.children = children;
	}

	public String getChildType() {
		return this.childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public void addChild(WSDLParameter param) {
		this.children.add(param);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public QName getTypeQName() {
		return typeQName;
	}

	public void setTypeQName(QName typeQName) {
		this.typeQName = typeQName;
	}

	public long getMaxOccurs() {
		return maxOccurs;
	}

	public void setMaxOccurs(long maxOccurs) {
		this.maxOccurs = maxOccurs;
	}

	public long getMinOccurs() {
		return minOccurs;
	}

	public void setMinOccurs(long minOccurs) {
		this.minOccurs = minOccurs;
	}

	public boolean isNillable() {
		return isNillable;
	}

	public void setNillable(boolean isNillable) {
		this.isNillable = isNillable;
	}

	public QName getElementQName() {
		return elementQName;
	}

	public void setElementQName(QName elementQName) {
		this.elementQName = elementQName;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("id=").append(id).append(",");
		buf.append("xpath=").append(xpath).append(",");
		buf.append("pId=").append(pId).append(",");
		buf.append("name=").append(name).append(",");
		buf.append("type=").append(type).append(",");
		buf.append("elementQName=").append(elementQName).append(",");
		buf.append("typeQName=").append(typeQName).append(",");
		buf.append("maxOccurs=").append(maxOccurs).append(",");
		buf.append("minOccurs=").append(minOccurs).append(",");
		buf.append("isNillable=").append(isNillable).append(",");
		buf.append("childType=").append(childType).append(",");
		if(children!=null){
			buf.append("\nchildren=").append("\n");
			buf.append("[");
			for(WSDLParameter child:children) {
				buf.append(child.toString()).append("\n");
			}
			buf.append("]").append("\n");
			
		}else{
			buf.append("children=null");
		}
		
		return buf.toString();
	}

	public NameSpacePerfix getSpacePerfix() {
		return spacePerfix;
	}

	public void setSpacePerfix(NameSpacePerfix spacePerfix) {
		this.spacePerfix = spacePerfix;
	}

	public boolean isSimpleType() {
		return isSimpleType;
	}

	public void setIsSimpleType(boolean isSimpleType) {
		this.isSimpleType = isSimpleType;
	}

}
