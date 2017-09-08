package HomeWork1;

import java.io.*; // 입력을 받기 위해 이 라이브러리가 필요하다.

public class BigInteger{
	public static void main(String args[]){
		// 입력을 받기 위한 작업이다.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// quit가 나올 때 까지 입력을 받아야 하므로
		// while(true) 문을 사용하여 계속 반복한다.
		while (true)
		{
			try{
				// 한 줄을 입력 받아 input 문자열에 저장
				String input = br.readLine();	
			
				if (input.compareTo("quit") == 0)//quit이 입력으로 들어오면 종료
					break;

				//그런 뒤 공백을 전부 제거
				input=input.replace(" ", "");
				input=input.replace("	", "");
			
				
				Calculate(input);					
				
			}
			catch (Exception e)
			{
				// 만약 try { } 안에서 오류가 발생했다면 이곳으로 오게 된다.
				// 이렇게 함으로써 입력을 이상하게 했을 경우 발생하는 오류를 방지한다.
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
	}
}

	private static void Calculate(String input){
		//수식을 피연산자와 연산자로 바꾼다
		int len=input.length();
		int temp=0;
		char ContainOper='\0';
		
		for(int i=1; i<len; i++){//처음에 +와 같은 부호가 오는 경우를 배제하기 위해 1번째 문자부터 읽는다	
			if(input.charAt(i)=='+' || input.charAt(i)=='-' || input.charAt(i)=='*'){
				//연산자를 만나면 oper에 저장하고 나간다 
				ContainOper=input.charAt(i);
				temp=i;
				break;
			
			}
		}
		
		char[] ContainNum1=new char[temp];
		
		for(int i=0; i<temp; i++){
			//연산자가 나오기 전까지 문자는 첫번째 입력값들이다
			ContainNum1[i]=input.charAt(i);
		}
		
		char[] ContainNum2=new char[len-temp-1];
		
		for(int i=temp+1; i<len; i++){
			//연산자 다음부터는 num2의 값들이다
			ContainNum2[i-(temp+1)]=input.charAt(i);
		}
		
		SelectSign(ContainNum1, ContainNum2, ContainOper);
		
	}

	private static void SelectSign(char[] ContainNum1, char[] ContainNum2, char ContainOper){
	
		//부호를 결정하고 적절한 연산코드를 호출하는 코드이다. 선택된 부호를 인자인 sign에 저장한다.
		char sign1, sign2;
		char ContainSign='\0';
		
		//num1의 부호를 취한다
		if(ContainNum1[0]!='+' && ContainNum1[0]!='-')//맨 앞에 부호가 오지않으면
			sign1='+';//부호는 +이다
		else//맨 앞이 부호면 그 부호를 저장
			sign1=ContainNum1[0];
	
		//num2의 부호를 취한다
		if(ContainNum2[0]!='+' && ContainNum2[0]!='-')//맨 앞에 부호가 오지않으면
			sign2='+';//부호는 +이다
		else//맨 앞이 부호면 그 부호를 저장
			sign2=ContainNum2[0];
		
		//부호를 얻었으면 숫자에서 모두 부호를 떼어낸다
		//이때 MakeNumberOnlyString을 쓴다
		ContainNum1=MakeNumberOnlyString(ContainNum1);
		ContainNum2=MakeNumberOnlyString(ContainNum2);
	
		//여기부터의 num1과 num2는 부호가 없어진 절댓값들이다
		
		//곱셈일 경우 부호를 결정한다
		if(ContainOper=='*'){
		
			if((sign1=='+' && sign2=='+') || (sign1=='-' && sign2=='-')){
				//(+, +)를 곱하는 경우와 (-, -)를 곱하는 경우
				//이런 경우 항상 결과가 양수가 된다
				if(BiggerNum(ContainNum1, ContainNum2)==1 || BiggerNum(ContainNum1, ContainNum2)==2){
					ContainSign='+';
					MultiBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else{
					
					ContainSign='+';
					MultiBigInteger(ContainNum2, ContainNum1, ContainSign);
				}
			}
			else{
				//그외의 경우
				//이런 경우 항상 결과가 양수가 된다
				if(BiggerNum(ContainNum1, ContainNum2)==1 || BiggerNum(ContainNum1, ContainNum2)==2){
					ContainSign='-';
					MultiBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else{
					
					ContainSign='-';
					MultiBigInteger(ContainNum2, ContainNum1, ContainSign);
				}
			}
		
		}
		else{//덧셈혹은 뺄셈에 대해 부호를 결정함
		
			if((sign1=='+' && ContainOper=='+' && sign2=='+') || (sign1=='+' && ContainOper=='-' && sign2=='-')){
				//이런 경우 항상 결과가 양수가 된다
				if(BiggerNum(ContainNum1, ContainNum2)==1 || BiggerNum(ContainNum1, ContainNum2)==2){
					ContainSign='+';
					PlusBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else{
					
					ContainSign='+';
					PlusBigInteger(ContainNum2, ContainNum1, ContainSign);
				}
			}
			else if((sign1=='+' && ContainOper=='+' && sign2=='-') || (sign1=='+' && ContainOper=='-' && sign2=='+')){
			
				if(BiggerNum(ContainNum1, ContainNum2)==1 || BiggerNum(ContainNum1, ContainNum2)==2){//만일 num1의 절댓값이 크거나 두 수가 같다면
					//결과가 양수가 된다
					ContainSign='+';
					MinusBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else{
					//num2의 절댓값이 더 크면 음수가 된다
					ContainSign='-';
					MinusBigInteger(ContainNum2, ContainNum1, ContainSign);
				}
			
			}
			else if((sign1=='-' && ContainOper=='+' && sign2=='+') || (sign1=='-' && ContainOper=='-' && sign2=='-')){
			
				if(BiggerNum(ContainNum1, ContainNum2)==1){//num1의 절댓값이 크면 
					//계산 결과가 '-'
					ContainSign='-';
					MinusBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else if(BiggerNum(ContainNum1, ContainNum2)==2){
					//서로 절댓값이 같으면
					ContainSign='+';
					MinusBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else{
					//만일 num2의 절댓값이 더 크면
					ContainSign='+';
					MinusBigInteger(ContainNum2, ContainNum1, ContainSign);
				}
			
			}
			else if((sign1=='-' && ContainOper=='-' && sign2=='+') || (sign1=='-' && ContainOper=='+' && sign2=='-')){
				//연산결과가 항상 음수인 경우
				if(BiggerNum(ContainNum1, ContainNum2)==1 || BiggerNum(ContainNum1, ContainNum2)==2){
					ContainSign='-';
					PlusBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else{
					ContainSign='-';
					PlusBigInteger(ContainNum2, ContainNum1, ContainSign);
				}
			}
			else{
			
			
			
			}
		}//부호에 대한 if문
	
	}

	private static void MergeSignAndNumber(char ContainSign, char[] ContainResult){
		
		String tmp=new String(ContainResult);
		StringBuffer result=new StringBuffer(tmp);
		
		result.insert(0, ContainSign);
		
		tmp=result.toString();
		ContainResult=tmp.toCharArray();
		
		PrintResult(ContainResult);
	}

	private static void PlusBigInteger(char[] ContainNum1, char[] ContainNum2, char ContainSign){
		//num1과 num2를 받아서 더하는 연산을 실행한다	
		int carry=0;
		
		int top=ContainNum1.length;
		int low=ContainNum2.length;
	
		int differ=top-low;
	
		//자릿수마다 계산한 값을 임시적으로 넣는다 
		int[] temp=new int[120];
	
		for(int i=top; i>(top-low); --i){
			//합을 계산한다
			//합은 많으면 한자리가 더 커지므로 index의 값이 top인 곳부터 값을 저장한다
			temp[i]=((ContainNum1[i-1]+ContainNum2[i-differ-1]+carry)%48);
		
			if(temp[i]>=10){
				//합이  10보다 같거나 크면
				temp[i]=temp[i]%10;
				carry=1;
	
			}
			else{
				//합이 10보다 작으면 그대로 저장
				carry=0;
			}
		}
	
		if(differ!=0){//두 수가 자릿수가 다르면
			//num1만 있는 자릿수를 계산하는 과정
			for(int i=differ; i>0; i--){
			
				temp[i]=(ContainNum1[i-1]+carry)%48;
			
				if(temp[i]>=10){
					//만일 두수의 합이 10보다 클 경우 
					temp[i]=temp[i]%10;
					carry=1;
				}
				else{
					carry=0;
				}
			}
		}
		else{
			//같은 자릿수이면 
			temp[0]=0;
		}
	
		if(carry==1){
			//마지막 carry가 1이면
			//가장 앞자리가 1
			temp[0]=1;
			char[] ContainResult=MakeResult(temp,0, top, ContainSign);
			MergeSignAndNumber(ContainSign, ContainResult);
		}
		else{
		
			char[] ContainResult=MakeResult(temp,1, top, ContainSign);
			MergeSignAndNumber(ContainSign, ContainResult);
		}
	
	}//Plus

	private static void MinusBigInteger(char[] ContainNum1, char[] ContainNum2, char ContainSign){
		//num1과 num2를 받아 빼는 연산을 시행한다
		
		//연산 중간에 숫자를 저장하기 위한 용도
		int temp[]=new int[120];
		int borrow=0;
	
		int top=ContainNum1.length;
		int low=ContainNum2.length;
	
		int differ=top-low;
	
		for(int i=top-1; i>=(top-low); i--){
		
			if(ContainNum1[i]>=ContainNum2[i-differ]){
				//borrow가 없어도 뺄셈이 가능할때
				temp[i]=(ContainNum1[i]-ContainNum2[i-differ])%48;
			}
			else{
				
				//만일  빼는 앞자리가 0이면
				int tmp=1;
				while(ContainNum1[i-tmp]=='0'){
					//앞자리들이 0인 동안에
					ContainNum1[i-tmp]='9';
					tmp++;
				}
				char tempContainer=ContainNum1[i-tmp];
		
				ContainNum1[i-tmp]=(char)(tempContainer-1);
				
				borrow=10;
			
				temp[i]=(ContainNum1[i]-ContainNum2[i-differ]+borrow)%48;
			
			}
		}
	
		for(int i=0; i<top-low; ++i){
		
			temp[i]=(ContainNum1[i])%48;
		}
	
		char[] ContainResult=MakeResult(temp, 0, top-1, ContainSign);
		ContainResult=TruncZero(ContainResult);
		
		MergeSignAndNumber(ContainSign, ContainResult);
	}

	private static void MultiBigInteger(char[] ContainNum1, char[] ContainNum2, char ContainSign){
		//num1과 num2를 받아 곱하는 연산을 실행한다
		int carry=0;
		int cnt=0, tmp=0;
		int temp[]=new int[240];
	
		int top=ContainNum1.length;
		int low=ContainNum2.length;
	
		//곱셈을 쉽게 하기 위해 역순으로 바꾼다
		ContainNum1=makeReverse(ContainNum1);
		ContainNum2=makeReverse(ContainNum2);
	
		for(int i=0; i<top; ++i){
		
			if(low==1){//한자리수이면
			
				for(int j=0; j<low; ++j){
				
					tmp=(ContainNum1[i]-48)*(ContainNum2[j]-48)+carry+temp[i+j];
				
					temp[i+j]=tmp%10;
					carry=tmp/10;
				}
			}
			else{
			
				if(cnt==low){
					carry=0;
					cnt=0;
				}
			
				for(int j=0; j<low; j++){
					tmp=(ContainNum1[i]-48)*(ContainNum2[j]-48)+carry+temp[i+j];
				
					temp[i+j]=tmp%10;
					carry=tmp/10;
					cnt++;
				}
			
				if(carry!=0){
					temp[i+low]=carry;
				}
			}
		
		}//for
	
		if(carry!=0){
		
			temp[top+low-1]=carry;
			char[] ContainResult=MakeResult(temp, 0, top+low-1, ContainSign);
			ContainResult=makeReverse(ContainResult);
			
			MergeSignAndNumber(ContainSign, ContainResult);
		}
		else{
		
			char[] ContainResult=MakeResult(temp, 0, top+low-2, ContainSign);
			ContainResult=makeReverse(ContainResult);
			
			MergeSignAndNumber(ContainSign, ContainResult);
		}
	}

	private static char[] MakeNumberOnlyString(char[] ContainNum){
		//부호를 제거한 완전한 숫자만의 문자열로 만든다. 나중에 안정적으로 다루기 위함
		//부호가 있을경우 부호를 떼어낸다
		String tmp1=new String(ContainNum);
		StringBuffer num=new StringBuffer(tmp1);
		
		if(num.charAt(0)=='+' || num.charAt(0)=='-')
			num.deleteCharAt(0);
	
		tmp1=num.toString();
		
		char[] returnVal=tmp1.toCharArray();
		//부호가 없는 경우 아무것도 하지 않고 종료한다
		return returnVal;
	}

	private static int BiggerNum(char[] ContainNum1, char[] ContainNum2){
		//두 수가 같으면 2, num1이 크면 1, num2가 크면 0을 리턴하는 함수 
		//인자들은 이미 부호를 제거한 상태이므로 숫자만 다룬다
		int len1=ContainNum1.length;
		int len2=ContainNum2.length;
		
		if(len1>len2)//절대값이므로 길이가 길면 큰수이다
			return 1;
		else if(len1<len2)
			return 0;
		else{//두 수의 길이가 같은 경우
			
			for(int i=0; i<len1; ++i){
				
				if(ContainNum1[i]!=ContainNum2[i]){//각 자릿수를 비교하여 숫자가 달라지면
					
					if(ContainNum1[i]>ContainNum2[i])//num1의 숫자가 더 크면 num1이 더 큰수
						return 1;
					else//그렇지 않은 경우 num2가 더 큰수
						return 0;
						
				}
					
			}
			//만일 위의 반복문을 통과하면 모든 숫자가 같으므로 같은 숫자이다
			return 2;
		}
			
		//error가 있는 경우
		
	}
	
	private static char[] MakeResult(int[] temp,int start, int end, char ContainSign){
		//tempString과 result를 이용해 출력할 result문자열을 완성한다.
		//start와 end를 이용해서 tempString에 어디서부터 어디까지 복사할 유효한 숫자가 있는지 알 수 있다
	
		int cnt=0;
		char[] returnVal;
		
		for(int i=0; i<=end; i++){
		
			if(temp[i]==0)
				cnt++;//0의 갯수
		}
	
		if(cnt==end+1){//전부 0이면
			returnVal=new char[2];
			returnVal[0]=(char)48;
			returnVal[1]='\0';
		}
		else{
		
			if(start!=0){
			
				returnVal=new char[end-start+1];
				
				for(int i=start; i<=end; ++i){
					//아스키코드로 변환
					returnVal[i-start]=(char)(temp[i]+48);
				}
			
			}
			else{
			
				returnVal=new char[end+1];
				
				for(int i=start; i<=end; i++){
				
					returnVal[i]=(char)(temp[i]+48);
				}
				
			}
		}
		
		return returnVal;
			
	}//MakeResult

	private static void PrintResult(char[] ContainResult){
	
		String tmp=new String(ContainResult);
		StringBuffer result=new StringBuffer(tmp);
		
		//앞에 나오는 0들은 잘리고 온다
		if(result.charAt(0)!='-'){
		
			if(result.charAt(0)=='+' && result.charAt(1)=='\0')
				System.out.println(0);
			else if(result.charAt(0)=='+' && result.charAt(1)=='0' && result.charAt(2)=='\0')
				System.out.println(0);
			else{
				//+부호는 생략하고 답을 출력
				result.deleteCharAt(0);
				
				String tmp2=new String(result);
				ContainResult=tmp2.toCharArray();
				
				System.out.println(ContainResult);
			}
		}
		else{
		
			if(result.charAt(1)=='0'){//결과가 0인 경우
				System.out.println(0);
			}
			else if(result.charAt(1)=='\0' && result.charAt(2)=='0')
				System.out.println(0);
			else{
				String tmp2=new String(result);
				ContainResult=tmp2.toCharArray();
				
				System.out.println(ContainResult);
			}
		}
	}//PrintResult

	private static char[] TruncZero(char[] ContainResult){
	
		int i=0;
		int cnt=0;
		
		while(ContainResult[i]=='0'){
			cnt++;
			i++;
		}
	
		String tmp=new String(ContainResult);
		StringBuffer result=new StringBuffer(tmp);
		result.delete(0, cnt);
		
		tmp=result.toString();
		
		char[] returnVal=tmp.toCharArray();
		
		return returnVal;
	}
	
	private static char[] makeReverse(char[] ContainResult){
		
		String tmp=new String(ContainResult);
		StringBuffer result=new StringBuffer(tmp);
		
		result=result.reverse();
		
		tmp=result.toString();
		ContainResult=tmp.toCharArray();
		
		return ContainResult;
	}
}

