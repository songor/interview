# Offer 直通车——数据库

* 架构

  存储（文件系统）

  程序实例：存储管理、缓存机制、SQL 解析、日志管理、权限划分、容灾机制、索引管理、锁管理

* 索引

  * 为什么要使用索引

    避免全表扫描，快速查询数据。

    每种查找算法都只能应用于特定的数据结构之上，例如二分查找要求被检索数据有序，而二叉树查找只能应用于二叉查找树上，但是数据本身的组织结构不可能完全满足各种数据结构（理论上不可能同时将两列都按顺序进行组织）。所以，在数据之外，数据库系统还维护着满足特定查找算法的数据结构，这些数据结构以某种方式引用（指向）数据，这样就可以在这些数据结构上实现高级查找算法。

  * 什么样的信息能成为索引

    主键、唯一键以及普通键等。

  * 索引的数据结构

    二叉查找树、B-Tree、B+Tree、Hash、BitMap。

    一般使用磁盘 I/O 次数评价索引结构的优劣。

  * 二叉查找树

    二叉树、红黑树导致树高度非常高，逻辑上很近的节点（父子）物理上可能很远，无法利用局部性，IO 次数多。

  * B-Tree

    ![B-Tree](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/B-Tree.png)

    根节点至少包括两个孩子

    树中每个节点最多包含 m 个孩子（m >= 2）

    除根节点和叶节点外，其他每个节点至少有 ceil(m / 2) 个孩子

    所有叶节点都位于同一层

    假设每个非终端节点包含有 n 个关键字信息，其中：

    Ki(i=1...n) 为关键字，且关键字按顺序升序排序 K(i-1) < Ki

    关键字的个数 n 必须满足：ceil(m / 2) - 1 <= n <= m - 1

    非叶子节点的指针：P[1], P[2], ..., P[M]，其中 P[1] 指向关键字小于 K[1] 的子树，P[M] 指向关键字大于 K[M - 1] 的子树，其它 P[i] 指向关键字属于 (K[i - 1], K[i]) 的子树

  * B-Tree 特点

    每个节点中不仅包含数据的 key 值，还有 data 值。而每一个页的存储空间是有限的，如果 data 数据较大时将会导致每个节点（即一个页）能存储的 key 的数量很小，当存储的数据量很大时同样会导致 B-Tree 的深度较大，增大查询时的磁盘 I/O 次数，进而影响查询效率。

  * B+Tree

    ![B+Tree](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/B%2BTree.png)

    B+Tree 是 B-Tree 的变体：

    非叶子节点的子树指针与关键字个数相同

    非叶子节点的子树指针 P[i]，指向关键字值 [K[i], K[i + 1] 的子树

    非叶子节点仅用来索引，数据都保存在叶子节点中

    所有叶子节点均有一个链指针指向下一个叶子节点

  * B+Tree 更适合用来做存储索引

    B+Tree 的磁盘读写代价更低（容纳更多的关键字）

    B+Tree 的查询效率更加稳定（从根节点到叶子节点）

    B+Tree 更有利于对数据库的扫描（范围查询）

  * B+Tree 拾遗

    数据库系统的设计者巧妙利用了磁盘预读原理，将一个节点的大小设为等于一个页，这样每个节点只需要一次 I/O 就可以完全载入。

    所有数据记录节点都是按照键值大小顺序存放在同一层的叶子节点上，而非叶子节点上只存储 key 值信息，这样可以大大加大每个节点存储的 key 值数量，降低 B+Tree 的高度。

    通常在 B+Tree 上有两个头指针，一个指向根节点，另一个指向关键字最小的叶子节点，而且所有叶子节点（即数据节点）之间是一种链式环结构。因此可以对 B+Tree 进行两种查找运算：一种是对于主键的范围查找和分页查找，另一种是从根节点开始，进行随机查找。

  * Hash

    仅仅能满足 =，IN，不能使用范围查询

    Hash 值无法排序

    不能利用部分索引键查询（对于组合索引，Hash 索引在计算 Hash 值的时候是组合索引键合并后再一起计算 Hash 值，而不是单独计算 Hash 值，所以通过组合索引的前面一个或几个索引键进行查询的时候，Hash 索引也无法被利用）

    可能会遇到大量 Hash 值相等的情况，查询效率不稳定

  * BitMap

* 密集索引和稀疏索引

  ![密集索引和稀疏索引](https://github.com/songor/interview/blob/master/Offer%20%E7%9B%B4%E9%80%9A%E8%BD%A6/picture/%E5%AF%86%E9%9B%86%E7%B4%A2%E5%BC%95%E5%92%8C%E7%A8%80%E7%96%8F%E7%B4%A2%E5%BC%95.png)

  MyISAM（稀疏索引），索引和数据分开存储，叶子节点保存行数据的物理地址。

  主索引和辅助索引在结构上没有任何区别，只是主索引要求 key 是唯一的，而辅助索引的 key 可以重复。

  InnoDB（有且仅有一个密集索引），行数据存储在主键索引的叶子节点上（主键索引和数据存储在同一个文件中）。

  若一个主键被定义，该主键则作为密集索引。

  若没有主键被定义，该表的第一个唯一非空索引则作为密集索引。

  若不满足以上条件，InnoDB 内部会生成一个隐藏主键作为密集索引。

  非主键索引（辅助索引）存储相关键值和其对应的主键值（不存储行数据的物理地址），包含两次查找（首先检索辅助索引获得主键，然后用主键到主索引中检索获得记录）。

* 如何定位并优化慢查询 sql

  * 根据慢日志定位慢查询 sql

    `show variables like '%query%';`

    slow_query_log、slow_query_log_file、long_query_time

    `set global slow_query_log = on;`

    `set global long_query_time = 1;`

    `show status like '%slow_queries%';`

  * 使用 explain 等工具分析 sql

    * type

      all、index、range、ref、eq_ref、const

      all：不使用任何索引，进行全表扫描，性能最差

      index：查找所有的索引树，比 all 要快的多，因为索引文件要比数据文件小的多

      range：查找某个索引的部分索引，一般在 where 子句中使用 <、>、in、between 等关键词。只检索给定范围的行，属于范围查找

      ref：查找非唯一性索引，返回匹配某一条件的多条数据；精确查询

      eq_ref：查找唯一性索引，返回的数据至多一条；精确查询

      const：查找主键索引，返回的数据至多一条；精确查询

    * extra

      Using filesort：表示 MySQL 会对结果使用一个外部索引排序，而不是从表里按索引次序读到相关内容。可能在内存或者磁盘上进行排序。MySQL 中无法利用索引完成的排序操作称为“文件排序”。

      Using temporary：表示 MySQL 在对查询结果排序时使用临时表。常见于排序 order by 和分组查询 group by。

  * 修改 sql 或者尽量让 sql 走索引

    字段使用函数，将无法使用索引

    Join 语句中 Join 条件字段类型不一致的时候 MySQL 无法使用索引

    复合索引的情况下，不满足最左前缀原则，则不会使用索引

    以 % 开头的 like 查询

    数据类型出现隐式转换的时候也不会使用索引

  * 拾遗

    避免 % 开头的 like 模糊查询 like '%name'

    exists 替代 in

    避免在 where 子句中使用 or 来连接条件，用 union all

    避免在 where 子句中对字段进行表达式操作

    避免在 where 子句中对字段进行函数操作

    考虑在 where 及 order by 涉及的列上建立索引

    尽量避免在 where 子句中使用 != 或 <> 操作符

    尽量避免在 where 子句中对字段进行 null 值判断

    何地方都不要使用 select \*，用具体的字段列表代替 \*，不要返回用不到的任何字段

    select count(id) 索引选择（没有选择主键索引）：

    查询优化器尽可能使用索引，并且使用最严格的索引来消除尽可能多的数据行，最终目标是提交 select 语句查找数据行，而不是排除数据行（优化器试图排除数据行的原因在于排除数据行的速度越快，那么找到与条件匹配的数据行就越快）。

    在 InnoDB 下，密集索引的叶子节点存放了其他列的数据，效率比稀疏索引低。

* 联合索引的最左匹配原则

  MySQL 会一直向右匹配直到遇到范围查询（>、<、between、like）就停止匹配，比如 a = 3 and b = 4 and c > 5 and d = 6 如果建立 (a, b, c, d) 顺序的索引，d 是用不到索引的（因为 c 是一个范围，在这个范围中 d 是无序的），如果建立 (a, b, d, c) 的索引则都可以用到，a，b，d 的顺序可以任意调整。

  = 和 in 可以乱序，比如 a = 1 and b = 2 and c = 3 建立 (a, b, c) 索引可以任意顺序，MySQL 的查询优化器会帮你优化成索引可以识别的形式。

  idx_a_b(a, b) -> 只对 where a = 'x' 或 where a = 'x' and b = 'y' 有效。

  （MySQL 依据联合索引第一个索引字段来构建B+Tree）首先会根据第一个索引字段对数据进行排序，在此基础上，再根据第二个索引字段对数据排序。第一个索引字段是绝对有序的，第二个字段就不是有序的了。因此使用第二个索引字段进行条件判断是用不到索引的。

* 索引是建立得越多越好吗

  数据量小的表不需要建立索引，建立会增加额外的索引开销。

  数据变更需要维护索引，因此更多的索引意味着更多的维护成本，也意味着需要更多的空间。

* 锁

  MyISAM 与 InnoDB 关于锁方面的区别

  * MyISAM 默认使用表级锁，不支持行级锁

    MyISAM 不支持事务

    lock tables xxx read | write; -- 加锁

    unlock tables; -- 释放锁

    读锁（select）也叫做共享锁，写锁（insert、delete、update）叫做排它锁。在共享锁上可添加共享锁，会阻塞排它锁，而排它锁会阻塞共享锁和排它锁。

    可以为 select 添加排它锁（select ... for update）

    MyISAM 适合的场景：频繁执行全表 count 语句（使用一个变量保存了整个表的行数）；对数据进行增删改的频率不高，查询非常频繁、没有事务

  * InnoDB 默认使用行级锁，也支持表级锁

    InnoDB 支持事务二阶段提交

    show variables like 'autocommit'; -- 事务默认自动提交

    select ... ; -- InnoDB 对其进行了优化，没有添加共享锁

    select ... lock in share mode; -- 显式添加共享锁

    InnoDB 在 sql 没有用到索引的时候，使用表级锁（行级锁都是基于索引的）

    InnoDB 也支持表级意向锁 IS、IX

    InnoDB 适合的场景：数据增删改查都相当频繁，可靠性要求比较高，要求支持事务

  * 拾遗

    InnoDB 为了支持多粒度（表锁与行锁）的锁并存，引入意向锁

    意向锁是表级锁，可分为意向共享锁（IS 锁）和意向排他锁（IX 锁）

    事务在请求 S 锁和 X 锁前，需要先获得对应的 IS、IX 锁

    意向锁产生的主要目的是为了处理行锁和表锁之间的冲突，用于表明“某个事务正在某一行上持有了锁，或者准备去持有锁”

* 事务

  * 数据库事务的四大特性

    原子性（Atomic）、一致性（Consistency）、隔离性（Isolation）、持久性（Durability）

  * 事务隔离级别

    select @@tx_isolation; -- 查看事务隔离级别

    set session transaction isolation level read uncommitted | read committed | repeatable read（默认） | serializable; -- 设置事务隔离级别

    start transaction; ... rollback | commit; -- 手动事务

  * 事务并发访问引起的问题及如何避免

    * 更新丢失

      A 事务的更新覆盖了 B 事务的更新；MySQL 所有事务隔离级别在数据库层面上均可避免（锁）。

    * 脏读

      A 事务读到 B 事务未提交的更改数据；READ-COMMITTED 事务隔离级别以上可避免。

    * 不可重复读

      A 事务读到 B 事务已经提交的更改数据；REPEATABLE-READ 事务隔离级别以上可避免。

    * 幻读

      A 事务读到 B 事务提交的新增（删除）数据；SERIALIZABLE 事务隔离级别可避免。

  * InnoDB 可重复读隔离级别下如何避免幻读

    表象：快照读（非阻塞读），伪 MVCC（MVCC 是多版本共存，InnoDB 中多版本是串行）

    内在：next-key 锁（行锁 + GAP 锁）

    * 当前读和快照读

      当前读（加锁，读取最新版本）：select ... lock in share mode;（扫描到的索引记录上加共享的 next-key lock，还有主键聚集索引上加排它锁） select ... for update;（在扫描到的索引记录上加排它的 next-key lock，还有主键聚集索引上加排它锁） update; delete;（在扫描到的索引记录上加 next-key lock，还有主键聚集索引上加排它锁） insert;

      快照读（不加锁，可能是过期的数据，在事务隔离级别不为 SERIALIZABLE 成立）：select ... ;

      在 READ-COMMITTED 事务隔离级别中，当前读（每个 select 都会创建快照）和快照读都取到最新版本的数据。在 REPEATABLE-READ 事务隔离级别中，当前读能取到最新版本的数据，快照读能否取到最新版本的数据关键在于事务首次快照读（创建快照）的时机。

    * GAP 锁

      防止同一事务的两次当前读出现幻读，仅存在于 REPEATABLE-READ 和 SERIALIZABLE。

    * 对主键索引或者唯一索引会用 GAP 锁吗

      如果 where 条件全部命中，则不会用 GAP 锁，只会加记录锁（先给 unique key 加 record lock，再给 primary key 加 record lock）。

      如果 where 条件部分命中或者全不命中，则会加 GAP 锁。

    * GAP 锁会用在非唯一索引或者不走索引的当前读中

      非唯一索引（对要修改位置的周边加 GAP 锁）

      10, 11, 13 and 20

      (negative infinity, 10], (10, 11], (11, 13], (13, 20], (20, positive infinity)

      不走索引（对所有的间隙加 GAP 锁，类似于锁表，要避免此种情况）
      
    * 拾遗

      简单的 insert 会在 insert 的行对应的索引记录上加一个排它锁，这是一个 record lock，并没有 gap，所以并不会阻塞其他事务在 gap 间隙里插入记录。

      不过在 insert 操作之前，还会加一种锁，官方文档称它为 insertion intention gap lock，也就是意向的 gap 锁。这个意向 gap 锁的作用就是预示着当多事务并发插入相同的空隙时，只要插入的记录不是间隙中的相同位置，则无需等待其他事务就可完成，这样就使得 insert 操作无须加真正的 gap lock。

  * RC、RR 级别下的 InnoDB 的非阻塞读如何实现

    核心原理：

    写任务发生时，将数据克隆一份，以版本号区分

    写任务操作新克隆的数据，直至提交

    并发读任务可以继续读取旧版本的数据，不至于阻塞

    提高并发的演进思路：

    普通锁，本质是串行执行

    读写锁，可以实现读读并发

    数据多版本，可以实现读写并发

    InnoDB 实现：

    数据行里的内部属性： DB_TRX_ID（记录每一行最近一次修改它的事务 ID）、DB_ROLL_PTR（记录指向回滚段 undo 日志的指针）、DB_ROW_ID（单调递增的行 ID） 字段

    redo 日志：将修改行为先写到 redo 日志里，再定期将数据刷到磁盘上，这样能极大提高性能。这里的架构设计方法是，随机写优化为顺序写。redo 日志用于保障已提交事务的 ACID 特性。

    undo 日志：数据库事务未提交时，会将事务修改数据的镜像（即修改前的旧版本）存放到 undo 日志里，当事务回滚时，或者数据库奔溃时，可以利用 undo 日志撤销未提交事务对数据库产生的影响。更细节的，对于 insert 操作，undo 日志记录新数据的 PK(ROW_ID)，回滚时直接删除；对于 delete/update 操作，undo 日志记录旧数据 row，回滚时直接恢复。undo 日志用于保障未提交事务不会对数据库的 ACID 特性产生影响。

    回滚段：存储 undo 日志的地方。

    MVCC 就是通过“读取旧版本数据”来降低并发事务的锁冲突，提高任务的并发度。

    consistent read view：在事务执行期间，事务能看到怎样的数据。

    快照：在 InnoDB 中，每个事务都有一个唯一的事务 ID（transaction id），在事务开始的时候向 InnoDB 的事务系统申请，按申请的顺序严格递增。每行数据都有多个版本，每次事务更新数据的时候，都会生成一个新的数据版本，事务会把自己的 transaction id 赋值给这个数据版本的事务 ID，记为 row trx_id（每个数据版本都有对应的 row trx_id）。同时也要逻辑保留旧的数据版本，通过新的数据版本和 undolog 可以计算出旧的数据版本。

  * 拾遗

    record lock 锁住的永远是索引，而非记录本身

    gap lock 在索引记录的间隙中加锁，并不包括索引记录本身

* 关键语法

  * GROUP BY（Using temporary）

    对查询结果进行分组统计

    满足“SELECT 子句中的列名必须为分组列或列函数”（仅限定于一张表，如果是多张表，SELECT 子句中的列名还可以是其他表的列）

    列函数对于 GROUP BY 子句定义的每个组返回一个结果

    查询所有同学的学号、选课数、总成绩：

    `select student_id, count(course_id), sum(score) from score group by student_id;`

    查询所有同学的学号、姓名、选课数、总成绩：

    `select s.student_id, stu.name, count(s.course_id), sum(s.score) from score s, student stu where s.student_id = stu.student_id group by s.student_id;`

  * HAVING

    通常与 GROUP BY 子句一起使用，指定过滤条件。如果没有 GROUP BY 子句，它的行为和 WHERE 子句一样

    WHERE 过滤行，HAVING 过滤组

    同一个 sql 的顺序为 WHERE > GROUP BY > HAVING

    查询平均成绩大于 60 分的同学的学号和平均成绩：

    `select student_id, avg(score) from score group by student_id having avg(score) > 60;`

    查询没有学全所有课程的同学的学号、姓名：

    `select stu.student_id, stu.name from student stu, score s where stu.student_id = s.student_id group by s.student_id having count(\*) < (select count(\*) from course);`

  * 统计相关

    COUNT、SUM、MAX、MIN、AVG
