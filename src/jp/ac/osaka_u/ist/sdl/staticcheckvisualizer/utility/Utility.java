package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility;

/**
 * ���[�e�B���e�B�N���X�D��Ƀf�o�b�O�p�D
 * @author y-mutoh
 *
 */
public class Utility {
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

}
