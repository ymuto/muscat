package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.preference;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * プリファレンスページ．
 * 「ウィンドウ > 設定」から表示できる設定画面を作成する．
 * @author y-mutoh
 *
 */
public class SCVPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public SCVPreferencePage() {
		super(GRID);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	/**
	 * 設定画面の項目
	 */
	@Override
	protected void createFieldEditors() {
		//生成コマンド
		this.addField(new StringFieldEditor(PreferenceKey.GENERATE_COMMAND, "XML生成コマンド", this.getFieldEditorParent()));
		//出力ディレクトリ
		this.addField(new DirectoryFieldEditor(PreferenceKey.OUTPUT_DIR, "XML出力先ディレクトリ", this.getFieldEditorParent()));
		//エッジの最大レベル
		this.addField(new IntegerFieldEditor(PreferenceKey.EDGE_MAX_LEVEL, "エッジのレベル数", this.getFieldEditorParent()));
	}
	
	/**
	 * 「適用」「OK」ボタンがおされたとき
	 */
	public boolean performOk() {
		super.performOk();
		//内部設定を更新
		Activator.getConfig().updateFromPreference();
		return true;
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO 自動生成されたメソッド・スタブ
	}
	


}
