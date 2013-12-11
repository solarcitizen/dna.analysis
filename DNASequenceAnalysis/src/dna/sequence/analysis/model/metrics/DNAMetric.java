package dna.sequence.analysis.model.metrics;


import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;

import dna.sequence.analysis.controller.main.UserDecisions;
import edu.tau.compbio.gui.motif.MotifLogoComponent;

// This is a base class for a DNA metric
// The two basic needs left for concrete implementation are converting to
// normalized weighted probability matrix and updating the metric with a new motif
//
// Note: no requriement for the metric to be normalized at all time (i.e. to
// have proper probabilities, if that's the case)
//
// In addition the class supplies two utility methods:
// 1. Displaying the metric as a motif logo (using 3rd party library)
// 2. Printing the metric to a file (for future reference)
public abstract class DNAMetric {

	// For ease of certain methods and interoperability with 3rd party libraries,
	// the metric needs to be sometimes represented as a normalized weighted
	// probability matrix of the following format:
	//
	// metricStructure[i][j] is the probability (or weight) of the j'th base
	// (0 = A, 1 = C, 2 = G, 3 = T) at position i (0 <= i <MOTIFLENGTH)
	abstract public double[][] toDoubleNormalizedMatrix();
	
	abstract public void updateMetricWithMotif(String motif);
	
	// Class service utility for representing the motif logo
	// Interoperates with a supplied 3rd party library
	public static final void displayMotif(DNAMetric metric) throws Exception {
		
		MotifLogoComponent logos = new MotifLogoComponent();
		
		//String[] motif = new String[] { "A", "C", "CG", "ACGT", "AGT", "AT", "T" };
		
		/*double[][] motif = {
				{0.3, 0.3, 0.2, 0.2},
				{0.2, 0.2, 0.2, 0.4},
				{0.3, 0.1, 0.1, 0.3},
				{0.3, 0.2, 0.4, 0.1},
				{0.3, 0.2, 0.5, 0.0},
				{0.4, 0.6, 0.0, 0.0}
		};*/
		
		/*double[][] motif = {
				{0.34071003675348005, 0.24494096866915036, 0.201119471732517, 0.21322952284485255},
				{0.3397215247028539, 0.2429495857124198, 0.20236165689659, 0.21496723268813633},
				{0.34147739936330873, 0.24143948391159442, 0.20217458811277064, 0.21490852861232618},
				{0.3416468223247333, 0.2405934647601105, 0.20209126061812854, 0.21566845229702764},
				{0.33997242984416026, 0.2410466625320083, 0.20146944721001384, 0.2175114604138176},
				{0.33852276210173277, 0.24068884446678043, 0.2010337222622521, 0.2197546711692347}
		};*/
		
		logos.setMotifLength(UserDecisions.motifLength);
		logos.setPreferences(60, 56, Color.WHITE);
		logos.addMotif(metric.toDoubleNormalizedMatrix());
		//logos.addMotif(motif);
				
		JFrame guiFrame = new JFrame("Motif Logo");
		guiFrame.add(logos);
		guiFrame.pack();
		guiFrame.setVisible(true);
		
	}
	
	// Class service utility for writing a metric to a file
	// The metric is written as a normalzied weighted probability matrix according to a specified format:
	//
	// metricStructure[i][j] is the probability (or weight) of the j'th base
	// (0 = A, 1 = C, 2 = G, 3 = T) at position i (0 <= i <MOTIFLENGTH)
	public static final void writeMetricToFile(DNAMetric metric, String targetFilename) {
		
		double[][] normalizedDoubleMatrixMetric = metric.toDoubleNormalizedMatrix();
		
		try (PrintWriter pw = new PrintWriter(new FileWriter(targetFilename, /*appendToFile*/ false))) { 
			
			// The column headers for clarity
			pw.println("Position" + '\t' + "A" + '\t' + "C" + '\t' + "G" + '\t' + "T");
			
			for(int i = 0; i < UserDecisions.motifLength; i++) {
				
				pw.print(i + 1);
				pw.print('\t');
				
				for (int j = 0; j < 4; j++) {
					
					pw.print(normalizedDoubleMatrixMetric[i][j]);
					pw.print('\t');
					
				}
				
				pw.println();
				
			}
	           
	   	} catch (FileNotFoundException e) {
	       	System.out.println("File " + targetFilename + " not found");
	   	} catch (IOException e) {
	       	System.out.println("Unable to read file " + targetFilename);
	   	}
		
	}

}
