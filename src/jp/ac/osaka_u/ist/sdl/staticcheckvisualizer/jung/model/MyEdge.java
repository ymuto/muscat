package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model;


public class MyEdge {
	/**
	 * �G�b�W�̏d�݁B�\���p�B
	 */
	private int weight;
	
	/**
	 * �Ăяo�����N���X�B�J�n�m�[�h�B
	 */
	private MyNode callerNode;
	
	/**
	 * �Ăяo����N���X�B�I���m�[�h�B
	 */
	private MyNode calleeNode;
	
	public MyEdge(MyNode callerNode, MyNode calleeNode) {
		this.callerNode = callerNode;
		this.calleeNode = calleeNode;
		this.weight = 1;
	}
	
	public MyEdge() {
		this.weight = 0;
	}
	
	/**
	 * �J�n�m�[�h�A�I���m�[�h����������Γ���G�b�W�ƌ��Ȃ��B
	 * @param ��r�ΏۃG�b�W�B
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		MyEdge edge = (MyEdge)o;
		if (!edge.getCallerNode().equals(this.callerNode)) return false;
		if (!edge.getCalleeNode().equals(this.calleeNode)) return false;
		return true;
	}

	@Override
	public String toString() {
		String callerNodeName = this.callerNode.getSimpleName();
		String calleeNodeName = this.calleeNode.getSimpleName();
		return callerNodeName + "-(" + this.weight + ")->" + calleeNodeName;
	}
	
	public void incrementWeight() {
		this.weight++;
	}

	//�A�N�Z�T
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public MyNode getCallerNode() {
		return callerNode;
	}

	public void setCallerNode(MyNode callerNode) {
		this.callerNode = callerNode;
	}

	public MyNode getCalleeNode() {
		return calleeNode;
	}

	public void setCalleeNode(MyNode calleeNode) {
		this.calleeNode = calleeNode;
	}
	
	
	
}
