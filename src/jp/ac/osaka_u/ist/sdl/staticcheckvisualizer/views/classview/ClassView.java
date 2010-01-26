package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.classview;

import java.util.List;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.dialogs.ViewLabelProvider;
import org.eclipse.ui.part.*;


public class ClassView extends ViewPart {
	
	//TableViewを使用する
	private TableViewer viewer;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.ClassView";

	/**
	 * The constructor.
	 */
	public ClassView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		//ここにビューの内容を書く
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		//テーブル取得
		Table table = viewer.getTable();
		//テーブル設定
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		//カラム作成
		TableColumn column1 = new TableColumn(table, SWT.NULL);
		column1.setText("単純名");
		column1.setWidth(200);
		TableColumn column2 = new TableColumn(table, SWT.NULL);
		column2.setText("完全限定名");
		column2.setWidth(200);
		TableColumn column3 = new TableColumn(table, SWT.NULL);
		column3.setText("coverage");
		column3.setWidth(200);
		//設定
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new TargetClassLabelProvider());
		viewer.setInput(getItems());
		
		//選択オブジェクトを提供する
		this.getSite().setSelectionProvider(viewer);
	
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
//	
//	/**
//	 * 表示するクラス情報を生成する．テストデータ．
//	 * @return
//	 */
	private List<TargetClass> getItems() {
		TargetClassList targetClasses = new TargetClassList();
		//class1
		TargetClass targetClass1 = new TargetClass("Class1","package1.Class1",10);
		MethodList methodList1 = new MethodList();
//		methodList1.add(new Method("method4","int"));
//		methodList1.add(new Method("method5","int,int"));
//		methodList1.add(new Method("method6","int,boolean"));
		targetClass1.setMethods(methodList1);
		targetClasses.add(targetClass1);
//		//class2
//		TargetClass targetClass2 = new TargetClass("Class2","package1.Class2",20);
//		MethodList methodList2 = new MethodList();
//		methodList2.add(new Method("method7","int"));
//		methodList2.add(new Method("method8","String"));
//		methodList2.add(new Method("method9",""));
//		targetClass2.setMethods(methodList2);
//		targetClasses.add(targetClass2);
//		
		return targetClasses;
	}


}
