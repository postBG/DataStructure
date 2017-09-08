package Easy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class NumberOfWords {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Hashtable<String, ArrayList<Integer>> H=new Hashtable<>();
		
		while(true){	
			try {
				
				String input=br.readLine();
				
				if(input.charAt(0)=='Q')
					break;
				
				StringTokenizer st=new StringTokenizer(input);
				
				String address=st.nextToken();
				Integer value=Integer.parseInt(st.nextToken());
				
				if(H.containsKey(address)){
					
					H.get(address).add(value);
					
				}
				else{
					ArrayList<Integer> Tmp=new ArrayList<>();
					Tmp.add(value);
					H.put(address, Tmp);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		while(true){
				
			try {
				
				String input=br.readLine();
				
				if(input.charAt(0)=='Q')
					break;
				
				
				System.out.println("here");
				for(int i=0; i<H.get(input).size(); i++)
					System.out.println(H.get(input).get(i));
				

				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
	}

}
