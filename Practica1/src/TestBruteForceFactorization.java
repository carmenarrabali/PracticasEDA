import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * Test the factorization of a number by brute force
 * @author ccottap
 *
 */
public class TestBruteForceFactorization {
    /**
     * class-level random generator;
     */
    protected static Random r =  new Random(1); 
    /**
     * Minimal number of digits in batch test
     */
    private final static int minDigits = 6;
    /**
     * Maximal number of digits in batch test
     */
    private final static int maxDigits = 15;
    /**
     * basename for batch test
     */
    private final static String basename = "digits.txt";
    /**
     * basename for batch test
     */
    private final static String stats_filename = "stats.txt";
    
    
	/**
	 * Main method to test brute force factorization
	 * @param args command-line args
	 * @throws IOException if data cannot be read/written to file
	 */
	public static void main(String[] args) throws IOException {
		
		if (args.length < 1) {
			System.out.println("TestBruteForceFactorization (-n <num>) | (-f <input-file>) | -t");
			System.exit(1);
		}
		else {
			BruteForceFactorization bf = new BruteForceFactorization();
			if (args[0].equals("-n")){
				if (args.length < 2) {
					System.out.println("Number to be factorized is missing");
					System.exit(1);
				}
				else {
					long n = Long.parseLong(args[1]);
					List<Long> factors = bf.factorize(n);
					System.out.println(bf.getName() + " took " +  bf.getTime() + " seconds");
					System.out.println("The factorization of " + n + " is " + factors);
				}
			}
			else if (args[0].equals("-f")){
				if (args.length < 2) {
					System.out.println("Input filename is missing");
					System.exit(1);
				}
				else {
					Scanner inputFile = new Scanner (new File(args[1]));
					
					while (inputFile.hasNext()) {
						long n = inputFile.nextLong();
						System.out.println(String.format("%18d", n) + "\t" + bf.factorize(n));
					}
					inputFile.close();
				}
			}
			else if (args[0].equals("-t")){
		     	PrintWriter outputFile = new PrintWriter(new FileWriter(stats_filename));

				for (int i=minDigits; i<=maxDigits; i++) {
					System.out.println("Trying " + i + " digits...");
					Scanner inputFile = new Scanner (new File(i + basename));
					outputFile.print(String.format("%2d", i));
					
					while (inputFile.hasNext()) {
						long n = inputFile.nextLong();
						bf.factorize(n);
						outputFile.print("\t" + bf.getTime());
					}
					outputFile.println();
					inputFile.close();
				}
				outputFile.close();
				
			}
			else {
				System.err.println("Unknown option " + args[0] + ". Must be in {-n, -f, -t}.");
				System.exit(1);
			}
		}

	}
}
