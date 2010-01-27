package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility.Utility;

public class TargetClassList extends ArrayList<TargetClass> implements IStructuredSelection{
	
	public static final TargetClassList EMPTY = new TargetClassList();
	
	/**
	 * �����^�C�g���D���\�b�h���r���[�Ŏg�p�D
	 */
	private ArrayList<String> attributeTitles;
	
	public TargetClassList() {
		this.attributeTitles = new ArrayList<String>();
	}

	/**
	 * ���S���薼����v����N���X���������ĕԂ��D
	 * @param fullQualifiedName ���S���薼�D
	 * @return ������Ȃ��ꍇ��null
	 */
	public TargetClass searchClass(String fullQualifiedName) {
		TargetClass targetClass = new TargetClass();
		targetClass.setFullQualifiedName(fullQualifiedName);
		int index = this.indexOf(targetClass);
		if (index == -1) return null;
		return this.get(index);
	}
	
	public boolean add(TargetClass targetClass) {
		boolean ret = super.add(targetClass);
		//TargetClasslist��ݒ肷��
		targetClass.setTargetClasses(this);
		return ret;
	}
	
	public void update() {
		this.updateAttributeTitles();
	}

	/**
	 * �����^�C�g���ꗗ���X�V����D
	 */
	private void updateAttributeTitles() {
		this.attributeTitles.clear();
		for (TargetClass targetClass : this) {
			Utility.addAllNoOverlaps(this.attributeTitles, targetClass.getMethods().getAttributeTitles());
		}
	}
	
	//TODO �f�o�b�O�p�D
	//�N���X���ꗗ��\������D
	public void printClassNames() {
		for (TargetClass targetClass : this) {
			System.out.println(targetClass.getFullQualifiedName());
		}
	}

	//�A�N�Z�T
	public ArrayList<String> getAttributeTitles() {
		return attributeTitles;
	}

	//implement
	@Override
	public Object getFirstElement() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		if (this.size() <= 0) return null;
		return this.get(0);
	}

	@Override
	public List toList() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return this;
	}	
	
}
