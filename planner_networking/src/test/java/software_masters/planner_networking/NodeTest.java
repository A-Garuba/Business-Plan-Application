package software_masters.planner_networking;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import org.junit.Test;

public class NodeTest
{

	@Test
	public void test() throws RemoteException
	{
		// make node +
		// add child +
		// remove child
		// get name, data +
		// set name, data +
		// get child when no child

		// make tree node and test data methods
		Node tree = new Node(null, "TreeNode", null, null, null);
		assertEquals("TreeNode", tree.getName());
		tree.setName("Tree");
		assertEquals("Tree", tree.getName());

		tree.setData("Desciption of mission goals.");
		assertEquals("Desciption of mission goals.", tree.getData());
		tree.setData("Description");
		assertEquals("Description", tree.getData());
		assertEquals(null, tree.getParent());
		assertEquals(true, tree.getChildren().isEmpty());

		// make child nodes for tree, test addChild and getParent
		Node n1 = new Node(tree, "Vision", null, null, null);
		tree.addChild(n1);
		assertEquals(tree, n1.getParent());
		assertEquals("Vision", n1.getName());
		assertEquals(true, tree.getChildren().contains(n1));

		Node n2 = new Node(tree, "node", null, null, null);
		tree.addChild(n2);
		assertEquals(true, tree.getChildren().contains(n2));
		assertEquals(tree, n2.getParent());

		// add child to n2
		Node n3 = new Node(n2, "node3", null, null, null);
		n2.addChild(n3);
		assertEquals(true, n2.getChildren().contains(n3));
		assertEquals(n2, n3.getParent());

		// add child to n3
		Node n4 = new Node(n3, "node3", null, null, null);
		n3.addChild(n4);

		// get grandparent
		assertEquals(n2, n4.getParent().getParent());

		// remove child from tree with no other children
		tree.removeChild(n1);
		assertEquals(false, tree.getChildren().contains(n1));

		// remove child that has children
		tree.removeChild(n2);
		assertEquals(false, tree.getChildren().contains(n2));

	}

}