package dna.sequence.analysis.controller.strategies;

import dna.sequence.analysis.controller.main.UserDecisions;
import dna.sequence.analysis.model.metrics.DNAMetric;
import dna.sequence.analysis.model.sequences.DNASequence;

// This is a concrete ranking strategy
//
// This ranking strategy naively (no mathematical treatment of data) goes over
// all data, exhausting all possible motif variations, and scores sequences
// by going over all possible motifs in them, adding the probabilities for each
// letter in the motif for each motif
public class NaiveExhaustiveAnalysisRankingStrategy extends RankingStrategy {
	
	public NaiveExhaustiveAnalysisRankingStrategy(DNAMetric metricToUse) {
		
		metric = metricToUse;
		
	}

	@Override
	public void updateMetricWithSequence(DNASequence sequence) {
		
		// In this strategy we use just the string information of the sequence
		String sequenceString = sequence.getSequence();
		
		// Go over all substrings of length Decisions.motifLength and treat them as motifs
		for (int i = 0; i < sequenceString.length() - UserDecisions.motifLength + 1; i++) {
			
			metric.updateMetricWithMotifAndScore(sequenceString.substring(i, i + UserDecisions.motifLength), 1);
			
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
		
		// For each substring of length Decisions.motifLength and for each letter in the
		// substring - add the probability/weight at the appropriate cell in the metric matrix 
		for (int i = 0; i < sequenceString.length() - UserDecisions.motifLength + 1; i++) {
			
			for(int j = 0; j < UserDecisions.motifLength; j++) {
				
				switch (sequenceString.charAt(i + j)) {
					case 'A':
						score += positionWeightMatrix[j][0];
						break;
					case 'C':
						score += positionWeightMatrix[j][1];
						break;
					case 'G':
						score += positionWeightMatrix[j][2];
						break;
					case 'T':
						score += positionWeightMatrix[j][3];
						break;
				}
				
			}
			
		}
		
		return score;
		
	}

}
