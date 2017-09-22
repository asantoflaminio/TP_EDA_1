
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;

public class AvlTree<T> {

	private Node<T> root;
	private Comparator<T> cmp;
	int totalnodes = 0; //keeps track of the inorder number for horiz. scaling 
    int maxheight=0;//keeps track of the depth of the tree for vert. scaling

	public AvlTree(Comparator<T> cmp) {
		this.cmp = cmp;
	}


	public AvlTree<T> insert(T value) {
		this.root = add(this.root, value);
		return this;
	}

	public Node<T> getRoot() {
		return root;
	}


	private Node<T> add(Node<T> current, T elem) {
		if (current == null) {
			return new Node<T>(elem);
		}

		if (this.cmp.compare(current.value, elem) == 0) {
			throw new EqualsElementException("El elemento al que se quiere acceder ya esta ingresado");
		} else if (this.cmp.compare(current.value, elem) > 0) {
			current.leftTree = add(current.leftTree, elem);
			if (current.rightTree != null) {
				current.height = Math.max(current.leftTree.height, current.rightTree.height) + 1;
			} else {
				current.height = current.leftTree.height + 1;
			}
			if (current.leftTree.height >= 1) {
				if (current.rightTree == null) {
					if (this.cmp.compare(current.leftTree.value, elem) > 0) {
						Node<T> aux = rotateRight(current);
						return aux;
					} else if (this.cmp.compare(current.leftTree.value, elem) < 0) {
						Node<T> aux = rotateRightLeft(current);
						return aux;
					}
				} else if (current.rightTree.height + 1 < current.leftTree.height) {
					if (this.cmp.compare(current.leftTree.value, elem) > 0) {
						Node<T> aux = rotateRight(current);
						return aux;
					} else if (this.cmp.compare(current.leftTree.value, elem) < 0) {
						Node<T> aux = rotateRightLeft(current);
						return aux;
					}
				}
			}
			return current;
		} else if (this.cmp.compare(current.value, elem) < 0) {
			current.rightTree = add(current.rightTree, elem);
			if (current.leftTree != null) {
				current.height = Math.max(current.leftTree.height, current.rightTree.height) + 1;
			} else {
				current.height = current.rightTree.height + 1;
			}
			if (current.rightTree.height >= 1) {
				if (current.leftTree == null) {
					if (this.cmp.compare(current.rightTree.value, elem) > 0) {
						Node<T> aux = rotateLeftRight(current);
						return aux;
					} else if (this.cmp.compare(current.rightTree.value, elem) < 0) {
						Node<T> aux = rotateLeft(current);
						return aux;
					}
				} else if (current.leftTree.height + 1 < current.rightTree.height) {
					if (this.cmp.compare(current.rightTree.value, elem) > 0) {
						Node<T> aux = rotateLeftRight(current);
						return aux;
					} else if (this.cmp.compare(current.rightTree.value, elem) < 0) {
						Node<T> aux = rotateLeft(current);
						return aux;
					}
				}
			}
			return current;
		}
		return current;
	}

	public AvlTree<T> remove(T t) {
		this.root = delete(this.root, t);
		return this;
	}

	private Node<T> delete(Node<T> current, T t) {
		if (current == null) {
			return null;
		}
		if (this.cmp.compare(current.value, t) == 0) {
			if (current.leftTree == null && current.rightTree == null) {
				return null;
			} else if (current.leftTree == null) {
				return current.rightTree;
			} else if (current.rightTree == null) {
				return current.leftTree;
			}
			Node<T> aux;
			if (current.leftTree.height > current.rightTree.height) {
				aux = getRightest(current.leftTree);
				if (this.cmp.compare(aux.value, current.leftTree.value) == 0 && current.leftTree.leftTree == null) {
					aux.leftTree = aux.leftTree.leftTree;
				} else {
					aux.leftTree = current.leftTree;
				}
				aux.rightTree = current.rightTree;
				aux.leftTree = balanceRight(aux.leftTree);
			} else {
				aux = getLeftest(current.rightTree);
				if (this.cmp.compare(aux.value, current.rightTree.value) == 0) {
					aux.rightTree = current.rightTree.rightTree;
				} else {
					aux.rightTree = current.rightTree;
				}
				aux.leftTree = current.leftTree;
				aux.rightTree = balanceLeft(aux.rightTree);
			}
			if (aux.rightTree == null) {
				aux.height = aux.leftTree.height + 1;
			} else if (aux.leftTree == null) {
				aux.height = aux.rightTree.height + 1;
			} else {
				aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
			}
			return aux;
		} else if (this.cmp.compare(current.value, t) < 0) {
			current.rightTree = delete(current.rightTree, t);
			if (current.rightTree == null && current.leftTree == null) {
				current.height = 0;
				return current;
			} else if (current.rightTree == null) {

				if (current.leftTree.height < 1) {
					current.height = current.leftTree.height + 1;
					return current;
				}

				if (current.leftTree.leftTree != null) {
					if (current.leftTree.rightTree == null
							|| current.leftTree.leftTree.height > current.leftTree.rightTree.height) {
						return rotateRight(current);
					} else if (current.leftTree.rightTree.height > current.leftTree.leftTree.height) {
						return rotateRightLeft(current);
					} else {
						Node<T> aux = current.leftTree;
						current.leftTree = aux.rightTree;
						aux.rightTree = current;
						current.height = current.leftTree.height + 1;
						aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
						return aux;
					}
				} else {
					return rotateRightLeft(current);
				}

			} else if (current.leftTree == null) {
				current.height = current.rightTree.height + 1;
				return current;
			} else {

				if (current.leftTree.height > current.rightTree.height + 1) {
					if (current.leftTree.leftTree != null) {
						if (current.leftTree.rightTree == null
								|| current.leftTree.leftTree.height > current.leftTree.rightTree.height) {
							return rotateRight(current);
						} else if (current.leftTree.rightTree.height > current.leftTree.leftTree.height) {
							return rotateRightLeft(current);
						} else {
							Node<T> aux = current.leftTree;
							current.leftTree = aux.rightTree;
							aux.rightTree = current;
							current.height = Math.max(current.leftTree.height, current.rightTree.height) + 1;
							aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
							return aux;
						}
					}
				}

				current.height = Math.max(current.leftTree.height, current.rightTree.height) + 1;
				return current;
			}
		}

		else {

			current.leftTree = delete(current.leftTree, t);
			if (current.rightTree == null && current.leftTree == null) {
				current.height = 0;
				return current;
			} else if (current.leftTree == null) {
				if (current.rightTree.height < 1) {
					current.height = current.rightTree.height + 1;
					return current;
				} else {
					if (current.rightTree.rightTree != null) {
						if (current.rightTree.leftTree == null
								|| current.rightTree.rightTree.height > current.rightTree.leftTree.height) {
							return rotateLeft(current);
						} else if (current.rightTree.leftTree.height > current.rightTree.rightTree.height) {
							return rotateLeftRight(current);
						} else {
							Node<T> aux = current.rightTree;
							current.rightTree = aux.leftTree;
							aux.leftTree = current;
							current.height = current.rightTree.height + 1;
							aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
							return aux;
						}
					} else {
						return rotateLeftRight(current);
					}
				}
			} else if (current.rightTree == null) {
				current.height = current.leftTree.height + 1;
				return current;
			} else {
				if (current.leftTree.height + 1 < current.rightTree.height) {
					if (current.rightTree.rightTree != null) {
						if (current.rightTree.leftTree == null
								|| current.rightTree.rightTree.height > current.rightTree.leftTree.height) {
							return rotateLeft(current);
						} else if (current.rightTree.leftTree.height > current.rightTree.rightTree.height) {
							return rotateLeftRight(current);
						} else {
							Node<T> aux = current.rightTree;
							current.rightTree = aux.leftTree;
							aux.leftTree = current;
							current.height = Math.max(current.rightTree.height, current.leftTree.height) + 1;
							aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
							return aux;
						}
					} else {
						return rotateLeftRight(current);
					}
				}
				current.height = Math.max(current.leftTree.height, current.rightTree.height) + 1;
				return current;
			}
		}
	}

	private Node<T> getRightest(Node<T> c) {
		if (c == null) {
			return null;
		}
		if (c.rightTree == null) {
			return c;
		}
		if (c.leftTree == null) {
			c.height--;
		} else {
			c.height = Math.max(c.leftTree.height + 1, c.rightTree.height);
		}
		if (c.rightTree.rightTree == null) {
			Node<T> aux = c.rightTree;
			c.rightTree = null;
			return aux;
		}
		return getRightest(c.rightTree);
	}

	private Node<T> getLeftest(Node<T> c) {
		if (c == null) {
			return null;
		}
		if (c.leftTree == null) {
			return c;
		}
		if (c.rightTree == null) {
			c.height--;
		} else {
			c.height = Math.max(c.leftTree.height, c.rightTree.height + 1);
		}
		if (c.leftTree.leftTree == null) {
			Node<T> aux = c.leftTree;
			c.leftTree = null;
			return aux;
		}
		return getLeftest(c.leftTree);
	}

	private Node<T> balanceLeft(Node<T> current) {
		if (current == null) {
			return null;
		}
		if (current.height < 2) {
			return current;
		}
		current.leftTree = balanceLeft(current.leftTree);

		if (current.leftTree == null) {
			if (current.rightTree.rightTree != null) {
				if (current.rightTree.leftTree == null
						|| current.rightTree.leftTree.height < current.rightTree.rightTree.height) {
					return rotateLeft(current);
				} else if (current.rightTree.leftTree.height > current.rightTree.rightTree.height) {
					return rotateLeftRight(current);
				} else {
					Node<T> aux = current.rightTree;
					current.rightTree = aux.leftTree;
					aux.leftTree = current;
					current.height = current.rightTree.height + 1;
					aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
					return aux;
				}
			} else {
				return rotateLeftRight(current);
			}
		}
		if (current.leftTree.height < current.rightTree.height + 1) {
			if (current.rightTree.rightTree != null) {
				if (current.rightTree.leftTree == null
						|| current.rightTree.rightTree.height > current.rightTree.leftTree.height) {
					return rotateLeft(current);
				} else if (current.rightTree.leftTree.height > current.rightTree.rightTree.height) {
					return rotateLeftRight(current);
				} else {
					Node<T> aux = current.rightTree;
					current.rightTree = aux.leftTree;
					aux.leftTree = current;
					current.height = Math.max(current.rightTree.height, current.leftTree.height) + 1;
					aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
					return aux;
				}
			} else {
				return rotateLeftRight(current);
			}
		}
		current.height = Math.max(current.leftTree.height, current.rightTree.height) + 1;
		return current;
	}

	private Node<T> balanceRight(Node<T> current) {
		if (current == null) {
			return null;
		}
		if (current.height < 2) {
			return current;
		} else if (current.rightTree == null) {

			if (current.leftTree.height < 1) {
				current.height = current.leftTree.height + 1;
				return current;
			}

			if (current.leftTree.leftTree != null) {
				if (current.leftTree.rightTree == null
						|| current.leftTree.leftTree.height > current.leftTree.rightTree.height) {
					return rotateRight(current);
				} else if (current.leftTree.rightTree.height > current.leftTree.leftTree.height) {
					return rotateRightLeft(current);
				} else {
					Node<T> aux = current.leftTree;
					current.leftTree = aux.rightTree;
					aux.rightTree = current;
					current.height = current.leftTree.height + 1;
					aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
					return aux;
				}
			} else {
				return rotateRightLeft(current);
			}

		} else if (current.leftTree == null) {
			current.height = current.rightTree.height + 1;
			return current;
		} else {

			if (current.leftTree.height > current.rightTree.height + 1) {
				if (current.leftTree.leftTree != null) {
					if (current.leftTree.rightTree == null
							|| current.leftTree.leftTree.height > current.leftTree.rightTree.height) {
						return rotateRight(current);
					} else if (current.leftTree.rightTree.height > current.leftTree.leftTree.height) {
						return rotateRightLeft(current);
					} else {
						Node<T> aux = current.leftTree;
						current.leftTree = aux.rightTree;
						aux.rightTree = current;
						current.height = Math.max(current.leftTree.height, current.rightTree.height) + 1;
						aux.height = Math.max(aux.leftTree.height, aux.rightTree.height) + 1;
						return aux;
					}
				}
			}

			current.height = Math.max(current.leftTree.height, current.rightTree.height) + 1;
			return current;
		}
	}

	public boolean contains(T key) {
		return contains(root, key);
	}

	private boolean contains(Node<T> node, T key) {
		if (node == null)
			return false;

		int aux = cmp.compare(node.value, key);
		if (aux < 0)
			return contains(node.rightTree, key);
		else if (aux > 0)
			return contains(node.leftTree, key);

		return true;
	}

	private Node<T> rotateRight(Node<T> big) {
		Node<T> aux = big.leftTree;
		big.leftTree = aux.rightTree;
		aux.rightTree = big;
		big.height = aux.height - 1;
		return aux;
	}

	private Node<T> rotateRightLeft(Node<T> big) {
		big.leftTree.rightTree.height++;
		big.leftTree = rotateLeft(big.leftTree);
		big = rotateRight(big);
		return big;
	}

	private Node<T> rotateLeft(Node<T> big) {
		Node<T> aux = big.rightTree;
		big.rightTree = aux.leftTree;
		aux.leftTree = big;
		big.height = aux.height - 1;
		return aux;
	}

	private Node<T> rotateLeftRight(Node<T> big) {
		big.rightTree.leftTree.height++;
		big.rightTree = rotateRight(big.rightTree);
		big = rotateLeft(big);
		return big;
	}

	public Boolean check() {
		Check c = new Check();
		check(this.root, c);
		return c.getValue();
	}

	private int check(Node<T> current, Check c) {
		if (current == null) {
			return -1;
		}
		int aux1 = check(current.leftTree, c);
		int aux2 = check(current.rightTree, c);
		if (Math.abs(aux1 - aux2) <= 1) {
			c.setValue(c.getValue() && true);
		} else {
			c.setValue(false);
		}
		return Math.max(aux1, aux2) + 1;
	}

	public void print() {
		ArrayList<Node<T>> ls = new ArrayList<>();
		if (this.root != null) {
			ls.add(this.root);
		}
		Node<T> aux;
		while (!ls.isEmpty()) {
			aux = ls.get(0);
			ls.remove(0);
			if (aux.leftTree != null) {
				ls.add(aux.leftTree);
			}
			if (aux.rightTree != null) {
				ls.add(aux.rightTree);
			}
			System.out.println(aux.value + " height: " + aux.height);
		}
	}
	
	public void draw() {
		this.computeNodePositions(); //posiciones XY de cada nodo
	    this.maxheight=this.treeHeight(this.root);
		DrawnTree tree = new DrawnTree(this);
		
		tree.setVisible(true); 
	}
	
	public int treeHeight(Node<T> t){
		if(t==null) return -1;
	          else return 1 + max(treeHeight(t.leftTree),treeHeight(t.rightTree));
	    }
	
	public int max(int a, int b){
		  if(a>b) return a; else return b;
	}

	public void computeNodePositions() {
	      int depth = 1;
	      inorder_traversal(root, depth);
	}
	
	public void inorder_traversal(Node<T> avl, int depth) { 
	      if (avl != null) {
	        inorder_traversal(avl.leftTree, depth + 1); //sumar 1 a depth (coordenada y) 
	        avl.xpos = totalnodes++; //x es nro de nodo en inorder transversal
	        avl.ypos = depth; // y es la depth
	        inorder_traversal(avl.rightTree, depth + 1);
	      }
	    }
	
	

}