package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview.model;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;


/**
 * ���\�b�h���r���[�ɕ\�����郂�f���B
 * Method�ɃN���X������ǉ��������́B
 * @author y-mutoh
 *
 */
public class ViewMethod extends Method {
	private String classSimpleName;
	private String classFullQualifiedName;
	
	/**
	 * �R���X�g���N�^�B
	 * @param classSimpleName
	 * @param classFullQualifiedName
	 * @param method
	 */
	public ViewMethod(String classSimpleName, String classFullQualifiedName, Method method) {
		this.classSimpleName = classSimpleName;
		this.classFullQualifiedName = classFullQualifiedName;
		this.setAttributes(method.getAttributes());
		this.setId(method.getId());
		this.setName(method.getName());
		this.setParameter(method.getParameter());
	}

	//�A�N�Z�T
	public String getClassSimpleName() {
		return classSimpleName;
	}

	public void setClassSimpleName(String classSimpleName) {
		this.classSimpleName = classSimpleName;
	}

	public String getClassFullQualifiedName() {
		return classFullQualifiedName;
	}

	public void setClassFullQualifiedName(String classFullQualifiedName) {
		this.classFullQualifiedName = classFullQualifiedName;
	}
}
