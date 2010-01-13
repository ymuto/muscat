package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.masu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config.Config;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility.Utility;
import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;

public class MasuManager extends MetricsTool {
	/**
	 * 対象となるクラス一覧
	 */
	private TargetClassList targetClasses;
	
	/**
	 * 解析対象となるディレクトリ
	 */
	private String targetDirectory;
	
	/**
	 * Javaソースファイル一覧
	 */
	private ArrayList<File> javaSourceFiles;
	
	/**
	 * コンストラクタ
	 */
	public MasuManager(String targetDirectory){
		this.targetDirectory = targetDirectory;
		targetClasses = new TargetClassList();
		javaSourceFiles = new ArrayList<File>();
	}
	
	/**
	 * MASUを利用してクラス一覧データを生成する。
	 */
	public  void createTargetClasses(){		
		// 解析用設定
		Settings.getInstance().setLanguage(Config.TARGET_LANGUAGE);
		Settings.getInstance().setVerbose(true);
		Settings.getInstance().setTargetDirectory(targetDirectory);

		// 対象ファイルの解析
		this.readTargetFiles();
		this.analyzeTargetFiles();

		// 対象クラス一覧を生成
		final Set<TargetClassInfo> classes = DataManager.getInstance().getClassInfoManager().getTargetClassInfos();
		for (final TargetClassInfo classInfo : classes) {
			targetClasses.add(targetClassInfoToTargetClass(classInfo));
		}	
		//全対象クラスにクラス呼び出し情報をセット
		for (final TargetClassInfo classInfo : classes) {
			TargetClass targetClass = targetClasses.searchClass(classInfo.getFullQualifiedName("."));
			setTargetClassCall(targetClass, classInfo);
		}	
		
		System.out.println("masu 対象クラス一覧取得完了");
		
		//対象Javaソースファイルをセット
		final Set<FileInfo> fileInfos = DataManager.getInstance().getFileInfoManager().getFileInfos();
		for (FileInfo fileinfo : fileInfos) {
			javaSourceFiles.add(new File(fileinfo.getName()));
		}
		
		System.out.println("masu createTargetClasses完了");

	}

	/**
	 * 対象となる呼び出しであるかどうかを判定する。
	 * 
	 * @param info 判定対象
	 * @return 対象となるかどうか。
	 */
	private boolean isTargetInfo(CallableUnitInfo info) {
		if (info instanceof TargetMethodInfo) return true;
		if (info instanceof TargetConstructorInfo) return true;
		return false;
	}
	
	/**
	 * MASUのクラス情報からTargetClassを生成する
	 * 
	 * @param classInfo　MASUのクラス情報
	 * @return TargetClass
	 */
	public TargetClass targetClassInfoToTargetClass(TargetClassInfo classInfo) {
		TargetClass targetClass = new TargetClass();
		//パッケージ名
		String packageNameWithDot = classInfo.getNamespace().getName("."); //末尾がドット(.)なので削除する必要あり
		if (!packageNameWithDot.equals("")) {
			targetClass.setPackageName(packageNameWithDot.substring(0, packageNameWithDot.length()-1));
		}
		//クラス名
		targetClass.setSimpleName(classInfo.getClassName());
		System.out.println("name=" + targetClass.getSimpleName());
		
		return targetClass;
	}
	
	/**
	 * MASUのクラス情報から呼び出し情報をセットする
	 * 
	 * @param classInfo　MASUのクラス情報。呼び出し情報を含む。
	 * @param targetClass 呼び出し情報を追加したい対象クラス
	 * @return TargetClass
	 */
	public void setTargetClassCall(TargetClass targetClass, TargetClassInfo classInfo) {
		//呼び出し
		Set<CallInfo<? extends CallableUnitInfo>> calls = classInfo.getCalls();
		for (CallInfo<? extends CallableUnitInfo> callinfo : calls) {
			//System.out.println("callee begin");
			for (String calleeClassName : getCalleeClassNames(callinfo, false)) {
				TargetClass calleeClass = this.targetClasses.searchClass(calleeClassName);
				if (calleeClass != null) {
					targetClass.getCalleeClasses().add(calleeClass);
				}
			}
			//System.out.println("callee end");
		}
	}
	
	
	/**
	 * このクラスが呼び出しているクラス名を取得する。
	 * 
	 * @param callinfo 呼び出し情報。
	 * @param isContainExternal 対象外クラスを結果に含めるかどうか。
	 * @return このクラスが呼び出しているクラスの完全限定名のリスト。
	 */
	public ArrayList<String> getCalleeClassNames(CallInfo<? extends CallableUnitInfo> callinfo, boolean isContainExternal) {
		if (callinfo == null) return null;
		//System.out.println("line " + callinfo.getFromLine());
		ArrayList<String> targetList = new ArrayList<String>(); 
		List<ExpressionInfo> arguments = callinfo.getArguments(); //同じ引数が複数回出現したら、別物とカウント
		//メソッド呼び出しの引数に対して再帰
		for (ExpressionInfo argument: arguments) {
			Set<CallInfo<?>> argumentCalls = argument.getCalls(); //同じ呼び出しが複数回出現しても、1つにカウント
			for (CallInfo argumentCallInfo: argumentCalls) {
				targetList.addAll(getCalleeClassNames(argumentCallInfo, isContainExternal)); //再帰
			}
		}
		CallableUnitInfo callee = callinfo.getCallee();

		if (isTargetInfo(callee) || isContainExternal){
			ClassInfo callerClass;
			try {				
				callerClass = callinfo.getOwnerSpace().getOwnerClass(); //呼び出し元クラス
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}	
			ClassInfo calleeClass = callee.getOwnerClass(); //呼び出されるクラス
			//System.out.println(" " + callerClass.getClassName() + " -> " + calleeClass.getClassName());
			targetList.add(calleeClass.getFullQualifiedName("."));
		}
		return targetList;
	}
	
	/**
	 * このクラスが呼び出しているクラス名と呼び出し回数を取得する。
	 * 
	 * @param calleeClassNames 呼び出されるクラス名のリスト。
	 * @return このクラスが呼び出しているクラスの、完全限定名をキー、呼び出し回数を値とするハッシュマップ。
	 */
	public HashMap<Integer,String> getCalleeNamesAndCounts(ArrayList<String> calleeClassNames) {
		HashMap<Integer, String> callees = new HashMap<Integer, String>();
		//TODO
		return callees;
	}
	
	public ArrayList<String> getCalleeClassNames(CallInfo<? extends CallableUnitInfo> callinfo) {
		return getCalleeClassNames(callinfo, false);
	}
	

	
	
	
	/**
	 * MASUのクラス情報からクラスを検索する
	 * 
	 * @param fullQualifiedName 検索したいクラスの完全限定名
	 * @return 見つかったクラス情報
	 */
	public TargetClassInfo searchTargetClassInfo(String fullQualifiedName) {
		final Set<TargetClassInfo> classes = DataManager.getInstance().getClassInfoManager().getTargetClassInfos();
		for (final TargetClassInfo classInfo : classes) {
			if (classInfo.getFullQualifiedName(".").equals(fullQualifiedName)) {
				return classInfo;	//見つかった
			}
		}
		return null;	//見つからなかった
	}

	/* アクセサ */
	public TargetClassList getTargetClasses() {
		return targetClasses;
	}

	public void setTargetClasses(TargetClassList targetClasses) {
		this.targetClasses = targetClasses;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public ArrayList<File> getJavaSourceFiles() {
		return javaSourceFiles;
	}

	public void setJavaSourceFiles(ArrayList<File> javaSourceFiles) {
		this.javaSourceFiles = javaSourceFiles;
	}
}