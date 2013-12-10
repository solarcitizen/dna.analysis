package dna.sequence.analysis.model.sequences;

// This is a concrete class representing an HT Selex DNA sequence
public class HTSelexSequence extends DNASequence {
	
	private int motifOccurance = 0;
	
	public HTSelexSequence(String newSequence, int newMotifOccurance) {
		sequence = newSequence;
		motifOccurance = newMotifOccurance;
	}
	
	@Override
	public String toString() {
		return new String(sequence + '\t' + motifOccurance); // according to format
	}

}
