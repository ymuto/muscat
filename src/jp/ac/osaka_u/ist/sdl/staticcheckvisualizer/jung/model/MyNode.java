package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model;

/**
 * �O���t�̃m�[�h��\���N���X�B
 * @author y-mutoh
 *
 */
public class MyNode
{
	/**
	 * �P�����B�\���ɗp����B
	 */
    private String simpleName;
    
    /**
     * ���S���薼�B�����Ńm�[�h�̔�r�ɗp����B
     */
    private String fullQualifiedName;
    
    /**
     * �J�o���b�W�B�~�O���t�̊����ɗp����B
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
	 * ���S���薼����������Γ���m�[�h�ƌ��Ȃ��B
	 * @param obj ��r�Ώۃm�[�h
	 * @return ��r���ʁB����ł����true�A�قȂ��Ă����false�B
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		MyNode node = (MyNode)obj;
		return node.getFullQualifiedName().equals(this.fullQualifiedName);
	}
    
	/**
	 * �P������\���ɗp����B
	 */
	@Override
	public String toString()
    {
        return this.simpleName;
    }
 
    //�A�N�Z�T
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
