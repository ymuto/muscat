package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 属性を表すクラス．
 * @author y-mutoh
 *
 */
public class Attribute {
	/**
	 * この属性が所属するAttributeList．
	 */
	private AttributeList attributeList;
	
	/**
	 * 属性タイトル．
	 */
	private String title;
	/**
	 * 属性の値．
	 */
	private String value;
	
	/**
	 * コンストラクタ．
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
	 * XMLノードから読み込む
	 * 
	 * @param nAttribute Attributeノード
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
