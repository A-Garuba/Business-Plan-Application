package software_masters.planner_networking;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author john.dyar
 *
 */
public class Driver extends Application implements ViewTransitionalModel
{

	private Registry registry;
	private Client client;
	
	private LoginController loginController;
	private MainController mainController;
	private CompareController compareController;
	
	private Stage stage;

	/**
	 * launches the javafx application
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		launch(args);
	}

	/*
	 * Connects to the server and houses the primary stage of the application
	 * 
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try
		{
			registry = LocateRegistry.getRegistry(1060);
			Server stub = (Server) registry.lookup("PlannerServer");
			client = new Client(stub);

		} catch (Exception e)
		{
			e.printStackTrace();

		}

		stage = primaryStage;
		showLogin();
		primaryStage.setTitle("Centre Business Plans");
		primaryStage.show();
		primaryStage.setOnCloseRequest(e ->
			{
				e.consume();
				try
				{
					closing();
				} catch (RemoteException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

	}

	/**
	 * Creates a confirmation box and closes the client's application
	 * 
	 * @throws RemoteException
	 */
	public void closing() throws RemoteException
	{
		boolean confirm = false;
		confirm = ConfirmationBox.show("Are you sure you want to quit? \n" + "Unsaved data will not be saved ",
				"Confirmation", "Yes", "No");
		if (confirm)
		{
			client.getServer().save();
			stage.close();
		}
	}

	public void showLogin() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));

		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);

		loginController = loader.getController();
		loginController.setClient(client);
		loginController.setViewTransitionalModel(this);
		stage.setScene(scene);

	}

	public void showMain() throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));

		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);

		mainController = loader.getController();
		mainController.setClient(client);
		mainController.setViewTransitionalModel(this);
		mainController.getPlans(mainController.yearDropdown);
		stage.setScene(scene);
	}
	
	public void showCompare() throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CompareScene.fxml"));

		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);

		compareController = loader.getController();
		compareController.setClient(client);
		compareController.setViewTransitionalModel(this);
		//compareController.getPlans(compareController.yearDropdown);
		stage.setScene(scene);
	}

}
