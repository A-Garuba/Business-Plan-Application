package software_masters.planner_networking;

import static org.junit.Assert.*;
import org.junit.After;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class LoginTest extends ApplicationTest
{

	private Scene scene;

	private TextField t;
	private Label l;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		// ServerImplementation.main(null);
		ApplicationTest.launch(Driver.class);
	}

	/**
	 * Connects to the server and houses the primary stage of the application
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.show();

		scene = primaryStage.getScene();
	}

	/**
	 * This method logs in and back out to reset the login screen
	 */
	@After
	public void reset()
	{
		t = (TextField) scene.lookup("#hostText");
		if (!t.getText().equals("localhost"))
		{
			clickOn("#hostText");
			clickOn("#hostText");
			write("localhost");
		}

		t = (TextField) scene.lookup("#userText");
		if (!t.getText().equals("user"))
		{
			clickOn("#userText");
			clickOn("#userText");
			write("user");
		}

		t = (TextField) scene.lookup("#passText");
		if (!t.getText().equals("user"))
		{
			clickOn("#passText");
			clickOn("#passText");
			write("user");
		}

		clickOn("#loginButton");
		clickOn("#logout");
		clickOn("#yes");
	}

	@Test
	public void testStart()
	{
		t = (TextField) scene.lookup("#hostText");
		assertTrue(t.getText().equals("localhost"));

		t = (TextField) scene.lookup("#userText");
		assertTrue(t.getText().isEmpty());

		t = (TextField) scene.lookup("#passText");
		assertTrue(t.getText().isEmpty());
	}

	@Test
	public void testFailHost()
	{
		clickOn("#hostText");
		clickOn("#hostText");
		type(KeyCode.BACK_SPACE);

		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");

		l = (Label) scene.lookup("#hostError");

		assertFalse(l.isVisible());

		clickOn("#loginButton");

		assertTrue(l.isVisible());
	}

	@Test
	public void testFailUser()
	{
		clickOn("#passText");
		write("user");

		l = (Label) scene.lookup("#userError");

		assertFalse(l.isVisible());

		clickOn("#loginButton");

		assertTrue(l.isVisible());
	}

	@Test
	public void testFailPass()
	{
		clickOn("#userText");
		write("user");

		l = (Label) scene.lookup("#passError");

		assertFalse(l.isVisible());

		clickOn("#loginButton");

		assertTrue(l.isVisible());
	}

	@Test
	public void testInvalidLogin()
	{
		clickOn("#userText");
		write(" ");
		clickOn("#passText");
		write(" ");

		l = (Label) scene.lookup("#badLogin");

		assertFalse(l.isVisible());

		clickOn("#loginButton");

		assertTrue(l.isVisible());
	}
}
