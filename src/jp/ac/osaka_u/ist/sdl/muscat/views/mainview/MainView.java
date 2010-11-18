package jp.ac.osaka_u.ist.sdl.muscat.views.mainview;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;

import jp.ac.osaka_u.ist.sdl.muscat.Activator;
import jp.ac.osaka_u.ist.sdl.muscat.StaticCheckVisualizer;
import jp.ac.osaka_u.ist.sdl.muscat.config.Config;
import jp.ac.osaka_u.ist.sdl.muscat.jung.JungManager;
import jp.ac.osaka_u.ist.sdl.muscat.utility.Utility;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.*;

public class MainView extends ViewPart implements ISelectionProvider {
	private StaticCheckVisualizer scv;
	
	/**
	 * アクション．
	 */
	private IAction refreshAction;
	private IAction executeAction;
	private IAction exportAction;

	
	/**
	 * グラフを表示する領域（SWT）
	 */
	private Composite composite;
	
	/**
	 * グラフを表示する領域（AWT）
	 */
	private Frame frame;
	

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "jp.ac.osaka_u.ist.sdl.muscat.views.MainView";

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
		
		//ツールバー作成
		createActions();
		
		//ウィンドウにグラフ領域を作成	
		composite = new Composite(parent,SWT.EMBEDDED);
		frame = SWT_AWT.new_Frame(composite);
		
    	//ノード選択をメソッドビューに伝える
    	this.getSite().setSelectionProvider(this);
	}
	
	/**
	 * ビューのグラフ領域にグラフをセット
	 */
	public void setGraph() {
		JungManager jungManager = scv.getJungManager();
		//TODO グラフサイズの計算
		jungManager.setSize(500,500);
		jungManager.doSetting();
		frame.removeAll();
    	frame.add(jungManager.getVisualizationServer()); //グラフをセット
    	frame.pack(); 	
    	
	}
		
	/**
	 * アクションを作成してツールバーに組み込む．
	 */
	private void createActions() {
		//ツールバーのCreate Graphはplugin.xmlに記述．
    	//リフレッシュアクション作成
    	ImageDescriptor iconRefresh = Activator.getImageDescriptor("icons/refresh.gif");
		this.refreshAction = new Action("Refresh", iconRefresh){
			public void run() {
				//グラフをセット
				setGraph();
			}
		};
		//ツールバーに組み込む
		this.getViewSite().getActionBars().getToolBarManager().add(this.refreshAction);
		//エクスポートアクション作成
		if (Config.EXPORT_ENABLE) {
		   	ImageDescriptor iconExport = Activator.getImageDescriptor("icons/export.gif");
			this.exportAction = new Action("Export", iconExport){
				public void run() {
					export();
				}
			};
			this.getViewSite().getActionBars().getToolBarManager().add(this.exportAction);
		}
		
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
	
	/**
	 * グラフのエクスポートを行う．
	 */
	private void export() {
		//create filter for save file dialog
	    HashSet<String> filterExtensionsSet = new HashSet<String>();
	    String[] formatNames = ImageIO.getWriterFormatNames();
	    for (String formatName : formatNames) {
	    	filterExtensionsSet.add("*." + formatName.toLowerCase());
	    }
	    ArrayList<String> filterExtensions = new ArrayList<String>(filterExtensionsSet);
	    filterExtensions.add("*.*");	    
		//show save file dialog
		FileDialog dialog = new FileDialog(new Shell(), SWT.SAVE);
		dialog.setFilterExtensions(filterExtensions.toArray(new String[filterExtensions.size()]));
		String filename = dialog.open();
		if (null == filename) return;
		// add extension if filename has no extension
		if (null == Utility.getExtensionFromFileName(filename) && 0 <= dialog.getFilterIndex()) {	
			String selectedExtension = dialog.getFilterExtensions()[dialog.getFilterIndex()];
			if (!selectedExtension.equals("*.*"))
				filename += selectedExtension.substring("*".length());
		}
		//Save graph
    	scv.getJungManager().saveGraph(new File(filename));
	}

}
