package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.generateXml.GenerateXml;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.JungManager;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.masu.MasuManager;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace.WorkspaceManager;


public class StaticCheckVisualizer {
	private TargetClassList targetClasses;
	private MasuManager masuManager;
	private JungManager jungManager;
	
	private String targetDirectory;
	private String classpath;
	
	/**
	 * 静的チェックが終了したかどうか。
	 */
	private boolean isFinishedCheck = false;
	


	public boolean isFinishedCheck() {
		return isFinishedCheck;
	}

	/**
	 * 選択されているTargetClassのリスト。
	 */
	private TargetClassList selectedTargetClasses;
	
	/**
	 * TODO JungManagerのノード選択の変化を処理
	 */
	//
	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			System.out.println("JungManagerの変化を処理");
			//MyNodeの集合をTargetClassListに変換する
			Set<MyNode> selectedNodes = jungManager.getSelectedNodes();
			if (selectedNodes == null) return;
			selectedTargetClasses.clear();
			for (MyNode selectedNode : selectedNodes) {
				TargetClass targetClass = targetClasses.searchClass(selectedNode.getFullQualifiedName());
				selectedTargetClasses.add(targetClass);
			}
		}
	};
	
	/**
	 * コンストラクタ。
	 */
	public StaticCheckVisualizer() {
		targetClasses = new TargetClassList();	
		selectedTargetClasses = new TargetClassList();
		this.isFinishedCheck = false;
	}
	
	public StaticCheckVisualizer(String targetDirectory, String classpath) {
		targetClasses = new TargetClassList();	
		selectedTargetClasses = new TargetClassList();
		this.targetDirectory = targetDirectory;
		this.classpath = classpath;
		this.isFinishedCheck = false;
	}
	
	/**
	 * 静的チェックを実行する。
	 */
	public void execute() {
//		//現在のプロジェクト取得
		//String targetDirectory = WorkspaceManager.getActiveJavaProjectPath();

		String srcdir = WorkspaceManager.getActiveProjectSrcDirPath();
		if (srcdir == null) {
			System.out.println("ソースディレクトリが見つかりません");
			return;
		}
		String targetDirectory = srcdir;
		String classpath = srcdir;
		
		
		//String targetDirectory = "C:\\Users\\y-mutoh\\workspace\\StockManagement\\src";
		//String targetDirectory = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\src";
		//String classpath = targetDirectory;
		
		//チェック開始
		//String targetDirectory = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\src";
		//String classpath = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\src";	
		setClasspath(classpath);
		setTargetDirectory(targetDirectory);
		check();
	}
	
	/**
	 * 静的チェックを行う。
	 */
	public void check() {
		this.isFinishedCheck = false;
	
		System.out.println("target Dir=" + this.targetDirectory);
		System.out.println("classpath=" + this.classpath);
		
		//クラス情報初期化
		targetClasses.clear();
	
		//MASUでクラス情報を解析
		masuManager = new MasuManager(targetDirectory);
		masuManager.createTargetClasses();
		
		masuManager.getTargetClasses().printClassNames();
		System.out.println("masu OK");

		// 対象javaソースファイルからXMLを生成してクラス情報読み込み
		Date before = new Date(); //開始時刻
		//cleanXmlFiles(new File(Activator.getConfig().getOutputDir())); 
		for (File javafile : masuManager.getJavaSourceFiles()) {
			System.out.println("javafile=" + javafile.getName());
			//XML生成
			System.out.println("XML生成開始...");
			GenerateXml generator = new GenerateXml(new File(javafile.getPath()), Activator.getConfig().getOutputDir(), classpath, true);
			System.out.println("XML生成完了!");

			//XML読み込み
			for (File xmlfile: generator.getXmlFiles()) {
				System.out.println("java=" + javafile.getName() + " xml="+xmlfile.getName());
				TargetClass c = new TargetClass(javafile.getAbsolutePath(), xmlfile.getAbsolutePath());
				targetClasses.add(c);
				//MASUのクラス情報から一致するクラスの呼び出し情報を取ってきて代入
				TargetClass masuSameTargetClass = masuManager.getTargetClasses().searchClass(c.getFullQualifiedName());
				if (masuSameTargetClass != null) {
					c.setCallees(masuSameTargetClass.getCallees());
				}
			}
			System.out.println("現在のTargetClasses");
			targetClasses.printClassNames();
		}
		//経過時間
		long diffTime = new Date().getTime() - before.getTime();
		SimpleDateFormat timeFormatter = new SimpleDateFormat ("HH:mm:ss");
		timeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String diffTimeStr = timeFormatter.format(diffTime); 
		System.out.println("XML生成にかかった時間=" + diffTimeStr);
				
		//
		targetClasses.printClassNames();
		
		//クラス情報出力
		for (TargetClass tc : targetClasses) {
			System.out.println(tc.toString() + "\n");
		}
		
		System.out.println("Finish.");
		
		//Jungでグラフを生成
		jungManager = new JungManager(targetClasses);
		
    	//TODO
    	jungManager.addItemListener(itemListener);
    	
    	//チェック完了
    	this.isFinishedCheck = true;
		
	}

	public JungManager getJungManager() {
		return jungManager;
	}

	public TargetClassList getTargetClasses() {
		return targetClasses;
	}
	
	public TargetClassList getSelectedTargetClasses() {
		//MyNodeの集合をTargetClassListに変換する
		Set<MyNode> selectedNodes = jungManager.getSelectedNodes();
		if (selectedNodes == null) {
			System.out.println("jungManager.selectedNodes = null");
			return null;
		}
		selectedTargetClasses.clear();
		for (MyNode selectedNode : selectedNodes) {
			TargetClass targetClass = getTargetClasses().searchClass(selectedNode.getFullQualifiedName());
			selectedTargetClasses.add(targetClass);
		}
		return selectedTargetClasses;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	
	/**
	 * ディレクトリ内のXMLファイルを削除する。
	 * @param dir 対象ディレクトリ
	 */
	private void cleanXmlFiles(File file) {
		//ファイルの場合
		if (file.isFile()) {
			if (file.getPath().endsWith(".xml")) {
				file.delete();
			}
			return;
		}
		//ディレクトリの場合は再帰
		if (file.isDirectory()) {
			for (File f :file.listFiles()) {
				cleanXmlFiles(f);
			}
			return;
		}
	}
	
}
