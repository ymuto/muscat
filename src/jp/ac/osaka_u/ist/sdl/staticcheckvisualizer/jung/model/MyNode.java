package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model;

/**
 * グラフのノードを表すクラス．
 * @author y-mutoh
 *
 */
public class MyNode
{
	/**
	 * 単純名．表示に用いる．
	 */
    private String simpleName;
    
    /**
     * 完全限定名．内部でノードの比較に用いる．
     */
    private String fullQualifiedName;
    
    /**
     * カバレッジ．円グラフの割合に用いる．
     */
    private int coverage;
 
    public MyNode(String simpleName, String fullQualifiedName, int coverage)
    {
        this.simpleName = simpleName;
        this.fullQualifiedName = fullQualifiedName;
        this.coverage = coverage;
    }
 
    public MyNode()
    {
        this.simpleName = "noname";
        this.fullQualifiedName = null;
        this.coverage = 0;
    }
 
    
	/**
	 * 完全限定名が等しければ同一ノードと見なす．
	 * @param obj 比較対象ノード
	 * @return 比較結果．同一であればtrue，異なっていればfalse．
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof MyNode)) return false;
		MyNode node = (MyNode)obj;
		return node.getFullQualifiedName().equals(this.fullQualifiedName);
	}
    
	/**
	 * 単純名を表示に用いる．
	 */
	@Override
	public String toString()
    {
        return this.simpleName;
    }

	//アクセサ
    public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getFullQualifiedName() {
		return fullQualifiedName;
	}

	public void setFullQualifiedName(String fullQualifiedName) {
		this.fullQualifiedName = fullQualifiedName;
	}

	public int getCoverage() {
        return coverage;
    }
 
    public void setCoverage(int coverage) {
        this.coverage = coverage;
    }
 
}
