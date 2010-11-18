package jp.ac.osaka_u.ist.sdl.muscat.jung.model;


public class MyEdge {
	/**
	 * �G�b�W�̏d�݁D�����p�D�Ăяo���񐔂ɑ�������D
	 */
	private int weight;
	
	/**
	 * �G�b�W�̑����D�\���p�D
	 */
	private int width;
	
	/**
	 * �Ăяo�����N���X�D�J�n�m�[�h�D
	 */
	private MyNode callerNode;
	
	/**
	 * �Ăяo����N���X�D�I���m�[�h�D
	 */
	private MyNode calleeNode;
	
	public MyEdge(MyNode callerNode, MyNode calleeNode) {
		this.callerNode = callerNode;
		this.calleeNode = calleeNode;
		this.weight = 1;
	}
	
	public MyEdge(MyNode callerNode, MyNode calleeNode, int weight) {
		this.callerNode = callerNode;
		this.calleeNode = calleeNode;
		this.weight = weight;
	}
	
	public MyEdge() {
		this.weight = 0;
	}
	
//	/**
//	 * �J�n�m�[�h�C�I���m�[�h����������Γ���G�b�W�ƌ��Ȃ��D
//	 * @param ��r�ΏۃG�b�W�D
//	 * @return
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null) return false;
//		if (!(obj instanceof MyEdge)) return false;
//		MyEdge edge = (MyEdge)obj;
//		if (!edge.getCallerNode().equals(this.callerNode)) return false;
//		if (!edge.getCalleeNode().equals(this.calleeNode)) return false;
//		return true;
//	}
	


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

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
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
