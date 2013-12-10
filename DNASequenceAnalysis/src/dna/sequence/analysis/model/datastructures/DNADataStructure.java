package dna.sequence.analysis.model.datastructures;


import java.util.Collection;
import java.util.Iterator;

import dna.sequence.analysis.model.sequences.DNASequence;

// This is the base wrapper class for the data structure used to store and sort
// DNA sequences 
//
// The problem domain demands the ability to add sequences to a data structure,
// which will either be kept sorted at all times or can be sorted, and the ability
// to iterate over the sequences in a sorted way
public abstract class DNADataStructure {
	
	// The required data structure needs to be a Collection of DNASequences
	// Required for generality and interoperability
	protected Collection<DNASequence> dataStructure;

	// Required method
	abstract public void add(DNASequence sequence);
	
	// If the concrete data structure is not being kept sorted at all times,
	// use this method to sort it (and normalize it otherwise)
	//
	// Otherwise - leave the method as it is
	public void normalize() {}
	
	// Required method
	abstract public Iterator<DNASequence> sortedIterator();

}
