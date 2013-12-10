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
		
	public DNAMetric getMetric() {
		return metric;
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
