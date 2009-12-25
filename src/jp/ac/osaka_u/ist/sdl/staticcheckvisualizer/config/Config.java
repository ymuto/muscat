package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config;


import java.util.HashMap;

import org.eclipse.swt.graphics.Color;

/**
 * �ݒ���L�q����N���X�B
 * @author y-mutoh
 *
 */
public final class Config {
    //�ݒ�p�萔
	/**
	 * �m�[�h�̉����B
	 */
    public static final int NODE_WIDTH = 80;
    /**
     * �m�[�h�̍����B
     */
    public static final int NODE_HEIGHT = 80;

    /**
     * �����l�̔z�F�B
     * key�͑����̒l�Avalue�͂���ɑΉ�����F�B
     */
    private HashMap<String, Color> attributeColors;
    
    /**
     * �R���X�g���N�^
     */
    public Config() {
    	attributeColors = new HashMap<String, Color>();
    	setDefaultAttributeColors();
    }
    

	/**
	 * �����̐F�����̏����l��ݒ�B
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