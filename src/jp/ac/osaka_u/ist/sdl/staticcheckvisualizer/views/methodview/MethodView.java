package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview;

import java.util.ArrayList;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

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
	
	/**
	 * ������ԂŐ������鑮���p�J�����̐�
	 */
	private final static int DEFAULT_ATTRIBUTE_COLUMN_COUNT = 4;
	
	//TableView���g�p����
	private TableViewer viewer;
	private Table table;
	
	
	//�N���X�I�����󂯎�郊�X�i
    private ISelectionListener nodeListener = new ISelectionListener() {
        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
     	
            if (!(selection instanceof IStructuredSelection)) return;
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            //List list = structuredSelection.toList(); //List<Object>�ɑ���
            //if (list == null) return;
            if (!(structuredSelection  instanceof TargetClassList)) return;
            
            TargetClassList targetClasses = (TargetClassList)structuredSelection ;
            
            //�\�����郁�\�b�h���X�g�𐶐� 
            MethodList methods = new MethodList();
            for (TargetClass targetClass : targetClasses) {
            		methods.addAll(targetClass.getMethods());
          	}
            //�����ɉ����ăJ�����X�V
            updateAttributeColumns(targetClasses.getAttributeTitles());
            
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
		columnClassName.setText("Class Name");
		columnClassName.setWidth(100);
		//���\�b�h��
		TableColumn columnName = new TableColumn(table, SWT.NULL);
		columnName.setText("Method Name");
		columnName.setWidth(200);		
		//����
		TableColumn columnParameter = new TableColumn(table, SWT.NULL);
		columnParameter.setText("Parameters");
		columnParameter.setWidth(100);
		//����
		//TODO
		for (int i=0; i< DEFAULT_ATTRIBUTE_COLUMN_COUNT; i++) {
			TableColumn columnAttribute = new TableColumn(table, SWT.NULL);
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
	 * �e�[�u���̃J�������X�V����D
	 * �J����������Ȃ�������쐬���C���܂����畝��0�ɂ��ĉB���D
	 * @param methods ���\�b�h���D
	 */
	public void updateAttributeColumns(ArrayList<String> attributeTitles) {		
		//�����J�������Ƒ����f�[�^�����r
		int attributeColumnCount = viewer.getTable().getColumnCount() - FIX_COLUMN_COUNT;
		int diff = attributeColumnCount - attributeTitles.size();
		
		if (diff > 0) {	// �J���������܂��Ă���
			//�g�p���Ȃ��J�����͕�0�ɂ��ĉB��
			for (int i=0; i<diff; i++) {
				int noUsedColumnIndex = viewer.getTable().getColumnCount() - 1 - i;
				TableColumn noUsedColumn = viewer.getTable().getColumn(noUsedColumnIndex);
				noUsedColumn.setText("");
				noUsedColumn.setWidth(0);
				noUsedColumn.setResizable(false);
				noUsedColumn.setMoveable(false);
			}
		} else if (diff < 0) { // �J����������Ȃ�
			//����Ȃ��J�����𐶐�����
			for (int i=diff; i <0; i++) {
				int newColumnIndex = viewer.getTable().getColumnCount() - 1 + i;
				TableColumn attributeColumn = new TableColumn(table, SWT.NULL, newColumnIndex);
			}
			viewer.getTable().update();
		}
				
		//�������X�g�ɉ����ăJ�����^�C�g����ݒ�
		int i=0;
		for (TableColumn attributeColumn : viewer.getTable().getColumns()) {
			i++;
			int attributeIndex = i - 1 - FIX_COLUMN_COUNT;
			if (attributeIndex < 0) continue;			
			//�񂪑����̏ꍇ
			if (attributeTitles.size() <= attributeIndex) break;
			String title = attributeTitles.get(attributeIndex);
			attributeColumn.setText(title);
			attributeColumn.setWidth(100);
			attributeColumn.setResizable(true);
			attributeColumn.setMoveable(true);
		}
		viewer.getTable().update();	
	}
	

	/**
	 * �e�[�u���J�����̃C���f�b�N�X�𑮐��C���f�b�N�X�ɕϊ�����D
	 * @param columnIndex 
	 * @return �Ή����鑮���C���f�b�N�X�D���݂��Ȃ����null�D
	 */
	public Integer columnIndexToAttributeIndex(int columnIndex, ArrayList<String> attributeTitles) {
		int attributeIndex = columnIndex - FIX_COLUMN_COUNT;
		if (attributeIndex < 0) return null;
		if (attributeIndex >= attributeTitles.size()) return null;
		return attributeIndex;
	}


}
