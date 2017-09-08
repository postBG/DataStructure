package HomeWork5;

import java.io.*;
import java.util.LinkedList;

public class Matching
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Hash Table=new Hash();
		String filename=null;
		LinkedList<String> Text=new LinkedList<>();
		
		while (true)
		{
			try
			{
				String input = br.readLine();
				
				if (input.compareTo("QUIT") == 0)
					break;
				else if(input.charAt(0)=='<'){
					input=input.trim();
					StringBuffer strbuf=new StringBuffer(input);
					strbuf=strbuf.deleteCharAt(0);
					strbuf=strbuf.deleteCharAt(0);
					filename=strbuf.toString();
					Text=new LinkedList<>();
					
					FileReader fr=new FileReader(filename);
					BufferedReader BrForFile=new BufferedReader(fr);
					
					Table=new Hash();
					String tmpString;
					int line=0;
					
					while((tmpString=BrForFile.readLine())!=null){
						line++;
						Table.HashFunction(tmpString, line);
						Text.add(tmpString);
					}
					
					fr.close();
				}
				else if(input.charAt(0)=='@'){
					input=input.trim();
					StringBuffer strbuf=new StringBuffer(input);
					strbuf=strbuf.deleteCharAt(0);
					strbuf=strbuf.deleteCharAt(0);
					String strHashValue=strbuf.toString();
					
					int HashValue=Integer.parseInt(strHashValue);
					
					Table.getAVLTree(HashValue).PrintByHashValue(HashValue);
				}
				else if(input.charAt(0)=='?'){
					
					StringBuffer strbuf=new StringBuffer(input);
					strbuf=strbuf.deleteCharAt(0);
					strbuf=strbuf.deleteCharAt(0);
					String pattern=strbuf.toString();
					
					Table.retrievalPattern(pattern, Text);
					
				}		
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

}
class Hash{
	
	private int TableSize=0;
	private AVLTree AVLTrees[] = null;
	/////////////////////////////////////////////
	
	public Hash(){
		TableSize=100;
		AVLTrees=new AVLTree[100];
		
		for(int i=0; i<100; i++){
			AVLTrees[i]=new AVLTree(i);
		}
	}
	public void HashFunction(String OneLine, int line){
		int size=OneLine.length();
		int sum;
		for(int i=0; i<size-5; i++){
			sum=0;
			for(int j=0; j<6; j++){
				sum+=OneLine.charAt(i+j);
			}
			int HashValue=sum%100;
			
			String NewPattern=new String(String.copyValueOf(OneLine.toCharArray(), i, 6));
			//System.out.println(NewPattern+": "+HashValue);
			ListNode NewNode=new ListNode(NewPattern, line, i+1, HashValue);
			
			AVLTrees[HashValue].InsertNode(NewNode);
		}
	}
	public AVLTree getAVLTree(int HashValue){
		return AVLTrees[HashValue];
	}
	public int getTableSize(){
		return TableSize;
	}
	public void retrievalPattern(String pattern, LinkedList<String> Text){
		
		int NumberOfRemain=pattern.length()-6;//몇 개의 hashvalue가 필요한지 구한다
		String SubStringOfPattern;
		LinkedList<ListNode> list=new LinkedList<>();
		
		int HashValue;
		
		SubStringOfPattern=new String(String.copyValueOf(pattern.toCharArray(), 0, 6));
		
		HashValue=0;
		
		for(int j=0; j<6; j++){
			HashValue+=SubStringOfPattern.charAt(j);
		}
		
		HashValue=HashValue%100;
		//이 패턴을 가진 treenode를 저장
		TreeNode TmpTreeNode=this.getAVLTree(HashValue).SearchNode(SubStringOfPattern);
		
		//맨 처음의 경우
		//먼저 이 해당 패턴을 가진 트리노드의 원소의 수를 구한다
		int NumberOfElement=TmpTreeNode.getNumberOfElement();
		ListNode Tmp=TmpTreeNode.SearchByIndex(0);
		
		for(int j=0; j<NumberOfElement; j++){
			//전부 새로운 링크드리스트에 그 좌표들을 옮긴다
			list.add(Tmp);
			Tmp=Tmp.getNext();
		}
		
		int TextLineIndex;
		int TextColIndex;
		int flag=0;
		
		for(int i=list.size()-1; i>=0; i--){
			ListNode TmpListNode=list.get(i);
			flag=0;	
			TextLineIndex=TmpListNode.getLine()-1;
			TextColIndex=TmpListNode.getCol()+5;
			
			for(int j=0; j<NumberOfRemain; j++){
				
				if((Text.get(TextLineIndex).length()<=(TextColIndex+j))){
					//길이가 검색패턴보다 짧을 경우
					flag=1;
					break;
				}
				
				if(pattern.charAt(6+j)!=Text.get(TextLineIndex).charAt(TextColIndex+j)){
					flag=1;
				}
			}
			
			if(flag==1){
				list.remove(i);
			}
		}//for
		
		if(list.size()!=0){
			for(int i=0; i<list.size()-1; i++){
				System.out.print("("+list.get(i).getLine()+", "+list.get(i).getCol()+") ");
			}
			System.out.println("("+list.get(list.size()-1).getLine()+", "+list.get(list.size()-1).getCol()+")");
		}
		else if(list.size()==0){
			System.out.println("(0, 0)");
		}
	}
}
class AVLTree{
	//각  AVLTree는 전부 헤쉬테이블의 버킷들이다.
	//따라서 각각 어떤 헤쉬를 담당하는지 이 값에 저장한다.
	private int HashValue;
	private int NumberOfElement;
	
	//각 루트의 부모는 이 SuperParent이다.
	private TreeNode SuperParent=null;
	
	//이 트리노드는 출력의 제어를 하기 위해 존재
	private TreeNode NodeForPrint=null;
	//////////////////////////////////////////////////
	public AVLTree(int HashValue){
		initAVLTree(HashValue);
	}
	public void initAVLTree(int HashValue){
		this.HashValue=HashValue;
		NumberOfElement=0;
		SuperParent=new TreeNode();
		NodeForPrint=new TreeNode();
	}
	public int InsertNode(ListNode NewNode){
		//ListNode를 받아 그것을 이용해 Node를 트리에 삽입한다.
		if(NewNode.getHashValue()!=HashValue){
			System.out.println("HashValue is not equal!");
			return 1;
		}
		
		String TmpPattern=NewNode.getPattern();
		
		if(this.SearchNode(TmpPattern)!=NodeForPrint){
			//이미 존재하는 경우 이곳에서 처리
			//링크드리스트에 추가된다.
			TreeNode TmpNode=this.SearchNode(TmpPattern);
			TmpNode.pushList(NewNode);
			return 0;
		}
		
		//존재하지 않는 경우
		TreeNode Parent=SuperParent;
		TreeNode iter=SuperParent.getLeft();
		
		while(iter!=null){
			
			Parent=iter;
			
			if(TmpPattern.compareTo(iter.getPattern())<0){
				//TmpPattern이 더 앞이므로 왼쪽으로 가야한다
				iter=iter.getLeft();
				
			}
			else if(TmpPattern.compareTo(iter.getPattern())>0){
				//TmpPattern이 알파벳순으로 더 뒤면 오른쪽으로 가야한다.	
				iter=iter.getRight();
			}
			else if(TmpPattern.compareTo(iter.getPattern())==0){
				//같은 패턴인 경우 링크드리스트에 추가
				iter.pushList(NewNode);
				return 0;
			}
		}
		
		iter=new TreeNode(NewNode);
		if(Parent==this.SuperParent || TmpPattern.compareTo(Parent.getPattern())<0){
			Parent.setLeft(iter);
			iter.setParent(Parent);
			NumberOfElement++;
			//ChangeAllBalanceFactor(SuperParent.getLeft());
			changeBalancedFactor(iter);
		}
		else{
	 		Parent.setRight(iter);
			iter.setParent(Parent);
			NumberOfElement++;
			//ChangeAllBalanceFactor(SuperParent.getLeft());
			changeBalancedFactor(iter);
		}
		
		MakeBalanced(iter);
		return 0;
	}
	public void RotateLeft(TreeNode Curr){
		//balancefactor가 2인 것의 자식이 curr
		
		TreeNode RightChild=Curr.getRight();
		TreeNode Parent=Curr.getParent();
		TreeNode RightChildsLeftChild=RightChild.getLeft();
		
		int flag;
		
		if(Parent.getLeft()==Curr)
			flag=1;
		else
			flag=-1;
		
		if(flag==1)
			Parent.setLeft(RightChild);
		else
			Parent.setRight(RightChild);
		
		RightChild.setParent(Parent);
		
		Curr.setParent(RightChild);
		RightChild.setLeft(Curr);
		
		Curr.setRight(RightChildsLeftChild);
		if(RightChildsLeftChild!=null){
			RightChildsLeftChild.setParent(Curr);
		}
		
		ChangeAllBalanceFactor(SuperParent.getLeft());
		
	}
	public void RotateRight(TreeNode Curr){
		TreeNode Parent=Curr.getParent();
		TreeNode LeftChild=Curr.getLeft();
		TreeNode LeftChildsRightChild=LeftChild.getRight();
		
		int flag;
		
		if(Parent.getLeft()==Curr)
			flag=1;
		else
			flag=-1;
	
		if(flag==1)
			Parent.setLeft(LeftChild);
		else
			Parent.setRight(LeftChild);
		
		LeftChild.setParent(Parent);
		
		Curr.setParent(LeftChild);
		LeftChild.setRight(Curr);
		
		Curr.setLeft(LeftChildsRightChild);
		if(LeftChildsRightChild!=null){
			LeftChildsRightChild.setParent(Curr);
		}
		
		ChangeAllBalanceFactor(SuperParent.getLeft());
	}
	public int getNumberOfElement(){
		return NumberOfElement;
	}
	public int getHashvalue(){
		return HashValue;
	}
	public TreeNode getSuperParent(){
		return SuperParent;
	}
	public TreeNode SearchNode(String Pattern){
		//패턴을 가진 노드를 찾아서 그 주소를 리턴한다.
		TreeNode iter=SuperParent.getLeft();
		
		while(iter!=null && Pattern.compareTo(iter.getPattern())!=0){
			if(Pattern.compareTo(iter.getPattern())<0){
				//알파벳순으로 Pattern이 더 앞이면  왼쪽 자식으로 간다.
				iter=iter.getLeft();
			}
			else{
				//알파벳순으로 Pattern이 더 뒤거나 같으면 오른쪽 자식으로 간다.
				iter=iter.getRight();
			}
		}
		
		if(iter==null)
			return NodeForPrint;//못찾은 것
		else
			return iter;
	}
	public int PreOrederTraverse(TreeNode Start, int num){
		if(Start!=null){
			num++;
			if(num==this.NumberOfElement){
				System.out.println(Start.getPattern());
			}
			else{
				System.out.print(Start.getPattern()+" ");
			}
			num=PreOrederTraverse(Start.getLeft(), num);
			num=PreOrederTraverse(Start.getRight(), num);
			
			return num;
		}
		
		return num;
	}
	public void PrintByHashValue(int HashValue){
		//만일 이 AVLTree에 맞는 HashValue가 들어오면 패턴들을 전부 출력한다.
		
		if(HashValue==this.HashValue){
			if(NumberOfElement!=0){
				PreOrederTraverse(SuperParent.getLeft(), 0);
			}
			else{
				System.out.println("EMPTY");
			}
		}
		
	}
	public int getLevel(TreeNode Root){
		//Root를 뿌리로 하는 트리의 최대 높이를 반환
		if(Root==null) return 0;
		
		int lLevel=getLevel(Root.getLeft());
		int rLevel=getLevel(Root.getRight());
		
		if(lLevel>rLevel)
			return lLevel+1;
		else
			return rLevel+1;
	}
	public void setBalanceFactor(TreeNode Node){
		//Node의 balacneFactor를 갱신한다.
		int LeftSubTreeLevel=getLevel(Node.getLeft());
		int RightSubTreeLevel=getLevel(Node.getRight());
		
		int NewBlanceFactor=LeftSubTreeLevel-RightSubTreeLevel;
		
		Node.setBalanceFactor(NewBlanceFactor);
		
	}
	public void ChangeAllBalanceFactor(TreeNode Root){
		if(Root!=null){
			setBalanceFactor(Root);
			ChangeAllBalanceFactor(Root.getLeft());
			ChangeAllBalanceFactor(Root.getRight());
		}
	}
	public void changeBalancedFactor(TreeNode Leaf){
		//Leaf부터 부모까지 가면서 BF를 바꾼다
		TreeNode iter=Leaf;
		while(iter!=SuperParent){
			setBalanceFactor(iter);
			iter=iter.getParent();
		}
	}
	public TreeNode FindUnbalancedNode(TreeNode NewNode){
		//새로운 노드를 중심으로 불균형이 발생한 조상 노드를 찾아 리턴
		
		TreeNode iter=NewNode;
		
		while(iter.getBalnceFactor()!=-2 && iter.getBalnceFactor()!=2 && iter!=SuperParent){
			iter=iter.getParent();
		}
		
		return iter;
	}
	public void MakeBalanced(TreeNode NewNode){
		//balance가 안맞는 노드를 받아 그것을 이용해 회전 연산을 적용	
		TreeNode iter=NewNode;
		
		//System.out.println("here1 "+iter.getPattern());
		
		while(iter.getBalnceFactor()!=-2 && iter.getBalnceFactor()!=2 && iter!=SuperParent){
			iter=iter.getParent();
		}
		
		//System.out.println("here2 "+iter.getPattern());
		
		if(iter==SuperParent){//균형이 전부 맞는 경우
			ChangeAllBalanceFactor(SuperParent.getLeft());
			return;
		}
		
		if(iter.getBalnceFactor()==2){
			TreeNode LeftChild=iter.getLeft();
			if(LeftChild.getBalnceFactor()==-1){//"Left Right case"
				RotateLeft(LeftChild);//reduce to "Left Left case"
			}
			//Left Left Case
			RotateRight(iter);
		}
		else if(iter.getBalnceFactor()==-2){
			TreeNode RightChild=iter.getRight();
			if(RightChild.getBalnceFactor()==1){
				//Right Left Case
				RotateRight(RightChild);//reduce to Right Right Case
			}
			//Right Right Case
			RotateLeft(iter);
		}
		
		MakeBalanced(NewNode);
	}
}

class TreeNode{
	//이 트리의 노드는 사실은 링크드 리스트이다.
	
	private int BalanceFactor=0;
	
	//이 변수는 문자열 패턴을 저장한다.
	private String TreePattern;
	
	//아래의 세 변수는 head만 사용한다.
	private TreeNode parent=null;
	private TreeNode left=null;
	private TreeNode right=null;
	
	//아래의 두 변수는 내부 링크드리스트의 구현을 위해 이용한다.
	private ListNode head=null;
	private ListNode tail=null;
	
	//링크드리스트의 원소의 갯수
	private int NumberOfElement=0;
	//////////////////////////////////////////////
	public TreeNode(){
		initList();
	}
	public TreeNode(String Pattern){
		initList(Pattern);
	}
	public TreeNode(ListNode NewNode){
		initList(NewNode.getPattern());
		pushList(NewNode);
	}
	public void initList(){
		//이 생성자는 root의 parent를 만들때 이용한다.
		//TreePattern의 값이 null인 것을 이용해 구별한다.
		
		//리스트의 생성을 위한 코드
		NumberOfElement=0;
		head=new ListNode();
		tail=new ListNode();
		
		head.setIndex(-1);
		
		head.setNext(tail);
		head.setPrev(head);
		
		tail.setNext(tail);
		tail.setPrev(head);
		
		//트리에 필요한 변수를 초기화
		parent=null;
		left=null;
		right=null;
		
		TreePattern=null;
	}
	public void initList(String Pattern){
		//각 좌표를 담은 리스트를 생성하고 초기화한다.
		
		//리스트의 생성을 위한 코드
		NumberOfElement=0;
		head=new ListNode();
		tail=new ListNode();
		
		head.setIndex(-1);
		
		head.setNext(tail);
		head.setPrev(head);
		
		tail.setNext(tail);
		tail.setPrev(head);
		
		//트리에 필요한 변수를 초기화
		parent=null;
		left=null;
		right=null;
		
		TreePattern=new String(Pattern);
	}
	public int getNumberOfElement(){
		return NumberOfElement;
	}
	
	public void pushList(ListNode NewNode){
		//외부에서 ListNode를 초기화하여 이 링크드리스트로 보낸다.
		ListNode Front=tail.getPrev();
		ListNode Back=tail;
		
		NewNode.setNext(Back);
		NewNode.setPrev(Front);
		
		Front.setNext(NewNode);
		Back.setPrev(NewNode);
		
		NewNode.setIndex();
		
		NumberOfElement++;
	}
	
	public void setParent(TreeNode Parent){
		this.parent=Parent;
	}
	
	public void setLeft(TreeNode Left){
		this.left=Left;
	}
	
	public void setRight(TreeNode Right){
		this.right=Right;
	}
	
	public void SetPattern(String Pattern){
		this.TreePattern=new String(Pattern);
	}
	
	public void setBalanceFactor(int newBalanceFactor){
		this.BalanceFactor=newBalanceFactor;
	}
	
	public String getPattern(){
		return this.TreePattern;
	}
	
	public TreeNode getParent(){
		return this.parent;
	}
	
	public TreeNode getLeft(){
		return this.left;
	}
	
	public TreeNode getRight(){
		return this.right;
	}
	
	public int getBalnceFactor(){
		return BalanceFactor;
	}
	
	public void PrintNode(){
		//이 함수는 그대로 사용할 수 없다.
		
		ListNode iter=head.getNext();
		
		if(NumberOfElement==0){
			System.out.println("(0, 0)");
			return;
		}
		
		while(iter!=tail.getPrev()){
			System.out.printf("(%d, %d) ", iter.getLine(), iter.getCol());
			iter=iter.getNext();
		}
		
		//마지막 좌표 뒤에는 공백을 출력하지 않는다.
		System.out.printf("(%d, %d)", iter.getLine(), iter.getCol());
		System.out.println();
		
		return;
	}
	
	public ListNode SearchByIndex(int index){
		//index를 받아 그 index를 가진 노드를 리턴한다
		
		ListNode iter=head.getNext();
		
		while(iter!=tail && iter.getIndex()!=index){
			iter=iter.getNext();
		}
		
		if(iter==tail)
			return null;
		
		return iter;
	}
}

class ListNode{
	
	private String pattern;
	
	//이 두개가 key값이다 
	private int line;
	private int col;
	private int index;
	
	private ListNode next=null;
	private ListNode prev=null;
	
	private int HashValue;
	//////////////////////////////////
	public ListNode(){
		
	}
	public ListNode(String Pattern, int line, int col, int hash){
		//계산된 해쉬의 값과 위치 그리고 패턴을 입력받아 리스트를 만들 노드를 초기화한다.
		HashValue=hash;
		this.line=line;
		this.col=col;
		this.pattern=new String(Pattern);
		
	}
	
	public void setNext(ListNode NextNode){
		this.next=NextNode;
	}
	
	public void setPrev(ListNode PrevNode){
		this.prev=PrevNode;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getCol(){
		return col;
	}
	
	public void setIndex(){
		this.index=(this.getPrev().getIndex())+1;
	}
	
	public void setIndex(int a){
		this.index=a;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public ListNode getNext(){
		return next;
	}
	public ListNode getPrev(){
		return prev;
	}
	
	public int getHashValue(){
		return HashValue;
	}
	
	public String getPattern(){
		return pattern;
	}
	public boolean IsEqual(int Line, int Col){
		if(this.line==Line && this.col==Col)
			return true;
		else
			return false;
	}
}