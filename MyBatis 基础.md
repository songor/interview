# MyBatis 基础

### JDBC

* 模板

  ```java
  Class.forName("com.mysql.jdbc.Driver");
  Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/message", "root", "root");
  String sql = "";
  PreparedStatement statement = connection.prepareStatement(sql);
  ResultSet rs = statement.executeQuery();
  while (rs.next()) {
      rs.getString("");
  }
  rs.close();
  statement.close();
  connection.close();
  ```

### MyBatis

* 基础知识
  * SqlSession 作用
    * 向 SQL 语句传入参数
    * 执行 SQL 语句
    * 获取执行 SQL 语句的结果
    * 事务控制
    
  * SqlSession 如何获取
    * 通过配置文件获取数据库连接相关信息
    * 通过配置信息构建 SqlSessionFactory
    * 通过 SqlSessionFactory 打开数据库会话
    
  * 代码
  
    ```java
    Reader reader = Resources.getResourceAsReader("Configuration.xml");
    SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
    SqlSession sqlSession = factory.openSession();
    sqlSession.close();
    ```
  
    ```xml
    <!-- Configuration.xml -->
    <configuration>
    
      <environments default="development">
        <environment id="development">
          <transactionManager type="JDBC">
            <property name="" value=""/>
          </transactionManager>
          <dataSource type="UNPOOLED">
            <property name="driver" value="com.mysql.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://127.0.0.1:3306/message"/>
            <property name="username" value="root"/>
            <property name="password" value="root"/>
          </dataSource>
        </environment>
      </environments>
    
      <mappers>
        <mapper resource="Message.xml"/>
        <mapper resource="Command.xml"/>
        <mapper resource="CommandContent.xml"/>
      </mappers>
    
    </configuration>
    ```
  
    ```xml
    <!-- Message.xml -->
    <mapper namespace="Message">
    
      <resultMap type="bean.Message" id="MessageResult">
        <id column="ID" jdbcType="INTEGER" property="id"/>
        <result column="COMMAND" jdbcType="VARCHAR" property="command"/>
        <result column="DESCRIPTION" jdbcType="VARCHAR" property="description"/>
        <result column="CONTENT" jdbcType="VARCHAR" property="content"/>
      </resultMap>
    
      <select id="queryMessages" parameterType="bean.Message" resultMap="MessageResult">
        select <include refid="columns"/> from MESSAGE
        <where>
        	<if test="command != null and !&quot;&quot;.equals(command.trim())">
    	    	and COMMAND = #{command}
    	    </if>
    	    <if test="description != null and !&quot;&quot;.equals(description.trim())">
    	    	and DESCRIPTION like '%' #{description} '%'
    	    </if>
        </where>
      </select>
    
      <sql id="columns">ID,COMMAND,DESCRIPTION,CONTENT</sql>
    
      <delete id="deleteOne" parameterType="int">
      	delete from MESSAGE where ID = #{_parameter}
      </delete>
    
      <delete id="deleteBatch" parameterType="java.util.List">
    	delete from MESSAGE where ID in (
      		<foreach collection="list" item="item" separator=",">
      			#{item}
      		</foreach>
      	)
      </delete>
    
    </mapper>
    ```
  
    ```java
    Message message = new Message();
    message.setCommand("XXX");
    message.setDescription("XXX");
    sqlSession.selectList("Message.queryMessages", message);
    
    sqlSession.delete("Message.deleteOne", id);
    sqlSession.commit();
    
    List<Integer> ids = new ArrayList<>();
    ids.add(1);
    ids.add(2);
    sqlSession.delete("Message.deleteBatch", ids);
    sqlSession.commit();
    ```
  
  * OGNL
  
    * 从集合中取出一条数据
  
      数组：array[索引]
  
      List：list[索引]
  
      Map：key.属性名
  
    * 利用 foreach 标签从集合中取出数据
    
      ```xml
      <foreach collection="array" index="i" item="item">
      ```
  
      数组：i - 索引，item - value
      
      List：i - 索引，item - value
      
      Map：i - key，item - value
  
  * 一对多关系
  
    ```xml
    <!-- Command.xml -->
    <mapper namespace="Command">
    
      <resultMap type="bean.Command" id="CommandResult">
        <id column="C_ID" jdbcType="INTEGER" property="id"/>
        <result column="NAME" jdbcType="VARCHAR" property="name"/>
        <result column="DESCRIPTION" jdbcType="VARCHAR" property="description"/>
        <collection property="contents" resultMap="CommandContent.ContentResult"/>
      </resultMap>
    
      <select id="queryCommands" parameterType="bean.Command" resultMap="CommandResult">
        select a.ID C_ID, a.NAME, a.DESCRIPTION, b.ID, b.CONTENT, b.COMMAND_ID
        from COMMAND a left join COMMAND_CONTENT b
        on a.ID = b.COMMAND_ID
        <where>
        	<if test="name != null and !&quot;&quot;.equals(name.trim())">
    	    	and a.NAME = #{name}
    	    </if>
    	    <if test="description != null and !&quot;&quot;.equals(description.trim())">
    	    	and a.DESCRIPTION like '%' #{description} '%'
    	    </if>
        </where>
      </select>
    
    </mapper>
    ```
  
    ```xml
    <!-- CommandContent.xml -->
    <mapper namespace="CommandContent">
    
      <resultMap type="bean.CommandContent" id="ContentResult">
        <id column="ID" jdbcType="INTEGER" property="id"/>
        <result column="CONTENT" jdbcType="VARCHAR" property="content"/>
        <result column="COMMAND_ID" jdbcType="VARCHAR" property="commandId"/>
        <!-- association property="command" resultMap="Command.CommandResult" /> -->
      </resultMap>
    
    </mapper>
    ```
  
    ```java
    Command command = new Command();
    command.setName(name);
    command.setDescription(description);
    sqlSession.selectList("Command.queryCommands", command);
    ```
  
  * 其他标签
  
    ```xml
    <mapper namespace="Message">
    
      <update id="updateMessage" parameterType="bean.Message">
        update MESSAGE
        <set>
        	<if test="command != null and !&quot;&quot;.equals(command.trim())">
    	    	COMMAND = #{command},
    	    </if>
    	    <if test="description != null and !&quot;&quot;.equals(description.trim())">
    	    	DESCRIPTION = #{description},
    	    </if>
        </set>
      </update>
    
      <trim prefix="where" prefixOverrides="and|or">
        <!-- 替代 where 标签 -->
      </trim>
    
      <trim prefix="set" suffixOverrides=",">
        <!-- 替代 set 标签 -->
      </trim>
    
      <!-- 替换 if -->
      <choose>
        <when test=""></when>
        <when test=""></when>
        <otherwise></otherwise>
      </choose>
    
    </mapper>
    ```
  
  * 标签总结
  
    * 定义 SQL 语句：insert、delete、update、select
    * 配置 Java 对象属性和查询结果集中列名对应关系：resultMap
    * 控制动态 SQL 拼接：foreach，if，choose、when、otherwise
    * 格式化输出：where、set、trim
    * 配置关联关系：collection、association
    * 定义常量：sql
    * 引用常量：include

  * 容易混淆概念
  
    * resultMap 和 resultType
  
      resultType 自动映射属性和列名，大小写不敏感
  
    * parameterMap 和 parameterType
    
      parameterMap 已不被推荐使用

    * #{} 和 ${}
    
      select * from TABLE where id = #{id} / ${id}
    
      预编译，#{} 替换为 ?
    
      ${} 替换为 ABC，用在 order by #{id}
    
  * 常见问题
  
    * 获取自增主键值
  
      ```xml
      <!-- Message.xml -->
      <mapper namespace="Message">
      
        <insert id="insertMessage" useGeneratedKeys="true" keyProperty="id" parameterType="bean.Message">
          insert into MESSAGE(COMMAND, DESCRIPTION) values(#{command}, #{description})
        </insert>
      
      </mapper>
      ```
  
    * 乱码
    
      请求参数：
    
      .java 文件本身编码
    
      .jsp 设置的编码（\<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /\>）
    
      req.setCharacterEncoding("UTF-8");
    
      get 请求，tomcat 编码
    
      数据库：
    
      jdbc:mysql://127.0.0.1:3306/database?useUnicode=true\&amp;characterEncoding=utf8
    
      建立数据库及表编码
  
* 强化知识

  * 接口式编程

    * 实现

      ```java
      package com.package;
      
      public interface IMessage() {
          public List<Message> queryMessages(Message message);
      }
      ```

      ```xml
      <!-- Message.xml -->
      <mapper namespace="com.package.IMessage">
      
        <select id="queryMessages" parameterType="bean.Message" resultMap="MessageResult"></select>
      
      </mapper>
      ```

      ```java
      IMessage iMessage = sqlSession.getMapper(IMessage.class);
      iMessage.queryMessages(message);
      ```

    * 原理
  
      * 动态代理
  
        MapperProxy implements InvocationHandler
  
        @Override invoke()
  
        Proxy.newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
  
      * 推导
  
        Imessage iMessage = sqlSession.getMapper() => Proxy.newProxyInstance()
  
        iMessage.queryMessages() => MapperProxy.invoke()
  
        MapperProxy.invoke() 方法中包含 sqlSession.selectList(namespace.id, parameter);
  
      * 源码
    
        MapperRegistry：`knownMappers.put(type, new MapperProxyFactory<>(type));`
    
        MapperProxyFactory：`final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);` 
      
        MapperProxy：`final MapperMethod mapperMethod = cachedMapperMethod(method);`
  
  * 分页
  
    * 简单实现
    
      ```xml
      <mapper namespace="com.package.IMessage">
      
        <select id="queryMessages" parameterType="java.util.Map" resultMap="MessageResult">
          select <include refid="columns"/> from MESSAGE
          <where>
          	<if test="message.command != null and !&quot;&quot;.equals(message.command.trim())">
      	    	and COMMAND = #{message.command}
      	    </if>
      	    <if test="message.description != null and !&quot;&quot;.equals(message.description.trim())">
      	    	and DESCRIPTION like '%' #{message.description} '%'
      	    </if>
          </where>
          order by ID limit #{page.dbIndex}, #{page.dbNumber}
        </select>
      
        <select id="count" parameterType="bean.Message" resultType="int">
          select count(*) from MESSAGE
          <where>
          	<if test="command != null and !&quot;&quot;.equals(command.trim())">
      	    	and COMMAND = #{command}
      	    </if>
      	    <if test="description != null and !&quot;&quot;.equals(description.trim())">
      	    	and DESCRIPTION like '%' #{description} '%'
      	    </if>
          </where>
        </select>
      
      </mapper>
      ```
    
      ```java
      public void count() {
      	// 计算总页数
      	int totalPageTemp = this.totalNumber / this.pageNumber;
      	int plus = (this.totalNumber % this.pageNumber) == 0 ? 0 : 1;
      	totalPageTemp = totalPageTemp + plus;
      	if(totalPageTemp <= 0) {
      		totalPageTemp = 1;
      	}
      	this.totalPage = totalPageTemp;
      	
      	// 设置当前页数
      	// 总页数小于当前页数，应将当前页数设置为总页数
      	if(this.totalPage < this.currentPage) {
      		this.currentPage = this.totalPage;
      	}
      	// 当前页数小于 1 设置为 1
      	if(this.currentPage < 1) {
      		this.currentPage = 1;
      	}
      	
      	// 设置 limit 参数
      	this.dbIndex = (this.currentPage - 1) * this.pageNumber;
      	this.dbNumber = this.pageNumber;
      }
      ```
    
      计算 dbIndex 和 dbNumber，传入 parameterType 为 java.util.Map
    
    * 拦截器实现
      
      ```java
      @Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
      public class PageInterceptor implements Interceptor {
      
      	private String onlyTest;
      
      	@Override
      	public void setProperties(Properties properties) {
      		this.onlyTest = properties.getProperty("onlyTest");
      	}
      
      	@Override
      	public Object plugin(Object target) {
      		return Plugin.wrap(target, this);
      	}
      
      	@Override
      	public Object intercept(Invocation invocation) throws Throwable {
      		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
      		MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY);
      		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
      		// 配置文件中 SQL 语句的 ID
      		String id = mappedStatement.getId();
      		if (id.matches(".+ByPage$")) {
      			BoundSql boundSql = statementHandler.getBoundSql();
      			// 原始的 SQL 语句
      			String sql = boundSql.getSql();
      
                  // 查询总条数的SQL语句
      			String countSql = "select count(*) from (" + sql + ")";
      			Connection connection = (Connection) invocation.getArgs()[0];
      			PreparedStatement countStatement = connection.prepareStatement(countSql);
      			ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
      			parameterHandler.setParameters(countStatement);
      			ResultSet rs = countStatement.executeQuery();
                  int totalNumber = 0;
                  if (rs.next()) {
      				totalNumber = rs.getInt(1);
      			}
      
      			Map<?,?> parameter = (Map<?,?>) boundSql.getParameterObject();
      			Page page = (Page) parameter.get("page");
      			// setTotalNumber() 中计算 dbIndex 和 dbNumber
                  page.setTotalNumber(totalNumber);
      			// 带分页查询的 SQL 语句
      			String pageSql = sql + " limit " + page.getDbIndex() + "," + page.getDbNumber();
      			metaObject.setValue("delegate.boundSql.sql", pageSql);
      		}
      		return invocation.proceed();
      	}
      
      }
      ```
      
      ```xml
      <!-- Configuration.xml -->
      <configuration>
        <plugins>
        	<plugin interceptor="com.interceptor.PageInterceptor">
        		<property name="onlyTest" value="XXX"/>
        	</plugin>
        </plugins>
      </configuration>
      ```
      
      ```xml
      <!-- Message.xml -->
      <mapper namespace="com.package.IMessage">
        <select id="queryMessagesByPage" parameterType="java.util.Map" resultMap="MessageResult">
          select <include refid="columns"/> from MESSAGE
          <where>
          	<if test="message.command != null and !&quot;&quot;.equals(message.command.trim())">
      	    	and COMMAND = #{message.command}
      	    </if>
      	    <if test="message.description != null and !&quot;&quot;.equals(message.description.trim())">
      	    	and DESCRIPTION like '%' #{message.description} '%'
    	    </if>
          </where>
        order by ID
        </select>
    </mapper>
      ```
  
  * 批量新增
  
    * JDBC
  
      ```java
      Class.forName("com.mysql.jdbc.Driver");
      Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/database", "root", "root");
      String insertSql = "insert into COMMAND_CONTENT(CONTENT,COMMAND_ID) values(?,?)";
      PreparedStatement statement = conn.prepareStatement(insertSql);
      for (CommandContent content : contentList) {
      	statement.setString(1, content.getContent());
    	statement.setString(2, content.getCommandId());
      	// statement.executeUpdate();
    	statement.addBatch();
      }
      statement.executeUpdate();
      ```
  
    * MyBatis
  
      ```xml
      <mapper namespace="com.package.ICommandContent">
    	<insert id="insertOne" parameterType="com.bean.CommandContent">
      		insert into COMMAND_CONTENT(CONTENT,COMMAND_ID) values(#{content},#{commandId})
      	</insert>
      
    	<insert id="insertBatch" parameterType="java.util.List">
      		insert into COMMAND_CONTENT(CONTENT,COMMAND_ID) values
      		<foreach collection="list" item="item" separator=",">
      			(#{item.content},#{item.commandId})
      		</foreach>
      	</insert>
      </mapper>
      ```
  
* 配置文件解析
  
  DOM、XPath、XNode
  
  parameterType - registerAlias("int[]", Integer[].class)
  
* 其他问题
  
    * 配置文件加载时机
    
      整合 Spring
    
    * SQL 语句与代码分离
    
      优点：便于管理和维护
    
      缺点：不便于调试，需要借助日志工具（log4j）获得信息
    
  * 用标签控制动态 SQL 的拼接
    
      优点：用标签代替编写逻辑代码
    
      缺点：拼接复杂 SQL 语句时，没有代码灵活，比较复杂
    
    * 结果集与 Java 对象的自动映射
    
      优点：保证名称相同即可自动映射
    
      缺点：对开发人员所写的 SQL 依赖性强
    
    * 编写原生 SQL
    
      优点：接近 JDBC，很灵活
    
      缺点：对 SQL 语句依赖程度高，半自动，数据库移植不方便