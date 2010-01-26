package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model;

import java.util.ArrayList;


public class MyNodeList extends ArrayList<MyNode> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 完全限定名が一致するノードを検索する．
	 * @param fullQualifiedName 完全限定名．
	 * @return 見つからない場合はnull
	 */
	public MyNode searchMyNode(String fullQualifiedName) {
		MyNode node = new MyNode();
		node.setFullQualifiedName(fullQualifiedName);
		int index = this.indexOf(node);
		if (index == -1) return null;
		return this.get(index);
	}
}
