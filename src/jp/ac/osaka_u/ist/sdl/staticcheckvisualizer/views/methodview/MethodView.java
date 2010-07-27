package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview;

import java.util.ArrayList;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

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
	
	/**
	 * 初期状態で生成する属性用カラムの数
	 */
	private final static int DEFAULT_ATTRIBUTE_COLUMN_COUNT = 4;
	
	//TableViewを使用する
	private TableViewer viewer;
	private Table table;
	
	
	//クラス選択を受け取るリスナ
    private ISelectionListener nodeListener = new ISelectionListener() {
        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
     	
            if (!(selection instanceof IStructuredSelection)) return;
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            //List list = structuredSelection.toList(); //List<Object>に相当
            //if (list == null) return;
            if (!(structuredSelection  instanceof TargetClassList)) return;
            
            TargetClassList targetClasses = (TargetClassList)structuredSelection ;
            
            //表示するメソッドリストを生成 
            MethodList methods = new MethodList();
            for (TargetClass targetClass : targetClasses) {
            		methods.addAll(targetClass.getMethods());
          	}
            //属性に応じてカラム更新
            updateAttributeColumns(targetClasses.getAttributeTitles());
            
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
		columnClassName.setText("Class Name");
		columnClassName.setWidth(100);
		//メソッド名
		TableColumn columnName = new TableColumn(table, SWT.NULL);
		columnName.setText("Method Name");
		columnName.setWidth(200);		
		//引数
		TableColumn columnParameter = new TableColumn(table, SWT.NULL);
		columnParameter.setText("Parameters");
		columnParameter.setWidth(100);
		//属性
		//TODO
		for (int i=0; i< DEFAULT_ATTRIBUTE_COLUMN_COUNT; i++) {
			TableColumn columnAttribute = new TableColumn(table, SWT.NULL);
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
	 * テーブルのカラムを更新する．
	 * カラムが足りなかったら作成し，あまったら幅を0にして隠す．
	 * @param methods メソッド情報．
	 */
	public void updateAttributeColumns(ArrayList<String> attributeTitles) {		
		//属性カラム数と属性データ数を比較
		int attributeColumnCount = viewer.getTable().getColumnCount() - FIX_COLUMN_COUNT;
		int diff = attributeColumnCount - attributeTitles.size();
		
		if (diff > 0) {	// カラムがあまっている
			//使用しないカラムは幅0にして隠す
			for (int i=0; i<diff; i++) {
				int noUsedColumnIndex = viewer.getTable().getColumnCount() - 1 - i;
				TableColumn noUsedColumn = viewer.getTable().getColumn(noUsedColumnIndex);
				noUsedColumn.setText("");
				noUsedColumn.setWidth(0);
				noUsedColumn.setResizable(false);
				noUsedColumn.setMoveable(false);
			}
		} else if (diff < 0) { // カラムが足りない
			//足りないカラムを生成する
			for (int i=diff; i <0; i++) {
				int newColumnIndex = viewer.getTable().getColumnCount() - 1 + i;
				TableColumn attributeColumn = new TableColumn(table, SWT.NULL, newColumnIndex);
			}
			viewer.getTable().update();
		}
				
		//属性リストに応じてカラムタイトルを設定
		int i=0;
		for (TableColumn attributeColumn : viewer.getTable().getColumns()) {
			i++;
			int attributeIndex = i - 1 - FIX_COLUMN_COUNT;
			if (attributeIndex < 0) continue;			
			//列が属性の場合
			if (attributeTitles.size() <= attributeIndex) break;
			String title = attributeTitles.get(attributeIndex);
			attributeColumn.setText(title);
			attributeColumn.setWidth(100);
			attributeColumn.setResizable(true);
			attributeColumn.setMoveable(true);
		}
		viewer.getTable().update();	
	}
	

	/**
	 * テーブルカラムのインデックスを属性インデックスに変換する．
	 * @param columnIndex 
	 * @return 対応する属性インデックス．存在しなければnull．
	 */
	public Integer columnIndexToAttributeIndex(int columnIndex, ArrayList<String> attributeTitles) {
		int attributeIndex = columnIndex - FIX_COLUMN_COUNT;
		if (attributeIndex < 0) return null;
		if (attributeIndex >= attributeTitles.size()) return null;
		return attributeIndex;
	}


}
