package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ���[�e�B���e�B�N���X�D��Ƀf�o�b�O�p�D
 * @author y-mutoh
 *
 */
public class Utility {
	
	/**
	 * 2��List�𕹍�����ArrayList�𐶐�����D
	 * �����͎�����D
	 * @param <E>
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static <E> List<E> margeList(List<E> list1, List<E> list2) {
		Set<E> set1 = new HashSet<E>(list1);
		Set<E> set2 = new HashSet<E>(list2);
		Set<E> retSet = new HashSet<E>();
		if (set1 == null && set2 == null) return null;
		if (set1 == null) return new ArrayList<E>(set2);
		if (set2 == null) return new ArrayList<E>(set1);
		set1.addAll(set2);
		return new ArrayList<E>(retSet);
	}
	
	/**
	 * �d���̂Ȃ��悤�ɁClist1��list2��ǉ�����D
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
	 * �z���glue�ŘA�����ĕ�����Ƃ��ĕԂ�
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
	
	/**
	 * �f�o�b�O�p�D
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
