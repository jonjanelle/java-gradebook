/**
 * Score objects represent percentage grades on particular assignments
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * @author Jon Janelle
 * @version Modified 4/20/2016
 */
public class Score {
	private double score; //A 0.0-100.0 percentage score
	private int id; 	  //An assignment id number
	
	/**
	 * Default constructor sets score and assignment id to 0
	 */
	public Score() 
	{
		score = 0;
		id = 0;
	}
	
	/**
	 * Constructor -- initializes Score object given
	 * an Assignment identification number and percentage
	 * score.
	 * @param id Assignment ID number
	 * @param score Percentage score
	 */
	public Score(int id,double score)
	{
		this.score = score;
		this.id = id;
	}
	
	/**
	 * Get the percentage score for this assignment
	 * @return Percentage score on assignment
	 */
	public double getScore() { return score; }
	
	/**
	 * Get assignment ID number 
	 * @return The assignment ID number
	 */
	public int getID() { return id; }
	
	/**
	 * Set percentage score on assignment
	 * @param newScore A percentage score
	 */
	public void setScore(double newScore) { score = newScore; }
	
	/**
	 * Set assignment ID number for score
	 * @param newID An assignment ID number
	 */
	public void setID(int newID) { id = newID; }

	/**
	 * @return assignment ID number and score as a String.
	 */
	public String toString()
	{
		return "Assignment ID: "+id+" Score: "+score;
	}
	
	/**
	 * Determine whether this score is equal to another score. 
	 * Scores are equal if they have the same assignment id number
	 * and the same percentage score.
	 * @param s A score object
	 * @return true if s is equal to this score, false otherwise.
	 */
	public boolean equals(Score s)
	{
		return (s.getID()==id&&s.getScore()==score);
	}
}
