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
	 * 対象となるクラス一覧
	 */
	private TargetClassList targetClasses;
	
	/**
	 * グラフ．
	 */
	private Graph<MyNode, MyEdge> g;
	
	/**
	 * ノード．
	 */
	private MyNodeList nodes;
	
	/**
	 * エッジ．
	 */
	private MyEdgeList edges;
	
	/**
	 * レイアウト．(円環レイアウトアルゴリズムなど)
	 */
	private Layout<MyNode, MyEdge> layout;
	
	/**
	 * 可視化サーバ．
	 */
	private VisualizationViewer<MyNode, MyEdge> vv;
	
	/**
	 * グラフの横幅．
	 */
	private int width;
	
	/**
	 * グラフの高さ．
	 */
	private int height;
	
	/**
	 * 現在選択されているノード．
	 */
	private Set<MyNode> selectedNodes;
	
	/**
	 * ノードの移動，選択，拡大・縮小
	 */
	private DefaultModalGraphMouse<MyNode,MyEdge> defaultModalGraphMouse;
	
	
	protected EventListenerList listenerList = new EventListenerList();
	
	/**
	 * ノード選択の変化を検知するリスナ．
	 */
	private ItemListener itemListener = new ItemListener() {
		/**
		 * 選択されているノードの集合を更新する．
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {
			System.out.println("jungManager.itemStateChanged");
			selectedNodes = vv.getPickedVertexState().getPicked();
		}
	};
	
	
	/**
	 * コンストラクタ．
	 */
	public JungManager(TargetClassList targetClasses) {
		this.targetClasses = targetClasses;
		
		//グラフ
    	g = new DirectedSparseMultigraph<MyNode,MyEdge>();
    	
       	//ノード追加
    	nodes = new MyNodeList();
		for (TargetClass c: targetClasses) {
			MyNode node = new MyNode(c.getSimpleName(), c.getFullQualifiedName(), c.getCoverage());
			nodes.add(node);
			g.addVertex(node);
		}
		
		System.out.println("ノード追加完了");
		
	   	//エッジ追加
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
		
		System.out.println("エッジ追加完了");
		
	  	//グラフ表示設定
    	layout = new CircleLayout<MyNode,MyEdge>(g);
    	layout.setSize(new Dimension(400,400)); // sets the initial size of the space
    	vv = new VisualizationViewer<MyNode,MyEdge>(layout);
    	//表示エリアのサイズを設定
    	vv.setPreferredSize(new Dimension(500,400)); //Sets the viewing area size
    	// ノードにクラス名を表示
    	vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<MyNode>());
    	//ノードの形変更
        vv.getRenderContext().setVertexShapeTransformer(new MyVertexShapeTransformer());
        //ノードを円グラフに変更
        vv.getRenderContext().setVertexIconTransformer(new MyVertexIconTransformer<MyNode>());
    	//エッジの太さを変更
    	vv.getRenderContext().setEdgeStrokeTransformer(new MyEdgeStrokeTransformer<MyEdge>());
    	//マウスでノード移動・ホイールで拡大縮小
        defaultModalGraphMouse = new DefaultModalGraphMouse<MyNode,MyEdge>();
        defaultModalGraphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(defaultModalGraphMouse);
        //ノード選択の変化を受け取るリスナ
        vv.getPickedVertexState().addItemListener(itemListener);
        
    	//マウスホイールで拡大・縮小
    	//graphMouse.add(new ScalingGraphMousePlugin(new LayoutScalingControl(), 0));
    	//vv.setGraphMouse(graphMouse);
    	
	}

	/**
	 * 可視化サーバを返す．
	 * @return 可視化サーバ．これをJFrameに追加することでグラフを表示できる．
	 */
	public VisualizationViewer<MyNode, MyEdge> getVisualizationServer() {
		return vv;
	}

	/**
	 * 現在選択されているノードの集合を返す．
	 * @return 現在選択されているノードの集合
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
