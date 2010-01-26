package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model;

import java.util.ArrayList;


public class MyNodeList extends ArrayList<MyNode> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ���S���薼����v����m�[�h����������D
	 * @param fullQualifiedName ���S���薼�D
	 * @return ������Ȃ��ꍇ��null
	 */
	public MyNode searchMyNode(String fullQualifiedName) {
		MyNode node = new MyNode();
		node.setFullQualifiedName(fullQualifiedName);
		int index = this.indexOf(node);
		if (index == -1) return null;
		return this.get(index);
	}
}
