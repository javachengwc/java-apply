package com.pseudocode.lang;

import sun.nio.ch.Interruptible;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.security.util.SecurityConstants;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.LockSupport;

public class Thread1  implements Runnable {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    private volatile String name;
    private int            priority;
    private Thread         threadQ;
    private long           eetop;

    private boolean     single_step;

    private boolean     daemon = false;

    /* JVM state */
    private boolean     stillborn = false;

    //实际的运行runnable
    private Runnable target;

    private ThreadGroup group;

    private ClassLoader contextClassLoader;

    private AccessControlContext inheritedAccessControlContext;

    private static int threadInitNumber;
    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }

    //线程内部map
    ThreadLocal1.ThreadLocalMap threadLocals = null;

    ThreadLocal1.ThreadLocalMap inheritableThreadLocals = null;

    private long stackSize;

    private long nativeParkEventPointer;

    //线程Id
    private long tid;

    private static long threadSeqNumber;

    private volatile int threadStatus = 0;

    private static synchronized long nextThreadID() {
        return ++threadSeqNumber;
    }

    volatile Object parkBlocker;

    private volatile Interruptible blocker;
    private final Object blockerLock = new Object();

    void blockedOn(Interruptible b) {
        synchronized (blockerLock) {
            blocker = b;
        }
    }

    public final static int MIN_PRIORITY = 1;

    //默认情况下，线程的优先级为5，因为main线程的优先级为5，
    //而main为所有线程的父进程，因此默认情况下线程的优先级也是5。
    public final static int NORM_PRIORITY = 5;

    public final static int MAX_PRIORITY = 10;

    public static native Thread1 currentThread();

    //线程让步,它让掉当前线程CPU的时间片，使当前线程由运行中变成就绪状态，并重新竞争CPU的调度权。它可能会获取到，也有可能被其他线程获取到。
    //如果是在同步场景下，yield线程如已经持有锁，不会释放锁资源
    public static native void yield();

    //线程休眠,它会暂停当前线程，把cpu资源让出给其他线程，到期自动苏醒，并返回到可运行状态（就绪）。
    //如果是在同步场景下，sleep线程如已经持有锁，则不会释放锁资源
    public static native void sleep(long millis) throws InterruptedException;

    public static void sleep(long millis, int nanos) throws InterruptedException {
        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException( "nanosecond timeout value out of range");
        }

        if (nanos >= 500000 || (nanos != 0 && millis == 0)) {
            millis++;
        }

        sleep(millis);
    }

    private void init(ThreadGroup g, Runnable target, String name,long stackSize) {
        init(g, target, name, stackSize, null, true);
    }

    private void init(ThreadGroup g, Runnable target, String name,
                      long stackSize, AccessControlContext acc,
                      boolean inheritThreadLocals) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        this.name = name;

        Thread1 parent = currentThread();
        SecurityManager security = System.getSecurityManager();
        if (g == null) {
            if (security != null) {
                g = security.getThreadGroup();
            }

            if (g == null) {
                g = parent.getThreadGroup();
            }
        }

        g.checkAccess();

        if (security != null) {
            if (isCCLOverridden(getClass())) {
                security.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
            }
        }

        //g.addUnstarted();

        this.group = g;
        this.daemon = parent.isDaemon();
        this.priority = parent.getPriority();
        if (security == null || isCCLOverridden(parent.getClass()))
            this.contextClassLoader = parent.getContextClassLoader();
        else
            this.contextClassLoader = parent.contextClassLoader;
        this.inheritedAccessControlContext =
                acc != null ? acc : AccessController.getContext();
        this.target = target;
        setPriority(priority);
        if (inheritThreadLocals && parent.inheritableThreadLocals != null)
            this.inheritableThreadLocals = ThreadLocal1.createInheritedMap(parent.inheritableThreadLocals);
        this.stackSize = stackSize;
        tid = nextThreadID();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Thread1() {
        init(null, null, "Thread-" + nextThreadNum(), 0);
    }

    public Thread1(Runnable target) {
        init(null, target, "Thread-" + nextThreadNum(), 0);
    }

    Thread1(Runnable target, AccessControlContext acc) {
        init(null, target, "Thread-" + nextThreadNum(), 0, acc, false);
    }

    public Thread1(ThreadGroup group, Runnable target) {
        init(group, target, "Thread-" + nextThreadNum(), 0);
    }

    public Thread1(String name) {
        init(null, null, name, 0);
    }

    public Thread1(ThreadGroup group, String name) {
        init(group, null, name, 0);
    }

    public Thread1(Runnable target, String name) {
        init(null, target, name, 0);
    }

    public Thread1(ThreadGroup group, Runnable target, String name) {
        init(group, target, name, 0);
    }

    public Thread1(ThreadGroup group, Runnable target, String name,
                  long stackSize) {
        init(group, target, name, stackSize);
    }

    //启动线程
    public synchronized void start() {
        if (threadStatus != 0)
            throw new IllegalThreadStateException();
        //group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    //group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
            }
        }
    }

    private native void start0();

    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }

    private void exit() {
        if (group != null) {
            //group.threadTerminated(this);
            group = null;
        }
        /* Aggressively null out all reference fields: see bug 4006245 */
        target = null;
        /* Speed the release of some of these resources */
        threadLocals = null;
        inheritableThreadLocals = null;
        inheritedAccessControlContext = null;
        blocker = null;
        uncaughtExceptionHandler = null;
    }

    //关闭线程，是个过时方法，存在的问题是有可能不会释放monitor的锁，不建议使用该方法关闭线程。
    @Deprecated
    public final void stop() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            checkAccess();
            if (this != Thread1.currentThread()) {
                security.checkPermission(SecurityConstants.STOP_THREAD_PERMISSION);
            }
        }
        // A zero status value corresponds to "NEW", it can't change to
        // not-NEW because we hold the lock.
        if (threadStatus != 0) {
            resume(); // Wake up thread if it was suspended; no-op otherwise
        }

        // The VM can handle all thread states
        stop0(new ThreadDeath());
    }

    //调用interrupt()可以打断阻塞，打断阻塞并不等于线程的生命周期结束，仅仅是打断了线程的阻塞状态。
    //一旦在阻塞状态下被打断，就会抛出一个InterruptedException的异常，这个异常就像一个信号一样通知线程被打断了
    //让线程进入阻塞状态的方法有:Object.wait(),Thread.sleep(),Thread.join(),Selector.wakeup(),可调用interrupt()方法打断阻塞
    public void interrupt() {
        if (this != Thread1.currentThread())
            checkAccess();

        synchronized (blockerLock) {
            Interruptible b = blocker;
            if (b != null) {
                interrupt0();           // Just to set the interrupt flag
               // b.interrupt(this);
                return;
            }
        }
        interrupt0();
    }

    //当前线程是否中断，并将清除线程的中断状态。也就是说，当连续调用两次该方法，如果是中断线程，
    //第一次调用会返回true，并清除中断状态，第二次调用时由于中断状态已经被清除，将返回false。
    //注意与isInterrupted()方法的区别
    public static boolean interrupted() {
        return currentThread().isInterrupted(true);
    }

    //线程是否被中断,仅仅查询中断状态来判断是否发生中断并返回true或者false。
    public boolean isInterrupted() {
        return isInterrupted(false);
    }

    private native boolean isInterrupted(boolean ClearInterrupted);

    @Deprecated
    public void destroy() {
        throw new NoSuchMethodError();
    }

    //判断线程是否处于活动状态；活动状态是线程已经启动尚未终止，那么线程就是存活的，返回true，否则则返回false
    public final native boolean isAlive();

    @Deprecated
    public final void suspend() {
        checkAccess();
        suspend0();
    }

    @Deprecated
    public final void resume() {
        checkAccess();
        resume0();
    }

    public final void setPriority(int newPriority) {
        ThreadGroup g;
        checkAccess();
        if (newPriority > MAX_PRIORITY || newPriority < MIN_PRIORITY) {
            throw new IllegalArgumentException();
        }
        if((g = getThreadGroup()) != null) {
            if (newPriority > g.getMaxPriority()) {
                newPriority = g.getMaxPriority();
            }
            setPriority0(priority = newPriority);
        }
    }

    public final int getPriority() {
        return priority;
    }

    public final synchronized void setName(String name) {
        checkAccess();
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        this.name = name;
        if (threadStatus != 0) {
            setNativeName(name);
        }
    }

    public final String getName() {
        return name;
    }

    public final ThreadGroup getThreadGroup() {
        return group;
    }

    public static int activeCount() {
        return currentThread().getThreadGroup().activeCount();
    }

    public static int enumerate(Thread tarray[]) {
        return currentThread().getThreadGroup().enumerate(tarray);
    }

    @Deprecated
    public native int countStackFrames();

    //是主线程等待子线程的终止。主线程的代码块中，如果碰到t.join()，此时主线程需要等待（阻塞），
    //等待子线程结束了(Waits for this thread to die.),才能继续执行t.join()之后的代码块。
    public final synchronized void join(long millis) throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }

    public final synchronized void join(long millis, int nanos)
            throws InterruptedException {

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                    "nanosecond timeout value out of range");
        }

        if (nanos >= 500000 || (nanos != 0 && millis == 0)) {
            millis++;
        }

        join(millis);
    }

    public final void join() throws InterruptedException {
        join(0);
    }

    public static void dumpStack() {
        new Exception("Stack trace").printStackTrace();
    }

    public final void setDaemon(boolean on) {
        checkAccess();
        if (isAlive()) {
            throw new IllegalThreadStateException();
        }
        daemon = on;
    }

    public final boolean isDaemon() {
        return daemon;
    }

    public final void checkAccess() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            //security.checkAccess(this);
        }
    }

    public String toString() {
        ThreadGroup group = getThreadGroup();
        if (group != null) {
            return "Thread[" + getName() + "," + getPriority() + "," + group.getName() + "]";
        } else {
            return "Thread[" + getName() + "," + getPriority() + "," + "" + "]";
        }
    }

    @CallerSensitive
    public ClassLoader getContextClassLoader() {
        if (contextClassLoader == null)
            return null;
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
          //  ClassLoader.checkClassLoaderPermission(contextClassLoader, Reflection.getCallerClass());
        }
        return contextClassLoader;
    }

    public void setContextClassLoader(ClassLoader cl) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("setContextClassLoader"));
        }
        contextClassLoader = cl;
    }

    public static native boolean holdsLock(Object obj);

    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];

    public StackTraceElement[] getStackTrace() {
        if (this != Thread1.currentThread()) {
            // check for getStackTrace permission
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                security.checkPermission(
                        SecurityConstants.GET_STACK_TRACE_PERMISSION);
            }
            // optimization so we do not call into the vm for threads that
            // have not yet started or have terminated
            if (!isAlive()) {
                return EMPTY_STACK_TRACE;
            }
            StackTraceElement[][] stackTraceArray = dumpThreads(new Thread1[] {this});
            StackTraceElement[] stackTrace = stackTraceArray[0];
            // a thread that was alive during the previous isAlive call may have
            // since terminated, therefore not having a stacktrace.
            if (stackTrace == null) {
                stackTrace = EMPTY_STACK_TRACE;
            }
            return stackTrace;
        } else {
            // Don't need JVM help for current thread
            return (new Exception()).getStackTrace();
        }
    }

    public static Map<Thread1, StackTraceElement[]> getAllStackTraces() {
        // check for getStackTrace permission
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(SecurityConstants.GET_STACK_TRACE_PERMISSION);
            security.checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
        }

        Thread1[] threads = getThreads();
        StackTraceElement[][] traces = dumpThreads(threads);
        Map<Thread1, StackTraceElement[]> m = new HashMap<>(threads.length);
        for (int i = 0; i < threads.length; i++) {
            StackTraceElement[] stackTrace = traces[i];
            if (stackTrace != null) {
                m.put(threads[i], stackTrace);
            }
            // else terminated so we don't put it in the map
        }
        return m;
    }

    private static final RuntimePermission SUBCLASS_IMPLEMENTATION_PERMISSION =
            new RuntimePermission("enableContextClassLoaderOverride");

    private static class Caches {

        static final ConcurrentMap<Thread1.WeakClassKey,Boolean> subclassAudits = new ConcurrentHashMap<>();

        static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue<>();
    }

    private static boolean isCCLOverridden(Class<?> cl) {
        if (cl == Thread.class)
            return false;

        processQueue(Thread1.Caches.subclassAuditsQueue, Thread1.Caches.subclassAudits);
        Thread1.WeakClassKey key = new Thread1.WeakClassKey(cl, Thread1.Caches.subclassAuditsQueue);
        Boolean result = Thread1.Caches.subclassAudits.get(key);
        if (result == null) {
            result = Boolean.valueOf(auditSubclass(cl));
            Thread1.Caches.subclassAudits.putIfAbsent(key, result);
        }

        return result.booleanValue();
    }

    private static boolean auditSubclass(final Class<?> subcl) {
        Boolean result = AccessController.doPrivileged(
                new PrivilegedAction<Boolean>() {
                    public Boolean run() {
                        for (Class<?> cl = subcl;
                             cl != Thread.class;
                             cl = cl.getSuperclass())
                        {
                            try {
                                cl.getDeclaredMethod("getContextClassLoader", new Class<?>[0]);
                                return Boolean.TRUE;
                            } catch (NoSuchMethodException ex) {
                            }
                            try {
                                Class<?>[] params = {ClassLoader.class};
                                cl.getDeclaredMethod("setContextClassLoader", params);
                                return Boolean.TRUE;
                            } catch (NoSuchMethodException ex) {
                            }
                        }
                        return Boolean.FALSE;
                    }
                }
        );
        return result.booleanValue();
    }

    private native static StackTraceElement[][] dumpThreads(Thread1[] threads);
    private native static Thread1[] getThreads();

    public long getId() {
        return tid;
    }

    public enum State {
        NEW,
        RUNNABLE,
        BLOCKED,
        WAITING,
        TIMED_WAITING,
        TERMINATED;
    }

    public Thread.State getState() {
        // get current thread state
        return sun.misc.VM.toThreadState(threadStatus);
    }

    @FunctionalInterface
    public interface UncaughtExceptionHandler {
        void uncaughtException(Thread1 t, Throwable e);
    }

    // null unless explicitly set
    private volatile Thread1.UncaughtExceptionHandler uncaughtExceptionHandler;

    // null unless explicitly set
    private static volatile Thread1.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    public static void setDefaultUncaughtExceptionHandler(Thread1.UncaughtExceptionHandler eh) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("setDefaultUncaughtExceptionHandler"));
        }
        defaultUncaughtExceptionHandler = eh;
    }

    public static Thread1.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler(){
        return defaultUncaughtExceptionHandler;
    }

    public Thread1.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        //return uncaughtExceptionHandler != null ? uncaughtExceptionHandler : group;
        return uncaughtExceptionHandler != null ? uncaughtExceptionHandler : null;
    }

    public void setUncaughtExceptionHandler(Thread1.UncaughtExceptionHandler eh) {
        checkAccess();
        uncaughtExceptionHandler = eh;
    }

    private void dispatchUncaughtException(Throwable e) {
        getUncaughtExceptionHandler().uncaughtException(this, e);
    }

    static void processQueue(ReferenceQueue<Class<?>> queue,
                             ConcurrentMap<? extends
                             WeakReference<Class<?>>, ?> map)
    {
        Reference<? extends Class<?>> ref;
        while((ref = queue.poll()) != null) {
            map.remove(ref);
        }
    }

    static class WeakClassKey extends WeakReference<Class<?>> {

        private final int hash;

        WeakClassKey(Class<?> cl, ReferenceQueue<Class<?>> refQueue) {
            super(cl, refQueue);
            hash = System.identityHashCode(cl);
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;

            if (obj instanceof Thread1.WeakClassKey) {
                Object referent = get();
                return (referent != null) &&
                        (referent == ((Thread1.WeakClassKey) obj).get());
            } else {
                return false;
            }
        }
    }

    @sun.misc.Contended("tlr")
    long threadLocalRandomSeed;

    @sun.misc.Contended("tlr")
    int threadLocalRandomProbe;

    @sun.misc.Contended("tlr")
    int threadLocalRandomSecondarySeed;

    /* Some private helper methods */
    private native void setPriority0(int newPriority);
    private native void stop0(Object o);
    private native void suspend0();
    private native void resume0();
    private native void interrupt0();
    private native void setNativeName(String name);
}
