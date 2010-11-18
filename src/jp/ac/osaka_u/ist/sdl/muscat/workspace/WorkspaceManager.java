package jp.ac.osaka_u.ist.sdl.muscat.workspace;

import java.io.File;
import java.util.ArrayList;

import jp.ac.osaka_u.ist.sdl.muscat.model.Method;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Eclipse�̃��[�N�X�y�[�X�����擾����N���X�D
 * @author y-mutoh
 *
 */
public class WorkspaceManager {
	
	/**
	 * ��͑Ώۃv���W�F�N�g�D
	 */
	private IJavaProject javaProject;
	
	/**
	 * ��͑Ώۃf�B���N�g���D
	 */
	private File targetDirectory;
	

	/**
	 * �R���X�g���N�^�D
	 */
	public WorkspaceManager() {
		//��͑Ώۃv���W�F�N�g���Z�b�g
		this.javaProject = getActiveJavaProject();
		//��͑Ώۃf�B���N�g�����Z�b�g
		String srcdir = getSrcDirPath(getProjectSrcDirs(this.javaProject));
		this.targetDirectory = new File(srcdir);
	}
	
	/**
	 * �\�[�X���̊Y�����\�b�h�L�q�փW�����v����D
	 * @param method �W�����v���������\�b�h
	 */
	public void jumpSource(Method method) {
		jumpSourceWithJavaProject(this.javaProject, method);
	}
	
	/**
	 * ���\�b�h�̈������\�[�X�R�[�h��̌`�ŕԂ��D
	 * @param method
	 * @return ���\�b�h�̈����̌^���D�\�[�X�R�[�h��̕\�L�Ɠ����`���D
	 */
	public String[] getParametersFromSource(Method method) {
		IMethod imethod = getIMethodFromMethod(this.javaProject, method);
		if (imethod != null) {
			return getUnresolvedParameters(imethod);
		}
		return null;
	}
	
	/**
	 * ���[�N�X�y�[�X�̃f�B�X�N��̃p�X��Ԃ��D
	 * @return
	 */
	public static String getWorkspaceDirPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
	}
	
	/**
	 * �v���W�F�N�g�̃\�[�X�f�B���N�g�����擾����D
	 * @return �\�[�X�f�B���N�g���̔z��D
	 */
	private IResource[] getProjectSrcDirs(IJavaProject javaProject) {	
		if (javaProject == null) return null;
		//�\�[�X�f�B���N�g������������
		ArrayList<IResource> resources = new ArrayList<IResource>();
		try {
			IPackageFragmentRoot[] packageRoots = javaProject.getPackageFragmentRoots();
			for (IPackageFragmentRoot packageRoot : packageRoots) {
				if (packageRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
					resources.add(packageRoot.getResource());
				}
			}
			return resources.toArray(new IResource[resources.size()]);
		} catch (Exception e) {
			System.out.println("getActiveProjectSrcDirs " + e);
			return null;
		}
	}
	

	
	/**
	 * �v���W�F�N�g�̃\�[�X�f�B���N�g���̃p�X���擾����D
	 * @return
	 */
	private String getSrcDirPath(IResource[] resources) {
		if (resources.length <= 0) return null;
		return resources[0].getLocation().toOSString();
	}
	
	/**
	 * ���݊J����Ă���Java�v���W�F�N�g���擾����D
	 * @return
	 */
	private IJavaProject getActiveJavaProject() {
		//Java�v���W�F�N�g�𓾂�
		IProject project = getActiveProjectFromEditor();
		if (project == null) return null;
		return JavaCore.create(project);
	}
	
	/**
	 * �Y��JavaProject�̃��\�b�h�փW�����v����D
	 * @param javaProject �Ώۃv���W�F�N�g
	 * @param method �W�����v���������\�b�h
	 * @return ������true�C���s��false
	 */
	private boolean jumpSourceWithJavaProject(IJavaProject javaProject, Method method) {
		try {
			//���\�b�h������
			IMethod imethod = getIMethodFromMethod(javaProject, method);
			if (imethod == null) return false;
			//�G�f�B�^���J��
			if(JavaUI.openInEditor(imethod) == null) return false;
				
		} catch (Exception e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * ���\�b�h�̈����̌^���𖢉����ŕԂ��D
	 * @param imethod
	 * @return �����̌^���̔z��D(String�Ȃǂ̌`��)
	 */
	private String[] getUnresolvedParameters(IMethod imethod) {
		String unresolvedParameters[] = new String[imethod.getParameterTypes().length];
		for (int i=0; i<imethod.getParameterTypes().length; i++) {
			unresolvedParameters[i] = Signature.toString(imethod.getParameterTypes()[i]);
			System.out.println(imethod.getElementName() + " " + unresolvedParameters[i]);
		}
		return unresolvedParameters;
	}
	
	/**
	 * ���݊J����Ă���v���W�F�N�g���擾����D
	 * @return
	 */
	//TODO �p�b�P�[�W�G�N�X�v���[���ŃA�N�e�B�u�ȃv���W�F�N�g�𖢍l��
	private static IProject getActiveProjectFromEditor() {
		//�A�N�e�B�u�ȃG�f�B�^���擾
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) return null;
		IWorkbenchPage page = window.getActivePage();
		if (page == null) return null;
		IEditorPart editor = page.getActiveEditor();
		if (editor == null) return null;
		
		//�A�N�e�B�u�ȃv���W�F�N�g���擾
		IEditorInput input = editor.getEditorInput();
		if (!(input instanceof IFileEditorInput)) return null;
		IFileEditorInput fileInput = (IFileEditorInput)input;
		IFile file = fileInput.getFile();
		return file.getProject();
	}
	
	/**
	 * ���\�b�h��񂩂�\�[�X�R�[�h��̃��\�b�h�𓾂�D
	 * @param javaProject �Ώۃv���W�F�N�g
	 * @param method ���\�b�h���
	 * @return �\�[�X�R�[�h��̃��\�b�h
	 */
	private IMethod getIMethodFromMethod(IJavaProject javaProject, Method method) {
		IMethod imethod = null;	
		try {
			//�Y���N���X������
			IType type = javaProject.findType(method.getMethodList().getTargetClass().getFullQualifiedName());
			if (type == null) {
				System.out.println("�N���X��������܂���:");
				return null;
			}
			if (!type.isClass()) return null;

			//�Y�����\�b�h������
			methodLoop:
			for (IMethod im : type.getMethods()) {
				//���\�b�h�����r
				if (!method.getName().equals(im.getElementName())) continue;
				//�����Ȃ�
				if (im.getParameterTypes().length <= 0 && method.getParameters() == null) {
					imethod = im;
					break;
				}
				//�����̐����r
				if (im.getParameterTypes().length != method.getParameters().length) continue;
				//��������
				for (int i=0; i<im.getParameterTypes().length; i++) {
					//�\�[�X�R�[�h�̈����̌^�������S���薼�ɕϊ�(String -> java.lang.String)
					//String types[] = im.getParameterTypes(); //[QString;�݂����Ȍ`��
					//String unresolvedParameter = Signature.toString(im.getParameterTypes()[i]);
					//String fullQualifiedParameterType = getFullQualifiedTypeNameFromSimpleTypeName(unresolvedParameter, im);
					String fullQualifiedParameterType = getFullQualifiedTypeNameFromUnresolvedName(im.getParameterTypes()[i], im);
					//�����^����r
					if (!method.getParameters()[i].equals(fullQualifiedParameterType)) break;					
					//�ŏI��������v����
					if (i >= im.getParameterTypes().length-1 && i >= method.getParameters().length-1) {
						//���\�b�h��v
						imethod = im;
						break methodLoop;
					}
				}
			}

		} catch (JavaModelException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
		return imethod;
	}
	
	/**
	 * �����̌^�������S���薼�ɕϊ�����D
	 * �� String -> java.lang.String
	 * @param parameterType �����̌^���D��������Ă��Ȃ��`�D(��FString[]�Ȃ�)
	 * @param method �������������郁�\�b�h
	 * @return
	 */
	private String getFullQualifiedTypeNameFromSimpleTypeName(String parameterType, IMethod method) {
		//String arrayBracket = 
		String fullQualifiedType = parameterType; //�v���~�e�B�u�`�̏ꍇ�Ɏg�p
		String[][] parameterElements;
		try {
			parameterElements = method.getDeclaringType().resolveType(parameterType);
			if (parameterElements != null) {
				String parameterPackage = parameterElements[0][0];
				String parameterName = parameterElements[0][1];
				fullQualifiedType = parameterName;	
				if (parameterPackage != null) {
					fullQualifiedType = parameterPackage + "." + fullQualifiedType;
				}
			}
		} catch (JavaModelException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}

		return fullQualifiedType;
	}
	
	
	

	/**
	 * �����̖������̌^�����犮�S���薼�𓾂�D
	 * �� [[QString; -> java.lang.String[][]
	 * @param unresolvedName �����̖������̌^���D
	 * @param imethod ���̈����̏������郁�\�b�h�D
	 * @return ���S���薼
	 */
	private String getFullQualifiedTypeNameFromUnresolvedName(String unresolvedName, IMethod imethod) {
		//�z��̏���
		int bracketLastIndex = unresolvedName.lastIndexOf("["); //"[[QString;".lastIndexOf("[[") -> 1
		String typeName = unresolvedName.substring(bracketLastIndex + 1); //[���Ȃ��ꍇ��-1���Ԃ���邽��0�ƂȂ�
		String arrayBracket = unresolvedName.substring(0, bracketLastIndex + 1);
		//�f�R�[�h
		String resolvedName = Signature.toString(typeName);
		String fullQualifiedType = getFullQualifiedTypeNameFromSimpleTypeName(resolvedName,imethod);
		//���S���薼�𐶐�
		return fullQualifiedType + arrayBracket.replace("[", "[]");
	}
	
	//�A�N�Z�T
	public File getTargetDirectory() {
		return targetDirectory;
	}
	
}
