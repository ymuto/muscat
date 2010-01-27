package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config;


import java.util.HashMap;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.preference.PreferenceKey;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;

/**
 * 内部設定を記述するクラス．
 * シングルトン．
 * @author y-mutoh
 *
 */
public final class Config {
	//シングルトン実現用．自分の型のインスタンス．
	private static Config instance = new Config();
	
	//デフォルト値
	public static final String DEFAULT_GENERATE_COMMAND = "perl C:\\Users\\y-mutoh\\workspace\\jp.ac.osaka_u.ist.sdl.staticcheckvisualizer\\xml-generator\\ESCJava2\\escj2xml.pl";
    public static final int DEFAULT_EDGE_MAX_LEVEL = 5;
	public static final int DEFAULT_METHODVIEW_ATTRIBUTE_COLUMN_COUNT = 3;
    
	//設定用定数
	/**
	 * ノードの横幅．
	 */
    public static final int NODE_WIDTH = 70;
    /**
     * ノードの高さ．
     */
    public static final int NODE_HEIGHT = 70;
    
    /**
     * 対象となる言語．java14はJava1.4．
     */
    public static final String TARGET_LANGUAGE = "java14";

    /**
     * 属性値の配色．
     * keyは属性の値，valueはそれに対応する色．
     */
    private HashMap<String, Color> attributeColors;
    
    //チェックに用いる値
    /**
     * XMLを出力するディレクトリ．
     */
    private String outputDir;
    
    /**
     * 実行するスクリプト．
     */
    private String generateCommand;
    
    /**
     * エッジの太さレベルの最大数．
     */
    private int edgeMaxLevel;
    
    private int methodViewAttributeColumnCount;
    
    /**
     * コンストラクタ
     */
    private Config() {
    	attributeColors = new HashMap<String, Color>();
    	setDefaultAttributeColors();
    	updateFromPreference();
    }  

	/**
	 * 属性の色分けの初期値を設定．
	 */
	private void setDefaultAttributeColors() {
    	attributeColors.put("passed", new Color(null,0,127,0)); //緑
    	attributeColors.put("failed", new Color(null,255,0,0)); //赤
    	attributeColors.put("true",   new Color(null,0,127,0));	//緑
    	attributeColors.put("false",  new Color(null,255,0,0));	//赤
    	attributeColors.put("OK",     new Color(null,0,127,0)); //緑
    	attributeColors.put("NG",     new Color(null,255,0,0));	//赤
	}
	
	/**
	 * プリファレンスストアから設定を取得
	 */
	public void updateFromPreference() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		this.outputDir = store.getString(PreferenceKey.OUTPUT_DIR);
		this.generateCommand = store.getString(PreferenceKey.GENERATE_COMMAND);
		this.edgeMaxLevel = store.getInt(PreferenceKey.EDGE_MAX_LEVEL);
	}


	//アクセサ
	public HashMap<String, Color> getAttributeColors() {
		return attributeColors;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public String getGenerateCommand() {
		return generateCommand;
	}


	public int getEdgeMaxLevel() {
		return edgeMaxLevel;
	}


	public int getMethodViewAttributeColumnCount() {
		return methodViewAttributeColumnCount;
	}
	
	//シングルトン実現用．インスタンス取得
	public static Config getInstance() {
		return Config.instance;
	}
	   
}
