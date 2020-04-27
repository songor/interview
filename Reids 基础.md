# Reids 基础

### NoSQL（Not Only SQL，非关系型数据库）

* 为什么需要 NoSQL

  * High performance - 高并发读写

  * Huge Storage - 海量数据的高效率存储和访问

  * High Scalability && High Availability - 高扩展性和高可用性

### Redis

* 安装

  ./bin/redis-server ./redis.conf

  ps -ef | grep -i redis

  ./bin/redis-cli shutdown

  ./bin/redis-cli

  flushall

* Jedis

  vim /etc/sysconfig/iptables

  service iptables restart

  ```java
  Jedis jedis = new Jedis(ip, port);
  
  JedisConfig config = new JedisConfig();
  config.setXXX();
  JedisPool pool = new JedisPool(config, ip, port);
  Jedis jedis = pool.getResource();
  ```

* 数据结构

  * String

    二进制；Value 最多可以容纳的数据长度是 512 MB。

    set company imooc

    get company

    getset company baidu：将给定 key 的值设为 value ，并返回 key 的旧值（old value）

    del company

    incr number / incrby number 5

    decr number / decrby number 5

    append company .com

  * Hash

    String Key 和 String Value 的 Map 容器；每个 Hash 可以存储 2^32 - 1（4294967295）个键值对。

    hset users username jack / hset users age 18 / hmset customers username tom age 21

    hget users username / hmget users username age / hgetall users

    hdel customers username age

    del customers

    hincrby users age 5

    hexists users username

    hlen users：获取哈希表中字段的数量

    hkeys users / hvals users：获取哈希表中所有字段 / 获取哈希表中所有值

  * List

    ArrayList 使用数组方式；LinkedList 使用双向链表方式。

    lpush lists a b c / rpush lists 1 2 3 / lpushx lists d / rpushx lists 4：lpushx 如果指定的列表是空，则不插入

    lrange lists 0 5 / -1

    lpop lists / rpop lists：移除并获取列表的第一个元素 / 移除并获取列表的最后一个元素

    llen lists

    lrem lists 2 a / lrem lists -2 a / lrem lists 0 a：根据参数 count 的值，移除列表中与参数 value 相等的元素

    count > 0：从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count

    count < 0：从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值

    count = 0：移除表中所有与 value 相等的值

    lset lists 2 c：将列表 key 下标为 index 的元素的值设置为 value

    linsert lists before c b / linsert lists after c d：将值 value 插入到列表 key 当中，位于值 pivot 之前或之后

    rpoplpush list1 list2：移除列表的最后一个元素，并将该元素添加到另一个列表并返回

  * Set

    sadd sets a b c

    srem sets a c

    smembers sets

    sismember sets b

    sdiff set1 set2 / sinter set1 set2 / sunion set1 set2：差集 / 交集 / 并集

    scard sets：获取集合的成员数

    srandmemeber sets：返回集合中一个或多个随机数

    sdiffstore diffsets set1 set2 / sinterstore intersets set1 set2 / sunionstore unionsets set1 set2：返回给定所有集合的差集、交集、并集并存储在 destination 中

  * Sorted Set

    zadd sorts 5 a 6 b 7 c

    zscore sorts a

    zcard sorts

    zrem sorts a

    zrange sorts 0 -1 / zrange sorts 0 -1 withscores / zrevrange sorts 0 -1 withscores

    zremrangebyrank sorts 0 4 / zremrangebyscore sorts 6 7：返回有序集中，指定区间内的成员，按分数值递增来排序 / 返回有序集中，指定区间内的成员，按分数值递减来排序 / 移除有序集中，指定排名（rank）区间内的所有成员 / 移除有序集中，指定分数（score）区间内的所有成员

    zrangebyscore sorts 5 7 / zrangebyscore sorts 5 7 with scores / zrangebyscore sorts 5 7 with scores limit 0 2

    zincrby sorts 1 a

    zcount sorts 5 7：计算有序集合中指定分数区间的成员数量

* Keys 的通用命令

  keys * / keys prefix*：查找所有符合给定模式（pattern）的 key

  del key1 key2

  exists key

  rename key newkey

  expire key 10(s)

  ttl key：以秒为单位，返回给定 key 的剩余生存时间（TTL，time to live)

  type key

* Redis 特性

  * 多数据库

    一个 Redis 实例最多包含 16 个数据库，客户端默认连接 0 数据库，可以通过 select 选择连接哪个数据库。

    select 0 > keys * > move key 1

  * 事务

    串行化隔离机制。

    Redis 命令在事务中可能会执行失败，但是 Redis 事务不会回滚，而是继续执行余下的命令。

    multi：用于标记一个事务块的开始

    incr number

    incrby number 5

    exec / discard：提交 / 取消事务

* Redis 持久化

  * RDB

    By default Redis saves snapshots of the dataset on disk, in a binary file called `dump.rdb`. You can configure Redis to have it save the dataset every N seconds if there are at least M changes in the dataset, or you can manually call the [SAVE](https://redis.io/commands/save) or [BGSAVE](https://redis.io/commands/bgsave) commands.

    save 900 1

    save 300 10

    save 60 10000

  * AOF

    appendonly yes

    appendfsync always: fsync every time a new command is appended to the AOF. Very very slow, very safe.

    appendfsync everysec: fsync every second. Fast enough, and you can lose 1 second of data if there is a disaster.
  
    appendfsync no: Never fsync, just put your data in the hands of the Operating System. The faster and less safe method. Normally Linux will flush data every 30 seconds with this configuration, but it's up to the kernel exact tuning.