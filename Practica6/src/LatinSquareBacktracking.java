
import es.uma.ada.backtracking.Backtracking;
import es.uma.ada.datastructures.tuple.Pair;
import es.uma.ada.problem.combinatorial.puzzle.latinsquare.LatinSquare;

/**
 * Backtracking for latinSquare
 * 
 * @author ccottap
 *
 */
public class LatinSquareBacktracking extends Backtracking {
	/**
	 * The Latin square to solve
	 */
	protected LatinSquare latinSquare;

	/**
	 * Creates the solver
	 */
	
	public LatinSquareBacktracking() {
		super();
		latinSquare = null;
	}

	/**
	 * Creates the solver with a specific puzzle
	 * 
	 * @param latinSquare a Latin square puzzle
	 */
	public LatinSquareBacktracking(LatinSquare latinSquare) {
		super();
		setPuzzle(latinSquare);
	}

	@Override
	protected Object initialState() {		
		// The initial state is a pair with the coordinates of the 
		// first position, namely the upper left corner (0,0)
		
		Pair<Integer, Integer> initialLS = new Pair<Integer, Integer>(0, 0);
		return initialLS;
	}

	@Override
	protected boolean backtracking(Object state) {
		@SuppressWarnings("unchecked")
		Pair<Integer, Integer> p = (Pair<Integer, Integer>) state; // Par
		int i = p.getFirst(); 						//Primer valor del par
		int j = p.getSecond(); 						//Segundo valor del par
		boolean ok;
		int n = latinSquare.getSize();				//Tamaño del cuadrado latino
		
		
		/**
		 * El algoritmo debe intentar llenar la tabla fila por fila, de izquierda a derecha.
		 * Si una posición no está especificada, el algoritmo debe comprobar qué valores son factibles para esa posición y continuar recursivamente.
		 * Si una posición es fija, el algoritmo debe seguir comprobando si ese valor es factible, porque la instancia inicial podría ser irresoluble.
		 */

		if(i == n & j == 0) {
			ok = true;
		} else {
			ok = false;
			if(latinSquare.isFixed(i, j)) {
				boolean value = latinSquare.test(i, j, latinSquare.get(i, j));
				if(value) {
					this.nodes++;
					if(j < n-1) {
						ok = backtracking(new Pair<Integer, Integer>(i, j+1));
					} else {
						ok = backtracking(new Pair<Integer, Integer>(i+1, 0));
					}
				}
			} else {
				int k = 1;
				while(!ok && k <= n) {
					boolean value2 = latinSquare.test(i, j, k);
					if(value2) {
						this.nodes++;
						latinSquare.set(i, j, k);
						if(j < n-1) {
							ok = backtracking(new Pair<Integer, Integer>(i, j+1));
						} else {
							ok = backtracking(new Pair<Integer, Integer>(i+1, 0));
						}
						if(!ok) {
							latinSquare.set(i, j, -1);
						}
					}
					k++;
				}
			}
		}
		return ok;
	}

	/**
	 * Returns the internal Latin square
	 * 
	 * @return the internal Latin square
	 */
	public LatinSquare getPuzzle() {
		return latinSquare;
	}

	/**
	 * Sets the Latin square. The parameter is internally cloned, so it remains
	 * independent from the solver.
	 * 
	 * @param latinSquare the Latin square
	 */
	public void setPuzzle(LatinSquare latinSquare) {
		this.latinSquare = latinSquare.clone();
		if (verbosity) {
			System.out.println(this.latinSquare);
		}
	}

	@Override
	public String getName() {
		return "Latin-square backtracking";
	}

}
