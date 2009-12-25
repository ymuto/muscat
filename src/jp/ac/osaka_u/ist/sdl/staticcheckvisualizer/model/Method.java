package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Method {
	
	/**
	 * ���̃��\�b�h����������MethodList�B
	 */
	private MethodList methodList;
	
	/**
	 * ���\�b�h���B
	 */
	private String name;
	
	/**
	 * ���̃��\�b�h�̈����B
	 */
	private String parameter;
	
	/**
	 * ID�B�i���g�p�j
	 */
	private int id;
	
	/**
	 * �������B
	 */
	private AttributeList attributes;
	
	public Method(String name, String parameter) {
		this.name = name;
		this.parameter = parameter;
		this.id = 0;
		this.attributes = new AttributeList();
	}
	
	public Method() {
		this.name = "";
		this.parameter = "";
		this.id = 0;
		this.attributes = new AttributeList();
	}
	
	/**
	 * XML�m�[�h����ǂݍ���
	 * 
	 * @param nMethod Method�m�[�h
	 */
	public void importXmlNode(MethodList methodList, Node nMethod) {
		this.methodList = methodList;
		Element eMethod = (Element)nMethod;
		//id
		setId(Integer.valueOf(eMethod.getAttribute("id")));
		//name
		Element eName = (Element)eMethod.getElementsByTagName("name").item(0);
		setName(eName.getTextContent());
		//parameter
		NodeList parameterNodeList = eMethod.getElementsByTagName("parameter");
		if (parameterNodeList != null) {
			Element eParameter = (Element)parameterNodeList.item(0);
			if (eParameter != null) {
				setParameter(eParameter.getTextContent());
			}
		}
		//attributes
		Element eAttributes = (Element)eMethod.getElementsByTagName("attributes").item(0);
		this.attributes.importXmlNode(this, eAttributes);
	}
	
	

	@Override
	public String toString() {
		return name + "(" + parameter + ") id=" + id + ", attributes="
				+ attributes.toString() + "]\n";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AttributeList getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributeList attributes) {
		this.attributes = attributes;
	}

	public MethodList getMethodList() {
		return methodList;
	}

	public void setMethodList(MethodList methodList) {
		this.methodList = methodList;
	}
}
