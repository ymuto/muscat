package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility;

/**
 * ユーティリティクラス．主にデバッグ用．
 * @author y-mutoh
 *
 */
public class Utility {
	/**
	 * デバッグ用．
	 * @param obj
	 */
	public static void debugPrintObject(Object obj) {
		if (obj == null) {
			System.out.println("null");
		} else {
			System.out.println(obj.getClass().getName() + " " + obj.toString() + " != null");
		}
	}
	
	/**
	 * 配列をglueで連結して文字列として返す
	 * @return
	 */
	public static String concatArray(String[] array, String glue) {
		if (array == null) return null;
		if (array.length <= 0) return "";
		StringBuilder builder = new StringBuilder();
		builder.append(array[0]);
		for (int i=1; i<array.length; i++){
			builder.append(glue);
			builder.append(array[i]);
		}
		return builder.toString();
	}

}
