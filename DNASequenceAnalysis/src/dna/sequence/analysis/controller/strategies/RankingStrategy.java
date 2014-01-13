package dna.sequence.analysis.controller.strategies;


import java.util.Iterator;

import dna.sequence.analysis.model.datastructures.DNADataStructure;
import dna.sequence.analysis.model.files.DNADataFile;
import dna.sequence.analysis.model.metrics.DNAMetric;
import dna.sequence.analysis.model.sequences.DNASequence;

// This is the base class for all ranking strategies
public abstract class RankingStrategy {
	
	// The metric generated and used by the ranking strategy
	protected DNAMetric metric = null;
	
	// The metric may be stored in whatever format but for easy calculation we need
	// the metric to be weighted probability matrix of the specified format:
	//
	// metricStructure[i][j] is the probability (or weight) of the j'th base
	// (0 = A, 1 = C, 2 = G, 3 = T) at position i (0 <= i <MOTIFLENGTH)
	//
	// Note: each cell in the matrix is a double
	protected double[][] positionWeightMatrix = null;
		
	public DNAMetric getMetric() {
		return metric;
	}
	
	public double[][] getPositionWeightMatrix() {
		return positionWeightMatrix;
	}
	
	abstract public void updateMetricWithSequence(DNASequence sequence);
	
	public void updateMetricWithDataStructure(DNADataStructure dataStructure) {
		
		for (Iterator<DNASequence> i = dataStructure.sortedIterator(); i.hasNext(); ) {
			
			updateMetricWithSequence(i.next());
			
		}
	}
	
	public void updateMetricWithFile(DNADataFile inputDataFile) {
		
		DNASequence readSequence = null;
		
		while ((readSequence = inputDataFile.readSequence()) != null) {
			
			updateMetricWithSequence(readSequence);
			
		}
		
	}
	
	abstract public double scoreSequence(DNASequence sequence);
	
	public void scoreDataStructure(DNADataStructure dataStructure) {
		
		for (Iterator<DNASequence> i = dataStructure.sortedIterator(); i.hasNext(); ) {
			
			DNASequence currSequence = i.next();
			currSequence.setScore(scoreSequence(currSequence));
		}
		
		// In case the data structure needs to be re-sorted or treated otherwise
		dataStructure.normalize();
		
	}
	
	public void scoreFileIntoDataStructure(DNADataFile sourceDataFile, DNADataStructure targetDataStructure) {
		
		DNASequence readSequence = null;
		
		while ((readSequence = sourceDataFile.readSequence()) != null) {
			
			readSequence.setScore(scoreSequence(readSequence));
			
			targetDataStructure.add(readSequence);
			
		}
		
		// In case the data structure needs to be sorted or treated otherwise
		targetDataStructure.normalize();
		
	}
	
}
