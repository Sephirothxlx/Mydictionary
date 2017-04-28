package project1;

//红黑树节点
public class RBtreeNode {
	private String color = "RED";
	private RBtreeNode parent;
	private RBtreeNode left;
	private RBtreeNode right;
	private Word word=new Word("","");//一开始关键字为空

	public RBtreeNode() {

	}

	public RBtreeNode(Word word) {
		this.word = word;
	}
	
	public String getColor() { //得到颜色
		return this.color;
	}

	public void setColor(String color) { //设置颜色
		this.color = color;
	}

	public RBtreeNode getParent() { //得到父节点
		return this.parent;
	}

	public void setParent(RBtreeNode parent) { //设置父节点
		this.parent = parent;
	}

	public RBtreeNode getLeft() { //得到左孩子
		return this.left;
	}

	public void setLeft(RBtreeNode left) { //设置左孩子
		this.left = left;
	}

	public RBtreeNode getRight() { //得到右孩子
		return this.right;
	}

	public void setRight(RBtreeNode right) { //设置右孩子
		this.right = right;
	}
	public Word getWord(){ //得到关键字
		return this.word;
	}
	public void setWord(Word word){ //设置关键字
		this.word=word;
	}
}
