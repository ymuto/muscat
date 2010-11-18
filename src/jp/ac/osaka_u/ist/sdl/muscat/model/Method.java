package jp.ac.osaka_u.ist.sdl.muscat.model;

import java.util.ArrayList;

import jp.ac.osaka_u.ist.sdl.muscat.Activator;
import jp.ac.osaka_u.ist.sdl.muscat.utility.Utility;
import jp.ac.osaka_u.ist.sdl.muscat.workspace.WorkspaceManager;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Method {
	
	/**
	 * このメソッドが所属するMethodList．
	 */
	private MethodList methodList;
	
	/**
	 * メソッド名．
	 */
	private String name;
	
	/**
	 * このメソッドの引数．（完全限定名）
	 */
	private String[] parameters;
	
	/**
	 * このメソッドの引数．（ソースコード上での形）
	 */
	private String[] parametersSourceCode;
	
	/**
	 * ID．（未使用）
	 */
	private int id;
	
	/**
	 * 属性情報．
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
	 * XMLノードから読み込む
	 * 
	 * @param nMethod Methodノード
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
		return name + "(" + this.getParameterWithComma() + ") id=" + id + ", attributes="
				+ attributes.toString() + "]\n";
	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 完全限定名の引数を「, 」で連結して文字列として返す
	 * @return
	 */
	public String getParameterWithComma() {
		return  Utility.concatArray(this.parameters, ", ");
	}

	public String[] getParameters() {
		return parameters;
	}

	/**
	 * 文字列を配列に分割してパラメータにセットする．
	 * @param str ，区切りの文字列
	 */
	public void setParametersFromString(String str) {
		 setParameters(str.split(", "));
	}
	
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
		this.setParametersSourceCode();
	}

	public void setParametersSourceCode() {
		this.parametersSourceCode = Activator.getScv().getWorkspaceManager().getParametersFromSource(this);
	}

	public String[] getParametersSourceCode() {
		return parametersSourceCode;
	}
	
	/**
	 * ソースコード上の引数を「, 」で連結して文字列として返す
	 * @return
	 */
	public String getParameterSourceCodeWithComma() {
		return  Utility.concatArray(this.parametersSourceCode, ", ");
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
