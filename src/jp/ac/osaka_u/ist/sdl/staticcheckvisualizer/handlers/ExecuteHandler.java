package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.handlers;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ExecuteHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//静的チェックを実行する．
		Activator.getScv().execute();
		
		return null;
	}
}
