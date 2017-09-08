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
				System.out.println("�Է��� �߸��Ǿ����ϴ�. ���� : " + e.toString());
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
		
		int NumberOfRemain=pattern.length()-6;//�� ���� hashvalue�� �ʿ����� ���Ѵ�
		String SubStringOfPattern;
		LinkedList<ListNode> list=new LinkedList<>();
		
		int HashValue;
		
		SubStringOfPattern=new String(String.copyValueOf(pattern.toCharArray(), 0, 6));
		
		HashValue=0;
		
		for(int j=0; j<6; j++){
			HashValue+=SubStringOfPattern.charAt(j);
		}
		
		HashValue=HashValue%100;
		//�� ������ ���� treenode�� ����
		TreeNode TmpTreeNode=this.getAVLTree(HashValue).SearchNode(SubStringOfPattern);
		
		//�� ó���� ���
		//���� �� �ش� ������ ���� Ʈ������� ������ ���� ���Ѵ�
		int NumberOfElement=TmpTreeNode.getNumberOfElement();
		ListNode Tmp=TmpTreeNode.SearchByIndex(0);
		
		for(int j=0; j<NumberOfElement; j++){
			//���� ���ο� ��ũ�帮��Ʈ�� �� ��ǥ���� �ű��
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
					//���̰� �˻����Ϻ��� ª�� ���
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
	//��  AVLTree�� ���� �콬���̺��� ��Ŷ���̴�.
	//���� ���� � �콬�� ����ϴ��� �� ���� �����Ѵ�.
	private int HashValue;
	private int NumberOfElement;
	
	//�� ��Ʈ�� �θ�� �� SuperParent�̴�.
	private TreeNode SuperParent=null;
	
	//�� Ʈ������ ����� ��� �ϱ� ���� ����
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
		//ListNode�� �޾� �װ��� �̿��� Node�� Ʈ���� �����Ѵ�.
		if(NewNode.getHashValue()!=HashValue){
			System.out.println("HashValue is not equal!");
			return 1;
		}
		
		String TmpPattern=NewNode.getPattern();
		
		if(this.SearchNode(TmpPattern)!=NodeForPrint){
			//�̹� �����ϴ� ��� �̰����� ó��
			//��ũ�帮��Ʈ�� �߰��ȴ�.
			TreeNode TmpNode=this.SearchNode(TmpPattern);
			TmpNode.pushList(NewNode);
			return 0;
		}
		
		//�������� �ʴ� ���
		TreeNode Parent=SuperParent;
		TreeNode iter=SuperParent.getLeft();
		
		while(iter!=null){
			
			Parent=iter;
			
			if(TmpPattern.compareTo(iter.getPattern())<0){
				//TmpPattern�� �� ���̹Ƿ� �������� �����Ѵ�
				iter=iter.getLeft();
				
			}
			else if(TmpPattern.compareTo(iter.getPattern())>0){
				//TmpPattern�� ���ĺ������� �� �ڸ� ���������� �����Ѵ�.	
				iter=iter.getRight();
			}
			else if(TmpPattern.compareTo(iter.getPattern())==0){
				//���� ������ ��� ��ũ�帮��Ʈ�� �߰�
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
		//balancefactor�� 2�� ���� �ڽ��� curr
		
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
		//������ ���� ��带 ã�Ƽ� �� �ּҸ� �����Ѵ�.
		TreeNode iter=SuperParent.getLeft();
		
		while(iter!=null && Pattern.compareTo(iter.getPattern())!=0){
			if(Pattern.compareTo(iter.getPattern())<0){
				//���ĺ������� Pattern�� �� ���̸�  ���� �ڽ����� ����.
				iter=iter.getLeft();
			}
			else{
				//���ĺ������� Pattern�� �� �ڰų� ������ ������ �ڽ����� ����.
				iter=iter.getRight();
			}
		}
		
		if(iter==null)
			return NodeForPrint;//��ã�� ��
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
		//���� �� AVLTree�� �´� HashValue�� ������ ���ϵ��� ���� ����Ѵ�.
		
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
		//Root�� �Ѹ��� �ϴ� Ʈ���� �ִ� ���̸� ��ȯ
		if(Root==null) return 0;
		
		int lLevel=getLevel(Root.getLeft());
		int rLevel=getLevel(Root.getRight());
		
		if(lLevel>rLevel)
			return lLevel+1;
		else
			return rLevel+1;
	}
	public void setBalanceFactor(TreeNode Node){
		//Node�� balacneFactor�� �����Ѵ�.
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
		//Leaf���� �θ���� ���鼭 BF�� �ٲ۴�
		TreeNode iter=Leaf;
		while(iter!=SuperParent){
			setBalanceFactor(iter);
			iter=iter.getParent();
		}
	}
	public TreeNode FindUnbalancedNode(TreeNode NewNode){
		//���ο� ��带 �߽����� �ұ����� �߻��� ���� ��带 ã�� ����
		
		TreeNode iter=NewNode;
		
		while(iter.getBalnceFactor()!=-2 && iter.getBalnceFactor()!=2 && iter!=SuperParent){
			iter=iter.getParent();
		}
		
		return iter;
	}
	public void MakeBalanced(TreeNode NewNode){
		//balance�� �ȸ´� ��带 �޾� �װ��� �̿��� ȸ�� ������ ����	
		TreeNode iter=NewNode;
		
		//System.out.println("here1 "+iter.getPattern());
		
		while(iter.getBalnceFactor()!=-2 && iter.getBalnceFactor()!=2 && iter!=SuperParent){
			iter=iter.getParent();
		}
		
		//System.out.println("here2 "+iter.getPattern());
		
		if(iter==SuperParent){//������ ���� �´� ���
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
	//�� Ʈ���� ���� ����� ��ũ�� ����Ʈ�̴�.
	
	private int BalanceFactor=0;
	
	//�� ������ ���ڿ� ������ �����Ѵ�.
	private String TreePattern;
	
	//�Ʒ��� �� ������ head�� ����Ѵ�.
	private TreeNode parent=null;
	private TreeNode left=null;
	private TreeNode right=null;
	
	//�Ʒ��� �� ������ ���� ��ũ�帮��Ʈ�� ������ ���� �̿��Ѵ�.
	private ListNode head=null;
	private ListNode tail=null;
	
	//��ũ�帮��Ʈ�� ������ ����
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
		//�� �����ڴ� root�� parent�� ���鶧 �̿��Ѵ�.
		//TreePattern�� ���� null�� ���� �̿��� �����Ѵ�.
		
		//����Ʈ�� ������ ���� �ڵ�
		NumberOfElement=0;
		head=new ListNode();
		tail=new ListNode();
		
		head.setIndex(-1);
		
		head.setNext(tail);
		head.setPrev(head);
		
		tail.setNext(tail);
		tail.setPrev(head);
		
		//Ʈ���� �ʿ��� ������ �ʱ�ȭ
		parent=null;
		left=null;
		right=null;
		
		TreePattern=null;
	}
	public void initList(String Pattern){
		//�� ��ǥ�� ���� ����Ʈ�� �����ϰ� �ʱ�ȭ�Ѵ�.
		
		//����Ʈ�� ������ ���� �ڵ�
		NumberOfElement=0;
		head=new ListNode();
		tail=new ListNode();
		
		head.setIndex(-1);
		
		head.setNext(tail);
		head.setPrev(head);
		
		tail.setNext(tail);
		tail.setPrev(head);
		
		//Ʈ���� �ʿ��� ������ �ʱ�ȭ
		parent=null;
		left=null;
		right=null;
		
		TreePattern=new String(Pattern);
	}
	public int getNumberOfElement(){
		return NumberOfElement;
	}
	
	public void pushList(ListNode NewNode){
		//�ܺο��� ListNode�� �ʱ�ȭ�Ͽ� �� ��ũ�帮��Ʈ�� ������.
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
		//�� �Լ��� �״�� ����� �� ����.
		
		ListNode iter=head.getNext();
		
		if(NumberOfElement==0){
			System.out.println("(0, 0)");
			return;
		}
		
		while(iter!=tail.getPrev()){
			System.out.printf("(%d, %d) ", iter.getLine(), iter.getCol());
			iter=iter.getNext();
		}
		
		//������ ��ǥ �ڿ��� ������ ������� �ʴ´�.
		System.out.printf("(%d, %d)", iter.getLine(), iter.getCol());
		System.out.println();
		
		return;
	}
	
	public ListNode SearchByIndex(int index){
		//index�� �޾� �� index�� ���� ��带 �����Ѵ�
		
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
	
	//�� �ΰ��� key���̴� 
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
		//���� �ؽ��� ���� ��ġ �׸��� ������ �Է¹޾� ����Ʈ�� ���� ��带 �ʱ�ȭ�Ѵ�.
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