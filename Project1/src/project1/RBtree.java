package project1;
import java.io.*;

//红黑树
public class RBtree {
	private RBtreeNode nil;
	private RBtreeNode root; //两个基本属性

	public RBtree() {
		this.nil = new RBtreeNode();
		this.nil.setColor("BLACK"); //nil应该是黑色的
		this.root = this.nil;
	}
	public RBtreeNode getNil(){
		return this.nil;
	}
	public RBtreeNode getRoot(){
		return this.root;
	}
	
	public int compare(String s0,String s1){ //compare字符串的词典序，使用原生的compareTo方法
		String s2=s0.toLowerCase();
		String s3=s1.toLowerCase(); //由于有大写的单词，转为小写再比较
		if(s2.charAt(0)==s3.charAt(0))
			return s0.compareTo(s1); //如果首字母相同，直接比较原字符串即可
		return s2.compareTo(s3); //不同就要比较变为小写后的字符串
	}
	
	public RBtreeNode search(RBtreeNode x, String k) { //search方法
		while (x != this.nil && !k.equals(x.getWord().getWord())) {//不为空或者没有就往下继续
			if (compare(k,x.getWord().getWord()) < 0)//比较，确定该往哪边走
				x = x.getLeft();
			else
				x = x.getRight();
		}
		return x;
	}

	public void rangeSearch(RBtreeNode x, String key0,String key1){ //范围搜索
		if(!x.getWord().getWord().equals("")){
			if(compare(x.getWord().getWord(),key0)>=0&&compare(x.getWord().getWord(),key1)<=0){ //模仿遍历的方法，分为三类，第一类是父节点在范围内
				System.out.println(x.getWord().getWord()+":"+x.getWord().getMeaning());
				rangeSearch(x.getLeft(),key0,key1);
				rangeSearch(x.getRight(),key0,key1);
			}else if(compare(x.getWord().getWord(),key0)<0){ //父节点比左端点小
				rangeSearch(x.getRight(),key0,key1);
			}else if(compare(x.getWord().getWord(),key1)>0){ //父节点比右端大
				rangeSearch(x.getLeft(),key0,key1);
			}
		}
	}
	
	public void leftRotate(RBtreeNode x) { //左旋
		RBtreeNode y = x.getRight(); //以x为轴，取其右孩子
		x.setRight(y.getLeft()); //y的左孩子要处理
		if (y.getLeft() != this.nil)
			y.getLeft().setParent(x);
		y.setParent(x.getParent());
		if (x.getParent() == this.nil) //根节点的特殊情况
			this.root = y;
		else {
			if(x==x.getParent().getLeft()) //分类，看应该把y接在哪里
				x.getParent().setLeft(y);
			else
				x.getParent().setRight(y);
		}
		y.setLeft(x);
		x.setParent(y); //
	}

	public void rightRotate(RBtreeNode x) { //右旋，模仿左旋，这里不做叙述了
		RBtreeNode y = x.getLeft();
		x.setLeft(y.getRight());
		if (y.getRight() != this.nil)
			y.getRight().setParent(x);
		y.setParent(x.getParent());
		if (x.getParent() == this.nil)
			this.root = y;
		else {
			if(x==x.getParent().getRight())
				x.getParent().setRight(y);
			else
				x.getParent().setLeft(y);
		}
		y.setRight(x);
		x.setParent(y);
	}

	public void insert(RBtreeNode z) { //插入操作
		RBtreeNode y = this.nil;
		RBtreeNode x = this.root;
		while (x != this.nil) { //如果根节点为空，说明走到头了
			y = x;
			if (compare(z.getWord().getWord(),x.getWord().getWord()) < 0)
				x = x.getLeft();
			else
				x = x.getRight();  //确定应该插入到哪里
		}
		z.setParent(y);
		if (y == this.nil) //根节点的情况
			this.root = z;
		else {
			if (compare(z.getWord().getWord(),y.getWord().getWord()) < 0)
				y.setLeft(z);
			else
				y.setRight(z); //确定插入位置
		}
		z.setLeft(this.nil);
		z.setRight(this.nil); //设置其孩子
		insertFixup(z); //修复
	}

	public void insertFixup(RBtreeNode z) { //修复过程
		RBtreeNode y = new RBtreeNode();
		while (z.getParent().getColor().equals("RED")) { //如果父亲为红
			if (z.getParent() == z.getParent().getParent().getLeft()) { //如果z的父亲是其祖父的左孩子
				y = z.getParent().getParent().getRight();
				if (y.getColor().equals("RED")) { //如果叔叔也是红色
					z.getParent().setColor("BLACK");
					y.setColor("BLACK"); //交换祖父与叔父的颜色
					z.getParent().getParent().setColor("RED");
					z = z.getParent().getParent();
				} else {
					if (z == z.getParent().getRight()) { //如果z的父亲是右孩子
						z = z.getParent();
						leftRotate(z);
					}
					z.getParent().setColor("BLACK");
					z.getParent().getParent().setColor("RED");
					rightRotate(z.getParent().getParent()); //祖父黑变红  父亲红变黑
				}
			} else {  //对称操作，这里不做详述
				y = z.getParent().getParent().getLeft();
				if (y.getColor().equals("RED")) {
					z.getParent().setColor("BLACK");
					y.setColor("BLACK");
					z.getParent().getParent().setColor("RED");
					z = z.getParent().getParent();
				} else {
					if (z == z.getParent().getLeft()) {
						z = z.getParent();
						rightRotate(z);
					} 
					z.getParent().setColor("BLACK");
					z.getParent().getParent().setColor("RED");
					leftRotate(z.getParent().getParent());
				}
			}
		}
		this.root.setColor("BLACK");
	}

	public void transplant(RBtreeNode u, RBtreeNode v) { //移植操作
		if (u.getParent() == this.nil)
			this.root = v;
		else {
			if (u == u.getParent().getLeft())
				u.getParent().setLeft(v);
			else
				u.getParent().setRight(v); //确定应该怎么接
		}
		v.setParent(u.getParent());  //用v取替换u
	}

	public RBtreeNode min(RBtreeNode x) { //取这个节点之后的最小，即后继
		while (x.getLeft() != this.nil)
			x = x.getLeft();
		return x;
	}

	public void deleteFixup(RBtreeNode x) { //删除修复
		RBtreeNode w;
		while (x != this.root && x.getColor().equals("BLACK")) {
			if (x == x.getParent().getLeft()) { //大的分类
				w = x.getParent().getRight();
				if (w.getColor().equals("RED")) { //情况1
					w.setColor("BLACK");
					x.getParent().setColor("RED"); //先变色再转
					leftRotate(x.getParent());
					w = x.getParent().getRight();
				}
				if (w.getLeft().getColor().equals("BLACK") //情况2
						&& w.getRight().getColor().equals("BLACK")) {
					w.setColor("RED");
					x = x.getParent();
				} else {
					if (w.getRight().getColor().equals("BLACK")) { //情况3
						w.getLeft().setColor("BLACK");
						w.setColor("RED");
						rightRotate(w);
						w = x.getParent().getRight();
					}
					w.setColor(w.getParent().getColor());  //情况4，情况3可以转到4
					x.getParent().setColor("BLACK");
					w.getRight().setColor("BLACK");
					leftRotate(x.getParent());
					x = this.root;
				}
			} else { //对称的操作，这里不做详述
				w = x.getParent().getLeft();
				if (w.getColor().equals("RED")) {
					w.setColor("BLACK");
					x.getParent().setColor("RED");
					rightRotate(x.getParent());
					w = x.getParent().getLeft();
				}
				if (w.getRight().getColor().equals("BLACK")
						&& w.getLeft().getColor().equals("BLACK")) {
					w.setColor("RED");
					x = x.getParent();
				} else {
					if (w.getLeft().getColor().equals("BLACK")) {
						w.getRight().setColor("BLACK");
						w.setColor("RED");
						leftRotate(w);
						w = x.getParent().getLeft();
					}
					w.setColor(w.getParent().getColor());
					x.getParent().setColor("BLACK");
					w.getLeft().setColor("BLACK");
					rightRotate(x.getParent());
					x = this.root;
				}
			}
		}
		x.setColor("BLACK");
	}

	public void delete(String key) { //删除的过程
		RBtreeNode z=search(this.getRoot(),key);
		RBtreeNode y=z;
		RBtreeNode x;
		String y_original_color = y.getColor();
		if (z.getLeft() == this.nil) { //如果左孩子为空，则接右孩子
			x = z.getRight();
			transplant(z, z.getRight());
		} else if (z.getRight() == this.nil) { //如果右孩子为空，则接左孩子
				x = z.getLeft();
				transplant(z, z.getLeft());
		} else {  //都不空，接后继
			y=min(z.getRight()); //得到后继
			y_original_color = y.getColor();
			x = y.getRight();
			if (y.getParent() == z) //y直接是z孩子的情况
				x.setParent(y);
			else {
				transplant(y, y.getRight()); //否则要处理y的孩子
				y.setRight(z.getRight());
				y.getRight().setParent(y);
			}
			transplant(z, y); //移植
			y.setLeft(z.getLeft()); //设置好指向的对象
			y.getLeft().setParent(y);
			y.setColor(z.getColor());
		}
		if (y_original_color.equals("BLACK")) //继承人是黑色则需要修复
			deleteFixup(x);
	}
	public void inorder(PrintWriter out,RBtreeNode x,int level){ //中序遍历
		if(!x.getWord().getWord().equals("")){ //如果节点不为空
			if((x.getLeft()!=this.nil&&x.getRight()!=this.nil)){ //左右孩子都不为空
				inorder(out,x.getLeft(),level+1);
				if(x.getParent().getWord().getWord().equals("")){ //判断当前节点是左孩子还是右孩子
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
				inorder(out,x.getRight(),level+1);
			}else if(x.getLeft()==this.nil&&x.getRight()!=this.nil){//左孩子为空，则不需要向左边遍历下去
				out.println("Level="+(level+1)+" child=0 "+"null"+" (BLACK)");
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
				inorder(out,x.getRight(),level+1);
			}else if(x.getLeft()!=this.nil&&x.getRight()==this.nil){ //右孩子为空，则不需要向右边遍历下去
				inorder(out,x.getLeft(),level+1);
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
				out.println("Level="+(level+1)+" child=1 "+"null"+" (BLACK)");
			}else if((x.getLeft()==this.nil&&x.getRight()==this.nil)){ //都为空，输出即可，不用遍历
				out.println("Level="+(level+1)+" child=0 "+"null"+" (BLACK)");
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
				out.println("Level="+(level+1)+" child=1 "+"null"+" (BLACK)");
			}
		}
	}
	public void preorder(PrintWriter out,RBtreeNode x,int level){ //前序遍历，模仿上面
		if(!x.getWord().getWord().equals("")){
			if((x.getLeft()!=this.nil&&x.getRight()!=this.nil)){
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
				preorder(out,x.getRight(),level+1);
				preorder(out,x.getLeft(),level+1);
			}else if(x.getLeft()==this.nil&&x.getRight()!=this.nil){
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
				out.println("Level="+(level+1)+" child=0 "+"null"+" (BLACK)");
				preorder(out,x.getRight(),level+1);
			}else if(x.getLeft()!=this.nil&&x.getRight()==this.nil){
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
				preorder(out,x.getLeft(),level+1);
				out.println("Level="+(level+1)+" child=1 "+"null"+" (BLACK)");
			}else if((x.getLeft()==this.nil&&x.getRight()==this.nil)){
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
				out.println("Level="+(level+1)+" child=0 "+"null"+" (BLACK)");
				out.println("Level="+(level+1)+" child=1 "+"null"+" (BLACK)");
			}
		}
	}
	public void postorder(PrintWriter out,RBtreeNode x,int level){ //后序遍历，模仿上面
		if(!x.getWord().getWord().equals("")){
			if((x.getLeft()!=this.nil&&x.getRight()!=this.nil)){
				postorder(out,x.getLeft(),level+1);
				postorder(out,x.getRight(),level+1);
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
			}else if(x.getLeft()==this.nil&&x.getRight()!=this.nil){
				out.println("Level="+(level+1)+" child=0 "+"null"+" (BLACK)");
				postorder(out,x.getRight(),level+1);
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
			}else if(x.getLeft()!=this.nil&&x.getRight()==this.nil){
				postorder(out,x.getLeft(),level+1);
				out.println("Level="+(level+1)+" child=1 "+"null"+" (BLACK)");
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
			}else if((x.getLeft()==this.nil&&x.getRight()==this.nil)){
				out.println("Level="+(level+1)+" child=0 "+"null"+" (BLACK)");
				out.println("Level="+(level+1)+" child=1 "+"null"+" (BLACK)");
				if(x.getParent().getWord().getWord().equals("")){
					out.println("Level="+level+" child=-1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getLeft()==x){
					out.println("Level="+level+" child=0 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}else if(x.getParent().getRight()==x){
					out.println("Level="+level+" child=1 "+x.getWord().getWord()+" ("+x.getColor()+")");
				}
			}
		}
	}
}
