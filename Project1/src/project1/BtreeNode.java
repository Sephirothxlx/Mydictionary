package project1;

//B树节点
public class BtreeNode {
	private Word key[];
	private boolean leaf;
	private BtreeNode c[];
	public BtreeNode(){
		
	}
	public BtreeNode(int t){ //传进t
		this.leaf=true;
		key=new Word[2*t-1];
		for(int i=0;i<2*t-1;i++)
			key[i]=new Word("",""); //关键字都是""
		c=new BtreeNode[2*t];
		for(int i=0;i<2*t;i++)
			c[i]=null; //孩子都是null
	}
	public void setKey(Word word,int index){ //设置关键字
		this.key[index]=word;
	}
	public Word getKey(int index){ //得到关键字
		return key[index];
	}
	public void setC(BtreeNode btn,int index){ //设置孩子
		this.c[index]=btn;
	}
	public BtreeNode getC(int index){ //得到孩子
		return this.c[index];
	}
	public void setLeaf(boolean t){ //设置叶属性
		this.leaf=t;
	}
	public boolean getLeaf(){ //得到叶属性
		return this.leaf;
	}
	public int getN(int t){ //得到不为空的关键字个数
		int n=0;
		for(int i=0;i<2*t-1;i++)
			if(!key[i].getWord().equals(""))
				n++;
		return n;
	}
}
