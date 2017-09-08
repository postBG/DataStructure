package HomeWork1;

import java.io.*; // �Է��� �ޱ� ���� �� ���̺귯���� �ʿ��ϴ�.

public class BigInteger{
	public static void main(String args[]){
		// �Է��� �ޱ� ���� �۾��̴�.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// quit�� ���� �� ���� �Է��� �޾ƾ� �ϹǷ�
		// while(true) ���� ����Ͽ� ��� �ݺ��Ѵ�.
		while (true)
		{
			try{
				// �� ���� �Է� �޾� input ���ڿ��� ����
				String input = br.readLine();	
			
				if (input.compareTo("quit") == 0)//quit�� �Է����� ������ ����
					break;

				//�׷� �� ������ ���� ����
				input=input.replace(" ", "");
				input=input.replace("	", "");
			
				
				Calculate(input);					
				
			}
			catch (Exception e)
			{
				// ���� try { } �ȿ��� ������ �߻��ߴٸ� �̰����� ���� �ȴ�.
				// �̷��� �����ν� �Է��� �̻��ϰ� ���� ��� �߻��ϴ� ������ �����Ѵ�.
				System.out.println("�Է��� �߸��Ǿ����ϴ�. ���� : " + e.toString());
			}
	}
}

	private static void Calculate(String input){
		//������ �ǿ����ڿ� �����ڷ� �ٲ۴�
		int len=input.length();
		int temp=0;
		char ContainOper='\0';
		
		for(int i=1; i<len; i++){//ó���� +�� ���� ��ȣ�� ���� ��츦 �����ϱ� ���� 1��° ���ں��� �д´�	
			if(input.charAt(i)=='+' || input.charAt(i)=='-' || input.charAt(i)=='*'){
				//�����ڸ� ������ oper�� �����ϰ� ������ 
				ContainOper=input.charAt(i);
				temp=i;
				break;
			
			}
		}
		
		char[] ContainNum1=new char[temp];
		
		for(int i=0; i<temp; i++){
			//�����ڰ� ������ ������ ���ڴ� ù��° �Է°����̴�
			ContainNum1[i]=input.charAt(i);
		}
		
		char[] ContainNum2=new char[len-temp-1];
		
		for(int i=temp+1; i<len; i++){
			//������ �������ʹ� num2�� �����̴�
			ContainNum2[i-(temp+1)]=input.charAt(i);
		}
		
		SelectSign(ContainNum1, ContainNum2, ContainOper);
		
	}

	private static void SelectSign(char[] ContainNum1, char[] ContainNum2, char ContainOper){
	
		//��ȣ�� �����ϰ� ������ �����ڵ带 ȣ���ϴ� �ڵ��̴�. ���õ� ��ȣ�� ������ sign�� �����Ѵ�.
		char sign1, sign2;
		char ContainSign='\0';
		
		//num1�� ��ȣ�� ���Ѵ�
		if(ContainNum1[0]!='+' && ContainNum1[0]!='-')//�� �տ� ��ȣ�� ����������
			sign1='+';//��ȣ�� +�̴�
		else//�� ���� ��ȣ�� �� ��ȣ�� ����
			sign1=ContainNum1[0];
	
		//num2�� ��ȣ�� ���Ѵ�
		if(ContainNum2[0]!='+' && ContainNum2[0]!='-')//�� �տ� ��ȣ�� ����������
			sign2='+';//��ȣ�� +�̴�
		else//�� ���� ��ȣ�� �� ��ȣ�� ����
			sign2=ContainNum2[0];
		
		//��ȣ�� ������� ���ڿ��� ��� ��ȣ�� �����
		//�̶� MakeNumberOnlyString�� ����
		ContainNum1=MakeNumberOnlyString(ContainNum1);
		ContainNum2=MakeNumberOnlyString(ContainNum2);
	
		//��������� num1�� num2�� ��ȣ�� ������ ���񰪵��̴�
		
		//������ ��� ��ȣ�� �����Ѵ�
		if(ContainOper=='*'){
		
			if((sign1=='+' && sign2=='+') || (sign1=='-' && sign2=='-')){
				//(+, +)�� ���ϴ� ���� (-, -)�� ���ϴ� ���
				//�̷� ��� �׻� ����� ����� �ȴ�
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
				//�׿��� ���
				//�̷� ��� �׻� ����� ����� �ȴ�
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
		else{//����Ȥ�� ������ ���� ��ȣ�� ������
		
			if((sign1=='+' && ContainOper=='+' && sign2=='+') || (sign1=='+' && ContainOper=='-' && sign2=='-')){
				//�̷� ��� �׻� ����� ����� �ȴ�
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
			
				if(BiggerNum(ContainNum1, ContainNum2)==1 || BiggerNum(ContainNum1, ContainNum2)==2){//���� num1�� ������ ũ�ų� �� ���� ���ٸ�
					//����� ����� �ȴ�
					ContainSign='+';
					MinusBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else{
					//num2�� ������ �� ũ�� ������ �ȴ�
					ContainSign='-';
					MinusBigInteger(ContainNum2, ContainNum1, ContainSign);
				}
			
			}
			else if((sign1=='-' && ContainOper=='+' && sign2=='+') || (sign1=='-' && ContainOper=='-' && sign2=='-')){
			
				if(BiggerNum(ContainNum1, ContainNum2)==1){//num1�� ������ ũ�� 
					//��� ����� '-'
					ContainSign='-';
					MinusBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else if(BiggerNum(ContainNum1, ContainNum2)==2){
					//���� ������ ������
					ContainSign='+';
					MinusBigInteger(ContainNum1, ContainNum2, ContainSign);
				}
				else{
					//���� num2�� ������ �� ũ��
					ContainSign='+';
					MinusBigInteger(ContainNum2, ContainNum1, ContainSign);
				}
			
			}
			else if((sign1=='-' && ContainOper=='-' && sign2=='+') || (sign1=='-' && ContainOper=='+' && sign2=='-')){
				//�������� �׻� ������ ���
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
		}//��ȣ�� ���� if��
	
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
		//num1�� num2�� �޾Ƽ� ���ϴ� ������ �����Ѵ�	
		int carry=0;
		
		int top=ContainNum1.length;
		int low=ContainNum2.length;
	
		int differ=top-low;
	
		//�ڸ������� ����� ���� �ӽ������� �ִ´� 
		int[] temp=new int[120];
	
		for(int i=top; i>(top-low); --i){
			//���� ����Ѵ�
			//���� ������ ���ڸ��� �� Ŀ���Ƿ� index�� ���� top�� ������ ���� �����Ѵ�
			temp[i]=((ContainNum1[i-1]+ContainNum2[i-differ-1]+carry)%48);
		
			if(temp[i]>=10){
				//����  10���� ���ų� ũ��
				temp[i]=temp[i]%10;
				carry=1;
	
			}
			else{
				//���� 10���� ������ �״�� ����
				carry=0;
			}
		}
	
		if(differ!=0){//�� ���� �ڸ����� �ٸ���
			//num1�� �ִ� �ڸ����� ����ϴ� ����
			for(int i=differ; i>0; i--){
			
				temp[i]=(ContainNum1[i-1]+carry)%48;
			
				if(temp[i]>=10){
					//���� �μ��� ���� 10���� Ŭ ��� 
					temp[i]=temp[i]%10;
					carry=1;
				}
				else{
					carry=0;
				}
			}
		}
		else{
			//���� �ڸ����̸� 
			temp[0]=0;
		}
	
		if(carry==1){
			//������ carry�� 1�̸�
			//���� ���ڸ��� 1
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
		//num1�� num2�� �޾� ���� ������ �����Ѵ�
		
		//���� �߰��� ���ڸ� �����ϱ� ���� �뵵
		int temp[]=new int[120];
		int borrow=0;
	
		int top=ContainNum1.length;
		int low=ContainNum2.length;
	
		int differ=top-low;
	
		for(int i=top-1; i>=(top-low); i--){
		
			if(ContainNum1[i]>=ContainNum2[i-differ]){
				//borrow�� ��� ������ �����Ҷ�
				temp[i]=(ContainNum1[i]-ContainNum2[i-differ])%48;
			}
			else{
				
				//����  ���� ���ڸ��� 0�̸�
				int tmp=1;
				while(ContainNum1[i-tmp]=='0'){
					//���ڸ����� 0�� ���ȿ�
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
		//num1�� num2�� �޾� ���ϴ� ������ �����Ѵ�
		int carry=0;
		int cnt=0, tmp=0;
		int temp[]=new int[240];
	
		int top=ContainNum1.length;
		int low=ContainNum2.length;
	
		//������ ���� �ϱ� ���� �������� �ٲ۴�
		ContainNum1=makeReverse(ContainNum1);
		ContainNum2=makeReverse(ContainNum2);
	
		for(int i=0; i<top; ++i){
		
			if(low==1){//���ڸ����̸�
			
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
		//��ȣ�� ������ ������ ���ڸ��� ���ڿ��� �����. ���߿� ���������� �ٷ�� ����
		//��ȣ�� ������� ��ȣ�� �����
		String tmp1=new String(ContainNum);
		StringBuffer num=new StringBuffer(tmp1);
		
		if(num.charAt(0)=='+' || num.charAt(0)=='-')
			num.deleteCharAt(0);
	
		tmp1=num.toString();
		
		char[] returnVal=tmp1.toCharArray();
		//��ȣ�� ���� ��� �ƹ��͵� ���� �ʰ� �����Ѵ�
		return returnVal;
	}

	private static int BiggerNum(char[] ContainNum1, char[] ContainNum2){
		//�� ���� ������ 2, num1�� ũ�� 1, num2�� ũ�� 0�� �����ϴ� �Լ� 
		//���ڵ��� �̹� ��ȣ�� ������ �����̹Ƿ� ���ڸ� �ٷ��
		int len1=ContainNum1.length;
		int len2=ContainNum2.length;
		
		if(len1>len2)//���밪�̹Ƿ� ���̰� ��� ū���̴�
			return 1;
		else if(len1<len2)
			return 0;
		else{//�� ���� ���̰� ���� ���
			
			for(int i=0; i<len1; ++i){
				
				if(ContainNum1[i]!=ContainNum2[i]){//�� �ڸ����� ���Ͽ� ���ڰ� �޶�����
					
					if(ContainNum1[i]>ContainNum2[i])//num1�� ���ڰ� �� ũ�� num1�� �� ū��
						return 1;
					else//�׷��� ���� ��� num2�� �� ū��
						return 0;
						
				}
					
			}
			//���� ���� �ݺ����� ����ϸ� ��� ���ڰ� �����Ƿ� ���� �����̴�
			return 2;
		}
			
		//error�� �ִ� ���
		
	}
	
	private static char[] MakeResult(int[] temp,int start, int end, char ContainSign){
		//tempString�� result�� �̿��� ����� result���ڿ��� �ϼ��Ѵ�.
		//start�� end�� �̿��ؼ� tempString�� ��𼭺��� ������ ������ ��ȿ�� ���ڰ� �ִ��� �� �� �ִ�
	
		int cnt=0;
		char[] returnVal;
		
		for(int i=0; i<=end; i++){
		
			if(temp[i]==0)
				cnt++;//0�� ����
		}
	
		if(cnt==end+1){//���� 0�̸�
			returnVal=new char[2];
			returnVal[0]=(char)48;
			returnVal[1]='\0';
		}
		else{
		
			if(start!=0){
			
				returnVal=new char[end-start+1];
				
				for(int i=start; i<=end; ++i){
					//�ƽ�Ű�ڵ�� ��ȯ
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
		
		//�տ� ������ 0���� �߸��� �´�
		if(result.charAt(0)!='-'){
		
			if(result.charAt(0)=='+' && result.charAt(1)=='\0')
				System.out.println(0);
			else if(result.charAt(0)=='+' && result.charAt(1)=='0' && result.charAt(2)=='\0')
				System.out.println(0);
			else{
				//+��ȣ�� �����ϰ� ���� ���
				result.deleteCharAt(0);
				
				String tmp2=new String(result);
				ContainResult=tmp2.toCharArray();
				
				System.out.println(ContainResult);
			}
		}
		else{
		
			if(result.charAt(1)=='0'){//����� 0�� ���
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

