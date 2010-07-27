package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config.Config;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.generateXml.GenerateXml;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.JungManager;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.masu.MasuManager;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace.WorkspaceManager;


public class StaticCheckVisualizer {
	private MasuManager masuManager;
	private JungManager jungManager;
	private WorkspaceManager workspaceManager;
	
	/**
	 * 対象クラス情報．
	 */
	private TargetClassList targetClasses;
	
	/**
	 * 対象ディレクトリ．（未使用）
	 */
	private String targetDirectory;
	
	/**
	 * クラスパス．（未使用）
	 */
	private String classpath;
	
	/**
	 * 静的チェックが終了したかどうか．
	 */
	private boolean isFinishedCheck = false;
	
	/**
	 * 選択されているTargetClassのリスト．
	 */
	private TargetClassList selectedTargetClasses;

	
	/**
	 * TODO JungManagerのノード選択の変化を処理
	 */
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
	 * コンストラクタ．
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
	 * 設定してから静的チェックを実行する．
	 */
	public void execute() {
		//現在のプロジェクト取得
		workspaceManager = new WorkspaceManager();
		String srcdir = workspaceManager.getTargetDirectory().getPath();
		if (srcdir == null) {
			System.out.println("ソースディレクトリが見つかりません");
			return;
		}
		String classpath = srcdir;
				
		//TODO 終わったら消す
		//ASPECのため ここから
		//srcdir = "C:\\Users\\y-mutoh\\workspace\\addressbook-teacher-jml\\src\\addressbook";
		//classpath = "";
		//classpath = "C:\\Users\\y-mutoh\\workspace\\addressbook-teacher-jml\\src";
		//ASPECのため ここまで
		
		//チェック開始
		setClasspath(classpath);
		setTargetDirectory(srcdir);
		check();

	
	}
	
	/**
	 * 静的チェックを実行する．
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
		System.out.println("masu OK");

		// 対象javaソースファイルからXMLを生成してクラス情報読み込み
		Date before = new Date(); //開始時刻
		//cleanXmlFiles(new File(Activator.getConfig().getOutputDir()));
		for (File javafile : masuManager.getJavaSourceFiles()) {
			System.out.println("javafile=" + javafile.getName());
			//XML生成
			System.out.println("XML生成開始...");
			GenerateXml generator = new GenerateXml(new File(javafile.getPath()), Config.getInstance().getOutputDir(), classpath, true);
			System.out.println("XML生成完了!");
			
			//XML生成に失敗したクラスは無視
			if (generator.getXmlFiles() == null) continue;
			
			//XML読み込み
			for (File xmlfile: generator.getXmlFiles()) {
				try {
					System.out.println("java=" + javafile.getName() + " xml="+xmlfile.getName());
					TargetClass c = new TargetClass(javafile.getAbsolutePath(), xmlfile.getAbsolutePath());
					targetClasses.add(c);
					//MASUのクラス情報から一致するクラスの呼び出し情報を取ってきて代入
					TargetClass masuSameTargetClass = masuManager.getTargetClasses().searchClass(c.getFullQualifiedName());
					if (masuSameTargetClass != null) {
						c.setCallees(masuSameTargetClass.getCallees());
					}
				} catch (Exception e) {
					System.out.println( e.getMessage() + "\n\t" + e.getStackTrace().toString());
				}
			}
		}
		//経過時間
		long diffTime = new Date().getTime() - before.getTime();
		SimpleDateFormat timeFormatter = new SimpleDateFormat ("HH:mm:ss");
		timeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String diffTimeStr = timeFormatter.format(diffTime); 
		System.out.println("XML生成にかかった時間=" + diffTimeStr);
		
		//クラス情報出力
		for (TargetClass tc : targetClasses) {
			System.out.println(tc.toString() + "\n");
		}
		
		System.out.println("Finish.");
		
		//Jungでグラフを生成
		//TODO サイズ
		jungManager = new JungManager(targetClasses);
		
    	//TODO
    	jungManager.addItemListener(itemListener);

    	//チェック完了
    	this.isFinishedCheck = true;
		
	}


	/**
	 * 選択されているクラスリストを返す．
	 * @return
	 */
	public TargetClassList getSelectedTargetClasses() {
		if (jungManager == null) return TargetClassList.EMPTY;
		//MyNodeの集合をTargetClassListに変換する
		Set<MyNode> selectedNodes = jungManager.getSelectedNodes();
		if (selectedNodes == null) {
			System.out.println("jungManager.selectedNodes = null");
			return TargetClassList.EMPTY;
		}
		selectedTargetClasses.clear();
		for (MyNode selectedNode : selectedNodes) {
			TargetClass targetClass = getTargetClasses().searchClass(selectedNode.getFullQualifiedName());
			selectedTargetClasses.add(targetClass);
		}
		selectedTargetClasses.update(); //属性タイトルリストの更新
		return selectedTargetClasses;
	}
	
	/**
	 * ディレクトリ内のXMLファイルを削除する．
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
	
	//アクセサ

	public TargetClassList getTargetClasses() {
		return targetClasses;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	
	public JungManager getJungManager() {
		return jungManager;
	}
	
	public WorkspaceManager getWorkspaceManager() {
		return workspaceManager;
	}
	
	public boolean isFinishedCheck() {
		return isFinishedCheck;
	}


	
}
