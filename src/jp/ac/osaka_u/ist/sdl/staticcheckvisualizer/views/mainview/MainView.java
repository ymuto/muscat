package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.mainview;

import java.awt.Frame;
import java.util.Set;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.StaticCheckVisualizer;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.model.MyNode;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace.WorkspaceManager;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

public class MainView extends ViewPart implements ISelectionProvider {
	private StaticCheckVisualizer scv;
	
	//�t�H�[�����i
	/**
	 * �`�F�b�N�J�n�{�^��
	 */
	private Button btnCheck;
	/**
	 * �X�V�{�^��
	 */
	private Button btnRefresh;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.MainView";

	/**
	 * The constructor.
	 */
	public MainView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		scv = Activator.getScv();
		
		if (!scv.isFinishedCheck()) {
			//�ÓI�`�F�b�N���J�n����Ă��Ȃ��Ƃ�
			//TODO �J�n�{�^����ݒu
			btnCheck = new Button(parent, SWT.PUSH);
			btnCheck.setText("�ÓI�`�F�b�N���J�n");
			btnCheck.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("�`�F�b�N�{�^���������ꂽ");
					//���s
					Activator.getScv().execute();
				}
			});
			//TODO ��ʍX�V�{�^����ݒu
			btnRefresh = new Button(parent, SWT.PUSH);
			btnRefresh.setText("�X�V");
			btnRefresh.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO �����������ꂽ���\�b�h�E�X�^�u
					System.out.println("�X�V�{�^���������ꂽ");
				}
			});
			return;
		}
		
    	//�E�B���h�E
		Composite c = new Composite(parent,SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(c);
    	frame.add(scv.getJungManager().getVisualizationServer()); //�O���t���Z�b�g
    		
    	//�m�[�h�I�������\�b�h�r���[�ɓ`����
    	this.getSite().setSelectionProvider(this); 
    	
	}
		

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}

	//�ȉ��I�����ꂽ�m�[�h��ʒm���邽�߂̃��\�b�h
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
	}

	/**
	 * �I������Ă���TargetClassList��Ԃ��B
	 */
	@Override
	public ISelection getSelection() {
		System.out.println("MainView.getSelection()");
		if (scv.getSelectedTargetClasses() == null) return null;
		return new StructuredSelection(scv.getSelectedTargetClasses());
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}

	@Override
	public void setSelection(ISelection arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		System.out.println("MainView.setSelection");
	}

}