/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: GamePanel.java 839 2010-08-07 19:50:36Z andre.koenig $
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

package de.hochschule.bremen.minerva.client.ui.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import de.hochschule.bremen.minerva.client.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MArmyCountIcon;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MControl;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MSlidePanel;
import de.hochschule.bremen.minerva.client.ui.gui.listener.MMouseListener;
import de.hochschule.bremen.minerva.client.ui.gui.panels.subpanels.GamePanelControlbar;
import de.hochschule.bremen.minerva.client.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.commons.core.GameEngine;
import de.hochschule.bremen.minerva.commons.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.commons.exceptions.CountryOwnerException;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.commons.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.commons.util.ColorTool;
import de.hochschule.bremen.minerva.commons.util.Die;
import de.hochschule.bremen.minerva.commons.util.MapTool;
import de.hochschule.bremen.minerva.commons.vo.AttackResult;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.CountryCard;
import de.hochschule.bremen.minerva.commons.vo.Mission;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.PlayerState;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.commons.vo.World;

/**
 * Prototype of the actual game screen.
 * 
 * @version $Id: GamePanel.java 839 2010-08-07 19:50:36Z andre.koenig $
 * @since 1.0
 *
 */
public class GamePanel extends JLayeredPane implements MControl, TextResources, Observer {

	public MapPanel mapOverlay;
	public MapPanel mapUnderlay;
	public MSlidePanel slidePanel;
	public JLabel missionLabel;

	public GameEngine engine = MinervaGUI.getEngine();
	
	public HashMap<Country, MArmyCountIcon> armyIcons = new HashMap<Country, MArmyCountIcon>();

	private Player currentPlayer;

	private World world = null;
	private Vector<Player> players = null;
	
	private Country source = null;
	private Country destination = null;

	private static final long serialVersionUID = -2906065533734117968L;
	
	/**
	 * Constructor initializing screen.
	 *
	 */
	public GamePanel(World world) {
		this.world = world;
		
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);

		BufferedImage mapImage = null;
		
		// the mission panel
		JPanel missionPanel = new JPanel();
		missionPanel.setBounds(0, 0, 1000, 30);
		missionPanel.setLayout(new MigLayout());
		missionPanel.setOpaque(false);
		missionPanel.setBorder(BorderFactory.createMatteBorder (0, 0, 1, 0, new Color(71, 73, 75)));
		this.add(missionPanel, 10000);

		JLabel yourMissionLabel = new JLabel();

		Player clientPlayer = MinervaGUI.getEngine().getClientPlayer();
		yourMissionLabel.setText(GAME_PANEL_YOUR_MISSION.replace("{player}", clientPlayer.getFirstName()));

		yourMissionLabel.setFont(new Font(FONT.getFamily(), Font.BOLD, 12));
		yourMissionLabel.setForeground(new Color(1, 174, 253));
		missionPanel.add(yourMissionLabel);

		//shows mission of current player (or client player if not GameEngineLocal)
		this.missionLabel = new JLabel();
		this.missionLabel.setFont(new Font(FONT.getFamily(), Font.ROMAN_BASELINE, 12));
		this.missionLabel.setForeground(new Color(186, 187, 188));
		
		//refreshing mission text
		//this happens only in GameEngineLocal, otherwise current player doesn't change
		try {
			searchPlayerMission : for (Mission mission : this.engine.getGameMissions()) {
				if (mission.getOwner().getId() == this.engine.getClientPlayer().getId()) {
					this.missionLabel.setText(mission.getTitle());
					break searchPlayerMission;
				}
			}
			
			missionPanel.add(missionLabel);

			//lower map
			mapImage = this.engine.getGameMapUnderlayImage();
			this.mapUnderlay = new MapPanel(mapImage);
			this.mapUnderlay.setBounds(0,0,500,500);
			
			//upper map
			mapImage = this.engine.getGameMapImage();
			this.mapOverlay = new MapPanel(mapImage);
			this.mapOverlay.setBounds(0,0,500,500);
			
		} catch (DataAccessException e) {
			MMessageBox.error(e);
		} 
		
		
		//control bar
		this.slidePanel = new MSlidePanel(new GamePanelControlbar());
		slidePanel.setBounds(0, this.getPreferredSize().height - slidePanel.getRelativeHeight(), slidePanel.getPreferredSize().width,slidePanel.getPreferredSize().height);
		
		//adds mouse listener to the upper map
		this.addMapListener();
		
		// get the world from the server.
		HashMap<Country, Point> countryAnchors = MapTool.getCountryAnchors(this.mapOverlay.getMapImage(), mapUnderlay.getMapImage(), this.world);

		this.add(slidePanel, 10000);

		for (Country country : this.world.getCountries()) {
			MArmyCountIcon aci = new MArmyCountIcon(Color.RED, countryAnchors.get(country));
			aci.addMouseListener(new MMouseListener() {
				public void mouseClicked(MouseEvent e) {
					GamePanel.this.unmarkAll();
					Color color = ColorTool.fromInteger(GamePanel.this.mapUnderlay.getMapImage().getRGB(((MArmyCountIcon)e.getSource()).getX()+15, ((MArmyCountIcon)e.getSource()).getY()+15));
					Country country = GamePanel.this.world.getCountry(color);
					GamePanel.this.mapInteraction(country);
				}
			});
			this.armyIcons.put(country,aci);
			this.add(aci,-10000);
		}
			
		this.refreshArmyCounts();

		//Adding everything up
		this.add(this.mapOverlay,-20000);
		this.add(this.mapUnderlay,-30000);

		slidePanel.getControlBar().addListeners(this);

		this.validate();
		this.updateUI();
		this.updatePanel();
		MinervaGUI.getEngine().addObserver(this);
	}

	/**
	 * Adds the action listener on the upper map to interact with mouse clicks
	 *
	 */
	private void addMapListener() {
		this.mapOverlay.addMouseListener(new MMouseListener() {
			public void mouseClicked(MouseEvent e) {
				GamePanel.this.unmarkAll();

				Color color = ColorTool.fromInteger(GamePanel.this.mapUnderlay.getMapImage().getRGB(e.getX(), e.getY()));
				Country country = GamePanel.this.world.getCountry(color);

				if (country.getId() != ValueObject.getDefaultId()) {
					GamePanel.this.mapInteraction(country);
				}
			}
		});
	}

	/**
	 * Allocates one army on a destined country
	 *
	 * @param country country to put an army on
	 *
	 */
	private void allocate(Country country) {
		int armyCount = 0;
		try {
			this.engine.allocateArmy(country);
			armyCount = this.engine.getAllocatableArmyCount();
		} catch (NotEnoughArmiesException e) {
			MMessageBox.error(e.getMessage());
		} catch (CountryOwnerException e) {
			if (country.getName() != null) {
				MMessageBox.error(e.getMessage());
			}
		} catch (DataAccessException e) {
			MMessageBox.error(e);
		}
		if (armyCount == 0) {
			this.engine.getClientPlayer().setState(PlayerState.ATTACK);
		}
		this.updatePanel();
	}

	/**
	 * Releases a single card
	 *
	 * @param card Card
	 *
	 */
	public void TurnCardIn(CountryCard card) {
		if (this.currentPlayer.hasCountry(card.getReference())) {
			try {
				this.engine.releaseCard(card);
			} catch (DataAccessException e) {
				MMessageBox.error(e);
			}
			GamePanel.this.updatePanel();
		} else {
			GamePanel.this.updatePanel();
			MMessageBox.error(GAME_CARD_RELEASE_ERROR_COUNTRY_UNCONQUERED);
		}
	}

	/**
	 * Releases a card series
	 *
	 * @param series Vector of cards
	 *
	 */
	public void TurnSeriesIn(Vector<CountryCard> series) {
		try {
			this.engine.releaseCards(series);
		} catch (DataAccessException e) {
			MMessageBox.error(e);
		}
		GamePanel.this.updatePanel();
	}

	/**
	 * Attack one country from another.
	 * Use it once to set the sources country and twice to set destination country.
	 * After setting the destination you'll get an option pane to the army count.
	 *
	 * @param country first source then destination
	 *
	 */
	private void attack(Country country) {
		if (country.getId() > -1) {
			if (this.source == null) {
				//setting source country
				if (country.getArmyCount() > 1) {
					this.source = country;
					this.realArmyIconGetter(this.source).mark(Color.GREEN);
					for (Country c : GamePanel.this.world.getNeighbours(this.source)) {
						if (!this.engine.getClientPlayer().hasCountry(c)) {
							this.realArmyIconGetter(c).mark(Color.RED);
						}
					}
				}
				GamePanel.this.updatePanel();
			} else {
				this.destination = country;
				if (!(this.destination == this.source)) {
					//setting destination country
					
					this.realArmyIconGetter(country).mark(Color.YELLOW);
					
					this.realArmyIconGetter(this.source).mark(Color.YELLOW);
					this.updatePanel();
					try {
						//army count input
						int wert = Integer.parseInt(JOptionPane.showInputDialog("Wieviele Armeen " +
								"sollen angreifen? (max: "+this.calcMaxAttackCount(this.source)+")",
								""+(this.calcMaxAttackCount(this.source))));
						
						try {
							//actually attack and showing attack result afterwards
							AttackResult ar = this.engine.attack(this.source, this.destination, wert);
							GamePanel.this.updatePanel();
							if ((ar != null) && (!this.engine.isGameFinished())) {
								this.showAttackResult(ar);
							}
						} catch (CountriesNotInRelationException e) {
							GamePanel.this.updatePanel();
							MMessageBox.error(e.getMessage());
						} catch (NotEnoughArmiesException e) {
							GamePanel.this.updatePanel();
							MMessageBox.error(e.getMessage());
						} catch (IsOwnCountryException e) {
							GamePanel.this.updatePanel();
							MMessageBox.error(e.getMessage());
						} catch (DataAccessException e) {
							MMessageBox.error(e);
						}
					} catch (NumberFormatException e1) {
						GamePanel.this.updatePanel();
						//no need, just to make sure that the method doesn't end
					}
				
					this.source = null;
					this.destination = null;
				} else {
					this.unmarkAll();
					this.source = null;
					this.destination = null;
					GamePanel.this.updatePanel();
					MMessageBox.error(GAME_ATTACK_ERROR_SAME_COUNTRY);
				}
			}	
		} else {
			if (this.source != null) {
				this.realArmyIconGetter(country).mark(Color.GREEN);
				for (Country c : GamePanel.this.world.getNeighbours(this.source)) {
					if (!this.engine.getClientPlayer().hasCountry(c)) {
						this.realArmyIconGetter(c).mark(Color.RED);
					}
				}
			}
			this.updatePanel();
		}
	}

	/**
	 * Converts AttackResult into dialog box message
	 *
	 * @param attackResult AttackResult
	 *
	 */
	private void showAttackResult(AttackResult attackResult) {
		StringBuilder message = new StringBuilder();
		message.append(attackResult.getAttacker().getUsername()).append(" (Würfel: ");

		for (Die die : attackResult.getAttackerDice()) {
			message.append(die.getRollResult()).append(" ");
		}

		message.append(") hat ").append(attackResult.getDefender().getUsername()).append(" (Würfel: ");

		for (Die die : attackResult.getDefenderDice()) {
			message.append(die.getRollResult()).append(" ");
		}

		message.append(") angegriffen.\n").append(attackResult.getAttacker().getUsername()).append(" hat ");
		message.append(attackResult.getLostAttackerArmies()).append(" und ").append(attackResult.getDefender().getUsername());
		message.append(" hat ").append(attackResult.getLostDefenderArmies()).append(" Armeen verloren.\n");
		message.append(attackResult.getAttacker().getUsername()).append(" hat das Land ");
		message.append((attackResult.isWin() ? "erobert." : "NICHT erobert."));

		MMessageBox.show(message.toString());
	}

	/**
	 * Moves units from one to another country.
	 * Use it once to set the sources country and twice to set destination country.
	 * After setting the destination you'll get an option pane to the army count.
	 *
	 * @param country first source then destination
	 *
	 */
	private void move(Country country) {
		if (country.getId() > -1) {
			if (this.source == null) {
				//setting source country
				if (country.getArmyCount() > 1) {
					this.source = country;
					this.realArmyIconGetter(this.source).mark(Color.GREEN);
					for (Country c : GamePanel.this.world.getNeighbours(this.source)) {
						if (this.engine.getClientPlayer().hasCountry(c)) {
							this.realArmyIconGetter(c).mark(Color.RED);
						}
					}
				}
				GamePanel.this.updatePanel();
			} else {
				//setting destination country
				this.destination = country;
				if (!(this.destination == this.source)) {
					this.realArmyIconGetter(country).mark(Color.YELLOW);
					
					this.realArmyIconGetter(this.source).mark(Color.YELLOW);
					this.updatePanel();
					try {
						//army count input
						int wert = Integer.parseInt(JOptionPane.showInputDialog("Wieviele Armeen " +
								"sollen bewegt werden? (max: "+(this.source.getArmyCount()-1)+")",
								""+(this.source.getArmyCount()-1)));
						try {
							//actually move
							this.engine.move(this.source, this.destination, wert);
							GamePanel.this.updatePanel();
						} catch (CountriesNotInRelationException e) {
							GamePanel.this.updatePanel();
							MMessageBox.error(e.getMessage());
						} catch (NotEnoughArmiesException e) {
							GamePanel.this.updatePanel();
							MMessageBox.error(e.getMessage());
						} catch (CountryOwnerException e) {
							GamePanel.this.updatePanel();
							MMessageBox.error(e.getMessage());
						} catch (DataAccessException e) {
							MMessageBox.error(e);
						}
					} catch (NumberFormatException e1) {
						GamePanel.this.updatePanel();
						//no need, just to make sure that the methods doesn't end
					}
					
					this.source = null;
					this.destination = null;
				} else {
					this.unmarkAll();
					this.source = null;
					this.destination = null;
					GamePanel.this.updatePanel();
					MMessageBox.error(GAME_MOVE_ERROR_SAME_COUNTRY);
				}
				
			}
		} else {
			if (this.source != null) {
				this.realArmyIconGetter(this.source).mark(Color.GREEN);
				for (Country c : GamePanel.this.world.getNeighbours(this.source)) {
					if (this.engine.getClientPlayer().hasCountry(c)) {
						this.realArmyIconGetter(c).mark(Color.RED);
					}
				}
			}
			this.updatePanel();
		}
		
	}

	/**
	 * Updates whole panel, when something changed during the game
	 *
	 */
	public void updatePanel() {
		try {
			//game finished?
			if (this.engine.isGameFinished()) {
				MinervaGUI.getEngine().deleteObserver(this);
				
				MinervaGUI.getInstance().changePanel(new LoginPanel());

				MMessageBox.show(GAME_FINISHED_ANNOUCEMENT+"\n" + this.engine.getGameWinner().getUsername()
						+GAME_FINISHED_WINNER);
			} else {
				this.world = this.engine.getGameWorld();
				this.players = this.engine.getGamePlayers();
				
				for (Player player : this.players) {
					if (player.getState() != PlayerState.IDLE)
					this.currentPlayer = player;
				}
	
				//source and destination will be reset when player is in wrong state
				if ((this.engine.getClientPlayer().getState() == PlayerState.RELEASE_CARDS) || 
						(this.engine.getClientPlayer().getState() == PlayerState.ALLOCATE_ARMIES)) {
					this.unmarkAll();
					this.source = null;
					this.destination = null;
				}
	
				//if current player has no cards, card release will be skipped
				if ((this.engine.getClientPlayer().getState() == PlayerState.RELEASE_CARDS) && (this.engine.getClientPlayer().getCountryCards().isEmpty())) {
					this.engine.setCurrentPlayerState(PlayerState.ALLOCATE_ARMIES);
				}		
		
				//refreshing army count icons
				this.refreshArmyCounts();
	
				//refreshing control bar
				this.slidePanel.getControlBar().updateButtons();
				this.slidePanel.getControlBar().setCurrentPlayerLabel(this.currentPlayer);
				this.slidePanel.getControlBar().setAllocatableArmiesLabel(" "+this.engine.getAllocatableArmyCount()+" ");
				this.slidePanel.getControlBar().updateCardList(this.currentPlayer.getCountryCards());
		
				this.repaint();
				this.updateUI();
			}
		} catch (DataAccessException e) {
			MMessageBox.error(e);
		}
	}
	
	/**
	 * Repaints all ArmyCountIcons with correct parameters
	 *
	 */
	@SuppressWarnings("unchecked")
	public void refreshArmyCounts() {
		Iterator<?> iter = this.armyIcons.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry)iter.next();
			for (Country country : this.world.getCountries()) {
				if (country.getId() == ((Country)pairs.getKey()).getId()) {
					((MArmyCountIcon)pairs.getValue()).setPlayer(country, this.getPlayer(country));
				}	
			}
		}
	}

	/**
	 * Removes markings of all countries.
	 *
	 */
	@SuppressWarnings("unchecked")
	public void unmarkAll() {
		Iterator<?> iter = this.armyIcons.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry)iter.next();
			((MArmyCountIcon)pairs.getValue()).unmark();
		}
	}

	/**
	 * Gets the current player.
	 *
	 * @return current player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Sets the current player.
	 *
	 * @param currentPlayer
	 *
	 */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * Gets a player by given country.
	 *
	 * @param byCountry Country
	 * @return Player of the given country
	 *
	 */
	public Player getPlayer(Country byCountry) {
		if (this.players == null) {
			try {
				this.players = this.engine.getGamePlayers();
			} catch (DataAccessException e) {
				MMessageBox.error(e);
			}
		}
		for (Player player : this.players) {
			if (player.hasCountry(byCountry)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Calculates maximum number of armies which can attack from the selected country.
	 * 
	 * @param country Country where to calculate the maximum number to attack with.
	 * @return Maximum number of armies to attack with.
	 *
	 */
	public int calcMaxAttackCount(Country country) {
		int armyCount = country.getArmyCount();

		if (armyCount > 3) {
			return 3;
		} else {
			return armyCount-1;
		}
	}

	/**
	 * Interaction with the map when clicking on it.
	 *
	 * @param country country clicked on
	 *
	 */
	private void mapInteraction(Country country) {
		if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.ALLOCATE_ARMIES) {
			this.allocate(country);
		} else if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.RELEASE_CARDS) {
			this.updatePanel();
			/*
			 * Nothing will happen here.
			 * You can't interact with the map, when you're trying to
			 * release cards.
			 */
		} else if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.ATTACK) {
			this.attack(country);
		} else if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.MOVE) {
			this.move(country);
		} else if (MinervaGUI.getEngine().getClientPlayer().getState() == PlayerState.IDLE) {
			this.updatePanel();
		}
	}
	
	/**
	 * Ugly getter due to problems with references.
	 * Client references and server references aren't the same.
	 *
	 * @param country 
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	private MArmyCountIcon realArmyIconGetter(Country country) {
		Iterator<?> iter = this.armyIcons.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry)iter.next();
				if (country.getId() == ((Country)pairs.getKey()).getId()) {
					return (MArmyCountIcon)pairs.getValue();
				}
		}
		return null;
	}
	
	/**
	 * Method, which will be called if the observed object
	 * notifies the observers.
	 *
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.updatePanel();
	}
}