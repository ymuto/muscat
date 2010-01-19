package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.event.MouseEvent;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;


public class MyPickingGraphMousePlugin<V,E> extends PickingGraphMousePlugin<V,E> {
	
	/**
	 * ���̃v���O�C����o�^���Ă���JungManager�D
	 * �I������Ă���m�[�h��`���邽�߂ɕK�v�D
	 */
	private JungManager jungManager;
	
	/**
	 * �R���X�g���N�^�D
	 * @param jungManager ���̃v���O�C����o�^���Ă���JungManager
	 */
	public MyPickingGraphMousePlugin(JungManager jungManager) {
		this.jungManager = jungManager;
	}

    public void mousePressed(MouseEvent e) {
    	//���ݑΏۂƂȂ��Ă���vertex�̓o�^�Ȃǂ��s���D
    	super.mousePressed(e);
    	//�����ꂽ���_�̕�����\��
		if (vertex != null) {
			//this.jungManager.setSelectedNode((MyNode)vertex);
			System.out.println(vertex.toString());
		}
    }
    
}
