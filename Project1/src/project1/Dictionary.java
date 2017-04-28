package project1;

import java.util.*;
import java.io.*;

import javax.swing.JFileChooser;

//词典的程序
public class Dictionary {
	public static void set0(){ //输出项
		System.out.println("请选择要使用的存储结构：");
		System.out.println("0:红黑树");
		System.out.println("1:B树");
	}
	public static void set1(){ //输出项
		System.out.println("请选择进行的操作：");
		System.out.println("0:查询单词");
		System.out.println("1:删除单词");
		System.out.println("2:插入单词");
		System.out.println("3:导出词典");
		System.out.println("4:退出");
	}
	public static void set2(){ //输出项
		System.out.println("请选择进行的操作：");
		System.out.println("0:查询单个单词");
		System.out.println("1:范围查询(输入一个后，按回车继续输入，按照小大顺序输入)");
	}
	public static void set3(){ //输出项
		System.out.println("请选择进行的操作：");
		System.out.println("0:删除单个单词");
		System.out.println("1:导入文件删除");
	}
	public static void set4(){ //输出项
		System.out.println("请选择进行的操作：");
		System.out.println("0:插入单个单词");
		System.out.println("1:导入文件插入");
	}
	
	public static void input0(RBtree tree){ //打印红黑树的方法
		File f0=new File("src/rbt_pre.txt");
		File f1=new File("src/rbt_in.txt");
		File f2=new File("src/rbt_post.txt");
		try {
			PrintWriter out0=new PrintWriter(f0);
			PrintWriter out1=new PrintWriter(f1);
			PrintWriter out2=new PrintWriter(f2);
			tree.preorder(out0,tree.getRoot(),0);
			tree.inorder(out1,tree.getRoot(),0);
			tree.postorder(out2,tree.getRoot(),0);
			out0.close();
			out1.close();
			out2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void input1(Btree tree){ //打印B树的方法
		File f0=new File("src/bt_pre.txt");
		File f1=new File("src/bt_post.txt");
		try {
			PrintWriter out0=new PrintWriter(f0);
			PrintWriter out1=new PrintWriter(f1);
			tree.preorder(out0,tree.getRoot(),0,0);
			tree.postorder(out1,tree.getRoot(),0,0);
			out0.close();
			out1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static int compare(String s0,String s1){ //比较字符串
		String s2=s0.toLowerCase();
		String s3=s1.toLowerCase(); //由于有大写的单词，转为小写再比较
		if(s2.charAt(0)==s3.charAt(0))
			return s0.compareTo(s1); //如果首字母相同，直接比较原字符串即可
		return s2.compareTo(s3); //不同就要比较变为小写后的字符串
	}
	
	public static void RBtree(Scanner input){ //红黑树
		RBtree tree=new RBtree();
		String choice1="",choice2="",word="",meaning="";
		while(!choice1.equals("4")){
			set1();
			choice1=input.nextLine();
			if(choice1.equals("0")){
				set2();
				choice2=input.nextLine();
				if(choice2.equals("0")){ //单个查找
					word=input.nextLine();
					if(tree.search(tree.getRoot(),word)==tree.getNil()){
						System.out.println("对不起，您查找的单词不存在。");
					}else{
						System.out.print(tree.search(tree.getRoot(), word).getWord().getWord()+" ");
						System.out.println(tree.search(tree.getRoot(), word).getWord().getMeaning());
					}
				}else if(choice2.equals("1")){ //范围查找
					String key0=input.nextLine();
					String key1=input.nextLine();
					if(compare(key0,key1)<=0) //只有左边小于等于右边才可以
						tree.rangeSearch(tree.getRoot(), key0, key1);
					else
						System.out.println("输入值不符合要求");
				}
			}else if(choice1.equals("1")){ //删除
				set3();
				choice2=input.nextLine();
				if(choice2.equals("0")){ //单个删除
					word=input.nextLine();
					if(tree.search(tree.getRoot(),word)!=tree.getNil()){
	                	tree.delete(word);
	                	System.out.println("删除成功！");
					}else{
						System.out.println(word+":您要删除的词并不在词典中！");
						input0(tree);
					}
				}else if(choice2.equals("1")){ //导入文件删除
					JFileChooser chooser=new JFileChooser(new File("src/project1/sample files"));
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.showOpenDialog(null);
					File f = chooser.getSelectedFile();
					String str0="";
					try {
			            Scanner in = new Scanner(f);
			            str0 = in.nextLine();
			            if(str0.equals("DELETE")){ //查看文件开头
				            while (in.hasNextLine()) {
				                str0 = in.nextLine();
				                if(tree.search(tree.getRoot(),str0)!=tree.getNil()){ //查看是否有这个单词
				                	tree.delete(str0);
				                }else{
									System.out.println(str0+":您要删除的词并不在词典中！");
								}
				            }
				            input0(tree);
			            }
			        } catch (FileNotFoundException e) {
			        	System.out.println("文件不存在。");
			            continue;
			        }
				}
			}else if(choice1.equals("2")){ //插入
				set4();
				choice2=input.nextLine();
				if(choice2.equals("0")){ //单个单词插入
					word=input.nextLine();
					meaning=input.nextLine();
					if(tree.search(tree.getRoot(),word)==tree.getNil())
						tree.insert(new RBtreeNode(new Word(word,meaning)));
					input0(tree);
				}else if(choice2.equals("1")){ //导入文件插入
					JFileChooser chooser=new JFileChooser(new File("src/project1/sample files"));
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.showOpenDialog(null);
					File f = chooser.getSelectedFile();
					try {
			            Scanner in = new Scanner(f);
			            String str0 = in.nextLine();
			            String str1="";
			            if(str0.equals("INSERT")){ //查看文件开头
				            while (in.hasNextLine()) {
				                str0 = in.nextLine();
				                str1=in.nextLine();
				                if(tree.search(tree.getRoot(),str0)==tree.getNil()) //要看是否已经插入了
				                	tree.insert(new RBtreeNode(new Word(str0,str1)));
				            }
				            input0(tree);
			            }
			        } catch (FileNotFoundException e) {
			        	System.out.println("文件不存在。");
			            continue;
			        }
				}
			}else if(choice1.equals("3")){ //导出
				input0(tree);
			}
		}
		System.exit(0);
	}
	
	public static void Btree(Scanner input){ //B树的方法，大体结构和红黑树一样
		Btree tree=new Btree();
		String choice1="",choice2="",word="",meaning="";
		while(!choice1.equals("4")){
			set1();
			choice1=input.nextLine();
			if(choice1.equals("0")){
				set2();
				choice2=input.nextLine();
				if(choice2.equals("0")){
					word=input.nextLine();
					if(tree.search(tree.getRoot(),word)==null){
						System.out.println("对不起，您查找的单词不存在。");
					}else{
						System.out.print(tree.search(tree.getRoot(), word).getWord()+" ");
						System.out.println(tree.search(tree.getRoot(), word).getMeaning());
					}
				}else if(choice2.equals("1")){
					String key0=input.nextLine();
					String key1=input.nextLine();
					if(compare(key0,key1)<=0)
						tree.rangeSearch(tree.getRoot(), key0, key1);
					else
						System.out.println("输入值不符合要求");
				}
			}else if(choice1.equals("1")){
				set3();
				choice2=input.nextLine();
				if(choice2.equals("0")){
					word=input.nextLine();
					if(tree.search(tree.getRoot(),word)==null)
	                	System.out.println(word+":您要删除的词并不在词典中！");
	                else{
	                	tree.delete(tree.getRoot(),new Word(word,""));
	                	System.out.println("删除成功！");
						input1(tree);
					}
				}else if(choice2.equals("1")){
					JFileChooser chooser=new JFileChooser(new File("src/project1/sample files"));
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.showOpenDialog(null);
					File f = chooser.getSelectedFile();
					try {
			            Scanner in = new Scanner(f);
			            String str0 = in.nextLine();
			            if(str0.equals("DELETE")){
				            while (in.hasNextLine()) {
				                str0 = in.nextLine();
				                if(tree.search(tree.getRoot(),str0)==null)
				                	System.out.println(str0+":您要删除的词并不在词典中！");
				                else
				                	tree.delete(tree.getRoot(),new Word(str0,""));
				            }
				            input1(tree);
			            }
			        } catch (FileNotFoundException e) {
			        	System.out.println("文件不存在。");
			            continue;
			        }
				}
			}else if(choice1.equals("2")){
				set4();
				choice2=input.nextLine();
				if(choice2.equals("0")){
					word=input.nextLine();
					meaning=input.nextLine();
					if(tree.search(tree.getRoot(),word)==null)
						tree.insert(new Word(word,meaning));
					input1(tree);
				}else if(choice2.equals("1")){
					JFileChooser chooser=new JFileChooser(new File("src/project1/sample files"));
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.showOpenDialog(null);
					File f = chooser.getSelectedFile();
					try {
			            Scanner in = new Scanner(f);
			            String str0 = in.nextLine();
			            String str1="";
			            if(str0.equals("INSERT")){
				            while (in.hasNextLine()) {
				                str0 = in.nextLine();
				                str1=in.nextLine();
				                if(tree.search(tree.getRoot(),str0)==null)
				                	tree.insert(new Word(str0,str1));
				            }
				            input1(tree);
			            }
			        } catch (FileNotFoundException e) {
			        	System.out.println("文件不存在。");
			            continue;
			        }
				}
			}else if(choice1.equals("3")){
				input1(tree);
			}
		}
		System.exit(0);
	}
	
	public static void main(String[]args){
		Scanner input=new Scanner(System.in);
		set0();
		String choice0=input.nextLine();
		if(choice0.equals("0")){ //依据输入的值构建相应的树
			RBtree(input);
		}else if(choice0.equals("1")){
			Btree(input);
		}else {
			System.out.print("您的输入有误！");
			System.exit(0);
		}
		input.close();
	}
}
