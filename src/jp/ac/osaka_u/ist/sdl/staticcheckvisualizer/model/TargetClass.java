package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TargetClass {
	
	/**
	 * ���̃N���X����������TargetClassList�D
	 */
	private TargetClassList targetClasses;

	/**
	 * ���̃N���X�̒P�����D
	 */
	private String simpleName;
	
	/**
	 * ���̃N���X�̊��S���薼�D
	 */
	private String fullQualifiedName;
	
	/**
	 * ���̃N���X�̑�����p�b�P�[�W���D�����Ƀh�b�g���܂܂Ȃ��D
	 */
	private String packageName;

	/**
	 * ���̃N���X�̃\�[�X�t�@�C���D�t���p�X�D
	 */
	private String javaFileName;
	
	/**
	 * ���̃N���X��XML�t�@�C���D�t���p�X�D
	 */
	private String xmlFileName;
	
	/**
	 * ���̃N���X�̃J�o���b�W�D�l�̌ܓ������l�D
	 * �i����΁jpassedCount/methodCount�ƒ�`����D
	 */
	private Integer coverage;
	
	/**
	 * �S���\�b�h���D
	 */
	private Integer methodCount;
	
	/**
	 * �ʉ߂������\�b�h���D
	 */
	private Integer passedCount;
	
	/**
	 * ���̃N���X�Œ�`����Ă��郁�\�b�h�D
	 */
	private MethodList methods;
	/**
	 *  ���̃N���X���Ăяo���Ă���N���X�D
	 */
	private CalleeList callees;
	
	
	/**
	 * XML�t�@�C������N���X���𐶐�����
	 * 
	 * @param xmlfile XML�t�@�C���p�X
	 */
	public TargetClass(String javafilepath, String xmlfilepath) throws Exception {
		this.xmlFileName = xmlfilepath;
		this.javaFileName = javafilepath;
		methods = new MethodList();
		callees = new CalleeList();
		importXml(xmlfilepath);
	}

	public TargetClass() {
		this.simpleName = null;
		this.packageName = null;
		this.fullQualifiedName = null;
		this.coverage = null;
		methods = new MethodList();
		callees = new CalleeList();
	}
	
	public TargetClass(String simpleName, Integer coverage) {
		this.simpleName = simpleName;
		this.coverage = coverage;
		methods = new MethodList();
		callees = new CalleeList();
	}
	
	public TargetClass(String simpleName, String fullQualifiedName, Integer coverage) {
		this.simpleName = simpleName;
		this.fullQualifiedName = fullQualifiedName;
		this.coverage = coverage;
		methods = new MethodList();
		callees = new CalleeList();
	}

	/**
	 *  XML����ǂݍ���
	 * 
	 * @param xmlfile XML�t�@�C���p�X
	 */
	public void importXml(String xmlfile) throws Exception
	{
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
        if (eMethodCount != null)
        	setMethodCount(Integer.valueOf(eMethodCount.getTextContent()));
        //passedCount
        Element ePassedCount = (Element)root.getElementsByTagName("passedCount").item(0);
        if (ePassedCount != null)
        	setPassedCount(Integer.valueOf(ePassedCount.getTextContent()));
        //coverage(methodCount & passedCount�Ƃǂ��炩)
        Element eCoverage = (Element)root.getElementsByTagName("coverage").item(0);
        if (eCoverage != null)
        	setCoverage(Integer.valueOf(eCoverage.getTextContent()));
        //methods
        Node nMethods = root.getElementsByTagName("methods").item(0);
        if (nMethods != null)
        	this.methods.importXmlNode(this, nMethods);


	}
	
	/**
	 * ���S���薼����v����Γ���ƌ��Ȃ��D
	 * @param obj ��r�ΏۃI�u�W�F�N�g�D
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof TargetClass)) return false;
		TargetClass targetClass = (TargetClass)obj;
		return this.getFullQualifiedName().equals(targetClass.getFullQualifiedName());
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((fullQualifiedName == null) ? 0 : fullQualifiedName
						.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		String str = simpleName + "(fullname=" + this.getFullQualifiedName() +  ") ["
					+ "\n, coverage=" + coverage 
					+ "\n, methods=" + methods.toString();
		if (this.callees != null)
			str += "\n, calleeClasses=" + callees.toString();
		str += "\n, attributeTitles=" + this.methods.getAttributeTitles()
					+ "\n]";
		return str;
	}
	
	



	/**
	 * ���S���薼���p�b�P�[�W���ƃN���X�����琶������D
	 */
	public void setFullQualifiedName(){
		if (this.packageName == null)
			this.fullQualifiedName = this.simpleName;
		else if (this.packageName.equals(""))
			this.fullQualifiedName = this.simpleName;
		else
			this.fullQualifiedName =  this.packageName + "." + this.simpleName;
		System.out.println("full=" + this.fullQualifiedName);
	}
	
	/* �ȉ��̓A�N�Z�X���\�b�h */
	
	public TargetClassList getTargetClasses() {
		return targetClasses;
	}
	
	public void setTargetClasses(TargetClassList targetClasses) {
		this.targetClasses = targetClasses;
	}

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
	 * �J�o���b�W��S�����ŋ��߂�D
	 * @param passedCount pass�������\�b�h���D���ꂪ0�ł���ΕS������0�ƒ�`����D
	 * @param methodCount ���v���\�b�h���D
	 */
	public void setCoverage(Integer passedCount, Integer methodCount) {
		if (passedCount == null || methodCount == null) {
			this.coverage = null;
			return;
		}
		if (passedCount <= 0) {
			this.coverage = 0;
			return;
		} 
		this.coverage = Math.round( passedCount * 100 / methodCount);
	}

	public Integer getMethodCount() {
		return methodCount;
	}

	/**
	 * ���\�b�h����setter�D�����ɃJ�o���b�W�ɒl��������D
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

	public CalleeList getCallees() {
		return callees;
	}

	public void setCallees(CalleeList callees) {
		this.callees = callees;
	}





}
