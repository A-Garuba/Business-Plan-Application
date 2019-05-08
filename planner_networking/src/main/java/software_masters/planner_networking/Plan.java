package software_masters.planner_networking;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Courtney and Jack
 * @author wesley and lee.
 *
 */
public abstract class Plan implements Serializable// extends UnicastRemoteObject
{
	private static final long serialVersionUID = 1538776243780396317L;
	private String name;
	private ArrayList<String> defaultNodes = new ArrayList<String>();
	private Node root;

	/**
	 * @throws RemoteException
	 */
	public Plan() throws RemoteException
	{
		defaultNodes = new ArrayList<String>();
		setDefaultStrings();
		addDefaultNodes();
	}

	public Plan(Node root) throws RemoteException
	{
		this.root = root;
		defaultNodes = new ArrayList<String>();
		setDefaultStrings();
	}

	// creates string array node hierarchy in subclass
	abstract protected void setDefaultStrings();

	public ArrayList<String> getDefaultNodes()
	{
		return defaultNodes;
	}

	public void setDefaultNode()
	{
		setDefaultStrings();
	}

	/**
	 * This class builds default template based on string array
	 * 
	 * @throws RemoteException
	 */
	protected void addDefaultNodes() throws RemoteException
	{
		root = new Node(null, defaultNodes.get(0), null, null, null);
		Node newParent = new Node(root, defaultNodes.get(1), "Insert Content", null, null);
		root.addChild(newParent);
		addNode(newParent);
	}

	/**
	 * @param parent
	 * @return
	 */
	abstract public boolean addNode(Node parent) throws RemoteException, IllegalArgumentException;

	/**
	 * @param Node
	 * @return
	 */
	abstract public boolean removeNode(Node Node) throws IllegalArgumentException;

	/**
	 * Takes a Node node and String data Sets data for the node
	 * 
	 * @param node node to set data for
	 * @param data data to set in node
	 * 
	 */
	public void setNodeData(Node node, String data)
	{
		node.setData(data);
	}

	/**
	 * returns the root node
	 * 
	 * @return Node root node
	 * 
	 */
	public Node getRoot()
	{
		return root;
	}

	public void setRoot(Node root)
	{
		this.root = root;
	}

	/**
	 * returns a list of default node strings
	 * 
	 * @return ArrayList list of default node strings
	 */
	public ArrayList<String> getList()
	{
		return defaultNodes;
	}

	/**
	 * returns a String name of plan
	 * 
	 * @return String strings of plan name
	 * 
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Takes a String name and sets name of plan
	 * 
	 * @param name name to set as plan name
	 * 
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Plan other = (Plan) obj;
		if (defaultNodes == null)
		{
			if (other.defaultNodes != null)
			{
				return false;
			}
		} else if (!defaultNodes.equals(other.defaultNodes))
		{
			return false;
		}
		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		} else if (!name.equals(other.name))
		{
			return false;
		}
		if (root == null)
		{
			if (other.root != null)
			{
				return false;
			}
		} else if (!root.equals(other.root))
		{
			return false;
		}
		return true;
	}

}