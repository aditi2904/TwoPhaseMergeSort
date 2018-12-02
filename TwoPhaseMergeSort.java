package MergeSortTwoPhase;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;





public class TwoPhaseMergeSort {
	static final int recordSize = 4;
	static  String inputFile = "";
	static  String outputFile = "";
	static  Integer totalRecords =0;
	static  Integer memoryRecords =0;
	static  Integer numSubList =0 ;
	static  Integer blockSize =0;
	static  Integer numMemPages=0;
	static  Integer numInputBuffers=0;
	static  Integer numberOfPages=(1000000*4)/(1024);
	static Integer numOfPasses=0;
	
	public static void main(String[] args) throws Exception {
		inputFile=args[0];
		outputFile = args[1];
		 totalRecords=1000000;
		 long freemem = Runtime.getRuntime().freeMemory();
		 memoryRecords = (int) ((freemem)/((recordSize)*32));
		 numSubList = (int) Math.ceil(totalRecords / (memoryRecords*1.0)) ;
		 blockSize =  1024;
		 numMemPages = (int) ((freemem)/blockSize );
		 numInputBuffers = numMemPages - 1;
		 numberOfPages=(1000000*4)/(1024);
		 numOfPasses = (int) Math.ceil( (Math.log10(numSubList) / Math.log10(numInputBuffers)) );
		long beginTime = new Date().getTime();
		try {
			System.out.println("Creating Sublists...");
			createSublist();
			merge();
		}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long endTime = new Date().getTime();
			
			
			System.out.println("Time taken :"+((endTime-beginTime)/60000.0));
			
	}
	
	public static void createSublist()
	{
	String line="";
	try {
		long freemem = Runtime.getRuntime().freeMemory();
		Scanner buff = new Scanner(new FileReader("C:\\Sandeep\\Workspaces\\ProjectWorkSpace\\LabMergeSortEx\\src\\input2.txt"));
		int cnt=0;
		int size=0;
		int subListNum=0;
		//line = buff.readLine();
	
		StringTokenizer st = new StringTokenizer(line);
		List<Integer> loc_list;
		while(size!=totalRecords)
		{
			//List<List> tab = new ArrayList<>();
			loc_list = new ArrayList<>();
			/*while(cnt!=static final IntegermemoryRecords )
			{*/
				/*if(!st.hasMoreElements()) {
				 
				//StringTokenizer st = new StringTokenizer(line);
				}*/
	            while( cnt!=memoryRecords)
	            {
	            	int i =-1;
	            	if(buff.hasNextInt())
	            	 i=(int) buff.nextInt();
	            	else break;
	            	if(i!=-1) {
	                loc_list.add((i));
	                 cnt++;
	            	}
	            }
				//String tokens[] = line.split(",");
				
				//List tuple = new ArrayList<>();
				
				// for (int i = 0; i <tokens.length; i++) {
				//        tuple.add(tokens[i]);
				 //   } 
				 
				 
				/*if(!st.hasMoreElements()) {
					line = buff.readLine();
					if(line!=null)
					st = new StringTokenizer(line);
				}
			}
			*/
			
			Collections.sort(loc_list);
			
			 freemem = Runtime.getRuntime().freeMemory();
			createFile("Output"+subListNum+".txt");
			writeToFile("Output"+subListNum+".txt", loc_list);
			loc_list.clear();
			size = size+cnt;
			cnt=0;
			subListNum++;
			/*if(line==null)
			{
				if((line = buff.readLine())!=null)
					 st = new StringTokenizer(line);
			}
			
			
			if(line==null)
			{
				break;
			}*/
		}
					
		buff.close();
					
	} catch (IOException e) {
		// TODO Auto-generated catch block
		//Driver.myExit("Error Opening File");
		e.printStackTrace();
	}     
}

	public static void merge() throws Exception
	{
		try {
			
		
			Vector<ReadFile> readF = new  Vector<>();
			int i=0;
			
			createFile(outputFile);
			long freemem=0;
			if(numSubList==1) //only single output file
			{
				File oldName = new File("Output"+"0.txt");
			    File newName = new File(outputFile);
			    oldName.renameTo(newName);
			    return;
			}
			
			for (int passCount = 1; passCount <= numOfPasses; passCount++)
			{
				for(i=0;i<numSubList;i++)
				{
					try {
						ReadFile rd = new ReadFile();
						  freemem = Runtime.getRuntime().freeMemory();
						  //System.out.println(freemem);
						Scanner buff = new Scanner(new FileReader("Output"+i+".txt"));
						RandomAccessFile accessFile = new RandomAccessFile("Output"+i+".txt", "r");
						
						rd.buff = buff;
						//rd.tab = readFileToList(buff);	
						
						readF.add(rd);
						/*if(rd.tab==null)
						{
							rd.buff.close();
							rd.buff=null;
						}
						*/	
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(freemem);
						e.printStackTrace();
					}
				}
				int numGroups = (int) Math.ceil((double) numSubList / (double) numInputBuffers);
				for (int groupCount = 0; groupCount < numGroups; groupCount++)
				{
					int startList = groupCount * numInputBuffers;		// starting list of group in file
					int endList   = startList + (numInputBuffers - 1);
					ReadFile rd = new ReadFile();
					List<Integer> a = new ArrayList<>();
					for (int sortedList = startList; sortedList <= endList; sortedList++)
					{
						for(int k=0;k<numSubList;k++) {
							
						for(int u=0;u<blockSize;u++) {
						//	a = new ArrayList<>();
							if(readF.get(k)!=null)
								try {
									if(readF.get(k).buff.hasNext())
										readF.get(k).tab.add(readF.get(k).buff.nextInt());
								}catch (Exception e) {
									System.out.println(Runtime.getRuntime().freeMemory());
									e.printStackTrace();
								}
							
						}
					//	readF.get(k).tab=a;
						//a.clear();
					}
				}
			}
			//open buffered readers
				
		/*	for(i=0;i<numSubList;i++)
			{
				try {
					ReadFile rd = new ReadFile();
				//	  freemem = Runtime.getRuntime().freeMemory();
				//	  System.out.println(freemem);
					BufferedReader buff = new BufferedReader(new FileReader(Output+i+".txt"));
					rd.buff = buff;
					rd.tab = readFileToList(buff);	
					
					readF.add(rd);
					if(rd.tab==null)
					{
						rd.buff.close();
						rd.buff=null;
					}
						
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(freemem);
					e.printStackTrace();
				}
			}*/
			
			int minmax=0;
			List<Integer> output = new ArrayList<>(); 
			while(true)
			{
				List<Integer> tempTab = new ArrayList<>(); 
				int cnt = 0;
				for(i=0;i<numSubList;i++)
				{
					if(readF.get(i)==null || readF.get(i).tab==null)
					{
						tempTab.add(null);
						cnt++;
						continue;
					}
					
					if(readF.get(i).tab.size()==0)
					{
						//readF.get(i).tab = readFileToList(readF.get(i).buff);	
						
						for(int u=0;u<blockSize;u++) {
							if(readF.get(i).buff!=null && readF.get(i).buff.hasNext())
							readF.get(i).tab.add(readF.get(i).buff.nextInt());
						}
						if(readF.get(i).tab==null || readF.get(i).tab.isEmpty()) //file read is completed
						{
							if(readF.get(i).buff!=null )
							readF.get(i).buff.close();
							readF.get(i).buff=null;
							tempTab.add(null);
							cnt++;
							
						}else
						{
							tempTab.add(readF.get(i).tab.get(0));
						}
					}
					else
					{
						tempTab.add(readF.get(i).tab.get(0));
					}
				}
				
				if(cnt == numSubList) // all buffers have become null
				{
					writeToOutFile(outputFile, output);
					break;
				}
					
				
					minmax = getMin(tempTab);
				
				
				
				output.add(readF.get(minmax).tab.get(0));
				readF.get(minmax).tab.remove(0);
				
				if(output.size()==blockSize)
				{
					writeToOutFile(outputFile, output);
					output.clear();
					output = new ArrayList<>();
				}
				
				
				
			}
			
			//deleteAllOutputFiles();
			}
		}catch (Exception e) {
			System.out.println(Runtime.getRuntime().freeMemory());
			System.out.println("EXCEPTUION******************");
		}		
	}
	
	public static int getMin(List<Integer> tab)
	{/*
		
		int i=0;
		int min=0;
		while(i<meta.numSubList && tab.get(i)==null)
		{
			i++;
		}
		min=i;
		
		for(i=min+1;i<meta.numSubList;i++)
		{
			if(tab.get(i)!=null)
			{
				int cmp = new Asc_Comparator().compare(tab.get(min),tab.get(i));
				if(cmp>0)
				{
					min = i;
				}
			}
			
		}
		
		return min;
	*/
		List<Integer> sort = new ArrayList<>();
		int minIndex = 0;
		for(int i=0;i<tab.size();i++) {
			if(tab.get(i)!=null) {
				sort.add(tab.get(i));
			}
		}
		if(tab!=null) {
		 minIndex = tab.indexOf(Collections.min(sort));
		}
		return minIndex;
	}
	public static void writeToFile(String fileName, List<Integer> tab )
	{
		PrintWriter pw = null;
		
		for(int i = 0;i<1;i++)
		{
			StringBuilder sb = new StringBuilder();
		//	List tuple = tab.get(i);
	        sb.append(tab.get(0).toString());
	        sb.append(" ");
	        
			for(int j=1;j<tab.size();j++)
			{
				sb.append("\n");
				sb.append(tab.get(j).toString());
			}
			
			//if(i!=tab.size()-1)
				sb.append("\n");
				
			
			try {
				
				pw = new PrintWriter(new FileWriter(fileName,true));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        pw.write(sb.toString());
	        sb.delete(0	, sb.length());
	        pw.close();
			
		}
		
		
	
			
	}
	
	public static void writeToOutFile(String fileName, List<Integer> tab )
	{
		
		
		//Vector <Integer>outCols = outCols;
		Integer index = 0;
		PrintWriter pw = null;
		for(int i = 0;i<tab.size();i++)
		{
			StringBuilder sb = new StringBuilder();
			List<Integer> tuple = tab;
	       
			//index = outCols.get(0);
			sb.append(tuple.get(i).toString());
			sb.append(" ");
			
			/*for(int j=1;j<outCols.size();j++)
			{
				index = outCols.get(j);
				sb.append(",");
				sb.append(tuple.get(index).toString());
			}*/
			
			
			//if(i!=tab.size()-1)
				sb.append("\n");
				
				try {
					
					pw = new PrintWriter(new FileWriter(fileName,true));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		        pw.write(sb.toString());
		        pw.close();
			
		}
		
		
		
		
			
	}
	
	
	public static void createFile(String fileName)
	{
		PrintWriter pw = null;
		
		try {
			
			pw = new PrintWriter(new FileWriter(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}

        pw.close();
	}

	
}


 class ReadFile {

	public Scanner buff;
	public List<Integer> tab =new ArrayList<>();
	public ReadFile(Scanner buff, List<Integer> tab) {
		super();
		this.buff = buff;
		this.tab = tab;
	}
	public ReadFile() {
		super();
	}
}


