package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.event.EventListenerList;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyEdge;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyEdgeList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNodeList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Callee;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;

public class JungManager implements ItemSelectable {
	/**
	 * �ΏۂƂȂ�N���X�ꗗ
	 */
	private TargetClassList targetClasses;
	
	/**
	 * �O���t�D
	 */
	private Graph<MyNode, MyEdge> g;
	
	/**
	 * �m�[�h�D
	 */
	private MyNodeList nodes;
	
	/**
	 * �G�b�W�D
	 */
	private MyEdgeList edges;
	
	/**
	 * ���C�A�E�g�D(�~���C�A�E�g�A���S���Y���Ȃ�)
	 */
	private Layout<MyNode, MyEdge> layout;
	
	/**
	 * �����T�[�o�D
	 */
	private VisualizationViewer<MyNode, MyEdge> vv;
	
	/**
	 * �O���t�̉����D
	 */
	private int width;
	
	/**
	 * �O���t�̍����D
	 */
	private int height;
	
	/**
	 * ���ݑI������Ă���m�[�h�D
	 */
	private Set<MyNode> selectedNodes;
	
	/**
	 * �m�[�h�̈ړ��C�I���C�g��E�k��
	 */
	private DefaultModalGraphMouse<MyNode,MyEdge> defaultModalGraphMouse;
	
	
	protected EventListenerList listenerList = new EventListenerList();
	
	/**
	 * �m�[�h�I���̕ω������m���郊�X�i�D
	 */
	private ItemListener itemListener = new ItemListener() {
		/**
		 * �I������Ă���m�[�h�̏W�����X�V����D
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {
			System.out.println("jungManager.itemStateChanged");
			selectedNodes = vv.getPickedVertexState().getPicked();
		}
	};
	
	
	/**
	 * �R���X�g���N�^�D
	 */
	public JungManager(TargetClassList targetClasses) {
		this.targetClasses = targetClasses;
		
		//�O���t
    	g = new DirectedSparseMultigraph<MyNode,MyEdge>();
    	
       	//�m�[�h�ǉ�
    	nodes = new MyNodeList();
		for (TargetClass c: targetClasses) {
			MyNode node = new MyNode(c.getSimpleName(), c.getFullQualifiedName(), c.getCoverage());
			nodes.add(node);
			g.addVertex(node);
		}
		
		System.out.println("�m�[�h�ǉ�����");
		
	   	//�G�b�W�ǉ�
		edges = new MyEdgeList();
		for (TargetClass caller: targetClasses) {
			if (caller.getCallees() == null) continue;
			for (Callee callee: caller.getCallees()) {
				MyNode callerNode = nodes.searchMyNode(caller.getFullQualifiedName());
				MyNode calleeNode = nodes.searchMyNode(callee.getCalleeClass().getFullQualifiedName());
				if (callerNode == null || calleeNode == null) continue;
				MyEdge edge = new MyEdge(callerNode, calleeNode, callee.getCallCount());
				edges.add(edge);
				g.addEdge(edge, edge.getCallerNode(), edge.getCalleeNode());
			}
		}
		edges.update();
		
		System.out.println("�G�b�W�ǉ�����");
		
	  	//�O���t�\���ݒ�
    	layout = new CircleLayout<MyNode,MyEdge>(g);
    	layout.setSize(new Dimension(400,400)); // sets the initial size of the space
    	vv = new VisualizationViewer<MyNode,MyEdge>(layout);
    	//�\���G���A�̃T�C�Y��ݒ�
    	vv.setPreferredSize(new Dimension(500,400)); //Sets the viewing area size
    	// �m�[�h�ɃN���X����\��
    	vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<MyNode>());
    	//�m�[�h�̌`�ύX
        vv.getRenderContext().setVertexShapeTransformer(new MyVertexShapeTransformer());
        //�m�[�h���~�O���t�ɕύX
        vv.getRenderContext().setVertexIconTransformer(new MyVertexIconTransformer<MyNode>());
    	//�G�b�W�̑�����ύX
    	vv.getRenderContext().setEdgeStrokeTransformer(new MyEdgeStrokeTransformer<MyEdge>());
    	//�}�E�X�Ńm�[�h�ړ��E�z�C�[���Ŋg��k��
        defaultModalGraphMouse = new DefaultModalGraphMouse<MyNode,MyEdge>();
        defaultModalGraphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(defaultModalGraphMouse);
        //�m�[�h�I���̕ω����󂯎�郊�X�i
        vv.getPickedVertexState().addItemListener(itemListener);
        
    	//�}�E�X�z�C�[���Ŋg��E�k��
    	//graphMouse.add(new ScalingGraphMousePlugin(new LayoutScalingControl(), 0));
    	//vv.setGraphMouse(graphMouse);
    	
	}

	/**
	 * �����T�[�o��Ԃ��D
	 * @return �����T�[�o�D�����JFrame�ɒǉ����邱�ƂŃO���t��\���ł���D
	 */
	public VisualizationViewer<MyNode, MyEdge> getVisualizationServer() {
		return vv;
	}

	/**
	 * ���ݑI������Ă���m�[�h�̏W����Ԃ��D
	 * @return ���ݑI������Ă���m�[�h�̏W��
	 */
	public Set<MyNode> getSelectedNodes() {
		return selectedNodes;
	}

	//TODO
	@Override
    public void addItemListener(ItemListener l) {
        listenerList.add(ItemListener.class, l);
        
    }

	//TODO
	@Override
    public void removeItemListener(ItemListener l) {
        listenerList.remove(ItemListener.class, l);
    }

	//TODO
	@Override
	public Object[] getSelectedObjects() {
		if (selectedNodes == null) return null;
		return selectedNodes.toArray();
	}
	
}
