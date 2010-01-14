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
    public static final int NODE_WIDTH = 70;
    /**
     * �m�[�h�̍����B
     */
    public static final int NODE_HEIGHT = 70;
    
    /**
     * �ΏۂƂȂ錾��Bjava14��Java1.4�B
     */
    public static final String TARGET_LANGUAGE = "java14";

    /**
     * �����l�̔z�F�B
     * key�͑����̒l�Avalue�͂���ɑΉ�����F�B
     */
    private HashMap<String, Color> attributeColors;
    
    //�`�F�b�N�ɗp����l
    /**
     * XML���o�͂���f�B���N�g���B
     */
    private String outputDir;
    
    /**
     * ���s����X�N���v�g�B
     */
    private String generateCommand;
    
    /**
     * �R���X�g���N�^
     */
    public Config() {
    	attributeColors = new HashMap<String, Color>();
    	setDefaultAttributeColors();
    	setCheckArgs();
    }
    

	/**
	 * �����̐F�����̏����l��ݒ�B
	 */
	private void setDefaultAttributeColors() {
    	attributeColors.put("passed", new Color(null,0,127,0)); //��
    	attributeColors.put("failed", new Color(null,255,0,0)); //��
    	attributeColors.put("true", new Color(null,0,127,0));	//��
    	attributeColors.put("false", new Color(null,255,0,0));	//��
    	attributeColors.put("OK", new Color(null,0,127,0));		//��
    	attributeColors.put("NG", new Color(null,255,0,0));		//��
	}
	
	/**
	 * �`�F�b�N�ɗp����l���Z�b�g�B
	 */
	private void setCheckArgs(){
		//this.generateCommand = "perl C:\\Users\\y-mutoh\\research\\perl\\escj2xml.pl";
		//this.generateCommand = "perl C:\\Users\\y-mutoh\\workspace\\jp.ac.osaka_u.ist.sdl.staticcheckvisualizer\\perl\\escj2xml.pl";
		this.generateCommand = "perl C:\\Users\\y-mutoh\\workspace\\jp.ac.osaka_u.ist.sdl.staticcheckvisualizer\\perl\\dummy.pl";

		//String xml_out = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\xml";
		//String xml_out = "C:\\Users\\y-mutoh\\workspace\\StockManagement\\xml";
		//String xml_out = "C:\\Users\\y-mutoh\\runtime-Eclipse�A�v���P�[�V����\\StockManagement\\xml\\";
		this.outputDir = "C:\\Users\\y-mutoh\\runtime-Eclipse�A�v���P�[�V����\\StockManagement\\xml\\";
	}


	//�A�N�Z�T
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