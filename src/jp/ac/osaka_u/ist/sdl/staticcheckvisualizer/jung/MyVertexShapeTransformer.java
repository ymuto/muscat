package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.Shape;
import java.awt.geom.Arc2D;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config.Config;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
 
import org.apache.commons.collections15.Transformer;
 
public class MyVertexShapeTransformer implements Transformer<MyNode, Shape> {
	/**
	 * ノードを円形と認識させる。これによりエッジの矢印がノードにめり込まなくなる。
	 */
    public Shape transform(MyNode v) {
        //円グラフの輪郭と同じ大きさの円
        return new Arc2D.Float(-(Config.NODE_WIDTH/2), -(Config.NODE_HEIGHT/2),
                Config.NODE_WIDTH,Config.NODE_HEIGHT,0,360,Arc2D.OPEN);
    }
}
