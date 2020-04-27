# Java 生产环境下性能监控与调优

### 基于 JDK 命令行工具的监控

* JVM 的参数类型

  * 标准参数

    -help；-version

  * X 参数

    java -Xint -version（interpreted mode）

    java -Xcomp -version（compiled mode）

    java -Xmixed -version（mixed mode）

  * XX 参数

    * Boolean 类型

      -XX:[+-]\<name\> 启用或禁用 name 属性

      -XX:+UseConcMarkSweepGC

      -XX:+UseG1GC

    * key=value 类型

      -XX:\<name\>=\<value\>

      -XX:NewRatio=2

      -Xms 等价于 -XX:InitialHeapSize

      -Xmx 等价于 -XX:MaxHeapSize

* 查看 JVM 参数

  * -XX:+PrintFlagsInitial

  * -XX:+PrintFlagsFinal

    = 表示默认值；=: 表示被用户或者 JVM 修改后的值

  * -XX:+UnlockExperimentalVMOptions：解锁实验参数

  * -XX:+UnlockDiagnosticVMOptions：解锁诊断参数

  * -XX:+PrintCommandLineFlags：打印命令行参数

* 查看 JVM 运行时参数（[JDK Tools and Utilities](https://docs.oracle.com/javase/8/docs/technotes/tools/index.html)）

  * jps

    jps -l

  * jinfo

    jinfo -flag MaxHeapSize \<pid\>；jinfo -flags \<pid\>

    jinfo -flag UseConcMarkSweepGC \<pid\>；jinfo -flag UseG1GC \<pid\>

* jstat 查看 JVM 统计信息

  * 类加载

    jstat class \<pid\> interval count

  * 垃圾收集

    jstat gc \<pid\> interval count

  * JIT 编译

    jstat compiler \<pid\> interval count

* JVM 内存结构

  * Metaspace

    CCS：短指针

    CodeCache：JVM 生成的 native code 存放的内存空间称之为 Code Cache，JIT 编译、JNI 等都会编译代码到 native code，其中 JIT 生成的 native code 占用了 Code Cache 的绝大部分空间

* jmap + MAT 实战内存溢出

  * 内存溢出

    new 对象 + -Xmx32M -Xms32M

    ACM + -XX:MetaspaceSize=32M -XX:MaxMetaspaceSize=32M

  * 导出内存映像文件

    * 内存溢出自动导出

      -XX:+HeapDumpOnOutOfMemoryError

      -XX:HeapDumpPath=./

    * jmap 手动导出

      jmap -dump:format=b,file=heap.hprof \<pid\>

  * MAT 分析内存溢出

    维度：对象数量、对象内存

    Merge Shortest Paths to GC Roots -> exclude all phantom/weak/soft etc. references

* jstack 实战死循环与死锁

  * [线程的状态与转换](https://mp.weixin.qq.com/s/GsxeFM7QWuR--Kbpb7At2w)

  * jstack \<pid\>

  * 死循环

    nohup java -jar xx.jar &（后台运行，缺省输出到 nohup.out）

    top

    jstack \<pid\> > pid.txt

    top -p \<pid\> -H

    printf "%x" \<pid\>（将十进制转为十六进制）

  * 死锁

    jstack \<pid\> > pid.txt

    Found * deadlock

### 基于 JVisualVM 的可视化监控

* 监控本地 Tomcat

  * 概述

    * JVM 参数
    * 系统属性
  * 监视

    * jstat
    * 堆 Dump（jmap）
  * 线程

    * 线程 Dump（jstack）
  * 抽样器

    * CPU
    * 内存
  * Visual GC
    * [Plugins Centers](https://visualvm.github.io/pluginscenters.html)

* 监控远程 Tomcat

  * 修改 Catalina.sh（JMX）

    vi ./bin/catalina.sh

    ESC 进入命令模式

    /JAVA_OPTS 查找 JAVA_OPTS

    n 寻找下一个

    JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=\<port\> -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.net.preferIPv4Stack=true -Djava.rmi.server.hostname=\<hostname\>"

* 监控普通的 Java 进程

  nohup java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=\<port\> -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.net.preferIPv4Stack=true -Djava.rmi.server.hostname=\<hostname\> -jar xx.jar &

### 基于 Btrace 的监控调试

* Btrace 简介

  Btrace 可以动态地向目标应用程序的字节码注入追踪代码

* Btrace 安装

  * [btrace](https://github.com/btraceio/btrace)

  * 配置

    新建环境变量 BTRACE_HOME

    添加 Path：%BTRACE_HOME%\bin

* Btrace 入门

  * Btrace 脚本

    ```java
            <dependency>
                <groupId>com.sun.btrace</groupId>
                <artifactId>btrace-agent</artifactId>
                <version>1.3.11</version>
                <type>jar</type>
                <scope>system</scope>
                <systemPath>btrace-agent.jar</systemPath>
            </dependency>
            <dependency>
                <groupId>com.sun.btrace</groupId>
                <artifactId>btrace-boot</artifactId>
                <version>1.3.11</version>
                <type>jar</type>
                <scope>system</scope>
                <systemPath>btrace-boot.jar</systemPath>
            </dependency>
            <dependency>
                <groupId>com.sun.btrace</groupId>
                <artifactId>btrace-client</artifactId>
                <version>1.3.11</version>
                <type>jar</type>
                <scope>system</scope>
                <systemPath>btrace-client.jar</systemPath>
            </dependency>
    ```

    ```java
    @BTrace
    public class Print {
        @OnMethod(clazz = "", method = "", location = @Location(Kind.ENTRY))
        public static void any(@ProbeClassName String pcn, @ProbeMethodName String pmn, AnyType[] args) {
            BTraceUtils.printXX();
        }
    }
    ```

    btrace \<pid\> Print.java
  
  * JVisualVM BTrace Workbench 插件
  
* Btrace 使用详解

  * 拦截方法

    * 普通方法

      @OnMethod(clazz = "", method = "")

    * 构造函数

      @OnMethod(clazz = "", method = "\<init\>")

    * 同名函数

      public static void any(@ProbeClassName String pcn, @ProbeMethodName String pmn, String str, Integer i)

      public static void any(@ProbeClassName String pcn, @ProbeMethodName String pmn, String str)

  * 拦截时机

    Kind.ENTRY、Kind.RETURN、Kind.THROW、Kind.LINE

    * Kind.RETURN

      @OnMethod(clazz = "", method = "", location = @Location(Kind.RETURN))

      public static void any(@ProbeClassName String pcn, @ProbeMethodName String pmn, @Return AnyType result)

    * Kind.THROW

      ```java
      @BTrace
      public class PrintOnThrow {
          // store current exception in a thread local
          // variable (@TLS annotation). Note that we can't
          // store it in a global variable!
          @TLS
          static Throwable currentException;
      
          // introduce probe into every constructor of java.lang.Throwable
          // class and store "this" in the thread local variable.
          @OnMethod(clazz = "java.lang.Throwable", method = "<init>")
          public static void onthrow(@Self Throwable self) {
              currentException = self;
          }
      
          @OnMethod(clazz = "java.lang.Throwable", method = "<init>")
          public static void onthrow(@Self Throwable self, String s) {
              currentException = self;
          }
      
          @OnMethod(clazz = "java.lang.Throwable", method = "<init>")
          public static void onthrow(@Self Throwable self, String s, Throwable cause) {
              currentException = self;
          }
      
          @OnMethod(clazz = "java.lang.Throwable",method = "<init>")
          public static void onthrow(@Self Throwable self, Throwable cause) {
              currentException = self;
          }
      
          // when any constructor of java.lang.Throwable returns
          // print the currentException's stack trace.
          @OnMethod(clazz = "java.lang.Throwable", method = "<init>", location = @Location(Kind.RETURN))
          public static void onthrowreturn() {
              if (currentException != null) {
                  BTraceUtils.Threads.jstack(currentException);
                  currentException = null;
              }
          }
      }
      ```

    * Kind.LINE

      @OnMethod(clazz = "", method = "", location = @Location(value = Kind.LINE, line = 55)

      public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, int line)
  
  * 拦截 this、入参、返回值
  
    * this
  
      @Self
  
    * 入参
  
      AnyType，也可以用真实类型，同名用真实类型
  
    * 返回值
  
      @Return
  
  * 其他
  
    * 获取对象的值
  
      简单类型：直接获取
  
      复杂类型：反射，类名 + 属性名
  
      ```java
      @BTrace
      public class PrintArgComplex {
          @OnMethod(clazz = "", method = "", location = @Location(Kind.ENTRY))
          public static void any(@ProbeClassName String pcn, @ProbeMethodName String pmn, User user) {
              //print all fields
              BTraceUtils.printFields(user);
              //print one field
              Field filed = BTraceUtils.field("User", "name");
              BTraceUtils.println(BTraceUtils.get(filed, user));
          }
      }
      ```
  
      btrace -cp "target\\classes" \<pid\> PrintArgComplex.java
  
    * 正则
    
      @OnMethod(clazz = "", method = "/.*/")

* Btrace 注意

  默认只能本地运行

  生产环境下可以使用，但是被修改的字节码不会被还原

### Tomcat 性能监控与调优

* Tomcat 远程 Debug

  * JDWP 协议

  * 修改 Tomcat 参数

    vi ./bin/startup.sh

    添加 jpda 到 exec，jpda start

    vi ./bin/catalina.sh

    /JPDA

     JPDA_ADDRESS=“\<port\>” 设置监听端口

  * 检查端口是否启动

    netstat -nap | grep \<port\>

  * eclipse 或 IDEA 调试

    Remote Java Application / Remote

  * 普通 Java 线程远程 Debug

    -agentlib:jdwp=transport=dt_socket,address=\<port\>,server=y,suspend=n

* tomcat-manager 监控

  * 开启 tomcat-manager（docs/manager-howto.html）

    * conf/tomcat-users.xml 添加用户

      取消注释 \<role\>、\<user\>

    * conf/Catalina/localhost/manager.xml 配置允许的远程连接

      ```xml
      <Context privileged="true" antiResourceLocking="false" docBase="${catalina.home}/webapps/manager">
        <Valve className="org.apache.catalina.valves.RemoteAddrValve" allow="127\.0\.0\.1" />
      </Context>
      ```

  * 访问 manager

    127.0.0.1:8080/mamager

* psi-probe 监控

  * [psi-probe](https://github.com/psi-probe/psi-probe)

  * 安装

    git clone https://github.com/psi-probe/psi-probe

    mvn clean package

    将 web/target/probe.war 部署到 Tomcat 下

    配置 conf/tomcat-users.xml 和 conf/Catalina/localhost/manager.xml

    访问：127.0.0.1:8080/probe

* Tomcat 调优

  * 内存优化

  * 线程优化

    * doc/config/http.html

    * maxConnections

      **The maximum number of connections that the server will accept and process at any given time.** When this number has been reached, the server will accept, but not process, one further connection. This additional connection be blocked until the number of connections being processed falls below maxConnections at which point the server will start accepting and processing new connections again. Note that once the limit has been reached, the operating system may still accept connections based on the acceptCount setting. The default value varies by connector type. **For NIO and NIO2 the default is 10000.**
      
    * acceptCount
  
      **The maximum queue length for incoming connection requests when all possible request processing threads are in use.** Any requests received when the queue is full will be refused.  **The default value is 100.**
      
    * maxThreads
  
      **The maximum number of request processing threads to be created by this Connector, which therefore determines the maximum number of simultaneous requests that can be handled. If not specified, this attribute is set to 200.** If an executor is associated with this connector, this attribute is ignored as the connector will execute tasks using the executor rather than an internal thread pool. Note that if an executor is configured any value set for this attribute will be recorded correctly but it will be reported (e.g. via JMX) as -1 to make clear that it is not used.

    * minSpareThreads

      **The minimum number of threads always kept running.** This includes both active and idle threads. **If not specified, the default of 10 is used.** If an executor is associated with this connector, this attribute is ignored as the connector will execute tasks using the executor rather than an internal thread pool. Note that if an executor is configured any value set for this attribute will be recorded correctly but it will be reported (e.g. via JMX) as -1 to make clear that it is not used.
  
  * 配置优化
  
    * autoDeploy（doc/config/host.html）
  
      **This flag value indicates if Tomcat should check periodically for new or updated web applications while Tomcat is running.** If true, Tomcat periodically checks the appBase and xmlBase directories and deploys any new web applications or context XML descriptors found. Updated web applications or context XML descriptors will trigger a reload of the web application. **The flag's value defaults to true.**
  
    * enableLookups（doc/config/http.html）
  
      **Set to true if you want calls to request.getRemoteHost() to perform DNS lookups in order to return the actual host name of the remote client.** Set to false to skip the DNS lookup and return the IP address in String form instead (thereby improving performance). **By default, DNS lookups are disabled.**
  
    * reloadable（doc/config/context.html）
  
      **Set to true if you want Catalina to monitor classes in /WEB-INF/classes/ and /WEB-INF/lib for changes, and automatically reload the web application if a change is detected.** This feature is very useful during application development, but it requires significant runtime overhead and is not recommended for use on deployed production applications. **That's why the default setting for this attribute is false.**
      
    * potocol（对 HTTP 1.1 协议的长连接支持）
    
      server.xml
      
      ```xml
      <Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
      ```
    
  * Session 优化（禁用 Session）
    
    context.xml
    
    ```xml
    <Manager className="com.nosession.SessionManager" />
    ```
    
  * APR（高并发）
    

### Nginx 性能监控与调优

* 安装 Nginx

  * yum

    * 修改 yum 源

      vim /etc/yum.repos.d/nginx.repo

    * yum install nginx

* Nginx 启动停止

  启动 Nginx：nginx

  停止 Nginx：nginx -s stop

  重启 Nginx：nginx -s reload

  查看编译参数：nginx -V

  默认配置文件：/etc/nginx/nginx.conf

* ngx_http_stub_status 监控连接信息

  * 配置 ngx_http_stub_status

    /etc/nginx/conf.d/default.conf

    location = /nginx_status {

    ​    stub_status on;

    ​    access_log off;

    ​    allow 127.0.0.1;

    ​    deny all;

    }

  * 参数（[status_module](http://nginx.org/en/docs/http/ngx_http_stub_status_module.html)）

    * Active connections

      The current number of active client connections including Waiting connections.

    * Reading

      The current number of connections where nginx is reading the request header.

    * Writing

      The current number of connections where nginx is writing the response back to the client.

    * Waiting
    
      The current number of idle client connections waiting for a request.

* ngxtop 监控请求信息

  * ngxtop 安装

    安装 python-pip：

    yum install epel-release

    yum install python-pip

    安装 ngxtop：

    pip install ngxtop

  * [ngxtop](https://github.com/lebinh/ngxtop)

    * 指定配置文件：ngxtop -c /etc/nginx/nginx.conf
    * 查看状态是 504：ngxtop -c /ect/nginx/nginx.conf -i 'status == 504'
    * 查询访问最多 ip：ngxtop -c /etc/nginx/nginx.conf -g remote_addr

* nginx-rrd 图形化监控

  * 安装

  * httpd-tools

    yum -y install httpd-tools

    ab -n 10000 -c 10 http://127.0.0.1/index.html

* Nginx 优化

  * nginx -t 检查 Nginx 配置文件是否符合规范

  * 配置工作线程数和并发连接数

    /etc/nginx/nginx.conf

    worker_processes 4;（cpu 核心）

    events {

    ​    worker_connections 10240;（受操作系统限定，进程最大可打开的 fd）

    ​    multi_accept on;

    ​    use epoll;

    }

  * 配置后端 Server 长连接

    /etc/nginx/conf.d/default.conf

    upstream server_pool {

    ​    server localhost:8080 weight=1 max_fails=2 fail_timeout=30s;

    ​    server localhost:8080 weight=1 max_fails=2 fail_timeout=30s;

    ​    keepalive 300;

    }

    location / {

    ​    proxy_http_version 1.1;

    ​    proxy_set_header Upgrade $http_upgrade;

    ​    proxy_set_header Connection "upgrade";

    ​    proxy_pass http://server_pool/;

    }

  * 启用压缩

    gzip on;
    gzip_http_version 1.1;
    gzip_disable "MSIE [1-6]\.(?!.*SV1)";
    gzip_proxy any;
    gzip_types text/plain application/json application/x-javascript application/css application/xml application/xml+rss text/javascript application/x-httpd-php image/jpeg image/gif image/png image/x-ms-bmp;
    gzip_vary on;
    gzip_static on;

  * 操作系统优化

    * /etc/sysctl.conf

      sysctl -w net.ipv4.tcp_syncookies=1（开启 SYN Cookies，防止一个套接字在有过多试图连接到达时引起过载）

      sysctl -w net.core.somaxconn=1024（默认 128，Socket 的监听队列）

      sysctl -w net.ipv4.tcp_fin_timeout=10（TIME_WAIT 等待时间）

      sysctl -w net.ipv4.tcp_tw_reuse=1（直接使用 TIME_WAIT 的连接）

      sysctl -w net.ipv4.tcp_tw_recycle=0（禁用回收）

    * /etc/security/limits.conf（进程最大可打开的 fd）

      \* hard nofile204800

      \* soft nofile 204800

      \* soft core unlimited

      \* soft stack 204800

    * 其它

      sendfile on;（减少文件在应用和内核之间拷贝）

      tcp_nopush on;（当数据包达到一定大小再发送）

      tcp_nodelay off;（有数据即刻发送）

### JVM 层 GC 调优

* JVM 内存结构

  * Runtime Data Area

    * PC Register

      线程私有，当前正在执行的指令的地址

    * JVM Stacks
    
      线程私有，生命周期与线程相同，描述 Java 方法执行的内存模型（每个方法在执行时都会创建一个栈帧，用于存储局部变量表、操作栈、动态链接、方法返回值等）。每个方法从调用直至执行完成的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程
    
    * Heap
    
      被所有线程共享的一块内存区域，在虚拟机启动时创建，几乎所有的对象实例都在这里分配内存
    
    * Method Area（Non-Heap）
    
      线程共享的内存区域，用于存储被虚拟机加载的类信息、常量、静态变量、JIT 编译后的代码等
    
      * Run-Time Constant Pool
    
        运行时常量池是方法区的一部分，用于存放编译期生成的各种字面量和符号引用
    
    * Native Method Stacks
    
      Native 方法

  * JDK8

    * Metaspace（Class、Package、Method、Field、字节码、常量池、符号引用等）

      CCS（32 位指针的 Class 对象，-XX:+UseCompressedClassPointers）：压缩类空间。堆内的每一个对象，都存在指向 Class 对象的指针（64 位），如果使用短指针（32 位），那么所引用的 Class 对象存放在 CCS

      CodeCache（-Xint、-Xcomp）：JIT 即时编译后的本地代码、JNI 使用的 C 代码

  * 常用参数

    * -Xms -Xmx
    * -XX:NewSize -XX:MaxNewSize
    * -XX:NewRatio -XX:SurvivorRatio
    * -XX:MetaspaceSize -XX:MaxMetaspaceSize
    * -XX:UseCompressedClassPointers -XX:CompressedClassSpaceSize
    * -XX:InitialCodeCacheSize -XX:ReservedCodeCacheSize

* 垃圾回收算法

  * 可达性分析（根节点）

  * 标记清除算法

  * 复制算法

  * 标记整理算法

  * 分代垃圾回收（Young 区、Old 区）

  * 对象分配

    大对象直接进入老年代：-XX:PretenureSizeThreshold

    长期存活对象进入老年代：-XX:MaxTenuringThreshold

* 垃圾收集器

  * 串行收集器 Serial（Serial、Serial Old）

    -XX:+UseSerialGC -XX:+UseSerialOldGC

  * 并行收集器 Parallel（Parallel Scanvenge、Parallel Old - 吞吐量）

    -XX:+UseParallelGC -XX:+UseParallelOldGC

    -XX:ParallelGCThreads=\<N\>（并行线程数，The number of garbage collector threads；CPU > 8，N = CPU * 5 / 8 + 3；CPU < 8，N = CPU）

    Server 模式下默认收集器

    * Parallel Collector Ergonomics

      -XX:MaxGCPauseMillis=\<N\>（**Maximum Garbage Collection Pause Time**: This is interpreted as a hint that pause times of \<N\> milliseconds or less are desired; by default, there is no maximum pause time goal. If a pause time goal is specified, the heap size and other parameters related to garbage collection are adjusted in an attempt to keep garbage collection pauses shorter than the specified value）

      -XX:GCTimeRatio=\<N\>（**Throughput**: The throughput goal is measured in terms of the time spent doing garbage collection versus the time spent outside of garbage collection. Which sets the ratio of garbage collection time to application time to 1 / (1 + \<N\>. For example, -XX:GCTimeRatio=19 sets a goal of 1/20 or 5% of the total time in garbage collection. The default value is 99, resulting in a goal of 1% of the time in garbage collection）

      -Xmx\<N\>（**Footprint**: Maximum heap footprint. In addition, the collector has an implicit goal of minimizing the size of the heap as long as the other goals are being met）

    * 动态内存调整

      By default a generation grows in increments of 20% and shrinks in increments of 5%.

      -XX:YoungGenerationSizeIncrement=\<Y\>

      -XX:TenuredGenerationSizeIncrement=\<T\>
      
      -XX:AdaptiveSizeDecrementScaleFactor=\<D\>

  * 并发收集器 Concurrent（CMS、G1 - 停顿时间）

    CMS：-XX:+UseConcMarkSweepGC -XX:+UseParNewGC

    * 收集过程（6 步）

    * 缺点

      CPU 敏感

      浮动垃圾

      空间碎片

    * 相关参数

      -XX:ConcGCThreads（并发线程数，默认 (ParallelGCThreads + 3) / 4）

      -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=5

      -XX:CMSInitiatingOccupancyFraction=92（A concurrent collection also starts if the occupancy of the tenured generation exceeds an initiating occupancy. The default value for this initiating occupancy threshold is approximately 92%）

      -XX:+UseCMSInitiatingOccupancyOnly（不基于运行时收集的数据来启动 CMS 垃圾收集周期，通过 CMSInitiatingOccupancyFraction 值进行每一次 CMS 收集）

      FullGC 之前 YGC：-XX:+CMSScavengeBeforeRemark

      回收 Perm 区：-XX:+CMSClassUnloadingEnabled

    * iCMS

      适用于单核或者双核，JDK8 废弃

    G1：-XX:+UseG1GC（[garbage_first_garbage_collection](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/g1_gc.html#garbage_first_garbage_collection)）

    * The first focus of G1 is to provide a solution for users running applications that require large heaps with limited GC latency. This means heap sizes of around 6 GB or larger, and a stable and predictable pause time below 0.5 seconds.

    * 新生代和老生代收集器

    * 概念

      * Region（-XX:G1HeapRegionSize=n. Sets the size of a G1 region. The value will be a power of two and can range from 1 MB to 32 MB. The goal is to have around 2048 regions based on the minimum Java heap size）

      * SATB（snapshot-at-the-beginning）

      * RSet

      * Young GC

        Eden -> Survivor -> Old

      * MixedGC

        不是 FullGC，回收所有的 Young 和部分 Old

        global concurrent marking（Initial marking phase、Root region scanning phase、Concurrent marking phase、Remark phase、Cleanup phase）

        -XX:InitiatingHeapOccupancyPercent=\<NN\>（A **concurrent marking phase** is started when the occupancy of the entire Java heap reaches the value of the parameter InitiatingHeapOccupancyPercent. The default value of InitiatingHeapOccupancyPercent is 45）

        -XX:G1HeapWastePercent=5（Sets the percentage of heap that you are willing to waste. The Java HotSpot VM does not initiate the mixed garbage collection cycle when the reclaimable percentage is less than the heap waste percentage. The default is 5 percent）

        -XX:G1MixedGCLiveThresholdPercent=85（Sets the occupancy threshold for an old region to be included in a mixed garbage collection cycle. The default occupancy is 85 percent）

        -XX:G1MixedGCCountTarget=8（Sets the target number of mixed garbage collections after a marking cycle to collect old regions with at most G1MixedGCLIveThresholdPercent live data. The default is 8 mixed garbage collections）
        
        -XX:G1OldCSetRegionThresholdPercent=10（Sets an upper limit on the number of old regions to be collected during a mixed garbage collection cycle. The default is 10 percent of the Java heap）

    * 常用参数

      -XX:MaxGCPauseMillis=200（Sets a target value for desired maximum pause time. The default value is 200 milliseconds）

      -XX:G1NewSizePercent=5（Sets the percentage of the heap to use as the minimum for the young generation size. The default value is 5 percent of your Java heap）

      -XX:G1MaxNewSizePercent=60（Sets the percentage of the heap size to use as the maximum for young generation size. The default value is 60 percent of your Java heap）

      -XX:G1ReservePercent=10（Sets the percentage of reserve memory to keep free so as to reduce the risk of to-space overflows. The default is 10 percent）

      -XX:ParallelGCThreads=n（Sets the value of the STW worker threads）

      -XX:ConcGCThreads=n（Sets the number of parallel marking threads. Sets n to approximately 1/4 of the number of parallel garbage collection threads）

    * 特点

      G1 的设计原则是“首先收集尽可能多的垃圾（Garbage First，名称的由来）”。因此，G1 并不会等内存耗尽（串行、并行）或者快耗尽（CMS）的时候开始垃圾收集，而是在内部采用了启发式算法，在老年代找出具有高收集收益的分区进行收集。同时 G1 可以根据用户设置的暂停时间目标自动调整年轻代和总堆大小，暂停目标越短年轻代空间越小、总空间就越大；

      G1 采用内存分区（Region）的思路，将内存划分为一个个相等大小的内存分区，回收时则以分区为单位进行回收，存活的对象复制到另一个空闲分区中。由于都是以相等大小的分区为单位进行操作，因此 G1 天然就是一种压缩方案（局部压缩）；

      G1 虽然也是分代收集器，但整个内存分区不存在物理上的年轻代与老年代的区别，也不需要完全独立的 survivor（to space）堆做复制准备。G1 只有逻辑上的分代概念，或者说每个分区都可能随 G1 的运行在不同代之间前后切换。

      G1 收集都是 STW 的，但年轻代和老年代的收集界限比较模糊，采用了混合（mixed）收集的方式。即每次收集既可能只收集年轻代分区（年轻代收集），也可能在收集年轻代的同时，包含部分老年代分区（混合收集），这样即使堆内存很大时，也可以限制收集范围，从而降低停顿。

    * 最佳实践

      * **Young Generation Size**

        Avoid explicitly setting young generation size with the -Xmn option or any or other related option such as -XX:NewRatio. Fixing the size of the young generation overrides the target pause-time goal.

      * **Pause Time Goals**

        The throughput goal for the G1 GC is 90 percent application time and 10 percent garbage collection time.

      * **Taming Mixed Garbage Collections**

        -XX:InitiatingHeapOccupancyPercent: Use to change the marking threshold.

        -XX:G1MixedGCLiveThresholdPercent and -XX:G1HeapWastePercent: Use to change the mixed garbage collection decisions.

        -XX:G1MixedGCCountTarget and -XX:G1OldCSetRegionThresholdPercent: Use to adjust the CSet for old regions.

    * Switching to G1

      More than 50% of the Java heap is occupied with live data.

      The rate of object allocation rate or promotion varies significantly.

      The application is experiencing undesired long garbage collection or compaction pauses (longer than 0.5 to 1 second).

  * 停顿时间 vs 吞吐量

    停顿时间：-XX:MaxGCPauseMillis

    吞吐量：-XX:GCTimeRatio=\<n\>

  * 如何选择垃圾收集器

    [Selecting a Collector](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/collectors.html)

    [Garbage Collection Tuning Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/toc.html)

* 可视化 GC 日志分析工具

  * 打印 GC 日志

    vi ./bin/catalina.sh

    /JAVA_OPTS

    -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:$CATALINE_HOME/logs/gc.log

  * GC 日志格式

    * Parallel
    * CMS（[Understanding CMS GC Logs](https://blogs.oracle.com/poonam/understanding-cms-gc-logs)）
    * G1（[Understanding G1 GC Logs](https://blogs.oracle.com/poonam/understanding-g1-gc-logs)）

  * GCeasy（https://gceasy.io/）

  * GCViewer（[GCViewer](https://github.com/chewiebug/GCViewer)）

* Tomcat GC 调优实战

  * GC 调优步骤

    打印 GC 日志；根据日志得到关键性能指标；分析 GC 原因，调优 JVM 参数

  * 初始设置
  
    -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$CATALINA_HOME/logs/
  
  * Parallel Collector 调优
  
    * [Behavior-Based Tuning](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/ergonomics.html#ergonomics)
  
    * [Tuning Strategy](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/ergonomics.html#ergonomics)
  
      Do not choose a maximum value for the heap unless you know that you need a heap greater than the default maximum heap size. Choose a throughput goal that is sufficient for your application.
  
      The heap will grow or shrink to a size that will support the chosen throughput goal. A change in the application's behavior can cause the heap to grow or shrink. For example, if the application starts allocating at a higher rate, the heap will grow to maintain the same throughput.
  
      If the heap grows to its maximum size and the throughput goal is not being met, the maximum heap size is too small for the throughput goal. Set the maximum heap size to a value that is close to the total physical memory on the platform but which does not cause swapping of the application. Execute the application again. If the throughput goal is still not met, then the goal for the application time is too high for the available memory on the platform.
  
      If the throughput goal can be met, but there are pauses that are too long, then select a maximum pause time goal. Choosing a maximum pause time goal may mean that your throughput goal will not be met, so choose values that are an acceptable compromise for the application.
  
      It is typical that the size of the heap will oscillate as the garbage collector tries to satisfy competing goals. This is true even if the application has reached a steady state. The pressure to achieve a throughput goal (which may require a larger heap) competes with the goals for a maximum pause time and a minimum footprint (which both may require a small heap).
  
    * 参数示例
  
      -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=64M 
  
      -XX:GCTimeRatio=99 -XX:MaxGCPauseMillis=100
  
      -XX:YoungGenerationSizeIncrement=30
  
  * G1 Collector 调优
  
    * [Recommendations](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/g1_gc_tuning.html#g1_gc_tuning)
  
    * 参数示例
  
      -XX:MetaspaceSize=64M
  
      -Xms 128M -Xmx 128M
  
      -XX:MaxGCPauseMillis=100

### Java 代码层优化

* JVM 字节码指令和 javap

  * javap -verbose（[javap](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javap.html)）
  * [Field Descriptors](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.2)
  * [Method Descriptors](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.3)
  * [The Constant Pool](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4-140)
  * [Local Variables](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6.1)
  * [The LocalVariableTable Attribute](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.13)
  * [Operand Stacks](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6.2)
  * [The Code Attribute](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3)
  * [The LineNumberTable Attribute](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.12)
  * 基于栈的架构

* i++ 和 ++i

  * 字节码

    * i++

      iload_0

      iinc 0, 1

      istore_1

    * ++i

      iinc 0, 1

      iload_0

      istore_1

  * for(int i = 0; i < 10; i++) 和 for(int i = 0; i < 10; ++i) 效率一致（字节码相同）

* 字符串拼接 + 原理

  * for(int i = 0; i < 10; i++) { s = s + "str"; }

    在每次循环 new StringBuilder(s).append("str").toString()

  * for(int i = 0; i < 10; i++) { s.append("str"); }

* try-finally

  String str = "str";

  try {

  ​    return str;

  } finally {

  ​    str = "finally";

  }

  返回 "str"

* [String Constant Variable](https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.12.4)

  * 类、方法、变量尽量指定 final 修饰

    字符串拼接背后不一定是 StringBuilder（编译时替换）

  * intern()

    jdk6：拷贝值到常量池

    jdk7 及以上：拷贝引用到常量池

  * String Deduplication in G1

* 代码优化方法

  * 尽量重用对象，不要循环创建对象，如 for 循环字符串拼接

  * 容器类（如 ArrayList、HashMap）初始化时指定长度

  * ArrayList 随机访问；LinkedList 双向链表，添加、删除快

  * 集合遍历尽量减少重复计算

    for(int i = 0, len = collection.size(); i < len; i++)

  * 使用 Entry 遍历 Map
  
    for(Map.Entry<String, Object> entry : map.entrySet()) {}
  
  * 大数组复制使用 System.arraycopy
  
  * 尽量使用基本类型而不是包装类型
  
    自动装箱，如 Integer i = 500;// Integer.valueOf(500); -> new Integer(500);
  
  * 不要手动调用 System.gc()
  
  * 及时消除过期对象的引用，防止内存泄露
  
    public Object pop() { return elements[--size]; }
  
  * 尽量使用局部变量，减小变量的作用域
  
  * 尽量使用非同步容器（如 ArrayList 替代 Vector）
  
  * 尽量减小同步作用范围（如 synchronized 代码块替代 synchronized 方法）
  
  * ThreadLocal 缓存线程不安全的对象（如 SimpleDateFormat）
  
  * 尽量使用延迟加载（如单例模式使用静态内部类）
  
  * 使用反射配备缓存
  
  * 尽量使用连接池、线程池、对象池
  
  * 及时释放资源（如 I/O、Socket）
  
  * 慎用异常，不要用抛异常来表示正常的业务逻辑
  
  * String 操作慎用正则表达式
  
  * 日志输出使用不同的级别
  
  * 日志中参数拼接使用占位符