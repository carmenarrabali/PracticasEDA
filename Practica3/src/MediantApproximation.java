

import es.uma.eda.problem.algebra.diophantine.RationalNumber;

/**
 * Approximates a real number to an irreducible rational fraction using
 * the mediant approximation - see: Farey sequence, Stern-Brocot tree 
 * @author ccottap
 *
 */
public class MediantApproximation extends BinarySearchDiophantineApproximator {

	@Override
	public String getName() {
		return "Mediant-Approximation";
	}


	@Override
	protected RationalNumber getMiddlePoint(RationalNumber l, RationalNumber r) {
		// TODO
		// Return the mediant of l and r.
		//
		RationalNumber M = new  RationalNumber(l.numerator() + r.numerator(), l.denominator() + r.denominator());
		return M;
	}

}
