package software_masters.planner_networking;

import java.io.IOException;
import java.rmi.RemoteException;

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
	private TreeView<Node> tree_left;

	@FXML
	private TreeView<Node> tree_right;

	@FXML
	private TreeView<Node> tree_content_left;

	@FXML
	private TreeView<Node> tree_content_right;

	@FXML
	ChoiceBox<PlanFile> yearDropdown_left;

	@FXML
	ChoiceBox<PlanFile> yearDropdown_right;
	
	//Non-FX attributes
	private Client client;
	private ViewTransitionalModel vtmodel;
	
	
	public void setViewTransitionalModel(ViewTransitionalModel model)
	{
		this.vtmodel = model;
	}

	public void setClient(Client client)
	{

		this.client = client;
	}
	
	/**
	 * This function updates the TreeViews with the selected years when the 'Compare' button is pressed
	 * 
	 * @param event
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	@FXML
	void planChange(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		tree_left.setRoot(makeTree(yearDropdown_left.getValue().getYear()).getRoot());
		tree_right.setRoot(makeTree(yearDropdown_right.getValue().getYear()).getRoot());
	}
	
	/**
	 * Creates a tree for display in the application
	 * 
	 * @param year
	 * @return TreeView<Node>(treeRoot)
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public TreeView<Node> makeTree(String year) throws IllegalArgumentException, RemoteException
	{

		client.getPlan(year);
		Node nodeRoot = client.getCurrNode();
		TreeItem<Node> treeRoot = new TreeItem<Node>(nodeRoot);
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot);
		return new TreeView<Node>(treeRoot);
	}

	/**
	 * used to recursive search through a graph to create a TreeView
	 * 
	 * @param parent
	 */
	private static void recursiveSearch(TreeItem<Node> parent)
	{
		if (parent.getValue().getChildren().size() != 0)
		{

			for (int i = 0; i < parent.getValue().getChildren().size(); i++)
			{
				TreeItem<Node> tree = new TreeItem<Node>(parent.getValue().getChildren().get(i));
				tree.setExpanded(true);
				parent.getChildren().add(tree);
				recursiveSearch(tree);
			}
		}
	}
	
	/**
	 * Gets the plans of the department for display in a choiceBox
	 * 
	 * @param ChoiceBox<PlanFile> plans
	 * @throws Exception
	 */
	public void getPlans(ChoiceBox<PlanFile> plans) throws Exception
	{
		plans.getItems().addAll(client.getPlans());
	}
	
	/**
	 * This function produces a logout confirmation box when 'Logout' is clicked
	 * @param event
	 * @throws IOException
	 */
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
