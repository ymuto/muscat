package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
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
	//TODO �v���W�F�N�g�G�N�X�v���[���ŃA�N�e�B�u�ȃv���W�F�N�g�𖢍l��
	private static IProject getActiveProjectFromEditor() {
		//�A�N�e�B�u�ȃG�f�B�^���擾
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		//�A�N�e�B�u�ȃv���W�F�N�g���擾
		IEditorInput input = editor.getEditorInput();
		if (!(input instanceof IFileEditorInput)) return null;
		IFileEditorInput fileInput = (IFileEditorInput)input;
		IFile file = fileInput.getFile();
		return file.getProject();
	}
	
	/**
	 * �w�肵���t�@�C���փW�����v����B
	 * @param file
	 */
	public static void jumpSource(String filepath) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		try {
			IFile ifile = convertFileToIFile(new File(filepath));
			if (ifile == null) System.out.println("ifile=null");
			IEditorPart editor = IDE.openEditor(page, ifile);
		} catch (Exception e) {
			System.out.println("jumpSource " + e);
		}
	}
	
	/**
	 * �A�N�e�B�u�ȃv���W�F�N�g�̃\�[�X�f�B���N�g���̒�����t���p�X����v����IFile�I�u�W�F�N�g����������B
	 * @param file
	 * @return
	 */
	public static IFile convertFileToIFile(File file) {
		//�T�������t�@�C���̃f�B���N�g���p�X�擾
		String dirpath;
		try {
			dirpath = file.getCanonicalFile().getParent();
		} catch (Exception e) {
			System.out.println("convertFileToIFile " + e);
			return null;
		}
		//�t�@�C��������
		IResource[] srcdirs = getActiveProjectSrcDirs();
		for (IResource srcdir: srcdirs) {
			System.out.println(srcdir.getFullPath());
			if (srcdir instanceof IFolder) {
				IFolder ifolder = (IFolder)srcdir;
				System.out.println(ifolder.getFullPath() + " =? " + dirpath);
				if (ifolder.getFullPath().toOSString().equals(dirpath)) {
					System.out.println("��������");
					return ifolder.getFile(file.getName());
				}
			}
		}		
		return null;
	}
	
	
}
