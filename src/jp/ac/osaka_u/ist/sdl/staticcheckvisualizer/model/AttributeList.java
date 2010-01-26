package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AttributeList extends ArrayList<Attribute> {

	/**
	 * ���̑������X�g���������郁�\�b�h�D
	 */
	private Method method = null;
	
	/**
	 * XML�m�[�h����ǂݍ���
	 * 
	 * @param nMethods method�v�f
	 */
	public void importXmlNode(Method method, Node nAttributes) {
		this.method = method;
		Element eAttributes = (Element)nAttributes;
		NodeList nlAttribute = eAttributes.getElementsByTagName("attribute");
		//attribute�ɑ΂��ď���
		for (int i=0; i<nlAttribute.getLength(); i++) {
			Element eAttribute = (Element)nlAttribute.item(i);
			Attribute attribute = new Attribute();
			attribute.importXmlNode(this, eAttribute);
			this.add(attribute);
		}
	}
	
	/**
	 * �����^�C�g������v���鑮������������D
	 * @param title �����^�C�g��
	 * @return ������Ȃ�������null
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
