package dna.sequence.analysis.controller.main;

import dna.sequence.analysis.model.metrics.DNAMetric;


public class HTSelexAnalysisWorkshopMain {

	public static void main(String[] args) {
		
		// Make sure the format is correct
		if (args.length < 2) {
			
			System.out.println("Wrong input format. " +
					"Input should be in the following format: " +
					"java HTSelexAnalysisWorkshop PBMFilename HTSelexFilename1 [HTselexFilename2] ... " +
					"(at least one HTSelex filename)");
			
			return;
			
		}
		
		/*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));*/

		// Read and process all the input files - read file, update metric accordingly, close file
		for (int i = 1; i < args.length; i++) {
			
			UserDecisions.inputSourceDataFile.openForReading(args[i]);
			UserDecisions.rankingStrategy.updateMetricWithFile(UserDecisions.inputSourceDataFile);
			UserDecisions.inputSourceDataFile.close();
			
		}
		
		// Read and process the target file - read it, score the sequences according to
		// previously generated metric and construct a sorted data structure, close file
		UserDecisions.inputTargetDataFile.openForReading(args[0]);
		UserDecisions.rankingStrategy.scoreFileIntoDataStructure(UserDecisions.inputTargetDataFile, UserDecisions.dataStructure);
		UserDecisions.inputTargetDataFile.close();
		
		// Write the sorted data structure to a file - open file, write the data structure to it, close file
		String[] tokens = args[0].split("_");
		UserDecisions.outputTargetDataFile.openForWriting(
				tokens[0] + "_" + tokens[1] + ".pbm"); // Current requirement for the output is TF_<num>.pbm
		/*UserDecisions.outputTargetDataFile.openForWriting(
				args[0].replace(".pbm", "_motiflen_" + UserDecisions.motifLength + "_ranked.pbm"));*/
		UserDecisions.outputTargetDataFile.writeDataStructureToFile(UserDecisions.dataStructure);
		UserDecisions.outputTargetDataFile.close();
		
		// Write the result metric to file for future use
		DNAMetric.writeMetricToFile(UserDecisions.metric,
				tokens[0] + "_" + tokens[1] + ".metric"); // Current requirement for the output is TF_<num>.metric
		/*DNAMetric.writeMetricToFile(UserDecisions.metric,
				args[0].replace(".pbm", "_motiflen_" + UserDecisions.motifLength + ".metric"));*/
		
		
		/*// Display the motif logo
		try {
			DNAMetric.displayMotif(UserDecisions.metric);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}*/
		
		
	}

}
