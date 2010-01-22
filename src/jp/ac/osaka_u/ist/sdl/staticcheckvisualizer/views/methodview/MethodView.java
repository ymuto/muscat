package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview;

import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.utility.Utility;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.workspace.WorkspaceManager;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.*;


public class MethodView extends ViewPart {
	
	/**
	 * �Œ��i�N���X���C���\�b�h���C�����j�̗�
	 */
	public final static int FIX_COLUMN_COUNT = 3;
	
	//TableView���g�p����
	private TableViewer viewer;
	private Table table;
	private Composite tableParent = null;
	
	ArrayContentProvider contentProvider;
	MethodLabelProvider labelProvider;
	
	//�\���`��
	MethodLabelProvider methodLabelProvider;
	
	//������\������J����
	private ArrayList<TableColumn> attributeColumns;
	
	//�N���X�I�����󂯎�郊�X�i
    private ISelectionListener nodeListener = new ISelectionListener() {
        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        
        	
            if (!(selection instanceof IStructuredSelection)) return;
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            List list = structuredSelection.toList(); //List<Object>�ɑ���
            if (list == null) return;
         
            //�\�����郁�\�b�h���X�g�𐶐� 
            MethodList methods = new MethodList();
            for (Object obj : list) {
            	if (obj instanceof TargetClass) {
            		TargetClass targetClass = (TargetClass)obj;
            		methods.addAll(targetClass.getMethods());
            	}
          	}
            //�����ɉ����ăJ�����X�V
            updateAttributeColumns(methods);
            
            //�\������f�[�^���Z�b�g
            viewer.setInput(methods);
            viewer.update(table, null);
            
            System.out.println("selectionChaged����");
            

            
        }
    };

	
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.MethodView";

	/**
	 * The constructor.
	 */
	public MethodView() {
		attributeColumns = new ArrayList<TableColumn>();
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		//�����Ƀr���[�̓��e������
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		table = viewer.getTable();	
		
		//�e�[�u���ݒ�
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(true);
		
		//��w�b�_�쐬
		//�N���X��
		TableColumn columnClassName = new TableColumn(table, SWT.NULL);
		columnClassName.setText("�N���X");
		columnClassName.setWidth(100);
		//���\�b�h��
		TableColumn columnName = new TableColumn(table, SWT.NULL);
		columnName.setText("���\�b�h��");
		columnName.setWidth(200);		
		//����
		TableColumn columnParameter = new TableColumn(table, SWT.NULL);
		columnParameter.setText("����");
		columnParameter.setWidth(100);
		//����
		//TODO
		for (int i=0; i< 3/*Activator.getConfig().getMethodViewAttributeColumnCount()*/; i++) {
			TableColumn columnAttribute = new TableColumn(table, SWT.NULL);
			columnAttribute.setWidth(100);
		}
		
		
		//���e�ƕ\���̐ݒ�
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new MethodLabelProvider());	

		//���X�i�o�^
		this.getSite().getPage().addSelectionListener(nodeListener);
				
		//�Z�����_�u���N���b�N�ŊY�����\�b�h�̃\�[�X�փW�����v
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("DoubleClick");
				//�Ώۃ��\�b�h���擾
				if (!(event.getSelection() instanceof IStructuredSelection)) return;
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if (!(sel.getFirstElement() instanceof Method)) return;
				Method method = (Method)sel.getFirstElement();
				//�\�[�X�R�[�h�փW�����v
				Activator.getScv().getWorkspaceManager().jumpSource(method);
			}
		});	
		
	}
	
	/**
	 * �j��
	 */
	public void dispose() {
		//���X�i�폜
		this.getSite().getPage().removeSelectionListener(nodeListener);
		super.dispose();
	}
		

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
	
	/**
	 * �e�[�u���̃J�������쐬����D
	 * @param table �ΏۂƂȂ�e�[�u���D
	 * @param methods ���\�b�h���D
	 */
	// TODO �J����������Ȃ�������쐬���C���܂����畝��0�ɂ��ĉB���Ƃ��D
	public void updateAttributeColumns(MethodList methods) {	
		//�������X�g�ɉ����ăJ�����𐶐�
		int i=0;
		for (TableColumn attributeColumn : viewer.getTable().getColumns()) {
			i++;
			int attributeIndex = i - 1 - this.FIX_COLUMN_COUNT;
			if (attributeIndex < 0) continue;
			if (methods.getAttributeTitles().size() <= attributeIndex) break;
			String title = methods.getAttributeTitles().get(attributeIndex);
			attributeColumn.setText(title);
			attributeColumn.setWidth(100);
		}
		table.update();
		
	}
	



}
