package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;

public class TargetClassList extends ArrayList<TargetClass> {

	/**
	 * ���S���薼����v����N���X���������ĕԂ��B
	 * @param fullQualifiedName ���S���薼�B
	 * @return ������Ȃ��ꍇ��null
	 */
	public TargetClass searchClass(String fullQualifiedName) {
		TargetClass targetClass = new TargetClass();
		targetClass.setFullQualifiedName(fullQualifiedName);
		int index = this.indexOf(targetClass);
		if (index == -1) return null;
		return this.get(index);
	}

	//TODO �f�o�b�O�p�B
	//�N���X���ꗗ��\������B
	public void printClassNames() {
		for (TargetClass targetClass : this) {
			System.out.println(targetClass.getFullQualifiedName());
		}
	}
	
	
}
