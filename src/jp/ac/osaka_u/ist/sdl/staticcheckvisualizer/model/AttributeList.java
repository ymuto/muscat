package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AttributeList extends ArrayList<Attribute> {

	/**
	 * この属性リストが所属するメソッド．
	 */
	private Method method = null;
	
	/**
	 * XMLノードから読み込む
	 * 
	 * @param nMethods method要素
	 */
	public void importXmlNode(Method method, Node nAttributes) {
		this.method = method;
		Element eAttributes = (Element)nAttributes;
		NodeList nlAttribute = eAttributes.getElementsByTagName("attribute");
		//attributeに対して処理
		for (int i=0; i<nlAttribute.getLength(); i++) {
			Element eAttribute = (Element)nlAttribute.item(i);
			Attribute attribute = new Attribute();
			attribute.importXmlNode(this, eAttribute);
			this.add(attribute);
		}
	}
	
	/**
	 * 属性タイトルが一致する属性を検索する．
	 * @param title 属性タイトル
	 * @return 見つからなかったらnull
	 */
	public Attribute searchAttributeFromTitle(String title) {
		for (Attribute attribute : this) {
			if (attribute.getTitle().equals(title)) return attribute;
		}
		return null;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	
}
