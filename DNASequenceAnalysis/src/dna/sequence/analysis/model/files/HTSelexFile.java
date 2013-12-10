package dna.sequence.analysis.model.files;


import java.io.IOException;

import dna.sequence.analysis.model.sequences.DNASequence;
import dna.sequence.analysis.model.sequences.HTSelexSequence;

// This is a concrete class representing a HT Selex data file
public class HTSelexFile extends DNADataFile {

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
			
			// Split the line according to format and create a sequence object			
			String[] sequenceParts = readLine.split("\\t");
			
			// HTSelexSequence = sequence string + number of BF occurances
			return new HTSelexSequence(sequenceParts[0],
				Integer.valueOf(sequenceParts[1]).intValue());
		}

	}

}
