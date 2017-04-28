package project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//B树的性能测试，t取10
public class Test1 {
	public static void initial(Btree tree){ //初始化树
		File f=new File("src/project1/sample files/1_initial.txt");
		try {
			Scanner in = new Scanner(f);
			String str0 = in.nextLine();
			String str1 = "";
			if (str0.equals("INSERT")) {
				while (in.hasNextLine()) {
					str0 = in.nextLine();
					str1 = in.nextLine();
					if (tree.search(tree.getRoot(), str0) == null)
						tree.insert(new Word(str0, str1));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		long[] t0 = new long[25];
		System.out.println("initial插入测试");//插入5000个词
		try {
			File f=new File("src/project1/sample files/1_initial.txt");
			for (int i = 0; i < 1000; i++) { //循环1000次取平均，避免不稳定性
				Scanner in = new Scanner(f);
				Btree tree = new Btree();
				String str0 = in.nextLine();
				String str1 = "";
				int n0 = 0;
				long start = 0, end = 0;
				if (str0.equals("INSERT")) {
					start = System.nanoTime() / 1000; //取纳秒作为单位
					while (in.hasNextLine()) {
						str0 = in.nextLine();
						str1 = in.nextLine();
						if (tree.search(tree.getRoot(), str0) == null)//判断是否存在，存在就不插入，这个时间应当计算进去
							tree.insert(new Word(str0, str1));
						n0++;
						if (n0 > 0 && n0 % 200 == 0) { //每200次算一下时间
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
			System.out.print("第" + j + "组:" + t0[j] / 1000 + "μs\n");//输出1000次的平均值
		System.out.println();

		long[] t1 = new long[5];//删除测试，只有5组
		System.out.println("delete删除测试");
		try {
			File f=new File("src/project1/sample files/2_delete.txt");
			for (int i = 0; i < 1000; i++) {
				Scanner in = new Scanner(f);
				Btree tree = new Btree();
				initial(tree);
				String str0 = in.nextLine();
				int n0 = 0;
				long start = 0, end = 0;
				if (str0.equals("DELETE")) {
					start = System.nanoTime() / 1000;
					while (in.hasNextLine()) {
						str0 = in.nextLine();
						if (tree.search(tree.getRoot(), str0) != null)//判断是否单词存在，存在才删除，这个时间应当计算进去
							tree.delete(tree.getRoot(),new Word(str0,""));
						n0++;
						if (n0 > 0 && n0 % 200 == 0) {
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
			System.out.print("第" + j + "组:" + t1[j] / 1000 + "μs\n");
		System.out.println();

		long[] t2 = new long[5];
		System.out.println("isert插入测试");//这个测试使用文件insert.txt
		try {
			File f=new File("src/project1/sample files/3_insert.txt");
			for (int i = 0; i < 1000; i++) { //依旧循环1000遍
				Scanner in = new Scanner(f);
				Btree tree = new Btree();
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
						if (tree.search(tree.getRoot(), str0) == null) //查找是否存在，不存在就插入
							tree.insert(new Word(str0, str1));
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
			System.out.print("第" + j + "组:" + t2[j] / 1000 + "μs\n"); //取平均值
		System.out.println();
		
		long[] t3 = new long[25];
		System.out.println("搜索测试");//搜索就是把所有的单词按顺序搜索一遍
		try {
			File f=new File("src/project1/sample files/1_initial.txt");
			for (int i = 0; i < 1000; i++) { //依旧循环1000次
				Scanner in = new Scanner(f);
				Btree tree = new Btree();
				initial(tree);
				String str0 = in.nextLine();
				String str1 = "";
				int n0 = 0;
				long start = 0, end = 0;
				if (str0.equals("INSERT")) { //使用了INSERT的文件
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
			System.out.print("第" + j + "组:" + t3[j] / 1000 + "μs\n");
		System.out.println();
		
		System.out.println("范围搜索测试"); //选取g-m搜索，我采用手动运行10次程序来取平均
		Btree tree = new Btree();
		initial(tree);
		long start=0,end=0;
		long time=0;
		start=System.nanoTime() / 1000;
		tree.rangeSearch(tree.getRoot(),"g","m");
		end=System.nanoTime() / 1000;
		time=end-start;
		System.out.println("范围搜索g-m测试时间："+time+"μs");
	}
}
