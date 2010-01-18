package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.mainview;

import java.awt.Frame;
import java.util.Set;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.StaticCheckVisualizer;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace.WorkspaceManager;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

public class MainView extends ViewPart implements ISelectionProvider {
	private StaticCheckVisualizer scv;
	
	//フォーム部品
	/**
	 * チェック開始ボタン
	 */
	private Button btnCheck;
	/**
	 * 更新ボタン
	 */
	private Button btnRefresh;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.MainView";

	/**
	 * The constructor.
	 */
	public MainView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		scv = Activator.getScv();
		
		if (!scv.isFinishedCheck()) {
			//静的チェックが開始されていないとき
			//TODO 開始ボタンを設置
			btnCheck = new Button(parent, SWT.PUSH);
			btnCheck.setText("静的チェックを開始");
			btnCheck.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("チェックボタンが押された");
					//実行
					Activator.getScv().execute();
				}
			});
			//TODO 画面更新ボタンを設置
			btnRefresh = new Button(parent, SWT.PUSH);
			btnRefresh.setText("更新");
			btnRefresh.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO 自動生成されたメソッド・スタブ
					System.out.println("更新ボタンが押された");
				}
			});
			return;
		}
		
    	//ウィンドウ
		Composite c = new Composite(parent,SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(c);
    	frame.add(scv.getJungManager().getVisualizationServer()); //グラフをセット
    		
    	//ノード選択をメソッドビューに伝える
    	this.getSite().setSelectionProvider(this); 
    	
	}
		

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}

	//以下選択されたノードを通知するためのメソッド
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener arg0) {
		// TODO 自動生成されたメソッド・スタブ
	}

	/**
	 * 選択されているTargetClassListを返す。
	 */
	@Override
	public ISelection getSelection() {
		System.out.println("MainView.getSelection()");
		if (scv.getSelectedTargetClasses() == null) return null;
		return new StructuredSelection(scv.getSelectedTargetClasses());
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void setSelection(ISelection arg0) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("MainView.setSelection");
	}

}