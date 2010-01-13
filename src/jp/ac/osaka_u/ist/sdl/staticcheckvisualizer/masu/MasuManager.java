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
	 * �ΏۂƂȂ�N���X�ꗗ
	 */
	private TargetClassList targetClasses;
	
	/**
	 * ��͑ΏۂƂȂ�f�B���N�g��
	 */
	private String targetDirectory;
	
	/**
	 * Java�\�[�X�t�@�C���ꗗ
	 */
	private ArrayList<File> javaSourceFiles;
	
	/**
	 * �R���X�g���N�^
	 */
	public MasuManager(String targetDirectory){
		this.targetDirectory = targetDirectory;
		targetClasses = new TargetClassList();
		javaSourceFiles = new ArrayList<File>();
	}
	
	/**
	 * MASU�𗘗p���ăN���X�ꗗ�f�[�^�𐶐�����B
	 */
	public  void createTargetClasses(){		
		// ��͗p�ݒ�
		Settings.getInstance().setLanguage(Config.TARGET_LANGUAGE);
		Settings.getInstance().setVerbose(true);
		Settings.getInstance().setTargetDirectory(targetDirectory);

		// �Ώۃt�@�C���̉��
		this.readTargetFiles();
		this.analyzeTargetFiles();

		// �ΏۃN���X�ꗗ�𐶐�
		final Set<TargetClassInfo> classes = DataManager.getInstance().getClassInfoManager().getTargetClassInfos();
		for (final TargetClassInfo classInfo : classes) {
			targetClasses.add(targetClassInfoToTargetClass(classInfo));
		}	
		//�S�ΏۃN���X�ɃN���X�Ăяo�������Z�b�g
		for (final TargetClassInfo classInfo : classes) {
			TargetClass targetClass = targetClasses.searchClass(classInfo.getFullQualifiedName("."));
			setTargetClassCall(targetClass, classInfo);
		}	
		
		System.out.println("masu �ΏۃN���X�ꗗ�擾����");
		
		//�Ώ�Java�\�[�X�t�@�C�����Z�b�g
		final Set<FileInfo> fileInfos = DataManager.getInstance().getFileInfoManager().getFileInfos();
		for (FileInfo fileinfo : fileInfos) {
			javaSourceFiles.add(new File(fileinfo.getName()));
		}
		
		System.out.println("masu createTargetClasses����");

	}

	/**
	 * �ΏۂƂȂ�Ăяo���ł��邩�ǂ����𔻒肷��B
	 * 
	 * @param info ����Ώ�
	 * @return �ΏۂƂȂ邩�ǂ����B
	 */
	private boolean isTargetInfo(CallableUnitInfo info) {
		if (info instanceof TargetMethodInfo) return true;
		if (info instanceof TargetConstructorInfo) return true;
		return false;
	}
	
	/**
	 * MASU�̃N���X��񂩂�TargetClass�𐶐�����
	 * 
	 * @param classInfo�@MASU�̃N���X���
	 * @return TargetClass
	 */
	public TargetClass targetClassInfoToTargetClass(TargetClassInfo classInfo) {
		TargetClass targetClass = new TargetClass();
		//�p�b�P�[�W��
		String packageNameWithDot = classInfo.getNamespace().getName("."); //�������h�b�g(.)�Ȃ̂ō폜����K�v����
		if (!packageNameWithDot.equals("")) {
			targetClass.setPackageName(packageNameWithDot.substring(0, packageNameWithDot.length()-1));
		}
		//�N���X��
		targetClass.setSimpleName(classInfo.getClassName());
		System.out.println("name=" + targetClass.getSimpleName());
		
		return targetClass;
	}
	
	/**
	 * MASU�̃N���X��񂩂�Ăяo�������Z�b�g����
	 * 
	 * @param classInfo�@MASU�̃N���X���B�Ăяo�������܂ށB
	 * @param targetClass �Ăяo������ǉ��������ΏۃN���X
	 * @return TargetClass
	 */
	public void setTargetClassCall(TargetClass targetClass, TargetClassInfo classInfo) {
		//�Ăяo��
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
	 * ���̃N���X���Ăяo���Ă���N���X�����擾����B
	 * 
	 * @param callinfo �Ăяo�����B
	 * @param isContainExternal �ΏۊO�N���X�����ʂɊ܂߂邩�ǂ����B
	 * @return ���̃N���X���Ăяo���Ă���N���X�̊��S���薼�̃��X�g�B
	 */
	public ArrayList<String> getCalleeClassNames(CallInfo<? extends CallableUnitInfo> callinfo, boolean isContainExternal) {
		if (callinfo == null) return null;
		//System.out.println("line " + callinfo.getFromLine());
		ArrayList<String> targetList = new ArrayList<String>(); 
		List<ExpressionInfo> arguments = callinfo.getArguments(); //����������������o��������A�ʕ��ƃJ�E���g
		//���\�b�h�Ăяo���̈����ɑ΂��čċA
		for (ExpressionInfo argument: arguments) {
			Set<CallInfo<?>> argumentCalls = argument.getCalls(); //�����Ăяo����������o�����Ă��A1�ɃJ�E���g
			for (CallInfo argumentCallInfo: argumentCalls) {
				targetList.addAll(getCalleeClassNames(argumentCallInfo, isContainExternal)); //�ċA
			}
		}
		CallableUnitInfo callee = callinfo.getCallee();

		if (isTargetInfo(callee) || isContainExternal){
			ClassInfo callerClass;
			try {				
				callerClass = callinfo.getOwnerSpace().getOwnerClass(); //�Ăяo�����N���X
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}	
			ClassInfo calleeClass = callee.getOwnerClass(); //�Ăяo�����N���X
			//System.out.println(" " + callerClass.getClassName() + " -> " + calleeClass.getClassName());
			targetList.add(calleeClass.getFullQualifiedName("."));
		}
		return targetList;
	}
	
	/**
	 * ���̃N���X���Ăяo���Ă���N���X���ƌĂяo���񐔂��擾����B
	 * 
	 * @param calleeClassNames �Ăяo�����N���X���̃��X�g�B
	 * @return ���̃N���X���Ăяo���Ă���N���X�́A���S���薼���L�[�A�Ăяo���񐔂�l�Ƃ���n�b�V���}�b�v�B
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
	 * MASU�̃N���X��񂩂�N���X����������
	 * 
	 * @param fullQualifiedName �����������N���X�̊��S���薼
	 * @return ���������N���X���
	 */
	public TargetClassInfo searchTargetClassInfo(String fullQualifiedName) {
		final Set<TargetClassInfo> classes = DataManager.getInstance().getClassInfoManager().getTargetClassInfos();
		for (final TargetClassInfo classInfo : classes) {
			if (classInfo.getFullQualifiedName(".").equals(fullQualifiedName)) {
				return classInfo;	//��������
			}
		}
		return null;	//������Ȃ�����
	}

	/* �A�N�Z�T */
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