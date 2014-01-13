package dna.sequence.analysis.controller.main;

import dna.sequence.analysis.model.metrics.DNAMetric;


public class HTSelexAnalysisWorkshopMain {

	public static void main(String[] args) {
		
		// Make sure the format is correct
		if (args.length < 2) {
			
			System.out.println("Wrong input format. " +
					"Input should be in the following format: " +
					"java -jar HTSelex.jar [-nl] PBMFilename HTSelexFilename1 [HTselexFilename2] ... " +
					"(at least one HTSelex filename)");
			
			return;
			
		}
		
		/*System.out.println();
		System.out.print("Start:  ");
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));*/
		
		// Helper variables in case the logo display is disabled via the command line
		Boolean showLogo = true; // show, by default
		int argsOffset = 0; // if no flag - no offset required
		
		if (args[0].equals("-nl")) { // nl = no logo
			showLogo = false;
			argsOffset = 1;
		}

		// Read and process all the input files - read file, update metric accordingly, close file
		for (int i = 1 + argsOffset; i < args.length; i++) {
			
			UserDecisions.inputSourceDataFile.openForReading(args[i]);
			UserDecisions.rankingStrategy.updateMetricWithFile(UserDecisions.inputSourceDataFile);
			UserDecisions.inputSourceDataFile.close();
			
		}
		
		// Read and process the target file - read it, score the sequences according to
		// previously generated metric and construct a sorted data structure, close file
		UserDecisions.inputTargetDataFile.openForReading(args[0 + argsOffset]);
		UserDecisions.rankingStrategy.scoreFileIntoDataStructure(UserDecisions.inputTargetDataFile, UserDecisions.dataStructure);
		UserDecisions.inputTargetDataFile.close();
		
		// Write the sorted data structure to a file - open file, write the data structure to it, close file
		String[] tokens = args[0 + argsOffset].split("_");
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
		
		/*dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		cal = Calendar.getInstance();
		System.out.print("Finish: ");
		System.out.println(dateFormat.format(cal.getTime()));
		System.out.println();*/
		
		// If not disabled via command line, display the motif logo 
		if (showLogo) {
			
			try {
				DNAMetric.displayMotifLogo(UserDecisions.metric, tokens[0] + "_" + tokens[1]);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		
	}

}
