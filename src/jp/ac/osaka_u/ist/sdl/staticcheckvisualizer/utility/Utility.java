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
	

}
