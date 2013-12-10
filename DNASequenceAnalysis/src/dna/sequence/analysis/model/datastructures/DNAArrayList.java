package dna.sequence.analysis.model.datastructures;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import dna.sequence.analysis.model.sequences.DNASequence;

// This is a concrete class for a data structure obeying the requirements
public class DNAArrayList extends DNADataStructure {
	
	public DNAArrayList() {
		
		// This collection was chosen since the add operation is amortized O(1)
		// and the sort is relatively fast in practice (even though O(n log n) )
		dataStructure = new ArrayList<DNASequence>();
		
	}

	@Override
	public void add(DNASequence sequence) {
		
		// Just use the collection method
		dataStructure.add(sequence);
		
	}

	@Override
	public Iterator<DNASequence> sortedIterator() {
		
		// The collection method is enough, if the collection is sorted beforehand
		return dataStructure.iterator();
		
	}
	
	
	@Override
	public void normalize() {
		
		// Since the collection is not normally sorted, normalization will require sorting
		//
		// Collection method is more than enough for this
		Collections.sort((ArrayList<DNASequence>) dataStructure);
		
	}

}
