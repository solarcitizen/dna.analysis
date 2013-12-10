package edu.tau.compbio.gui.motif.test;

import java.awt.Color;

import javax.swing.JFrame;

import edu.tau.compbio.gui.motif.MotifLogoComponent;

public class MotifLogoComponentTest {

	// Check the motif logo component - create a simple window with a motif logo
	public static void main(String[] argv)
	{
		
		// Set motif
		//String[] motif = new String[] { "A", "C", "CG", "ACGT", "AGT", "AT"};
		double[][] motif = {
				{0.3, 0.3, 0.2, 0.2},
				{0.2, 0.2, 0.2, 0.4},
				{0.3, 0.1, 0.1, 0.3},
				{0.3, 0.2, 0.4, 0.1},
				{0.3, 0.2, 0.5, 0.0},
				{0.4, 0.6, 0.0, 0.0}
		};

		// Create a frame
		JFrame frame = new JFrame("Motif logo");
		
		// Create motif logo component
		MotifLogoComponent logos = new MotifLogoComponent();
		logos.setMotifLength(6);
		logos.setPreferences (60, 56, Color.WHITE);
		try {
			logos.addMotif(motif);
		} catch (Exception e) {
			
		}
		frame.add(logos);

		// Display the window
		frame.pack();
		frame.setVisible(true);
		
		//System.out.println("Done.");
	}
 

}
