# Offer 直通车——计算机网络

* 开放式系统互联（OSI）模型（已经被淘汰）

  应用层、表示层、会话层、传输层、网络层、数据链路层、物理层。

* TCP/IP 模型

  应用层（HTTP、FTP、SMTP）

  传输层（TCP、UDP）

  网络层（IP、ARP）

  链路层（IEEE 802.x、PPP）

  程序在发送消息时，应用层按既定的协议打包数据，随后由传输层加上双方的端口号，由网络层加上双方的 IP 地址，由链路层加上双方的 MAC 地址，并将数据拆分成数据帧，经过多个路由器和网关后，到达目标机器。

* TCP

  * TCP 简介

    是一种面向连接、确保数据在端到端间可靠传输的协议。

    所谓面向连接，是一种端到端间通过失败重传机制建立的可靠数据传输方式。

  * TCP 报头

    TCP 报头中的源机器端口号和目标机器端口号与 IP 报头中的源 IP 地址和目标 IP 地址所组成的四元组可唯一标识一条 TCP 连接。

    序列号 seq=?；确认序号 ack=?

  * TCP Flags

    URG、**ACK**、PSH、RST、**SYN**、**FIN**

* TCP 三次握手

  * 流程图

    ![TCP 三次握手](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/TCP%20%E4%B8%89%E6%AC%A1%E6%8F%A1%E6%89%8B.png)

    在 TCP/IP 协议中，TCP 协议提供可靠的连接服务，采用三次握手建立一个连接。

    第一次握手：建立连接时，客户端发送 SYN 包（seq = x）到服务器，并进入 SYN_SEND 状态，等待服务器确认。

    第二次握手：服务器收到 SYN 包，必须确认客户的 SYN（ack = x + 1），同时自己也发送一个 SYN 包（seq = y），即 SYN + ACK 包，此时服务器进入 SYN_RECV 状态。

    第三次握手：客户端收到服务器的 SYN + ACK 包，向服务器发送确认包 ACK（ack = y + 1），此包发送完毕，客户端和服务器进入 ESTABLISHED 状态，完成三次握手。

  * 为什么需要三次握手才能建立起连接

    信息对等：双方只有确认 4 类信息，才能建立连接。自己发报能力，自己收报能力，对方发报能力、对方收报能力。

    防止超时导致脏连接。

  * SYN 超时

    Server 收到 Client 的 SYN，回复 SYN+ACK 的时候未收到 ACK 确认，Server 不断重试直至超时，Linux 默认等待 63 秒（1 + 2 + 4 + 8 + 16 + 32）才断开连接。

  * 针对 SYN Flood 的防护措施

    SYN 队列满后，通过 tcp_syncookies 参数发送 SYN Cookie，若为正常连接则 Client 会回发 SYN Cookie，直接建立连接。

  * 保活机制（KEEP-ALIVE）

    向对方发送保活探测报文，如果未收到响应则继续发送，尝试次数达到保活探测数仍未收到响应则中断连接。

* TCP 四次挥手

  * 流程图

    ![TCP 四次挥手](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/TCP%20%E5%9B%9B%E6%AC%A1%E6%8C%A5%E6%89%8B.png)

    第一次挥手：Client 发送一个 FIN，用来关闭 Client 到 Server 的数据传送，Client 进入 FIN_WAIT_1 状态。

    第二次挥手：Server 收到 FIN 后，发送一个 ACK 给 Client，确认序号为收到序号 + 1（与 SYN 相同，一个 FIN 占用一个序号），Server 进入 CLOSE_WAIT 状态。

    第三次挥手：Server 发送一个 FIN，用来关闭 Server 到 Client 的数据传送，Server 进入 LAST_ACK 状态。

    第四次挥手：Client 收到 FIN 后，Client 进入 TIME_WAIT 状态，接着发送一个 ACK 给 Server，确认序号为收到序号 + 1，Server 进入 CLOSED 状态，完成四次挥手。

  * 为什么有 TIME_WAIT 状态

    确保有足够的时间让对方收到 ACK 包。

  * 为什么需要四次挥手才能断开连接

    因为全双工通信，发送方和接收方都需要 FIN 报文和 ACK 报文。

  * 服务器出现大量 CLOSE_WAIT 状态的原因

    对方关闭 socket 连接，我方忙于读或写，没有及时关闭连接。

    解决思路：检查代码，特别是释放资源的代码；检查配置，特别是处理请求的线程配置。

    统计 TCP 连接数：netstat -n | awk '/^tcp/{++S[$NF]}END{for(a in S) print a,S[a]}'

* UDP

  * UDP 报头

  * UDP 特点

    面向非连接

    不维护连接状态，支持同时向多个客户端传输相同的消息

    数据包报头只有 8 个字节，额外开销较小

    吞吐量只受限于数据生成速率、传输速率以及机器性能

    尽最大努力交付，不保证可靠交付，不需要维持复杂的链接状态表

    面向报文，不对应用程序提交的报文信息进行拆分或者合并（应用层交给 UDP 多长的报文，UDP 就照样发送，即一次发送一个报文。因此，应用程序必须选择合适大小的报文，若报文太长，则 IP 层需要分片，降低效率）

* TCP 和 UDP 区别

  面向（非）连接

  可靠性

  有序性

  速度

  量级（报文头）

* TCP 滑窗

  * RTT 和 RTO

    RTT：发送一个数据包到收到对应的 ACK 所花费的时间。

    RTO：重传时间间隔。

    为了防止数据包丢失，当 TCP 发送一个报文时，就启动重传计时器。

  * 滑动窗口

    如果接收端和发送端对数据包的处理速度不同，如何让双方达成一致？

    TCP 使用滑动窗口做流量控制（Window）与乱序重排，保证 TCP 的可靠性和流控特性。

    Window 字段的流量控制：用于接收方通知发送方自己还有多少缓冲区可以接收数据，发送方根据接收方的处理能力来发送数据，不会导致接收方处理不过来。

    滑动窗口机制体现了 TCP 面向字节流的设计（TCP 有一个缓冲，当应用程序传送的数据块太长，TCP 就可以把它划分短一些再传送。如果应用程序一次只发送一个字节，TCP 也可以等待积累有足够多的字节后再构成报文段发送出去）。

    ![TCP 发送方窗口](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/TCP%20%E5%8F%91%E9%80%81%E6%96%B9%E7%AA%97%E5%8F%A3.png)

    发送窗口只有收到发送窗口内字节的 ACK 确认，才会移动发送窗口的左边界。

    <已发送，已收到 ACK>**<已发送，未收到 ACK><未发送，但允许发送>**<未发送，但不允许发送>

    ![TCP 接收方窗口](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/TCP%20%E6%8E%A5%E6%94%B6%E6%96%B9%E7%AA%97%E5%8F%A3.png)

    接收窗口只有在前面所有的字节都确认的情况下才会移动左边界。当在前面还有字节未接收但收到后面字节的情况下，窗口不会移动，并不对后续字节确认，以此确保对端会对这些数据重传。
    
    <已接收>**<未接收但准备接收>**<未接收而且不准备接收>
    
    窗口数据计算过程：
    
    ![窗口数据计算过程](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/%E7%AA%97%E5%8F%A3%E6%95%B0%E6%8D%AE%E8%AE%A1%E7%AE%97%E8%BF%87%E7%A8%8B.png)
    
    接收方窗口：AdvertisedWindow = MaxRcvBuffer - (LastByteRcvd - LastByteRead)
    
    发送方窗口：EffecitveWindow = AdvertisedWindow - (LastByteSent - LastByteAcked)

* HTTP

  * 特点

    支持客户端、服务器模式

    简单快速（请求方法 + 路径）

    灵活（Content-type）：只要客户端和服务器知道如何处理的数据内容，任何类型的数据都可以通过 HTTP 发送

    无连接：无连接的含义是限制每次连接只处理一个请求。服务器处理完客户的请求，并收到客户的应答后即断开连接

    无状态：无状态是指协议对于事务处理没有记忆能力。缺少状态意味着如果后续处理需要前面的信息，则它必须重传，这样可能导致每次连接传送的数据量增大

  * HTTP 请求结构

    ![HTTP 请求结构](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/HTTP%20%E8%AF%B7%E6%B1%82%E7%BB%93%E6%9E%84.png)

    请求行：请求方法、URL、协议版本

    请求头部

    请求正文

  * HTTP 响应结构

    ![HTTP 响应结构](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/HTTP%20%E5%93%8D%E5%BA%94%E7%BB%93%E6%9E%84.png)

    状态行：协议版本、状态码、状态码描述

    响应头部

    响应正文

  * 在浏览器地址栏键入 URL，按下回车之后经历的流程

    DNS 解析（逐层查询 DNS 缓存，解析域名对应的 IP 地址 => 浏览器缓存，系统缓存，路由器缓存，IPS 服务器缓存，根域名服务器缓存，顶级域名服务器缓存）

    TCP 连接（TCP 三次握手）

    发送 HTTP 请求（HTTP 请求结构）

    服务器处理请求并返回 HTTP 报文（HTTP 响应结构）

    浏览器解析渲染页面

    连接结束（TCP 四次挥手）

  * HTTP 状态码

    1xx：指示信息，表示请求已接收，继续处理

    2xx：成功，表示请求已被成功接收、理解

    3xx：重定向，要完成请求必须进行更进一步的操作

    4xx：客户端错误，请求有语法错误或请求无法实现

    5xx：服务端错误，服务器未能实现合法的请求

  * 常见状态码

    200 OK：正常返回消息

    400 Bad Request：客户端请求有语法错误，不能被服务器所理解

    401 Unauthorized：请求未经授权，这个状态码必须和 WWW-Authenticate 报头域一起使用

    403 Forbidden：服务器收到请求，但是拒绝提供服务

    404 Not Found：请求资源不存在

    500 Internal Server Error：服务器发生不可预期的错误

    503 Server Unavailable：服务器当前不能处理客户端的请求，一段时间后可能恢复正常

* GET 请求和 POST 请求的区别

  HTTP 报文层面：GET 将请求信息放在 URL（? + 键值对，长度限制），POST 放在报文体中。

  数据库层面：GET 符合幂等性和安全性，POST 不符合。

  其他层面：GET 可以被缓存、被存储，而 POST 不行。

* 幂等性和安全性

  安全性：仅指该方法的多次调用不会产生副作用，不涉及传统意义上的“安全”。这里的副作用是指资源状态，即安全的方法不会修改资源状态，尽管多次调用的返回值可能不一样（被其他非安全方法修改过）。

  幂等性：是指该方法多次调用返回的效果（形式）一致，客户端可以重复调用并且期望同样的结果。

* Cookie 和 Session

  * Cookie

    * 特点

      是由服务器发给客户端的特殊信息，以文本的形式存放在客户端

      客户端再次请求的时候，会把 Cookie 回发

      服务器接收到后，会解析 Cookie 生成与客户端相对应的内容

    * Cookie 的设置以及发送过程

      HTTP Request -> HTTP Response + Set-Cookie -> HTTP Request + Cookie -> HTTP Response

  * Session

    * 特点

      服务器端的机制，使用一种类似于散列表的结构来保存信息

      解析客户端请求并操作 session id，按需保存状态信息

    * Session 的实现方式

      使用 Cookie 来实现：

      Response -> **Set-Cookie: JESSIONID=xxx**

      Request -> **Cookie: JESSIONID=xxx**

      使用 URL 回写来实现：URL 中携带 JESSIONID

  * Cookie 和 Session 的区别
    
    Cookie 数据存放在客户的浏览器上，Session 数据放在服务器上
    
    Session 相对于 Cookie 更安全
    
    若考虑减轻服务器负担，应当使用 Cookie


* HTTPS

  * SSL（Security Socket Layer）

    为网络通信提供安全及数据完整性的一种安全协议

    SSL 3.0 后更名为 TLS

    采用身份验证和数据加密保证网络通信的安全和数据的完整性

  * 加密方式

    对称加密

    非对称加密

    哈希算法

    数字签名

  * HTTPS 数据传输流程

    在整个 HTTPS 的传输过程中，主要分为两部分：首先是 HTTPS 的握手，然后是数据的传输。前者是建立一个 HTTPS 的通道，并确定连接使用的加密套件及数据传输使用的密钥。而后者主要使用密钥对数据加密并传输。

    Client Hello

    Server Hello，Certificate，Server Hello Done

    Certification Verity，Change Cipher Spec，Encrypted Handshake Message

    Change Cipher Spec，Encrypted Handshake Message

  * HSTS

    浏览器默认填充 http://，请求需要进行跳转，有被劫持的风险，可以使用 HSTS（HTTP Strict Transport Security）优化。

    HSTS 的作用是强制客户端（如浏览器）使用 HTTPS 与服务器创建连接。

    HSTS 最为核心的是一个 HTTP 响应头（Strict-Transport-Security），正是它可以让浏览器得知，在接下来的一段时间内，当前域名只能通过 HTTPS 进行访问，并且在浏览器发现当前连接不安全的情况下，强制拒绝用户的后续访问要求。

* HTTP 和 HTTPS 的区别

  HTTP：HTTP -> TCP -> IP

  HTTPS：HTTP -> SSL or TLS -> TCP -> IP

  HTTPS 需要到 CA 申请证书

  HTTPS 密文传输，HTTP 明文传输

  HTTPS 默认使用 443 端口，HTTP 使用 80 端口

  HTTPS = HTTP + 加密 + 认证 + 完整性保护，较 HTTP 安全

* Socket

  Socket 是对 TCP/IP 协议的抽象。

  Socket 通信流程

  ![Socket 通信流程](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/Socket%20%E9%80%9A%E4%BF%A1%E6%B5%81%E7%A8%8B.png)

  server -> socket()；bind()；listen()；accept()；recv()；close()
  
  client -> socket()；connect()；send()；close()