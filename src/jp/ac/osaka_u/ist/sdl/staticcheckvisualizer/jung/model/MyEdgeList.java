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
	 * 重みの最大値．
	 */
	private int maxWeight;
	
	/**
	 * 0でない重みの最小値．
	 */
	private int minWeight;
	
	public void update()
	{
		updateMinMax();
		//エッジの太さを計算
		for (MyEdge edge : this) {
			if (edge == null) continue;
			int width = judgeLevel(edge.getWeight(), this.minWeight, this.maxWeight, Config.getInstance().getEdgeMaxLevel());
			System.out.println(width);
			edge.setWidth(width);
		}
	}
	
	/**
	 * 最大値・最小値を更新する．
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

	//TODO 改良の余地あり．
	/**
	 * エッジをレベル分けする．
	 * 0はレベル0とし，1以上はレベル1以上とする．
	 * @param data レベル判定対象
	 * @param min 0より大きい最小値
	 * @param max 最大値
	 * @param maxLevel 最大レベル
	 * @return レベル．レベル0から最大レベルまでの間のいずれかの整数値．
	 */
	private int judgeLevel(int data, int min, int max, int maxLevel) {
		if (data <= 0) return 0;
		//対象が1以上の場合はレベルも必ず1以上
		int span = (max-min) / maxLevel;
		if (span <= 0) return data-min + 1;
		return (data-min) / span + 1;
	}
}
