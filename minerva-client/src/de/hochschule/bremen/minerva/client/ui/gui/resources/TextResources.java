/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: TextResources.java 825 2010-07-06 21:05:00Z andre.koenig $
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
package de.hochschule.bremen.minerva.client.ui.gui.resources;

/**
 * GUI text resources.<br />
 * Yeah. It's better to use resource bundles. But this is a lightweight
 * solution. Not so nice, but it works :\
 * 
 * @version $Id: TextResources.java 825 2010-07-06 21:05:00Z andre.koenig $
 * @since 1.0
 *
 */
public interface TextResources {

	// Login panel
	public static final String LOGIN_PANEL_BUTTON                            = "Let's rock!";
	public static final String LOGIN_PANEL_STATUS_WHILE_LOGIN                = "Login ...";
	public static final String LOGIN_PANEL_MESSAGE_INPUT_INCOMPLETE          = "Bitte geben Sie Ihre Zugangsdaten ein.";
	public static final String LOGIN_PANEL_MESSAGE_USER_INPUT_INCOMPLETE     = "Bitte geben Sie einen Username ein.";
	public static final String LOGIN_PANEL_MESSAGE_PASSWORD_INPUT_INCOMPLETE = "Bitte geben Sie ein Passwort ein.";

	// Registration panel
	public static final String REGISTRATION_BUTTON_DONE              = "Fertig!";
	public static final String REGISTRATION_BUTTON_BACK              = "Zurück";
	public static final String REGISTRATION_TOOLTIP_EMAIL            = "Nein, Sie bekommen kein Spam. Versprochen ;)";
	public static final String REGISTRATION_USERNAME_EMPTY           = "Bitte geben Sie einen Usernamen an.";
	public static final String REGISTRATION_FIRST_NAME_EMPTY         = "Bitte geben Sie Ihren Vorname an.";
	public static final String REGISTRATION_LAST_NAME_EMPTY          = "Bitte geben Sie Ihren Nachnamen an.";
	public static final String REGISTRATION_EMAIL_EMPTY              = "Bitte geben Sie eine E-Mail-Adresse an.";
	public static final String REGISTRATION_EMAIL_IS_INVALID 	     = "Die angegebene E-Mail-Adresse ist ungültig (Gültig: name@host.de)";
	public static final String REGISTRATION_PASSWORD_EMPTY           = "Bitte geben Sie ein Passwort an.";
	public static final String REGISTRATION_PASSWORD_RETYPED_EMPTY   = "Bitte geben Sie ihr Passwort erneut ein.";
	public static final String REGISTRATION_PASSWORD_RETYPED_INVALID = "Das erneut eingegebene Passwort stimmt nicht mit dem ursprünglichen Passwort überein.";
	public static final String REGISTRATION_ACCOUNT_CREATED          = "Account erstellt ...";

	// GameInitPanel
	public static final String GAME_INIT_PANEL_BUTTON_START_GAME = "Spiel starten ...";
	
	// PlayerInitPanel
	public static final String PLAYER_INIT_PANEL_BUTTON_ADD_PLAYER = "Spieler hinzufügen";
	
	// WorldInitPanel
	public static final String WORLD_INIT_PANEL_INTRODUCTION = "<html>{gm}, bitte wähle aus der unteren Liste eine Welt aus, auf der gespielt werden soll.</html>";
	public static final String WORLD_INIT_PANEL_SELECTION    = "Auswahl:";
	public static final String WORLD_INIT_PANEL_VERSION      = "Version:";
	public static final String WORLD_INIT_PANEL_AUTHOR       = "Autor(en):";
	public static final String WORLD_IMPORT_BUTTON_TEXT		 = "+";
	public static final String WORLD_IMPORT_SUCCESSFUL		 = "Die Welt wurde erfolgreich importiert.";
	
	// GamePanel
	public static final String GAME_PANEL_YOUR_MISSION = "Hallo {player}, das ist deine Mission: ";

	// GamePanelControlbar
	public static final String GAME_PANEL_CARDS = "Karten";
	public static final String GAME_PANEL_CARDS_SELECT_CARD = "Bitte wähle erst eine einzulösende Karte aus.";
	public static final String GAME_PANEL_CONTROLBAR_BUTTON_SET_ARMIES      = "Armeen setzen";
	public static final String GAME_PANEL_CONTROLBAR_BUTTON_TURN_CARDS_IN   = "Karten abgeben";
	public static final String GAME_PANEL_CONTROLBAR_BUTTON_ATTACK          = "Angriff";
	public static final String GAME_PANEL_CONTROLBAR_BUTTON_ALLOCATE_ARMIES = "Armeen verschieben";
	public static final String GAME_PANEL_CONTROLBAR_BUTTON_FINISH_TURN     = "Zug beenden";
	public static final String GAME_PANEL_CONTROLBAR_BUTTON_SLIDE           = "Karten »";
	public static final String GAME_PANEL_CONTROLBAR_CARD_SYMBOL_SOLDIER    = "Symbol: Soldier, Land: ";
	public static final String GAME_PANEL_CONTROLBAR_CARD_SYMBOL_CAVALERIE  = "Symbol: Cavalerie, Land: ";
	public static final String GAME_PANEL_CONTROLBAR_CARD_SYMBOL_CANON      = "Symbol: Canon, Land: ";

	// GamePanel Attack Result
	
	// Game
	public static final String GAME_CARD_RELEASE_ERROR_COUNTRY_UNCONQUERED	= "Das der Länderkarte zugehörige Land ist nicht von dir besetzt.";
	public static final String GAME_ATTACK_ERROR_SAME_COUNTRY				= "Du kannst dein eigenes Land nicht angreifen.";
	public static final String GAME_MOVE_ERROR_SAME_COUNTRY					= "Du kannst nicht auf das selbe Land Armeen verschieben.";
	public static final String GAME_FINISHED_ANNOUCEMENT					= "Das Spiel ist vorbei!";
	public static final String GAME_FINISHED_WINNER							= " hat das Spiel gewonnen.";
	
	// -- Controls

	// MPlayerIcon
	public static final String MPLAYERICON_USERNAME = "Benutzername:";
	public static final String MPLAYERICON_EMAIL = "E-Mail:";
	public static final String MPLAYERICON_GAMEMASTER = "Spielmaster";
	
	// MMessageBox
	public static final String MMESSAGE_INFO_TITLE = "minerva - the game";
	public static final String MMESSAGE_ERROR_TITLE = MMESSAGE_INFO_TITLE + " - Fehler";
}