package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;

public class TargetClassList extends ArrayList<TargetClass> {

	/**
	 * 完全限定名が一致するクラスを検索して返す。
	 * @param fullQualifiedName 完全限定名。
	 * @return 見つからない場合はnull
	 */
	public TargetClass searchClass(String fullQualifiedName) {
		TargetClass targetClass = new TargetClass();
		targetClass.setFullQualifiedName(fullQualifiedName);
		int index = this.indexOf(targetClass);
		if (index == -1) return null;
		return this.get(index);
	}
}
