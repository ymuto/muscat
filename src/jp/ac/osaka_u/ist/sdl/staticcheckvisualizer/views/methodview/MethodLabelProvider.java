package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview;

import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Attribute;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview.model.ViewMethod;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * �����ɑ����̓��e�ɉ����ĕ����F��ݒ肷��B
 * @author y-mutoh
 *
 */
public class MethodLabelProvider extends LabelProvider implements ITableLabelProvider,ITableColorProvider {
	private Display display;
	
	private ArrayList<String> attributeTitles;
	
    /**
     * �����̒l�ɉ����ĐF��������ݒ�B�i���g�p�j
     */
	private HashMap<String, Color> attributeColors;
	
	/**
	 * �R���X�g���N�^�B
	 */
	public MethodLabelProvider(Display display) {
		this.display = display;
    	attributeColors = Activator.getConfig().getAttributeColors();
	}
	
	public MethodLabelProvider() {
		this.display = null;
    	attributeColors = Activator.getConfig().getAttributeColors();

	}
	


	
	/**
	 * Method���f������Z���ɕ\�����镶����𐶐�����B
	 */
	@Override
	public String getColumnText(Object obj, int index) {
		System.out.println("MethodLabelProvider index=" + index);
		if (!(obj instanceof ViewMethod)) return "";
		ViewMethod viewMethod = (ViewMethod)obj;
		//0��ڂ̓N���X���A1��ڂ̓��\�b�h���A2��ڂ͈���
		switch (index) {
			case 0: return viewMethod.getClassSimpleName();
			case 1: return viewMethod.getName();
			case 2: return viewMethod.getParameter();
		}
		//3��ڈȍ~�͑����B��^�C�g���Ƒ����^�C�g������v���鑮���̒l��Ԃ��B
		if (attributeTitles == null) return "";
		if (attributeTitles.size() <= 0) return "";
		//�����^�C�g�����擾����
		System.out.println("MethodLabelProvider titles=" + attributeTitles.toString());
		String title = attributeTitles.get(index - 3);
		//�^�C�g������v���鑮������������
		Attribute attribute = viewMethod.getAttributes().searchAttributeFromTitle(title);
		if (attribute == null) return "";
		return attribute.getValue();
	}


	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}

	@Override
	/**
	 * �Z���̓��e�ɉ����ĕ����F��ݒ肷��B
	 */
	public Color getForeground(Object element, int columnIndex) {
		if (columnIndex < 3) return null;
		if (!(element instanceof Method)) return null;
		Method method = (Method)element;
		//�����̒l�ɉ����ĐF������
		Attribute attribute = method.getAttributes().get(columnIndex - 3);
		return  attributeColors.get(attribute.getValue());
	}
	
	//�A�N�Z�T
	public void setAttributeTitles(ArrayList<String> attributeTitles) {
		this.attributeTitles = attributeTitles;
	}

	
	
}
