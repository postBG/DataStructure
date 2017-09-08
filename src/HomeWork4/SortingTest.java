package HomeWork4;

import java.io.*;
import java.util.*;

public class SortingTest {

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
					/*for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}*/
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		int size=value.length;
		int tmp;
		
		for(int i=0; i<size; i++){
			
			for(int j=1; j<size-i; j++){
				
				if(value[j-1]>value[j]){
					//더 작으면 교환한다.
					tmp=value[j-1];
					value[j-1]=value[j];
					value[j]=tmp;
				}
				
			}
			
		}
		return (value);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		// TODO : Insertion Sort 를 구현하라.
		int size=value.length;
		
		for(int i=1; i<size; i++){
			
			int tmp=value[i];
			int j=i;
			
			while(j>0 && value[j-1]>tmp){
				//앞의 배열이 보다 작은 값일 경우
				value[j]=value[j-1];//배열을 한칸씩 뒤로 미룬다.
				j--;
			}
			
			value[j]=tmp;
		}
		
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value)
	{
		// TODO : Heap Sort 를 구현하라.
		HeapForSort heap=new HeapForSort(value);
		heap.returnSortedArray(value);
		
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value)
	{	
		Mergesort sort=new Mergesort();
		sort.sort(value);
		
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value)
	{	// from "C로 배우는 알고리즘"을 자바로 고침, 또 에러 수정
		
		int size=value.length;
		int pivot, tmp;
		int LeftIndex, RightIndex;
		int LeftBoundary, RightBoundary;
		
		Random rand=new Random();
		Stack<Integer> StackLeft=new Stack<>();
		Stack<Integer> StackRight=new Stack<>();
		
		LeftBoundary=0;
		RightBoundary=size-1;
		
		StackRight.push(RightBoundary);
		StackLeft.push(LeftBoundary);
		
		while(!StackRight.isEmpty() && !StackLeft.isEmpty()){
			LeftBoundary=StackLeft.pop();
			RightBoundary=StackRight.pop();
			
			if(RightBoundary-LeftBoundary+1>1){
				tmp=(rand.nextInt(RightBoundary-LeftBoundary+1)+LeftBoundary);
				
				pivot=value[tmp];
				value[tmp]=value[RightBoundary];
				value[RightBoundary]=pivot;
				
				LeftIndex=LeftBoundary;
				RightIndex=RightBoundary-1;
				
				while(true){
					while(LeftIndex<RightBoundary && value[LeftIndex]<pivot)
						LeftIndex++;
					while(RightIndex>LeftBoundary && value[RightIndex]>=pivot)
						RightIndex--;
					
					if(LeftIndex>=RightIndex)
						break;
					
					tmp=value[LeftIndex];
					value[LeftIndex]=value[RightIndex];
					value[RightIndex]=tmp;
					
				}
				
				tmp=value[LeftIndex];
				value[LeftIndex]=value[RightBoundary];
				value[RightBoundary]=tmp;
				
				StackRight.push(RightBoundary);
				StackLeft.push(LeftIndex+1);
				StackRight.push(LeftIndex-1);
				StackLeft.push(LeftBoundary);
			}
			
		}
		
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value){
		
		int MaxValue=value[0];
		int MinValue=value[0];
		
		for(int i=0; i<value.length; i++){ //searching for max and min value
			if(value[i]>MaxValue){
				MaxValue=value[i];
			}
			if(value[i]<MinValue){
				MinValue=value[i];
			}
		}
		
		int MaxDigit=digit_count(MaxValue)>digit_count(MinValue)? digit_count(MaxValue):digit_count(MinValue);
		
		int c;
		ArrayList<ArrayList<Integer>> Positions=new ArrayList<ArrayList<Integer>>();
		for(int j=0; j<20; j++){
			Positions.add(new ArrayList<Integer>());
		}
		int d;
		int digit = 1;
		
		for(int i=0; i<MaxDigit; i++){
			
			for(int j=0; j<value.length; j++){
				d=((value[j]/digit))%10+10;
				Positions.get(d).add(value[j]);
			}
			digit *= 10;
			c=0;
			
			for(int j=1; j<20; j++){
				
				for(int index=0; index<Positions.get(j).size(); index++){
					value[c++]=Positions.get(j).get(index);
				}
				Positions.get(j).clear();
			}
			
		}
		
		return value;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int digit_count(int n){
		
		int d=0;
		
		while(n!=0){
			d++;
			n/=10;
		}
		
		return d;
	}
	///////////////////////////////////////////////////////////////////////////////
	
}

class HeapForSort{
	//heap은 배열로 구현하며, 완전한 이진트리이다.
	//부모노드의 인덱스는 ((자식노드인덱스-1)/2) 이고, 자식노드의 인덱스는 (부모노드인덱스*2+1), (부모노드인덱스*2+2)이다
	//내부노드의 수는 (HeapSize-1)/2까지의 인덱스를 가지는 것들이다.
	//1번 인덱스부터 사용하고 0번은 보초를 둔다
	
	final int INT_MAX=Integer.MAX_VALUE;
	
	private int[] HeapArray;
	private int HeapSize;
	
	public HeapForSort(){
		//쓸 일이 없어서 구현은 하지않음
	}
	
	public int getHeapSize(){
		return HeapSize;
	}
	
	public HeapForSort(int[] value){
		
		HeapArray=value;//추가적인 메모리는 없다
		
		HeapSize=0;
		for(int i=0; i<value.length; i++){
			this.insert(value[i]);
		}
		
	}
	
	public void makeUpHeap(int nodeindex){
		//힙을 저장한 배열과 상승시킬 노드의 index 를 받아 upheap시킨다.
		int NodeValue=HeapArray[nodeindex];
		//힙에서 0번째 index는 사용하지 않으므로 보초를 둔다.
		
		while(nodeindex!=0 && HeapArray[(nodeindex-1)/2]<=NodeValue){
			
			//자식 노드에 부모의 값을 넣는다
			HeapArray[nodeindex]=HeapArray[(nodeindex-1)/2];
			
			nodeindex=(nodeindex-1)/2;
		}
		
		HeapArray[nodeindex]=NodeValue;
	}
	
	public void makeDownHeap(int nodeindex){
		//힙을 저장한 배열과 그 힙의 크기(배열의 크기가 아니다), 그리고 하강시킬 노드의 인덱스를 입력받아 downheap시킨다.
		int NodeValue=HeapArray[nodeindex];
		int IndexForChild;//자식 노드의 인덱스를 추적하는데 이용한다.
		
		while(nodeindex<=((HeapSize)/2-1)){
			//내부노드에 한해서
			IndexForChild=nodeindex*2+1;//IndexForChild는 왼쪽 자식노드를 가리키게 된다.
			
			//더 큰 자식 노드와 비교하기 위해 오른쪽 자식이 더 크면 오른쪽 자식을 참조하게 Index를 올린다
			if(IndexForChild<HeapSize && HeapArray[IndexForChild]<HeapArray[IndexForChild+1])
				IndexForChild++;
			
			if(NodeValue>=HeapArray[IndexForChild])//부모의 값이  값이 더 큰 자식보다 커지면 나간다. 
				break;
			
			HeapArray[nodeindex]=HeapArray[IndexForChild];
			nodeindex=IndexForChild;
		}
		HeapArray[nodeindex]=NodeValue;
		
	}
	
	public void insert(int NewValue){
		//가장 말단 노드에 삽입 후에 heap조건에 맞게 upheap시킨다. 
		makeUpHeap(HeapSize);
		HeapSize++;
	}
	
	public int extract(){
		
		int RootValue=HeapArray[0];
		HeapArray[0]=HeapArray[HeapSize-1];//말단을 뿌리로 올린다
		HeapSize--;
		
		makeDownHeap(0);
		
		return RootValue;
	}
	
	public void returnSortedArray(int[] value){
		//인자로 받은 배열에 이 힙소트로 소트한 값을 넣어준다.
		for(int i=value.length-1; i>=0; i--){
			value[i]=extract();
		}
	}
	
	public void printHeap(){
		
		for(int i=0; i<HeapSize; i++){
			System.out.printf("%d ", HeapArray[i]);
		}
		System.out.println();
	}
}
class Mergesort{
	//copy from "http://www.vogella.com/tutorials/JavaAlgorithmsMergesort/article.html"
	  private int[] numbers;
	  private int[] helper;

	  private int number;

	  public void sort(int[] values) {
	    this.numbers = values;
	    number = values.length;
	    this.helper = new int[number];
	    mergesort(0, number - 1);
	  }

	  private void mergesort(int low, int high) {
	    // check if low is smaller then high, if not then the array is sorted
	    if (low < high) {
	      // Get the index of the element which is in the middle
	      int middle = low + (high - low) / 2;
	      // Sort the left side of the array
	      mergesort(low, middle);
	      // Sort the right side of the array
	      mergesort(middle + 1, high);
	      // Combine them both
	      merge(low, middle, high);
	    }
	  }

	  private void merge(int low, int middle, int high) {

	    // Copy both parts into the helper array
	    for (int i = low; i <= high; i++) {
	      helper[i] = numbers[i];
	    }

	    int i = low;
	    int j = middle + 1;
	    int k = low;
	    // Copy the smallest values from either the left or the right side back
	    // to the original array
	    while (i <= middle && j <= high) {
	      if (helper[i] <= helper[j]) {
	        numbers[k] = helper[i];
	        i++;
	      } else {
	        numbers[k] = helper[j];
	        j++;
	      }
	      k++;
	    }
	    // Copy the rest of the left side of the array into the target array
	    while (i <= middle) {
	      numbers[k] = helper[i];
	      k++;
	      i++;
	    }

	  }
	} 
