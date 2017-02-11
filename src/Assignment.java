/**
 * Model for an academic course assignment
 * 
 * An assignment consists of a name,
 * integer identification number, and
 * an optional description
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * 
 * @author Jon Janelle
 * @version Modified 5/2/2016
 */
public class Assignment {
	
	private String name; //assignment name 
	private String desc; //Course description
	private int id;		 //assignment id number

	/**
	 * Construct a new assignment by name and 
	 * identification number
	 * @param name The assignment name
	 * @param id The assignment identification number
	 */
	public Assignment(String name, int id)
	{
		this.name = name;
		this.id = id;
		desc = "";
	}
	
	/**
	 * Construct a new assignment by name, 
	 * identification number, and description
	 * 
	 * @param name The assignment name
	 * @param id The assignment identification number
	 * @param desc The course description
	 */
	public Assignment(String name, int id, String desc)
	{
		this(name, id);
		this.desc = desc;
	}
	
	
	/**  
	 * @return The assignment name
	 */
	public String getName() { return name; }
	
	/**
	 * Get assignment identification number
	 * @return The assignment id number
	 */
	public int getID() { return id; }
	
	/**
	 * @return The assignment description
	 */
	public String getDescription() { return desc; }
	
	/**
	 * Set the assignment name
	 * @param newName The new assignment name
	 */
	public void setName(String newName) { name = newName; }
	
	/**
	 * Set the assignment id number
	 * @param newID The new assignment id number
	 */
	public void setID(int newID) 		{ id = newID; }
	
	/**
	 * Create a String description of the assignment
	 * @return A String containing the assignment name, description, and id number
	 */
	public String toString()
	{
		return "Name: "+name+"\tID: "+id+"\nDescription: "+desc;
	}
	
	/**
	 * Compare two assignments. Two assignments are equal if they have the
	 * same name, identification number, and description
	 * @param other The assignment with which to compare this assignment
	 * @return true if the assignments are equal, false otherwise.
	 */
	public boolean equals(Assignment other)
	{
		return (this.name.equals(other.getName()) &&
				this.id == other.getID() && 
				this.desc.equals(other.getDescription()));
	}
	
}
