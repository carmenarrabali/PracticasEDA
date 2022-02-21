

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import es.uma.eda.problem.algebra.diophantine.RationalNumber;
import es.uma.eda.problem.algebra.diophantine.approximation.DiophantineApproximator;


/**
 * Tests the binary search approximation algorithm
 * @author ccottap
 *
 */
public class TestBinarySearchApproximation {
	/**
	 * filename for statistics
	 */
	private final static String out_filename = "stats.txt";
    /**
     * class-level random generator;
     */
    protected static Random r =  new Random(1); 
    
    
	/**
	 * Main method to test the binary search approximation 
	 * @param args command-line args
	 * @throws IOException is statistics cannot be written to file
	 */
	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			BinarySearchDiophantineApproximator bsa = new MediantApproximation();
			switch (args[0]) {
			case "-n":
				if (args.length > 2) {
					double x = Double.parseDouble(args[1]);
					double epsilon = Double.parseDouble(args[2]);
					RationalNumber f = bsa.approximate(x, epsilon);
					System.out.println(x + " = " + f + " with error " + Math.abs(x-f.asDouble()) + " < " + epsilon);		
				}
				else {
					System.out.println("TestBinarySearchApproximation -n <x> <epsilon>");
					System.exit(1);
				}
				break;
				
			case "-f":
    			if (args.length > 1) {
    				Scanner inputFile = new Scanner (new File(args[1]));
    				inputFile.useLocale(Locale.US);
    				bsa.setVerbosity(false);
    				while (inputFile.hasNext()) {
    					double x = inputFile.nextDouble();
    					double epsilon = inputFile.nextDouble();
    					RationalNumber f = bsa.approximate(x, epsilon);
    					System.out.println(x + "\t" + epsilon + "\t" + f);
    				}
    				inputFile.close();
    			}
    			else {
					System.out.println("TestBinarySearchApproximation -f <filename>");
					System.exit(1);   				
    			}
    			break;
    			
			case "-r":
				if (args.length > 2) {
					int maxDigits = Integer.parseInt(args[1]);
					int numtests = Integer.parseInt(args[2]);
					RunBatch(bsa, maxDigits, numtests);	
				}
				else {
					System.out.println("TestBinarySearchApproximation -r <maxdigits> <numtests>");
					System.exit(1);
				}
				break;
			default:
				System.out.println("Unknown parameter: " + args[0]);
				System.exit(1);
			}
		}
		else {
			System.out.println("TestBinarySearchApproximation (-n|-f|-r) <parameters>");
			System.exit(1);
		}
	}
	
	/**
	 * Runs a batch of tests for rational approximation with different precisions (from 1 to maxDigits) 
	 * @param da the algorithm for approximation
	 * @param maxDigits maximum number of precision digits 
	 * @param numtests number of tests per precision value
	 * @throws IOException if the data cannot be written to file
	 */
	private static void RunBatch (DiophantineApproximator da, int maxDigits, int numtests) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(da.getName() + "-" + out_filename));
		da.setVerbosity(false);
		double base = 10.0;
		for (int i=1; i<=maxDigits; i++, base*=10) {
			System.out.println("Trying with " + i + " digits of precision...");
			out.print(i);
			double epsilon = 0.5/base;
			for (int j=0; j<numtests; j++) {
				double x = r.nextDouble();
				da.approximate(x, epsilon);
				out.print("\t" + da.getTime());
			}
			out.println();
		}
		out.close();	
	}

}
