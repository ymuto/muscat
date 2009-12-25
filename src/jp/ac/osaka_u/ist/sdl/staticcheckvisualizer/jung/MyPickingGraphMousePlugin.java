package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.event.MouseEvent;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;


public class MyPickingGraphMousePlugin<V,E> extends PickingGraphMousePlugin<V,E> {
	
	/**
	 * このプラグインを登録しているJungManager。
	 * 選択されているノードを伝えるために必要。
	 */
	private JungManager jungManager;
	
	/**
	 * コンストラクタ。
	 * @param jungManager このプラグインを登録しているJungManager
	 */
	public MyPickingGraphMousePlugin(JungManager jungManager) {
		this.jungManager = jungManager;
	}

    public void mousePressed(MouseEvent e) {
    	//現在対象となっているvertexの登録などを行う。
    	super.mousePressed(e);
    	//押された頂点の文字列表示
		if (vertex != null) {
			//this.jungManager.setSelectedNode((MyNode)vertex);
			System.out.println(vertex.toString());
		}
    }
    
}
