package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;
import java.util.HashSet;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MethodList extends ArrayList<Method> {
	
	/**
	 * �����̃^�C�g���ꗗ���D
	 */
	private ArrayList<String> attributeTitles;
	
	/**
	 * ��������N���X�D
	 */
	private TargetClass targetClass = null;
	
	/**
	 * �R���X�g���N�^�D
	 */
	public MethodList() {
		attributeTitles = new ArrayList<String>();
	}
	
	/**
	 * XML�m�[�h����ǂݍ���
	 * @param nMethods methods�m�[�h
	 */
	public void importXmlNode(TargetClass targetClass, Node nMethods) {
		this.targetClass = targetClass;
		Element eMethods = (Element)nMethods;
		NodeList nlMethod = eMethods.getElementsByTagName("method");
		//method�ɑ΂��ď���
		for (int i=0; i<nlMethod.getLength(); i++) {
			Element eMethod = (Element)nlMethod.item(i);
			Method method = new Method();
			method.importXmlNode(this, eMethod);
			this.add(method);
			//�����^�C�g���ꗗ���X�V
			for (Attribute attribute : method.getAttributes()) {
				String title = attribute.getTitle();
				if (this.attributeTitles.indexOf(title) == -1) {
					attributeTitles.add(title);
				}
			}
		}
	}
	
	
	/**
	 * ���\�b�h���X�g�𕹍�����D
	 * �����^�C�g���ꗗ���̏����͎�����D
	 * @param methods �ǉ����������\�b�h���X�g�D
	 */
	// method��methodList���X�V���ׂ������C���������method.getMethodList().getTargetClass()������
	public void addAll(MethodList methods) {
		super.addAll(methods);
		//�����^�C�g���ꗗ���𕹍�����DSet����̂ŏ��Ԃ͎�����D
		HashSet<String> set1 = new HashSet<String>(methods.attributeTitles);
		HashSet<String> set2 = new HashSet<String>(this.attributeTitles);
		set1.addAll(set2);
		this.attributeTitles = new ArrayList<String>(set1);
	}
	

	//�A�N�Z�T
	/**
	 * �����^�C�g���ꗗ�����擾����D
	 */
	public ArrayList<String> getAttributeTitles() {
		return attributeTitles;
	}
	
	public TargetClass getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(TargetClass targetClass) {
		this.targetClass = targetClass;
	}
}
