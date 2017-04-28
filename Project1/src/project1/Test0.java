package project1;

import java.util.*;
import java.io.*;

//红黑树性能测试
public class Test0 {
	public static void initial(RBtree tree){ //初始化树
		File f=new File("src/project1/sample files/1_initial.txt");
		try {
			Scanner in = new Scanner(f);
			String str0 = in.nextLine();
			String str1 = "";
			if (str0.equals("INSERT")) { //判断是否是INSERT
				while (in.hasNextLine()) {
					str0 = in.nextLine();
					str1 = in.nextLine();
					if (tree.search(tree.getRoot(), str0) == tree.getNil())
						tree.insert(new RBtreeNode(new Word(str0, str1)));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		long[] t0 = new long[25];
		System.out.println("initial插入测试"); //使用initial测试
		try {
			File f=new File("src/project1/sample files/1_initial.txt");
			for (int i = 0; i < 1000; i++) {
				Scanner in = new Scanner(f);
				RBtree tree = new RBtree();
				String str0 = in.nextLine();
				String str1 = "";
				int n0 = 0;
				long start = 0, end = 0;
				if (str0.equals("INSERT")) { //判断是否是INSERT
					start = System.nanoTime() / 1000;
					while (in.hasNextLine()) {
						str0 = in.nextLine();
						str1 = in.nextLine();
						if (tree.search(tree.getRoot(), str0) == tree.getNil()) //判断是否存在这个节点的操作应当计算时间
							tree.insert(new RBtreeNode(new Word(str0, str1)));
						n0++;
						if (n0 > 0 && n0 % 200 == 0) {
							end = System.nanoTime() / 1000;
							t0[n0 / 200 - 1] = t0[n0 / 200 - 1] + end - start;
							start = System.nanoTime() / 1000;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < 25; j++)
			System.out.print("第" + j + "组:" + t0[j] / 1000 + "μs\n"); //把每组打出来
		System.out.println();

		long[] t1 = new long[5];
		System.out.println("delete删除测试"); //删除测试基本过程和插入一样
		try {
			File f=new File("src/project1/sample files/2_delete.txt");
			for (int i = 0; i < 1000; i++) {
				Scanner in = new Scanner(f);
				RBtree tree = new RBtree();
				initial(tree);
				String str0 = in.nextLine();
				int n0 = 0;
				long start = 0, end = 0;
				if (str0.equals("DELETE")) {
					start = System.nanoTime() / 1000;
					while (in.hasNextLine()) {
						str0 = in.nextLine();
						if (tree.search(tree.getRoot(), str0) != tree.getNil()) //判断是否存在这个单词，这个时间应当计算进去
							tree.delete(str0);
						n0++;
						if (n0 > 0 && n0 % 200 == 0) { //每200个单词计算一下时间
							end = System.nanoTime() / 1000;
							t1[n0 / 200-1] = t1[n0 / 200-1] + end - start;
							start = System.nanoTime() / 1000;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < 5; j++)
			System.out.print("第" + j + "组:" + t1[j] / 1000 + "μs\n"); //取1000次的平均值
		System.out.println();

		long[] t2 = new long[5];
		System.out.println("isert插入测试");
		try {
			File f=new File("src/project1/sample files/3_insert.txt");
			for (int i = 0; i < 1000; i++) {
				Scanner in = new Scanner(f);
				RBtree tree = new RBtree();
				initial(tree);
				String str0 = in.nextLine();
				String str1 = "";
				int n0 = 0;
				long start = 0, end = 0;
				if (str0.equals("INSERT")) {
					start = System.nanoTime() / 1000;
					while (in.hasNextLine()) {
						str0 = in.nextLine();
						str1 = in.nextLine();
						if (tree.search(tree.getRoot(), str0) == tree.getNil())
							tree.insert(new RBtreeNode(new Word(str0, str1)));
						n0++;
						if (n0 > 0 && n0 % 200 == 0) {
							end = System.nanoTime() / 1000;
							t2[n0 / 200-1] = t2[n0 / 200-1] + end - start;
							start = System.nanoTime() / 1000;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < 5; j++)
			System.out.print("第" + j + "组:" + t2[j] / 1000 + "μs\n");
		System.out.println();
		
		long[] t3 = new long[25];
		System.out.println("搜索测试"); //搜索测试使用insert进行
		try {
			File f=new File("src/project1/sample files/1_initial.txt");
			for (int i = 0; i < 1000; i++) { //依旧是1000次
				Scanner in = new Scanner(f);
				RBtree tree = new RBtree();
				initial(tree);
				String str0 = in.nextLine();
				String str1 = "";
				int n0 = 0;
				long start = 0, end = 0;
				if (str0.equals("INSERT")) {
					start = System.nanoTime() / 1000;
					while (in.hasNextLine()) {
						str0 = in.nextLine();
						str1 = in.nextLine();
						tree.search(tree.getRoot(), str0);
						n0++;
						if (n0 > 0 && n0 % 200 == 0) {
							end = System.nanoTime() / 1000;
							t3[n0 / 200 - 1] = t3[n0 / 200 - 1] + end - start;
							start = System.nanoTime() / 1000;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < 25; j++)
			System.out.print("第" + j + "组:" + t3[j] / 1000 + "μs\n"); //1000次取平均并把每组打出来
		System.out.println();
		
		System.out.println("范围搜索测试"); //选取g-m搜索
		RBtree t = new RBtree();
		initial(t);//初始化
		long start=0,end=0;
		long time=0;
		start=System.nanoTime() / 1000;
		t.rangeSearch(t.getRoot(),"g","m");
		end=System.nanoTime() / 1000;
		time=end-start;
		System.out.println("范围搜索g-m测试时间："+time+"μs");
	}
}
