package jp.ac.osaka_u.ist.sdl.muscat.jung.model;


public class MyEdge {
	/**
	 * エッジの重み．内部用．呼び出し回数に相当する．
	 */
	private int weight;
	
	/**
	 * エッジの太さ．表示用．
	 */
	private int width;
	
	/**
	 * 呼び出し元クラス．開始ノード．
	 */
	private MyNode callerNode;
	
	/**
	 * 呼び出し先クラス．終了ノード．
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
//	 * 開始ノード，終了ノードが等しければ同一エッジと見なす．
//	 * @param 比較対象エッジ．
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

	//アクセサ
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
