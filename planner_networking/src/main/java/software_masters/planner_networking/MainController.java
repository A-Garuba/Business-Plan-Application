package software_masters.planner_networking;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MainController
{
	
	@FXML
	private Button yearSelectButton;

	@FXML
	private Button addChildButton;

	@FXML
	private Button removeButton;

	@FXML
	private Button editButton;

	@FXML
	private Button saveButton;

	@FXML
	private Button copyButton;

	@FXML
	private Button logout;
	
    @FXML
    private Button deleteButton;

    @FXML
    private Button commentButton;
    
    @FXML
    private ListView<String> commentArea;
	
    @FXML
    private TextField contentField;

    @FXML
    private TextField commentField;

	@FXML
	private TextField newYearTxtField;

	@FXML
	private Button enterNewYearButton;

	@FXML
	private TreeView<Node> tree;
	
	@FXML
	ChoiceBox<PlanFile> yearDropdown;
	
	@FXML
	private Label commentError;
	
	//Non-FX attributes
	private Client client;

	private TreeItem<Node> currNode;

	ViewTransitionalModel vtmodel;

	ChangeListener<String> listener;

	@FXML
	void addBranch(MouseEvent event) throws Exception
	{
		addBranch(currNode, yearDropdown.getValue().getYear());
	}
	
	/**
	 * This functions allows any user to add a comment to any selected plan section by typing in the
	 * comment TextField and pressing the 'Enter' button
	 * @param event
	 * @throws RemoteException 
	 */
	@FXML
	void addComment(MouseEvent event) throws RemoteException
	{
		commentError.setVisible(false);
		if (commentField.getText() == null)
		{
			commentError.setVisible(true);
		}
		else
		{
			currNode.getValue().addComment(client.getUser() + ": " + commentField.getText());
			
			commentField.setText("");
			updateComments(currNode);
			
			saveComment(tree.getRoot().getValue(), yearDropdown.getValue().getYear());
		}
	}
	
	/**
	 * This function allows any user to delete any comment selected in the comment TextArea by pressing the
	 * 'Delete Selected Comment' button
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void deleteComment(MouseEvent event) throws RemoteException
	{
		String comment = commentArea.getSelectionModel().getSelectedItem();
		currNode.getValue().removeComment(comment);
		updateComments(currNode);
		
		saveComment(tree.getRoot().getValue(), yearDropdown.getValue().getYear());
	}
	
	/**
	 * Saves a planFile to the server for commenting purposes
	 * 
	 * @param node
	 * @param s
	 * @throws RemoteException
	 */
	public void saveComment(Node node, String s) throws RemoteException
	{
		Centre plan = new Centre(node);
		PlanFile planF = new PlanFile(s, true, plan);
		client.pushComment(planF);
	}
	
	/**
	 * This function handles the user pressing "ENTER" while entering a comment
	 * 
	 * @param e KeyEvent
	 * @throws Exception
	 */
	@FXML
	public void enterPressed(KeyEvent e) throws Exception
	{
		if (e.getCode().toString().equals("ENTER"))
		{
			addComment(null);
		}
	}

	@FXML
	void copy(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		newYearTxtField.setText("");
		newYearTxtField.setEditable(true);
		removeButton.setDisable(true);
		addChildButton.setDisable(true);
		enterNewYearButton.setDisable(false);
		copyButton.setDisable(true);
		String year = yearDropdown.getValue().getYear();
		yearSelectButton.setDisable(true);
		yearDropdown.setDisable(true);
		tree.setRoot(makeDeepCopy(year).getRoot());
		tree.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> treeSelectionChanged(newValue));
		editButton.setText("View");
		editButton.setDisable(true);
		saveButton.setDisable(true);
	}

	@FXML
	void edit(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		client.getPlan(yearDropdown.getValue().getYear());
		Boolean bool = client.getCurrPlanFile().isCanEdit();

		if ((editButton.getText() == "Edit") && bool)
		{
			editButton.setText("View");
			contentField.setEditable(true);
			saveButton.setDisable(false);
			copyButton.setDisable(false);
		} else if (editButton.getText() == "View")
		{
			editButton.setText("Edit");
			contentField.setEditable(false);
			addChildButton.setDisable(true);
			removeButton.setDisable(true);
			saveButton.setDisable(true);
			copyButton.setDisable(true);
		}
	}

	@FXML
	void logout(MouseEvent event) throws IOException
	{
		boolean confirm = false;
		TreeItem<Node> check = tree.getRoot();

		confirm = ConfirmationBox.show("Do you want to save before you log out?", "Save", "Yes", "No");
		if (confirm)
		{
			if (!(check == null))
			{
				saveC(tree.getRoot().getValue(), yearDropdown.getValue().getYear());
			}
		}
		vtmodel.showLogin();
	}
	
	/**
	 * This function allows the 'Compare' button to switch the stage to the CompareScene view
	 * @param event
	 * @throws IOException
	 * @throws Exception
	 */
	@FXML
	void compare(MouseEvent event) throws IOException, Exception
	{
		vtmodel.showCompare();
	}

	@FXML
	void newPlan(MouseEvent event) throws Exception
	{
		String year = newYearTxtField.getText();
		if (year.length() >= 1)
		{
			saveC(tree.getRoot().getValue(), year);
			yearSelectButton.setDisable(false);
			yearDropdown.setDisable(false);
			editButton.setDisable(false);
			client.getPlan(newYearTxtField.getText());
			yearDropdown.getItems().add(client.getCurrPlanFile());
			newYearTxtField.setText("");
			newYearTxtField.setEditable(false);
			enterNewYearButton.setDisable(true);
			saveButton.setDisable(false);
			yearDropdown.getItems().clear();
			getPlans(yearDropdown);
			tree.setRoot(null);
			addChildButton.setDisable(true);
			removeButton.setDisable(true);
			editButton.setDisable(true);
			saveButton.setDisable(true);
		} else
		{
			System.out.println("Please enter a valid year please");
		}
	}

	@FXML
	void planChange(MouseEvent event) throws IllegalArgumentException, RemoteException
	{
		editButton.setDisable(false);
		logout.setDisable(false);
		commentButton.setDisable(true);
		commentField.setDisable(true);
		commentArea.setItems(null);
		contentField.setDisable(false);
		
		tree.setRoot(makeTree(yearDropdown.getValue().getYear()).getRoot());

		tree.getSelectionModel().selectedItemProperty()
				.addListener((v, oldValue, newValue) -> treeSelectionChanged(newValue));
		editButton.setText("View");
		edit(event);
	}

	@FXML
	void remove(MouseEvent event)
	{
		if (currNode.getParent().getChildren().size() > 1)
		{
			client.setCurrNode(currNode.getValue());
			client.getCurrNode().getParent().removeChild(client.getCurrNode());
			currNode.getParent().getChildren().remove(currNode);
		}
	}

	@FXML
	void save(MouseEvent event) throws RemoteException
	{
		saveC(tree.getRoot().getValue(), yearDropdown.getValue().getYear());
	}

	void setText(String newvalue)
	{
		currNode.getValue().setData(newvalue);
	}

	void treeSelectionChanged(TreeItem<Node> item)
	{
		if (listener != null)
		{
			contentField.textProperty().removeListener(listener);
		}

		if (item != null)
		{
			String str = item.getValue().getData();
			contentField.setText(str);
			
			commentButton.setDisable(false);
			commentField.setDisable(false);
			updateComments(item);
			
			this.currNode = item;
			if (editButton.getText().contentEquals("View"))
			{
				addChildButton.setDisable(false);
				removeButton.setDisable(false);
			}
		}
		listener = (observable, oldvalue, newvalue) -> setText(newvalue);
		contentField.textProperty().addListener(listener);
	}
	
	/**
	 * This function updates the commentArea with the comments stored in the TreeItem's Node
	 * 
	 * @param selected TreeItem<Node> that is currently selected
	 */
	public void updateComments(TreeItem<Node> selected)
	{
		ArrayList<String> comments = selected.getValue().getComments();
		ObservableList<String> data = FXCollections.observableArrayList();
		for (String i: comments)
		{
			data.add(i);
		}
		
		commentArea.setItems(data);
		
		deleteButton.setDisable(commentArea.getItems().size() < 1);
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
				TreeItem<Node> tree1 = new TreeItem<Node>(parent.getValue().getChildren().get(i));
				tree1.setExpanded(true);
				parent.getChildren().add(tree1);
				recursiveSearch(tree1);
			}
		}
	}

	/**
	 * Creates a new branch for the Node tree, then adds it to the treeView
	 * 
	 * @param currNode
	 * @param year
	 * @throws Exception
	 */
	public void addBranch(TreeItem<Node> currNode, String year) throws Exception
	{
		client.getPlan(year);
		client.setCurrNode(currNode.getValue().getChildren().get(0));
		client.addBranch();
		TreeItem<Node> tree2 = new TreeItem<Node>(
				currNode.getValue().getChildren().get(currNode.getValue().getChildren().size() - 1));
		recursiveSearch(tree2);
		currNode.getChildren().add(tree2);
	}

	/**
	 * Saves a planFile to the server
	 * 
	 * @param node
	 * @param s
	 * @throws RemoteException
	 */
	public void saveC(Node node, String s) throws RemoteException
	{
		Centre plan = new Centre(node);
		PlanFile planF = new PlanFile(s, true, plan);
		client.pushPlan(planF);
	}

	/**
	 * Makes a deep copy of a tree for use as templates
	 * 
	 * @param year
	 * @return TreeView<Node>(treeRoot)
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public TreeView<Node> makeDeepCopy(String year) throws IllegalArgumentException, RemoteException
	{
		client.getPlan(year);
		Node master = client.getCurrNode();
		Node copy = new Node(null, master.getName(), master.getData(), null, null);
		deepCopier(master, copy);
		TreeItem<Node> treeRoot = new TreeItem<Node>(copy);
		treeRoot.setExpanded(true);
		recursiveSearch(treeRoot);
		return new TreeView<Node>(treeRoot);
	}

	/**
	 * recursive searcher for the deep copier
	 * 
	 * @param master
	 * @param copy
	 * @throws RemoteException
	 */
	private static void deepCopier(Node master, Node copy) throws RemoteException
	{
		if (master.getChildren().size() != 0)
		{

			for (int i = 0; i < master.getChildren().size(); i++)
			{
				Node tree1 = new Node(copy, master.getChildren().get(i).getName(),
						master.getChildren().get(i).getData(), null, null);
				copy.getChildren().add(tree1);
				deepCopier(master.getChildren().get(i), tree1);
			}
		}
	}
	
	public void setViewTransitionalModel(ViewTransitionalModel model)
	{
		this.vtmodel = model;
	}

	public void setClient(Client client)
	{

		this.client = client;
	}

}
