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
 * �v���t�@�����X�y�[�W�D
 * �u�E�B���h�E > �ݒ�v����\���ł���ݒ��ʂ��쐬����D
 * @author y-mutoh
 *
 */
public class SCVPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public SCVPreferencePage() {
		super(GRID);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	/**
	 * �ݒ��ʂ̍���
	 */
	@Override
	protected void createFieldEditors() {
		//�����R�}���h
		this.addField(new StringFieldEditor(PreferenceKey.GENERATE_COMMAND, "XML�����R�}���h", this.getFieldEditorParent()));
		//�o�̓f�B���N�g��
		this.addField(new DirectoryFieldEditor(PreferenceKey.OUTPUT_DIR, "XML�o�͐�f�B���N�g��", this.getFieldEditorParent()));
		//�G�b�W�̍ő僌�x��
		this.addField(new IntegerFieldEditor(PreferenceKey.EDGE_MAX_LEVEL, "�G�b�W�̃��x����", this.getFieldEditorParent()));
	}
	
	/**
	 * �u�K�p�v�uOK�v�{�^���������ꂽ�Ƃ�
	 */
	public boolean performOk() {
		super.performOk();
		//�����ݒ���X�V
		Activator.getConfig().updateFromPreference();
		return true;
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
	}
	


}
