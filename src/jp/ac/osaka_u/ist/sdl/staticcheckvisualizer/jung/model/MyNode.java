package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model;

/**
 * �O���t�̃m�[�h��\���N���X�D
 * @author y-mutoh
 *
 */
public class MyNode
{
	/**
	 * �P�����D�\���ɗp����D
	 */
    private String simpleName;
    
    /**
     * ���S���薼�D�����Ńm�[�h�̔�r�ɗp����D
     */
    private String fullQualifiedName;
    
    /**
     * �J�o���b�W�D�~�O���t�̊����ɗp����D
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
	 * ���S���薼����������Γ���m�[�h�ƌ��Ȃ��D
	 * @param obj ��r�Ώۃm�[�h
	 * @return ��r���ʁD����ł����true�C�قȂ��Ă����false�D
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof MyNode)) return false;
		MyNode node = (MyNode)obj;
		return node.getFullQualifiedName().equals(this.fullQualifiedName);
	}
    
	/**
	 * �P������\���ɗp����D
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
