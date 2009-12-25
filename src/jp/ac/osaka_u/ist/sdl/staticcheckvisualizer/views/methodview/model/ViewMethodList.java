package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview.model;

import java.util.ArrayList;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;

/**
 * ���\�b�h���r���[�ɕ\�����郂�f���̃��X�g�B
 * @author y-mutoh
 *
 */
public class ViewMethodList extends ArrayList<ViewMethod> {
	
	public ViewMethodList(){
		
	}
	
//	/**
//	 * �N���X���X�g�Ɋ܂܂�Ă���S���\�b�h�ɑ΂��ăN���X������t������B
//	 * @param targetClasses
//	 */
//	public ViewMethodList(TargetClassList targetClasses) {
//        for (TargetClass targetClass : targetClasses) {
//            //�\������f�[�^�𐶐��B�N���X������t���������\�b�h����ǉ��B
//            this.addAll(targetClass.getSimpleName(), targetClass.getFullQualifiedName(), targetClass.getMethods());
//        }
//	}
	
	/**
	 * ���\�b�h��񃊃X�g�ɃN���X������t�����ă��X�g�ɒǉ�����B
	 * @param classSimpleName �N���X�̒P�����B
	 * @param classFullQualifiedName �N���X�̊��S���薼�B
	 * @param methods �ǉ����������\�b�h��񃊃X�g�B
	 */
	public void addAll(String classSimpleName, String classFullQualifiedName, MethodList methods) {
		//�S���\�b�h�v�f�ɑ΂��ăN���X������t������B
		for (Method method : methods) {
			this.add(classSimpleName, classFullQualifiedName, method);
		}
	}
	
	/**
	 * ���\�b�h���ɃN���X������t�����ă��X�g�ɒǉ�����B
	 * @param classSimpleName �N���X�̒P�����B
	 * @param classFullQualifiedName �N���X�̊��S���薼�B
	 * @param methods �ǉ����������\�b�h���B
	 */
	public void add(String classSimpleName, String classFullQualifiedName, Method method) {
		//���\�b�h�v�f�ɑ΂��ăN���X������t������B
		ViewMethod viewMethod = new ViewMethod (classSimpleName, classFullQualifiedName, method);
		this.add(viewMethod);
	}
	
	
}
