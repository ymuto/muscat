package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.mainview;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;




import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.StaticCheckVisualizer;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config.Config;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung.JungManager;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

public class MainView extends ViewPart implements ISelectionProvider {
	private StaticCheckVisualizer scv;
	
	/**
	 * �A�N�V�����D
	 */
	private IAction refreshAction;
	private IAction executeAction;
	private IAction exportAction;

	
	/**
	 * �O���t��\������̈�iSWT�j
	 */
	private Composite composite;
	
	/**
	 * �O���t��\������̈�iAWT�j
	 */
	private Frame frame;
	

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
		
		//�c�[���o�[�쐬
		createActions();
		
		//�E�B���h�E�ɃO���t�̈���쐬	
		composite = new Composite(parent,SWT.EMBEDDED);
		frame = SWT_AWT.new_Frame(composite);
		
    	//�m�[�h�I�������\�b�h�r���[�ɓ`����
    	this.getSite().setSelectionProvider(this);
	}
	
	/**
	 * �r���[�̃O���t�̈�ɃO���t���Z�b�g
	 */
	public void setGraph() {
		JungManager jungManager = scv.getJungManager();
		//TODO �O���t�T�C�Y�̌v�Z
		jungManager.setSize(500,500);
		jungManager.doSetting();
		frame.removeAll();
    	frame.add(jungManager.getVisualizationServer()); //�O���t���Z�b�g
    	frame.pack(); 	
    	
	}
		
	/**
	 * �A�N�V�������쐬���ăc�[���o�[�ɑg�ݍ��ށD
	 */
	private void createActions() {
		//�c�[���o�[��Create Graph��plugin.xml�ɋL�q�D
    	//���t���b�V���A�N�V�����쐬
    	ImageDescriptor iconRefresh = Activator.getImageDescriptor("icons/refresh.gif");
		this.refreshAction = new Action("Refresh", iconRefresh){
			public void run() {
				//�O���t���Z�b�g
				setGraph();
			}
		};
		//�c�[���o�[�ɑg�ݍ���
		this.getViewSite().getActionBars().getToolBarManager().add(this.refreshAction);
		//�G�N�X�|�[�g�A�N�V�����쐬
		if (Config.EXPORT_ENABLE) {
		   	ImageDescriptor iconExport = Activator.getImageDescriptor("icons/export.gif");
			this.exportAction = new Action("Export", iconExport){
				public void run() {
					export();
				}
			};
			this.getViewSite().getActionBars().getToolBarManager().add(this.exportAction);
		}
		
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
	
	/**
	 * �O���t�̃G�N�X�|�[�g���s���D
	 */
	private void export() {
		//TODO �t�@�C���ۑ��_�C�A���O
		scv.getJungManager().saveGraph("C:\\test.jpg");

	}

}
