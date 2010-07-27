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
	 * �ΏۃN���X���D
	 */
	private TargetClassList targetClasses;
	
	/**
	 * �Ώۃf�B���N�g���D�i���g�p�j
	 */
	private String targetDirectory;
	
	/**
	 * �N���X�p�X�D�i���g�p�j
	 */
	private String classpath;
	
	/**
	 * �ÓI�`�F�b�N���I���������ǂ����D
	 */
	private boolean isFinishedCheck = false;
	
	/**
	 * �I������Ă���TargetClass�̃��X�g�D
	 */
	private TargetClassList selectedTargetClasses;

	
	/**
	 * TODO JungManager�̃m�[�h�I���̕ω�������
	 */
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
	 * �R���X�g���N�^�D
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
	 * �ݒ肵�Ă���ÓI�`�F�b�N�����s����D
	 */
	public void execute() {
		//���݂̃v���W�F�N�g�擾
		workspaceManager = new WorkspaceManager();
		String srcdir = workspaceManager.getTargetDirectory().getPath();
		if (srcdir == null) {
			System.out.println("�\�[�X�f�B���N�g����������܂���");
			return;
		}
		String classpath = srcdir;
				
		//TODO �I����������
		//ASPEC�̂��� ��������
		//srcdir = "C:\\Users\\y-mutoh\\workspace\\addressbook-teacher-jml\\src\\addressbook";
		//classpath = "";
		//classpath = "C:\\Users\\y-mutoh\\workspace\\addressbook-teacher-jml\\src";
		//ASPEC�̂��� �����܂�
		
		//�`�F�b�N�J�n
		setClasspath(classpath);
		setTargetDirectory(srcdir);
		check();

	
	}
	
	/**
	 * �ÓI�`�F�b�N�����s����D
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
		System.out.println("masu OK");

		// �Ώ�java�\�[�X�t�@�C������XML�𐶐����ăN���X���ǂݍ���
		Date before = new Date(); //�J�n����
		//cleanXmlFiles(new File(Activator.getConfig().getOutputDir()));
		for (File javafile : masuManager.getJavaSourceFiles()) {
			System.out.println("javafile=" + javafile.getName());
			//XML����
			System.out.println("XML�����J�n...");
			GenerateXml generator = new GenerateXml(new File(javafile.getPath()), Config.getInstance().getOutputDir(), classpath, true);
			System.out.println("XML��������!");
			
			//XML�����Ɏ��s�����N���X�͖���
			if (generator.getXmlFiles() == null) continue;
			
			//XML�ǂݍ���
			for (File xmlfile: generator.getXmlFiles()) {
				try {
					System.out.println("java=" + javafile.getName() + " xml="+xmlfile.getName());
					TargetClass c = new TargetClass(javafile.getAbsolutePath(), xmlfile.getAbsolutePath());
					targetClasses.add(c);
					//MASU�̃N���X��񂩂��v����N���X�̌Ăяo����������Ă��đ��
					TargetClass masuSameTargetClass = masuManager.getTargetClasses().searchClass(c.getFullQualifiedName());
					if (masuSameTargetClass != null) {
						c.setCallees(masuSameTargetClass.getCallees());
					}
				} catch (Exception e) {
					System.out.println( e.getMessage() + "\n\t" + e.getStackTrace().toString());
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
		//TODO �T�C�Y
		jungManager = new JungManager(targetClasses);
		
    	//TODO
    	jungManager.addItemListener(itemListener);

    	//�`�F�b�N����
    	this.isFinishedCheck = true;
		
	}


	/**
	 * �I������Ă���N���X���X�g��Ԃ��D
	 * @return
	 */
	public TargetClassList getSelectedTargetClasses() {
		if (jungManager == null) return TargetClassList.EMPTY;
		//MyNode�̏W����TargetClassList�ɕϊ�����
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
		selectedTargetClasses.update(); //�����^�C�g�����X�g�̍X�V
		return selectedTargetClasses;
	}
	
	/**
	 * �f�B���N�g������XML�t�@�C�����폜����D
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
	
	//�A�N�Z�T

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
