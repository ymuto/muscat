package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * Eclipse�̃��[�N�X�y�[�X�����擾����N���X�D
 * @author y-mutoh
 *
 */
public class WorkspaceManager {

	/**
	 * ���݊J����Ă���v���W�F�N�g�̃\�[�X�f�B���N�g�����擾����D
	 * @return �\�[�X�f�B���N�g���̔z��D
	 */
	public static IResource[] getActiveProjectSrcDirs() {
		IJavaProject javaProject = getActiveJavaProject();		
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
	 * ���݊J����Ă���v���W�F�N�g�̃\�[�X�f�B���N�g���̃p�X���擾����D
	 * @return
	 */
	public static String getActiveProjectSrcDirPath() {
		IResource[] resources = getActiveProjectSrcDirs();
		if (resources.length <= 0) return null;
		return resources[0].getLocation().toOSString();
	}
	
	/**
	 * ���݊J����Ă���v���W�F�N�g�̃f�B���N�g���p�X���擾����D
	 * @return
	 */
	public static String getActiveJavaProjectPath() {
		IProject project = getActiveProjectFromEditor();
		return project.getLocation().toOSString();
	}
	
	/**
	 * ���݊J����Ă���Java�v���W�F�N�g���擾����D
	 * @return
	 */
	public static IJavaProject getActiveJavaProject() {
		//Java�v���W�F�N�g�𓾂�
		IProject project = getActiveProjectFromEditor();
		if (project == null) return null;
		return JavaCore.create(project);
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
	 * �\�[�X���̊Y�����\�b�h�L�q�փW�����v����D
	 * @param method �W�����v���������\�b�h
	 */
	public static void jumpSource(Method method) {
		//�A�N�e�B�u�ȃv���W�F�N�g���猟��
		if (jumpSourceWithJavaProject(getActiveJavaProject(), method)) return;
		//������Ȃ�������C�S�v���W�F�N�g����T��
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			IJavaProject javaProject = JavaCore.create(project);
			if (jumpSourceWithJavaProject(javaProject, method)) return;
		}
	}
	
	/**
	 * �Y��JavaProject�̃��\�b�h�փW�����v����D
	 * @param javaProject �Ώۃv���W�F�N�g
	 * @param method �W�����v���������\�b�h
	 * @return ������true�C���s��false
	 */
	public static boolean jumpSourceWithJavaProject(IJavaProject javaProject, Method method) {
		try {
			//�Y���N���X������
			IType type = javaProject.findType(method.getMethodList().getTargetClass().getFullQualifiedName());
			if (type == null) {
				System.out.println("�N���X��������܂���:");
				return false;
			}
			if (!type.isClass()) return false;

			//�Y�����\�b�h������
			IMethod imethod = null;
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
					String unresolvedParameter = Signature.toString(im.getParameterTypes()[i]);
					String fullQualifiedParameterType = getFullQualifiedTypeFromType(unresolvedParameter, im);
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
	 * �����̌^�������S���薼�ɕϊ�����D
	 * @param parameterType �����̌^���D��������Ă��Ȃ��`�D(��FString�Ȃ�)
	 * @param method �������������郁�\�b�h
	 * @return
	 */
	private static String getFullQualifiedTypeFromType(String parameterType, IMethod method) {
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
	
}
