
//rotem nizhar -208646984 -rotemnizhar
// tom sabag -208845842 - tomsabag
//
// import java.util.Arrays;
import java.util.List;



/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {

	private IAVLNode root;
	private static int keysToArrayIndex = 0;
	private static int infoToArrayIndex = 0;
	private IAVLNode Max;
	private IAVLNode Min;

	public AVLTree() {
		this.root = null;
	}

	public AVLTree(IAVLNode root) {
		root.setParent(null);
		this.root = root;
		this.Max=root;
		this.Min=root;
	}


	/**
	 * public boolean empty()
	 * <p>
	 * returns true if and only if the tree is empty
	 */
	public boolean empty() {
		return this.root == null; // returns a boolean indicating whether the tree is empty or not
	}

	/**
	 * public String search(int k)
	 * <p>
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 */
	public String search(int k) // uses AVLTreeUtils module to perform recursive search for a key k
	{
		return search_rec(k, root);
	}

	private String search_rec(int k, IAVLNode root) // uses AVLTreeUtils module to perform recursive search for a key k. returns null if Key k is not inside the tree, else returns its value
	{
		if (root == null)
			return null;
		else if (root.getKey() == k)
			return root.getValue();
		else if (k > root.getKey())
			return search_rec(k, root.getRight());
		else
			return search_rec(k, root.getLeft());
	}


	/**
	 * public int insert(int k, String i)
	 * <p>
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
	 * returns -1 if an item with key k already exists in the tree.
	 */
	public int insertCaseRootIsNull(int k, String i) {
		this.root = new AVLNode(k, i); // create a new node
		root.getRight().setParent(root); // connect between the virtual node to the root
		root.getLeft().setParent(root);	// connect between the virtual node to the root
		this.Max=this.root;
		this.Min=this.root;
		return 0;
	}

	public IAVLNode NaiveInsert(int k, String i) {
		IAVLNode parent = insert_pointer(k, root); // get the place where we need to insert th new node
		IAVLNode node = new AVLNode(k, i); 
		node.getRight().setParent(node); 
		node.getLeft().setParent(node); 
		if (node.getKey() > parent.getKey()) // if the new node is a right son
			parent.setRight(node);
		else {								// if the new node is a left son
			parent.setLeft(node);
		}
		node.setParent(parent);
		return node;
	}

	private int rebalance(IAVLNode current) {
		int val =0;
		int rebalancing_ops = 0;
		while (SituationforInsert(current)!=0) {
			int Situation= SituationforInsert(current);
			if (current.getParent().getKey()==root.getKey()) { // root is one of the nodes being rotated
				val =1;
			}
			if (Situation==1) {   // do promote
				current.getParent().setRank(current.getParent().getRank()+1);
				rebalancing_ops++;
				current=current.getParent();
			}
			else if(Situation==2) { // rotate right
				current.getParent().setRank(current.getRank()-1);
				RotateRight(current);
				rebalancing_ops+=2;
				if (val==1) {
					this.root=current;
				}
				break;
			}
			else if(Situation==3) { // do double rotate
				IAVLNode son= current.getRight();
				son.setRank(son.getRank()+1);
				current.setRank(current.getRank()-1);
				current.getParent().setRank(current.getParent().getRank()-1);
				RotateLeft(son);
				RotateRight(son);
				rebalancing_ops=rebalancing_ops+5;
				if (val==1) {
					this.root=son;
				}
				break;
			}
			else if (Situation==4) { // do rotate left
				current.getParent().setRank(current.getRank()-1);
				RotateLeft (current);
				rebalancing_ops+=2;
				if (val==1) {
					root=current;
				}
				break;
			}
			else if (Situation==6) { // case thats happend only in join- rotate right
				current.getParent().setRank(current.getParent().getRank()-1);
				RotateRight(current);
				rebalancing_ops+=2;
				break;
			}
			else if (Situation==7) { // case thats happend only in join- rotate left
				current.getParent().setRank(current.getParent().getRank()-1);
				RotateLeft(current);
				rebalancing_ops+=2;
				break;
			}
			else  {
				IAVLNode son= current.getLeft(); // do double rotate (other case)
				son.setRank(son.getRank()+1);
				current.setRank(current.getRank()-1);
				current.getParent().setRank(current.getParent().getRank()-1);
				RotateRight(son);
				RotateLeft(son);
				rebalancing_ops=rebalancing_ops+5;
				if (val==1) {
					this.root=son;
				}
				break;
			}
		}
		return rebalancing_ops;
	}

	public int insert(int k, String i) { // take care if tree is empty
		if (root == null) // the tree is empty
			return insertCaseRootIsNull(k, i);

		if (search(k) != null) // the node is already in the tree
			return -1;

		IAVLNode current= NaiveInsert(k, i); // insert the node without rebalance ops
		if ((current.getKey()<Min.getKey())|| (this.Min==null)) { // canage the min if necessary
			this.Min=current;
		}
		if((current.getKey()>Max.getKey()) || (this.Max==null)) { // canage the max if necessary
			this.Max=current;
		}
		// insert while loops to a rebalancing function
		int n= this.rebalance(current);
		int max= Math.max(root.getRight().getRank(), root.getLeft().getRank())+1; // just to be sure
		if(root.getRank() != max) {
			root.setRank(max);
			n++;
		}
		return (n);    
	}
	
	private void RotateRight (IAVLNode node) {
		if(node.getParent()==null)
			return;
		
		IAVLNode parent =  node.getParent(); 
		IAVLNode left= node.getRight(); // right son of current node 
		IAVLNode GrandParent =  parent.getParent(); 
		int val =WhichSon(parent);
		node.setRight(parent);
		parent.setLeft(left);
		left.setParent(parent);
		parent.setParent(node);
		if (val==0) { // the root is involve in the rotate
			node.setParent(null);
		}
		if (val==1) {	// parent is a right son
			GrandParent.setRight(node);
			node.setParent(GrandParent);
		}
		if (val==2) { //parent is a left son
			GrandParent.setLeft(node);
			node.setParent(GrandParent);
		}

		


	}
	private void RotateLeft (IAVLNode node) {
		if(node.getParent()==null)
			return;

		IAVLNode parent =  node.getParent();
		IAVLNode right= node.getLeft();
		IAVLNode GrandParent =  parent.getParent();
		int val =WhichSon(parent);
		node.setLeft(parent);
		parent.setRight(right);
		right.setParent(parent);
		parent.setParent(node);	
		if (val==0) { // the node is the root now (after the rotate)
			node.setParent(null);
		}
		if (val==1) { // parent is a right son
			GrandParent.setRight(node);
			node.setParent(GrandParent);
		}
		if (val==2) { //parent is a left son
			GrandParent.setLeft(node);
			node.setParent(GrandParent);
		}
		
	}
	private int WhichSon (IAVLNode node) { // returns 1 if the node is a right son, 2 if the node is the left son, 0 if the node is a root
		if(node.getParent()==null) {
			return (0);
		}
		IAVLNode parent =  node.getParent();
		if (node.getKey()==parent.getRight().getKey()) {
			return (1);
		}
		return (2);
	}
 

	private char direction(IAVLNode parent, IAVLNode node) {
		if (node.getKey() > parent.getKey())
			return 'R';
		return 'L';
	}

	private IAVLNode insert_pointer(int k, IAVLNode root) {
		IAVLNode parent = root;
		while (root.getKey() != -1) {
			if (k > root.getKey()) { // the new node will be a right son
				parent = root; 
				root = root.getRight();
			}
			else if (k < root.getKey()) {  // the new node will be a left son
				parent = root;
				root = root.getLeft();
			}
		}
		return parent;
	}

	private int SituationforInsert(IAVLNode node) { // decide which rebalncing step to fo next
		if (node.getParent() == null) { // the tree is balanced //
			return (0);
		}
		IAVLNode parent = node.getParent();
		if (parent.getRank() - node.getRank() == 1) { // the tree is balanced
			return (0);
		}

		//else, rank difference between parent and node equals 0

		if (direction(parent, node) == 'L') {
			if (parent.getRank() - parent.getRight().getRank()==  1) { // need promote step. 0,1 --> 1,2
				return (1);
			}
			
			// 0,2 here
			else if (node.getRank() - node.getRight().getRank() ==  2) { // need rotate right step. sons are (1,2)
				return (2);
			}
			else if( (node.getRank() - node.getRight().getRank() ==  1) && (node.getRank() - node.getLeft().getRank() ==  1)){
				return (6);
			}
			else {  // need double rotate right step // sons are (2,1)
				return (3);
			}
		}
		else {
			if (parent.getLeft().getRank() == parent.getRank() - 1) {    // need promote step  0,1 --> 1,2
				return (1);
			} else if (node.getLeft().getRank() == node.getRank() - 2) {    // need rotate Left step. sons are (1,2)
				return (4);
			}
			else if( (node.getRank() - node.getRight().getRank() ==  1) && (node.getRank() - node.getLeft().getRank() ==  1)){
				return (7);
			}
			else {
				return (5); // need double rotate left step. sons are (2,1)
			}
		}
	}
	/**
	 * public int delete(int k)
	 * <p>
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
	 * returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) {
		IAVLNode node= SearchNode ( k ,this.root);// if the node not in thr tree
		if (node==null) {
			return (-1);
		}
		if (IsLeaf(this.root)==true) { // there is only one node in the tree
			this.root=null;
			this.Max=null;
			this.Min=null;
			return(0);
		}
		if ( (this.root.getKey()==node.getKey()) &&(IsUnary(node)==true) ) { // we need to delete the root, it is unary root
			if(this.root.getRight().getKey()==-1) {
				IAVLNode predsuccsesor = FindPredecessor(node);
				SwitchRoot ( node, predsuccsesor);
				this.root=predsuccsesor;
			}
			else {
				IAVLNode succsesor = FindSuccessor(node);
				SwitchRoot (node,  succsesor );
				this.root=succsesor;
			}
		}
		
		if ((IsLeaf(node)==false) && (IsUnary(node)==false)) {// make sure the node we delete is leaf or unary
			IAVLNode succsesor = FindSuccessor(node);
			SwitchNodes ( node, succsesor);
		}
		
		int rebalancing_ops = 0;
		IAVLNode parent= node.getParent();
		To_Delete(node);
		IAVLNode current= parent;
		int val=0;
		while (SituationforDelete (current)!=0){ // 0- the tree is balanced
			IAVLNode rotate;
			int s= SituationforDelete (current);
			if (current.getKey()==this.root.getKey()) { // the current node is the root- we need to stop rebalance
				val=1;
			}
			if (s==1) {	//demote
				current.setRank(current.getRank()-1);
				rebalancing_ops ++;
				if(val==1) {
					break;
				}
				current=current.getParent();
			}
			else if (s==2) { // case number two -single rotate and solved
				 rotate=current.getRight();
				 rotate.setRank(rotate.getRank()+1);
				 current.setRank(current.getRank()-1);
				 rebalancing_ops =rebalancing_ops +3;
				 RotateLeft(rotate);
				 if(val==1) {
					 this.root=rotate;
				 }
				 break;
			}
			else if (s==3) {	// case number two simmetery -single rotate and solved
				 rotate=current.getLeft();
				 rotate.setRank(rotate.getRank()+1);
				 current.setRank(current.getRank()-1);
				 rebalancing_ops =rebalancing_ops +3;
				 RotateRight(rotate);
				 if(val==1) {
					 this.root=rotate;
				 }
				 break;
			}
			else if (s==4) { // case number three - single rotate and demote
				 rotate=current.getRight();
				 current.setRank(current.getRank()-2);
				 rebalancing_ops =rebalancing_ops +3;
				 RotateLeft(rotate);
				 if(val==1) {
					 this.root=rotate;
					 break;
				 }
				 current=rotate.getParent();
			}
			else if (s==5) { // case number three simmetery -single rotate and demote
				rotate=current.getLeft();
				current.setRank(current.getRank()-2);
				 rebalancing_ops =rebalancing_ops +3;
				 RotateRight(rotate);
				 if(val==1) {
					 this.root=rotate;
					 break;
				 }
				 current=rotate.getParent();
			}
			else if (s==6) { // case number four - double rotate and demote
				rotate=current.getRight().getLeft();
				rebalancing_ops =rebalancing_ops +6;
				rotate.setRank(rotate.getRank()+1);
				current.setRank(current.getRank()-2);
				current.getRight().setRank(current.getRight().getRank()-1);
				RotateRight(rotate);
				RotateLeft(rotate);
				if(val==1) {
					 this.root=rotate;
					 break;
				 }
				 current=rotate.getParent();
			}
			else {
				rotate=current.getLeft().getRight();
				rebalancing_ops =rebalancing_ops +6;
				rotate.setRank(rotate.getRank()+1);
				current.setRank(current.getRank()-2);
				current.getLeft().setRank(current.getLeft().getRank()-1);
				RotateLeft(rotate);
				RotateRight(rotate);
				if(val==1) {
					 this.root=rotate;
					 break;
				 }
				 current=rotate.getParent();
			}
		}
		if (node.getKey()==this.Min.getKey()) {
			this.FindMin();
		}
		if (node.getKey()==this.Max.getKey()) {
			this.FindMax();
		}
		return (rebalancing_ops);
	}
	
	private int SituationforDelete (IAVLNode node) {
		int l =node.getLeft().getRank();
		int r= node.getRight().getRank();
		int rank= node.getRank();
		IAVLNode right= node.getRight();
		IAVLNode left= node.getLeft();
		if ((rank-r==2) && (rank -l==1)) { // tree is balanced
			return (0);
		}
		else if ((rank-l==2) && (rank -r==1)) { // tree is balanced
			return (0);
		}
		else if ((rank-l==1) && (rank -r==1)) { // tree is balanced
			return (0);
		}
		else if ((rank-l==2) && (rank -r==2)) { // case number one - demote
			return (1);
		}
		else if ((rank-l==3) && (rank -r==1) && (right.getRank()-right.getRight().getRank()==1) && (right.getRank()-right.getLeft().getRank()==1)) { // case number two -single rotate and solved
			return (2);
		}
		else if ((rank-l==1) && (rank -r==3)&& (left.getRank()-left.getRight().getRank()==1) && (left.getRank()-left.getLeft().getRank()==1)) {	// case number two simmetery -single rotate and solved
			return (3);
		}
		else if ((rank-l==3) && (rank -r==1) && (right.getRank()-right.getRight().getRank()==1) && (right.getRank()-right.getLeft().getRank()==2)) { // case number three - single rotate and demote
			return (4);
		}
		else if ((rank-l==1) && (rank -r==3)&& (left.getRank()-left.getRight().getRank()==2) && (left.getRank()-left.getLeft().getRank()==1)) {	// case number three simmetery -single rotate and demote
			return (5);
		}
		else if  ((rank-l==3) && (rank -r==1) && (right.getRank()-right.getRight().getRank()==2) && (right.getRank()-right.getLeft().getRank()==1)) { // case number four - double rotate and demote {
			return (6);
		}
		else {
			return (7);
		}
	}
	private void To_Delete (IAVLNode node) {
		IAVLNode parent=node.getParent();
		IAVLNode r=node.getRight();
		IAVLNode l=node.getLeft();
		if(IsLeaf(node)==true) {
			if(parent.getRight().getKey()==node.getKey()) {// the node that we delete is a leaf and right son of the parent
				parent.setRight(r);
				r.setParent(parent);
			}
			else {	// the node that we delete is a leaf and left son of the parent
				parent.setLeft(l);
				l.setParent(parent);
			}
		}
		else { // node is unary
			IAVLNode chose;
			if (r.getKey()>l.getKey()) {
				 chose=r;
			}
			else {
				 chose=l;
			}
			if(parent.getRight().getKey()==node.getKey()) {// the node that we delete  right son of the parent
				parent.setRight(chose);
				chose.setParent(parent);
			}
			else {	// the node that we delete left son of the parent
				parent.setLeft( chose);
				chose.setParent(parent);
			}
		}
		/**
		node.setParent(null);
		node.setRight(null);
		node.setLeft(null);
		*/
	}
	
	
	

	private IAVLNode SearchNodeFromRoot (int k) {
		IAVLNode node = this.root;
		return SearchNode(k, this.root);
	}
	private  void SwitchRoot (IAVLNode root, IAVLNode node) { // cange th place between tje toot in his son
		IAVLNode v= new VirtualAVLNode();
		int tmp = root.getRank();
		if (node.getKey()>root.getKey()) {
			node.setLeft(root);
			root.setRight(v);
			node.setParent(null);
			root.setParent(node);
			root.setRank(node.getRank());
			node.setRank(tmp);
		}
		else {
			node.setRight(root);
			root.setLeft(v);
			node.setParent(null);
			root.setParent(node);
			root.setRank(node.getRank());
			node.setRank(tmp);
		}
	}

	private IAVLNode SearchNode (int k ,IAVLNode root) { // from a certain point
		IAVLNode node = root;
		if (node == null)
			return (null);

		while (node.getKey() != -1) {
			if (node.getKey() == k) { // found the node
				return (node);
			} else if (node.getKey() < k) { // the node we search need to be right son
				node = node.getRight();
			} else {
				node = node.getLeft();	// the node we search need to be left son
			}
		}
		return (null);
	}

	/**
	 * public String min()
	 * <p>
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 */
	public String min() {
		if (this.Min==null) {
			return (null);
		}
		return (this.Min.getValue());
	}
	

	
	private void FindMin() {
		IAVLNode current=this.root;
		IAVLNode parent=current.getParent();
		while (current.isRealNode()) { // find the most left node in the tree
			current=current.getLeft();
			parent=current.getParent();
		}
		this.Min=parent;
	}
	/**
	 * public String max()
	 * <p>
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 */
	public String max() {
		if (this.Max==null) {
			return (null);
		}
		return (this.Max.getValue());
	}
	private void FindMax() {
		IAVLNode current=this.root;
		IAVLNode parent=current.getParent();
		while (current.isRealNode()) {	// find the most right node in the tree
			current=current.getRight();
			parent=current.getParent();
		}
		this.Max=parent;
	}
	
	
	public IAVLNode min1() {
		if (this.Min==null) {
			return (null);
		}
		return (this.Min);
	}
	public IAVLNode max1() {
		if (this.Max==null) {
			return (null);
		}
		return (this.Max);
	}
	
	/**
	 * public int[] keysToArray()
	 * <p>
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 */
	public int[] keysToArray() {
		if (this.root == null)
			return new int[0];
		int[] arr = new int[this.root.getSize()]; // to be replaced by student code
		keysToArrayRec(root, arr);
		keysToArrayIndex = 0;
		return arr;              // to be replaced by student code
	}


	private static void keysToArrayRec(AVLTree.IAVLNode root, int[] arr) { // the argument is not good
		if (root.getKey() != -1) {
			keysToArrayRec(root.getLeft(), arr);
			arr[keysToArrayIndex++] = root.getKey();
			keysToArrayRec(root.getRight(), arr);
		}
	}
	/**
	 * public String[] infoToArray()
	 * <p>
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		if (this.root == null) return new String[0];
		String[] arr = new String[this.root.getSize()]; // to be replaced by student code
		infoToArrayRec(root, arr);
		infoToArrayIndex = 0;
		return arr;                    // to be replaced by student code
	}

	private void infoToArrayRec(AVLTree.IAVLNode root, String[] arr) {
		if (root.getKey() != -1) {
			infoToArrayRec(root.getLeft(), arr);
			arr[infoToArrayIndex++] = root.getValue();
			infoToArrayRec(root.getRight(), arr);
		}
	}
	/**
	 * public int size()
	 * <p>
	 * Returns the number of nodes in the tree.
	 * <p>
	 * precondition: none
	 * postcondition: none
	 */
	public int size() {
		if (root==null) {
			return (0);
		}
		return  (root.getSize());
		
	}

	/**
	 * public int getRoot()
	 * <p>
	 * Returns the root AVL node, or null if the tree is empty
	 * <p>
	 * precondition: none
	 * postcondition: none
	 */
	public IAVLNode getRoot() {
		return this.root;
	}

	/**
	 * public string split(int x)
	 * <p>
	 * splits the tree into 2 trees according to the key x.
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
	 * postcondition: none
	 */

	public AVLTree[] split(int x) {
		IAVLNode current = SearchNodeFromRoot(x); // find splitting node
		// missed nodes might occur only 1st & second iterations
		AVLNode missedNodeRightSubTree = null;
		AVLNode missedNodeLeftSubTree = null;

		AVLNode missedNodeRightSubTree2 = null;
		AVLNode missedNodeLeftSubTree2 = null;

		// initialize merging leftSubTree and rightSubTree
		AVLTree leftSubTree = null;
		AVLTree rightSubTree = null;

		if (current.getLeft().isRealNode())
			leftSubTree = new AVLTree(current.getLeft());
		if (current.getRight().isRealNode())
			rightSubTree= new AVLTree(current.getRight());

		IAVLNode parent = current.getParent();
		// every iteration, parents has a current children, and another children, the other childen, holds parentsOtherSubtree
		AVLTree parentsOtherSubTree;
		AVLNode parent_copy;
		while (parent != null) {
			if (WhichSon(current) == 1) { // current is a right child
				parentsOtherSubTree = new AVLTree(parent.getLeft());
				parent_copy = new AVLNode(parent.getKey(), parent.getValue());
				if (leftSubTree != null) { // if theres a leftSubTree , merge it under a copy of parent node
					if (parentsOtherSubTree.getRoot().isRealNode()) {
						leftSubTree.join(parent_copy, parentsOtherSubTree);
					}
				}
				else  {
					if (parentsOtherSubTree.getRoot().isRealNode())
						leftSubTree = parentsOtherSubTree;
					//might occurs only 1st & 2nd steps
					if (missedNodeLeftSubTree == null)
						missedNodeLeftSubTree = parent_copy;
					else
						missedNodeLeftSubTree2 = parent_copy;

				}
			}
			else // current is a left child
			{
				parentsOtherSubTree = new AVLTree(parent.getRight());
				parent_copy = new AVLNode(parent.getKey(), parent.getValue());
				if (rightSubTree != null) { // if theres a rightSubTree , merge it under a copy of parent node
					if (parentsOtherSubTree.getRoot().isRealNode()) {
						rightSubTree.join(parent_copy, parentsOtherSubTree);
					}
				}
				else {
					if (parentsOtherSubTree.getRoot().isRealNode())
						rightSubTree = parentsOtherSubTree;
					//might occurs only 1st & 2nd steps
					if (missedNodeRightSubTree == null)
						missedNodeRightSubTree = parent_copy;
					else
						missedNodeRightSubTree2 = parent_copy;
				}
			}
			//proceed to next iteration
			current = current.getParent();
			parent = parent.getParent();

		}
		// if we missed nodes 1st or 2nd iteration, add them with extra O(logn) time complexity
		if (missedNodeLeftSubTree != null) leftSubTree.insert(missedNodeLeftSubTree.getKey(), missedNodeLeftSubTree.getValue()); // happens only once
		if (missedNodeRightSubTree != null) rightSubTree.insert(missedNodeRightSubTree.getKey(), missedNodeRightSubTree.getValue()); // happens only once
		if (missedNodeLeftSubTree2 != null) leftSubTree.insert(missedNodeLeftSubTree2.getKey(), missedNodeLeftSubTree2.getValue()); // happens only once
		if (missedNodeRightSubTree2 != null) rightSubTree.insert(missedNodeRightSubTree2.getKey(), missedNodeRightSubTree2.getValue()); // happens only once
		// in case right or left subTree dont exist
		if (leftSubTree == null) leftSubTree = new AVLTree();
		if (rightSubTree == null) rightSubTree = new AVLTree();
		// update min and max fields with O(logn) extra time complexity
		setMinMax(leftSubTree);
		setMinMax(rightSubTree);

		return new AVLTree[] {leftSubTree, rightSubTree};
	}
	private void setMinMax(AVLTree t) {
		t.setMin();
		t.setMax();
	}

	private void setMax() {
		IAVLNode current = this.root;
		if (current == null) return ;
		while (current.getRight().isRealNode())
			current = current.getRight();
		this.Max = current;
	}

	private void setMin() {
		IAVLNode current = this.root;
		if (current == null) return ;
		while (current.getLeft().isRealNode())
			current = current.getLeft();
		this.Min = current;
	}
	/**
	 * public join(IAVLNode x, AVLTree t)
	 * <p>
	 * joins t and x with the tree.
	 * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
	 * postcondition: none
	 */
	// in case the higher tree of the two is a leaf, fix pointers & min & max fields and return l 0 - 0 l + 1 = 1
	public int joinCaseHigherTreeIsANode(IAVLNode x, AVLTree t) {
		if (t.getRoot().getKey() < x.getKey())
			x.setLeft(t.getRoot());
		else
			x.setRight(t.getRoot());

		if (this.getRoot().getKey() < x.getKey())
			x.setLeft(this.getRoot());
		else
			x.setRight(this.getRoot());
		x.setRank(1);
		this.root.setParent(x);
		t.root.setParent(x);
		this.root = x;
		if (!this.root.getLeft().isRealNode() && !this.root.getRight().isRealNode()) {
			this.Max = this.root;
			this.Min = this.root;
		}
		else if (this.root.getRight().isRealNode()) {
			this.Max = this.root.getRight();
			this.Min = this.root;
		}
		else if (this.root.getLeft().isRealNode()) {
			this.Max = this.root;
			this.Min = this.root.getLeft();
		}
		return 1;
	}


	public int join(IAVLNode x, AVLTree t) {
		// first 3 if statements verify that both trees are not empty.
		if (t.getRoot() == null && this.getRoot() == null) {
			insert(x.getKey(), x.getValue());
			return 1;
		}

		if (t.getRoot() == null) {
			insert(x.getKey(), x.getValue());
			return this.getRoot().getRank() + 1;
		}

		if (this.getRoot() == null) {
			t.insert(x.getKey(), x.getValue());
			this.root = t.getRoot();
			return t.getRoot().getRank() + 1;
		}
		// set a higher and smaller trees variables
		AVLTree t_higher = (t.root.getRank() >= this.root.getRank()) ? t:this;
		AVLTree t_smaller = (t.root.getRank() < this.root.getRank()) ? t:this;
		// get the min and max keys of the trees, and determine new min and max keys for future settings
		int max_value = Math.max(t_higher.Max.getKey(), t_smaller.Max.getKey());
		int min_value = Math.min(t_higher.Min.getKey(), t_smaller.Min.getKey());

		if (t_higher.getRoot().getHeight() == 0) {
			return joinCaseHigherTreeIsANode(x, t);
		}
		// determine in which direction we traverse down our AVL tree
		char direction = (t_higher.getRoot().getKey() > t_smaller.getRoot().getKey()) ?'L':'R';
		int t_smaller_rank = t_smaller.getRoot().getRank();
		int t_higher_rank = t_higher.getRoot().getRank();

		//traverse down ...
		IAVLNode current = t_higher.root;
		while (current.getRank() > t_smaller_rank)
			current = (direction=='L') ? current.getLeft() : current.getRight();


		IAVLNode currentParent = current.getParent();
		x.setRank(t_smaller_rank + 1);
		// pointers settings
		if (direction == 'L') {
			if (currentParent != null)
				currentParent.setLeft(new VirtualAVLNode());
			x.setLeft(t_smaller.getRoot());
			x.setRight(current);
		}
		else {
			if (currentParent != null)
				currentParent.setRight(new VirtualAVLNode());
			x.setRight(t_smaller.getRoot());
			x.setLeft(current);
		}
		t_smaller.getRoot().setParent(x);
		current.setParent(x);
		if (currentParent != null)
			if (direction == 'R')
				currentParent.setRight(x);
			else
				currentParent.setLeft(x);

		x.setParent(currentParent);
		// if x is the parent node of the two merging trees,
		// the ranks difference is zero. therefore, make x the root, update min & max fields
		// and return rank difference (0) + 1 = 1
		if (x.getParent() == null) {
			this.root = x;
			this.Max = SearchNode(max_value, this.root);
			this.Min = SearchNode(min_value, this.root);
			return(1);
		}
		// otherwise, update root, and send the AVL tree to a rebalancing function. in case the root changes again,
		// the rebalancing func will take care of it.
		this.root = t_higher.getRoot();
		rebalance(x);
		// update min & max fields
		this.Max = SearchNode(max_value, this.root);
		this.Min = SearchNode(min_value, this.root);

		return (t_higher_rank - t_smaller_rank + 1);
	}

	private IAVLNode FindSuccessor(IAVLNode node) {
		if (node.getParent() == null && node.getRight().getKey() == -1 && node.getLeft().getKey() == -1)  // use instead IsUnary?
			return (null);

		if (node.getRight().getKey() != -1) { // go one time right and then all the time left
			IAVLNode right = node.getRight();
			if (right.getKey()!=-1) {
				while (right.getLeft().getKey() != -1) {
					right = right.getLeft();
				}
			}
			return (right);
		}
		if (node.getRight().getKey() == -1) {
			if (node.getParent() == null)  // if there is no succssor
				return null;

			IAVLNode current = node;
			while (current.getParent() != null) { // go up in the tree, try to find "right" parent
				IAVLNode curerntParent = current.getParent();
				if (curerntParent.getLeft().getKey() == current.getKey())
					return (curerntParent);
				current = curerntParent;
			}
		}
		return (null);
	}

	private IAVLNode FindPredecessor(IAVLNode node) {
		if (node.getParent() == null && node.getRight().getKey() == -1 && node.getLeft().getKey() == -1)  // there is no predecessor
			return (null);

		if (node.getLeft().getKey() != -1) { // go one time left and then all the time right
			IAVLNode left = node.getLeft();
			if (left.getKey()!=-1) {
				while (left.getRight().getKey() != -1) {
					left = left.getRight();
				}
			}
			return (left);
		}
		if (node.getLeft().getKey() == -1) { // we need to go up
			if (node.getParent() == null)  // if there is no succssor
				return null;

			IAVLNode current = node;
			while (current.getParent() != null) { // go up in the tree, try to find "left" parent
				IAVLNode curerntParent = current.getParent();
				if (curerntParent.getRight().getKey() == current.getKey())
					return (curerntParent);
				current = curerntParent;
			}
		}
		return (null);
	}

	private boolean IsUnary (IAVLNode node) {
		int r= node.getRight().getKey();
		int l= node.getLeft().getKey();
		if ((l==-1) && (r!=-1)) {
			return true;
		}
		if ((r==-1) && (l!=-1)) {
			return true;
		}
		return false;
	}

	private boolean IsLeaf (IAVLNode node) {
		int r= node.getRight().getKey();
		int l= node.getLeft().getKey();
		if ((l==-1) && (r==-1)) {
			return true;
		}
		return (false);
	}

	public IAVLNode areRelated(IAVLNode node1, IAVLNode node2) { // returns parent node if related. otherwise null
		if (node1.getRight() == node2 || node1.getLeft() == node2)
			return node1;
		else if (node2.getRight() == node1 || node2.getLeft() == node1)
			return node2;
		return null;
	}

 	public void SwitchNodes(IAVLNode node1, IAVLNode node2) { //switch between to nodes
 		int rankk_node1=node1.getRank();
 		int rankk_node2=node2.getRank();
 		node1.setRank(rankk_node2);
 		node2.setRank(rankk_node1);
		if (!node1.isRealNode() || !node2.isRealNode()) System.out.println("error");
		int h_node1 = node1.getHeight();
		int s_node1 = node1.getSize();
		int rank_node1 = node1.getRank();

		int h_node2 = node2.getHeight();
		int s_node2 = node2.getSize();
		int rank_node2 = node2.getRank();
		if (areRelated(node1, node2) != null) {
			IAVLNode parent = areRelated(node1, node2);
			IAVLNode son = (areRelated(node1, node2) == node2) ? node1 : node2;
			IAVLNode grandParent = parent.getParent();
			
			IAVLNode parentOtherSon = (WhichSon(son) == 1) ? parent.getLeft() : parent.getRight();
			String parentOtherSonString = (WhichSon(son) == 1) ? "left" : "right";
			String whichSonIsParent = (WhichSon(parent) == 1) ? "right" : "left";

			parent.setRight(son.getRight());
			parent.setLeft(son.getLeft());
			parent.setParent(son);
			parent.getRight().setParent(parent);
			parent.getLeft().setParent(parent);
			son.setParent(grandParent);

			if (parentOtherSonString.equals("left")) {
				son.setLeft(parentOtherSon);
				son.setRight(parent);
			}
			else {
				son.setLeft(parent);
				son.setRight(parentOtherSon);
			}
			if (whichSonIsParent.equals("left") && grandParent != null)
				grandParent.setLeft(son);
			else {
				if (grandParent != null)
					grandParent.setRight(son);
			}
			son.getLeft().setParent(son);
			son.getRight().setParent(son);
			if (this.root == parent)
				this.root = son;
		}
		else {
			IAVLNode r_node1 = node1.getRight();
			IAVLNode l_node1 = node1.getLeft();
			IAVLNode p_node1 = node1.getParent();

			IAVLNode r_node2 = node2.getRight();
			IAVLNode l_node2 = node2.getLeft();
			IAVLNode p_node2 = node2.getParent();
			int whichSonNode1 = WhichSon(node1);
			int whichSonNode2 = WhichSon(node2);
			// node 1 settings
			r_node2.setParent(node1);
			l_node2.setParent(node1);

			node1.setRight(r_node2);
			node1.setLeft(l_node2);

			node1.setParent(p_node2);

			if (whichSonNode2 == 1) {
				if (p_node2 != null)
					p_node2.setRight(node1);
			}
			else {
				if (p_node2!=null)
					p_node2.setLeft(node1);
			}
			if (whichSonNode1 == 1) {
				if (p_node1 != null)
					p_node1.setRight(node2);
			}
			else
				if (p_node1 != null)
					p_node1.setLeft(node2);

			// node2 settings
			node2.setRight(r_node1);
			node2.setLeft(l_node1);
			node2.setParent(p_node1);

			r_node1.setParent(node2);
			l_node1.setParent(node2);


			if (this.root == node1) {
				this.root = node2;
			}
			else if (this.root == node2) {
				this.root = node1;
			}
			
		}


	}

	public int FingerSearchTree (IAVLNode node) {
		IAVLNode max= this.Max;
		IAVLNode parent = max.getParent();
		int k=0;
		while((parent!=null)  && (parent.getKey()>node.getKey())) {
			max=parent;
			parent=max.getParent();
			k++;
		}
		if (parent==null) {
			 k=k-1;
			 return (search1( node, this.root)+k);	
		}
		return (k+search1( node, this.root));
		
		
	}
	
	public static int search1(IAVLNode node, IAVLNode root) // uses AVLTreeUtils module to perform recursive search for a key k
	{
		int n=0;
		IAVLNode current = root;
		while (node.getKey()!=current.getKey()) {
			if (node.getKey()>current.getKey()) {
				n++;
				current=current.getRight();
			}
			else {
				n++;
				current=current.getLeft();
			}
		}
		return (n);
	}




	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode {


		public int getKey(); //returns node's key (for virtuval node return -1)

		public String getValue(); //returns node's value [info] (for virtuval node return null)

		public void setLeft(IAVLNode node); //sets left child

		public IAVLNode getLeft(); //returns left child (if there is no left child return null)

		public void setRight(IAVLNode node); //sets right child

		public IAVLNode getRight(); //returns right child (if there is no right child return null)

		public void setParent(IAVLNode node); //sets parent

		public IAVLNode getParent(); //returns the parent (if there is no parent return null)

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

		public void setHeight(int height); // sets the height of the node

		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)

		public void setRank(int r);

		public int getRank();

		public int getSize();

		public void setSize(int s);

	}

	/**
	 * public class AVLNode
	 * <p>
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public static class AVLNode implements IAVLNode {
		private int Key;
		private String Info;
		private IAVLNode Right = null;
		private IAVLNode Left = null;
		private IAVLNode Parent = null;
		private int Height;
		private int Rank;
		private int Size;


		public AVLNode(int key, String info) {
			this(key, info, null);
		}

		public AVLNode(int key, String info, IAVLNode parent) { // create a node thats connect who has a parent
			this.Key = key;
			this.Info = info;
			this.Parent = parent;
			this.Right = new VirtualAVLNode();
			this.Left = new VirtualAVLNode();
			this.Height = 0;
			this.Rank = 0;
			this.Size = 1;
			//UpdateHeight (this);
		}


		public AVLNode(int key, String info, IAVLNode right, IAVLNode left, IAVLNode parent, int rank) { // create a node thats  connect to other nodes
			this.Key = key;
			this.Info = info;
			this.Left = left;
			this.Right = right;
			this.Parent = parent;
			this.Height = 1 + Math.max(right.getHeight(), left.getHeight());
			this.Rank = 1 + Math.max(right.getRank(), left.getRank());
			this.Size = 1 + right.getSize() + left.getSize();
			UpdateHeight (this);
		}

		public int getKey() // return key
		{
			return (this.Key);
		}

		public String getValue() // return value
		{
			return (this.Info);
		}

		public void setLeft(IAVLNode node) // set the left son of a node
		{
			this.Left = node;
			if (node.getHeight() > this.Right.getHeight()) {
			}
			if (node.getSize() != this.Right.getSize()) {
			}
		}

		public IAVLNode getLeft() // return the left son of a node
		{
			return (this.Left);
		}

		public void setRight(IAVLNode node) // set the right son of a node - changed
		{
			this.Right = node;
			if (node.getHeight() > this.Left.getHeight()) {
			}
			if (node.getSize() != this.Left.getSize()) {
			}
		}

		public IAVLNode getRight() // return the right son of a node
		{
			return this.Right;
		}

		public void setParent(IAVLNode node) // set the parent of a node
		{
			this.Parent = node;
			if (node == null)
				return;
			UpdateSize (node);
		}

		public IAVLNode getParent() // return the parent of a node
		{
			return this.Parent;
		}

		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode() {
			return true;
		}

		public void setHeight(int height) {
			this.Height = height;
		}

		public int getHeight() {
			return this.Height;
		}

		public void setRank(int r) {
			this.Rank = r;
			UpdateHeight (this);
		}

		public int getRank() // return the Rank of a node
		{
			return this.Rank;
		}

		public void setSize(int s) {
			this.Size = s;
		}

		public int getSize() // return the Rank of a node
		{
			return this.Size;
		}

		private void UpdateSize (IAVLNode node) {
			int cnt=0;
			IAVLNode current= node;
			while (current.getSize()!= (current.getLeft().getSize()+current.getRight().getSize()+1)) {
				if ((cnt>0) &&(current.getKey()==node.getKey())) {
					break;
				}
				current.setSize(current.getLeft().getSize()+current.getRight().getSize()+1);
				if (current.getParent()==null) {
					break;
				}
				current=current.getParent();
				cnt++;
			}
		}


		
		
		private void UpdateHeight (IAVLNode node) {
			int r= node.getRank();
			int h= node.getHeight();
			if (r!=h) {
				node.setHeight(r);
			}
		}
		
		private void updateAnccestorsHeight(IAVLNode node) {    // update the Height of all the anccestors of a node
			IAVLNode current = node;
			while (current.getParent()!= null) {
				IAVLNode parent = current.getParent();
				int h_parent = parent.getHeight();
				int h_parent_right = parent.getRight().getHeight();
				int h_parent_left = parent.getLeft().getHeight();
				int h_max = 1 + Math.max(h_parent_left, h_parent_right);
				if (h_parent == h_max)
					break;
				current.setHeight(h_max);
				current = current.getParent();
			}
		}

		private void updateAnccestorsSize(IAVLNode node) { // update the Size of all the anccestors of a node
			IAVLNode current = node;
			while (current != null) {
				int s_right = current.getRight().getSize();
				int s_left = current.getLeft().getSize();
				current.setSize(1 + s_left + s_right);
				current = current.getParent();
			}
		}


	}

	public static class VirtualAVLNode implements IAVLNode {
		private final String info = null;
		private final IAVLNode Right = null;
		private final IAVLNode Left = null;
		private IAVLNode Parent = null;
		private final int Height = -1;
		private final int Rank = -1;
		private final int Size = 0;


		public VirtualAVLNode() { // create virtual node
		}

		public VirtualAVLNode(IAVLNode parent) { // create a node thats connect who has a parent
			this.Parent = parent;
		}

		public int getKey() // return key
		{
			return (-1);
		}

		public String getValue() // return value
		{
			return (this.info);
		}

		public void setLeft(IAVLNode node) // set the left son of a node
		{

		}

		public IAVLNode getLeft() // return the left son of a node
		{
			return (this.Left);
		}

		public void setRight(IAVLNode node) // set the right son of a node
		{

		}

		public IAVLNode getRight() // return the right son of a node
		{
			return this.Right;
		}

		public void setParent(IAVLNode node) // set the parent of a node
		{
			this.Parent = node;
			if (node == null)
				return;
			UpdateSize (node);
		}

		public IAVLNode getParent() // return the parent of a node
		{
			return this.Parent;
		}

		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode() {
			return false;
		}

		public void setHeight(int height) {

		}

		public int getHeight() {
			return this.Height;
		}

		public void setRank(int s) {

		}

		public int getRank() {
			return this.Rank;
		}

		public void setSize(int s) {

		}

		public int getSize() {
			return (0);
		}
		private void UpdateSize (IAVLNode node) {
			int cnt=0;
			IAVLNode current= node;
			while (current.getSize()!= (current.getLeft().getSize()+current.getRight().getSize()+1)) {
				if ((cnt>0) &&(current.getKey()==node.getKey())) {
					break;
				}
				current.setSize(current.getLeft().getSize()+current.getRight().getSize()+1);
				if (current.getParent()==null) {
					break;
				}
				current=current.getParent();
				cnt++;
			}
		}
	}
}
  

  



