package project1;

//单词对象
public class Word {
	private String word; //单词
	private String meaning; //意思
	public Word(){
		
	}
	public Word(String a,String b){
		this.word=a;
		this.meaning=b;
	}
	public String getWord(){ //得到单词
		return word;
	}
	public String getMeaning(){ //得到意思
		return this.meaning;
	}
	public void setWord(String a,String b){ //设置单词
		this.word=a;
		this.meaning=b;
	}
}
