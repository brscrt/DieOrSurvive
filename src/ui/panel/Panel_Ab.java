package ui.panel;

import java.awt.EventQueue;

import javax.swing.JPanel;

public abstract class Panel_Ab extends JPanel implements Panel_IO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6526356203980402337L;

	@Override
	public void invoke() {
		EventQueue.invokeLater(() -> {
			display();
		});

	}

}
