# Java 并发与多线程

### Java 线程

* 实现方式

  * extends Thread

    * 不推荐覆写 start 方法

      这里的关键是本地方法 start0，它实现了启动线程、申请栈内存、运行 run 方法、修改线程状态等职责，线程管理和栈内存管理都是由 JVM 负责的，如果覆盖了 start 方法，也就是撤消了线程管理和栈内存管理的能力。

  * implements Runnable

    * 使用线程异常处理器提升系统可靠性

      Java 1.5 版本以后在 Thread 类中增加了 setDefaultUncaughtExceptionHandler 方法，实现了线程异常的捕获和处理。

      * 共享资源锁定

        如果线程异常产生的原因是资源被锁定，在此情况下最好的办法是停止所有的线程，释放资源。

      * 脏数据引起系统逻辑混乱

        异常的产生中断了正在执行的业务逻辑，特别是如果正在执行一个原子操作。

      * 内存溢出

        线程异常了，但由该线程创建的对象并不会马上回收，如果再重新启动新线程，再创建一批新对象，特别是加入了场景接管，就非常危险了。
      

  * implements Callable

    * Callable 与 Runnable 有两点不同

      * 可以通过 call() 获得返回值

        前两种方式都有一个共同的缺陷，即在任务执行完成后，无法直接获取执行结果，需要借助共享变量等获取，而 Callable 和 Future 则很好地解决了这个问题。

      * call() 可以抛出异常

        而 Runnable 只有通过 setDefaultUncaughtExceptionHandler() 的方式才能在主线程中捕捉到子线程异常。
      
    * 异步运算考虑使用Callbale接口
      
      实现Callable接口的类，只是表明它是一个可调用的任务，并不表示它具有多线程运算能力，还是需要执行器来执行的。
      
      ```java
      package thread;
      
      import java.util.concurrent.Callable;
      import java.util.concurrent.TimeUnit;
      
      public class TaxCalculator implements Callable<Integer> {
      
          private Integer seedMoney;
      
          public TaxCalculator(Integer seedMoney) {
              this.seedMoney = seedMoney;
          }
      
          @Override
          public Integer call() throws Exception {
              TimeUnit.MILLISECONDS.sleep(10_000);
              return seedMoney / 10;
          }
      
      }
      
      ```
      
      ```java
      package thread;
      
      import java.util.concurrent.*;
      
      public class TaxCalculatorDemo {
      
          public static void main(String[] args) throws InterruptedException, ExecutionException {
              // Executors 是一个静态工具类，提供了异步执行器的创建能力，一般它是异步计算的入口类
              ExecutorService es = Executors.newSingleThreadExecutor();
              Future<Integer> future = es.submit(new TaxCalculator(100));
              // Future 关注的是线程执行后的结果
              while (!future.isDone()) {
                  TimeUnit.MILLISECONDS.sleep(200);
                  System.out.print("#");
              }
              System.out.println("\n计算完成，税金是：" + future.get() + " 元");
              es.shutdown();
          }
      
      }
      
      ##################################################
      计算完成，税金是：10 元
      
      ```
      
      此类异步计算的好处是：
      
      * 尽可能多地占用系统资源，提供快速运算。
      
      * 可以监控线程执行的情况，比如是否执行完毕、是否有返回值、是否有异常等。
      
      * 可以为用户提供更好地支持，比如例子中的运算进度。

* 线程在生命周期内存在多种状态

  ![线程状态图](https://github.com/songor/interview/blob/master/picture/%E7%BA%BF%E7%A8%8B%E7%8A%B6%E6%80%81%E5%9B%BE.jpg)

  * 新建状态（NEW）

    是线程被创建且未启动的状态。

    * 线程优先级只使用三个等级

      线程的优先级（Priority）决定了线程获得 CPU 运行的机会，优先级越高获得的运行机会越大，优先级越低获得的机会越小。Java 的线程有 10 个级别（准确地说是 11 个级别，级别为 0 的线程是 JVM 的，应用程序不能设置该级别）。

      ```java
      package thread;
  
      public class ThreadPriority implements Runnable {

          public void start(int priority) {
              Thread t = new Thread(this);
              t.setPriority(priority);
              t.start();
          }
      
          @Override
          public void run() {
              for (int i = 0; i < 100000; i++) {
                  /**
                   * 该计算毫无意义，只是为了保证一个线程尽可能多地消耗 CPU 资源，目的是为了观察 CPU 繁忙时不同优先级线程的执行顺序。
                   * 需要说明的是，如果此处使用了 Thread.sleep() 方法，则不能体现出线程优先级的本质了，因为 CPU 并不繁忙，线程调度不会遵循优先级顺序来进行调度。
                   */
                  Math.hypot(Math.pow(924526789, i), Math.cos(i));
              }
              System.out.println("Priority: " + Thread.currentThread().getPriority());
          }
      
      }
      
      ```
      
      ```java
      package thread;
      
      /**
       * 线程优先级的一个重要表现：
       * 并不是严格遵照线程优先级别来执行的
       * 优先级差别越大，运行机会差别越明显
       */
      public class ThreadPriorityDemo {
      
          public static void main(String[] args) {
              for (int i = 0; i < 20; i++) {
                  new ThreadPriority().start(i % 10 + 1);
              }
          }
      
      }
      
      ```
      
      每个线程要运行，需要操作系统分配优先级和 CPU 资源，对于 Java 来说，JVM 调用操作系统的接口设置优先级，事实上，不同操作系统线程优先级是不相同的。Java 是跨平台的系统，需要把这 10 个优先级映射成不同操作系统的优先级，于是界定了 Java 的优先级只是代表抢占 CPU 的机会大小，优先级越高，抢占 CPU 的机会越大，被优先执行的可能性越高，优先级相差不大，则抢占 CPU 的机会差别也不大。
      
      在编码时直接使用优先级常量（MIN_PRIORITY、NORM_PRIORITY、MAX_PRIORITY）。
      
      不能把这个优先级作为核心业务的必然条件，Java 无法保证优先级高肯定会先执行，只能保证高优先级有更多的执行机会。如果优先级相同，基本上是按照 FIFO 原则，但也不能完全保证。
  
    * 守护线程
  
  * 就绪状态（RUNNABLE）
  
    是调用 start() 之后运行之前的状态。
  
  * 运行状态（RUNNING）
  
    是 run() 正在执行时线程的状态。
  
  * 阻塞状态（BLOCKED）
  
    进入此状态，有以下种情况
  
    * 同步阻塞
  
      锁被其他线程占用。
  
    * 主动阻塞
  
      调用 Thread 的某些方法，主动让出 CPU 执行权，比如 sleep()、join() 等。
  
    * 等待阻塞
  
      执行了 wait()。
  
      * sleep() 与 wait() 区别
  
        A wait can be "woken up" by another thread calling notify on the monitor which is being waited on whereas a sleep cannot.
  
        Also a wait (and notify) must happen in a block synchronized on the monitor object whereas sleep does not.
  
        Another point is that you call wait on Object itself (i.e. you wait on an object's monitor) whereas you call sleep on Thread. You can also call notifyAll if more than one thread is waiting on the monitor – this will wake *all of them up*. However, only one of the threads will be able to grab the monitor (remember that the wait is in a synchronized block) and carry on – the others will then be blocked until they can acquire the monitor's lock.
  
        One key difference not yet mentioned is that while sleeping a thread does *not* release the locks it holds, while waiting releases the lock on the object that wait() is called on.
  
  * 终止状态（DEAD）
  
    是 run() 执行结束，或因异常退出后的状态，此状态不可逆转。
  
    * 启动线程前 stop 方法是不可靠的
    
      Thread类的 stop 方法会根据线程状态来判断是终结线程还是设置线程为不可运行状态，对于未启动的线程（NEW）来说，会设置其标志位为不可启动（stopBeforeStart = true），而其他的状态则是直接停止。
    
      在 start 方法中是这样校验的：
    
      ```java
      public synchronized start() {
          start0();
          if (stopBeforeStart) {
              stop0();
          }
      }
      ```
    
      注意看 start0 方法和 stop0 方法的顺序，start0 方法在前，也就是说即使 stopBeforeStart 为 true，也会先启动一个线程，然后再 stop0 结束这个线程，而罪魁祸首就在这里！
    
      不再使用 stop 方法进行状态的设置，直接通过判断条件来决定线程是否可启用。
    
    * 不使用 stop 方法停止线程
    
      * stop 方法是过时的
    
      * stop 方法会导致代码逻辑不完整
    
        stop 方法是一种“恶意”的中断，一旦执行 stop 方法，即终止当前正在运行的线程，不管线程逻辑是否完整，这是非常危险的。
      
        ```java
      package thread;
        
        public class StopThreadDemo {
        
            public static void main(String[] args) throws InterruptedException {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Important logic");
                    }
                };
                t.start();
                Thread.sleep(100);
                /**
                 * 这是极度危险的，因为我们不知道子线程会在什么时候被终止，stop 连基本的逻辑完整性都无法保证。
                 * 而且此种操作也是非常隐蔽的，子线程执行到何处会被关闭很难定位，这为以后的维护带来了很多麻烦。
                 */
                t.stop();
            }
        
        }
        
        ```
      
      * stop 方法会破坏原子逻辑
      
        丢弃所有的锁，导致原子逻辑受损。
      
        ```java
        package thread;
        
        public class MultiThread implements Runnable {
        
            int a = 0;
        
            @Override
            public void run() {
                synchronized ("") {
                    a++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    a--;
                    System.out.println(Thread.currentThread().getName() + ": a is " + a);
                }
            }
        
        }
        
        ```
      
        ```java
        package thread;
        
        public class MultiThreadDemo {
        
            public static void main(String[] args) {
                MultiThread t = new MultiThread();
                Thread t1 = new Thread(t);
                t1.start();
        
                for (int i = 0; i < 5; i++) {
                    new Thread(t).start();
                }
        
                /**
                 * 原本期望 synchronized 同步代码块中的逻辑都是原子逻辑，不受外界线程的干扰，但是结果却出现原子逻辑被破坏的情况，这也是 stop 方法被废弃的一个重要原因：破坏了原子逻辑
                 */
                t1.stop();
            }
        
        }
        
        Thread-3: a is 1
        Thread-2: a is 1
        Thread-1: a is 1
        Thread-4: a is 1
        Thread-5: a is 1
        
        ```
      
    * 终止正在运行的线程
      
        * 使用自定义的标志位决定线程的执行情况
        
          ```java
          package thread;
          
          public class SafeStopThread extends Thread {
          
              private volatile boolean stop = false;
          
              @Override
              public void run() {
                  while (!stop) {
                      // do something
                  }
              }
          
              public void terminate() {
                  stop = true;
              }
          
          }
          ```
        
        * interrupt
        
          interrupt 不能终止一个正在执行着的线程，它只能修改中断标志而已。我们可以使用 interrupt 编写出更加简洁、安全的终止线程代码。
        
          ```java
           package thread;
            
            public class SafeStopThread extends Thread {
            
                // private volatile boolean stop = false;
            
                @Override
                public void run() {
                    // while (!stop) {
                    // do something
                    // }
                    while (!isInterrupted()) {
                        // do something
                    }
                }
            
                // public void terminate() {
                // stop = true;
                // }
            
            }
          ```
        
        * 如果我们使用的是线程池（比如 ThreadPoolExecutor 类），那么可以通过 shutdown 方法逐步关闭池中的线程，它采用的是比较温和、安全的关闭线程方法。

### Java 线程安全

线程安全的核心理念就是“要么只读，要么加锁”。

* 并发（Concurrency）与并行（Parallelism）

  并发是指在某个时间段内，多任务交替处理的能力。所谓不患寡而患不均，每个 CPU 不可能只顾着执行某个进程，让其他进程一直处于等待状态。所以，CPU 把执行时间均匀地分成若干份，每个进程执行一段时间后，记录当前的工作状态，释放相关的执行资源并进入等待状态，让其他进程抢占 CPU 资源。

  并行是指同时处理多任务的能力。目前，CPU 已经发展为多核，可以同时执行多个互不依赖的指令及执行块。

  它们的核心区别在于进程是否同时执行。

* 不是线程的安全

  线程安全不是指线程的安全，而是指内存的安全。

  目前主流操作系统都是多任务的，即多个进程同时运行。为了保证安全，每个进程只能访问分配给自己的内存空间，而不能访问别的进程的，这是由操作系统保障的。

  在每个进程的内存空间中都会有一块特殊的公共区域，通常称为堆（内存）。进程内的所有线程都可以访问到该区域，这就是造成问题的潜在原因。

  所以线程安全指的是，在堆内存中的数据由于可以被任何线程访问到，在没有任何限制的情况下存在被意外修改的风险。

* 保证高并发场景下的线程安全，可以从以下四个维度考量：

  * 数据单线程内可见

    线程可以拥有自己的操作栈、程序计数器、局部变量表等资源，它与同一进程内的其他线程共享该进程的所有资源。

    单线程总是安全的。通过限制数据仅在单线程内可见，可以避免数据被其他线程篡改。最典型的就是线程局部变量，它存储在独立虚拟机栈帧的局部变量表中，与其他线程毫无瓜葛。ThreadLocal 就是采用这种方式来实现线程安全的。

  * 只读对象

    只读对象总是安全的。它的特性是允许复制、拒绝写入。最典型的只读对象有 String、Integer 等。一个对象想要拒绝任何写入，必须要满足以下条件：使用 final 关键字修饰类，避免被继承；使用 private final 关键字避免属性被中途修改；没有任何更新方法；返回值不能为可变对象引用。
  
  * 线程安全类
  
    某些线程安全类的内部有非常明确的线程安全机制。比如 StringBuffer 就是一个线程安全类，它采用 synchronized 关键字来修饰相关方法。
  
  * 同步与锁机制
  
    * 锁
    
      锁主要提供了两种特性：互斥性和不可见性。
    
      * 用并发包中的锁类
    
        并发包的类族中，Lock 是 JUC 包的顶层接口，它的实现逻辑并未用到 synchronized，而是利用了 volatile 的可见性。
    
        在 AQS（AbstractQueuedSynchronizer） 中，定义了一个 volatile int state 变量作为共享资源，如果线程获取资源失败，则进入同步 FIFO 队列中等待；如果成功获取资源就执行临界区代码。执行完释放资源时，会通知同步队列中的等待线程来获取资源后出队并执行。
    
      * 利用同步代码块
    
        同步代码块一般使用 Java 的 synchronized 关键字来实现。这里的原则是锁的范围尽可能小，锁的时间尽可能短，既能锁对象，就不要锁类；能锁代码块，就不要锁方法。
    
        synchronized 锁特性由 JVM 负责实现。JVM 底层是通过监视锁来实现 synchronized 同步。监视锁即 monitor，是每个对象与生俱来的一个隐藏字段。
    
        JVM 对 synchronized 的优化主要在于对 monitor 的加锁、解锁上。JDK6 后不断优化使得 synchronized 提供三种锁的实现，包括偏向锁、轻量级锁、重量级锁，还提供自动的升级和降级机制。
    
        * 偏向锁
    
    * 同步
    
      资源共享的两个原因是资源紧缺和共建需求。线程共享 CPU 是从资源紧缺的维度来考虑的，而多线程共享同一变量，通常是从共建需求的维度来考虑的。在多个线程对同一变量进行写操作时，如果操作没有原子性，就可能产生脏数据。所谓原子性是指不可分割的一系列操作指令，在执行完毕前不会被任何其他操作中断，要么全部执行，要么全部不执行。如果每个线程的修改都是原子操作，就不存在线程同步问题。有些看似非常简单的操作其实不具备原子性，典型的就是 i++ 操作，它需要分为三步，即 ILOAD -> IINC -> ISTORE。
    
      计算机的线程同步，就是线程之间按某种机制协调先后次序执行，当有一个线程在对内存进行操作时，其他线程都不可以对这个内存地址进行操作，直到该线程完成操作。
    
      * volatile
    
        把 happen before 定义为方法 hb(a,b)，表示 a happen before b。如果 hb(a,b) 且 hb(b,c)，能够推导出 hb(a,c)。
    
        指令优化，计算机并不会根据代码顺序按部就班地执行相关指令（CPU 在处理信息时会进行指令优化，分析哪些取数据动作可以合并进行，哪些存数据动作可以合并进行）。
    
        时间成本的巨大差异只要存在，缓冲策略自然就会产生。线程本地内存保存了引用变量在堆内存中的副本，线程对变量的所有操作都在本地内存区域中进行，执行结束后再同步到堆内存中去。这里必然有一个时间差，在这个时间差内，该线程对副本的操作，对于其他线程都是不可见的。
    
        可见性是指某线程修改共享变量的指令对其他线程来说都是可见的，它反映的是指令执行的实时透明度。
    
        当使用 volatile 修饰变量时，意味着任何对此变量的操作都会在内存中进行，不会产生副本，以保证共享变量的可见性，局部阻止了指令重排的发生。
    
        单例模式双检锁
    
        ```java
        package thread;
        
        public class Singleton {
        
            // 用 volatile 关键字修饰目标属性，这样 singleton 就限制了编译器对它的相关读写操作，对它的读写操作进行指令重排，确定对象实例化之后才返回引用
            private static volatile Singleton singleton;
        
            private Singleton() {
            }
        
            public static Singleton getInstance() {
                if (singleton == null) {
                    synchronized (Singleton.class) {
                        if (singleton == null) {
                            // 初始化 Singleton 实例和将对象地址写到 singleton 字段并非原子操作，且这两个阶段的执行顺序是未定义的
                            singleton = new Singleton();
                        }
                    }
                }
                return singleton;
            }
        
        }
        
        ```
    
        锁也可以确保变量的可见性，但是实现方式和 volatile 略有不同。线程在得到锁时读入副本，释放时写回内存，锁的操作尤其要符合 happen before 原则。
    
        volatile 解决的是多线程共享变量的可见性问题，类似于 synchronized，但不具备 synchronized 的互斥性。所以对 volatile 变量的操作并非都具有原子性，这是一个容易犯错误的地方。
    
        ```java
        package thread;
        
        public class VolatileNotAtomic {
        
            private static volatile long count = 0L;
        
            public static void main(String[] args) {
                Thread subtractThread = new SubtractThread();
                subtractThread.start();
        
                for (int i = 0; i < 10000; i++) {
                    count++;
                }
        
                while (subtractThread.isAlive()) {
                }
        
                System.out.println("count: " + count);
            }
        
            private static class SubtractThread extends Thread {
        
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        count--;
                    }
                }
        
            }
        }
        
        ```
    
        如果是一写多读的并发场景，使用 volatile 修饰变量则非常合适。volatile 一写多读最经典的应用是 CopyOnWriteArrayList。
  
* JUC 并发包

  * 线程同步类
    * CountDownLatch
    * Semaphore
    * CyclicBarrier
  * 并发集合类
    * ConcurrentHashMap
    * CopyOnWriteArrayList
    * BlockingQueue
  * 线程管理类
    * Executors 静态工厂（ThreadPoolExecutor）
  * 锁相关类
    * ReentrankLock
    * ReentrantReadWriteLock
    * StamptedLock

* 线程池

* ThreadLocal