package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.mainview;

import java.awt.Frame;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.StaticCheckVisualizer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

public class MainView extends ViewPart implements ISelectionProvider {
	private StaticCheckVisualizer scv;
	
	private IAction refreshAction;
	
	//フォーム部品
	/**
	 * チェック開始ボタン
	 */
	private Button btnCheck;

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
	 * 画面作成
	 */
	public void createPartControl(Composite parent) {
		scv = Activator.getScv();
		
    	//リフレッシュアクション作成
    	ImageDescriptor icon = Activator.getImageDescriptor("icons/refresh.gif");
		this.refreshAction = new Action("リフレッシュ", icon){
			public void run() {
				//画面のリフレッシュ
				System.out.println("メインビューをリフレッシュ");
				
			}
		};
		//ツールバーに組み込む
		this.getViewSite().getActionBars().getToolBarManager().add(this.refreshAction);
		
		//チェック開始ボタン
		if (!scv.isFinishedCheck()) {
			//静的チェックが開始されていないとき
			//TODO 開始ボタンを設置
			btnCheck = new Button(parent, SWT.PUSH);
			btnCheck.setText("静的チェックを開始");
			btnCheck.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("チェック開始ボタンが押された");
					//実行
					scv.execute();
				}
			});
			return;
		}
		
    	//ウィンドウにグラフを表示
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
	 * 選択されているTargetClassListを返す．
	 */
	@Override
	public ISelection getSelection() {
		System.out.println("MainView.getSelection()");
		if (scv.getSelectedTargetClasses() == null) return null;
		return scv.getSelectedTargetClasses();
		//return new StructuredSelection(scv.getSelectedTargetClasses());
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
