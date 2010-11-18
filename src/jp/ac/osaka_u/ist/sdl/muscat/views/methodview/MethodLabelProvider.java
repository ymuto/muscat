package jp.ac.osaka_u.ist.sdl.muscat.views.methodview;

import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.osaka_u.ist.sdl.muscat.Activator;
import jp.ac.osaka_u.ist.sdl.muscat.config.Config;
import jp.ac.osaka_u.ist.sdl.muscat.model.Attribute;
import jp.ac.osaka_u.ist.sdl.muscat.model.Method;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * �����ɑ����̓��e�ɉ����ĕ����F��ݒ肷��D
 * @author y-mutoh
 *
 */
public class MethodLabelProvider extends LabelProvider implements ITableLabelProvider,ITableColorProvider {
	/**
	 * Method���f������Z���ɕ\�����镶����𐶐�����D
	 */
	@Override
	public String getColumnText(Object obj, int index) {
		System.out.println("MethodLabelProvider index=" + index);
		if (!(obj instanceof Method)) return "";
		Method method = (Method) obj;
		switch (index) {
			case 0: return method.getMethodList().getTargetClass().getSimpleName();
			case 1: return method.getName();
			case 2: return method.getParameterSourceCodeWithComma();
			//case 2: return method.getParameterWithComma();
		}
		
		//3��ڈȍ~�͑����D��^�C�g���Ƒ����^�C�g������v���鑮���̒l��Ԃ��D
		int attributeIndex = index - MethodView.FIX_COLUMN_COUNT;
		ArrayList<String> attributeTitles = method.getMethodList().getTargetClass().getTargetClasses().getAttributeTitles();
		if (attributeTitles == null) return "";
		if (attributeTitles.size() <= 0 || attributeTitles.size() <= attributeIndex) return "";
		//�����^�C�g�����擾����
		System.out.println("MethodLabelProvider titles=" + attributeTitles.toString());
		String title = attributeTitles.get(attributeIndex);
		//�^�C�g������v���鑮������������
		Attribute attribute = method.getAttributes().searchAttributeFromTitle(title);
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
	 * �Z���̓��e�ɉ����ĕ����F��ݒ肷��D
	 */
	public Color getForeground(Object element, int columnIndex) {
		int attributeIndex = columnIndex - MethodView.FIX_COLUMN_COUNT;
		if (attributeIndex < 0) return null;
		if (!(element instanceof Method)) return null;
		Method method = (Method)element;
		ArrayList<String> attributeTitles = method.getMethodList().getTargetClass().getTargetClasses().getAttributeTitles();
		if (attributeTitles.size() <= attributeIndex) return null;
		//�����̒l�ɉ����ĐF������
		//�����^�C�g�����擾����
		String title = attributeTitles.get(attributeIndex);
		//�^�C�g������v���鑮������������
		Attribute attribute = method.getAttributes().searchAttributeFromTitle(title);
		if (attribute == null) return null;
		return Config.getInstance().getAttributeColors().get(attribute.getValue());
	}
		
	
}
