/**
 * Panel containing a histogram based on data provided in an array of 
 * double values. The histogram is drawn to fit within the dimensions
 * panelWidth by panelHeight, which must be passed to the constructor
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * @author Jon Janelle
 * @version Modified 4/30/2016
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class HistogramPanel extends JPanel{

	private int min; //minimum bin value
	private int max; //maximum bin value
	private int binWidth = 5;
	private int numBins;
	private int[] binCounts;
	private double[] values;
	private int panelWidth;
	private int panelHeight;
	
	
	/**
	 * Constructor sets the panel width, panel height, and data array of doubles.
	 * Then, the number of bins required and frequency counts for each bin are 
	 * calculated. 
	 * 
	 * @param values The array of doubles to represent with a histogram
	 * @param panelWidth The width of the panel on which the histogram is be drawn
	 * @param panelHeight The height of the panel on which the histogram is to be drawn 
	 */
	public HistogramPanel(double[] values, int panelWidth, int panelHeight)
	{
		this.values = values;
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		
		min = 10*(((int)Stats.getMin(values))/10); 
		max = 10*(((int)(Stats.getMax(values)+10))/10);
		numBins = (max-min)/binWidth; //bins are 5 points wide
		binTally();

	}

	/**
	 * Make a frequency count array using the values array.
	 * The counts are stored in binCounts upon completion
	 */
	private void binTally()
	{
		binCounts = new int[numBins];
		Arrays.sort(values); //sort values in ascending order
		
		for (int i = 0; i < binCounts.length; i++)
		{
			for (double x: values){
				int binMin = min + binWidth*i;
				int binMax = binMin+binWidth;
				
				if (x>=binMin && x < binMax){
					binCounts[i]++;
				}
				else if (x >= binMax){
					break;
				}
			}
		}
	}

	/**
	 * Draw a histogram to represent the count data in binCounts.
	 * The histogram is drawn to fit within the dimensions of 
	 * panelWidth by panelHeight
	 */
	public void paintComponent(Graphics g)
	{
		int barWidth = panelWidth/(numBins+4);
		int maxCount = Stats.getMax(binCounts);
		int barUnit = ((int)(.45*panelHeight))/(maxCount+1); //how much height each additional count should have
		
		super.paintComponent(g);
				
		for (int i = 0; i < binCounts.length; i++){
			int upperY = (int)(.45*panelHeight)-barUnit*binCounts[i]; //each count adds 25px height
			int upperX = barWidth*(1+i);
			g.setColor(Color.blue);
			g.fillRect(upperX, upperY, barWidth, barUnit*binCounts[i]);
			g.setColor(Color.black);
			g.drawRect(upperX, upperY, barWidth, barUnit*binCounts[i]);
			if (binCounts[i]>0)
				g.drawString(""+binCounts[i], upperX+barWidth/2, upperY);
			String labelText = ""+(min+binWidth*i);
			g.drawString(labelText, upperX-5, (int)(.45*panelHeight)+20);
		}
	}
	
}
