public class PhylogeneticUtils {	
	public static char alphabet[] = {'A','C', 'G', 'T'};
	public static int delta[][] = {{0, 1, 1, 1}, {1, 0, 1, 1}, {1, 1, 0, 1}, {1, 1, 1, 0}};
	public static int m = 0;
	public static char label[] = new char[m];
	
	// Calcula la puntuacon de parsimonia del arbol t
	public static int computeParsimonyScore(PhylogeneticTree t) {
		int parsimony = 0;
		int min = 0;
		int s [][] = computeScoreRec(t);
		for(int i = 0; i <= m-1; i++) {
			min = s[i][0];
			for(int j = 1; j <= alphabet.length-1; j++) {
				if(s[i][j] < min) {
					min = s[i][j];
				}
			} parsimony += min;
		}
		return parsimony;
	}
	
	// Calcula la matriz de coste s para el nodo raiz de t
	public static int[][] computeScoreRec(PhylogeneticTree t) {
		int s [][] = new int[m][alphabet.length];
		
		if(t.isLeaf()) {
			label = t.root().toCharArray();
			for(int i = 0; i <= m-1; i++) {
				for(int j = 0; j <= alphabet.length-1; j++) {
					if(label[i] == alphabet[j]) {
						s[i][j] = 0; //No cambia el nucleótido
					} else {
						s[i][j] = Integer.MAX_VALUE - 1; // si se cambia el nucleótido
					}
				}
			}
		} else{
			int sl [][] = computeScoreRec(t.left());
			int sr [][] = computeScoreRec(t.right());
			for(int i = 0; i <= m-1; i++) {
				for(int j = 0; j <= alphabet.length-1; j++) {
					s[i][j] = mink(s, sl, sr, i, j);
				}
			}
		}
		return s;
	}	


	public static int mink(int[][] s, int[][] sl, int[][]sr, int i, int j) {
		int minParLeft = Integer.MAX_VALUE - 1;
		int minParRight = Integer.MAX_VALUE - 1;
		for(int k = 0; k <= alphabet.length - 1; k++) {
			if(sl[i][k] + delta[j][k] < minParLeft) {
				minParLeft = sl[i][k] + delta[j][k];
			}
			if(sr[i][k] + delta[j][k] < minParRight) {
				minParRight = sr[i][k] + delta[j][k];
			}
		}
		return minParLeft + minParRight;
	}
}