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
					//Insert인 경우			
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
					//Delete인 경우
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
					//Search인 경우
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
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}

	}
	
	
}

class NodeForGenre{//이 노드는 영화의 장르를 담고있다. 
	
	public String Genre;
	public NodeForGenre next=null;
	public NodeForGenre prev=null;
	public MovieLinkedList movielist;
	
}

class NodeForMovie{//이 노드는 영화의 제목을 담고있다.
	
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
		//생성자
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
		//GenreList를 생성하고 초기화한다
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
		//TargetGenre가 있으면 true, 없으면 false을 반환
		NodeForGenre iter=head.next;
		
		while(iter!=tail && iter.Genre.compareTo(TargetGenre)!=0)
			iter=iter.next;
		
		if(iter==tail)//못찾았으면
			return false;
		
		//찾았으면
		return true;
	}
	public NodeForGenre FindNode(String TargetGenre){
		//TargetGenre가 있으면 그 주소를, 없으면 null을 반환
		NodeForGenre iter=head.next;
				
		while(iter!=tail && iter.Genre.compareTo(TargetGenre)!=0)
			iter=iter.next;
				
		if(iter==tail)//못찾았으면
			return null;
				
		//찾았으면
		return iter;
	}
	public int pushOrdered(String GenreName, String Movie){
			
		if(isExistGenre(GenreName)==false){//만일 이 장르가 없다면
			//이 장르를 가진 노드를 만들고, 거기에 이 영화의 이름을 넣는다
			NodeForGenre iter=head.next;
			
			//compareTo함수는 MovieName이 알파벳순으로 앞이면 음수를 반환, 같으면 0, 더 뒤면 양수를 반환
			while(iter!=tail && iter.Genre.compareTo(GenreName)<=0){
				//새로운 노드가 들어갈 장소를 찾음
				iter=iter.next;
			}
			//iter에는 현재 GenreName와 알파벳순이 더 뒤인 노드가 들어있다.
			//즉 새로운 노드는 iter의 앞에 삽입되어야 한다
			NodeForGenre PointerOfInsertNode=new NodeForGenre();;
			NodeForGenre front=iter.prev;
			NodeForGenre back=iter;
			
			PointerOfInsertNode.Genre=GenreName;
			PointerOfInsertNode.next=back;
			PointerOfInsertNode.prev=front;
			
			front.next=PointerOfInsertNode;
			back.prev=PointerOfInsertNode;
			
			NumberOfElement++;
			
			//새로운 장르가 만들어 졌으므로 이때는 argument로 들어온 영화가 존재하는지는 확인할 필요가 없다.
			PointerOfInsertNode.movielist=new MovieLinkedList();
			PointerOfInsertNode.movielist.pushOrdered(Movie);
			
			return 0;
		}
		else{//만일 이 장르가 존재하는 경우
			NodeForGenre PointerOfGenre=FindNode(GenreName);
			
			//이때는 이미 movielist가 존재한다
			if(PointerOfGenre.movielist.isExistMovie(Movie)==true){
				//만일 장르와 영화 이름도 같은 것이 이미 저장되어 있는 경우
				return 1;
			}
			else{
				//장르는 있지만 영화는 없는 경우
				//영화를 입력한다
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
			//장르값을 넘겨주면  Movie도 출력됨
			iter.movielist.PrintList(iter.Genre);
			iter=iter.next;
		}
		
		return;
	}
	
	public int erase(String GenreName, String Movie){
		
		if(isExistGenre(GenreName)==true){
			//존재하는 장르면 일단 해당 장르로 간다
			NodeForGenre PointerOfGenre=FindNode(GenreName);
			
			PointerOfGenre.movielist.erase(Movie);
			
			if(PointerOfGenre.movielist.getNumberOfElement()==0){
				//movielist가 비었으면 해당 장르도 삭제
				//즉, PointerOfGenre삭제
				NodeForGenre front=PointerOfGenre.prev;
				NodeForGenre back=PointerOfGenre.next;
			
				front.next=back;
				back.prev=front;
			
				NumberOfElement--;
			
				return 0;
			}
		
			//아직 남아있으면 그냥 종료
			return 0;
		}
		else{
			
			return 1;
		}
	}
	
	public void SearchAndPrint(String TargetName){
		//문자열을 받아 그 문자열을 가지고 있는 모든 영화를 장르와 함께 출력
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
	
	public MovieLinkedList(){//생성자
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
		//MovieList를 생성하고 초기화한다.
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
		//FrontNode뒤에 Movie의 String을 가진 노드를 삽입한다
		NodeForMovie NewNode=new NodeForMovie();
		NodeForMovie front=FrontNode;
		NodeForMovie back=FrontNode.next;
		
		if(FrontNode==tail){//tail뒤에 삽입할 수 없다
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
		//Movie를 가진 노드를 정렬된 순서로 삽입한다 
		
		NodeForMovie iter=head.next;
		
		//compareTo함수는 MovieName이 알파벳순으로 앞이면 음수를 반환, 같으면 0, 더 뒤면 양수를 반환
		while(iter!=tail && iter.MovieName.compareTo(Movie)<=0){
			//새로운 노드가 들어갈 장소를 찾음
			iter=iter.next;
		}
		//iter에는 현재 Movie와 알파벳순이 더 뒤인 노드가 들어있다.
		//즉 새로운 노드는 iter의 앞에 삽입되어야 한다
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
		//TargetName을 가진 노드를 찾아 삭제
		NodeForMovie iter=head.next;
		while(iter!=tail && (iter.MovieName.compareTo(TargetName)!=0)){
			iter=iter.next;
		}
		
		if(iter==tail)//못찾았으면
			return 1;
		
		//찾았으면
		NodeForMovie front=iter.prev;
		NodeForMovie back=iter.next;
		
		front.next=back;
		back.prev=front;
		
		NumberOfElement--;
		
		return 0;
	}
	public Boolean isExistMovie(String TargetName){
		//TargetName을 가진 노드를 찾아 있으면 1을 반환
		//못찾으면 0을 반환
		NodeForMovie iter=head.next;
		while(iter!=tail && (iter.MovieName.compareTo(TargetName)!=0)){
			iter=iter.next;
		}
		
		if(iter==tail)//못찾았으면
			return false;
		
		//찾았으면
		return true;
	}
	
	public NodeForMovie FindNode(String TargetName){
		//TargetName을 가진 노드를 찾아 있으면 그 주소를 반환
		//못찾으면 null을 반환
		NodeForMovie iter=head.next;
		while(iter!=tail && iter.MovieName.compareTo(TargetName)!=0){
			iter=iter.next;
		}
		
		if(iter==tail)//못찾았으면
			return null;
		
		//찾았으면
		return iter;
	}
	
	public void PrintList(String GenreName){
		//리스트의 내용을 전부 출력
		//출력방식은 GenreName을 입력받아 알맞은 출력형식으로 출력하게 된다
		NodeForMovie iter=head.next;
		
		while(iter!=tail){
			System.out.println('('+GenreName+", "+iter.MovieName+')');
			iter=iter.next;
		}
		
		return;
		
	}
	
	public int SearchAndPrint(String TargetName, String Genre, int NumberOfPrint){
		//문자열이 들어가면 전부 출력하는 함수
		//출력한 횟수를 반환한다
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