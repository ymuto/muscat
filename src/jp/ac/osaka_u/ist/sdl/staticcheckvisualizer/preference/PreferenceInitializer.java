package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.preference;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace.WorkspaceManager;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer{

	@Override
	public void initializeDefaultPreferences() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceKey.GENERATE_COMMAND, Activator.getConfig().DEFAULT_GENERATE_COMMAND);
		store.setDefault(PreferenceKey.OUTPUT_DIR, WorkspaceManager.getWorkspaceDirPath());
		store.setDefault(PreferenceKey.EDGE_MAX_LEVEL, Activator.getConfig().DEFAULT_EDGE_MAX_LEVEL);
	}

}