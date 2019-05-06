package software_masters.planner_networking;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class LoginController
{

	private Client client;

	@FXML
	private TextField hostText;

	@FXML
	private Label hostError;

	@FXML
	private TextField userText;

	@FXML
	private Label userError;

	@FXML
	private PasswordField passText;

	@FXML
	private Label passError;

	@FXML
	private Button loginButton;

	@FXML
	private Label badLogin;

	ViewTransitionalModel vtmodel;

	public void setViewTransitionalModel(ViewTransitionalModel model)
	{
		this.vtmodel = model;
	}

	/**
	 * This function handles when the login button is pressed
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void loginPress(ActionEvent event) throws Exception
	{
		userError.setVisible(false);
		passError.setVisible(false);
		hostError.setVisible(false);
		badLogin.setVisible(false);

		String user = userText.getText();
		String pass = passText.getText();
		String host = hostText.getText();

		if (user.equals("") || pass.equals("") || host.equals(""))
		{
			if (user.equals(""))
			{
				userError.setVisible(true);
			}
			if (pass.equals(""))
			{
				passError.setVisible(true);
			}
			if (host.equals(""))
			{
				hostError.setVisible(true);
			}
		} else
		{

			try
			{
				// TODO set server somehow //
				client.login(user, pass);
				// TODO reset scene and switch to ClientView
				vtmodel.showMain();
			} catch (IllegalArgumentException e)
			{
				badLogin.setVisible(true);
			}
		}
	}

	/**
	 * This function handles the user pressing "ENTER" at the login screen
	 * 
	 * @param e KeyEvent
	 * @throws Exception
	 */
	@FXML
	public void buttonPressed(KeyEvent e) throws Exception
	{
		if (e.getCode().toString().equals("ENTER"))
		{
			loginPress(null);
		}
	}

	/**
	 * @return the client
	 */
	public Client getClient()
	{
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Client client)
	{
		this.client = client;
	}

}
