package dna.sequence.analysis.model.sequences;

// This is a base class representing DNA sequences
// Basic needs are accessors, toString and compareTo
//
// Implements Comparable interface for ability to integrate with collections
//
// Note: this class has a natural ordering that is inconsistent with equals - the ordering is by score alone
public abstract class DNASequence implements Comparable<DNASequence> {
	
	// The base characteristics of DNA sequence - sequence string and a score
	protected String sequence = null;
	protected double score = 0;
	
	public String getSequence() {
		return sequence;
	}
	
	public void setSequence(String newSequence) {
		sequence = newSequence;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double newScore) {
		score = newScore;
	}
	
	// Sequence is represented according to its type thus implementation is left for concrete classes
	@Override
	abstract public String toString();
	
	@Override
	public int compareTo(DNASequence otherSequence) throws NullPointerException, ClassCastException {
		
		// Values assigned to produce a descending ordering in the data structure sort
		if (score > otherSequence.getScore()) {
			return -1;
		} else if (score < otherSequence.getScore()) {
			return 1;
		} else {
			return 0;
		}
	}

}
