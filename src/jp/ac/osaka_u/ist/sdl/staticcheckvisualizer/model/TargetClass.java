package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TargetClass {
	/**
	 * このクラスの単純名。
	 */
	private String simpleName;
	
	/**
	 * このクラスの完全限定名。
	 */
	private String fullQualifiedName;
	
	/**
	 * このクラスの属するパッケージ名。末尾にドットを含まない。
	 */
	private String packageName;

	/**
	 * このクラスのソースファイル。フルパス。
	 */
	private String javaFileName;
	
	/**
	 * このクラスのXMLファイル。フルパス。
	 */
	private String xmlFileName;
	
	/**
	 * このクラスのカバレッジ。四捨五入した値。passedCount/methodCountと定義する。
	 */
	private Integer coverage;
	
	/**
	 * 全メソッド数。
	 */
	private Integer methodCount;
	
	/**
	 * 通過したメソッド数。
	 */
	private Integer passedCount;
	
	/**
	 * このクラスで定義されているメソッド。
	 */
	private MethodList methods;
	/**
	 *  このクラスが呼び出しているクラス。
	 */
	private List<String> calleeClasses;
	
	
	/**
	 * XMLファイルからクラス情報を生成する
	 * 
	 * @param xmlfile XMLファイルパス
	 */
	public TargetClass(String javafilepath, String xmlfilepath) {
		methods = new MethodList();
		calleeClasses = new ArrayList<String>();
		this.xmlFileName = xmlfilepath;
		this.javaFileName = javafilepath;
		importXml(xmlfilepath);
	}

	public TargetClass() {
		this.simpleName = null;
		this.packageName = null;
		this.fullQualifiedName = null;
		this.coverage = null;
		methods = new MethodList();
		calleeClasses = new ArrayList<String>();
	}
	
	public TargetClass(String simpleName, Integer coverage) {
		this.simpleName = simpleName;
		this.coverage = coverage;
		methods = new MethodList();
		calleeClasses = new ArrayList<String>();
	}
	
	public TargetClass(String simpleName, String fullQualifiedName, Integer coverage) {
		this.simpleName = simpleName;
		this.fullQualifiedName = fullQualifiedName;
		this.coverage = coverage;
		methods = new MethodList();
		calleeClasses = new ArrayList<String>();
	}

	/**
	 *  XMLから読み込む
	 * 
	 * @param xmlfile XMLファイルパス
	 */
	public void importXml(String xmlfile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlfile));
            //ルート
            Element root = document.getDocumentElement();
            //packageName
            Element ePackageName = (Element)root.getElementsByTagName("packageName").item(0);
            setPackageName(ePackageName.getTextContent());
            //simpleName
            Element eSimpleName = (Element)root.getElementsByTagName("simpleName").item(0);
            String simpleNameWithDollar = eSimpleName.getTextContent();       
            setSimpleName(simpleNameWithDollar.replace("$", "."));            
            //methodCount
            Element eMethodCount = (Element)root.getElementsByTagName("methodCount").item(0);
            setMethodCount(Integer.valueOf(eMethodCount.getTextContent()));
            //passedCount
            Element ePassedCount = (Element)root.getElementsByTagName("passedCount").item(0);
            setPassedCount(Integer.valueOf(ePassedCount.getTextContent()));
            //methods
            Node nMethods = root.getElementsByTagName("methods").item(0);
            this.methods.importXmlNode(this, nMethods);
        } catch (Exception e) {
        	System.out.println(e.toString());
        }

	}
	
	/**
	 * 完全限定名が一致すれば同一と見なす。
	 * @param obj 比較対象オブジェクト。
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		//System.out.println("obj=" + obj.toString());	
		TargetClass targetClass = (TargetClass)obj;
		return this.getFullQualifiedName().equals(targetClass.getFullQualifiedName());
	}
	
	
	@Override
	public String toString() {
		return simpleName + "(" + this.getFullQualifiedName() +  ") ["
					+ "\n, coverage=" + coverage 
					+ "\n, methods=" + methods.toString() 
					+ "\n, calleeClasses=" + calleeClasses.toString()
					+ "\n, attributeTitles=" + this.methods.getAttributeTitles()
					+ "]";
	}
	
	
	/**
	 * 完全限定名をパッケージ名とクラス名から生成する。
	 */
	public void setFullQualifiedName(){
		if (getPackageName() == null) this.fullQualifiedName = getSimpleName();
		this.fullQualifiedName =  getPackageName() + "." + getSimpleName();
	}

	/* 以下はアクセスメソッド */
	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
		this.setFullQualifiedName();
	}

	public String getPackageName() {
		return packageName;
	}
	
	public void setFullQualifiedName(String fullQualifiedName) {
		this.fullQualifiedName = fullQualifiedName;
	}
	
	public String getFullQualifiedName() {
		return this.fullQualifiedName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
		this.setFullQualifiedName();
	}

	public String getJavaFileName() {
		return javaFileName;
	}

	public void setJavaFileName(String javaFileName) {
		this.javaFileName = javaFileName;
	}

	public String getXmlFileName() {
		return xmlFileName;
	}

	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	public Integer getCoverage() {
		return coverage;
	}

	public void setCoverage(Integer coverage) {
		this.coverage = coverage;
	}
	
	/**
	 * カバレッジを百分率で求める。
	 * @param passedCount passしたメソッド数。これが0であれば百分率を0と定義する。
	 * @param methodCount 合計メソッド数。
	 */
	public void setCoverage(Integer passedCount, Integer methodCount) {
		if (passedCount == null || methodCount == null) this.coverage = null;
		if (passedCount <= 0) {
			this.coverage = 0;
		} 
		else {
			this.coverage = Math.round( passedCount * 100 / methodCount);
		}
	}

	public Integer getMethodCount() {
		return methodCount;
	}

	/**
	 * メソッド数のsetter。同時にカバレッジに値を代入する。
	 * @param methodCount
	 */
	public void setMethodCount(Integer methodCount) {
		this.methodCount = methodCount;
		if (this.passedCount != null)
			this.setCoverage(passedCount, methodCount);
	}

	public Integer getPassedCount() {
		return passedCount;
	}

	/**
	 * 通過したメソッド
	 * @param passedCount
	 */
	public void setPassedCount(Integer passedCount) {
		this.passedCount = passedCount;
		if (this.methodCount != null)
			this.setCoverage(passedCount, methodCount);
	}

	public MethodList getMethods() {
		return methods;
	}

	public void setMethods(MethodList methods) {
		this.methods = methods;
	}

	public List<String> getCalleeClasses() {
		return calleeClasses;
	}

	public void setCalleeClasses(List<String> calleeClasses) {
		this.calleeClasses = calleeClasses;
	}

}
