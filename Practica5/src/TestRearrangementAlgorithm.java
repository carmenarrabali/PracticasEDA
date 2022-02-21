

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import es.uma.ada.problem.combinatorial.sequence.rearrangement.Permutation;
import es.uma.ada.problem.combinatorial.sequence.rearrangement.SequenceRearrangementAlgorithm;

/**
 * Main class to test rearrangement algorithms
 * 
 * @author ccottap
 *
 */
public class TestRearrangementAlgorithm {
	/**
	 * Filename for storing statistics
	 */
	private final static String outputFilename = "rearrangement.txt";
	/**
	 * default value for the scale factor used to increase the size of permutations
	 */
	private static final double defaultScale = 1.5;

	/**
	 * Main method to test rearrangement algorithms
	 * 
	 * @param args command-line arguments
	 * @throws IOException is stats cannot be written to file
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.out.println("java TestRearrangementAlgorithm (-g|-r) <algorithm> <arguments>");
			System.out.println("\t-g: generates a permutation at random with specified parameters");
			System.out.println("\t-r: performs tests with permutations of increasing sizes");
		} else {
			boolean signed;
			SequenceRearrangementAlgorithm sra;
			
			switch (args[0].toLowerCase()) {
			case "-g":
				if (args.length < 3) {
					System.out.println("java TestRearrangementAlgorithm -g <algorithm> <lenght> [<random seed>]");
					System.exit(-1);
				} else {
					sra = getMethod(args[1].toLowerCase());
					if (sra == null) {
						System.out.println("Wrong method: " + args[1]);
						System.exit(1);
					}
					if (args.length > 3) {
						Permutation.setSeed(Integer.parseInt(args[3]));
					}

					Permutation l = new Permutation(Integer.parseUnsignedInt(args[2]), sra.isSignedCompatible());
					sra.setVerbosity(true);
					System.out.println("Rearranging " + l);
					sra.run(l);
					System.out.println(sra.getName() + " took " + sra.getTime() + "s to perform "
							+ sra.getNumOperations() + " operations");
					System.out.println("Result: " + l);
					System.out.println("Is identity: " + (l.isIdentity() ? "yes" : "no"));
				}
				break;
				
			case "-f":
				if (args.length < 3) {
					System.out.println("java TestRearrangementAlgorithm -f <algorithm> <file>");
					System.exit(-1);
				} else {
					sra = getMethod(args[1].toLowerCase());
					if (sra == null) {
						System.out.println("Wrong method: " + args[1]);
						System.exit(1);
					}
					signed = sra.isSignedCompatible();
					sra.setVerbosity(false);
					Scanner inputFile = new Scanner(new File(args[2]));
					int n = inputFile.nextInt();
					while (n>0) {
						Permutation p = new Permutation(n, signed);
						for (int i=0; i<n; i++)
							p.set(i, inputFile.nextInt());
						sra.run(p);
						System.out.println(sra.getNumOperations());
						n = inputFile.nextInt();
					}
				}
				break;

			case "-r":
				if (args.length < 5) {
					System.out.println(
							"java TestRearrangementAlgorithm -r <algorithm> <initial-lenght> <num-doublings> <numtests> [<random seed>]");
					System.exit(-1);
				} else {
					sra = getMethod(args[1].toLowerCase());
					if (sra == null) {
						System.out.println("Wrong method: " + args[1]);
						System.exit(1);
					}
					sra.setVerbosity(false);
					if (args.length > 5) {
						Permutation.setSeed(Integer.parseInt(args[5]));
					}
					int initial = Integer.parseInt(args[2]);
					int doublings = Integer.parseInt(args[3]);
					int numtests = Integer.parseInt(args[4]);
					signed = sra.isSignedCompatible();
					PrintWriter out = new PrintWriter(new FileWriter(sra.getName() + outputFilename));

					for (int i = initial, k = 0; k < doublings; i *= defaultScale, k++) {
						System.out.print("\n" + i + "\t");
						out.print(i);
						for (int j = 0; j < numtests; j++) {
							Permutation p = new Permutation(i, signed);
							sra.run(p);
							System.out.print(".");
							if ((j - 1) % 100 == 99)
								System.out.print("\n  \t");
							out.print("\t" + sra.getNumOperations() + "\t" + (p.isIdentity() ? 1 : 0));
						}
						out.println();
					}
					out.close();
				}
				break;

			default:
				System.out.println("Wrong argument: " + args[0]);
				System.exit(-1);
			}
		}
	}

	/**
	 * Factory method to create a sequence rearrangement algorithm
	 * 
	 * @param name string with the name of the method
	 * @return a sequence rearrangement algorithm (or null if the name could not be
	 *         matched).
	 */
	private static SequenceRearrangementAlgorithm getMethod(String name) {
		switch (name.toLowerCase()) {
		case "prefixsort":
			return new PrefixSort();
		case "breakpointreversal":
			return new BreakpointReversalAlgorithm();
		default:
			return null;
		}
	}

}
