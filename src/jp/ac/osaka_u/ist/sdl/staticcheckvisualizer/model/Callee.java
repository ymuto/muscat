package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

public class Callee {
	/**
	 * 呼び出し回数。
	 */
	private int callCount;
	
	/**
	 * 呼び出されたクラス。
	 */
	private TargetClass calleeClass;

	public Callee(TargetClass calleeClass, int callCount) {
		this.calleeClass = calleeClass;
		this.callCount = callCount;
	}
	
	@Override
	public String toString() {
		return "[" + callCount + "]" + calleeClass.getFullQualifiedName();
	}
	
	//アクセサ
	public int getCallCount() {
		return callCount;
	}

	public void setCallCount(int callCount) {
		this.callCount = callCount;
	}

	public TargetClass getCalleeClass() {
		return calleeClass;
	}

	public void setCalleeClass(TargetClass calleeClass) {
		this.calleeClass = calleeClass;
	}


	
	
}
