package software_masters.planner_networking;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Courtney and Jack
 * @author lee and wesley
 */

public class Node implements Serializable
{
	private static final long serialVersionUID = 5908372020728915437L;
	
	private Node parent;
	private String name;
	private String data;
	
	private ArrayList<Node> children = new ArrayList<Node>();
	private ArrayList<String> comments = new ArrayList<String>();


	/**
	 * Takes a Node parent, String name, String data, and list of children Sets
	 * values in node
	 * 
	 * @param parent parent of node
	 * @param name   name of node
	 * @param data   data for node
	 * @param child  list of children
	 */
	public Node(Node parent, String name, String data, ArrayList<Node> child, ArrayList<String> comments) throws RemoteException
	{
		this.name = name;
		this.parent = parent;
		this.data = data;
	}

	/**
	 * empty constructor for XML
	 * 
	 * @throws RemoteException
	 */
	public Node() throws RemoteException
	{
		this(null, "blank", "empty", null, null);
	}

	/**
	 * returns a String name of node
	 * 
	 * @return String name of node
	 */
	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return name;
	}

	/**
	 * Sets name of node
	 * 
	 * @param name name to set as name of node
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns node's data
	 * 
	 * @return String data of node
	 */
	public String getData()
	{
		return data;
	}

	/**
	 * Takes a String data and sets node's data
	 * 
	 * @param data data to set as data of node
	 */
	public void setData(String data)
	{
		this.data = data;
	}

	/**
	 * returns the parent node
	 * 
	 * @return Node parent of node
	 */
	public Node getParent()
	{
		return parent;
	}

	/**
	 * Takes a Node parent and sets the nodes parent
	 * 
	 * @param parent parent to set as parent of node
	 */
	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	/**
	 * Returns a list of children nodes
	 * 
	 * @return ArrayList of children
	 */
	public ArrayList<Node> getChildren()
	{
		return children;
	}
	
	/**
	 * Setter for children ArrayList of Nodes
	 * 
	 * @param children
	 */
	public void setChildren(ArrayList<Node> children)
	{
		this.children = children;
	}

	/**
	 * Getter for comment ArrayList of Strings
	 * 
	 * @return ArrayList of comments
	 */
	public ArrayList<String> getComments()
	{
		return comments;
	}

	public void setComments(ArrayList<String> comments)
	{
		this.comments = comments;
	}

	/**
	 * Takes a node child and adds child to child list
	 * 
	 * @param child child to be added to this node
	 */
	public void addChild(Node child)
	{
		children.add(child);
	}
	
	/**
	 * remove child node from a node's children list
	 * 
	 * @param child child to be removed from this node
	 */
	public void removeChild(Node child)
	{
		if (children.size() > 1)
		{
			children.remove(child);
		}
	}

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
		Node other = (Node) obj;
		if (children == null)
		{
			if (other.children != null)
			{
				return false;
			}
		} else if (!children.equals(other.children))
		{
			return false;
		}
		if (data == null)
		{
			if (other.data != null)
			{
				return false;
			}
		} else if (!data.equals(other.data))
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
		if (parent == null)
		{
			if (other.parent != null)
			{
				return false;
			}
		}
		return true;
	}

}
