import tree.BinaryTree;

public class PhylogeneticTree implements BinaryTree<String>{
	public static class Node{
		String label;
		Node left;
		Node right;
	}
	
	protected Node root;
	
	public PhylogeneticTree() { 
		//TODO: Constructor del arbol vacio (su root es nulo)
		root = null;	
	}
	
	public PhylogeneticTree(String label) { 
		//TODO: Constructor del nodo hoja (solo tiene etiqueta)
		root = new Node();
		root.label = label;
	}
	
	public PhylogeneticTree(String label, PhylogeneticTree left, PhylogeneticTree right){
		//TODO: Constructor del nodo con etiqueta e hijos izquierdo y derecho
		root = new Node();
		root.label = label;
		root.left = left.root;
		root.right = right.root;
	}
	
	public PhylogeneticTree(PhylogeneticTree left, PhylogeneticTree right){ 
		//TODO: Constructor de nodo interno -> su etiqueta es ""
		root = new Node();
		root.label = "";
		root.left = left.root;
		root.right = right.root;
	}
	
	@Override
	public boolean isEmpty() {
		//TODO: Devuelve si el arbol esta vacio
		return root == null;
	}

	@Override
	public String root() {		
		//TODO: Devuelve la etiqueta. Si el árbol esta vacio devuelve ""
		if(root == null) {
			return "";
		} else {
			return root.label;
		}
	}

	@Override
	public PhylogeneticTree left() {
		//TODO: Devuelve el subarbol izquierdo
		PhylogeneticTree T = new PhylogeneticTree();
		T.root = this.root.left;
		return T;
	}

	@Override
	public PhylogeneticTree right() {
		//TODO: Devuelve el subarbol derecho
		PhylogeneticTree T = new PhylogeneticTree();
		T.root = this.root.right;
		return T;
	}

	@Override
	public boolean isLeaf() {
		//TODO: Devuelve si el nodo es hoja
		if(root != null) {
			return root.left == null && root.right == null;
		}
		return false;		
	}
	
	public String toString() {
		return toStringAuxiliar() + ";";
	}
	
	public String toStringAuxiliar() {
		//TODO: Devuelve un String con el arbol en notacion de Newick
		// Si el arbol está vacio se representa graficamente con la etiqueta ""
		String output = "";
		if(!isLeaf()) {
			output += "(";
			output += left().toStringAuxiliar();
			output += ",";
			output += right().toStringAuxiliar();
			output += ")";
		} else {
			output = root();
		}
	    return output;
	} 
}
