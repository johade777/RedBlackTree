import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class RedBlackTree<T extends Comparable<? super T>> implements
		Iterable<RedBlackTree.BinaryNode> {
	BinaryNode root;
	public static int rotationCount;
	public boolean inserted;
	public boolean removed;
	public int size = 0;

	public RedBlackTree() {
		this.root = null;
		rotationCount = 0;
	}

	public boolean insert(T obj) {
		if (root == null) {
			root = new BinaryNode(obj);
			root.color = Color.BLACK;
			return true;
		}

		root.insert(obj, null, null, null);
		root.color = Color.BLACK;
		return inserted;
	}

	public boolean remove(T obj) {
		if (obj == null) {
			throw new IllegalArgumentException();
		}
		if (root == null) {
			return false;
		}

		root.remove(obj, null, null, null);
		return removed;
	}

	public int getRotationCount() {
		return this.rotationCount;
	}

	public int height() {
		if (root == null) {
			return -1;
		}
		return root.getHeight();
	}

	public int size() {
		return this.size;
	}

	public Iterator<RedBlackTree.BinaryNode> iterator() {
		return new iterator();
	}

	class BinaryNode {
		private T data;
		private BinaryNode left;
		private BinaryNode right;
		private Color color;

		public BinaryNode() {
			this.data = null;
			this.left = null;
			this.right = null;
			this.color = Color.RED;
		}

		public BinaryNode(T obj) {
			this.data = obj;
			this.left = null;
			this.right = null;
			this.color = Color.RED;
		}

		public void insert(T obj, BinaryNode parent, BinaryNode grandParent,
				BinaryNode greatGrandparent) {
			this.checkRedChildren();
			this.checkBalance(parent, grandParent, greatGrandparent);

			if (this.data.compareTo(obj) == 0) {
				inserted = false;
				return;
			}

			if (this.data.compareTo(obj) > 0) {
				if (this.left == null) {
					this.left = new BinaryNode(obj);
					this.checkBalance(parent, grandParent, greatGrandparent);
					inserted = true;
					return;
				}
				this.left.insert(obj, this, parent, grandParent);
				return;
			}

			if (this.data.compareTo(obj) < 0) {
				if (this.right == null) {
					this.right = new BinaryNode(obj);
					this.checkBalance(parent, grandParent, greatGrandparent);
					inserted = true;
					return;
				}
				this.right.insert(obj, this, parent, grandParent);
				return;
			}
		}

		public void remove(T obj, BinaryNode parent, BinaryNode grandParent, BinaryNode greatGrandParent) {
			
			if (this.left != null && this.right != null) {
				if (this.left.color == Color.BLACK && this.right.color == Color.BLACK) {
					this.changeColor();

					if (this.data.compareTo(obj) == 0) {
						this.step3(obj, parent);
					} else {
						if (this.data.compareTo(obj) > 0) {
							this.left.step2(obj, this, parent);
							return;
						}
						this.right.step2(obj, this, parent);
						return;
					}
				} else {
					this.step2B(obj, null, null, null);
					return;
				}
			}

			if (this.left != null || this.right != null) {
				if (this.data.compareTo(obj) == 0) {
					if (this.left != null) {
						root = left.getMax();
						root.left = null;
						root.color = Color.BLACK;
						return;
					} else {
						//this is wrong fix it
						root = right.getMax();
						right.right = null;
						root.color = Color.BLACK;
						return;
					}
				}
				if (this.left != null) {
					this.left.step2B(obj, this, null, null);
				} else {
					this.right.step2B(obj, this, null, null);
				}
			}

			if (this.data.compareTo(obj) == 0) {
				root = null;
				removed = true;
			}
		}

		public void step2(T obj, BinaryNode parent, BinaryNode grandParent) {
			
			if (this.left == null && this.right == null) {
				this.step2A(obj, parent, grandParent);
				return;
			}
			
			if(this.left == null || this.right == null){
				if(this.left != null){
					if(this.left.color == Color.RED){
						this.left.step2B1(obj, this, null, null);
						return;
					}
				}else{
					if(this.right != null){
						if(this.right.color == Color.RED){
							this.right.step2B1(obj, this, parent, grandParent);
							return;
						}
					}
				}
			}
			
			if (this.right.color == Color.BLACK && this.left.color == Color.BLACK) {
				this.step2A(obj, parent, grandParent);
				return;
			} else {
				this.step2B(obj, parent, grandParent, null);
			}
		}

		public void step2A(T obj, BinaryNode parent, BinaryNode grandParent) {
			BinaryNode sibling;

			if (this.checkLeftRight(parent)) {
				sibling = parent.right;
				
				if (sibling.left == null && sibling.right == null) {
					this.step2A1(obj, parent, sibling);
					return;
				}
				
				
				if(sibling.left == null || sibling.right == null){
					if(sibling.left == null){
						if(sibling.right != null){
							if(sibling.right.color == Color.RED){
								this.step2A3(obj, parent, grandParent, sibling);
								return;
							}
						}
					}else{
						if(sibling.left != null){
							if(sibling.left.color == Color.RED){
								this.step2A2(obj, parent, grandParent, sibling);
								return;
							}
						}
					}
				}
				
				if (sibling.left.color == Color.BLACK && sibling.right.color == Color.BLACK) {
					this.step2A1(obj, parent, sibling);
					return;
				} 
				
				if (sibling.left.color == Color.RED && sibling.right.color == Color.RED) {
					// this.step2A3()
				} else if (sibling.left.color == color.RED) {
					// this.step2A2(obj, parent, grandParent, sibling);
				} else {
					// this.step2A3();
				}
			} else {
				sibling = parent.left;
				if (sibling.left == null && sibling.right == null) {
					this.step2A1(obj, parent, sibling);
					return;
				} 
				
				if(sibling.left == null || sibling.right == null){
					if(sibling.left == null){
						if(sibling.right != null){
							if(sibling.right.color == Color.RED){
								this.step2A2(obj, parent, grandParent, sibling);
								return;
							}
						}
					}else{
						if(sibling.left != null){
							if(sibling.left.color == Color.RED){
								this.step2A3(obj, parent, grandParent, sibling);
								return;
							}
						}
					}
				}
			}
				
				
				if (sibling.left.color == Color.BLACK && sibling.right.color == Color.BLACK) {
					this.step2A1(obj, parent, sibling);
					return;
				} else if (sibling.left.color == Color.RED && sibling.right == null) {
					this.step2A3(obj, parent, grandParent, sibling);
					return;
				} else if (sibling.right.color == Color.RED && sibling.left.color == Color.RED) {
					// this.step2A3();
				} else {
					// this.step2A3();
				}
		}

		public void step2A1(T obj, BinaryNode parent, BinaryNode sibling) {
			
			this.changeColor();
			parent.changeColor();
			sibling.changeColor();
			
			if (this.data.compareTo(obj) == 0) {
				this.step3(obj, parent);
				return;
			}
			if (this.data.compareTo(obj) > 0) {
				this.left.step2(obj, this, parent);
				return;
			}
			if (this.data.compareTo(obj) < 0) {
				if (this.right == null) {
					removed = false;
					return;
				}
				this.right.step2(obj, this, parent);
				return;
			}
		}

		public void step2A2(T obj, BinaryNode parent, BinaryNode grandParent, BinaryNode sibling) {
			
			boolean left;
			
			if(parent.checkLeftRight(grandParent)){
				grandParent.left = parent.right.doubleLeftRotation(parent);
				left = true;
			}else{
				grandParent.right = parent.left.doubleRightRotation(parent);
				left = false;
			}
			
			this.changeColor();
			
			if(left){
				grandParent.left.changeColor();
			}else{
				grandParent.right.changeColor();
			}
			
			if (this.data.compareTo(obj) == 0) {
				this.step3(obj, parent);
				return;
			}
			if (this.data.compareTo(obj) > 0) {
				this.left.step2(obj, this, parent);
				return;
			} else {
				this.right.step2(obj, this, parent);
				return;
			}
		}

		public void step2A3(T obj, BinaryNode parent, BinaryNode grandParent, BinaryNode sibling) {
			
			if (parent.checkLeftRight(grandParent)) {
				grandParent.left = parent.left.singleRightRotation(parent);
			}else{
				grandParent.right = parent.right.singleLeftRotation(parent);
			}
			
			this.changeColor();
			
			if(parent.checkLeftRight(sibling)){
				if(sibling.right != null){
					sibling.right.changeColor();
				}
			}else if (sibling.left != null) {
				sibling.left.changeColor();
			}
			
			if (this.data.compareTo(obj) == 0) {
				this.step3(obj, parent);
				return;
			}
			if (this.data.compareTo(obj) > 0) {
				this.left.step2(obj, this, parent);
				return;
			} else {
				this.right.step2(obj, this, parent);
				return;
			}
		}

		public void step2B(T obj, BinaryNode parent, BinaryNode grandParent, BinaryNode sibling) {
			if (this.data.compareTo(obj) == 0) {
				this.step3(obj, parent);
				return;
			}
			if (this.data.compareTo(obj) > 0) {
				if (this.left == null) {
					removed = false;
					return;
				} else if (this.left.color == Color.RED) {
					this.left.step2B1(obj, this, null, null);
					return;
				} else {
					this.left.step2B2(obj, this, parent, grandParent);
					return;
				}
			} else {
				if (this.right == null) {
					removed = false;
					return;
				} else if (this.right.color == Color.RED) {
					this.right.step2B1(obj, this, null ,null);
				} else{
					this.right.step2B2(obj, this, parent, grandParent);
				}
			}
		}

		public void step2B1(T obj, BinaryNode parent, BinaryNode grandParent, BinaryNode sibling) {
			if (this.data.compareTo(obj) == 0) {
				this.step3(obj, parent);
				return;
			}
			if (this.data.compareTo(obj) > 0) {
				this.left.step2(obj, this, parent);
				return;
			} else {
				this.right.step2(obj, this, parent);
				return;
			}
		}

		public void step2B2(T obj, BinaryNode parent, BinaryNode grandParent, BinaryNode sibling) {
			if(this.checkLeftRight(parent)){
				sibling = parent.right;
			}else{
				sibling = parent.left;
			}
			if(parent == root){
				if(this.checkLeftRight(parent)){
					root = sibling.singleLeftRotation(parent);
				}else{
					root = sibling.singleRightRotation(parent);
				}
			}
			BinaryNode temp = root;
			
			if(this.data.compareTo(obj)==0){
				this.step2B1(obj, parent, grandParent, sibling);
				return;
			}
			this.step2(obj, parent, grandParent);
		}

		public void step3(T obj, BinaryNode parent) {
			if (this.left != null && this.right != null) {
				T variable = this.left.getMax().data;
				BinaryNode node = this.left.getMax();

				if (this.color == Color.RED) {
					this.data = variable;
					this.left.step2(variable, this, parent);
					return;
				} else {
					this.step2B(variable, null, null, null);
					this.data = variable;
					return;
				}
			}

			if (this.left != null || this.right != null) {
				if(this.checkLeftRight(parent)){
					parent.left = this.left;
					this.left.changeColor();
					return;
				}else{
					parent.right = this.right;
					this.right.changeColor();
					return;
				}
			}

			if (this.checkLeftRight(parent)) {
				parent.left = null;
				removed = true;
			} else {
				parent.right = null;
				removed = true;
			}

		}

		public BinaryNode getMax() {
			BinaryNode temp;

			if (this.right == null) {
				return this;
			}
			temp = this.right;
			while (temp.right != null) {
				temp = temp.right;
			}
			return temp;
		}

		public void checkBalance(BinaryNode parent, BinaryNode grandParent,
				BinaryNode greatGrandparent) {

			if (this.left != null) {
				if (this.color == Color.RED && this.left.color == Color.RED) {
					if (parent != root) {
						if (this.checkLeftRight(parent)) {
							grandParent.left = this.singleRightRotation(parent);
							return;
						} else {
							if (this.left != null) {
								if (!parent.checkLeftRight(grandParent)) {
									grandParent.right = this
											.doubleLeftRotation(parent);
									return;
								} else {
									grandParent.left = this
											.doubleLeftRotation(parent);
									return;
								}
							}
							grandParent.right = this
									.singleRightRotation(parent);
							return;
						}
					} else {
						if (!this.checkLeftRight(parent) && this.left != null) {
							root = this.doubleLeftRotation(parent);
							return;
						}
						root = this.singleRightRotation(parent);
						return;
					}
				}
			} else if (this.right != null) {
				if (this.color == Color.RED && this.right.color == Color.RED) {
					if (parent != root) {
						if (this.checkLeftRight(parent)) {
							if (parent.checkLeftRight(grandParent)) {
								grandParent.left = this
										.doubleRightRotation(parent);
								return;
							} else {
								grandParent.right = this
										.doubleRightRotation(parent);
								return;
							}
						} else {
							grandParent.right = this.singleLeftRotation(parent);
							return;
						}
					} else {
						if (this.checkLeftRight(parent) && this.right != null) {
							root = this.doubleRightRotation(parent);
							return;
						}
						root = this.singleLeftRotation(parent);
						return;
					}
				}
			}

			if (parent != null) {
				if (this.color == Color.RED && parent.color == Color.RED) {
					if (parent.checkLeftRight(grandParent)) {
						if (this.checkLeftRight(parent)) {
							if (greatGrandparent == null) {
								root = parent.singleRightRotation(grandParent);
								return;
							} else {
								greatGrandparent.left = parent
										.singleRightRotation(grandParent);
								return;
							}
						} else {
							if (greatGrandparent == null) { // here
								if (grandParent == root) {
									root = parent
											.doubleRightRotation(grandParent);
									return;
								}
							}
							greatGrandparent.left = this
									.doubleRightRotation(parent);
							return;
						}
					} else {
						if (this.checkLeftRight(parent)) {
							if (greatGrandparent == null) {
								if (grandParent == root) {
									root = parent
											.doubleLeftRotation(grandParent);
									return;
								}
							} else {
								greatGrandparent.right = this
										.doubleLeftRotation(parent);
								return;
							}
						} else {
							if (greatGrandparent == null) {
								root = parent.singleLeftRotation(grandParent);
								return;
							} else {
								greatGrandparent.right = parent
										.singleLeftRotation(grandParent);
								return;
							}
						}
					}
				}
			}
		}

		private BinaryNode doubleRightRotation(BinaryNode parent) {
			BinaryNode temp;
			BinaryNode temp2;
			BinaryNode temp4;
			if (this.right != null) {
				temp = this.right.right;
			} else {
				temp = this.right;
			}
			if (this.left != null) {
				temp2 = this.right.left;
			} else {
				temp2 = this.left;
			}

			BinaryNode temp3 = this.right;
			temp4 = temp3.right;
			temp3.changeColor();
			this.right = temp2;
			parent.left = temp;
			temp3.left = this;
			temp3.right = parent;

			parent.changeColor();
			rotationCount += 2;
			return temp3;
		}

		private BinaryNode doubleLeftRotation(BinaryNode parent) {
			// BinaryNode temp = this.left.left;
			// BinaryNode temp2 = this.left.right;
			BinaryNode temp;
			BinaryNode temp2;
			BinaryNode temp4;
			if (this.right != null) {
				temp = this.right.right;
			} else {
				temp = this.right;
			}
			if (this.left != null) {
				temp2 = this.left.right;
			} else {
				temp2 = this.left;
			}

			BinaryNode temp3 = this.left;
			temp4 = temp3.left;
			temp3.changeColor();
			this.left = temp2;
			parent.right = temp;
			temp3.right = this;
			temp3.left = parent;
			parent.right = temp4;

			parent.changeColor();
			rotationCount += 2;
			return temp3;
		}

		public BinaryNode singleLeftRotation(BinaryNode parent) {
			BinaryNode temp = this.left;
			this.left = parent;
			parent.right = temp;

			this.changeColor();
			parent.changeColor();
			rotationCount++;
			return this;
		}

		public BinaryNode singleRightRotation(BinaryNode parent) {
			BinaryNode temp = this.right;
			this.right = parent;
			parent.left = temp;

			this.changeColor();
			parent.changeColor();
			rotationCount++;
			return this;
		}

		public boolean checkLeftRight(BinaryNode parent) {
			if (this == parent.left) {
				return true;
			}
			return false;
		}

		public T getElement() {
			return this.data;
		}

		public Color getColor() {
			return this.color;
		}

		public void changeColor() {
			this.color = (this.color == Color.RED) ? this.color = Color.BLACK
					: Color.RED;
		}

		public int getHeight() {
			if (this.left == null && this.right == null) {
				return 0;
			} else if (this.left == null && this.right != null) {
				return 1 + right.getHeight();
			} else if (this.left != null && this.right == null) {
				return 1 + left.getHeight();
			}
			return 1 + Math.max(left.getHeight(), right.getHeight());
		}

		public void checkRedChildren() {
			if (this.left != null && this.right != null) {
				if (this.left.color == Color.RED
						&& this.right.color == Color.RED) {
					this.changeColor();
					this.left.changeColor();
					this.right.changeColor();
				}
			}
		}

		public BinaryNode getLeftChild() {
			// TODO Auto-generated method stub.
			return this.left;
		}

		public BinaryNode getRightChild() {
			// TODO Auto-generated method stub.
			return this.right;
		}
	}

	public class iterator implements Iterator<RedBlackTree.BinaryNode> {
		private Stack<BinaryNode> storage = new Stack<BinaryNode>();
		private BinaryNode temp = new BinaryNode();

		/**
		 * Constructs an iterator and pushes the root on the stack
		 */
		public iterator() {
			if (root != null) {
				storage.push(root);
			}
		}

		/*
		 * Checks to see if storage stack is empty
		 */

		@Override
		public boolean hasNext() {
			return (!this.storage.isEmpty());
		}

		/*
		 * Calls the next element in a pre-order traversal
		 */

		@Override
		public RedBlackTree.BinaryNode next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			this.temp = this.storage.pop();
			if (this.temp.right != null) {
				this.storage.push(this.temp.right);
			}
			if (this.temp.left != null) {
				this.storage.push(this.temp.left);
			}
			return this.temp;
		}
	}

	enum Color {
		RED, BLACK;
	}
}