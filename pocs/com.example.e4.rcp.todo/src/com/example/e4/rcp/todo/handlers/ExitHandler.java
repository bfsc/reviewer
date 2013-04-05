package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.core.di.annotations.Execute;

public class ExitHandler {
  @Execute
  public void execute(IWorkbench workbench) {
	  workbench.close();
  }
}