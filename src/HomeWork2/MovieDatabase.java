package HomeWork2;

import java.io.*;

public class MovieDatabase {
	
	public static void main(String[] args) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		GenreLinkedList list=new GenreLinkedList();
		
		while (true)
		{
			try
			{
				String input = br.readLine();
				
				input=input.trim();
				input=input.toUpperCase();
				
				if (input.compareTo("QUIT") == 0)
					break;
				else if(input.compareTo("PRINT")==0)
					list.PrintList();
				else if(input.charAt(0)=='I'){
					//Insert�� ���			
					int[] index=new int[4];
					int cnt=0;
					for(int i=0; i<input.length(); i++){
						if(input.charAt(i)=='%'){
							index[cnt]=i;
							cnt++;
						}
					}
					
					String Genre=new String(input.substring(index[0]+1, index[1]));
					String Movie=new String(input.substring(index[2]+1, index[3]));
					
					list.pushOrdered(Genre, Movie);
				}
				else if(input.charAt(0)=='D'){
					//Delete�� ���
					int[] index=new int[4];
					int cnt=0;
					for(int i=0; i<input.length(); i++){
						if(input.charAt(i)=='%'){
							index[cnt]=i;
							cnt++;
						}
					}
					
					String Genre=new String(input.substring(index[0]+1, index[1]));
					String Movie=new String(input.substring(index[2]+1, index[3]));
					
					list.erase(Genre, Movie);
				}
				else if(input.charAt(0)=='S'){
					//Search�� ���
					int[] index=new int[2];
					int cnt=0;
					for(int i=0; i<input.length(); i++){
						if(input.charAt(i)=='%'){
							index[cnt]=i;
							cnt++;
						}
					}
					
					String TargetName=new String(input.substring(index[0]+1, index[1]));
					
					list.SearchAndPrint(TargetName);
				}
				else{
					//error
				}
			}
			catch (Exception e)
			{
				System.out.println("�Է��� �߸��Ǿ����ϴ�. ���� : " + e.toString());
			}
		}

	}
	
	
}

class NodeForGenre{//�� ���� ��ȭ�� �帣�� ����ִ�. 
	
	public String Genre;
	public NodeForGenre next=null;
	public NodeForGenre prev=null;
	public MovieLinkedList movielist;
	
}

class NodeForMovie{//�� ���� ��ȭ�� ������ ����ִ�.
	
	public String MovieName;
	public NodeForMovie next=null;
	public NodeForMovie prev=null;
	
}

class GenreLinkedList{
	
	private NodeForGenre head;
	private NodeForGenre tail;
	private int NumberOfElement;
	
	////////////////////////////////////////////
	
	public GenreLinkedList(){
		//������
		initList();
	}
	public NodeForGenre getHead(){
		return head;
	}
	public NodeForGenre getTail(){
		return tail;
	}
	public int getNumberOFElement(){
		return NumberOfElement;
	}
	public void initList(){
		//GenreList�� �����ϰ� �ʱ�ȭ�Ѵ�
		NumberOfElement=0;
		head=new NodeForGenre();
		tail=new NodeForGenre();
		
		//head.MovieName="\0";
		head.next=tail;
		head.prev=head;
				
		//tail.MovieName="\0";
		tail.next=tail;
		tail.prev=head;
	}
	public Boolean isExistGenre(String TargetGenre){
		//TargetGenre�� ������ true, ������ false�� ��ȯ
		NodeForGenre iter=head.next;
		
		while(iter!=tail && iter.Genre.compareTo(TargetGenre)!=0)
			iter=iter.next;
		
		if(iter==tail)//��ã������
			return false;
		
		//ã������
		return true;
	}
	public NodeForGenre FindNode(String TargetGenre){
		//TargetGenre�� ������ �� �ּҸ�, ������ null�� ��ȯ
		NodeForGenre iter=head.next;
				
		while(iter!=tail && iter.Genre.compareTo(TargetGenre)!=0)
			iter=iter.next;
				
		if(iter==tail)//��ã������
			return null;
				
		//ã������
		return iter;
	}
	public int pushOrdered(String GenreName, String Movie){
			
		if(isExistGenre(GenreName)==false){//���� �� �帣�� ���ٸ�
			//�� �帣�� ���� ��带 �����, �ű⿡ �� ��ȭ�� �̸��� �ִ´�
			NodeForGenre iter=head.next;
			
			//compareTo�Լ��� MovieName�� ���ĺ������� ���̸� ������ ��ȯ, ������ 0, �� �ڸ� ����� ��ȯ
			while(iter!=tail && iter.Genre.compareTo(GenreName)<=0){
				//���ο� ��尡 �� ��Ҹ� ã��
				iter=iter.next;
			}
			//iter���� ���� GenreName�� ���ĺ����� �� ���� ��尡 ����ִ�.
			//�� ���ο� ���� iter�� �տ� ���ԵǾ�� �Ѵ�
			NodeForGenre PointerOfInsertNode=new NodeForGenre();;
			NodeForGenre front=iter.prev;
			NodeForGenre back=iter;
			
			PointerOfInsertNode.Genre=GenreName;
			PointerOfInsertNode.next=back;
			PointerOfInsertNode.prev=front;
			
			front.next=PointerOfInsertNode;
			back.prev=PointerOfInsertNode;
			
			NumberOfElement++;
			
			//���ο� �帣�� ����� �����Ƿ� �̶��� argument�� ���� ��ȭ�� �����ϴ����� Ȯ���� �ʿ䰡 ����.
			PointerOfInsertNode.movielist=new MovieLinkedList();
			PointerOfInsertNode.movielist.pushOrdered(Movie);
			
			return 0;
		}
		else{//���� �� �帣�� �����ϴ� ���
			NodeForGenre PointerOfGenre=FindNode(GenreName);
			
			//�̶��� �̹� movielist�� �����Ѵ�
			if(PointerOfGenre.movielist.isExistMovie(Movie)==true){
				//���� �帣�� ��ȭ �̸��� ���� ���� �̹� ����Ǿ� �ִ� ���
				return 1;
			}
			else{
				//�帣�� ������ ��ȭ�� ���� ���
				//��ȭ�� �Է��Ѵ�
				PointerOfGenre.movielist.pushOrdered(Movie);
				return 0;
			}
		}
		
	}//pushOrdered
	
	public void PrintList(){
		
		NodeForGenre iter=head.next;
		
		if(NumberOfElement==0){
			System.out.println("EMPTY");
			return;
		}
		
		while(iter!=tail){
			//�帣���� �Ѱ��ָ�  Movie�� ��µ�
			iter.movielist.PrintList(iter.Genre);
			iter=iter.next;
		}
		
		return;
	}
	
	public int erase(String GenreName, String Movie){
		
		if(isExistGenre(GenreName)==true){
			//�����ϴ� �帣�� �ϴ� �ش� �帣�� ����
			NodeForGenre PointerOfGenre=FindNode(GenreName);
			
			PointerOfGenre.movielist.erase(Movie);
			
			if(PointerOfGenre.movielist.getNumberOfElement()==0){
				//movielist�� ������� �ش� �帣�� ����
				//��, PointerOfGenre����
				NodeForGenre front=PointerOfGenre.prev;
				NodeForGenre back=PointerOfGenre.next;
			
				front.next=back;
				back.prev=front;
			
				NumberOfElement--;
			
				return 0;
			}
		
			//���� ���������� �׳� ����
			return 0;
		}
		else{
			
			return 1;
		}
	}
	
	public void SearchAndPrint(String TargetName){
		//���ڿ��� �޾� �� ���ڿ��� ������ �ִ� ��� ��ȭ�� �帣�� �Բ� ���
		NodeForGenre iter=head.next;
		int NumberOfPrint=0;
		
		while(iter!=tail){
			NumberOfPrint=iter.movielist.SearchAndPrint(TargetName, iter.Genre, NumberOfPrint);
			iter=iter.next;
		}
		
		if(NumberOfPrint==0){
			System.out.println("EMPTY");
		}
	}

}

class MovieLinkedList{

	private NodeForMovie head;
	private NodeForMovie tail;
	private int NumberOfElement;
	
	//////////////////////////////////////////////////
	
	public MovieLinkedList(){//������
		initList();
	}
	public NodeForMovie getHead(){
		return head;
	}
	
	public NodeForMovie getTail(){
		return tail;
	}
	public int getNumberOfElement(){
		return NumberOfElement;
	}
	public void initList(){
		//MovieList�� �����ϰ� �ʱ�ȭ�Ѵ�.
		NumberOfElement=0;
		head=new NodeForMovie();
		tail=new NodeForMovie();
		
		//head.MovieName="\0";
		head.next=tail;
		head.prev=head;
		
		//tail.MovieName="\0";
		tail.next=tail;
		tail.prev=head;
	}
	public void insertAfter(String Movie, NodeForMovie FrontNode){
		//FrontNode�ڿ� Movie�� String�� ���� ��带 �����Ѵ�
		NodeForMovie NewNode=new NodeForMovie();
		NodeForMovie front=FrontNode;
		NodeForMovie back=FrontNode.next;
		
		if(FrontNode==tail){//tail�ڿ� ������ �� ����
			System.out.println("Error");
		}
		
		NewNode.MovieName=Movie;
		NewNode.next=back;
		NewNode.prev=front;
		
		front.next=NewNode;
		back.prev=NewNode;
		
		NumberOfElement++;
	}
	
	public NodeForMovie pushOrdered(String Movie){
		//Movie�� ���� ��带 ���ĵ� ������ �����Ѵ� 
		
		NodeForMovie iter=head.next;
		
		//compareTo�Լ��� MovieName�� ���ĺ������� ���̸� ������ ��ȯ, ������ 0, �� �ڸ� ����� ��ȯ
		while(iter!=tail && iter.MovieName.compareTo(Movie)<=0){
			//���ο� ��尡 �� ��Ҹ� ã��
			iter=iter.next;
		}
		//iter���� ���� Movie�� ���ĺ����� �� ���� ��尡 ����ִ�.
		//�� ���ο� ���� iter�� �տ� ���ԵǾ�� �Ѵ�
		NodeForMovie PointerOfInsertNode=new NodeForMovie();;
		NodeForMovie front=iter.prev;
		NodeForMovie back=iter;
		
		PointerOfInsertNode.MovieName=Movie;
		PointerOfInsertNode.next=back;
		PointerOfInsertNode.prev=front;
		
		front.next=PointerOfInsertNode;
		back.prev=PointerOfInsertNode;
		
		NumberOfElement++;
		
		return PointerOfInsertNode;
	}
	public int erase(String TargetName){
		//TargetName�� ���� ��带 ã�� ����
		NodeForMovie iter=head.next;
		while(iter!=tail && (iter.MovieName.compareTo(TargetName)!=0)){
			iter=iter.next;
		}
		
		if(iter==tail)//��ã������
			return 1;
		
		//ã������
		NodeForMovie front=iter.prev;
		NodeForMovie back=iter.next;
		
		front.next=back;
		back.prev=front;
		
		NumberOfElement--;
		
		return 0;
	}
	public Boolean isExistMovie(String TargetName){
		//TargetName�� ���� ��带 ã�� ������ 1�� ��ȯ
		//��ã���� 0�� ��ȯ
		NodeForMovie iter=head.next;
		while(iter!=tail && (iter.MovieName.compareTo(TargetName)!=0)){
			iter=iter.next;
		}
		
		if(iter==tail)//��ã������
			return false;
		
		//ã������
		return true;
	}
	
	public NodeForMovie FindNode(String TargetName){
		//TargetName�� ���� ��带 ã�� ������ �� �ּҸ� ��ȯ
		//��ã���� null�� ��ȯ
		NodeForMovie iter=head.next;
		while(iter!=tail && iter.MovieName.compareTo(TargetName)!=0){
			iter=iter.next;
		}
		
		if(iter==tail)//��ã������
			return null;
		
		//ã������
		return iter;
	}
	
	public void PrintList(String GenreName){
		//����Ʈ�� ������ ���� ���
		//��¹���� GenreName�� �Է¹޾� �˸��� ����������� ����ϰ� �ȴ�
		NodeForMovie iter=head.next;
		
		while(iter!=tail){
			System.out.println('('+GenreName+", "+iter.MovieName+')');
			iter=iter.next;
		}
		
		return;
		
	}
	
	public int SearchAndPrint(String TargetName, String Genre, int NumberOfPrint){
		//���ڿ��� ���� ���� ����ϴ� �Լ�
		//����� Ƚ���� ��ȯ�Ѵ�
		NodeForMovie iter=head.next;
		while(iter!=tail){
			
			if(iter.MovieName.contains(TargetName)==true){
				System.out.println('('+Genre+", "+iter.MovieName+')');
				NumberOfPrint++;
			}

			iter=iter.next;
		}
		
		return NumberOfPrint;
	}
}