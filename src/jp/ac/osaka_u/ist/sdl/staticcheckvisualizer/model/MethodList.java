package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MethodList extends ArrayList<Method> {
	
	/**
	 * 属性のタイトル一覧情報．
	 */
	private ArrayList<String> attributeTitles;
	
	/**
	 * 所属するクラス．
	 */
	private TargetClass targetClass;
	


	public MethodList() {
		attributeTitles = new ArrayList<String>();
	}
	
	/**
	 * XMLノードから読み込む
	 * @param nMethods methodsノード
	 */
	public void importXmlNode(TargetClass targetClass, Node nMethods) {
		this.targetClass = targetClass;
		Element eMethods = (Element)nMethods;
		NodeList nlMethod = eMethods.getElementsByTagName("method");
		//methodに対して処理
		for (int i=0; i<nlMethod.getLength(); i++) {
			Element eMethod = (Element)nlMethod.item(i);
			Method method = new Method();
			method.importXmlNode(this, eMethod);
			this.add(method);
			//属性タイトル一覧を更新
			for (Attribute attribute : method.getAttributes()) {
				String title = attribute.getTitle();
				if (this.attributeTitles.indexOf(title) == -1) {
					attributeTitles.add(title);
				}
			}
		}
	}

	//アクセサ
	/**
	 * 属性タイトル一覧情報を取得する．
	 */
	public ArrayList<String> getAttributeTitles() {
		return attributeTitles;
	}
	
	public TargetClass getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(TargetClass targetClass) {
		this.targetClass = targetClass;
	}
}
