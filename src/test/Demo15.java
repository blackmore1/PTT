package test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
Condition,实现唤醒指定的部分线程
 */
public class Demo15 {

    static Lock lock = new ReentrantLock();
    static int count = 0;
    static Condition conditionA = lock.newCondition();// 必须配合lock.lock()使用
    static Condition conditionB = lock.newCondition();// 必须配合lock.lock()使用

    public static void main(String[] args) {
        Thread t1 = new Thread() {

            @Override
            public void run() {
                lock.lock();
                if (count < 5) {
                    System.out.println("线程1未达到业务要求,暂停中,等待线程2处理到达到要求后唤醒");
                    try {
                        conditionA.await();// 暂停线程并释放锁
                        System.out.println("conditionA被唤醒");
                        conditionB.await();
                        System.out.println("conditionB被唤醒");
                        System.out.println("我是线程1后面的代码");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                lock.unlock();
            }

        };

        Thread t2 = new Thread() {

            @Override
            public void run() {
                lock.lock();
                while (count < 10) {
                    count++;
                    System.out.println("线程2业务处理中: " + count);
                    try {
                        Thread.sleep(1000);
                        if (count == 5) {
                            conditionA.signal();
                            System.out.println("唤醒线程1");
                            lock.unlock();// 调用signal()方法后,线程2并不会释放锁,需要手动释放线程2才会执行
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    lock.lock();// 不加这个会报java.lang.IllegalMonitorStateException
                    System.out.println("等待3秒后conditionB会被唤醒");
                    Thread.sleep(3000);
                    conditionB.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();// 这里释放锁,线程2执行完,线程1才会执行
            }

        };

        t1.start();
        t2.start();

    }

}