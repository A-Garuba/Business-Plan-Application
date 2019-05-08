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

public class CompareTest extends ApplicationTest
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
		clickOn("#compareButton");
	}

	@After
	public void logout()
	{
		clickOn("#logoutButton");
		scene = stage.getScene();
		clickOn("#yes");
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

	public void selectYear(int left, int right)
	{
		clickOn("#yearDropdown_left");
		if (left == 1)
		{
			typeDown(2);
			typeDown(-1);
		} else
		{
			typeDown(left);
		}
		type(KeyCode.ENTER);

		clickOn("#yearDropdown_right");
		if (right == 1)
		{
			typeDown(2);
			typeDown(-1);
		} else
		{
			typeDown(right);
		}
		type(KeyCode.ENTER);

		clickOn("#compareButton");
	}

	@Test
	public void testSelected()
	{
		scene = stage.getScene();

		Label left_error = (Label) scene.lookup("#leftError");
		Label right_error = (Label) scene.lookup("#rightError");

		assertFalse(left_error.isVisible());
		assertFalse(right_error.isVisible());

		clickOn("#compareButton");

		assertTrue(left_error.isVisible());
		assertTrue(right_error.isVisible());

		selectYear(1, 2);

		assertFalse(left_error.isVisible());
		assertFalse(right_error.isVisible());

		clickOn("#returnButton");
		clickOn("#compareButton");

		assertFalse(left_error.isVisible());
		assertFalse(right_error.isVisible());
	}

	@Test
	public void testCompare()
	{
		scene = stage.getScene();

		selectYear(1, 3);

		TreeView right = (TreeView) scene.lookup("#tree_right");
		TreeView content_right = (TreeView) scene.lookup("#tree_content_right");

		clickOn(right);
		typeDown(5);

		TreeItem curr = (TreeItem) right.getSelectionModel().getSelectedItem();
		assertNotNull(curr.getGraphic());

		typeDown(1);
		curr = (TreeItem) right.getSelectionModel().getSelectedItem();
		assertNotNull(curr.getGraphic());

		typeDown(1);
		curr = (TreeItem) right.getSelectionModel().getSelectedItem();
		assertNotNull(curr.getGraphic());
	}
}