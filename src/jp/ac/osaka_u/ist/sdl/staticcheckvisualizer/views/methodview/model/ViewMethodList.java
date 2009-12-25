package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview.model;

import java.util.ArrayList;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;

/**
 * メソッド情報ビューに表示するモデルのリスト。
 * @author y-mutoh
 *
 */
public class ViewMethodList extends ArrayList<ViewMethod> {
	
	public ViewMethodList(){
		
	}
	
//	/**
//	 * クラスリストに含まれている全メソッドに対してクラス名情報を付加する。
//	 * @param targetClasses
//	 */
//	public ViewMethodList(TargetClassList targetClasses) {
//        for (TargetClass targetClass : targetClasses) {
//            //表示するデータを生成。クラス名情報を付加したメソッド情報を追加。
//            this.addAll(targetClass.getSimpleName(), targetClass.getFullQualifiedName(), targetClass.getMethods());
//        }
//	}
	
	/**
	 * メソッド情報リストにクラス名情報を付加してリストに追加する。
	 * @param classSimpleName クラスの単純名。
	 * @param classFullQualifiedName クラスの完全限定名。
	 * @param methods 追加したいメソッド情報リスト。
	 */
	public void addAll(String classSimpleName, String classFullQualifiedName, MethodList methods) {
		//全メソッド要素に対してクラス名情報を付加する。
		for (Method method : methods) {
			this.add(classSimpleName, classFullQualifiedName, method);
		}
	}
	
	/**
	 * メソッド情報にクラス名情報を付加してリストに追加する。
	 * @param classSimpleName クラスの単純名。
	 * @param classFullQualifiedName クラスの完全限定名。
	 * @param methods 追加したいメソッド情報。
	 */
	public void add(String classSimpleName, String classFullQualifiedName, Method method) {
		//メソッド要素に対してクラス名情報を付加する。
		ViewMethod viewMethod = new ViewMethod (classSimpleName, classFullQualifiedName, method);
		this.add(viewMethod);
	}
	
	
}
