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
    public static final int NODE_WIDTH = 80;
    /**
     * ノードの高さ。
     */
    public static final int NODE_HEIGHT = 80;

    /**
     * 属性値の配色。
     * keyは属性の値、valueはそれに対応する色。
     */
    private HashMap<String, Color> attributeColors;
    
    /**
     * コンストラクタ
     */
    public Config() {
    	attributeColors = new HashMap<String, Color>();
    	setDefaultAttributeColors();
    }
    

	/**
	 * 属性の色分けの初期値を設定。
	 */
	private void setDefaultAttributeColors() {
    	attributeColors.put("passed", new Color(null,0,127,0));
    	attributeColors.put("failed", new Color(null,255,0,0));
    	attributeColors.put("true", new Color(null,0,127,0));
    	attributeColors.put("false", new Color(null,255,0,0));
    	attributeColors.put("OK", new Color(null,0,127,0));
    	attributeColors.put("NG", new Color(null,255,0,0));
	}


	public HashMap<String, Color> getAttributeColors() {
		return attributeColors;
	}
	
    
}