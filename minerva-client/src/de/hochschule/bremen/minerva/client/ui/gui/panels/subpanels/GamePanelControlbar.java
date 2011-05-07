/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: GamePanelControlbar.java 825 2010-07-06 21:05:00Z andre.koenig $
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Christian Bollmann: cbollmann@stud.hs-bremen.de
 *     Carina Strempel: cstrempel@stud.hs-bremen.de
 *     André König: akoenig@stud.hs-bremen.de
 * 
 * Web:
 *     http://minerva.idira.de
 * 
 */

package de.hochschule.bremen.minerva.client.ui.gui.panels.subpanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import de.hochschule.bremen.minerva.client.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MControl;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MPlayerIcon;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MSlidePanel;
import de.hochschule.bremen.minerva.client.ui.gui.panels.GamePanel;
import de.hochschule.bremen.minerva.client.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.vo.CavalerieCard;
import de.hochschule.bremen.minerva.commons.vo.CountryCard;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.PlayerState;
import de.hochschule.bremen.minerva.commons.vo.SoldierCard;

/**
 * Panel sliced in halves. The upper half contains current player, allocatable
 * army count, game state buttons (allocate, turn cards in, attack, move, end
 * turn) and a slide button.
 * 
 * The lower half contains game informations country cards ...
 * 
 * @version $Id: GamePanelControlbar.java 825 2010-07-06 21:05:00Z andre.koenig $
 * @since 1.0
 * 
 */
public class GamePanelControlbar extends JPanel implements ActionListener, MControl, TextResources {

	private int relativeHeight;
	private MSlidePanel slidePanel;
	private GamePanel gamePanel;

	// upper half
	private JButton allocateButton;
	private JLabel allocatableArmies;
	private JButton cardButton;
	private JButton attackButton;
	private JButton moveButton;
	private JButton finishTurnButton;

	// lower half
	private JList cardList;
	private DefaultListModel model;
	private JButton buttonTurnIn;

	// slide
	private JButton slideButton;
	private JPanel upperHalf;
	private JPanel lowerHalf;

	private JPanel currentPlayerArea;

	private static final long serialVersionUID = 2420450895831821731L;

	/**
	 * Constructor
	 * 
	 */
	public GamePanelControlbar() {
		this.init();
	}

	/**
	 * Initializes ControlBarPanel.
	 * 
	 */
	private void init() {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		this.initUpperHalf();
		this.initLowerHalf();

		this.add(this.upperHalf, BorderLayout.NORTH);
		this.add(this.lowerHalf, BorderLayout.SOUTH);

		this.setPreferredSize(new Dimension(1000, 220));

		this.updateUI();
	}

	/**
	 * Initialization of the UPPER half of the control bar
	 * 
	 */
	private void initUpperHalf() {
		// upper half
		this.upperHalf = new JPanel();
		this.upperHalf.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		this.upperHalf.setOpaque(false);

		this.currentPlayerArea = new JPanel();
		this.currentPlayerArea.setOpaque(false);

		this.allocateButton = new JButton(
				GAME_PANEL_CONTROLBAR_BUTTON_SET_ARMIES);
		this.allocatableArmies = new JLabel("0");
		this.allocatableArmies.setForeground(new Color(187, 186, 187));

		this.cardButton = new JButton(
				GAME_PANEL_CONTROLBAR_BUTTON_TURN_CARDS_IN);
		this.attackButton = new JButton(GAME_PANEL_CONTROLBAR_BUTTON_ATTACK);
		this.moveButton = new JButton(
				GAME_PANEL_CONTROLBAR_BUTTON_ALLOCATE_ARMIES);
		this.finishTurnButton = new JButton(
				GAME_PANEL_CONTROLBAR_BUTTON_FINISH_TURN);

		this.upperHalf.add(this.currentPlayerArea);

		this.upperHalf.add(this.cardButton);
		this.upperHalf.add(this.allocateButton);
		this.upperHalf.add(this.allocatableArmies);
		this.upperHalf.add(this.attackButton);
		this.upperHalf.add(this.moveButton);
		this.upperHalf.add(this.finishTurnButton);
	}

	/**
	 * Initialization of the LOWER half of the control bar
	 * 
	 */
	private void initLowerHalf() {
		// lower half
		this.lowerHalf = new JPanel();
		this.lowerHalf.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.lowerHalf.setOpaque(false);

		// constructing all objects
		this.model = new DefaultListModel();
		this.cardList = new JList(this.model);
		this.buttonTurnIn = new JButton(
				GAME_PANEL_CONTROLBAR_BUTTON_TURN_CARDS_IN);

		// card list
		this.cardList.setVisibleRowCount(5);
		DefaultListSelectionModel selection = new DefaultListSelectionModel();
		selection
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		this.cardList.setOpaque(false);
		this.cardList.setSelectionModel(selection);
		this.cardList.setPreferredSize(new Dimension(250, 125));
		this.cardList.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(187, 186, 187)),
				GAME_PANEL_CARDS, 0, 0, FONT, Color.LIGHT_GRAY));
		this.cardList.setFont(FONT);
		this.cardList.setForeground(new Color(139, 140, 142));

		this.lowerHalf.add(this.cardList);
		this.lowerHalf.add(this.buttonTurnIn);
	}

	/**
	 * Sets slide panel if this panel is inside one. This method is
	 * automatically used if the control bar is deployed in a slide panel.
	 * 
	 * @param slidePanel
	 *            the slide panel
	 * 
	 */
	public void setSlidePanel(MSlidePanel slidePanel) {
		this.slidePanel = slidePanel;
		this.relativeHeight = (int) lowerHalf.getPreferredSize().getHeight();

		this.slideButton = new JButton(GAME_PANEL_CONTROLBAR_BUTTON_SLIDE);
		this.upperHalf.add(this.slideButton);
		this.addSlideListeners();
	}

	/**
	 * Adds action listeners to the slide button if control bar is inside a
	 * slide panel
	 * 
	 */
	private void addSlideListeners() {
		this.slideButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GamePanelControlbar.this.slidePanel.isMinimized()) {
					GamePanelControlbar.this.slidePanel.slideUp();
				} else {
					GamePanelControlbar.this.slidePanel.slideDown();
				}
			}
		});
	}

	/**
	 * Gets relative height of the control bar.
	 * 
	 * @return relative height
	 * 
	 */
	public int getRelativeHeight() {
		return this.relativeHeight;
	}

	/**
	 * Sets the label for the current player
	 * 
	 * @param currentPlayer
	 *            name of current player
	 * 
	 */
	public void setCurrentPlayerLabel(Player currentPlayer) {
		if (this.currentPlayerArea.getComponentCount() == 0) {
			this.currentPlayerArea.add(new MPlayerIcon(currentPlayer));
		} else {
			((MPlayerIcon)this.currentPlayerArea.getComponent(0)).updatePlayerIcon(currentPlayer);
		}
	}

	/**
	 * Update the card list of the current player to the given one.
	 * 
	 * @param cards
	 *            card list
	 * 
	 */
	public void updateCardList(Vector<CountryCard> cards) {
		this.model.clear();
		for (CountryCard card : cards) {
			String text;
			if (card instanceof SoldierCard) {
				text = GAME_PANEL_CONTROLBAR_CARD_SYMBOL_SOLDIER
						+ card.getReference().getName();
			} else if (card instanceof CavalerieCard) {
				text = GAME_PANEL_CONTROLBAR_CARD_SYMBOL_CAVALERIE
						+ card.getReference().getName();
			} else {
				text = GAME_PANEL_CONTROLBAR_CARD_SYMBOL_CANON
						+ card.getReference().getName();
			}
			model.addElement(text);
		}
	}

	/**
	 * Adds listeners to all Buttons and sets the game panel needed for their
	 * states.
	 * 
	 * @param gamePanel
	 *            game panel
	 * 
	 */
	public void addListeners(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.finishTurnButton.addActionListener(this);
		this.allocateButton.addActionListener(this);
		this.cardButton.addActionListener(this);
		this.attackButton.addActionListener(this);
		this.moveButton.addActionListener(this);
		this.buttonTurnIn.addActionListener(this);
	}

	/**
	 * Sets text of the label for allocatable armies.
	 * 
	 * @param allocatableArmies
	 *            count as string
	 * 
	 */
	public void setAllocatableArmiesLabel(String allocatableArmies) {
		this.allocatableArmies.setText(" " + allocatableArmies + " ");
	}

	/**
	 * Updates the state of all buttons
	 * 
	 */
	public void updateButtons() {
		if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.RELEASE_CARDS) {
			this.allocateButton.setEnabled(true);
			this.cardButton.setEnabled(true);
			this.attackButton.setEnabled(true);
			this.moveButton.setEnabled(true);
			this.buttonTurnIn.setEnabled(true);
			this.finishTurnButton.setEnabled(true);
		} else if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.ALLOCATE_ARMIES) {
			this.allocateButton.setEnabled(true);
			this.cardButton.setEnabled(false);
			this.attackButton.setEnabled(true);
			this.moveButton.setEnabled(true);
			this.buttonTurnIn.setEnabled(false);
			this.finishTurnButton.setEnabled(true);
		} else if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.ATTACK) {
			this.allocateButton.setEnabled(false);
			this.cardButton.setEnabled(false);
			this.attackButton.setEnabled(true);
			this.moveButton.setEnabled(true);
			this.buttonTurnIn.setEnabled(false);
			this.finishTurnButton.setEnabled(true);
		} else if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.MOVE) {
			this.allocateButton.setEnabled(false);
			this.cardButton.setEnabled(false);
			this.attackButton.setEnabled(false);
			this.moveButton.setEnabled(true);
			this.buttonTurnIn.setEnabled(false);
			this.finishTurnButton.setEnabled(true);
		} else if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.IDLE) {
			this.allocateButton.setEnabled(false);
			this.cardButton.setEnabled(false);
			this.attackButton.setEnabled(false);
			this.moveButton.setEnabled(false);
			this.buttonTurnIn.setEnabled(false);
			this.finishTurnButton.setEnabled(false);
		}
	}

	/**
	 * Buttons actions of this panel
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == this.finishTurnButton) {
				
					MinervaGUI.getEngine().finishTurn();
				
				this.gamePanel.unmarkAll();
			} else if (e.getSource() == this.allocateButton) {
				MinervaGUI.getEngine().setCurrentPlayerState(
						PlayerState.ALLOCATE_ARMIES);
				this.gamePanel.unmarkAll();
			} else if (e.getSource() == this.cardButton) {
				MinervaGUI.getEngine().setCurrentPlayerState(
						PlayerState.RELEASE_CARDS);
				this.gamePanel.unmarkAll();
			} else if (e.getSource() == this.attackButton) {
				MinervaGUI.getEngine().setCurrentPlayerState(
						PlayerState.ATTACK);
				this.gamePanel.unmarkAll();
			} else if (e.getSource() == this.moveButton) {
				MinervaGUI.getEngine().setCurrentPlayerState(
						PlayerState.MOVE);
				this.gamePanel.unmarkAll();
			} else if (e.getSource() == this.buttonTurnIn) {
	
				if (this.cardList.getSelectedIndices().length == 1) {
					this.gamePanel.TurnCardIn(MinervaGUI.getEngine().getClientPlayer()
							.getCountryCards()
							.get(this.cardList.getSelectedIndex()));
				} else if (this.cardList.getSelectedIndices().length == 3) {
					Vector<CountryCard> series = new Vector<CountryCard>();
					for (int i = 0; i < 3; i++) {
						series.add(MinervaGUI.getEngine().getClientPlayer()
								.getCountryCards()
								.get(this.cardList.getSelectedIndices()[i]));
					}
					this.gamePanel.TurnSeriesIn(series);
				} else {
					MMessageBox.error(GAME_PANEL_CARDS_SELECT_CARD);
				}
			}
		} catch (DataAccessException e1) {
			MMessageBox.error(e1);
		}
		this.gamePanel.updatePanel();
	}
}