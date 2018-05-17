package test;
//多生产者 多消费者
//if判断标记只有一次，会导致不该运行的线程运行
//whlie解决了线程获取执行权后，是否要运行
//while+notify()会导致死锁 notifyAll解决
class Resource
{
	private String name;
	private int count=1;
	private boolean flag=false;
	public synchronized void set(String name)
	{
		while(flag)
			try{this.wait();}catch(InterruptedException e){}
		this.name=name+count;
		count++;
		System.out.println(Thread.currentThread().getId()+"...生产者。。。"+this.name);
		flag=true;
		notifyAll();
	}
	public synchronized void out()
	{
		while(!flag)
			try{this.wait();}catch(InterruptedException e){}
		System.out.println(Thread.currentThread().getId()+"...消费者。。。"+this.name);
		flag=false;
		notifyAll();
	}
}
class Producer implements Runnable
{
	private Resource r;
	Producer(Resource r)
	{
		this.r=r;
	}
	public void run()
	{
		while(true)
		{
			r.set("烤鸭");
		}
	}
}
class Consumer implements Runnable
{
	private Resource r;
	Consumer(Resource r)
	{
		this.r=r;
	}
	public void run()
	{
		while(true)
		{
			r.out();
		}
	}
}
public class ProconDemo
{
	public static void main(String[] args)
	{
		Resource  r=new Resource();
		Producer pro=new Producer(r);
		Consumer con=new Consumer(r);
		Thread t0=new Thread(pro);
		Thread t1=new Thread(pro);
		Thread t2=new Thread(con);
		Thread t3=new Thread(con);
		t0.start();
		t1.start();
		t2.start();
		t3.start();
	}
}