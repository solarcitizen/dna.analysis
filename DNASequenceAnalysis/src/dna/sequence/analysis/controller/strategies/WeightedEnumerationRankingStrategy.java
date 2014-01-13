package dna.sequence.analysis.controller.strategies;

import dna.sequence.analysis.controller.main.UserDecisions;
import dna.sequence.analysis.model.metrics.DNAMetric;
import dna.sequence.analysis.model.sequences.DNASequence;
import dna.sequence.analysis.model.sequences.HTSelexSequence;

// This is a concrete ranking strategy
//
// This ranking strategy goes over all the data, exhausting all possible motif variations and updating
// the metric according to the number of oligo occurances, and scores sequences by going over all the
// possible motifs in them, checking vs the motif score in the metric
public class WeightedEnumerationRankingStrategy extends RankingStrategy {
	
	public WeightedEnumerationRankingStrategy(DNAMetric metricToUse) {
		
		metric = metricToUse;
		
	}

	@Override
	public void updateMetricWithSequence(DNASequence sequence) {
		
		String sequenceString = sequence.getSequence();
		
		// The default score if no other information present
		int motifScore = 1;
		
		// For generality purpose - if it's a HT-SELEX sequence, we have additional info;
		// otherwise - use the default score
		if (sequence.getClass().getName().equals("HTSelexSequence")) {
			motifScore = ((HTSelexSequence) sequence).getOligoOccurance(); // each possible motif is repeated in each occurance
		}
		
		// Go over all substrings of length Decisions.motifLength and treat them as motifs
		for (int i = 0; i < sequenceString.length() - UserDecisions.motifLength + 1; i++) {
			
			metric.updateMetricWithMotifAndScore(sequenceString.substring(i, i + UserDecisions.motifLength), motifScore);
			
		}
		
		// The current ranking strategy's PWM is no longer up to date
		positionWeightMatrix = null;

	}

	@Override
	public double scoreSequence(DNASequence sequence) {
		
		// If the PWM is not up to date - get the current one		
		if (positionWeightMatrix == null) {
			positionWeightMatrix = metric.toDoubleNormalizedMatrix();
		}
		
		// In this strategy we use just the string information of the sequence
		String sequenceString = sequence.getSequence();
				
		// The overall score
		double score = 0;
		
		// For each substring of length Decisions.motifLength add the score of that substring according to the metric  
		for (int i = 0; i < sequenceString.length() - UserDecisions.motifLength + 1; i++) {
			
			score += metric.getMotifScore(sequenceString.substring(i, i + UserDecisions.motifLength));
			
		}
		
		return score;
		
	}

}
