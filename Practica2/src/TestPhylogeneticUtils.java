import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestPhylogeneticUtils {
	
	public static void main(String[] args) {
		if (args.length > 1 && args[0].equals("-f")) {
			Scanner inputFile;
			try {
				inputFile = new Scanner (new File(args[1]));
				while (inputFile.hasNext()) {
					PhylogeneticUtils.m = inputFile.nextInt();
					if(!inputFile.hasNext())
					{
						System.out.println("TestPhylogeneticUtils incorrect file format");
						inputFile.close();
						System.exit(1); 
					}
					PhylogeneticTree t = readFromString(inputFile.nextLine());
					System.out.println("Tree: "+t);
					int parsimonyScore = PhylogeneticUtils.computeParsimonyScore(t);
					System.out.println("Parsimony Score "+ parsimonyScore);
				}
				inputFile.close();
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
		}
		else {
			System.out.println("TestPhylogeneticUtils -f <filename>");
			System.exit(1);   				
		}
		

	}
	
	// NO MODIFICAR !!
	
	
	//Metodos auxiliares para transformar notacion de Newick a PhylogeneticTree
	public static PhylogeneticTree readFromString(String strTree) {
		PhylogeneticTree t = new PhylogeneticTree();
		t.root = readSubTree(strTree.replaceAll("\\s+",""));
		return t;
	}
	
	private static PhylogeneticTree.Node readSubTree(String s) {
		int leftParen = s.indexOf('(');
        int rightParen = s.lastIndexOf(')');

        if (leftParen != -1 && rightParen != -1) {

            String label = s.substring(rightParen + 1);
            String[] childrenString = split(s.substring(leftParen + 1, rightParen));

            PhylogeneticTree.Node node = new PhylogeneticTree.Node();
            node.label = label.equals(";") ? "": label;
            node.left = readSubTree(childrenString[0]);
            node.right = readSubTree(childrenString[1]);
            return node;
        } else if (leftParen == rightParen) {

        	PhylogeneticTree.Node node = new PhylogeneticTree.Node();
            node.label =s;
            return node;

        } else throw new RuntimeException("unbalanced ()'s");
		
	}
	
	private static String[] split(String s) {

        ArrayList<Integer> splitIndices = new ArrayList<>();

        int rightParenCount = 0;
        int leftParenCount = 0;
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '(':
                    leftParenCount++;
                    break;
                case ')':
                    rightParenCount++;
                    break;
                case ',':
                    if (leftParenCount == rightParenCount) splitIndices.add(i);
                    break;
            }
        }

        int numSplits = splitIndices.size() + 1;
        String[] splits = new String[numSplits];

        if (numSplits == 1) {
            splits[0] = s;
        } else {

            splits[0] = s.substring(0, splitIndices.get(0));

            for (int i = 1; i < splitIndices.size(); i++) {
                splits[i] = s.substring(splitIndices.get(i - 1) + 1, splitIndices.get(i));
            }

            splits[numSplits - 1] = s.substring(splitIndices.get(splitIndices.size() - 1) + 1);
        }

        return splits;
    }
}
