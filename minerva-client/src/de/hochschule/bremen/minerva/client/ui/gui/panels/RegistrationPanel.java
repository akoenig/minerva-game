/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: RegistrationPanel.java 674 2010-07-04 16:34:12Z andre.koenig $
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
 *     AndrÃ© KÃ¶nig: akoenig@stud.hs-bremen.de
 * 
 * Web:
 *     http://minerva.idira.de
 * 
 */
package de.hochschule.bremen.minerva.client.ui.gui.panels;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import de.hochschule.bremen.minerva.client.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MPasswordField;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MTextField;
import de.hochschule.bremen.minerva.client.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.commons.vo.Player;

/**
 * Panel for user registration
 * 
 * @version $Id: RegistrationPanel.java 674 2010-07-04 16:34:12Z andre.koenig $
 * @since 1.0
 *
 */
public class RegistrationPanel extends JLayeredPane implements TextResources {

	private Background background;
	
	//text fields
	private MTextField firstName;
	private MTextField lastName;
	private MTextField username;
	private MPasswordField password;
	private MPasswordField passwordRetype;
	private MTextField email;
	
	//labels
	private JLabel statusLabel;
	
	//buttons
	private JButton registerButton;
	private JButton backButton;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6086001895310561512L;

	/**
	 * Initialization of the panel's construction
	 */
	public RegistrationPanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		
		//background
		this.background = new Background(this.getClass());
		this.background.setBounds(0, 0, 500, 500);
		
		//first name field
		this.firstName = new MTextField();
		this.firstName.setBounds(614, 153, 117, 25);
		this.firstName.setOpaque(true);
		
		//last name field
		this.lastName = new MTextField();
		this.lastName.setBounds(741, 153, 117, 25);
		this.lastName.setOpaque(true);
		
		//username field
		this.username = new MTextField();
		this.username.setBounds(614, 217, 244, 25);
		this.username.setOpaque(true);
		
		//password field
		this.password = new MPasswordField();
		this.password.setBounds(614, 286, 244, 25);
		this.password.setOpaque(true);
		
		//password retype
		this.passwordRetype = new MPasswordField();
		this.passwordRetype.setBounds(614, 357, 244, 25);
		this.passwordRetype.setOpaque(true);
		
		//email
		this.email = new MTextField();
		this.email.setBounds(614, 429, 244, 25);
		this.email.setOpaque(true);
		this.email.setToolTipText(REGISTRATION_TOOLTIP_EMAIL);
		
		//labels
		this.statusLabel = new JLabel();
		this.statusLabel.setBounds(614, 510, 244, 50);

		Rectangle buttonRectangle = new Rectangle();
		
		//login button
		this.registerButton = new JButton(REGISTRATION_BUTTON_DONE);
		buttonRectangle.width = (int)this.registerButton.getPreferredSize().getWidth();
		buttonRectangle.height = (int)this.registerButton.getPreferredSize().getHeight();
		buttonRectangle.x = ((this.email.getX() + this.email.getWidth()) - buttonRectangle.width);
		buttonRectangle.y = 476;
		this.registerButton.setBounds(buttonRectangle);

		//back button
		this.backButton = new JButton(REGISTRATION_BUTTON_BACK);

		buttonRectangle.width = (int)this.backButton.getPreferredSize().getWidth();
		buttonRectangle.height = (int)this.backButton.getPreferredSize().getHeight();
		buttonRectangle.x = this.email.getX();
		buttonRectangle.y = 476;
		this.backButton.setBounds(buttonRectangle);

		//adding everything to panel
		//labels
		this.add(this.statusLabel,30);
		this.add(this.firstName,20);
		this.add(this.lastName,20);
		this.add(this.username,20);
		this.add(this.password,20);
		this.add(this.passwordRetype,20);
		this.add(this.email,20);
		this.add(this.registerButton,20);
		this.add(this.backButton,30);
		this.add(this.background,10);
		
		this.addListeners();
		
		// Set the focus on "first name" textfield.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				RegistrationPanel.this.firstName.requestFocus();
			}
		});
	}
	
	/**
	 * Gets first name out of text field
	 * @return first name
	 */
	public String getFirstName() {
		return this.firstName.getText();
	}
	/**
	 * Gets last name out of text field
	 * @return last name
	 */
	public String getLastName() {
		return this.lastName.getText();
	}
	/**
	 * Gets username out of text field
	 * @return username
	 */
	public String getUsername() {
		return this.username.getText();
	}
	/**
	 * Gets password out of text field
	 * @return password
	 */
	public String getPassword() {
		return String.valueOf(this.password.getPassword());
	}
	/**
	 * Gets retyped password out of text field
	 * @return retyped password
	 */
	public String getPasswordRetype() {
		return String.valueOf(this.passwordRetype.getPassword());
	}
	/**
	 * Gets email out of text field
	 * @return email
	 */
	public String getEmail() {
		return this.email.getText();
	}
	/**
	 * Adds an action listener to the registration button
	 * @param l Action Listener
	 * @deprecated automatically added
	 */
	public void addRegisterButtonListener(ActionListener l) {
		this.registerButton.addActionListener(l);
	}

	/**
	 * Test is all field of the registration sheet are filled in properly.
	 * 
	 * @param player Player with all transfered attributes
	 * @param retypedPassword retyped password
	 * @return if sheet is valid or not
	 */
	private boolean areRegistrationCredentialsValid(Player player, String retypedPassword) {
		boolean valid = false;

		if (player.getFirstName().isEmpty()) {
			MMessageBox.show(REGISTRATION_FIRST_NAME_EMPTY);
			this.firstName.setValid(valid);
		} else if (player.getLastName().isEmpty()) {
			MMessageBox.show(REGISTRATION_LAST_NAME_EMPTY);
			this.lastName.setValid(valid);
		} else if (player.getUsername().isEmpty()) {
			MMessageBox.show(REGISTRATION_USERNAME_EMPTY);
			this.username.setValid(valid);
		} else if (player.getPassword().isEmpty()) {
			MMessageBox.show(REGISTRATION_PASSWORD_EMPTY);
			this.password.setValid(valid);
		} else if (retypedPassword.isEmpty()) {
			MMessageBox.show(REGISTRATION_PASSWORD_RETYPED_EMPTY);
			this.passwordRetype.setValid(valid);
		} else if (!player.getPassword().equals(retypedPassword)) {
			MMessageBox.show(REGISTRATION_PASSWORD_RETYPED_INVALID);
			this.passwordRetype.setValid(valid);
		} else if (player.getEmail().isEmpty()) {
			MMessageBox.show(REGISTRATION_EMAIL_EMPTY);
			this.email.setValid(valid);
		} else if (!this.isEmailValid(player.getEmail())) {
			MMessageBox.show(REGISTRATION_EMAIL_IS_INVALID);
			this.email.setValid(valid);
		} else {
			valid = true;
		}
		
		return valid;
	}

	/**
	 * Checks if an given email address is valid.
	 * 
	 * @param email The checkable email address.
	 * @return boolean
	 * 
	 */
	private boolean isEmailValid(String email) {
		String regex = ".+@.+\\.[a-z]+";
		return Pattern.matches(regex, email);
	}

	/**
	 * Converts the form data to an player object.
	 * 
	 * @return The player object with the form data.
	 * 
	 */
	private Player transformForm() {
		Player player = new Player();
		player.setUsername(RegistrationPanel.this.username.getText());
		player.setFirstName(RegistrationPanel.this.firstName.getText());
		player.setLastName(RegistrationPanel.this.lastName.getText());
		player.setPassword(String.copyValueOf(RegistrationPanel.this.password.getPassword()));
		player.setEmail(RegistrationPanel.this.email.getText());
		
		return player;
	}
	
	/**
	 * Adds all action listeners to this panel
	 */
	private void addListeners() {

		// Registration button event handler.
		this.registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Player player = RegistrationPanel.this.transformForm();
				String retypedPassword = String.copyValueOf(RegistrationPanel.this.passwordRetype.getPassword());

				if (RegistrationPanel.this.areRegistrationCredentialsValid(player, retypedPassword)) {
					
					try {
						MinervaGUI.getEngine().register(player);
						
						LoginPanel loginPanel = new LoginPanel();
						loginPanel.setStatusText(REGISTRATION_ACCOUNT_CREATED);
						MinervaGUI.getInstance().changePanel(loginPanel);
					} catch (PlayerExistsException e) {
						MMessageBox.show(e.getMessage());
					} catch (DataAccessException e) {
						// TODO: Handle this Exception.
						MMessageBox.show(e.getMessage());
						Runtime.getRuntime().exit(ERROR);
					}

				}
			}
		});
		
		// back button
		this.backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MinervaGUI.getInstance().changePanel(new LoginPanel());
			}
		});
	}
}
