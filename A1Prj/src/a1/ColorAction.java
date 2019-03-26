package a1;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ColorAction extends AbstractAction {
	
	public ColorAction() {
		putValue(NAME, "Change Color");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Starter.colSet();
	}

}
