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
	 * �ÓI�`�F�b�N���I���������ǂ����B
	 */
	private boolean isFinishedCheck = false;
	


	public boolean isFinishedCheck() {
		return isFinishedCheck;
	}

	/**
	 * �I������Ă���TargetClass�̃��X�g�B
	 */
	private TargetClassList selectedTargetClasses;
	
	/**
	 * TODO JungManager�̃m�[�h�I���̕ω�������
	 */
	//
	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			System.out.println("JungManager�̕ω�������");
			//MyNode�̏W����TargetClassList�ɕϊ�����
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
	 * �R���X�g���N�^�B
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
	 * �ÓI�`�F�b�N�����s����B
	 */
	public void execute() {
		//���݂̃v���W�F�N�g�擾
		String srcdir = WorkspaceManager.getActiveProjectSrcDirPath();
		if (srcdir == null) {
			System.out.println("�\�[�X�f�B���N�g����������܂���");
			return;
		}
		String targetDirectory = srcdir;
		String classpath = srcdir;
		
		//�`�F�b�N�J�n
		//String targetDirectory = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\src";
		//String classpath = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\src";	
		setClasspath(classpath);
		setTargetDirectory(targetDirectory);
		check();
	}
	
	/**
	 * �ÓI�`�F�b�N���s���B
	 */
	public void check() {
		this.isFinishedCheck = false;
	
		System.out.println("target Dir=" + this.targetDirectory);
		System.out.println("classpath=" + this.classpath);

		
		//MASU�ŃN���X�������
		masuManager = new MasuManager(targetDirectory);
		masuManager.createTargetClasses();
				
		ArrayList<File> javaFiles;
		javaFiles = masuManager.getJavaSourceFiles();
//		javaFiles = new ArrayList<File>();
//		javaFiles.add(new File("C:\\Users\\y-mutoh\\workspace\\StockManagement\\src\\AppMain.java"));
//		javaFiles.add(new File("C:\\Users\\y-mutoh\\workspace\\StockManagement\\src\\StockManagement\\ContainerItem.java"));
//		javaFiles.add(new File("C:\\Users\\y-mutoh\\workspace\\StockManagement\\src\\StockManagement\\Customer.java"));
//		javaFiles.add(new File("C:\\Users\\y-mutoh\\workspace\\StockManagement\\src\\StockManagement\\Item.java"));
//		javaFiles.add(new File("C:\\Users\\y-mutoh\\workspace\\StockManagement\\src\\StockManagement\\ReceiptionDesk.java"));
//		javaFiles.add(new File("C:\\Users\\y-mutoh\\workspace\\StockManagement\\src\\StockManagement\\Request.java"));
//		javaFiles.add(new File("C:\\Users\\y-mutoh\\workspace\\StockManagement\\src\\StockManagement\\StockState.java"));
//		javaFiles.add(new File("C:\\Users\\y-mutoh\\workspace\\StockManagement\\src\\StockManagement\\Storage.java"));


		// �Ώ�java�\�[�X�t�@�C������XML�𐶐����ăN���X���ǂݍ���
		Date before = new Date(); //�J�n����
		for (File javafile : javaFiles) {
			System.out.println(javafile.getName());
			//XML����
			String xml_out = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\xml";
			//String xml_out = "C:\\Users\\y-mutoh\\workspace\\StockManagement\\xml";
			//String xml_out = "xml\\";
			System.out.println("XML�����J�n...");
			GenerateXml generator = new GenerateXml(new File(javafile.getPath()), xml_out, classpath, true);
			System.out.println("XML��������!");

			//XML�ǂݍ���
			for (File xmlfile: generator.getXmlFiles()) {
				TargetClass c = new TargetClass(javafile.getAbsolutePath(), xmlfile.getAbsolutePath());				
				targetClasses.add(c);
				//MASU�̃N���X��񂩂��v����N���X�̌Ăяo����������Ă��đ��
				TargetClass masuSameTargetClass = masuManager.getTargetClasses().searchClass(c.getFullQualifiedName());
				if (masuSameTargetClass != null) {
					c.setCalleeClasses(masuSameTargetClass.getCalleeClasses());
				}
			}
		}
		//�o�ߎ���
		long diffTime = new Date().getTime() - before.getTime();
		SimpleDateFormat timeFormatter = new SimpleDateFormat ("HH:mm:ss");
		timeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String diffTimeStr = timeFormatter.format(diffTime); 
		System.out.println("XML�����ɂ�����������=" + diffTimeStr);
		
		//�N���X���o��
		for (TargetClass tc : targetClasses) {
			System.out.println(tc.toString() + "\n");
		}	
		
		System.out.println("Finish.");
		
		//Jung�ŃO���t�𐶐�
		jungManager = new JungManager(targetClasses);
		
    	//TODO
    	jungManager.addItemListener(itemListener);
    	
    	//�`�F�b�N����
    	this.isFinishedCheck = true;
		
	}

	public JungManager getJungManager() {
		return jungManager;
	}

	public TargetClassList getTargetClasses() {
		return targetClasses;
	}
	
	public TargetClassList getSelectedTargetClasses() {
		//MyNode�̏W����TargetClassList�ɕϊ�����
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
	
}
