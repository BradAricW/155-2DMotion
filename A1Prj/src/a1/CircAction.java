package a1;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class CircAction extends AbstractAction {
	
	public CircAction() {
		
		putValue(NAME, "Move Circularly");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Starter.circSet();
		
	}

}
