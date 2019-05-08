package software_masters.planner_networking;

import java.io.*;
import java.rmi.RemoteException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
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
	
    @FXML
    private Label leftError;

    @FXML
    private Label rightError;
	
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
	 * This function updates the TreeViews with the selected years when the 'Compare' button is pressed.
	 * The outer TreeViews are populated with the PlanFile's section names while the inner TreeViews are
	 * populated with the PlanFile's section contents.
	 * 
	 * @param event
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 * @throws FileNotFoundException 
	 */
	@FXML
	void plansSelected(MouseEvent event) throws IllegalArgumentException, RemoteException, FileNotFoundException
	{
		leftError.setVisible(false);
		rightError.setVisible(false);
		
		//must select years in both ChoiceBoxes
		if (yearDropdown_left.getValue() == null || yearDropdown_right.getValue() == null)
		{
			if (yearDropdown_left.getValue() == null )
			{
				leftError.setVisible(true);
			}
			if (yearDropdown_right.getValue() == null )
			{
				rightError.setVisible(true);
			}
		}
		else
		{
			tree_left.setRoot(makeTree(yearDropdown_left.getValue().getYear(), false).getRoot());
			tree_content_left.setRoot(makeTree(yearDropdown_left.getValue().getYear(), true).getRoot());
			
			tree_right.setRoot(makeTree(yearDropdown_right.getValue().getYear(), false).getRoot());
			tree_content_right.setRoot(makeTree(yearDropdown_right.getValue().getYear(), true).getRoot());
			
			compare(tree_left.getRoot(), tree_right.getRoot());
			compare(tree_content_left.getRoot(), tree_content_right.getRoot());
		}
	}
	
	/**
	 * This function compares 2 TreeViews and graphically updates TreeItems that contain conflicts
	 * 
	 * @param left the left TreeView's current TreeItem
	 * @param right the right TreeView's current TreeItem
	 */
	private void compare(TreeItem<Node> left, TreeItem<Node> right)
	{
		//at maximum left TreeView depth, rest of right TreeView is conflicts
		if (left == null && right != null)
		{
			ImageView conflict = new ImageView(new Image(getClass().getResourceAsStream("conflict.png")));
			conflict.setFitHeight(20);
			conflict.setFitWidth(20);
			
			right.setGraphic(conflict);
			for (int i = 0; i < right.getChildren().size(); i++)
			{
				compare(null,right.getChildren().get(i));
			}
		}
		//at maximum right TreeView depth, rest of left TreeView is conflicts
		else if (left != null && right == null)
		{
			ImageView conflict = new ImageView(new Image(getClass().getResourceAsStream("conflict.png")));
			conflict.setFitHeight(20);
			conflict.setFitWidth(20);
			
			left.setGraphic(conflict);
			for (int i = 0; i < left.getChildren().size(); i++)
			{
				compare(left.getChildren().get(i), null);
			}
		}
		//top or middle of both TreeViews (possibly at a leaf of one or other or both)
		else
		{
			if (!left.getValue().getName().equals(right.getValue().getName()))
			{
				ImageView conflict = new ImageView(new Image(getClass().getResourceAsStream("conflict.png")));
				conflict.setFitHeight(20);
				conflict.setFitWidth(20);
				ImageView conflict1 = new ImageView(new Image(getClass().getResourceAsStream("conflict.png")));
				conflict1.setFitHeight(20);
				conflict1.setFitWidth(20);
				
				left.setGraphic(conflict);
				right.setGraphic(conflict1);
			}

			//iterate through whichever TreeView is deeper
			//	(other will go to null, display conflict for remaining TreeItems)
			if (left.getChildren().size() > right.getChildren().size())
			{
				for (int i = 0; i < left.getChildren().size(); i++)
				{
					if (i >= right.getChildren().size())
					{
						compare(left.getChildren().get(i), null);
					}
					else
					{
						compare(left.getChildren().get(i), right.getChildren().get(i));
					}
				}
			}
			else
			{
				for (int i = 0; i < right.getChildren().size(); i++)
				{
					if (i >= left.getChildren().size())
					{
						compare(null, right.getChildren().get(i));
					}
					else
					{
						compare(left.getChildren().get(i), right.getChildren().get(i));
					}
				}
			}

		}
	}
	
	/**
	 * Creates a tree for display in the application
	 * 
	 * @param year The current year from the ChoiceBox
	 * @param showData a boolean regarding whether the TreeItem should display the plan section's content
	 * @return TreeView<Node>(treeRoot) which will be displayed
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public TreeView<Node> makeTree(String year, boolean showData) throws IllegalArgumentException, RemoteException
	{
		client.getPlan(year);
		Node nodeRoot = client.getCurrNode();
		TreeItem<Node> treeRoot = new TreeItem<Node>(nodeRoot);
		
		//Display plan section content if requested
		if (showData)
		{
			treeRoot.getValue().setName(treeRoot.getValue().getData());
		}
		
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot, showData);
		return new TreeView<Node>(treeRoot);
	}

	/**
	 * This function uses recursive search through the root node to create a TreeView
	 * 
	 * @param parent the TreeItem to be searched under
	 * @param showData a boolean regarding whether the TreeItem should display the plan section's content
	 */
	private static void recursiveSearch(TreeItem<Node> parent, boolean showData)
	{
		if (parent.getValue().getChildren().size() != 0)
		{

			for (int i = 0; i < parent.getValue().getChildren().size(); i++)
			{
				TreeItem<Node> tree = new TreeItem<Node>(parent.getValue().getChildren().get(i));
				
				//Display plan section content if requested
				if (showData)
				{
					tree.getValue().setName(tree.getValue().getData());
				}
				
				tree.setExpanded(true);
				parent.getChildren().add(tree);
				recursiveSearch(tree, showData);
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
