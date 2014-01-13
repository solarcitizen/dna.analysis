package dna.sequence.analysis.model.metrics;

import dna.sequence.analysis.controller.main.UserDecisions;

// This is a concrete DNA metric class represented as a regular probability
// matrix, weighted and normalized when required to be used or represented
public class ProbabilityMatrixMetric extends DNAMetric {
	
	// metricStructure[i][j] is the probability (or weight) of the j'th base
	// (0 = A, 1 = C, 2 = G, 3 = T) at position i (0 <= i < MOTIFLENGTH)
	private double[][] metricStructure = null;
	
	// For normalization needs and keeping track
	private long numOfMotifs = (long) 0;
	
	public ProbabilityMatrixMetric() {
		
		metricStructure = new double[UserDecisions.motifLength][4];
		
		for(int i = 0; i < UserDecisions.motifLength; i++) {
			for (int j = 0; j < 4; j++) {
				metricStructure[i][j] = 0;
			}
		}
		
	}

	@Override
	public double[][] toDoubleNormalizedMatrix() {
		
		double convertedNumOfMotifs = (double) numOfMotifs;
		
		double[][] weightedMetric = new double[UserDecisions.motifLength][4];
		
		// Adjust each cell according to the number of motifs represented in the metric
		for(int i = 0; i < UserDecisions.motifLength; i++) {
			for (int j = 0; j < 4; j++) {
				 weightedMetric[i][j] = metricStructure[i][j] / convertedNumOfMotifs;
			}
		}
		
		return weightedMetric;
		
	}

	@Override
	public void updateMetricWithMotifAndScore(String motif, double motifScore) {
		
		// Each letter in the motif increases the appropriate cell by motif score
		//
		// Note: the metric is not kept normalized during its lifecycle (for ease of
		// repeated updates) and being normalized to a new matrix only when required
		for(int i = 0; i < UserDecisions.motifLength; i++) {
			
			switch(motif.charAt(i)) {
				case 'A':
					metricStructure[i][0] += motifScore;
					break;
				case 'C':
					metricStructure[i][1] += motifScore;
					break;
				case 'G':
					metricStructure[i][2] += motifScore;
					break;
				case 'T':
					metricStructure[i][3] += motifScore;
					break;
			}
			
		}
		
		numOfMotifs++;
		
	}

	// Motif score is simply the sum of the probabilities of each letter of the motif
	@Override
	public double getMotifScore(String motif) {
		
		double score = 0;
		
		for(int i = 0; i < UserDecisions.motifLength; i++) {
			
			switch(motif.charAt(i)) {
				case 'A':
					score += metricStructure[i][0];
					break;
				case 'C':
					score += metricStructure[i][1];
					break;
				case 'G':
					score += metricStructure[i][2];
					break;
				case 'T':
					score += metricStructure[i][3];
					break;
			}
			
		}
		
		score /= (double) numOfMotifs;
		
		return score;
		
	}

}
