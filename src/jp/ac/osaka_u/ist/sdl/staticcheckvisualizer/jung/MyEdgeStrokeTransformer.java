package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.BasicStroke;
import java.awt.Stroke;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyEdge;

import org.apache.commons.collections15.Transformer;

/**
 * �G�b�W�̌`������߂�N���X�B
 * @author y-mutoh
 *
 * @param <E>
 */
public class MyEdgeStrokeTransformer<E> implements Transformer<E, Stroke> {
	Stroke edgeStroke;

	@Override
	public Stroke transform(E e) {
		MyEdge edge = (MyEdge)e;
		//�X�g���[�N�𐶐�
		edgeStroke = new BasicStroke(edge.getWidth());
		return edgeStroke;
	}

}
