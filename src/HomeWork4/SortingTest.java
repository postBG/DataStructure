package HomeWork4;

import java.io.*;
import java.util.*;

public class SortingTest {

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// �Է¹��� �迭�� �����ΰ� �ƴѰ�?
			int[] value;	// �Է� ���� ���ڵ��� �迭
			String nums = br.readLine();	// ù ���� �Է� ����
			if (nums.charAt(0) == 'r')
			{
				// ������ ���
				isRandom = true;	// �������� ǥ��

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// �� ����
				int rminimum = Integer.parseInt(nums_arg[2]);	// �ּҰ�
				int rmaximum = Integer.parseInt(nums_arg[3]);	// �ִ밪

				Random rand = new Random();	// ���� �ν��Ͻ��� �����Ѵ�.

				value = new int[numsize];	// �迭�� �����Ѵ�.
				for (int i = 0; i < value.length; i++)	// ������ �迭�� ������ �����Ͽ� ����
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// ������ �ƴ� ���
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// �迭�� �����Ѵ�.
				for (int i = 0; i < value.length; i++)	// ���پ� �Է¹޾� �迭���ҷ� ����
					value[i] = Integer.parseInt(br.readLine());
			}

			// ���� �Է��� �� �޾����Ƿ� ���� ����� �޾� �׿� �´� ������ �����Ѵ�.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// ���� ���� ��ȣ�� ���� ���纻�� �����Ѵ�.

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
						return;	// ���α׷��� �����Ѵ�.
					default:
						throw new IOException("�߸��� ���� ����� �Է��߽��ϴ�.");
				}
				if (isRandom)
				{
					// ������ ��� ����ð��� ����Ѵ�.
					System.out.println((System.currentTimeMillis() - t) + " ms");
					/*for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}*/
				}
				else
				{
					// ������ �ƴ� ��� ���ĵ� ������� ����Ѵ�.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("�Է��� �߸��Ǿ����ϴ�. ���� : " + e.toString());
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
					//�� ������ ��ȯ�Ѵ�.
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
		// TODO : Insertion Sort �� �����϶�.
		int size=value.length;
		
		for(int i=1; i<size; i++){
			
			int tmp=value[i];
			int j=i;
			
			while(j>0 && value[j-1]>tmp){
				//���� �迭�� ���� ���� ���� ���
				value[j]=value[j-1];//�迭�� ��ĭ�� �ڷ� �̷��.
				j--;
			}
			
			value[j]=tmp;
		}
		
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value)
	{
		// TODO : Heap Sort �� �����϶�.
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
	{	// from "C�� ���� �˰���"�� �ڹٷ� ��ħ, �� ���� ����
		
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
	//heap�� �迭�� �����ϸ�, ������ ����Ʈ���̴�.
	//�θ����� �ε����� ((�ڽĳ���ε���-1)/2) �̰�, �ڽĳ���� �ε����� (�θ����ε���*2+1), (�θ����ε���*2+2)�̴�
	//���γ���� ���� (HeapSize-1)/2������ �ε����� ������ �͵��̴�.
	//1�� �ε������� ����ϰ� 0���� ���ʸ� �д�
	
	final int INT_MAX=Integer.MAX_VALUE;
	
	private int[] HeapArray;
	private int HeapSize;
	
	public HeapForSort(){
		//�� ���� ��� ������ ��������
	}
	
	public int getHeapSize(){
		return HeapSize;
	}
	
	public HeapForSort(int[] value){
		
		HeapArray=value;//�߰����� �޸𸮴� ����
		
		HeapSize=0;
		for(int i=0; i<value.length; i++){
			this.insert(value[i]);
		}
		
	}
	
	public void makeUpHeap(int nodeindex){
		//���� ������ �迭�� ��½�ų ����� index �� �޾� upheap��Ų��.
		int NodeValue=HeapArray[nodeindex];
		//������ 0��° index�� ������� �����Ƿ� ���ʸ� �д�.
		
		while(nodeindex!=0 && HeapArray[(nodeindex-1)/2]<=NodeValue){
			
			//�ڽ� ��忡 �θ��� ���� �ִ´�
			HeapArray[nodeindex]=HeapArray[(nodeindex-1)/2];
			
			nodeindex=(nodeindex-1)/2;
		}
		
		HeapArray[nodeindex]=NodeValue;
	}
	
	public void makeDownHeap(int nodeindex){
		//���� ������ �迭�� �� ���� ũ��(�迭�� ũ�Ⱑ �ƴϴ�), �׸��� �ϰ���ų ����� �ε����� �Է¹޾� downheap��Ų��.
		int NodeValue=HeapArray[nodeindex];
		int IndexForChild;//�ڽ� ����� �ε����� �����ϴµ� �̿��Ѵ�.
		
		while(nodeindex<=((HeapSize)/2-1)){
			//���γ�忡 ���ؼ�
			IndexForChild=nodeindex*2+1;//IndexForChild�� ���� �ڽĳ�带 ����Ű�� �ȴ�.
			
			//�� ū �ڽ� ���� ���ϱ� ���� ������ �ڽ��� �� ũ�� ������ �ڽ��� �����ϰ� Index�� �ø���
			if(IndexForChild<HeapSize && HeapArray[IndexForChild]<HeapArray[IndexForChild+1])
				IndexForChild++;
			
			if(NodeValue>=HeapArray[IndexForChild])//�θ��� ����  ���� �� ū �ڽĺ��� Ŀ���� ������. 
				break;
			
			HeapArray[nodeindex]=HeapArray[IndexForChild];
			nodeindex=IndexForChild;
		}
		HeapArray[nodeindex]=NodeValue;
		
	}
	
	public void insert(int NewValue){
		//���� ���� ��忡 ���� �Ŀ� heap���ǿ� �°� upheap��Ų��. 
		makeUpHeap(HeapSize);
		HeapSize++;
	}
	
	public int extract(){
		
		int RootValue=HeapArray[0];
		HeapArray[0]=HeapArray[HeapSize-1];//������ �Ѹ��� �ø���
		HeapSize--;
		
		makeDownHeap(0);
		
		return RootValue;
	}
	
	public void returnSortedArray(int[] value){
		//���ڷ� ���� �迭�� �� ����Ʈ�� ��Ʈ�� ���� �־��ش�.
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
