package HomeWork6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.StringTokenizer;

public class Subway {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		GraphOfStation Subways=new GraphOfStation();
		String filename=args[0];
		
		try {
					
			FileReader fr=new FileReader(filename);
			BufferedReader BrForFile=new BufferedReader(fr);
					
			String tmpString;
					
			while((tmpString=BrForFile.readLine())!=null){
				if(tmpString.length()==0){
					Subways.SettingTransferStation();
					break;	
				}
						
				Subways.PushStation(tmpString);
			}
					
			while((tmpString=BrForFile.readLine())!=null){
						
				Subways.setAdjacent(tmpString);
						
			}
					
			fr.close();
				
		} catch (Exception e) {
				
				e.printStackTrace();
				
		}
		
		while(true){
			try {
				String input=br.readLine();
				
				if(input.compareTo("QUIT")==0)
					break;
				
				StringTokenizer st=new StringTokenizer(input);
				if(st.countTokens()==2){
					String StartName=st.nextToken();
					String EndName=st.nextToken();
				
					FindShortestWayVer1(Subways, StartName, EndName);
				}
				else{
					String StartName=st.nextToken();
					String EndName=st.nextToken();
					
					FindShortestWayVer2(Subways, StartName, EndName);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	static private void FindShortestWayVer1(GraphOfStation Subways, String StartName, String EndName){
		DurationComparator ComparatorWithDuration=new DurationComparator();
		PriorityQueue<Station> DurationQueue=new PriorityQueue<>(20, ComparatorWithDuration);
		HashMap<String, Station> ForReset=new HashMap<>();
		
		Station Start;
		ArrayList<Station> StartSet=Subways.SearchStationByName(StartName);
		
		//맨 처음까지 걸리는 시간은 0
		for(int i=0; i<StartSet.size(); i++){
			Start=StartSet.get(i);
			Start.setDuration(0);
			DurationQueue.add(new Station(Start));
			ForReset.put(Start.getCode(), Start);
		}
		
		int NumberOfAdjacent;
		
		Station iter;
		do{
			//우선순위 큐의 가장 윗노드를 취한다
			iter=DurationQueue.remove();
			if(iter.getName().compareTo(EndName)==0)//도착역을 찾았으면
				break;//나간다
			
			NumberOfAdjacent=iter.getNumberOfAdjacentStation();
			Station Tmp;
			
			for(int i=0; i<NumberOfAdjacent; i++){
				Tmp=iter.getAdjacentStation(i);
			
				if(Tmp.getDuration()>iter.getDuration()+iter.getAdjacentTime(i)){
					
					Tmp.setDuration(iter.getDuration()+iter.getAdjacentTime(i));
					Tmp.setPrevStation(iter);
					
					ForReset.put(Tmp.getCode(), Tmp);
					DurationQueue.add(new Station(Tmp));
				}
			}
			
		}while(!DurationQueue.isEmpty());
		
		long FinalDuration=iter.getDuration();
		
		Stack<Station> StackForBackTracking=new Stack<>();
		StackForBackTracking.add(iter);
		
		while(iter.getPrevStation()!=null){
			if(iter.getPrevStation().getName().compareTo(iter.getName())==0){
				
				if(iter.getPrevStation().getPrevStation()!=null){
					//환승을 한 경우
					iter.setflag(1);
					iter=iter.getPrevStation();
					continue;
				}
				FinalDuration-=5;
				iter=iter.getPrevStation();
				continue;
			}
			StackForBackTracking.add(iter.getPrevStation());
			iter=iter.getPrevStation();
		}
		
		Station tmp;
		int RoadLength=StackForBackTracking.size();
		for(int i=0; i<RoadLength-1; i++){
			tmp=StackForBackTracking.pop();
			if(tmp.getflag()!=0){
				//환승역
				System.out.print("["+tmp.getName()+"] ");
			}
			else{
				System.out.print(tmp.getName()+' ');
			}
		}
		tmp=StackForBackTracking.pop();
		if(tmp.getflag()!=0){
			//환승역
			System.out.print("["+tmp.getName()+"]");
		}
		else{
			System.out.print(tmp.getName());
		}
		
		System.out.println();
		System.out.println(FinalDuration);
		
		for(Station value:ForReset.values()){
			value.Reset();
		}
	}
	static private void FindShortestWayVer2(GraphOfStation Subways, String StartName, String EndName){
		DurationComparator ComparatorWithDuration=new DurationComparator();
		PriorityQueue<Station> DurationQueue=new PriorityQueue<>(20, ComparatorWithDuration);
		HashMap<String, Station> ForReset=new HashMap<>();
		
		Station Start;
		ArrayList<Station> StartSet=Subways.SearchStationByName(StartName);
		
		//맨 처음까지 걸리는 시간은 0
		for(int i=0; i<StartSet.size(); i++){
			Start=StartSet.get(i);
			Start.setDuration(0);
			DurationQueue.add(new Station(Start));
			ForReset.put(Start.getCode(), Start);
		}
		
		int NumberOfAdjacent;
		
		Station iter;
		do{
			//우선순위 큐의 가장 윗노드를 취한다
			iter=DurationQueue.remove();
			System.out.println(iter.getDuration());
			if(iter.getName().compareTo(EndName)==0)//도착역을 찾았으면
				break;//나간다
			
			NumberOfAdjacent=iter.getNumberOfAdjacentStation();
			Station Tmp;
			
			for(int i=0; i<NumberOfAdjacent; i++){
				Tmp=iter.getAdjacentStation(i);
				
				if(Tmp.getName().compareTo(iter.getName())==0){
					if(Tmp.getDuration()>iter.getDuration()+Integer.MAX_VALUE){

						Tmp.setDuration(iter.getDuration()+Integer.MAX_VALUE);
						Tmp.setPrevStation(iter);
						
						ForReset.put(Tmp.getCode(), Tmp);
						DurationQueue.add(new Station(Tmp));
					}
				}
				else{
					if(Tmp.getDuration()>iter.getDuration()+iter.getAdjacentTime(i)){
					
						Tmp.setDuration(iter.getDuration()+iter.getAdjacentTime(i));
						Tmp.setPrevStation(iter);
					
						ForReset.put(Tmp.getCode(), Tmp);
						DurationQueue.add(new Station(Tmp));
					}
				}
			}
			
		}while(!DurationQueue.isEmpty());
		
		long FinalDuration=iter.getDuration();
		
		Stack<Station> StackForBackTracking=new Stack<>();
		StackForBackTracking.add(iter);
		
		while(iter.getPrevStation()!=null){
			
			if(iter.getPrevStation().getName().compareTo(iter.getName())==0){
				
				if(iter.getPrevStation().getPrevStation()!=null){
					//환승을 한 경우
					iter.setflag(1);
					FinalDuration-=Integer.MAX_VALUE;
					FinalDuration+=5;
					iter=iter.getPrevStation();
					continue;
				}
				FinalDuration-=Integer.MAX_VALUE;
				iter=iter.getPrevStation();
				continue;
			}
			StackForBackTracking.add(iter.getPrevStation());
			iter=iter.getPrevStation();
		}
		
		Station tmp;
		int RoadLength=StackForBackTracking.size();
		for(int i=0; i<RoadLength-1; i++){
			tmp=StackForBackTracking.pop();
			if(tmp.getflag()!=0){
				//환승역
				System.out.print("["+tmp.getName()+"] ");
			}
			else{
				System.out.print(tmp.getName()+' ');
			}
		}
		tmp=StackForBackTracking.pop();
		if(tmp.getflag()!=0){
			//환승역
			System.out.print("["+tmp.getName()+"]");
		}
		else{
			System.out.print(tmp.getName());
		}
		
		System.out.println();
		System.out.println(FinalDuration);
		
		for(Station value:ForReset.values()){
			value.Reset();
		}
	}
}
class DurationComparator implements Comparator<Station>{

	@Override
	public int compare(Station o1, Station o2) {
		if(o1.getDuration()<o2.getDuration())
			return -1;
		else if(o1.getDuration()==o2.getDuration())
			return 0;
		else 
			return 1;
	}
	
}
class GraphOfStation{
	
	private HashMap<String, Station> CodeTable;//코드명으로 정렬할 때 이용
	private HashMap<String, ArrayList<Station>> NameTable;//이름으로 정렬할 때 이용 
	private Integer ForTransferTime;

	private Station[] GraphForNameSearch;
	private ArrayList<String> ForTransfer;
	private int NumberOfStation;
	//////////////////////////////////////////////////////////
	
	public GraphOfStation(){
		NumberOfStation=0;
		
		CodeTable=new HashMap<>();
		NameTable=new HashMap<>();
		ForTransferTime=new Integer(5);
		ForTransfer=new ArrayList<>();
	}
	
	public int getNumberOfStation(){
		return NumberOfStation;
	}
	public Station[] getGraphForNameSearch(){
		return GraphForNameSearch;
	}
	public Station SearchStationByCode(String Code){
		//BinarySearch이용	
		return CodeTable.get(Code);
	}
	
	public ArrayList<Station> SearchStationByName(String Name){
		//BinarySearch이용	
		return NameTable.get(Name);
	}
	public void PushStation(String input){
		//input을 받아 역을 만든다.
		StringTokenizer st=new StringTokenizer(input);
		PushStation(st.nextToken(), st.nextToken());
	
	}
	public void PushStation(String Code, String Name){
		//해쉬테이블로 고친다.
		Station NewStation=new Station(Code ,Name);
		
		if(NameTable.containsKey(Name)){
			//이미 있는 경우 환승역
			ArrayList<Station> Tmp=NameTable.get(Name);
			if(Tmp.size()==1)
				ForTransfer.add(Name);
			Tmp.add(NewStation);
		}
		else{
			
			ArrayList<Station> Tmp=new ArrayList<>();
			Tmp.add(NewStation);
			NameTable.put(Name, Tmp);
			
		}
		
		CodeTable.put(Code, NewStation);
		NumberOfStation++;
	}
	public void SettingTransferStation(){
		
		int size=ForTransfer.size();
		ArrayList<Station> Tmp;
		Station s1, s2;
		for(int k=0; k<size; k++){
			Tmp=NameTable.get(ForTransfer.get(k));
				
			for(int i=0; i<Tmp.size()-1; i++){
				s1=Tmp.get(i);
				for(int j=i+1; j<Tmp.size(); j++){
					s2=Tmp.get(j);				
					//서로 추가
					s1.setAdjacentStationAndTime(s2, ForTransferTime);
					s2.setAdjacentStationAndTime(s1, ForTransferTime);	
				}
			}
				
		}
		
	}
	public void setAdjacent(String input){
		//시간 정보를 저장한다
		StringTokenizer st=new StringTokenizer(input);
		String StartCode=st.nextToken();
		String EndCode=st.nextToken();
		String TimeString=st.nextToken();
		
		int time=Integer.parseInt(TimeString);
		
		setAdjacent(StartCode, EndCode, time);
	}
	public void setAdjacent(String StartCode, String EndCode, int time){
		Station StartStation=SearchStationByCode(StartCode);
		Station EndStation=SearchStationByCode(EndCode);
		
		StartStation.setAdjacentStationAndTime(EndStation, time);
	}
	
}

class Station{
	private String Code;
	private String Name;
	
	private long Duration=Long.MAX_VALUE;//지금까지의 누적시간
	private Station PrevStation=null;//backtracking에 이용
	
	private ArrayList<Station> AdjacentStation=new ArrayList<>();
	private ArrayList<Integer> TimeToAdjacentStation=new ArrayList<>();
	private int flag=0;//환승여부
	///////////////////////////////////////////////////////
	
	public Station(String Code, String Name){
		this.Name=new String(Name);
		this.Code=new String(Code);
	}
	public Station(Station NewStation){
		Code=new String(NewStation.getCode());
		Name=new String(NewStation.getName());
		Duration=NewStation.getDuration();
		AdjacentStation=NewStation.getAdjacentStationList();
		TimeToAdjacentStation=NewStation.getAdjacentTimeList();
		PrevStation=NewStation.PrevStation;
	}
	public long getDuration(){
		return Duration;
	}
	
	public Station getPrevStation(){
		return PrevStation;
	}
	
	public String getName(){
		return Name;
	}
	public String getCode(){
		return Code;
	}
	public int getflag(){
		return flag;
	}
	public void setflag(int newflag){
		this.flag=newflag;
	}
	public void setAdjacentStationAndTime(Station Node, int time){
		
		AdjacentStation.add(Node);
		TimeToAdjacentStation.add(time);
		return;
		
	}
	public void setAdjacentStationAndTime(Station Node, Integer ForTransferTime){
		
		AdjacentStation.add(Node);
		TimeToAdjacentStation.add(ForTransferTime);
		return;
		
	}
	public void setDuration(long newDuration){
		this.Duration=newDuration;
	}
	public void setPrevStation(Station PrevStation){
		this.PrevStation=PrevStation;
	}
	
	public Station getAdjacentStation(int i){
		return AdjacentStation.get(i);
	}
	public ArrayList<Station> getAdjacentStationList(){
		return AdjacentStation;
	}
	public ArrayList<Integer> getAdjacentTimeList(){
		return TimeToAdjacentStation;
	}
	public int getAdjacentTime(int i){
		return TimeToAdjacentStation.get(i);
	}
	public int getNumberOfAdjacentStation(){
		return AdjacentStation.size();
	}
	public void Reset(){
		Duration=Long.MAX_VALUE;
		PrevStation=null;
		flag=0;
	}
}