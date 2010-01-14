package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config;


import java.util.HashMap;

import org.eclipse.swt.graphics.Color;

/**
 * 設定を記述するクラス。
 * @author y-mutoh
 *
 */
public final class Config {
    //設定用定数
	/**
	 * ノードの横幅。
	 */
    public static final int NODE_WIDTH = 70;
    /**
     * ノードの高さ。
     */
    public static final int NODE_HEIGHT = 70;
    
    /**
     * 対象となる言語。java14はJava1.4。
     */
    public static final String TARGET_LANGUAGE = "java14";

    /**
     * 属性値の配色。
     * keyは属性の値、valueはそれに対応する色。
     */
    private HashMap<String, Color> attributeColors;
    
    //チェックに用いる値
    /**
     * XMLを出力するディレクトリ。
     */
    private String outputDir;
    
    /**
     * 実行するスクリプト。
     */
    private String generateCommand;
    
    /**
     * コンストラクタ
     */
    public Config() {
    	attributeColors = new HashMap<String, Color>();
    	setDefaultAttributeColors();
    	setCheckArgs();
    }
    

	/**
	 * 属性の色分けの初期値を設定。
	 */
	private void setDefaultAttributeColors() {
    	attributeColors.put("passed", new Color(null,0,127,0)); //緑
    	attributeColors.put("failed", new Color(null,255,0,0)); //赤
    	attributeColors.put("true", new Color(null,0,127,0));	//緑
    	attributeColors.put("false", new Color(null,255,0,0));	//赤
    	attributeColors.put("OK", new Color(null,0,127,0));		//緑
    	attributeColors.put("NG", new Color(null,255,0,0));		//赤
	}
	
	/**
	 * チェックに用いる値をセット。
	 */
	private void setCheckArgs(){
		//this.generateCommand = "perl C:\\Users\\y-mutoh\\research\\perl\\escj2xml.pl";
		//this.generateCommand = "perl C:\\Users\\y-mutoh\\workspace\\jp.ac.osaka_u.ist.sdl.staticcheckvisualizer\\perl\\escj2xml.pl";
		this.generateCommand = "perl C:\\Users\\y-mutoh\\workspace\\jp.ac.osaka_u.ist.sdl.staticcheckvisualizer\\perl\\dummy.pl";

		//String xml_out = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\xml";
		//String xml_out = "C:\\Users\\y-mutoh\\workspace\\StockManagement\\xml";
		//String xml_out = "C:\\Users\\y-mutoh\\runtime-Eclipseアプリケーション\\StockManagement\\xml\\";
		this.outputDir = "C:\\Users\\y-mutoh\\runtime-Eclipseアプリケーション\\StockManagement\\xml\\";
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
	   
}