package HomeWork3;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CalculatorTest {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{	
				String infix = br.readLine();
				StringBuffer postfix=new StringBuffer();
				Stack stack=new Stack(infix.length());
				StackForCalculate stacknum=new StackForCalculate(infix.length());
				
				if (infix.compareTo("q") == 0)
					break;
				
				infix=infix.trim();
				
				checkSpaceBetweenNumber(infix);
				
				//������ �����Ѵ�.
				infix=infix.replace(" ", "");
				infix=infix.replace("	", "");
				
				isWrongExpression(infix);
				
				postfix=makeInfixToPostfix(infix, postfix, stack);
				
				long answer=calculateWithPostfix(postfix, stacknum);
				
				printAnswer(answer, postfix);
					
				
				
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
				//System.out.println("�Է��� �߸��Ǿ����ϴ�. ���� : " + e.toString());
			}
		}	
	}
	
	private static void checkSpaceBetweenNumber(String infix) throws Exception{
		int length=infix.length();
		
		for(int i=0; i<length-1; i++){
			
			if((is_number(infix.charAt(i))==1) && 
					((infix.charAt(i+1)==' ') || (infix.charAt(i+1)=='	'))){
				//���� �ڿ� �ٷ� �����̳� ���� ���� ���
				for(int j=i+1; j<length; j++){
					
					if(is_number(infix.charAt(j))==1){
						Exception e=new Exception();
						throw e;
					}
					else if(infix.charAt(j)!=' ' && infix.charAt(j)!='	')
						break;
		
				}
			}
			
		}
		
	}
	
	private static StringBuffer makeInfixToPostfix(String infix, StringBuffer postfix, Stack stack) throws Exception{
		
		int cnt=0;
		
		int LengthOfExpression=infix.length();
		
		for(cnt=0; cnt<LengthOfExpression-1; cnt++){
			
			if(infix.charAt(cnt)=='('){//'('�� stack�� �ִ´�
				//0��°�϶��� �۵��Ѵ�
				stack.push(infix.charAt(cnt));
			}
			else if(infix.charAt(cnt)==')'){//')'�� ���� ��� '('�� ���ö����� ���� ���ϰ� '('�� ������
				//������ �� ó������ ���� �ʴ� ����
				//����ó��
				while(stack.top()!='('){
					//'('�� ����������
					postfix.append(stack.pop());
					postfix.append(' ');
				}
				
				//'('�� ������
				stack.pop();
			}
			else if((infix.charAt(cnt)=='+' || infix.charAt(cnt)=='-')){

				//���� ���� �����ں��� ó���Ѵ�
				if(cnt==0){//���� �տ� �� ���
					if(infix.charAt(cnt)=='-'){
						stack.push('~');
					}
					else{
						//���� �������̰� '+'�� ���
						//�ƹ��� �۾��� ���� ����
						Exception e=new Exception();
						throw e;
					}
				}
				else if( is_operator(infix.charAt(cnt-1))==1 || infix.charAt(cnt-1)=='(' ){
					//�̷� ��� ���� �������̴�.
					//*-, +-, ^-, (-, %-, /- ��� ���� �����̹Ƿ�
					if(infix.charAt(cnt)=='-'){
						stack.push('~');
					}
					else{
						//'+'���� �������� ���
						//�ƹ��͵� ���� ����
						Exception e=new Exception();
						throw e;
					}
				}
				else{
					//���� ������ '+'�� '-'�� �ƴ� ���
					while((stack.getTopOfStack()>=0) && precedence(stack.top())>=precedence(infix.charAt(cnt))){
						//stack�� ä�����ִ��� �켱������ �̺��� ���� ���� ���� ��
					
						postfix.append(stack.pop());
						postfix.append(' ');
						
					}
					stack.push(infix.charAt(cnt));//�׸��� ���� �� �����ڸ� Ǫ��
				}

			}
			else if(infix.charAt(cnt)=='*' || infix.charAt(cnt)=='/' || infix.charAt(cnt)=='%'){
				//'*'�� '/', '%'�����ڸ� ���� ���
				//������ ���� ó������ �����ʴ� ����
				while((stack.getTopOfStack()>=0) && precedence(stack.top())>=precedence(infix.charAt(cnt))){
					//stack�� ä�����ִ��� �켱������ �̺��� ���� ���� ���� ��
				
					postfix.append(stack.pop());
					postfix.append(' ');
					
				}
				stack.push(infix.charAt(cnt));//�׸��� ���� �� �����ڸ� Ǫ��

			}
			else if(infix.charAt(cnt)=='^'){//'^'�� ���� ���
				while((stack.getTopOfStack()>=0) && precedence(stack.top())>precedence(infix.charAt(cnt))){
					//stack�� ä�����ִ��� �켱������ �̺��� ���� ���� ���� ��
				
					postfix.append(stack.pop());
					postfix.append(' ');					
				}
				stack.push(infix.charAt(cnt));//�׸��� ���� �� �����ڸ� Ǫ��
			}
			else if((is_number(infix.charAt(cnt))==1) && (is_number(infix.charAt(cnt+1))==1)){
				//���ڰ� �������� �� ��� �����ڸ� �����̹Ƿ� ���й���(����)���� Ǫ���Ѵ�.
				postfix.append(infix.charAt(cnt));
			}
			else{//���ڸ����� ��� ������ �־� �����ڿ� �������� �����Ѵ�.
				
				postfix.append(infix.charAt(cnt));
				postfix.append(' ');
				
			}

		}//for���� ��

		if(infix.charAt(LengthOfExpression-1)==')'){//������ ���ڰ� ')'�϶� '('���� ��, �������� ����.
			while(stack.top()!='('){
				postfix.append(stack.pop());
				postfix.append(' ');
			}
			stack.pop();
		}
		else if(is_number(infix.charAt(LengthOfExpression-1))==1){
			postfix.append(infix.charAt(LengthOfExpression-1));
			postfix.append(' ');
		}
		else{
			System.out.println("This is called2");
		}

		while(!stack.isEmpty()){//���� ���ÿ� ���� �����ڰ� ������ ���� ��
			postfix.append(stack.pop());
			postfix.append(' ');
		}
		
		//System.out.println(postfix);
		return postfix;
	} 
	
	private static int is_operator(char c){//���� argument�� �������̸� 1�� �����ϰ� �ƴϸ� 0�� ����
		if(c=='+' || c=='-' || c=='*' || c=='/' || c=='^' || c=='%')
			return 1;
		else
			return 0;
	}
	
	private static int is_operator2(char c){
		if(c=='+' || c=='-' || c=='*' || c=='/' || c=='^' || c=='%' || c=='~')
			return 1;
		else
			return 0;
	}
	
	private static int precedence(int oper){//�� �ڵ�� ���� ������ ��ȯ�Ѵ�. 
		if(oper=='(')
			return 0;
		else if(oper=='+' || oper=='-')
			return 1;
		else if(oper=='*' || oper=='/' || oper=='%')
			return 2;
		else if(oper=='~')
			return 3;
		else if(oper=='^')
			return 4;
		else
			return 5;
	}
	
	private static int is_number(char c){

		if((c>='0' && c<='9') || c=='.')//�Ҽ����� �׳� ���ڶ�� ����
			return 1;
		else
			return 0;
		
	}
	
	private static void printAnswer(long num, StringBuffer postfix){

			System.out.println(postfix);
			System.out.println(num);
			
	}
	
	private static long calculateWithPostfix(StringBuffer postfix, StackForCalculate stacknum)throws Exception{
		int LenthOfPostfix=postfix.length();
		long num1, num2;
		
		for(int i=0; i<LenthOfPostfix; i++){
			
			//�� ���ڿ��� ���߿� ���ڷ� �ٲ��. ���ڷ� �ٲپ� ���ο� ����(double)�� �־� ����� �� ���� �ڵ��� �� �ִ�.
			StringBuffer number=new StringBuffer();
			
			if( (is_operator2(postfix.charAt(i))==1) && postfix.charAt(i)!=' ' ){
				//�����ڸ� ������ ��, �ΰ��� ���Ͽ� ����ѵ� �ٽ� stack�� �ִ´�.
				long temp_int1=1, temp_int2=1;

				switch(postfix.charAt(i)){
				//�ڿ� ���� �����ڸ� ������, �ڿ� ���°ſ��� ����.
				case '+':
					num2=stacknum.pop();
					num1=stacknum.pop();
					stacknum.push((long)(num1+num2));
					break;			
				case '-':
					num2=stacknum.pop();
					num1=stacknum.pop();
					stacknum.push((long)(num1-num2));
					break;
				case '*':
					num2=stacknum.pop();
					num1=stacknum.pop();
					stacknum.push((long)(num1*num2));
					break;
				case '/':
					num2=stacknum.pop();
					num1=stacknum.pop();
					stacknum.push((long)(num1/num2));
					break;					
				case '%':
					temp_int1=stacknum.pop();
					temp_int2=stacknum.pop();
					stacknum.push((long)((temp_int2)%(temp_int1)));
					break;
				case '^':
					num2=stacknum.pop();
					num1=stacknum.pop();
					if(num1==0 && num2<0){
						Exception e=new Exception();
						throw e;
					}
					stacknum.push((long)Math.pow(num1, num2));
					break;
				case '~':
					//unary�� ���
					num1=stacknum.pop();
					stacknum.push((long)(-num1));
					break;
				}
			}//if
			//���׿����ڿ� ���ڹ����� ��� number�� �ִ´�.
			else if(postfix.charAt(i)!=' ' && is_number(postfix.charAt(i))==1){
				
				int cnt=0;
			
				while(postfix.charAt(i+cnt)!=' ')//�� �ڸ������� ���
					cnt++;
				for(int j=0;j<cnt;j++)
					number.append(postfix.charAt(i+j));
				String NumberString=new String(number);
				long x=Long.parseLong(NumberString);//number�� �Ǽ��� ���ڷ� �ٲپ��ش�.
				i=i+cnt;
			
				stacknum.push(x);//���ο� ���ÿ� �ִ´�, ��� �ڵ尡 ������

			}
		
		}//for
		
		long answer=stacknum.pop();//�������� stack�� �����ִ� ���� �� 
		return answer;

		
	}	
	
	private static void isWrongExpression(String infix) throws Exception{
		//error�� ����
		//�����ڰ� ������ ���ӿ��� ���(^^, **, //, %%)
		//%�� ������ ��������?
		//��ȣ�� �߸��� ���
		//�̻��� ������
		//error�� ������ true�� ��ȯ
		int length=infix.length();
		int NumberOfLeftBracket=0, NumberOfRightBracket=0;
		
		
		for(int i=0; i<length; i++){
			
			////�����ڰ� ������ ���ӿ��� ���(^^, **, //, %%)
			if(	i<(length-1) && (infix.charAt(i)==infix.charAt(i+1)) && (is_operator(infix.charAt(i))==1) &&
					(infix.charAt(i)!='+' && infix.charAt(i)!='-')){
				Exception e=new Exception();
				throw e;
			}
			
			//�̻��� ������
			//���ڵ� �ƴϰ�, �����ڵ� �ƴϰ� ��ȣ�� �ƴ� ���
			if( is_number(infix.charAt(i))==0 && is_operator(infix.charAt(i))==0 && 
					infix.charAt(i)!='(' && infix.charAt(i)!=')' ){
				Exception e=new Exception();
				throw e;
			}
			
			if(infix.charAt(i)=='.'){
				Exception e=new Exception();
				throw e;
			}
			
			if((i<(length-1)) && (is_number(infix.charAt(i))==1) && infix.charAt(i+1)=='('){
				Exception e=new Exception();
				throw e;
			}
			
			//��ȣ�� �߸��� ���
			if(infix.charAt(i)=='('){
				NumberOfLeftBracket++;
			}
			else if(infix.charAt(i)==')'){
				NumberOfRightBracket++;
			}
			
			//�׳� ��ȣ�� �ִ� ���
			if( i<length-1 && infix.charAt(i)=='(' && infix.charAt(i+1)==')'){
				Exception e=new Exception();
				throw e;
			}
			
			//��ȣ�ڿ� ���ڰ� �ٷο��� ���
			if((i<length-1) && (infix.charAt(i)==')') && 
					((is_number(infix.charAt(i+1))==1) || infix.charAt(i+1)=='(')){
				
				Exception e=new Exception();
				throw e;
			
			}
		
		}//for��
		
		if(NumberOfLeftBracket!=NumberOfRightBracket){
			Exception e=new Exception();
			throw e;
		}
		
		//�������� �̻��� ���� ���� ���
		//�������� ������ �̰ų�, �������� ���ڰ� �ƴϰ� ��ȣ�� �ƴ� ���
		if(is_operator(infix.charAt(length-1))==1 || 
				(is_number(infix.charAt(length-1))==0 && infix.charAt(length-1)!='(' && infix.charAt(length-1)!=')' )){
			Exception e=new Exception();
			throw e;
		}
		
	}	
	
}

class Stack{
	//infix�� postfix�� �ٷ�� ���� ����
	private char[] stack;
	private int NumberOfElement;
	private int TopOfStackNum;
	
	//////////////////////////////////////
	
	public Stack(int size){
		stack=new char[size];
		NumberOfElement=0;
		TopOfStackNum=-1;
	}
	public Stack(){
		stack=new char[100];
		NumberOfElement=0;
		TopOfStackNum=-1;
	}
	
	public void push(char value){
		TopOfStackNum++;
		stack[TopOfStackNum]=value;
		NumberOfElement++;
	}
	
	public char pop(){
		char tmp=stack[TopOfStackNum--];
		NumberOfElement--;
		return tmp;
	}
	
	public char top(){
		return stack[TopOfStackNum];
	}
	
	public boolean isEmpty(){
		if(NumberOfElement==0)
			return true;
		else
			return false;
	}
	
	public int getTopOfStack(){
		return TopOfStackNum;
	}
}

class StackForCalculate{
	
	private long[] StackNum;
	private int TopOfStackNum;
	private int NumberOfElement;
	
	//////////////////////////////////////
	public StackForCalculate(int size){
		StackNum=new long[size];
		TopOfStackNum=-1;
		NumberOfElement=0;
	}
	public StackForCalculate(){
		StackNum=new long[100];
		TopOfStackNum=-1;
		NumberOfElement=0;
	}
	public void push(long value){
		TopOfStackNum++;
		StackNum[TopOfStackNum]=value;
		NumberOfElement++;
	}
	public long top(){
		return StackNum[TopOfStackNum];
	}
	public long pop(){
		long tmp=StackNum[TopOfStackNum--];
		NumberOfElement--;
		return tmp;
	}
	public int getNumberOfElement(){
		return NumberOfElement;
	}
	public boolean isEmpty(){
		if(NumberOfElement==0)
			return true;
		else
			return false;
	}
}





















