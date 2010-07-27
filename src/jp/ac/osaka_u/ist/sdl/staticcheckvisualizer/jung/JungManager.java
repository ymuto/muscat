package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.event.EventListenerList;

import org.eclipse.swt.graphics.Point;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyEdge;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyEdgeList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNodeList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Callee;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;

public class JungManager implements ItemSelectable {
	
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
	 * @param targetClasses �ΏۂƂȂ�N���X���X�g�D
	 */
	public JungManager(TargetClassList targetClasses) {
		this.targetClasses = targetClasses;
		//�O���t����
		createGraph();
	}
	
	/**
	 * �N���X���X�g����O���t�𐶐�����D
	 */
	private void createGraph() {
		if (this.targetClasses == null) return;
		
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
	}
	
	/**
	 * �O���t�̕\���ݒ���s���D
	 */
	public void doSetting() {
	  	//�O���t�\���ݒ�
//		layout = new DAGLayout<MyNode,MyEdge>(g);
//		layout = new SpringLayout<MyNode,MyEdge>(g);
//		layout = new SpringLayout2<MyNode,MyEdge>(g);
//    	layout = new CircleLayout<MyNode,MyEdge>(g);
    	layout = new ISOMLayout<MyNode,MyEdge>(g);
//    	layout = new FRLayout<MyNode,MyEdge>(g);
//    	layout = new KKLayout<MyNode,MyEdge>(g);
    	layout.setSize(new Dimension(this.width, this.height)); // sets the initial size of the space
    	
    	vv = new VisualizationViewer<MyNode,MyEdge>(layout);
    	//�\���G���A�̃T�C�Y��ݒ�
    	vv.setPreferredSize(new Dimension(this.width, this.height)); //Sets the viewing area size
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
        
        this.saveGraph("C:\\Users\\y-mutoh\\research\\graph1.jpg");
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
	
	//TODO �^�����ȉ摜���ۑ������
	public void saveGraph(String fileName) {
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_BGR);
		//�O���t��`��
		Graphics g = image.createGraphics();
		vv.printAll(g);
		//vv.paint(g);
		
		//�ۑ�
		try {
			ImageIO.write(image, "jpeg", new File(fileName));
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			g.dispose();
		}
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
	
	/**
	 * �O���t�̈�̃T�C�Y��ݒ肷��D
	 * @param size
	 */
	public void setSize(Point size) {
		this.width = size.x;
		this.height = size.y;
	}
	
	/**
	 * �O���t�̈�̃T�C�Y��ݒ肷��D
	 * @param size
	 */
	public void setSize(Dimension size) {
		this.width = size.width;
		this.height = size.height;
	}
	
	/**
	 * �O���t�̈�̃T�C�Y��ݒ肷��D
	 * @param size
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
}
