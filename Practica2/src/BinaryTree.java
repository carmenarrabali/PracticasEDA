
public interface BinaryTree<E> {
	/** Devuelve true si el arbol esta vacio */
	public boolean isEmpty();
	
	/** Devuelve el elemento raiz */
	public E root();	
	
	/** Devuelve el subarbol izquierdo */
	public BinaryTree<E> left();
	
	/** Devuelve el subarbol derecho*/
	public BinaryTree<E> right();
}
