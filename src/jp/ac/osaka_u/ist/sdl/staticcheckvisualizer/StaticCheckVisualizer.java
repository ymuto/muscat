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
//		//���݂̃v���W�F�N�g�擾
		//String targetDirectory = WorkspaceManager.getActiveJavaProjectPath();

		String srcdir = WorkspaceManager.getActiveProjectSrcDirPath();
		if (srcdir == null) {
			System.out.println("�\�[�X�f�B���N�g����������܂���");
			return;
		}
		String targetDirectory = srcdir;
		String classpath = srcdir;
		
		
		//String targetDirectory = "C:\\Users\\y-mutoh\\workspace\\StockManagement\\src";
		//String targetDirectory = "C:\\Users\\y-mutoh\\workspace\\SCVTestData\\src";
		//String classpath = targetDirectory;
		
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
		
		//�N���X��񏉊���
		targetClasses.clear();
	
		//MASU�ŃN���X�������
		masuManager = new MasuManager(targetDirectory);
		masuManager.createTargetClasses();
		
		masuManager.getTargetClasses().printClassNames();
		System.out.println("masu OK");

		// �Ώ�java�\�[�X�t�@�C������XML�𐶐����ăN���X���ǂݍ���
		Date before = new Date(); //�J�n����
		//cleanXmlFiles(new File(Activator.getConfig().getOutputDir())); 
		for (File javafile : masuManager.getJavaSourceFiles()) {
			System.out.println("javafile=" + javafile.getName());
			//XML����
			System.out.println("XML�����J�n...");
			GenerateXml generator = new GenerateXml(new File(javafile.getPath()), Activator.getConfig().getOutputDir(), classpath, true);
			System.out.println("XML��������!");

			//XML�ǂݍ���
			for (File xmlfile: generator.getXmlFiles()) {
				System.out.println("java=" + javafile.getName() + " xml="+xmlfile.getName());
				TargetClass c = new TargetClass(javafile.getAbsolutePath(), xmlfile.getAbsolutePath());
				targetClasses.add(c);
				//MASU�̃N���X��񂩂��v����N���X�̌Ăяo����������Ă��đ��
				TargetClass masuSameTargetClass = masuManager.getTargetClasses().searchClass(c.getFullQualifiedName());
				if (masuSameTargetClass != null) {
					c.setCallees(masuSameTargetClass.getCallees());
				}
			}
			System.out.println("���݂�TargetClasses");
			targetClasses.printClassNames();
		}
		//�o�ߎ���
		long diffTime = new Date().getTime() - before.getTime();
		SimpleDateFormat timeFormatter = new SimpleDateFormat ("HH:mm:ss");
		timeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String diffTimeStr = timeFormatter.format(diffTime); 
		System.out.println("XML�����ɂ�����������=" + diffTimeStr);
				
		//
		targetClasses.printClassNames();
		
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
	
	/**
	 * �f�B���N�g������XML�t�@�C�����폜����B
	 * @param dir �Ώۃf�B���N�g��
	 */
	private void cleanXmlFiles(File file) {
		//�t�@�C���̏ꍇ
		if (file.isFile()) {
			if (file.getPath().endsWith(".xml")) {
				file.delete();
			}
			return;
		}
		//�f�B���N�g���̏ꍇ�͍ċA
		if (file.isDirectory()) {
			for (File f :file.listFiles()) {
				cleanXmlFiles(f);
			}
			return;
		}
	}
	
}
