package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.event.MouseEvent;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;


public class MyPickingGraphMousePlugin<V,E> extends PickingGraphMousePlugin<V,E> {
	
	/**
	 * ���̃v���O�C����o�^���Ă���JungManager�B
	 * �I������Ă���m�[�h��`���邽�߂ɕK�v�B
	 */
	private JungManager jungManager;
	
	/**
	 * �R���X�g���N�^�B
	 * @param jungManager ���̃v���O�C����o�^���Ă���JungManager
	 */
	public MyPickingGraphMousePlugin(JungManager jungManager) {
		this.jungManager = jungManager;
	}

    public void mousePressed(MouseEvent e) {
    	//���ݑΏۂƂȂ��Ă���vertex�̓o�^�Ȃǂ��s���B
    	super.mousePressed(e);
    	//�����ꂽ���_�̕�����\��
		if (vertex != null) {
			//this.jungManager.setSelectedNode((MyNode)vertex);
			System.out.println(vertex.toString());
		}
    }
    
}
