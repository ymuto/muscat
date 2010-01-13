package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

public class Callee {
	/**
	 * �Ăяo���񐔁B
	 */
	private int callCount;
	
	/**
	 * �Ăяo���ꂽ�N���X�B
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
	
	//�A�N�Z�T
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
