package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.views.methodview;

import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.Method;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.MethodList;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClass;
import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.model.TargetClassList;
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
	
	//TableView���g�p����
	private TableViewer viewer;
	
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
            //TODO �����ɉ����ăJ�����X�V
            //updateColumns(viewer.getTable(), targetClass.getMethods());
            //TODO �����^�C�g�����Z�b�g
        	ArrayList<String> titles = new ArrayList<String>();
        	titles.add("passed/failed");
            methodLabelProvider.setAttributeTitles(titles);
            //�\������f�[�^���Z�b�g          
//          ViewMethodList viewMethodList = new ViewMethodList();
//          for (Object obj : list) {
//          	System.out.println("obj="+obj.getClass());
//            	if (obj instanceof TargetClass) {
//            		TargetClass targetClass = (TargetClass)obj;
//            		viewMethodList.addAll(targetClass.getSimpleName(), targetClass.getFullQualifiedName(), targetClass.getMethods());
//            	}
//          }
//          viewer.setInput(viewMethodList);
            MethodList methods = new MethodList();
            for (Object obj : list) {
            	System.out.println("obj="+obj.getClass());
            	if (obj instanceof TargetClass) {
            		TargetClass targetClass = (TargetClass)obj;
            		methods.addAll(targetClass.getMethods());
            	}
          	}
            viewer.setInput(methods);
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
		
		//�e�[�u���擾
		Table table = viewer.getTable();
		//�e�[�u���ݒ�
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
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
		//TODO passed/failed
		TableColumn passedParameter = new TableColumn(table, SWT.NULL);
		passedParameter.setText("passed/failed");
		passedParameter.setWidth(100);
	
		//���e�ƕ\���̐ݒ�
		viewer.setContentProvider(new ArrayContentProvider());
		methodLabelProvider = new MethodLabelProvider();
		viewer.setLabelProvider(methodLabelProvider);	

		//���X�i�o�^
		this.getSite().getPage().addSelectionListener(nodeListener);
				
		//TODO �Z�����_�u���N���b�N�ŊY�����\�b�h�̃\�[�X�փW�����v
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
				WorkspaceManager.jumpSource(method);
			}
		});	
		
	}
	
	/**
	 * �j��
	 */
	public void dispose() {
		//���X�i�폜
		this.getSite().getPage().removeSelectionListener(nodeListener);
	}
		

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
	
	/**
	 * �����^�C�g���ɑΉ�����悤�Ƀe�[�u���̃J�������X�V����B
	 * @param table �ΏۂƂȂ�e�[�u���B
	 * @param methods ���\�b�h���B
	 */
	public void updateColumns(Table table, MethodList methods) {
		System.out.println("createColumns titles=" + methods.getAttributeTitles());
		//���݂��鑮���J���������ׂč폜
		for (TableColumn column : attributeColumns) {
			column.dispose();
		}
		attributeColumns.clear();
		//�������X�g�ɉ����ăJ�����𐶐�
		for (String title : methods.getAttributeTitles()) {
			TableColumn attributeColumn = new TableColumn(table, SWT.NULL);
			attributeColumn.setText(title);
			attributeColumns.add(attributeColumn);
		}
	}
	
//	private List<Method> getItems() {
//		MethodList methodList = new MethodList();
//		
//		methodList.add(new Method("method1","int"));
//		methodList.add(new Method("method2","int,int"));
//		methodList.add(new Method("method3","int,boolean"));
//		
//		return methodList;
//	}
	



}