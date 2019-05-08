package software_masters.planner_networking;

import java.rmi.RemoteException;

/**
 * @author Courtney and Jack
 * @author wesley and lee.
 */
public class IowaState extends Plan
{

	private static final long serialVersionUID = 3096239674948462908L;

	/**
	 * @throws RemoteException
	 */
	public IowaState() throws RemoteException
	{
		super();
	}

	public IowaState(Node root) throws RemoteException
	{
		super(root);

	}

	// set strings for default stages IowaState plan
	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Plan#setDefaultStrings()
	 */
	protected void setDefaultStrings()
	{
		getList().add("Vision");
		getList().add("Mission");
		getList().add("Core Value");
		getList().add("Strategy");
		getList().add("Goal");
		getList().add("Objective");
		getList().add("Action Plan");
		getList().add("Assessment");
	}

	/**
	 * Takes a Node parent and returns a boolean true if added
	 * 
	 * @param parent parent of node to be added
	 * @return boolean true if added
	 */
	public boolean addNode(Node parent) throws RemoteException, IllegalArgumentException
	{
		if ((parent.getName() == "Vision") || (parent == null))
		{
			throw new IllegalArgumentException("Cannot add to this parent");
		} else
		{

			for (int i = (getList().indexOf(parent.getName())) + 1; i < getList().size(); i++)
			{

				Node newNode = new Node(parent, getList().get(i), null, null, null);

				parent.addChild(newNode);
				parent = newNode;

			}
			return true;
		}
	}

	/**
	 * Takes a Node nodeRemove and returns a boolean true if removed
	 * 
	 * @param nodeRemove node to be removed
	 * @return boolean true if removed
	 * 
	 */
	public boolean removeNode(Node nodeRemove) throws IllegalArgumentException
	{
		if ((nodeRemove.getName() == getRoot().getName()) || (nodeRemove.getParent().getChildren().size() == 1)
				|| (nodeRemove == null))
		{

			throw new IllegalArgumentException("Cannot remove this node");

		} else
		{
			nodeRemove.getParent().removeChild(nodeRemove);
			nodeRemove.setParent(null);
			return true;

		}
	}
}
