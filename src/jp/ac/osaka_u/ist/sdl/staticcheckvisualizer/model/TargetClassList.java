package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility.Utility;

public class TargetClassList extends ArrayList<TargetClass> implements IStructuredSelection{
	
	public static final TargetClassList EMPTY = new TargetClassList();
	
	/**
	 * 属性タイトル．メソッド情報ビューで使用．
	 */
	private ArrayList<String> attributeTitles;
	
	public TargetClassList() {
		this.attributeTitles = new ArrayList<String>();
	}

	/**
	 * 完全限定名が一致するクラスを検索して返す．
	 * @param fullQualifiedName 完全限定名．
	 * @return 見つからない場合はnull
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
		//TargetClasslistを設定する
		targetClass.setTargetClasses(this);
		return ret;
	}
	
	public void update() {
		this.updateAttributeTitles();
	}

	/**
	 * 属性タイトル一覧を更新する．
	 */
	private void updateAttributeTitles() {
		this.attributeTitles.clear();
		for (TargetClass targetClass : this) {
			Utility.addAllNoOverlaps(this.attributeTitles, targetClass.getMethods().getAttributeTitles());
		}
	}
	
	//TODO デバッグ用．
	//クラス名一覧を表示する．
	public void printClassNames() {
		for (TargetClass targetClass : this) {
			System.out.println(targetClass.getFullQualifiedName());
		}
	}

	//アクセサ
	public ArrayList<String> getAttributeTitles() {
		return attributeTitles;
	}

	//implement
	@Override
	public Object getFirstElement() {
		// TODO 自動生成されたメソッド・スタブ
		if (this.size() <= 0) return null;
		return this.get(0);
	}

	@Override
	public List toList() {
		// TODO 自動生成されたメソッド・スタブ
		return this;
	}	
	
}
