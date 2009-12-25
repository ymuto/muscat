package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview.model;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;


/**
 * メソッド情報ビューに表示するモデル。
 * Methodにクラス名情報を追加したもの。
 * @author y-mutoh
 *
 */
public class ViewMethod extends Method {
	private String classSimpleName;
	private String classFullQualifiedName;
	
	/**
	 * コンストラクタ。
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

	//アクセサ
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
