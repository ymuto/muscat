package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config;


import java.util.HashMap;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.preference.PreferenceKey;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;

/**
 * �����ݒ���L�q����N���X�D
 * �V���O���g���D
 * @author y-mutoh
 *
 */
public final class Config {
	//�V���O���g�������p�D�����̌^�̃C���X�^���X�D
	private static Config instance = new Config();
	
	//�f�t�H���g�l
	public static final String DEFAULT_GENERATE_COMMAND = "perl C:\\Users\\y-mutoh\\workspace\\jp.ac.osaka_u.ist.sdl.staticcheckvisualizer\\xml-generator\\ESCJava2\\escj2xml.pl";
    public static final int DEFAULT_EDGE_MAX_LEVEL = 5;
	public static final int DEFAULT_METHODVIEW_ATTRIBUTE_COLUMN_COUNT = 3;
    
	//�ݒ�p�萔
	/**
	 * �m�[�h�̉����D
	 */
    public static final int NODE_WIDTH = 70;
    /**
     * �m�[�h�̍����D
     */
    public static final int NODE_HEIGHT = 70;
    
    /**
     * �ΏۂƂȂ錾��Djava14��Java1.4�D
     */
    public static final String TARGET_LANGUAGE = "java14";

    /**
     * �����l�̔z�F�D
     * key�͑����̒l�Cvalue�͂���ɑΉ�����F�D
     */
    private HashMap<String, Color> attributeColors;
    
    //�`�F�b�N�ɗp����l
    /**
     * XML���o�͂���f�B���N�g���D
     */
    private String outputDir;
    
    /**
     * ���s����X�N���v�g�D
     */
    private String generateCommand;
    
    /**
     * �G�b�W�̑������x���̍ő吔�D
     */
    private int edgeMaxLevel;
    
    private int methodViewAttributeColumnCount;
    
    /**
     * �R���X�g���N�^
     */
    private Config() {
    	attributeColors = new HashMap<String, Color>();
    	setDefaultAttributeColors();
    	updateFromPreference();
    }  

	/**
	 * �����̐F�����̏����l��ݒ�D
	 */
	private void setDefaultAttributeColors() {
    	attributeColors.put("passed", new Color(null,0,127,0)); //��
    	attributeColors.put("failed", new Color(null,255,0,0)); //��
    	attributeColors.put("true",   new Color(null,0,127,0));	//��
    	attributeColors.put("false",  new Color(null,255,0,0));	//��
    	attributeColors.put("OK",     new Color(null,0,127,0)); //��
    	attributeColors.put("NG",     new Color(null,255,0,0));	//��
	}
	
	/**
	 * �v���t�@�����X�X�g�A����ݒ���擾
	 */
	public void updateFromPreference() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		this.outputDir = store.getString(PreferenceKey.OUTPUT_DIR);
		this.generateCommand = store.getString(PreferenceKey.GENERATE_COMMAND);
		this.edgeMaxLevel = store.getInt(PreferenceKey.EDGE_MAX_LEVEL);
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


	public int getEdgeMaxLevel() {
		return edgeMaxLevel;
	}


	public int getMethodViewAttributeColumnCount() {
		return methodViewAttributeColumnCount;
	}
	
	//�V���O���g�������p�D�C���X�^���X�擾
	public static Config getInstance() {
		return Config.instance;
	}
	   
}
