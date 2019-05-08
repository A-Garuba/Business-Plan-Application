package software_masters.planner_networking;

import static org.junit.Assert.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class CommentTest extends ApplicationTest
{

	private Stage stage;
	private Scene scene;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		// ServerImplementation.main(null);
		ApplicationTest.launch(Driver.class);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		this.stage = stage;
		stage.show();
	}

	@Before
	public void login()
	{
		scene = stage.getScene();
		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");
		clickOn("#loginButton");
	}

	@After
	public void logout()
	{
		clickOn("#logout");
		scene = stage.getScene();
		clickOn("#no");
		scene = stage.getScene();
	}

	public void typeDown(int x)
	{
		if (x > 0)
		{
			for (int i = 0; i < x; i++)
			{
				type(KeyCode.DOWN);
			}
		} else
		{
			for (int i = 0; i > x; i--)
			{
				type(KeyCode.UP);
			}
		}
	}

	public void selectYear(int index)
	{
		clickOn("#yearDropdown");
		typeDown(index);
		type(KeyCode.ENTER);
		clickOn("#yearSelectButton");
	}

	@Test
	public void testEditable()
	{
		scene = stage.getScene();

		TextField comment = (TextField) scene.lookup("#commentField");
		assertTrue(comment.isDisable());

		Button enter = (Button) scene.lookup("#commentButton");
		assertTrue(enter.isDisable());
	}

	@Test
	public void testAddDeleteComment()
	{
		scene = stage.getScene();

		TextField comment = (TextField) scene.lookup("#commentField");
		ListView<String> commentArea = (ListView<String>) scene.lookup("#commentArea");
		selectYear(1);
		typeDown(2);
		typeDown(-1);

		clickOn(comment);
		write("This mission is awful");
		clickOn("#commentButton");

		clickOn(commentArea);
		typeDown(1);

		assertTrue(commentArea.getItems().contains("user: This mission is awful"));

		clickOn("#deleteButton");

		assertFalse(commentArea.getItems().contains("user: This mission is awful"));
	}

	@Test
	public void testSwitchPlan()
	{
		scene = stage.getScene();

		TextField comment = (TextField) scene.lookup("#commentField");
		ListView<String> commentArea = (ListView<String>) scene.lookup("#commentArea");

		selectYear(1);
		typeDown(2);

		clickOn(comment);
		write("This goal is awful");
		clickOn("#commentButton");

		// go to different plan
		selectYear(1);
		typeDown(2);

		assertFalse(commentArea.getItems().contains("user: This goal is awful"));

		// return to original plan
		selectYear(1);
		typeDown(2);

		assertTrue(commentArea.getItems().contains("user: This goal is awful"));

		clickOn(commentArea);
		typeDown(1);
		clickOn("#deleteButton");
		clickOn(commentArea);
		typeDown(1);
		clickOn("#deleteButton");
	}

	@Test
	public void testLogoutLogin()
	{
		scene = stage.getScene();

		TextField comment = (TextField) scene.lookup("#commentField");

		selectYear(1);
		typeDown(2);

		clickOn(comment);
		write("This goal does not make sense");
		clickOn("#commentButton");

		logout();
		login();

		scene = stage.getScene();

		selectYear(1);
		typeDown(2);

		ListView<String> commentArea = (ListView<String>) scene.lookup("#commentArea");

		assertTrue(commentArea.getItems().contains("user: This goal does not make sense"));

		clickOn(commentArea);
		typeDown(1);
		clickOn("#deleteButton");
	}

	@Test
	public void testEmptyComment()
	{
		scene = stage.getScene();

		TextField comment = (TextField) scene.lookup("#commentField");
		Label error = (Label) scene.lookup("#commentError");

		selectYear(1);
		typeDown(2);

		assertFalse(error.isVisible());

		clickOn(comment);
		clickOn("#commentButton");

		assertTrue(error.isVisible());

		clickOn(comment);
		write(" ");
		clickOn("#commentButton");

		assertFalse(error.isVisible());

		ListView<String> commentArea = (ListView<String>) scene.lookup("#commentArea");
		clickOn(commentArea);
		typeDown(1);
		clickOn("#deleteButton");
	}
}