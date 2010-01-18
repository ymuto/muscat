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
	 * 現在開かれているプロジェクトのディレクトリパスを取得する。
	 * @return
	 */
	public static String getActiveJavaProjectPath() {
		IProject project = getActiveProjectFromEditor();
		return project.getLocation().toOSString();
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
	//TODO パッケージエクスプローラでアクティブなプロジェクトを未考慮
	private static IProject getActiveProjectFromEditor() {
		//アクティブなエディタを取得
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		
		if (window == null) return null;
		IWorkbenchPage page = window.getActivePage();
		if (page == null) return null;
		IEditorPart editor = page.getActiveEditor();
		if (editor == null) return null;
		
		//アクティブなプロジェクトを取得
		IEditorInput input = editor.getEditorInput();
		if (!(input instanceof IFileEditorInput)) return null;
		IFileEditorInput fileInput = (IFileEditorInput)input;
		IFile file = fileInput.getFile();
		return file.getProject();
	}
	

	/**
	 * ソース中の該当記述へジャンプする。
	 * @param method ジャンプしたいメソッド
	 */
	public static void jumpSource(Method method) {
		//アクティブなプロジェクトから検索
		if (jumpSourceWithJavaProject(getActiveJavaProject(), method)) return;
		//見つからなかったら、全プロジェクトから探索
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			IJavaProject javaProject = JavaCore.create(project);
			if (jumpSourceWithJavaProject(javaProject, method)) return;
		}	
	}
	
	/**
	 * 該当JavaProjectのメソッドへジャンプする。
	 * @param javaProject
	 * @param method メソッド
	 * @return 成功時true、失敗時false
	 */
	public static boolean jumpSourceWithJavaProject(IJavaProject javaProject, Method method) {
		try {
			//該当オブジェクトを検索
			IType type = javaProject.findType(method.getMethodList().getTargetClass().getFullQualifiedName());
			if (type == null) {
				System.out.println("見つかりません:");
				return false;
			}
			if (!type.isClass()) return false;
			//引数を変換
			String signatures[] = null;
			if (method.getParameters() != null) {
				signatures = new String[method.getParameters().length];
				for (int i=0; i<method.getParameters().length; i++) {
					signatures[i] = Signature.createTypeSignature(method.getParameters()[i], false);
					System.out.println(signatures[i]);
				}
			}
			//該当メソッドを検索
			IMethod imethod = type.getMethod(method.getName(), signatures);
//ダメ		IMethod imethod = type.getMethod(method.getName(), new String[] {"Qjava.lang.String;"});

			if (imethod == null) return false;
			//エディタを開く
			IEditorPart editorpart = JavaUI.openInEditor(imethod);
			
			
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
//	/**
//	 * アクティブなプロジェクトのソースディレクトリの中からフルパスが一致するIFileオブジェクトを検索する。
//	 * @param file
//	 * @return
//	 */
//	public static IFile convertFileToIFile(File file) {
//		//探したいファイルのディレクトリパス取得
//		String dirpath;
//		try {
//			dirpath = file.getCanonicalFile().getParent();
//		} catch (Exception e) {
//			System.out.println("convertFileToIFile " + e);
//			return null;
//		}
//		//ファイルを検索
//		IResource[] srcdirs = getActiveProjectSrcDirs();
//		for (IResource srcdir: srcdirs) {
//			System.out.println(srcdir.getFullPath());
//			if (srcdir instanceof IFolder) {
//				IFolder ifolder = (IFolder)srcdir;
//				System.out.println(ifolder.getFullPath() + " =? " + dirpath);
//				if (ifolder.getFullPath().toOSString().equals(dirpath)) {
//					System.out.println("見つかった");
//					return ifolder.getFile(file.getName());
//				}
//			}
//		}		
//		return null;
//	}
	
	
}
