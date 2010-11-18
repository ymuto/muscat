package jp.ac.osaka_u.ist.sdl.muscat.jung;

import javax.swing.Icon;
//import javax.swing.ImageIcon;

import jp.ac.osaka_u.ist.sdl.muscat.config.Config;
import jp.ac.osaka_u.ist.sdl.muscat.jung.model.MyNode;

import org.apache.commons.collections15.Transformer;
 
public class MyVertexIconTransformer<V> implements Transformer<V,Icon>{
 
	/**
	 * ノードの形を円グラフにする．
	 */
    public Icon transform(V v) {
        MyNode node = (MyNode)v;
        return new PieChart(node.getCoverage(),Config.NODE_WIDTH, Config.NODE_HEIGHT);
    }
 
}
