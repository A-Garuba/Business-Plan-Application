package software_masters.planner_networking;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class CompareController
{

	@FXML
	private BorderPane mainBorderPane;

	@FXML
	private Button compareButton;

	@FXML
	private Button returnButton;

	@FXML
	private Button logoutButton;

	@FXML
	private TreeView<?> tree_left;

	@FXML
	private TreeView<?> tree_right;

	@FXML
	private TreeView<?> tree_content_left;

	@FXML
	private TreeView<?> tree_content_right;

	@FXML
	private ChoiceBox<?> yearDropdown_left;

	@FXML
	private ChoiceBox<?> yearDropdown_right;
	
	//Non-FX attributes
	private Client client;
	private TreeItem<Node> currNode;
	private ViewTransitionalModel vtmodel;
	
	
	public void setViewTransitionalModel(ViewTransitionalModel model)
	{
		this.vtmodel = model;
	}

	public void setClient(Client client)
	{

		this.client = client;
	}

	@FXML
	void logout(MouseEvent event) throws IOException
	{
		if (ConfirmationBox.show("Are you sure you want to log out?", "Logout?", "Yes", "No"))
		{
			vtmodel.showLogin();
		}
	}
	
	/**
	 * This function allows the 'Return' button to switch the stage to the MainScene view
	 * @param event
	 * @throws IOException
	 * @throws Exception
	 */
	@FXML
	void ret(MouseEvent event) throws IOException, Exception
	{
		vtmodel.showMain();
	}

}
