package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ユーティリティクラス．
 * @author y-mutoh
 *
 */
public class Utility {
	
	/**
	 * コンストラクタ．インスタンス化不可．
	 */
	private Utility() {}
	
	/**
	 * 2つのListを併合したArrayListを生成する．
	 * 順序は失われる．
	 * @param <E>
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static <E> List<E> margeList(List<E> list1, List<E> list2) {
		if (list1 == null && list2 == null) return null;
		if (list1 == null) return list2;
		if (list2 == null) return list1;
		//Setに変換
		Set<E> set1 = new HashSet<E>(list1);
		Set<E> set2 = new HashSet<E>(list2);
		//Setを併合
		set1.addAll(set2);
		return new ArrayList<E>(set1);
	}
	
	/**
	 * 重複のないように，list1へlist2を追加する．
	 * @param <E>
	 * @param list1
	 * @param list2
	 */
	public static <E> void addAllNoOverlaps(List<E> list1, List<E> list2) {
		for (E element : list2) {
			if (!list1.contains(element)) list1.add(element);
		}
	}
	
	/**
	 * 配列をglueで連結して文字列として返す．
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
	
	public static String concatArray(String[] array) {
		return concatArray(array, ", ");
	}
	
	/**
	 * Get extension from given file name.
	 * If given file name has no extension, return null.
	 * ファイルの拡張子を取得する．
	 * @param filename
	 * @return extension without dot
	 */
	public static String getExtensionFromFileName(String filename) {
		int indexOfDot = filename.lastIndexOf(".");
		if (indexOfDot < 0 || filename.length()-1 <= indexOfDot) return null;
		return filename.substring(indexOfDot+1);
	}
	
	
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

}
