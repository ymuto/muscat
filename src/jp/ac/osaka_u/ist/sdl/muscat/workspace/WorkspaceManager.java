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
 * Eclipseのワークスペース情報を取得するクラス．
 * @author y-mutoh
 *
 */
public class WorkspaceManager {
	
	/**
	 * 解析対象プロジェクト．
	 */
	private IJavaProject javaProject;
	
	/**
	 * 解析対象ディレクトリ．
	 */
	private File targetDirectory;
	

	/**
	 * コンストラクタ．
	 */
	public WorkspaceManager() {
		//解析対象プロジェクトをセット
		this.javaProject = getActiveJavaProject();
		//解析対象ディレクトリをセット
		String srcdir = getSrcDirPath(getProjectSrcDirs(this.javaProject));
		this.targetDirectory = new File(srcdir);
	}
	
	/**
	 * ソース中の該当メソッド記述へジャンプする．
	 * @param method ジャンプしたいメソッド
	 */
	public void jumpSource(Method method) {
		jumpSourceWithJavaProject(this.javaProject, method);
	}
	
	/**
	 * メソッドの引数をソースコード上の形で返す．
	 * @param method
	 * @return メソッドの引数の型名．ソースコード上の表記と同じ形式．
	 */
	public String[] getParametersFromSource(Method method) {
		IMethod imethod = getIMethodFromMethod(this.javaProject, method);
		if (imethod != null) {
			return getUnresolvedParameters(imethod);
		}
		return null;
	}
	
	/**
	 * ワークスペースのディスク上のパスを返す．
	 * @return
	 */
	public static String getWorkspaceDirPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
	}
	
	/**
	 * プロジェクトのソースディレクトリを取得する．
	 * @return ソースディレクトリの配列．
	 */
	private IResource[] getProjectSrcDirs(IJavaProject javaProject) {	
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
	 * プロジェクトのソースディレクトリのパスを取得する．
	 * @return
	 */
	private String getSrcDirPath(IResource[] resources) {
		if (resources.length <= 0) return null;
		return resources[0].getLocation().toOSString();
	}
	
	/**
	 * 現在開かれているJavaプロジェクトを取得する．
	 * @return
	 */
	private IJavaProject getActiveJavaProject() {
		//Javaプロジェクトを得る
		IProject project = getActiveProjectFromEditor();
		if (project == null) return null;
		return JavaCore.create(project);
	}
	
	/**
	 * 該当JavaProjectのメソッドへジャンプする．
	 * @param javaProject 対象プロジェクト
	 * @param method ジャンプしたいメソッド
	 * @return 成功時true，失敗時false
	 */
	private boolean jumpSourceWithJavaProject(IJavaProject javaProject, Method method) {
		try {
			//メソッドを検索
			IMethod imethod = getIMethodFromMethod(javaProject, method);
			if (imethod == null) return false;
			//エディタを開く
			if(JavaUI.openInEditor(imethod) == null) return false;
				
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * メソッドの引数の型名を未解決で返す．
	 * @param imethod
	 * @return 引数の型名の配列．(Stringなどの形式)
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
	 * 現在開かれているプロジェクトを取得する．
	 * @return
	 */
	//TODO パッケージエクスプローラでアクティブなプロジェクトを未考慮
	private static IProject getActiveProjectFromEditor() {
		//アクティブなエディタを取得
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
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
	 * メソッド情報からソースコード上のメソッドを得る．
	 * @param javaProject 対象プロジェクト
	 * @param method メソッド情報
	 * @return ソースコード上のメソッド
	 */
	private IMethod getIMethodFromMethod(IJavaProject javaProject, Method method) {
		IMethod imethod = null;	
		try {
			//該当クラスを検索
			IType type = javaProject.findType(method.getMethodList().getTargetClass().getFullQualifiedName());
			if (type == null) {
				System.out.println("クラスが見つかりません:");
				return null;
			}
			if (!type.isClass()) return null;

			//該当メソッドを検索
			methodLoop:
			for (IMethod im : type.getMethods()) {
				//メソッド名を比較
				if (!method.getName().equals(im.getElementName())) continue;
				//引数なし
				if (im.getParameterTypes().length <= 0 && method.getParameters() == null) {
					imethod = im;
					break;
				}
				//引数の数を比較
				if (im.getParameterTypes().length != method.getParameters().length) continue;
				//引数あり
				for (int i=0; i<im.getParameterTypes().length; i++) {
					//ソースコードの引数の型名を完全限定名に変換(String -> java.lang.String)
					//String types[] = im.getParameterTypes(); //[QString;みたいな形式
					//String unresolvedParameter = Signature.toString(im.getParameterTypes()[i]);
					//String fullQualifiedParameterType = getFullQualifiedTypeNameFromSimpleTypeName(unresolvedParameter, im);
					String fullQualifiedParameterType = getFullQualifiedTypeNameFromUnresolvedName(im.getParameterTypes()[i], im);
					//引数型名比較
					if (!method.getParameters()[i].equals(fullQualifiedParameterType)) break;					
					//最終引数が一致した
					if (i >= im.getParameterTypes().length-1 && i >= method.getParameters().length-1) {
						//メソッド一致
						imethod = im;
						break methodLoop;
					}
				}
			}

		} catch (JavaModelException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return imethod;
	}
	
	/**
	 * 引数の型名を完全限定名に変換する．
	 * 例 String -> java.lang.String
	 * @param parameterType 引数の型名．解決されていない形．(例：String[]など)
	 * @param method 引数が所属するメソッド
	 * @return
	 */
	private String getFullQualifiedTypeNameFromSimpleTypeName(String parameterType, IMethod method) {
		//String arrayBracket = 
		String fullQualifiedType = parameterType; //プリミティブ形の場合に使用
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
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return fullQualifiedType;
	}
	
	
	

	/**
	 * 引数の未解決の型名から完全限定名を得る．
	 * 例 [[QString; -> java.lang.String[][]
	 * @param unresolvedName 引数の未解決の型名．
	 * @param imethod この引数の所属するメソッド．
	 * @return 完全限定名
	 */
	private String getFullQualifiedTypeNameFromUnresolvedName(String unresolvedName, IMethod imethod) {
		//配列の処理
		int bracketLastIndex = unresolvedName.lastIndexOf("["); //"[[QString;".lastIndexOf("[[") -> 1
		String typeName = unresolvedName.substring(bracketLastIndex + 1); //[がない場合は-1が返されるため0となる
		String arrayBracket = unresolvedName.substring(0, bracketLastIndex + 1);
		//デコード
		String resolvedName = Signature.toString(typeName);
		String fullQualifiedType = getFullQualifiedTypeNameFromSimpleTypeName(resolvedName,imethod);
		//完全限定名を生成
		return fullQualifiedType + arrayBracket.replace("[", "[]");
	}
	
	//アクセサ
	public File getTargetDirectory() {
		return targetDirectory;
	}
	
}
