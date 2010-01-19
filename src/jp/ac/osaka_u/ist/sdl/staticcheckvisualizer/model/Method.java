package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Method {
	
	/**
	 * ���̃��\�b�h����������MethodList�D
	 */
	private MethodList methodList;
	
	/**
	 * ���\�b�h���D
	 */
	private String name;
	
	/**
	 * ���̃��\�b�h�̈����D
	 */
	private String[] parameters;
	
	/**
	 * ID�D�i���g�p�j
	 */
	private int id;
	
	/**
	 * �������D
	 */
	private AttributeList attributes;
	
	public Method(String name, String[] parameter) {
		this.name = name;
		this.parameters = parameter;
		this.id = 0;
		this.attributes = new AttributeList();
	}
	
	public Method() {
		this.name = "";
		this.parameters = null;
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
				setParametersFromString(eParameter.getTextContent());
			}
		}
		//attributes
		Element eAttributes = (Element)eMethod.getElementsByTagName("attributes").item(0);
		this.attributes.importXmlNode(this, eAttributes);
	}
	
	

	@Override
	public String toString() {
		return name + "(" + parameters + ") id=" + id + ", attributes="
				+ attributes.toString() + "]\n";
	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �������C�ŘA�����ĕ�����Ƃ��ĕԂ�
	 * @return
	 */
	public String getParameterWithComma() {
		if (this.parameters == null) return null;
		if (this.parameters.length <= 0) return "";
		if (this.parameters.length <= 1) return this.parameters[0];
		StringBuilder builder = new StringBuilder();
		builder.append(parameters[0]);
		for (int i=1; i<this.parameters.length; i++){
			builder.append(", ");
			builder.append(this.parameters[i]);
		}
		return builder.toString();
	}

	public String[] getParameters() {
		return parameters;
	}

	/**
	 * �������z��ɕ������ăp�����[�^�ɃZ�b�g����D
	 * @param str �C��؂�̕�����
	 */
	public void setParametersFromString(String str) {
		 setParameters(str.split(", "));
	}
	
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
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
