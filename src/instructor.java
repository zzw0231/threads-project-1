import java.util.ArrayList;
import java.util.Random;
public class instructor extends Thread{
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
		}
	public int cap;
	public int id;
	public static boolean[] allend;		//flag control each thread is allow to dead or not
	public static int index=-1;
	public static boolean quitflag=false;		//flag to allow student to leave room
	public static ArrayList<student> s = new ArrayList<student>(); 
	instructor(int c,ArrayList<student> s){
		setName("instructor");
		cap=c;
		instructor.s=s;
		allend=new boolean[instructor.s.size()];
		for(int i=0;i<instructor.s.size();i++){
			allend[i]=false;
		}
	}
	public static boolean start=false;	//flag to tell student enter
	public static boolean end=false;	//flag to block student enter
	/*public static int setenter(int id){	//set index of how student entering the room		
		index++;	
		return index;
	}*/
	public static boolean getallend(int i){
		return allend[i];
	}
	public static boolean getquitflag(){
		return quitflag;
	}
	public static synchronized boolean getstart(){
		return start;
	}
	public static synchronized void setstart(){
		start=false;
		end=true;
	}
	public void gosleep(int n){
        try {  Thread.sleep(n); }
        catch (InterruptedException e) { /*e.printStackTrace(); */}
    }
	public void run() {
		Random rand=new Random();		
		for(int i=0;i<3;i++){
			//index=-1;
			gosleep(rand.nextInt(300));	
			quitflag=false;		
			start=true;
			msg("exam "+i+" is ready, student enter"+"\n");
			gosleep(5000);
			while(!end&&student.getremain()!=0){
				gosleep(50);
			}
			msg("exam "+i+" start"+"\n");
			gosleep(5000);
			start=false;
			student.setexam(i);		//tell the exam number
			msg("student are taking exam\n");
			msg("exam "+i+" end. "+"collecting exam\n");
			for(int j=0;j<cap;j++){		// let student out in the order how they enter the room.
				for(int z=0;z<s.size();z++){
				student temp=s.get(z);
				if(temp.getv()==j){
					 if( !(temp.isInterrupted()) ){
		                    temp.interrupt();
		                    msg(temp.getName()+" quit"+"\n");
		                    gosleep(100); 
		                }					 
				}
				}
			}
			student.setinx(0);
			msg("all student quit"+". take a short break then we start the next exam.\n");
			quitflag=true;
		}
		msg("all exam finished."+"\n");		//grades print out
		for(int i=0;i<s.size();i++){
			msg(""+s.get(i).getName()+" grades are: "+s.get(i).getscore(0)+" "+s.get(i).getscore(1)+" "+s.get(i).getscore(2)+"\n");
		}
		for(int i=s.size()-1;i>0;i--){		//threads join
			String sss;
			if(s.get(i).isAlive()){
				sss="alive";
			}
			else{
				sss="not alive";
			}
			msg(s.get(i).getName()+" is "+sss+"\n");
			if(s.get(i).isAlive()){		//if alive check i-1, if both alive, join
					if(s.get(i-1).isAlive()){
						sss="alive";
						msg(s.get(i-1).getName()+" is "+sss+"\n");
					}
					if(s.get(i-1).isAlive()){
					try {
						msg(s.get(i).getName()+" join with "+s.get(i-1).getName()+"\n");
						allend[i]=true;
						s.get(i).join();
						allend[i-1]=true;
						s.get(i-1).join();
	                
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						 e.printStackTrace();
					}	
					}
				}
			else if(!s.get(i-1).isAlive()){		//if i-1 not alive, leave by him self
					try {
						msg(s.get(i).getName()+" join with himself\n");
						s.get(i).join();	                
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						 e.printStackTrace();
					}	
				}
		}
		msg("all student has left school, program END.");
	}

}
