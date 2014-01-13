package dna.sequence.analysis.model.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import dna.sequence.analysis.controller.main.UserDecisions;

// This is a concrete DNA metric class represented as a HashMap<String, Double>,
// where the motifs are the keys and the representation intensity is the value.
//
// The class allows for querying the metric for the score of a motif (the representation
// intensity) for updating the metric with a given sequence and for deriving a normalized
// PWM, which takes into consideration the UserDecisions.numOfTopMotifsToConsider of over-
// represented motifs
public class MotifHashMapMetric extends DNAMetric {
	
	private HashMap<String, Double> metricStructure = new HashMap<String, Double>();

	// This method returns a standard PWM representing the top UserDecisions.numOfTopMotifsToConsider motifs
	// 
	// This method requires some explanation, since it juggles contradictory needs and requirements
	//
	// In order to have the add and lookup operations working fast (as they are the bulk of the operations),
	// the metric uses HashMap. However, HashMap has two drawbacks: (1) It has no meaningful iteration order,
	// much less actually a sorted one (2) Looking up the key of a given value is cumbersome and requires
	// iterating over the map until we hit an equal value
	//
	// To work around these two issues, the method constructs a complementary metric where each key-value pair
	// (motif - representation intensity) is reversed - String-Double to Double-String. The values of the
	// original metric are sorted by descending order into a new ArrayList and then the top 
	// UserDecisions.numOfTopMotifsToConsider values (i.e. intensities) are matched to the corresponding keys
	// (i.e. motifs) using the reversed metric
	//
	// All in all, the performance hit of this procedure is rather small and acceptable in practice
	@Override
	public double[][] toDoubleNormalizedMatrix() {
		
		// Required to normalize the matrix to probabilities
		double normalizationFactor = 0;
		
		// Initialize the matrix to be used
		//
		// normalizedMatrix[i][j] is the probability (or weight) of the j'th base
		// (0 = A, 1 = C, 2 = G, 3 = T) at position i (0 <= i < MOTIFLENGTH)
		double[][] normalizedMatrix = new double[UserDecisions.motifLength][4];
		for(int i = 0; i < UserDecisions.motifLength; i++) {
			for (int j = 0; j < 4; j++) {
				normalizedMatrix[i][j] = 0;
			}
		}
		
		// Get the list of values of the metric (representation intensities) and sort them in a descending order
		ArrayList<Double> valuesList = new ArrayList<Double>();
		valuesList.addAll(metricStructure.values());
		Collections.sort(valuesList, Collections.reverseOrder());
		
		// Create the complementary structure <String, Double> --> <Double, String>
		HashMap<Double, String> reversedKeyValueMetricStructure = new HashMap<Double, String>();
		Iterator<Entry<String, Double>> entries = metricStructure.entrySet().iterator(); // get all the entries of the metric
		while (entries.hasNext()) {
			Entry<String, Double> entry = entries.next();
			reversedKeyValueMetricStructure.put(entry.getValue(), entry.getKey()); // push each entry in reverse order 
		}
		
		// Go over the top UserDecisions.numOfTopMotifsToConsider values
		// and update the normalized matrix with corresponding motifs
		Iterator<Double> values = valuesList.iterator();
		for (int i = 0; i < UserDecisions.numOfTopMotifsToConsider && values.hasNext(); i++) {
			
			Double value = values.next();
			String key = reversedKeyValueMetricStructure.get(value); // the key is the motif in question
			
			for(int j = 0; j < UserDecisions.motifLength; j++) {
				
				switch (key.charAt(j)) {
					case 'A':
						normalizedMatrix[j][0] += value.doubleValue();
						break;
					case 'C':
						normalizedMatrix[j][1] += value.doubleValue();
						break;
					case 'G':
						normalizedMatrix[j][2] += value.doubleValue();
						break;
					case 'T':
						normalizedMatrix[j][3] += value.doubleValue();
						break;
				}
				
			}
			
			normalizationFactor += value.doubleValue();
			
		}
		
		// Normalize the matrix to represent probabilities
		for(int i = 0; i < UserDecisions.motifLength; i++) {
			for (int j = 0; j < 4; j++) {
				normalizedMatrix[i][j] /= normalizationFactor;
			}
		}
		
		return normalizedMatrix;
		
	}

	@Override
	public void updateMetricWithMotifAndScore(String motif, double motifScore) {
		
		double updatedScore = motifScore;
		
		// If the motif already in the metric, add the new score to the existing one
		if (metricStructure.containsKey(motif)) {
			updatedScore += metricStructure.get(motif).doubleValue();
		} 
		
		// Insert the new motif with it's score or update the existing motif with the new value
		metricStructure.put(motif, new Double(updatedScore));
		
	}
	
	@Override
	public double getMotifScore(String motif) {
		if (metricStructure.containsKey(motif)) {
			return metricStructure.get(motif).doubleValue(); // precisely the info kept in the metric
		} else {
			return (double) 0; // if the motif is not present, it's score is 0
		}
	}

}
