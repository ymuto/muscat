package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * ������\���N���X�D
 * @author y-mutoh
 *
 */
public class Attribute {
	/**
	 * ���̑�������������AttributeList�D
	 */
	private AttributeList attributeList;
	
	/**
	 * �����^�C�g���D
	 */
	private String title;
	/**
	 * �����̒l�D
	 */
	private String value;
	
	/**
	 * �R���X�g���N�^�D
	 */
	public Attribute() {
		this.title = "";
		this.value = "";
	}
	
	public Attribute(String title, String value) {
		this.title = title;
		this.value = value;
	}
	
	/**
	 * XML�m�[�h����ǂݍ���
	 * 
	 * @param nAttribute Attribute�m�[�h
	 */
	public void importXmlNode(AttributeList attributeList, Node nAttribute) {
		this.attributeList = attributeList;
		Element eAttribute = (Element)nAttribute;
		//title
		Element eTitle = (Element)eAttribute.getElementsByTagName("title").item(0);
		setTitle(eTitle.getTextContent());
		//value
		Element eValue = (Element)eAttribute.getElementsByTagName("value").item(0);
		setValue(eValue.getTextContent());
	}
	
	@Override
	public String toString() {
		return "Attribute [" + title + "=" + value + "]";
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public AttributeList getAttributeList() {
		return attributeList;
	}
	
	
}
