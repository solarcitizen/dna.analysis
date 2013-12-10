package dna.sequence.analysis.model.sequences;

//This is a concrete class representing a PBM DNA sequence
public class PBMSequence extends DNASequence {
	
	public PBMSequence(String newSequence) {
		sequence = newSequence;
	}

	@Override
	public String toString() {
		return sequence; // according to format
	}

}
