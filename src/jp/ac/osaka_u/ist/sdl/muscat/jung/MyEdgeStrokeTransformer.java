package jp.ac.osaka_u.ist.sdl.muscat.jung;

import java.awt.BasicStroke;
import java.awt.Stroke;

import jp.ac.osaka_u.ist.sdl.muscat.jung.model.MyEdge;

import org.apache.commons.collections15.Transformer;

/**
 * エッジの形状を求めるクラス．
 * @author y-mutoh
 *
 * @param <E>
 */
public class MyEdgeStrokeTransformer<E> implements Transformer<E, Stroke> {
	Stroke edgeStroke;

	@Override
	public Stroke transform(E e) {
		MyEdge edge = (MyEdge)e;
		//ストロークを生成
		edgeStroke = new BasicStroke(edge.getWidth());
		return edgeStroke;
	}

}
