package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class NewTodoHandler {
  @Execute
  public void execute(Composite parent) {
	  Label label = new Label(parent, SWT.NONE);
	  label.setText("Inserindo um texto");
	  }
} 
