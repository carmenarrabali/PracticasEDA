import java.util.LinkedList;
import java.util.List;

import es.uma.ada.problem.factorization.IntegerFactorization;

/**
 * Factorizes an integer n by exhaustively checking all prime
 * numbers less than or equal to sqrt(n).
 * @author ccottap
 *
 */
public class BruteForceFactorization extends IntegerFactorization {

	private int i, maxdiv;
	
	public BruteForceFactorization () {
		super();
	}
 
	@Override
	public List<Long> _factorize(long n) {
		List<Long> factors = new LinkedList<Long>();
		maxdiv = (int) Math.floor(Math.sqrt(n));
		i = 2;

		while(i <= maxdiv) {
            if(n % i == 0){
                do{
                    factors.add((long) i);
                    n = n/i;
                } while (n % i <= 0);
                maxdiv = (int) Math.floor(Math.sqrt(n));
			}
            i++;
		}
		if (n > 1) {
            factors.add(n);
		}
		return factors;
	}

	@Override
	public String getName() {
		return "Brute-force factorization";
	}

}
