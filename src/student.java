import java.util.Random;


public class student extends Thread{	
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
		}
	public int id;
	public int enterv=-1;	//the index record when the student enter the room
	public int examtake=0;	//how many exam that student has took yet;
	public boolean []exam={false,false,false}; //exam taken flag
	public int []examscore={0,0,0};	
	public static int nexam=0; //the index of exam
	//public static instructor teacher;
	public static int cap;	//capacity
	public static int remain;
	public static int inx=0;
	public static int studentintheroom=0;	//number of student in the room
	public static void setexam(int i){		
		nexam=i;
	}
	public static int getremain(){
		return remain;
	}
	public static void setinx(int i){
		inx=i;
	}
	public int getscore(int i){
		return examscore[i];
	}
	public void setv(int i){	//enter index setting function
		enterv=i;		
	}
	public int getv(){			//return index
		return enterv;		
	}
	public int getid(){
		return id;
	}
	student(int id,int c,int student){
		this.id=id;
		cap=c;
		setName("student-"+id);
		remain=student;
	}
	public synchronized void enter(){	//increase when enter
		studentintheroom++;
		//enterv=instructor.setenter(id);
		enterv=inx;
		inx++;
		msg(getName()+" entering"+"\n");
	}
	public synchronized void quit(){	//decrease when quit
		studentintheroom--;
		enterv=-1;
	}
	public synchronized void examplus(){	
		examtake++;
	}
	public static synchronized void remain_(){	
		remain--;		
	}
	public void run() {
		msg("I'm in the school\n");
		while(nexam<3&&examtake!=2){	//loop if 3 exam not finished and have not take 2 exam yet
			enterv=-1;
			gosleep(randomint(500,1000));
			while(!(instructor.getstart())||studentintheroom>=cap){	//busy wait if instructor not arrive yet
				gosleep(50);
				while(studentintheroom>=cap){	//busy wait if room is full
					yield();
					yield();
				}
			}
			this.setPriority(this.getPriority()+1);
			//enterv=studentintheroom;
			enter();
			examplus();	
			if(studentintheroom==cap){	//tell instructor class room is full
				instructor.setstart();				
			}
			gosleep(50000);
			quit();
			this.setPriority(this.getPriority()-2);
			examscore[nexam]=randomint(10,100);		//random grade
			while(instructor.getquitflag()){	//stay until all student left room
				gosleep(50);
			}
		}
		remain_();	
		while(!instructor.getallend(id)){	//not allow to leave unless instructor says so
			gosleep(50);
		}
	}
	public void gosleep(int n){		//small function to avoid every try catch of sleep method
        try {  Thread.sleep(n); }
        catch (InterruptedException e) { /*e.printStackTrace();*/}
    }
	public static int randomint(int min, int max) {		//random integer function
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
        }
}