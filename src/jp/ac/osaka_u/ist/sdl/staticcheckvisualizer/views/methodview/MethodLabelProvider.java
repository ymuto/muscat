package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview;

import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Attribute;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * 同時に属性の内容に応じて文字色を設定する．
 * @author y-mutoh
 *
 */
public class MethodLabelProvider extends LabelProvider implements ITableLabelProvider,ITableColorProvider {
	private Display display;
	
	private ArrayList<String> attributeTitles;
	
    /**
     * 属性の値に応じて色分けする設定．（未使用）
     */
	private HashMap<String, Color> attributeColors;
	
	/**
	 * コンストラクタ．
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
	 * Methodモデルからセルに表示する文字列を生成する．
	 */
	@Override
	public String getColumnText(Object obj, int index) {
		System.out.println("MethodLabelProvider index=" + index);
		if (!(obj instanceof Method)) return "";
		Method method = (Method) obj;
		switch (index) {
			case 0: return method.getMethodList().getTargetClass().getFullQualifiedName();
			case 1: return method.getName();
			case 2: return method.getParameterWithComma();
		}
		
		//3列目以降は属性．列タイトルと属性タイトルが一致する属性の値を返す．
		if (attributeTitles == null) return "";
		if (attributeTitles.size() <= 0) return "";
		//属性タイトルを取得する
		System.out.println("MethodLabelProvider titles=" + attributeTitles.toString());
		String title = attributeTitles.get(index - 3);
		//タイトルが一致する属性を検索する
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
	 * セルの内容に応じて文字色を設定する．
	 */
	public Color getForeground(Object element, int columnIndex) {
		if (columnIndex < 3) return null;
		if (!(element instanceof Method)) return null;
		Method method = (Method)element;
		//属性の値に応じて色をつける
		Attribute attribute = method.getAttributes().get(columnIndex - 3);
		return  attributeColors.get(attribute.getValue());
	}
	
	//アクセサ
	public void setAttributeTitles(ArrayList<String> attributeTitles) {
		this.attributeTitles = attributeTitles;
	}

	
	
}
