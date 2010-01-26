package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.mainview;

import java.awt.Frame;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.StaticCheckVisualizer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

public class MainView extends ViewPart implements ISelectionProvider {
	private StaticCheckVisualizer scv;
	
	private IAction refreshAction;
	
	//�t�H�[�����i
	/**
	 * �`�F�b�N�J�n�{�^��
	 */
	private Button btnCheck;

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
	 * ��ʍ쐬
	 */
	public void createPartControl(Composite parent) {
		scv = Activator.getScv();
		
    	//���t���b�V���A�N�V�����쐬
    	ImageDescriptor icon = Activator.getImageDescriptor("icons/refresh.gif");
		this.refreshAction = new Action("���t���b�V��", icon){
			public void run() {
				//��ʂ̃��t���b�V��
				System.out.println("���C���r���[�����t���b�V��");
				
			}
		};
		//�c�[���o�[�ɑg�ݍ���
		this.getViewSite().getActionBars().getToolBarManager().add(this.refreshAction);
		
		//�`�F�b�N�J�n�{�^��
		if (!scv.isFinishedCheck()) {
			//�ÓI�`�F�b�N���J�n����Ă��Ȃ��Ƃ�
			//TODO �J�n�{�^����ݒu
			btnCheck = new Button(parent, SWT.PUSH);
			btnCheck.setText("�ÓI�`�F�b�N���J�n");
			btnCheck.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("�`�F�b�N�J�n�{�^���������ꂽ");
					//���s
					scv.execute();
				}
			});
			return;
		}
		
    	//�E�B���h�E�ɃO���t��\��
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
	 * �I������Ă���TargetClassList��Ԃ��D
	 */
	@Override
	public ISelection getSelection() {
		System.out.println("MainView.getSelection()");
		if (scv.getSelectedTargetClasses() == null) return null;
		return scv.getSelectedTargetClasses();
		//return new StructuredSelection(scv.getSelectedTargetClasses());
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
