import java.util.concurrent.Semaphore;
import java.util.Random;


//GROUP : SAQEB, BOBUR, MAX, TEO

public class SleepingTeachingAssistant {

	/*main method*/
    public static void main(String[] args) 
    {
        /*student count*/
        int amountOfStudents = 5;
        
        /*semaphore creation*/
        SignalSemaphore studentsWoke = new SignalSemaphore();
        
        /*chair creation*/
        Semaphore chairs = new Semaphore(3);
        
        /*semaphores available*/
        Semaphore semAvail = new Semaphore(1);
        
        
        /*amount of time students will be waiting*/
        Random studentWaitingTime = new Random();
        
        /* starting thread of students*/
        for (int i = 0; i < amountOfStudents; i++)
        {
            Thread studentThread = new Thread(new Student(studentWaitingTime.nextInt(60), studentsWoke, chairs, semAvail, i+1));
            studentThread.start();
        }
        
        /*starting of teacher assistant thread */
        Thread teachAssist = new Thread(new TeachingAssistant(studentsWoke, chairs, semAvail));
        teachAssist.start();
    }
}





/* implementation begins*/




/* This semaphore for students Woke up the teaching assistant.*/
class SignalSemaphore {
    private boolean flag = false;
    /* set boolean flag*/
    public synchronized void receive() {
        this.flag = true;
        this.notify();
    }
    /*waiting till flag changes*/
    public synchronized void release() throws InterruptedException{
        while(!this.flag) wait();
        this.flag = false;
    }
}

/*student will program and then alternate to receive help from teacher assistant*/
class Student implements Runnable
{       
	private SignalSemaphore studentsWoke;
    private int studentNum;
    private int programmmingTime;

    private Semaphore chairs;
    
    /* Mutual exclusion lock checking if teach assistant is available */ 
    private Semaphore semAvail;

   /* thread reference*/
    private Thread t;

    public Student(int devTime, SignalSemaphore ss, Semaphore ch, Semaphore sa, int sNum)
    {
    	studentNum = sNum;
    	chairs = ch;
    	semAvail = sa;
    	programmmingTime = devTime;    
        studentsWoke = ss;
        
        t = Thread.currentThread();
    }

    @Override
    public void run()
    {
        /*Infinite loop*/
        while(true)
        {
            try
            {
               System.out.println("Student " + studentNum + " has started programming for " + programmmingTime + " seconds.");
               /* give a finite time*/
               t.sleep(programmmingTime * 1000);
                
               /*verifying teacher assistant availability*/
               System.out.println("Student " + studentNum + " is checking to see if a Teacher's Assistant is available.");
               if (semAvail.tryAcquire())
               {
                   try
                   {
                       /* students woken by the teach assistant*/
                       studentsWoke.receive();
                       System.out.println("Student " + studentNum + " has woke up the Teacher's Assistant and has started working with the Teacher's Assistant.");
                       /* give a finite time*/
                       t.sleep(3000);
                       System.out.println("Student " + studentNum + " has stopped working with the Teacher's Assistant.");
                   }
                   catch (InterruptedException e)
                   {
                	   System.out.println("Error: " + e + " has occured.");
                       continue;
                       /* prints error*/
                   }
                   finally
                   {
                	   /* release*/
                	   semAvail.release();
                   }
               }
               else
               {
                   System.out.println("No Teacher's Assistant available for" + studentNum + ", will find a chair and sit down.");
                   if (chairs.tryAcquire())
                   {
                       try
                       {
                           
                           System.out.println("Student " + studentNum + " is wating outside the office and sitting on a chair."
                                   + "Student is number " + ((3 - chairs.availablePermits())) + " in line for Teacher's Assistant.");
                           /**/
                           semAvail.acquire();
                           System.out.println("Student " + studentNum + " has started working with the Teacher's Assistant.");
                           t.sleep(3000);
                           System.out.println("Student " + studentNum + " has stopped working with the Teacher's Assistant.");
                           semAvail.release();
                       }
                       catch (InterruptedException e)
                       {
                           System.out.println("Error: " + e + " has occured.");
                           continue;
                           /* prints error*/
                       }
                   }
                   else
                   {
                       System.out.println("Student " + studentNum + " was not able to see the Teacher's Assistant and was not able to sit, continue working.");
                   }
               }
            }
            catch (InterruptedException e)
            {
            	System.out.println("Error: " + e + " has occured.");
                break;
                /* prints error*/
            }
        }
    }
}

class TeachingAssistant implements Runnable
{
	
	  /* used for chair outside office */
    private Semaphore chairs;
    /* this is for waking up teacher assistant*/
    private SignalSemaphore studentsWoke;
    private Semaphore semAvail;

    private Thread t;
    
    public TeachingAssistant(SignalSemaphore ss, Semaphore ch, Semaphore sa)
    {
        t = Thread.currentThread();
        studentsWoke = ss;
        chairs = ch;
        semAvail = sa;
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                System.out.println("Amount of students exhausted, Teacher's Assistant will sleep.");
                studentsWoke.release();
                System.out.println("The Teacher's Assistant wakes up due to student.");
                
                t.sleep(3000);
                
                if (chairs.availablePermits() != 3)
                {
                    do
                    {
                        t.sleep(3000);
                        chairs.release();
                    }
                    while (chairs.availablePermits() != 3);                   
                }
            }
            catch (InterruptedException e)
            {
            	System.out.println("Error: " + e + " has occured.");
                continue;
                /* prints error*/
            }
        }
    }
}