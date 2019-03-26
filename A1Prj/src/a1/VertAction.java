package a1;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class VertAction extends AbstractAction {

	public VertAction() {
		
		putValue(NAME, "Move Vertically");
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Starter.vertSet();
		
	}
	
}
