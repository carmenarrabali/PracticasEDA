

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

import es.uma.ada.problem.combinatorial.puzzle.latinsquare.LatinSquare;

/**
 * Main class for testing the resolution of Latin squares using backtracking
 * 
 * @author ccottap
 *
 */
public class TestLatinSquareBacktracking {
	/**
	 * name of the file to which the solved Latin squares will be written
	 */
	private final static String outputFilename = "solution_";
	/**
	 * name of the file to which resolution statistics will be written
	 */
	private final static String outputStatsFilename = "stats_";

	/**
	 * Main method
	 * 
	 * @param args command line arguments
	 * @throws IOException           if there is a problem writing solutions or
	 *                               stats
	 * @throws FileNotFoundException if there is a problem reading data from file
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		if (args.length < 1) {
			System.err.println("You must provide a filename to read the Latin squares(s).");
			System.exit(1);
		}
		Scanner inputFile = new Scanner(new File(args[0]));
		PrintWriter outputFile = new PrintWriter(new FileWriter(outputFilename + args[0]));
		PrintWriter outputStats = new PrintWriter(new FileWriter(outputStatsFilename + args[0]));
		LatinSquareBacktracking solver = new LatinSquareBacktracking();
		solver.setVerbosity(false);

		inputFile.useLocale(Locale.US);
		int order = inputFile.nextInt();
		LatinSquare latinSquare = new LatinSquare(order);
		int numInstances = inputFile.nextInt();
		double perc = inputFile.nextDouble();
		while (perc >= 0) {
			outputStats.print(perc);
			System.out.print("\n" + perc + "\t");
			for (int i = 0; i < numInstances; i++) {
				latinSquare.read(inputFile);
				solver.setPuzzle(latinSquare);
				solver.solve();
				System.out.print(".");
				solver.getPuzzle().write(outputFile);
				outputStats.print("\t" + solver.getNodes() + "\t" + (solver.hasSol() ? 1 : 0));
			}
			outputStats.println();
			perc = inputFile.nextDouble();
		}

		outputStats.close();
		outputFile.close();
	}

}
