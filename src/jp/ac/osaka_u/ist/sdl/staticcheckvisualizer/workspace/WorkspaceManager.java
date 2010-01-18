package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace;

import java.io.File;
import java.util.ArrayList;

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
 * Eclipse�̃��[�N�X�y�[�X�����擾����N���X�B
 * @author y-mutoh
 *
 */
public class WorkspaceManager {

	/**
	 * ���݊J����Ă���v���W�F�N�g�̃\�[�X�f�B���N�g�����擾����B
	 * @return �\�[�X�f�B���N�g���̔z��B
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
	 * ���݊J����Ă���v���W�F�N�g�̃\�[�X�f�B���N�g���̃p�X���擾����B
	 * @return
	 */
	public static String getActiveProjectSrcDirPath() {
		IResource[] resources = getActiveProjectSrcDirs();
		if (resources.length <= 0) return null;
		return resources[0].getLocation().toOSString();
	}
	
	/**
	 * ���݊J����Ă���v���W�F�N�g�̃f�B���N�g���p�X���擾����B
	 * @return
	 */
	public static String getActiveJavaProjectPath() {
		IProject project = getActiveProjectFromEditor();
		return project.getLocation().toOSString();
	}
	
	/**
	 * ���݊J����Ă���Java�v���W�F�N�g���擾����B
	 * @return
	 */
	public static IJavaProject getActiveJavaProject() {
		//Java�v���W�F�N�g�𓾂�
		IProject project = getActiveProjectFromEditor();
		if (project == null) return null;
		return JavaCore.create(project);
	}
	
	/**
	 * ���݊J����Ă���v���W�F�N�g���擾����B
	 * @return
	 */
	//TODO �p�b�P�[�W�G�N�X�v���[���ŃA�N�e�B�u�ȃv���W�F�N�g�𖢍l��
	private static IProject getActiveProjectFromEditor() {
		//�A�N�e�B�u�ȃG�f�B�^���擾
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		
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
	 * �\�[�X���̊Y���L�q�փW�����v����B
	 * @param method �W�����v���������\�b�h
	 */
	public static void jumpSource(Method method) {
		//�A�N�e�B�u�ȃv���W�F�N�g���猟��
		if (jumpSourceWithJavaProject(getActiveJavaProject(), method)) return;
		//������Ȃ�������A�S�v���W�F�N�g����T��
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			IJavaProject javaProject = JavaCore.create(project);
			if (jumpSourceWithJavaProject(javaProject, method)) return;
		}	
	}
	
	/**
	 * �Y��JavaProject�̃��\�b�h�փW�����v����B
	 * @param javaProject
	 * @param method ���\�b�h
	 * @return ������true�A���s��false
	 */
	public static boolean jumpSourceWithJavaProject(IJavaProject javaProject, Method method) {
		try {
			//�Y���I�u�W�F�N�g������
			IType type = javaProject.findType(method.getMethodList().getTargetClass().getFullQualifiedName());
			if (type == null) {
				System.out.println("������܂���:");
				return false;
			}
			if (!type.isClass()) return false;
			//������ϊ�
			String signatures[] = null;
			if (method.getParameters() != null) {
				signatures = new String[method.getParameters().length];
				for (int i=0; i<method.getParameters().length; i++) {
					signatures[i] = Signature.createTypeSignature(method.getParameters()[i], false);
					System.out.println(signatures[i]);
				}
			}
			//�Y�����\�b�h������
			IMethod imethod = type.getMethod(method.getName(), signatures);
//�_��		IMethod imethod = type.getMethod(method.getName(), new String[] {"Qjava.lang.String;"});

			if (imethod == null) return false;
			//�G�f�B�^���J��
			IEditorPart editorpart = JavaUI.openInEditor(imethod);
			
			
		} catch (Exception e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
//	/**
//	 * �A�N�e�B�u�ȃv���W�F�N�g�̃\�[�X�f�B���N�g���̒�����t���p�X����v����IFile�I�u�W�F�N�g����������B
//	 * @param file
//	 * @return
//	 */
//	public static IFile convertFileToIFile(File file) {
//		//�T�������t�@�C���̃f�B���N�g���p�X�擾
//		String dirpath;
//		try {
//			dirpath = file.getCanonicalFile().getParent();
//		} catch (Exception e) {
//			System.out.println("convertFileToIFile " + e);
//			return null;
//		}
//		//�t�@�C��������
//		IResource[] srcdirs = getActiveProjectSrcDirs();
//		for (IResource srcdir: srcdirs) {
//			System.out.println(srcdir.getFullPath());
//			if (srcdir instanceof IFolder) {
//				IFolder ifolder = (IFolder)srcdir;
//				System.out.println(ifolder.getFullPath() + " =? " + dirpath);
//				if (ifolder.getFullPath().toOSString().equals(dirpath)) {
//					System.out.println("��������");
//					return ifolder.getFile(file.getName());
//				}
//			}
//		}		
//		return null;
//	}
	
	
}
