package dna.sequence.analysis.controller.main;

import dna.sequence.analysis.controller.strategies.NaiveExhaustiveAnalysisRankingStrategy;
import dna.sequence.analysis.controller.strategies.RankingStrategy;
import dna.sequence.analysis.model.datastructures.DNAArrayList;
import dna.sequence.analysis.model.datastructures.DNADataStructure;
import dna.sequence.analysis.model.files.DNADataFile;
import dna.sequence.analysis.model.files.HTSelexFile;
import dna.sequence.analysis.model.files.PBMFile;
import dna.sequence.analysis.model.metrics.DNAMetric;
import dna.sequence.analysis.model.metrics.ProbabilityMatrixMetric;

// Collection of decisions to be made - for easy modification
public class UserDecisions {
	
	// The length of the motif to be searched for
	public static final int motifLength = 6;
	
	// The metric type used by ranking strategy
	public static final DNAMetric metric = new ProbabilityMatrixMetric();
	
	// The ranking strategy of choice
	public static final RankingStrategy rankingStrategy = new NaiveExhaustiveAnalysisRankingStrategy(metric);
	
	// The data structure to be used for scoring and sorting
	public static final DNADataStructure dataStructure = new DNAArrayList();
	
	// The DNA type of the training files 
	public static final DNADataFile inputSourceDataFile = new HTSelexFile();
	
	// The DNA type of the file to be ranked and sorted 
	public static final DNADataFile inputTargetDataFile = new PBMFile();
	
	// The DNA type of the output to be written
	public static final DNADataFile outputTargetDataFile = new PBMFile();
	
	// TODO get these instructions via command line
	// TODO get these instructions via interaction with user
	// TODO make these decisions dynamic via reflection

}
