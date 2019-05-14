import java.util.Arrays;
import java.util.Random;

public class DiskScheduling {
/*	Write a program that implements the following disk-scheduling algorithms:
		a. FCFS
		b. SSTF
		c. SCAN
		d. C-SCAN
		e. LOOK
		f. C-LOOK
		Your program will service a disk with 5,000 cylinders numbered 0 to
		4,999. The program will generate a random series of 1,000 cylinder
		requests and service them according to each of the algorithms listed
		above. The program will be passed the initial position of the disk head
		(as a parameter on the command line) and report the total amount of
		head movement required by each algorithm.
		*/
		private int Cylinders;
		private int seriesLength;
		private int[] Series;
		private int Starthead;
		private int Totalhead;
		private int randInt;
//		int[] testSeries =  {98, 183, 37, 122, 14, 124, 65, 67};
        public DiskScheduling() {						//Step 1) Create a series
        	Cylinders = 5000;
        	seriesLength = 1000;
        	FillSeries(); 										//Call the method to create the series.
        }
	    public int[] FillSeries() {						//Step 2) The Series is created randomly
	    	 Series = new int[seriesLength];
	    	 Random x =new Random();
	    	 for (int i = 0; i<seriesLength;i++) { 				// Series has a length of 1000.
	    		 randInt = x.nextInt(Cylinders+1); 				// 0 - 5000
    			 Series[i] = randInt;				//a random integer from 0 to 5000
	    	 }
	    	 
			return Series;
	    }
	    public int FCFS (int headDisk) {				//Step 3) Creating the FCFS method (First-Come-First-Serve)
	    	Totalhead = 0;										//The total amount of disk heads
	    	Starthead = headDisk;								//We start at the given headDisk
	    	for (int i = 0; i< seriesLength;i++) {				
	    		Totalhead += Math.abs(Starthead - Series[i]);	//algorithm: |a - b| = +c
//	    		System.out.println(Starthead+" - "+Series[i]);	//info to help understand it a bit better
	    		Starthead = Series[i];							//make the new head the Series[i], i being 0,1,2,3,4,..,4999
	    	}
//	    	System.out.println(Totalhead);
			return Totalhead;
	    }
	    
	    public int SSTF (int headDisk) {
	    	Totalhead = 0;								//Step 4) SStF algorithm
	    	Starthead = headDisk;						
	    	int indexofHead;										//Going to add the headDisk to the array, index of headDisk
	    	int indexofbehind=0;									//The index before headDisk
	    	int indexoffront = 0;									//The index after headDisk
	    	int behindDiff=0;										//The difference between the before-headDisk
	    	int frontDiff=0;										//The Difference between the after-headDisk
    	    int[] tempArray= new int[seriesLength+1];				//Creating a temporary array with a length of 5001	(including headDisk)
	    	for (int i = 0; i< seriesLength;i++) {				
	    		tempArray[i] = Series[i];
	    	}
	    	tempArray[seriesLength] = headDisk;
    	    Arrays.sort(tempArray);									//First, we sort the tempArray
    		indexofHead = Arrays.binarySearch(tempArray, headDisk); //We find the index of headDisk
    		indexofbehind = indexofHead-1;							
    		indexoffront = indexofHead+1;
	    	for (int i = 0; i< tempArray.length-1;i++) {
	    		if(indexofbehind>=0 && indexoffront< tempArray.length) {
		    		behindDiff = Math.abs(Starthead - tempArray[indexofbehind]); 
		    		frontDiff =  Math.abs(Starthead - tempArray[indexoffront]);
		    		if(behindDiff > frontDiff) {			//find out if the difference of behind - HD > front - HD, do this:
		    			Totalhead+= frontDiff;
		    			indexofHead = indexoffront;
		    			Starthead = tempArray[indexoffront];
		    			indexoffront+=1;
		    		}
		    		else{									//Else if the difference of behind - HD < front - HD, do this:
		    			Totalhead+= behindDiff;
		    			indexofHead = indexofbehind;
		    			Starthead = tempArray[indexofbehind];
		    			indexofbehind-=1;
		    		}
	    		}
	    		else if(indexofbehind == -1){				//else if all the index before the original headDisk were requested already,
		    		frontDiff =  Math.abs(Starthead - tempArray[indexoffront]);			//Just get difference of cylinders in the front of the original headDisk
	    			Totalhead+= frontDiff;
	    			indexofHead = indexoffront;
	    			Starthead = tempArray[indexoffront];
	    			indexoffront+=1;
	    		}
	    		else if(indexoffront == tempArray.length){	//else if all the index after the original headDisk were requested already,
		    		behindDiff = Math.abs(Starthead - tempArray[indexofbehind]);		//Just get difference of cylinders in the back of the original headDisk
	     			Totalhead+= behindDiff;
	    			indexofHead = indexofbehind;
	    			Starthead = tempArray[indexofbehind];
	    			indexofbehind-=1;
	    		}
	    	}
//	    	System.out.println(Totalhead);
			return Totalhead;
	    }
	    public int SCAN (int headDisk) {			//Step 5) SCAN algorithm
	    	Totalhead = 0;
	    	Starthead = headDisk;
    	    int[] tempArray= new int[seriesLength+2];			//We need 0 && the headDisk in the array.
    	    int indexofHead;
    	    int tempx = 0;
	    	for (int i = 0; i< seriesLength;i++) {				//the temparray = original Series, 0 && headDisk
	    		tempArray[i] = Series[i];	
	    	}
	    	tempArray[seriesLength] = headDisk;
    	    tempArray[seriesLength+1]=0;
    	    Arrays.sort(tempArray);								//Sort the array from Least to greatest : 0,...,whatever the last series is
    		indexofHead = Arrays.binarySearch(tempArray, headDisk);
    		int low = indexofHead-1;							
    		int high = indexofHead+1;
    		for (int i = 0; i<tempArray.length-1;i++) {
    			if(low>-1) {
		    		Totalhead += Math.abs(Starthead - tempArray[low]);
//		    		System.out.println(Starthead+" - "+ tempArray[low]);
		    		Starthead = tempArray[low];
		    		low+=-1;
    			}
				else {
		    		Totalhead += Math.abs(tempArray[tempx] - tempArray[high]);
		    		tempx=high;
		    		high+=1;
				}
    		}
//    		System.out.println(low+" - "+ high);
//	    	System.out.println(Totalhead);
			return Totalhead;
	    }
	    public int CSCAN (int headDisk) {
	    	Totalhead = 0;
	    	Starthead = headDisk;
    	    int[] tempArray= new int[seriesLength+3];
    	    int indexofHead;
    	    int tempx = 0;
	    	for (int i = 0; i< seriesLength;i++) {
	    		tempArray[i] = Series[i];
	    	}
	    	tempArray[seriesLength] = headDisk;
    	    tempArray[seriesLength+1]=0;
    	    tempArray[seriesLength+2]=5000;
    	    Arrays.sort(tempArray);
    		indexofHead = Arrays.binarySearch(tempArray, headDisk);
    		int low = 0;
    		int high = indexofHead+1;
    		for (int i = 0; i<tempArray.length-2;i++) {
    			if(high<=tempArray.length-1) {
		    		Totalhead += Math.abs(Starthead - tempArray[high]);
//		    		System.out.println(Starthead+" - "+ tempArray[high]);
		    		Starthead = tempArray[high];
		    		high+=1;
    			}
				else {
					low+=1; 
		    		Totalhead += Math.abs(tempArray[tempx] - tempArray[low]);
//		    		System.out.println(tempArray[tempx]+" - "+ tempArray[low]);
		    		tempx=low;
				}
    		}
//	    	System.out.println(Totalhead);
			return Totalhead;
	    }
	    public int CLOOK (int headDisk) {
	    	Totalhead = 0;
	    	Starthead = headDisk;
    	    int[] tempArray= new int[seriesLength+1];
    	    int indexofHead;
    	    int tempx = 0;
	    	for (int i = 0; i< seriesLength;i++) {
	    		tempArray[i] = Series[i];
	    	}
	    	tempArray[seriesLength] = headDisk;
    	    Arrays.sort(tempArray);
    		indexofHead = Arrays.binarySearch(tempArray, headDisk);
    		int low = 0;
    		int high = indexofHead+1;
    		for (int i = 0; i<tempArray.length-2;i++) {
    			if(high<tempArray.length) {
		    		Totalhead += Math.abs(Starthead - tempArray[high]);
//		    		System.out.println(Starthead+" - "+ tempArray[high]);
		    		Starthead = tempArray[high];
		    		high+=1;
    			}
				else {
					low+=1; 
		    		Totalhead += Math.abs(tempArray[tempx] - tempArray[low]);
//		    		System.out.println(tempArray[tempx]+" - "+ tempArray[low]);
		    		tempx=low;
				}
    		}
//	    	System.out.println(Totalhead);
			return Totalhead;
	    }
	    public int LOOK (int headDisk) {
	    	Totalhead = 0;
	    	Starthead = headDisk;
    	    int[] tempArray= new int[seriesLength+1];
    	    int indexofHead;
    	    int tempx = 0;
	    	for (int i = 0; i< seriesLength;i++) {
	    		tempArray[i] = Series[i];
	    	}
	    	tempArray[seriesLength] = headDisk;
    	    Arrays.sort(tempArray);
    		indexofHead = Arrays.binarySearch(tempArray, headDisk);
    		int low = indexofHead-1;
    		int high = indexofHead+1;
    		for (int i = 0; i<tempArray.length-1;i++) {
    			if(high<tempArray.length) {
		    		Totalhead += Math.abs(Starthead - tempArray[high]);
//		    		System.out.println(Starthead+" - "+ tempArray[high]);
		    		Starthead = tempArray[high];
		    		tempx= high;
		    		high+=1;
    			}
				else {
		    		Totalhead += Math.abs(tempArray[tempx] - tempArray[low]);
//		    		System.out.println(tempArray[tempx]+" - "+ tempArray[low]);
		    		tempx=low;
					low-=1; 
				}
    		}
//	    	System.out.println(Totalhead);
			return Totalhead;
	    }
	    
	public static void main(String[] args) {
		
		DiskScheduling Algorithm = new DiskScheduling();
		System.out.println("The total amount of head movement required by FCFS is : "+Algorithm.FCFS(Integer.parseInt(args[0])) +"\n");
		System.out.println("The total amount of head movement required by SSTF is : "+Algorithm.SSTF(Integer.parseInt(args[0]))+"\n");
		System.out.println("The total amount of head movement required by SCAN is : "+Algorithm.SCAN(Integer.parseInt(args[0]))+"\n");
		System.out.println("The total amount of head movement required by CSCAN is : "+Algorithm.CSCAN(Integer.parseInt(args[0]))+"\n");
		System.out.println("The total amount of head movement required by CLOOK is : "+Algorithm.CLOOK(Integer.parseInt(args[0]))+"\n");
		System.out.println("The total amount of head movement required by LOOK is : "+Algorithm.LOOK(Integer.parseInt(args[0]))+"\n");
	}

}
