package a1;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class HorizAction extends AbstractAction {

	public HorizAction() {
		
		putValue(NAME, "Move Horizontally");
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Starter.horizSet();
	}
	
}