package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview;

import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility.Utility;
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
	
	/**
	 * 固定列（クラス名，メソッド名，引数）の列数
	 */
	public final static int FIX_COLUMN_COUNT = 3;
	
	//TableViewを使用する
	private TableViewer viewer;
	private Table table;
	private Composite tableParent = null;
	
	ArrayContentProvider contentProvider;
	MethodLabelProvider labelProvider;
	
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
         
            //表示するメソッドリストを生成 
            MethodList methods = new MethodList();
            for (Object obj : list) {
            	if (obj instanceof TargetClass) {
            		TargetClass targetClass = (TargetClass)obj;
            		methods.addAll(targetClass.getMethods());
            	}
          	}
            //属性に応じてカラム更新
            updateAttributeColumns(methods);
            
            //表示するデータをセット
            viewer.setInput(methods);
            viewer.update(table, null);
            
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
		table = viewer.getTable();	
		
		//テーブル設定
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(true);
		
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
		//属性
		//TODO
		for (int i=0; i< 3/*Activator.getConfig().getMethodViewAttributeColumnCount()*/; i++) {
			TableColumn columnAttribute = new TableColumn(table, SWT.NULL);
			columnAttribute.setWidth(100);
		}
		
		
		//内容と表示の設定
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new MethodLabelProvider());	

		//リスナ登録
		this.getSite().getPage().addSelectionListener(nodeListener);
				
		//セルをダブルクリックで該当メソッドのソースへジャンプ
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("DoubleClick");
				//対象メソッドを取得
				if (!(event.getSelection() instanceof IStructuredSelection)) return;
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if (!(sel.getFirstElement() instanceof Method)) return;
				Method method = (Method)sel.getFirstElement();
				//ソースコードへジャンプ
				Activator.getScv().getWorkspaceManager().jumpSource(method);
			}
		});	
		
	}
	
	/**
	 * 破棄
	 */
	public void dispose() {
		//リスナ削除
		this.getSite().getPage().removeSelectionListener(nodeListener);
		super.dispose();
	}
		

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
	
	/**
	 * テーブルのカラムを作成する．
	 * @param table 対象となるテーブル．
	 * @param methods メソッド情報．
	 */
	// TODO カラムが足りなかったら作成し，あまったら幅を0にして隠すとか．
	public void updateAttributeColumns(MethodList methods) {	
		//属性リストに応じてカラムを生成
		int i=0;
		for (TableColumn attributeColumn : viewer.getTable().getColumns()) {
			i++;
			int attributeIndex = i - 1 - this.FIX_COLUMN_COUNT;
			if (attributeIndex < 0) continue;
			if (methods.getAttributeTitles().size() <= attributeIndex) break;
			String title = methods.getAttributeTitles().get(attributeIndex);
			attributeColumn.setText(title);
			attributeColumn.setWidth(100);
		}
		table.update();
		
	}
	



}
