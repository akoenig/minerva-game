package de.hochschule.bremen.minerva.client.ui.gui.panels.subpanels;

import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.client.ui.gui.panels.Background;

public class GameWaitingPanel extends JLayeredPane {

	private Background background;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8613726084601031573L;
	
	public GameWaitingPanel() {
		this.setOpaque(true);
		
		//background
		this.background = new Background(this.getClass());
		this.background.setBounds(0, 0, 500, 500);
		
		this.add(this.background,-30000);
	}

}
