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
 * Eclipseのワークスペース情報を取得するクラス。
 * @author y-mutoh
 *
 */
public class WorkspaceManager {

	/**
	 * 現在開かれているプロジェクトのソースディレクトリを取得する。
	 * @return ソースディレクトリの配列。
	 */
	public static IResource[] getActiveProjectSrcDirs() {
		IJavaProject javaProject = getActiveJavaProject();
		if (javaProject == null) return null;
		//ソースディレクトリを検索する
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
	 * 現在開かれているプロジェクトのソースディレクトリのパスを取得する。
	 * @return
	 */
	public static String getActiveProjectSrcDirPath() {
		IResource[] resources = getActiveProjectSrcDirs();
		if (resources.length <= 0) return null;
		return resources[0].getLocation().toOSString();
	}
	
	/**
	 * 現在開かれているJavaプロジェクトを取得する。
	 * @return
	 */
	public static IJavaProject getActiveJavaProject() {
		//Javaプロジェクトを得る
		IProject project = getActiveProjectFromEditor();
		if (project == null) return null;
		return JavaCore.create(project);
	}
	
	/**
	 * 現在開かれているプロジェクトを取得する。
	 * @return
	 */
	//TODO プロジェクトエクスプローラでアクティブなプロジェクトを未考慮
	private static IProject getActiveProjectFromEditor() {
		//アクティブなエディタを取得
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		//アクティブなプロジェクトを取得
		IEditorInput input = editor.getEditorInput();
		if (!(input instanceof IFileEditorInput)) return null;
		IFileEditorInput fileInput = (IFileEditorInput)input;
		IFile file = fileInput.getFile();
		return file.getProject();
	}
	
	/**
	 * 指定したファイルへジャンプする。
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
	 * アクティブなプロジェクトのソースディレクトリの中からフルパスが一致するIFileオブジェクトを検索する。
	 * @param file
	 * @return
	 */
	public static IFile convertFileToIFile(File file) {
		//探したいファイルのディレクトリパス取得
		String dirpath;
		try {
			dirpath = file.getCanonicalFile().getParent();
		} catch (Exception e) {
			System.out.println("convertFileToIFile " + e);
			return null;
		}
		//ファイルを検索
		IResource[] srcdirs = getActiveProjectSrcDirs();
		for (IResource srcdir: srcdirs) {
			System.out.println(srcdir.getFullPath());
			if (srcdir instanceof IFolder) {
				IFolder ifolder = (IFolder)srcdir;
				System.out.println(ifolder.getFullPath() + " =? " + dirpath);
				if (ifolder.getFullPath().toOSString().equals(dirpath)) {
					System.out.println("見つかった");
					return ifolder.getFile(file.getName());
				}
			}
		}		
		return null;
	}
	
	
}
