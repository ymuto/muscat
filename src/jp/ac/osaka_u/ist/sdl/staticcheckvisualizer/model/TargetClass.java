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
	 * ���̃N���X�̒P�����B
	 */
	private String simpleName;
	
	/**
	 * ���̃N���X�̊��S���薼�B
	 */
	private String fullQualifiedName;
	
	/**
	 * ���̃N���X�̑�����p�b�P�[�W���B�����Ƀh�b�g���܂܂Ȃ��B
	 */
	private String packageName;

	/**
	 * ���̃N���X�̃\�[�X�t�@�C���B�t���p�X�B
	 */
	private String javaFileName;
	
	/**
	 * ���̃N���X��XML�t�@�C���B�t���p�X�B
	 */
	private String xmlFileName;
	
	/**
	 * ���̃N���X�̃J�o���b�W�B�l�̌ܓ������l�BpassedCount/methodCount�ƒ�`����B
	 */
	private Integer coverage;
	
	/**
	 * �S���\�b�h���B
	 */
	private Integer methodCount;
	
	/**
	 * �ʉ߂������\�b�h���B
	 */
	private Integer passedCount;
	
	/**
	 * ���̃N���X�Œ�`����Ă��郁�\�b�h�B
	 */
	private MethodList methods;
	/**
	 *  ���̃N���X���Ăяo���Ă���N���X�B
	 */
	private List<String> calleeClasses;
	
	
	/**
	 * XML�t�@�C������N���X���𐶐�����
	 * 
	 * @param xmlfile XML�t�@�C���p�X
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
	 *  XML����ǂݍ���
	 * 
	 * @param xmlfile XML�t�@�C���p�X
	 */
	public void importXml(String xmlfile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlfile));
            //���[�g
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
	 * ���S���薼����v����Γ���ƌ��Ȃ��B
	 * @param obj ��r�ΏۃI�u�W�F�N�g�B
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
	 * ���S���薼���p�b�P�[�W���ƃN���X�����琶������B
	 */
	public void setFullQualifiedName(){
		if (getPackageName() == null) this.fullQualifiedName = getSimpleName();
		this.fullQualifiedName =  getPackageName() + "." + getSimpleName();
	}

	/* �ȉ��̓A�N�Z�X���\�b�h */
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
	 * �J�o���b�W��S�����ŋ��߂�B
	 * @param passedCount pass�������\�b�h���B���ꂪ0�ł���ΕS������0�ƒ�`����B
	 * @param methodCount ���v���\�b�h���B
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
	 * ���\�b�h����setter�B�����ɃJ�o���b�W�ɒl��������B
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
	 * �ʉ߂������\�b�h
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
