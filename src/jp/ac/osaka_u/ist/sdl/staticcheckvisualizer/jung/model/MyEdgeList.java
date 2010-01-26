package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model;

import java.util.ArrayList;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config.Config;


public class MyEdgeList extends ArrayList<MyEdge> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * �d�݂̍ő�l�D
	 */
	private int maxWeight;
	
	/**
	 * 0�łȂ��d�݂̍ŏ��l�D
	 */
	private int minWeight;
	
	public void update()
	{
		updateMinMax();
		//�G�b�W�̑������v�Z
		for (MyEdge edge : this) {
			if (edge == null) continue;
			int width = judgeLevel(edge.getWeight(), this.minWeight, this.maxWeight, Config.getInstance().getEdgeMaxLevel());
			System.out.println(width);
			edge.setWidth(width);
		}
	}
	
	/**
	 * �ő�l�E�ŏ��l���X�V����D
	 */
	private void updateMinMax() 
	{
		int max = this.get(0).getWeight();
		int min = this.get(0).getWeight();
		for (MyEdge edge: this) {
			System.out.println(edge.getWeight());
			if (edge.getWeight() <= 0) break;
			if (edge.getWeight() > max) max = edge.getWeight();
			if (edge.getWeight() < min) min = edge.getWeight();
		}
		this.maxWeight = max;
		this.minWeight = min;
		System.out.println("max=" + this.maxWeight);
		System.out.println("min=" + this.minWeight);
	}

	//TODO ���ǂ̗]�n����D
	/**
	 * �G�b�W�����x����������D
	 * 0�̓��x��0�Ƃ��C1�ȏ�̓��x��1�ȏ�Ƃ���D
	 * @param data ���x������Ώ�
	 * @param min 0���傫���ŏ��l
	 * @param max �ő�l
	 * @param maxLevel �ő僌�x��
	 * @return ���x���D���x��0����ő僌�x���܂ł̊Ԃ̂����ꂩ�̐����l�D
	 */
	private int judgeLevel(int data, int min, int max, int maxLevel) {
		if (data <= 0) return 0;
		//�Ώۂ�1�ȏ�̏ꍇ�̓��x�����K��1�ȏ�
		int span = (max-min) / maxLevel;
		if (span <= 0) return data-min + 1;
		return (data-min) / span + 1;
	}
}
