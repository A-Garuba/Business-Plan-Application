package software_masters.planner_networking;

import static org.junit.Assert.assertTrue;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class MainGUITest extends ApplicationTest
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

	public void editableButtons()
	{
		clickOn("#tree");
		type(KeyCode.DOWN);
		Button add = (Button) scene.lookup("#addChildButton");
		Button remove = (Button) scene.lookup("#removeButton");
		Button save = (Button) scene.lookup("#saveButton");
		Button copy = (Button) scene.lookup("#copyButton");
		Button submityear = (Button) scene.lookup("#enterNewYearButton");
		Button edit = (Button) scene.lookup("#editButton");
		assertTrue(!(add.isDisabled()));
		assertTrue(!(remove.isDisabled()));
		assertTrue(!(save.isDisabled()));
		assertTrue(!(copy.isDisabled()));
		assertTrue((submityear.isDisabled()));
		assertTrue(!(edit.isDisabled()));
		clickOn("#tree");
		type(KeyCode.UP);
	}

	public void viewButtons()
	{
		Button add = (Button) scene.lookup("#addChildButton");
		Button remove = (Button) scene.lookup("#removeButton");
		Button save = (Button) scene.lookup("#saveButton");
		Button copy = (Button) scene.lookup("#copyButton");
		Button submityear = (Button) scene.lookup("#enterNewYearButton");
		assertTrue((add.isDisabled()));
		assertTrue((remove.isDisabled()));
		assertTrue((save.isDisabled()));
		assertTrue((copy.isDisabled()));
		assertTrue((submityear.isDisabled()));
	}

	public void selectYear(int index)
	{
		clickOn("#yearDropdown");
		typeDown(index);
		type(KeyCode.ENTER);
		clickOn("#yearSelectButton");
	}

	@Test
	public void testSelectYear()
	{
		scene = stage.getScene();
		// some buttons should be unclickable
		TextField field = (TextField) scene.lookup("#newYearTxtField");
		assertTrue(!(field.isEditable()));
		viewButtons();

		// check first year is correct
		selectYear(1);
		clickOn("#tree");
		typeDown(10);
		field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Git abilities"));

		// and the second year
		selectYear(1);
		clickOn("#tree");
		typeDown(10);
		field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Passing sprint 2"));

		// and first again
		selectYear(-1);
		clickOn("#tree");
		typeDown(10);
		field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Git abilities"));
	}

	@Test
	public void testTextEdit()
	{
		scene = stage.getScene();
		selectYear(1);
		clickOn("#editButton");

		// check clicking edit disables the right buttons
		editableButtons();
		clickOn("#editButton");
		viewButtons();
		clickOn("#editButton");

		// when viewing a noneditable plan, clicking edit shouldn't do anything
		selectYear(3);
		clickOn("#editButton");
		Button button = (Button) scene.lookup("#editButton");
		assertTrue(button.getText().equals("Edit"));
		clickOn("#editButton");
		button = (Button) scene.lookup("#editButton");
		assertTrue(button.getText().equals("Edit"));

		selectYear(-2);
		clickOn("#editButton");
		clickOn("#tree");
		typeDown(1);
		clickOn("#contentField");
		clickOn("#contentField");
		clickOn("#contentField");
		write("first edit");
		clickOn("#tree");
		typeDown(-1);
		typeDown(1);
		TextField field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("first edit"));
	}

	@Test
	public void testAddBranches()
	{
		scene = stage.getScene();
		selectYear(2);
		clickOn("#editButton");
		clickOn("#tree");
		typeDown(1);
		clickOn("#addChildButton");
		clickOn("#tree");
		typeDown(20);
		TextField field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Insert Content"));
		type(KeyCode.RIGHT);
		typeDown(1);
		field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Insert Content"));
		clickOn("#contentField");
		clickOn("#contentField");
		clickOn("#contentField");
		write("first edit");
		clickOn("#tree");
		typeDown(20);
		typeDown(-1);
		field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("first edit"));
		typeDown(-7);
		field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Our goal is to ensure that " + "I get credit for this class."));
	}

	@Test
	public void testRemoveBranch()
	{
		scene = stage.getScene();
		selectYear(3);
		clickOn("#editButton");

		clickOn("#tree");
		typeDown(4);
		clickOn("#removeButton");
		clickOn("#tree");
		typeDown(-1);
		typeDown(1);
		TextField field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Result 2018"));
		typeDown(-2);
		clickOn("#addChildButton");
		clickOn("#tree");
		typeDown(3);
		clickOn("#removeButton");
		clickOn("#tree");
		typeDown(20);
		field = (TextField) scene.lookup("#contentField");
		System.out.println(field.getText());
		assertTrue(field.getText().equals("Result 2018"));
	}

	@Test
	public void testCopy()
	{
		scene = stage.getScene();
		selectYear(2);
		clickOn("#editButton");
		clickOn("#copyButton");
		clickOn("#newYearTxtField");
		write("2020");
		clickOn("#enterNewYearButton");
		selectYear(8);
		clickOn("#editButton");
		clickOn("#tree");
		typeDown(20);
		TextField field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Passing sprint 2"));
		clickOn("#contentField");
		clickOn("#contentField");
		clickOn("#contentField");
		write("edit 1");
		selectYear(-6);
		clickOn("#tree");
		typeDown(20);
		field = (TextField) scene.lookup("#contentField");
		assertTrue(field.getText().equals("Passing sprint 2"));

	}
}