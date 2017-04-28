package project1;

import java.io.*;
import java.util.ArrayList;

//B树
public class Btree {
	private BtreeNode root;
	private int t = 10; //t的大小一开始是10

	public Btree() {
		this.root = new BtreeNode(t);
	}
	public BtreeNode getRoot(){
		return this.root;
	}
	
	public int compare(String s0,String s1){ //字符串比较，使用了原生的compareTo
		String s2=s0.toLowerCase();
		String s3=s1.toLowerCase(); //由于有大写的单词，转为小写再比较
		if(s2.charAt(0)==s3.charAt(0))
			return s0.compareTo(s1); //如果首字母相同，直接比较原字符串即可
		return s2.compareTo(s3); //不同就要比较变为小写后的字符串
	}
	
	public Word search(BtreeNode x, String key) { //单个查找
		int i = 0;
		while (i < x.getN(t) && compare(key,x.getKey(i).getWord()) > 0)
			i++;
		if (i < x.getN(t) && compare(key,x.getKey(i).getWord()) == 0)
			return x.getKey(i);
		else if (x.getLeaf())
			return null;
		else
			return search(x.getC(i), key);
	}
	
	public void rangeSearch(BtreeNode x, String key0,String key1){ //范围查找，模仿遍历的过程
		if(x!=null){
			int i;
			for(i=0;i<x.getN(t);i++){
				if(compare(x.getKey(i).getWord(),key0)>=0&&compare(x.getKey(i).getWord(),key1)<=0){ //如果被夹在范围之间
					System.out.println(x.getKey(i).getWord()+":"+x.getKey(i).getMeaning());
					rangeSearch(x.getC(i),key0,key1);
				}else if(compare(x.getKey(i).getWord(),key1)>0){ //在范围右边
					break;
				}
			}
			rangeSearch(x.getC(i),key0,key1);
		}
	}

	public void btreeSplit(BtreeNode x, int i) { //分裂节点
		BtreeNode z = new BtreeNode(t);
		BtreeNode y = x.getC(i);
		z.setLeaf(y.getLeaf());
		for (int j = 0; j < t - 1; j++)
			z.setKey(y.getKey(j + t), j); //z取走t-1个关键字
		if (!y.getLeaf())     //如果y有孩子，再取走孩子
			for (int j = 0; j <=t - 1; j++)
				z.setC(y.getC(j + t), j);
		for (int j = x.getN(t); j >= i + 1; j--) //给升上去的分配孩子
			x.setC(x.getC(j), j + 1);
		x.setC(z, i + 1);
		for (int j = x.getN(t) - 1; j >= i; j--) //调整关键字的位置
			x.setKey(x.getKey(j), j + 1);
		x.setKey(y.getKey(t - 1), i);
		for (int j = t-1; j < 2*t-1; j++){ //前一个节点后面都为空了
			y.setKey(new Word("",""), j);
			y.setC(null, j+1);
		}
	}

	public void insertNonfull(BtreeNode x, Word key) { //未满时的插入
		int i = x.getN(t) - 1;
		if (x.getLeaf()) {
			while (i >= 0 && compare(key.getWord(),x.getKey(i).getWord()) < 0) { //确定插入位置
				x.setKey(x.getKey(i), i + 1);
				i--;
			}
			x.setKey(key, i + 1);
		} else {
			while (i >= 0 && compare(key.getWord(),x.getKey(i).getWord()) < 0) //确定应该向哪个孩子递归
				i--;
			i++;
			if (x.getC(i).getN(t) == 2 * t - 1) {  //关键在于这句
				btreeSplit(x, i);                  //遇到满的节点就分裂
				if (compare(key.getWord(),x.getKey(i).getWord()) > 0)
					i++;
			}
			insertNonfull(x.getC(i), key);
		}
	}

	public void insert(Word key) {   //插入
		BtreeNode r = this.root;     
		if (this.root.getN(t) == 2 * t - 1) {  //处理了了根节点为满的情况
			BtreeNode s = new BtreeNode(t);
			this.root = s;
			s.setLeaf(false);  //设置叶属性
			s.setC(r, 0);
			btreeSplit(s, 0);  //分裂
			insertNonfull(s, key);
		} else
			insertNonfull(r, key);
	}

	public Word getMin(BtreeNode x) {  //得到最小值
		Word y = new Word("", "");
		while (!x.getLeaf())
			x = x.getC(0);
		y = x.getKey(0);
		return y;
	}

	public Word successor(BtreeNode x) { //得到后继
		return getMin(x);
	}

	public Word getMax(BtreeNode x) {   //得到最大值
		while (!x.getLeaf())
			for (int i = x.getN(t); i >= 0; i--)
				if (x.getC(i) != null) {
					x = x.getC(i);
					break;
				}
		return x.getKey(x.getN(t)-1);
	}

	public Word predecessor(BtreeNode x) {  //得到前驱
		return getMax(x);
	}
	
	public void combine(BtreeNode x,int index){ //合并  
		BtreeNode y=x.getC(index);
		BtreeNode z=x.getC(index+1);  //把index+1合并到index中去
		int n = y.getN(t)+1;
		int m=n;
		y.setKey(x.getKey(index), n-1);
		for (int i = 0; i < z.getN(t); i++) { //设置关键字
			y.setKey(z.getKey(i), n);
			n++;
		}
		for (int i = 0; i <=x.getC(index + 1).getN(t); i++) { //设置孩子
			y.setC(z.getC(i), m);
			m++;
		}
		for (int i = index + 2; i <= x.getN(t); i++) //把父节点的一个关键字拿下了来，所以要调整其他孩子的位置
			x.setC(x.getC(i), i - 1);
		x.setC(null, x.getN(t));
		for (int i = index + 1; i < x.getN(t); i++) //调整关键字的位置
			x.setKey(x.getKey(i), i - 1);
		x.setKey(new Word("", ""), x.getN(t) - 1);
	}
	
	public void delete(BtreeNode x, Word key) {  //删除 主要参考书上的情况
		int index = 0;
		while (index < x.getN(t)&&compare(key.getWord(),x.getKey(index).getWord())>0) //确定位置
			index++;
		if (x.getLeaf()&&compare(key.getWord(),x.getKey(index).getWord()) == 0) { //情况1
			for (int i = index+1; i < x.getN(t); i++)
				x.setKey(x.getKey(i), i - 1);
			x.setKey(new Word("", ""), x.getN(t)-1);
		} else if (index<x.getN(t)&&!x.getLeaf()&&compare(key.getWord(),x.getKey(index).getWord()) == 0) { //情况2
			if (x.getC(index).getN(t) >= t) { //情况2a
				x.setKey(predecessor(x.getC(index)), index); //找前驱
				BtreeNode b=x.getC(index);
				while (!b.getLeaf())
					for (int i = b.getN(t); i >= 0; i--)
						if (b.getC(i) != null) {
							b = b.getC(i);
							break;
						}
				if(b.getN(t)==0){
					b=null;
				}else
					b.setKey(new Word("",""),b.getN(t)-1);
			} else if (x.getC(index + 1).getN(t) >= t) {  //情况2b
				x.setKey(successor(x.getC(index + 1)), index); //找后继
				BtreeNode b=x.getC(index + 1);
				while (!b.getLeaf())
					b = b.getC(0);
				for(int j=0;j<b.getN(t)-1;j++)
					b.setKey(b.getKey(j+1), j);
				if(b.getN(t)==0){
					b=null;
				}else
					b.setKey(new Word("",""), b.getN(t)-1);
			} else {    //情况2c
				combine(x,index); //需要合并
				if(x.getN(t)==0){
					this.root=x.getC(index);
					x=null;
					delete(this.root,key);
				}else
					delete(x.getC(index),key);
			}
		} else {   //情况3
			if (x.getC(index).getN(t) > t - 1) { //情况3a
				delete(x.getC(index), key);
			} else if (index-1>=0&&x.getC(index - 1).getN(t) > t - 1) { //情况3b，要分4种小情况
				int c0=x.getC(index).getN(t);                           //前一个兄弟孩子可以借
				for(int i=c0-1;i>=0;i--)
					x.getC(index).setKey(x.getC(index).getKey(i), i+1);
				for(int i=c0;i>=0;i--)
					x.getC(index).setC(x.getC(index).getC(i), i+1);
				x.getC(index).setKey(x.getKey(index-1), 0);
				x.getC(index).setC(x.getC(index-1).getC(x.getC(index-1).getN(t)), 0);
				x.setKey(x.getC(index-1).getKey(x.getC(index-1).getN(t)-1), index-1);
				x.getC(index-1).setC(null, x.getC(index-1).getN(t));
				x.getC(index-1).setKey(new Word("",""), x.getC(index-1).getN(t)-1); //这么多都是调整孩子和关键字的值和位置
				delete(x.getC(index),key); //将x移至下面
			} else if (index+1<=x.getN(t)&&x.getC(index + 1).getN(t) > t - 1) {  //后一个兄弟孩子可以借
				x.getC(index).setKey(x.getKey(index), x.getC(index).getN(t));
				x.getC(index).setC(x.getC(index+1).getC(0), x.getC(index).getN(t));
				x.setKey(x.getC(index+1).getKey(0),index);
				int c1=x.getC(index+1).getN(t);
				for(int i=0;i<c1-1;i++)
					x.getC(index+1).setKey(x.getC(index+1).getKey(i+1), i);
				for(int i=0;i<c1;i++)
					x.getC(index+1).setC(x.getC(index+1).getC(i+1), i);
				x.getC(index+1).setC(null, x.getC(index+1).getN(t));
				x.getC(index+1).setKey(new Word("",""), x.getC(index+1).getN(t)-1); //调整孩子和关键字的位置
				delete(x.getC(index),key); //将x移至下面
			} else if (index+1<=x.getN(t)) {  //都借不了，而且有后一个兄弟孩子
				combine(x,index);
				if(x.getN(t)==0){     //如果是根节点被合并了，就要重新设置根节点
					this.root=x.getC(index);
					x=null;
					delete(this.root,key);
				}else
					delete(x.getC(index),key);
			}else{                            //都借不了，而且有前一个兄弟孩子，没有后一个
				combine(x,index-1);
				if(x.getN(t)==0){  //判断是否需要重新设置根节点
					this.root=x.getC(index-1);
					x=null;
					delete(this.root,key);
				}else
					delete(x.getC(index-1),key);
			}
		}
	}
	public void preorder(PrintWriter out,BtreeNode x,int level,int index){ //前序遍历
		if(x!=null){
			for(int i=0;i<=x.getN(t);i++)
				preorder(out,x.getC(i),level+1,i);
			if(index==-1){
				out.print("Level="+level+" child=-1 |");
				for(int i=0;i<x.getN(t);i++)
					out.print(x.getKey(i).getWord()+"|");
				out.println();
			}
			else {
				out.print("Level="+level+" child="+index+" |");
				for(int i=0;i<x.getN(t);i++)
					out.print(x.getKey(i).getWord()+"|");
				out.println();
			}
		}
	}
	public void postorder(PrintWriter out,BtreeNode x,int level,int index){ //后序遍历
		if(x!=null){
			if(index==-1){
				out.print("Level="+level+" child=-1 |");
				for(int i=0;i<x.getN(t);i++)
					out.print(x.getKey(i).getWord()+"|");
				out.println();
			}
			else {
				out.print("Level="+level+" child="+index+" |");
				for(int i=0;i<x.getN(t);i++)
					out.print(x.getKey(i).getWord()+"|");
				out.println();
			}
			for(int i=0;i<=x.getN(t);i++)
				postorder(out,x.getC(i),level+1,i);
		}
	}
}
