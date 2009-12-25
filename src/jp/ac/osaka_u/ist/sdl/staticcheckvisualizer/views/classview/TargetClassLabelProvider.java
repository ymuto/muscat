package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.classview;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


/**
 * テスト用。不要になる予定
 * @author y-mutoh
 *
 */
public class TargetClassLabelProvider extends LabelProvider implements ITableLabelProvider {

	
	/**
	 * TargetClassモデルからセルに表示する文字列を生成する。
	 */
	@Override
	public String getColumnText(Object obj, int index) {
		System.out.println("TargetClassLabelProvider");
		if (!(obj instanceof TargetClass)) return "not TargetClass";
		TargetClass targetClass = (TargetClass)obj;
		switch (index) {
			case 0:return targetClass.getSimpleName();
			case 1:return targetClass.getFullQualifiedName();
			case 2:return targetClass.getCoverage() + "%";
		}
		return "";
	}


	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
