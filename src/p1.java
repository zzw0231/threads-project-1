import java.util.ArrayList;


public class p1 {
	public static long time = System.currentTimeMillis();
	public static int nstudent = 14;      
    public static int roomcap = 10;    
    public static ArrayList<student> s = new ArrayList<student>(); 
	public static void main(String[] args) {
		if (args.length == 2) {
			nstudent = Integer.parseInt(args[0]);  //number of students 
			roomcap  = Integer.parseInt(args[1]);  //room capacity
        }
        else        // If no arguments are provided (or the number of arguments is wrong) , then the program uses default values
            System.out.println("Wrong number of Arguments! (Will use default values)"+"\n"+"format: (number of student) (room capacity)"+"\n"+"example:10 10");
		for(int i=0;i<nstudent;i++){				//create and start the threads
			student si=new student(i,roomcap,nstudent);
			si.start();
			s.add(si);
			smallPause(600);
		}
		instructor ins=new instructor(roomcap,s);	//pass Arraylist of threads to instructor class
		ins.start();
	}
	public static void smallPause(int n){			//pause function
        try {  Thread.sleep(n); }
        catch (InterruptedException e) { e.printStackTrace();  }
    }

}
