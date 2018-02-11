package test;

import java.util.ArrayList;
import java.util.List;

public class threadtest {
	public static void main(String args[]) {
		jiegou j = new jiegou();
		List<Thread> threads = new ArrayList<>(10);  
        for (int i = 0; i < 10; i++) {  
            Thread thread = new Thread(new t(j));  
            threads.add(thread);  
            thread.start();  
        }  
        while (true) {  
            if (allThreadTerminated(threads)) {// 所有线程运行结束  
                System.out.println(j.get());  
                break;  
            }  
        } 
	}
	private static boolean allThreadTerminated(List<Thread> threads) {  
        for (Thread thread : threads) {  
            if (thread.isAlive()) {  
                return false;  
            }  
        }  
        return true;  
    }  
}
class t implements Runnable{
	private jiegou j;
	public t(jiegou j){
		this.j = j;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0; 
		while(i<1000){
			j.cre();
			i++;
		}
	}
	
}
class jiegou{
	private int count = 0;
	public synchronized void cre(){
		count++;
	}
	public int get(){
		return count;
	}
}