package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility;

/**
 * ユーティリティクラス。主にデバッグ用。
 * @author y-mutoh
 *
 */
public class Utility {
	/**
	 * デバッグ用。
	 * @param obj
	 */
	public static void debugPrintObject(Object obj) {
		if (obj == null) {
			System.out.println("null");
		} else {
			System.out.println(obj.getClass().getName() + " " + obj.toString() + " != null");
		}
	}
	

}
