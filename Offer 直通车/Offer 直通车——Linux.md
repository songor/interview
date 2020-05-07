# Offer 直通车——Linux

* Linux 的体系结构

  体系结构主要分为用户态（用户上层活动）和内核态

  内核：本质是一段管理计算机硬件设备的程序

  系统调用（man 2 syscalls）：内核的访问接口，是一种不能再简化的操作（类似于原子操作）

  公用函数库：系统调用的组合

  Shell：命令解释器，可编程 -> ls / which ls / cat / less / more / vi / vim

  echo $SHELL

  cat /etc/shells

  chsh -s /bin/bash

* 查找特定的文件

  find path [options] params

  find -name "target.txt" --当前目录递归查找

  find / -name "target.txt" --指定目录递归查找

  find ~ -name "target\*" / find ~ -iname "target\*" --模糊查询

  man find

  find . -perm 755 --当前目录（及子目录）属性为 755 的文件

  find . -user root --当前目录（及子目录）属主为 root 的文件

  find /var -mtime -5 --/var 下更改时间在 5 天内

  find /var -mtime +3 --/var 下更改时间在 3 天前

  （-a 最近一次访问时间；-m 最近一次内容修改时间；-c 最近一次属性修改时间）

  （-atime 天；-amin 分钟）

  find /etc -type l --/etc 下文件类型为 l 的链接文件

  find . -type f | xargs ls -l --查找出文件并查看其详细信息

  （由于很多命令不支持 | 管道来传递参数，find . -type f | ls -l 这个命令式错误的）

  （-a and；-o or；-not）

  find . -size 2M（-2M；+2M） --等于、小于、大于 2 M

* 检索文件内容（查找）

  grep [options] pattern file

  grep "imooc" target*

  |（管道操作符）：可将指令连接起来，前一个指令的输出作为后一个指令的输入。注意，只处理前一个命令正确输出，不处理错误输出；右边命令必须能够接收标准输入流，否则传递过程中的数据会被抛弃。sed，awk，grep，cut，head，top，less，more，wc，join，sort，split

  find ~ | grep "target"

  grep 'partial\[true\]' info.log | grep -o 'engine\[\[0-9a-z\]\*\]' --正则

  （-o 只输出符合 RE 的字符串）

  ps -ef | grep java | grep -v "grep" --过滤

  （-v 逆反模式）

  （-i 忽略大小写）

  （-n 打印行号）

  grep '\bboot\b' logs.txt （\b 单词锁定符，只匹配 boot）

  ifconfig eth0 | grep -E "(\[0-9\]{1,3}\.){3}" （-E 扩展的正则表达式 egrep）

  cat /etc/passwd | grep -E "boy|omc"（| 过滤多个关键字）

  grep -n 'yum' -A 3 logs.txt （匹配内容外 A - 后 n 行；B - 前 n 行；C - 上下 n 行）

* 对日志内容做统计（文本分析）

  awk [options] 'pattern condition {action}' file

  （pattern - 正则表达式，用斜杠括起来，表示 awk 在数据中查找的内容，包含两种特殊模式 BEGIN 和 END；action - 在找到匹配内容时所执行的一系列命令）

  一次读取一行文本，按输入分隔符进行切片，切成多个组成部分；将切片直接保存在内建的变量中，$1,$2...（$0 表示行的全部）；支持对单个切片的判断，支持循环判断，默认分隔符为空格

  awk '{print $1,$3}' demo.txt

  awk '$1=="tcp" && $2==1 {print $0}' demo.txt

  awk '($1=="tcp" && $2==1) || NR==1 {print $0}' demo.txt --添加表头

  awk -F "," '{print $2}' demo.txt --逗号分隔符

  grep 'partial\[true\]' info.log | grep -o 'engine\[\[0-9a-z\]\*\]' | awk '{arr[$1]++} END {for(i in arr) print i"\t"arr[i]}'

  awk '/root/' /etc/passwd --没有指定 action，默认输出每行的内容

  awk -F ':' '/root/ {print $7}' /etc/passwd ---F 设置输入域分隔符

  awk -F ':' '{print "filename:" FILENAME ",linenumber:" NR ",columns:" NF ",linecontent:" $0}' /etc/passwd --FILENAME - awk 浏览的文件名；NR - 已读的记录数；NF - 共有多少列

  awk -F ':' 'NR==2 {print "filename:" FILENAME,$0}' /etc/passwd

  awk -F ':' '{print $NF}' /etc/passwd --$NF - 最后一列；$1 - 第一列；$(NF-1) - 倒数第二列

  awk -F ':' '{if(NR<20 && NR>10) print $1}' /etc/passwd

  cat /etc/passwd | awk -F ':' '{print $1"\t"$7}'

  cat /etc/passwd | awk -F ':' 'BEGIN {print "name,shell"}  {print $1","$7} END {print "blue,/bin/nosh"}' --先执行 BEGIN，然后读取文件，读入有 /n 换行符分割的一条记录，然后将记录按指定的域分隔符划分域，填充域，$0 表示所有域，$1 表示第一个域，$n 表示第 n 个域，随后开始执行模式所对应的动作 action。接着开始读入第二条记录...直到所有的记录都读完，最后执行 END 操作。

  awk '{count++;print $0;} END {print "user count is ", count}' /etc/passwd --count 是自定义变量

* 批量替换文本内容（编辑）

  一次处理一行内容；处理时，把当前处理的行储存在临时缓冲区中，称为模式空间，接着用 sed 命令处理缓冲区中的内容。处理完成后，把缓冲区的内容送往屏幕，接着处理下一行。文件内容并没有改变，除非你使用重定向储存输出。

  sed [option] 'sed command' filename

  option - sed 提供的命令行参数

  sed command - 由 pattern 和 procedure 组成；pattern - / 分隔的正则表达式；procedure - 一串编辑指令

  sed 's/^Str/String/' demo.java --结果输出到终端，s - 取代

  sed -i 's/^Str/String/' demo.java / sed -i 's/\\.$/\;/' demo.java --替换原文件

  （-i 在源文件上修改并取代源文件）

  sed -i 's/A/B/g' demo.java --替换每一行的全部匹配项

  （g 在行内进行全局替换）

  sed -i '/^ *$/d' demo.java / sed -i '/Object/d' demo.java --删除行

  sed -n '3p' /var/log/yum.log --只打印第三行，-n 取消默认的完整输出，只要需要的；p 打印行

  sed -n '/root/p' yum.log --过滤特定字符串

  sed -n '/root/=' yum.log --仅显示匹配字符串的行号

  sed -e 's/PEK/bj' -e 's/TSA/tb' test.txt ---e 允许多项编辑；s 用一个字符串替换另外一个

