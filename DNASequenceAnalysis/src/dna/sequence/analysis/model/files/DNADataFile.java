package dna.sequence.analysis.model.files;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import dna.sequence.analysis.model.datastructures.DNADataStructure;
import dna.sequence.analysis.model.sequences.DNASequence;

// This is the base class for DNA sequences data files representations
//
// The basic needs are opening and closing files and reading and writing sequences
public abstract class DNADataFile {
	
	// Stored for ease of identification
	protected String filename = null;

	// Used if the file is for reading
	protected BufferedReader fileReader = null;
	
	// Used if the file is for writing
	protected PrintWriter fileWriter = null;
	private final boolean appendToFile = false; // Overwrite files whenever already exist
	
	public void openForReading(String newFilename) {
		
		try { 
			
			// Try opening file for structured reading
			FileReader fr = new FileReader(newFilename);
			BufferedReader br = new BufferedReader(fr);
			
			filename = newFilename;
			fileReader = br;
	           
	   	} catch (FileNotFoundException e) {
	       	System.out.println("File " + newFilename + " not found");
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   	}
		
	}
	
	public void openForWriting(String newFilename) {
		
		try {
			
			// Try opening the file for text writing
			FileWriter fw = new FileWriter(newFilename, appendToFile);
			PrintWriter pw = new PrintWriter(fw);
			
			filename = newFilename;
			fileWriter = pw;
	           
	   	} catch (FileNotFoundException e) {
	       	System.out.println("File " + newFilename + " not found");
	   	} catch (IOException e) {
	       	System.out.println("Unable to read file " + newFilename);
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   	}
		
	}
	
	// Reading and parsing sequences depends on the sequence type in question, thus
	// implementation is left for concrete classes
	abstract public DNASequence readSequence();
	
	public void writeSequence(DNASequence DNASequence) {
		fileWriter.printf("%s" + "%n", DNASequence.toString());
	}

	public void close() {
		
		if (fileReader != null) {
			
			try {
				
				fileReader.close();
				
			} catch (IOException e) {
				System.out.println("Unable to close file" + filename);
			}
			
		}
		
		if (fileWriter != null) {
			
			try {
				
				fileWriter.close();
				
			} catch (Exception e) {
				System.out.println("Unable to close file: " + filename);
			}
			
		}
		
	}
	
	// Service utility
	public void readIntoDataStructure(DNADataStructure dataStructure) {
		
		DNASequence readSequenceLine;
		
		while ((readSequenceLine = readSequence()) != null) {
			dataStructure.add(readSequenceLine);
		}
		
		dataStructure.normalize();
		
	}
	
	// Service utility
	public void writeDataStructureToFile(DNADataStructure dataStructure) {
		
		for (Iterator<DNASequence> i = dataStructure.sortedIterator(); i.hasNext(); ) {
			writeSequence(i.next());
		}
		
	}

}
