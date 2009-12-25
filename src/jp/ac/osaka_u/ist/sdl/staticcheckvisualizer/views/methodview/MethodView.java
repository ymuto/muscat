package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview;

import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview.model.ViewMethod;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview.model.ViewMethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace.WorkspaceManager;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.*;


public class MethodView extends ViewPart {
	
	//TableViewを使用する
	private TableViewer viewer;
	
	//表示形式
	MethodLabelProvider methodLabelProvider;
	
	//属性を表示するカラム
	private ArrayList<TableColumn> attributeColumns;
	
	//クラス選択を受け取るリスナ
    private ISelectionListener nodeListener = new ISelectionListener() {
        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
            if (!(selection instanceof IStructuredSelection)) return;
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            List list = structuredSelection.toList(); //List<Object>に相当
            if (list == null) return;
            //TODO 属性に応じてカラム更新
            //updateColumns(viewer.getTable(), targetClass.getMethods());
            //TODO 属性タイトルをセット
        	ArrayList<String> titles = new ArrayList<String>();
        	titles.add("passed/failed");
            methodLabelProvider.setAttributeTitles(titles);
            //表示するデータをセット
            ViewMethodList viewMethodList = new ViewMethodList();
            for (Object obj : list) {
            	System.out.println("obj="+obj.getClass());
            	if (obj instanceof TargetClass) {
            		TargetClass targetClass = (TargetClass)obj;
            		viewMethodList.addAll(targetClass.getSimpleName(), targetClass.getFullQualifiedName(), targetClass.getMethods());
            	}
            }
            viewer.setInput(viewMethodList);
            System.out.println("selectionChaged完了");
            
        }
    };

	
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.MethodView";

	/**
	 * The constructor.
	 */
	public MethodView() {
		attributeColumns = new ArrayList<TableColumn>();
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
		//列ヘッダ作成
		//クラス名
		TableColumn columnClassName = new TableColumn(table, SWT.NULL);
		columnClassName.setText("クラス");
		columnClassName.setWidth(100);
		//メソッド名
		TableColumn columnName = new TableColumn(table, SWT.NULL);
		columnName.setText("メソッド名");
		columnName.setWidth(200);		
		//引数
		TableColumn columnParameter = new TableColumn(table, SWT.NULL);
		columnParameter.setText("引数");
		columnParameter.setWidth(100);
		//TODO passed/failed
		TableColumn passedParameter = new TableColumn(table, SWT.NULL);
		passedParameter.setText("passed/failed");
		passedParameter.setWidth(100);
			
		//TODO セルダブルクリック時の動作(なぜか実行されない)
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("DoubleClick");
				//対象メソッドを取得
				//IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				//ViewMethod viewMethod = (ViewMethod) sel.getFirstElement();
				//System.out.println(viewMethod.getClassFullQualifiedName() + "." + viewMethod.getName());
				//ソースコードへジャンプ
				//WorkspaceManager.jumpSource(viewMethod.getMethodList().getTargetClass().getJavaFileName());
			}
		});	
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO 自動生成されたメソッド・スタブ
				System.out.println("selectionChanged");
			}
			
		});
		
		//内容と表示の設定
		viewer.setContentProvider(new ArrayContentProvider());
		methodLabelProvider = new MethodLabelProvider();
		viewer.setLabelProvider(methodLabelProvider);
		
	

		//リスナ登録
		this.getSite().getPage().addSelectionListener(nodeListener);
		
	}
	
	/**
	 * 破棄
	 */
	public void dispose() {
		//リスナ削除
		this.getSite().getPage().removeSelectionListener(nodeListener);
	}
		

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
	
	/**
	 * 属性タイトルに対応するようにテーブルのカラムを更新する。
	 * @param table 対象となるテーブル。
	 * @param methods メソッド情報。
	 */
	public void updateColumns(Table table, MethodList methods) {
		System.out.println("createColumns titles=" + methods.getAttributeTitles());
		//現在ある属性カラムをすべて削除
		for (TableColumn column : attributeColumns) {
			column.dispose();
		}
		attributeColumns.clear();
		//属性リストに応じてカラムを生成
		for (String title : methods.getAttributeTitles()) {
			TableColumn attributeColumn = new TableColumn(table, SWT.NULL);
			attributeColumn.setText(title);
			attributeColumns.add(attributeColumn);
		}
	}
	
//	private List<Method> getItems() {
//		MethodList methodList = new MethodList();
//		
//		methodList.add(new Method("method1","int"));
//		methodList.add(new Method("method2","int,int"));
//		methodList.add(new Method("method3","int,boolean"));
//		
//		return methodList;
//	}
	



}