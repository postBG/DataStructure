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
				
				//공백을 제거한다.
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
				//System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}	
	}
	
	private static void checkSpaceBetweenNumber(String infix) throws Exception{
		int length=infix.length();
		
		for(int i=0; i<length-1; i++){
			
			if((is_number(infix.charAt(i))==1) && 
					((infix.charAt(i+1)==' ') || (infix.charAt(i+1)=='	'))){
				//숫자 뒤에 바로 공백이나 탭이 오는 경우
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
			
			if(infix.charAt(cnt)=='('){//'('는 stack에 넣는다
				//0번째일때도 작동한다
				stack.push(infix.charAt(cnt));
			}
			else if(infix.charAt(cnt)==')'){//')'를 만날 경우 '('가 나올때까지 전부 팝하고 '('는 버린다
				//수식의 맨 처음에는 오지 않는 문자
				//예외처리
				while(stack.top()!='('){
					//'('를 만날때까지
					postfix.append(stack.pop());
					postfix.append(' ');
				}
				
				//'('는 버린다
				stack.pop();
			}
			else if((infix.charAt(cnt)=='+' || infix.charAt(cnt)=='-')){

				//먼저 단항 연산자부터 처리한다
				if(cnt==0){//가장 앞에 온 경우
					if(infix.charAt(cnt)=='-'){
						stack.push('~');
					}
					else{
						//단항 연산자이고 '+'인 경우
						//아무런 작업도 하지 않음
						Exception e=new Exception();
						throw e;
					}
				}
				else if( is_operator(infix.charAt(cnt-1))==1 || infix.charAt(cnt-1)=='(' ){
					//이런 경우 단항 연산자이다.
					//*-, +-, ^-, (-, %-, /- 들과 같은 형식이므로
					if(infix.charAt(cnt)=='-'){
						stack.push('~');
					}
					else{
						//'+'단항 연산자의 경우
						//아무것도 하지 않음
						Exception e=new Exception();
						throw e;
					}
				}
				else{
					//단항 연산자 '+'나 '-'가 아닌 경우
					while((stack.getTopOfStack()>=0) && precedence(stack.top())>=precedence(infix.charAt(cnt))){
						//stack이 채워져있는한 우선순위가 이보다 위인 것은 전부 팝
					
						postfix.append(stack.pop());
						postfix.append(' ');
						
					}
					stack.push(infix.charAt(cnt));//그리고 나서 이 연산자를 푸쉬
				}

			}
			else if(infix.charAt(cnt)=='*' || infix.charAt(cnt)=='/' || infix.charAt(cnt)=='%'){
				//'*'나 '/', '%'연산자를 만난 경우
				//수식의 가장 처음에는 오지않는 문자
				while((stack.getTopOfStack()>=0) && precedence(stack.top())>=precedence(infix.charAt(cnt))){
					//stack이 채워져있는한 우선순위가 이보다 위인 것은 전부 팝
				
					postfix.append(stack.pop());
					postfix.append(' ');
					
				}
				stack.push(infix.charAt(cnt));//그리고 나서 이 연산자를 푸쉬

			}
			else if(infix.charAt(cnt)=='^'){//'^'을 만난 경우
				while((stack.getTopOfStack()>=0) && precedence(stack.top())>precedence(infix.charAt(cnt))){
					//stack이 채워져있는한 우선순위가 이보다 위인 것은 전부 팝
				
					postfix.append(stack.pop());
					postfix.append(' ');					
				}
				stack.push(infix.charAt(cnt));//그리고 나서 이 연산자를 푸쉬
			}
			else if((is_number(infix.charAt(cnt))==1) && (is_number(infix.charAt(cnt+1))==1)){
				//숫자가 연속으로 온 경우 여러자리 숫자이므로 구분문자(공백)없이 푸쉬한다.
				postfix.append(infix.charAt(cnt));
			}
			else{//한자리수인 경우 공백을 넣어 연산자와 숫자임을 구별한다.
				
				postfix.append(infix.charAt(cnt));
				postfix.append(' ');
				
			}

		}//for문의 끝

		if(infix.charAt(LengthOfExpression-1)==')'){//마지막 문자가 ')'일때 '('까지 팝, 위에서와 같다.
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

		while(!stack.isEmpty()){//만일 스택에 남은 연산자가 있으면 전부 팝
			postfix.append(stack.pop());
			postfix.append(' ');
		}
		
		//System.out.println(postfix);
		return postfix;
	} 
	
	private static int is_operator(char c){//만일 argument가 연산자이면 1을 리턴하고 아니면 0을 리턴
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
	
	private static int precedence(int oper){//이 코드는 연산 순위를 반환한다. 
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

		if((c>='0' && c<='9') || c=='.')//소숫점도 그냥 숫자라고 생각
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
			
			//이 문자열은 나중에 숫자로 바뀐다. 숫자로 바꾸어 새로운 스택(double)에 넣어 계산을 더 쉽게 코딩할 수 있다.
			StringBuffer number=new StringBuffer();
			
			if( (is_operator2(postfix.charAt(i))==1) && postfix.charAt(i)!=' ' ){
				//연산자를 만났을 때, 두개를 팝하여 계산한뒤 다시 stack에 넣는다.
				long temp_int1=1, temp_int2=1;

				switch(postfix.charAt(i)){
				//뒤에 나온 연산자를 나누고, 뒤에 나온거에서 뺀다.
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
					//unary인 경우
					num1=stacknum.pop();
					stacknum.push((long)(-num1));
					break;
				}
			}//if
			//단항연산자와 숫자문자의 경우 number에 넣는다.
			else if(postfix.charAt(i)!=' ' && is_number(postfix.charAt(i))==1){
				
				int cnt=0;
			
				while(postfix.charAt(i+cnt)!=' ')//몇 자리수인지 계산
					cnt++;
				for(int j=0;j<cnt;j++)
					number.append(postfix.charAt(i+j));
				String NumberString=new String(number);
				long x=Long.parseLong(NumberString);//number를 실수형 숫자로 바꾸어준다.
				i=i+cnt;
			
				stacknum.push(x);//새로운 스택에 넣는다, 계산 코드가 쉬워짐

			}
		
		}//for
		
		long answer=stacknum.pop();//마지막에 stack에 남아있던 것이 답 
		return answer;

		
	}	
	
	private static void isWrongExpression(String infix) throws Exception{
		//error의 종류
		//연산자가 여러번 연속오는 경우(^^, **, //, %%)
		//%에 정수만 오는지는?
		//괄호가 잘못된 경우
		//이상한 연산자
		//error가 있으면 true를 반환
		int length=infix.length();
		int NumberOfLeftBracket=0, NumberOfRightBracket=0;
		
		
		for(int i=0; i<length; i++){
			
			////연산자가 여러번 연속오는 경우(^^, **, //, %%)
			if(	i<(length-1) && (infix.charAt(i)==infix.charAt(i+1)) && (is_operator(infix.charAt(i))==1) &&
					(infix.charAt(i)!='+' && infix.charAt(i)!='-')){
				Exception e=new Exception();
				throw e;
			}
			
			//이상한 연산자
			//숫자도 아니고, 연산자도 아니고 괄호도 아닌 경우
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
			
			//괄호가 잘못된 경우
			if(infix.charAt(i)=='('){
				NumberOfLeftBracket++;
			}
			else if(infix.charAt(i)==')'){
				NumberOfRightBracket++;
			}
			
			//그냥 괄호만 있는 경우
			if( i<length-1 && infix.charAt(i)=='(' && infix.charAt(i+1)==')'){
				Exception e=new Exception();
				throw e;
			}
			
			//괄호뒤에 숫자가 바로오는 경우
			if((i<length-1) && (infix.charAt(i)==')') && 
					((is_number(infix.charAt(i+1))==1) || infix.charAt(i+1)=='(')){
				
				Exception e=new Exception();
				throw e;
			
			}
		
		}//for문
		
		if(NumberOfLeftBracket!=NumberOfRightBracket){
			Exception e=new Exception();
			throw e;
		}
		
		//마지막에 이상한 것이 오는 경우
		//마지막이 연산자 이거나, 마지막이 숫자가 아니고 괄호도 아닌 경우
		if(is_operator(infix.charAt(length-1))==1 || 
				(is_number(infix.charAt(length-1))==0 && infix.charAt(length-1)!='(' && infix.charAt(length-1)!=')' )){
			Exception e=new Exception();
			throw e;
		}
		
	}	
	
}

class Stack{
	//infix와 postfix를 다루기 위한 스택
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





















