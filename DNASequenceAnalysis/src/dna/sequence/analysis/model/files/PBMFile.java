package dna.sequence.analysis.model.files;


import java.io.IOException;

import dna.sequence.analysis.model.sequences.DNASequence;
import dna.sequence.analysis.model.sequences.PBMSequence;

// This is a concrete class representing a PBM data file
public class PBMFile extends DNADataFile {

	@Override
	public DNASequence readSequence() {
		
		String readLine = null;
		
		try {
			readLine = fileReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (readLine == null) {
			return null;
		} else {
			return new PBMSequence(readLine);
		}
		
	}
	
}
