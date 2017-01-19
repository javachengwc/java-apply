package com.designptn;

import com.util.ThreadUtil;

/**
 * 单例模式
 * java的字节码指令重排序
 * 在计算机中，软件系统与硬件系统的一个共同目标，在不改变程序运行结果的前提下，尽可能地提高并行度。编译器、处理器也遵循这样一个目标。
 * 比如下面的计算语句：
 * double r = 1.1d; //(1)
 * double pi = 3.1415; //(2)
 * double area = pi * r * r; //(3)
 * area的计算依赖于r与pi两个变量的赋值指令。而r与pi无依赖关系。
 * 计算顺序(1)(2)(3)与(2)(1)(3) 对于r、pi、area变量的结果并无区别。
 * 编译器、Runtime在优化时可以根据情况重排序（1）与（2），而丝毫不影响程序的结果。
 * 这样造成的幻觉就是，单线程程序都是线性执行的，程序员无需担心重排序干扰代码的逻辑，也不需担心内存的可见性。
 * 指令重排序优化会影响初始化对象吗
 * 看起来inst=new Singleton();是一条赋值语句，事实上，它并不是一个原子操作。它大概会做日如下事情：
 * 1、栈内存开辟空间给inst引用
 * 2、堆内存开辟空间准备初始化对象
 * 3、初始化对象
 * 4、栈中引用指向这个堆内存空间地址
 * 这里并没有细化到指令的级别，但仍然可以分析出三个操作的依赖性： 3依赖于2，4依赖于2。3与4是独立无依赖的，是可以被优化重排序的。
 * 指令重排之后可能会是1、2、4、3；这样重排之后对单个线程来说效果是一样的，所以JVM认为是合法的重排序，
 * 但是在多线程环境下就会出问题，这里到4的时候inst已经指向了一块堆内存地址!=null，但这块堆内存还没初始化就直接返回了。
 */
public class Singleton {

    //volatile保证内存可见性，防止指令重排序,
    //保证可见性：使用该变量必须重新去主内存读取，修改了该变量必须立刻刷新主内存。
    //防止指令重排序：通过插入内存屏障。这里用到的就是防止指令重排序的性质
    //加入volatile之后查看汇编代码可以发现多了一句 lock addl $0x0,(%esp),相当于一个内存屏障
    private static volatile Singleton inst =null;

    private Integer d;

    private Integer k;

    private Singleton()
    {
        System.out.println("Singleton construct begin ............");
        k=3;
        ThreadUtil.sleep(5*1000l);
        d=2;
        System.out.println("Singleton construct end ............");
    }

    public static Singleton getInstance()
    {
        if(inst==null)
        {
            System.out.println(Thread.currentThread().getName()+ " Singleton inst null.......");
            synchronized(Singleton.class)
            {
                if(inst==null)
                {
                    //对象的初始化并非原子操作,从而留给了JVM重排序的机会。
                    //JVM的重排序也是有原则的，在单线程中，不管怎么排，保证最终结果一致。
                    //inst=new Singleton();这个操作可以拆成：
                    //  1、栈内存开辟空间给inst引用
                    //  2、堆内存开辟空间准备初始化对象
                    //  3、初始化对象
                    //  4、栈中引用指向这个堆内存空间地址
                    //指令重排之后可能会是1、2、4、3；这样重排之后对单个线程来说效果是一样的，所以JVM认为是合法的重排序，
                    //但是在多线程环境下就会出问题，这里到4的时候inst已经指向了一块堆内存地址!=null，但这块堆内存还没初始化就直接返回了
                    //为了防止指令重排序，inst声明成volatile对象引用即可。
                    inst=new Singleton();
                }
            }
        }else
        {
            System.out.println(Thread.currentThread().getName()+ " Singleton inst is not null.......");
        }
        return inst;
    }

    public Integer getD() {
        return d;
    }

    public static void main(String args [])
    {
        //100个线程
        for(int i=0;i<100;i++)
        {
            Thread t =new Thread()
            {
                public void run()
                {
                    Singleton s = Singleton.getInstance();
                    System.out.println(Thread.currentThread().getName()+ " Singleton d="+s.getD());
                }
            };
            t.start();
        }
    }

}
