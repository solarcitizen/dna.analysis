package dna.sequence.analysis.model.sequences;

// This is a concrete class representing an HT Selex DNA sequence
public class HTSelexSequence extends DNASequence {
	
	private int oligoOccurance = 0;
	
	public HTSelexSequence(String newSequence, int newOligoOccurance) {
		sequence = newSequence;
		oligoOccurance = newOligoOccurance;
	}
	
	public int getOligoOccurance() {
		return oligoOccurance;
	}
	
	@Override
	public String toString() {
		return new String(sequence + '\t' + oligoOccurance); // according to format
	}

}
