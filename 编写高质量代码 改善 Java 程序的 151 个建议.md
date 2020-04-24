# 编写高质量代码 改善 Java 程序的 151 个建议

### 第一章 Java 开发中通用的方法和准则

* 建议 1：不要在常量和变量中出现易混淆的字母

  包名全小写，类名首字母全大写，常量全部大写并用下划线分割，变量采用驼峰命名法命名等。

  `long i = 1l;`

  为了让您的程序更容易理解，字母“l”（还包括大写字母“O”）尽量不要和数字混用，以免使阅读者的理解与程序意图产生偏差。如果字母和数字必须混用使用，字母“l”务必大写，字母“O”则增加注释。

* 建议 2：莫让常量蜕变成变量

  常量就是常量，在编译期就必须确定其值，不应该在运行期更改，否则程序的可读性会非常差。

  ```java
  interface Const {
      public static final int RANDOM_COUNT = new Random().nextInt();
  }
  ```

* 建议 3：三元操作符的类型务必一致

  ```java
  int i = 80;
  String.valueOf(i < 100 ? 90 : 100);
  String.valueOf(i < 100 ? 90 : 100.0);
  ```

  三元操作符类型的转换规则：

  若两个操作数不可转换，则不做转换，返回值为 Object 类型。

  若两个操作数是明确类型的表达式（比如变量），则按照正常的二进制数字来转换，int 类型转换为 long 类型，long 类型转换为 float 类型等。

  若两个操作数中有一个是数字 S，另外一个是表达式，且其类型标示为 T，那么，若数字 S 在 T 的范围内，则转换为 T 类型；若 S 超出了 T 类型的范围，则 T 转换为 S。

  若两个操作数都是直接量数字，则返回值类型为范围较大者。

* 建议 4：避免带有变长参数的方法重载

  ```java
  public void calPrice(int price, int discount) {}
  public void calPrice(int price, int... discounts) {}
  calPrice(5000, 75);
  ```

  Java 在编译时，首先会根据实参的数量和类型来进行处理。因为 int 是一个原生数据类型，而数组本身是一个对象，编译器想要“偷懒”，于是它会从最简单的开始“猜想”，只要符合编译条件的即可通过。

* 建议 5：别让 null 值或空值威胁到变长方法

  ```java
  public void call(String str, Integer... i) {}
  public void call(String str, String... i) {}
  // 违反了 KISS 原则（Keep It Simple, Stupid），按照此规则设计的方法应该很容易调用，可是现在在遵循规范的情况下，程序竟然出错了
  client.call("China");
  // 坏味道：调用者隐藏了实参类型，这是非常危险的，不仅仅调用者需要“猜测”该调用哪个方法，而且被调用者也可能产生内部逻辑混乱的情况
  // String[] strs = null;
  // client.call("China", strs);
  client.call("China", null);
  ```

  方法模糊不清，编译器不知道调用哪一个方法。

* 建议 6：覆写变长方法也循规蹈矩

  ```java
  class Base {
      void fun(int price, int... discounts) {}
  }
  
  class Sub extends Base {
      @Override
      void fun(int price, int[] discounts) {}
  }
  
  main() {
      Base base = new Sub();
      base.fun(100, 50);
      Sub sub = new Sub();
      // 编译错误
      sub.fun(100, 50);
  }
  ```

  父类的 fun 编译成字节码后的形参是一个 int 类型的形参加上一个 int 数组类型的形参，子类的参数列表也与此相同，那覆写是理所当然的了，所以加上 @Override 注解没有问题。

  base 对象是把子类对象 Sub 做了向上转型，形参列表是由父类决定的，由于是变长参数，在编译时， base.fun(100, 50) 中的“50”这个实参会被编译器“猜测”而编译成“{50}”数组，再由子类 Sub 执行。再来看看直接调用子类的情况，这时编译器并不会把“50”做类型转换，因为数组本身也是一个对象，编译器还没有聪明到要在两个没有继承关系的类之间做转换，要知道 Java 是要求严格的类型匹配的，类型不匹配编译器自然就会拒绝执行，并给予错误提示。

* 建议 7：警惕自增的陷阱

  `count=count++;`，count++ 是一个表达式，是有返回值的，它的返回值就是 count 自加前的值，Java 对自加是这样处理的：首先把 count 的值（注意是值，不是引用）拷贝到一个临时变量区，然后对 count 变量加 1，最后返回临时变量区的值。

* 建议 8：不要让旧语法困扰你

  Java 中抛弃了 goto 语法，但还是保留了该关键字，只是不进行语义处理而已，与此类似的还有 const 关键字。

  Java 中虽然没有了 goto 关键字，但是扩展了 break 和 continue 关键字，它们的后面都可以加上标号做跳转，完全实现了 goto 功能，同时也把 goto 的诟病带了进来。

* 建议 9：少用静态导入

  类是“一类事物的描述”，缺少了类名的修饰，静态属性和静态方法的表象意义可以被无限放大，这会让阅读者很难弄清楚其属性或方法代表何意，甚至是哪一个类的属性（方法）都要思考一番，特别是在一个类中有多个静态导入语句时，若还使用了 *（星号）通配符，把一个类的所有静态元素都导入进来了，那简直就是噩梦。

  对于静态导入，一定要遵循两个规则：

  不使用 *（星号）通配符，除非是导入静态常量类（只包含常量的类和接口）。

  方法名是具有明确、清晰表象意义的工具类。

* 建议 10：不要在本类中覆盖静态导入的变量和方法

  编译器有一个“最短路径”原则：如果能够在本类中查找到的变量、常量、方法，就不会到其他包或父类、接口中查找，以确保本类的属性、方法优先。

  因此，如果要变更一个被静态导入的方法，最好的办法是在原始类中重构，而不是在本类中覆盖。

* 建议 11：养成良好的习惯，显式声明 UID

  类实现 Serializable 接口的目的是为了可持久化，比如网络传输或本地存储，为系统的分布和异构部署提供先决支持条件。

  在这种序列化和反序列化的类不一致的情况下，反序列化时会报一个 InvalidClassException 异常，原因是序列化和反序列化所对应的类版本（显式声明 SerialVersionUID）发生了变化，JVM 不能把数据流转换为实例对象。

  而隐式声明则是我不声明，你编译器在编译的时候帮我生成。生成的依据是通过包名、类名、继承关系、非私有的方法和属性，以及参数、返回值等诸多因子计算得出的，极度复杂，基本上计算出来的这个值是唯一的。

  JVM 在反序列化时，会比较数据流中的 serialVersionUID 与类的 serialVersionUID 是否相同，如果相同，则认为类没有发生改变，可以把数据流 load 为实例对象；如果不相同，对不起，我 JVM 不干了，抛个异常 InvalidClassException 给你瞧瞧。

  依靠显式声明 serialVersionUID，向 JVM 撒谎说“我的类版本没有变更”，如此，我们编写的类就实现了向上兼容。

* 建议 12：避免用序列化类在构造函数中为不变量赋值

  `public final String name = "混世魔王";`

  保持新旧对象的 final 变量相同，有利于代码业务逻辑统一，这是序列化的基本规则之一，也就是说，如果 final 属性是一个直接量，在反序列化时就会重新计算。

  ```java
  public class Person implements Serializable {
      private static final long serialVersionUID = 91282334L;
      public final String name;
      public Person() {
          name = "混世魔王";
      }
  }
  ```

  反序列化时构造函数不会执行。

  反序列化的执行过程是这样的：JVM 从数据流中获取一个 Object 对象，然后根据数据流中的类文件描述信息（在序列化时，保存到磁盘的对象文件中包含了类描述信息）查看，发现是 final 变量，需要重新计算，于是引用 Person 类中的 name 值，而此时 JVM 又发现 name 竟然没有赋值，不能引用，于是它很“聪明”地不再初始化，保持原值状态。

* 建议 13：避免为 final 变量复杂赋值

  `public final String name = initName();`

  通过方法赋值，即直接在声明时通过方法返回值赋值。

  上个建议所说 final 会被重新赋值，其中的“值”指的是简单对象，简单对象包括：8 个基本类型，以及数组、字符串（不通过 new 关键字生成 String 对象的情况下，final 变量的赋值与基本类型相同），但是不能方法赋值。

  保存到磁盘上（或网络传输）的对象文件包括两部分：

  类描述信息：包括包路径、继承关系、访问权限、变量描述、变量访问权限、方法签名、返回值，以及变量的关联类信息。

  非瞬态（transient）和非静态（static）的实例变量值：注意，这里的值如果是一个基本类型，好说，就是一个简单值保存下来；如果是复杂对象，也简单，连该对象和关联类信息一起保存，并且持续递归下去（关联类也必须实现 Serializable 接口，否则会出现序列化异常），也就是说递归到最后，其实还是基本数据类型的保存。

  反序列化时 final 变量在以下情况下不会被重新赋值：

  通过构造函数为 final 变量赋值。

  通过方法返回值为 final 变量赋值。

  final 修饰的属性不是基本类型。

* 建议 14：使用序列化类的私有方法巧妙解决部分属性持久化问题

  ```java
  private void writeObject(ObjectOutputStream out) throws IOException {
      // 告知 JVM 按照默认的规则写入对象
      out.defaultWriteObject();
      out.writeInt(salary.getBasePay());
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      // 告知 JVM 按照默认的规则读入对象
      in.defaultReadObject();
      salary = new Salary(in.readInt(), 0);
  }
  ```

  序列化回调：Java 调用 ObjectOutputStream 类把一个对象转换成流数据时，会通过反射检查被序列化的类是否有 writeObject 方法，并且检查其是否符合私有、无返回值的特性。若有，则会委托该方法进行对象序列化，若没有，则由 ObjectOutputSteam 按照默认规则继续序列化。同样，在从流数据恢复成实例对象时，也会检查是否有一个私有的 readObject 方法，如果有，则会通过该方法读取属性值。

* 建议 15：break 万万不可忘

* 建议 16：易变业务使用脚本语言编写

  脚本语言的三大特征：

  灵活。脚本语言一般都是动态类型，可以不用声明变量类型而直接使用，也可以在运行期改变类型。

  便捷。脚本语言是一种解释型语言，不需要编译成二进制代码，也不需要像 Java 一样生成字节码。它的执行是依靠解释器解释的，因此在运行期变更代码非常容易，而且不停止应用。

  简单。

  JSR223 规范，只有符合该规范的语言都可以在 Java 平台上运行。

  ```java
  // 获取一个 JavaScript 的执行引擎
  ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
  // 建立上下文变量
  Bindings bind = engine.createBindings();
  bind.put("factor", "1");
  // 绑定上下文，作用域是当前引擎范围
  engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
  // 执行 js 代码
  engine.eval(new FileReader("model.js"));
  // 是否可调用方法
  if (engine instanceof Invocable) {
      Invocable in = (Invocable) engine;
      in.invokeFunction("fun", i, j);
  }
  ```

  脚本语言对系统设计最有利的地方：可以随时发布而不用重新部署。

* 建议 17：慎用动态编译

  Java 的动态编译对源提供了多个渠道。比如，可以是字符串，可以是文本文件，也可以是编译过的字节码文件（.class文件），甚至可以是存放在数据库中的明文代码或是字节码。汇总成一句话，只要是符合 Java 规范的就都可以在运行期动态加载，其实现方式就是实现 JavaFileObject 接口，重写 getCharContent、openInputStream、openOutputStream，或者实现 JDK 已经提供的两个 SimpleJavaFileObject、ForwardingJavaFileObject。

* 建议 18：避免 instanceof 非预期结果

  instanceof 是一个简单的二元运算符，它是用来判断一个对象是否是一个类的实例的。

  `"String" instanceof Object`

  返回值是 true，"String"是一个字符串，字符串又继承了Object。

  `new String() instanceof String`

  返回值是 true，一个类的对象当然是它的实例了。

  `new Object() instanceof String`

  返回值是 false，Object 是父类，其对象当然不是 String 类的实例了。要注意的是，这句话其实完全可以编译通过，只要 instanceof 关键字的左右两个操作数有继承或实现关系，就可以编译通过。

  `'A' instanceof Character`

  编译不通过，因为'A'是一个 char 类型，也就是一个基本类型，不是一个对象，instanceof 只能用于对象的判断，不能用于基本类型的判断。

  `null instanceof String` 和 `(String)null instanceof String`

  返回值是 false，这是 instanceof 特有的规则：若左操作数是 null，结果就直接返回 false，不再运算右操作数是什么类。

  null 是一个万用类型，也可以说它没类型，即使做类型转换还是 null。

  `new Date() instanceof String`

  编译通不过，因为 Date 类和 String 没有继承或实现关系。

  `t instanceof Date`

  Java 的泛型是为编码服务的，在编译成字节码时，T 已经是 Object 类型了，传递的实参是 String 类型，也就是说 T 的表面类型是 Object，实际类型是 String，那“t instanceof Date”这句话就等价于"Object instanceof Date"了，所以返回 false 就很正常了。

* 建议 19：断言绝对不是鸡肋

  在防御式编程中经常会用断言（Assertion）对参数和环境做出判断，避免程序因不当的输入或错误的环境而产生逻辑异常。在 Java 中的断言使用的是 assert 关键字，其基本的用法如下：

  assert <布尔表达式>

  assert <布尔表达式> : <错误信息>

  在布尔表达式为假时，抛出 AssertionError 错误，并附带了错误信息。assert 的语法较简单，有以下两个特性：

  assert 默认是不启用的

  assert 抛出的异常 AssertionError 是继承自 Error 的

  它在以下两种情况不可使用：

  在对外公开的方法中（AssertionError 破坏了契约协定）

  我们知道防御式编程最核心的一点就是：所有的外部因素（输入参数、环境变量、上下文）都是“邪恶”的，都存在着企图摧毁程序的罪恶本源，为了抵制它，我们要在程序中处处检验，满地设卡，不满足条件就不再执行后续程序，以保护主程序的正确性。

  执行逻辑代码的情况下

  assert 的支持是可选的，在开发时可以让它运行，但在生产系统中则不需要其运行了（以便提高性能），因此在 assert 的布尔表达式中不能执行逻辑代码，否则会因为环境不同而产生不同的逻辑。

  那在什么情况下能够使用 assert 呢？一句话，按照正常逻辑不可能到达的代码区域可以放置 assert。具体分为三种情况：

  在私有方法中放置 assert 作为输入参数的校验

  因为私有方法的使用者是作者自己，私有方法的调用者和被调用者之间是一种弱契约关系，或者说没有契约关系，其间的约束是依靠作者自己控制的，因此加上 assert 可以更好地预防自己犯错，或者无意的程序犯错。

  流程控制中不可能达到的区域

  这类似于 JUnit 的 fail 方法，其标志性的意义就是：程序执行到这里就是错误的。

  建立程序探针

  在程序中到处设“桩”断言业务关系。

* 建议 20：不要只替换一个类

  对于 final 修饰的基本类型和 String 类型，编译器会认为它是稳定态（Immutable Status），所以在编译时就直接把值编译到字节码中了，避免了在运行期引用（Run-time Reference），以提高代码的执行效率。

  而对于 final 修饰的类（即非基本类型），编译器认为它是不稳定态（Mutable Status），在编译时建立的则是引用关系，如果引入的常量是一个类或实例，即使不重新编译也会输出最新值。

  发布应用系统时禁止使用类文件替换方式，整体 WA R包发布才是万全之策。

### 第二章 基本类型

* 建议 21：用偶判断不用奇判断

  ```java
  // 模拟取余计算，dividend 被除数，divisor 除数
  public static int remainder(int dividend, int divisor) {
      return dividend - dividend / divisor * divisor;
  }
  ```

  `i % 2 == 0 ? "偶数" : "奇数"`

* 建议 22：用整数类型处理货币

  在十进制的世界里没有办法准确表示 1/3，那在二进制的世界里当然也无法准确表示 1/5，在二进制的世界里 1/5 是一个无限循环小数。

  要解决此问题有两种方法：

  使用 BigDecimal

  BigDecimal 是专门为弥补浮点数无法精确计算的缺憾而设计的类，并且它本身也提供了加减乘除的常用数学算法。

  使用整型

  把参与运算的值扩大 100 倍，并转变为整型，然后在展现时缩小 100 倍，这样处理的好处是计算简单、准确，一般在非金融行业（如零售业）应用较多。

* 建议 23：不要让类型默默转换

  `long dis = LIGHT_SPEED * 60 * 8;`

  Java 是先运算然后再进行类型转换的，三个运算参数都是 int 类型，三者相乘的结果虽然也是 int 类型，但是已经超过了 int 的最大值，所以其值就是负值了，再转换成 long 型，结果还是负值。

  `long dis = 1L * LIGHT_SPEED * 60 * 8;`

  在实际开发中，更通用的做法主动声明式类型转换。

* 建议 24：边界，边界，还是边界

  在单元测试中，有一项测试叫做边界测试（也有叫做临界测试），如果一个方法接收的是 int 类型的参数，那以下三个值是必测的：0、正最大、负最小，其中正最大和负最小是边界值，如果这三个值都没有问题，方法才是比较安全可靠的。

  Java8 Math 新增的 *Exact() 方法，结果溢出时抛出 ArithmeticException。

* 建议 25：不要让四舍五入亏了一方

  Math.round 采用的舍入规则为正无穷方向舍入规则：

  `Math.round(10.5) = 11;`

  `Math.round(-10.5) = -10;`

  银行家舍入的近似算法：

  舍去位的数值小于 5 时，直接舍去；

  舍去位的数值大于等于 6 时，进位后舍去；

  当舍去位的数值等于 5 时，分两种情况：5 后面还有其他数字（非 0），则进位后舍去；若 5 后面是 0（即 5 是最后一个数字），则根据 5 前一位数的奇偶性来判断是否需要进位，奇数进位，偶数舍去。

  以上规则汇总成一句话：四舍六入五考虑，五后非零就进一，五后为零看奇偶，五前为偶应舍去，五前为奇要进一。

  ```java
  main() {
      // 存款
      BigDecimal d = new BigDecimal(888888);
      // 月利率，乘 3 计算季利率
      BigDecimal r = new BigDecimal(0.001875 * 3);
      // 利息
      BigDecimal i = d.multiply(r).setScale(2, RoundingMode.HALF_EVEN);
  }
  ```

  目前 Java 支持以下七种舍入方式：

  ROUND_UP：远离零方向舍入

  向远离 0 的方向舍入，也就是说，向绝对值最大的方向舍入，只要舍弃位非 0 即进位。

  ROUND_DOWN：趋向零方向舍入

  向 0 方向靠拢，也就是说，向绝对值最小的方向舍入，注意，所有的位都舍去，不存在进位情况。

  ROUND_CEILING：向正无穷方向舍入

  向正最大方向靠拢，如果是正数，舍入行为类似于 ROUND_UP；如果是负数，则舍入行为类似于 ROUND_DOWN。注意：Math.round 方法使用的即为此模式。

  ROUND_FLOOR：向负无穷方向舍入

  向负无穷方向靠拢，如果是正数，则舍入行为类似于 ROUND_DOWN；如果是负数，则舍入行为类似于 ROUND_UP。

  HALF_UP：最近数字舍入（5 进）

  HALF_DOWN：最近数字舍入（5 舍）

  HALF_EVENT：银行家算法

* 建议 26：提防包装类型的 null 值

  我们知道拆箱过程是通过调用包装对象 Integer 的 intValue 方法来实现的，由于包装对象是 null 值，访问其 intValue 方法报空指针异常也就在所难免了。

  包装类型参与运算时，要做 null 值校验。

* 建议 27：谨慎包装类型的大小比较

  在 Java 中“==”是用来判断两个操作数是否有相等关系的，如果是基本类型则判断值是否相等，如果是对象则判断是否是一个对象的两个引用，也就是地址是否相等。

  在 Java 中“>”和“<”是用来判断两个数字类型的大小关系，注意只能是数字型的判断，对于 Integer 包装类型，是根据其 intValue() 方法的返回值（也就是其相应的基本类型）进行比较的。

  直接使用 Integer 实例的 compareTo 方法即可。

  两个对象之间的比较就应该采用相应的方法，而不是通过 Java 的默认机制来处理。

* 建议 28：优先使用整型池

  ```java
  main() {
      int num = 127;
      // 通过 new 产生的 Integer 对象
      Integer i = new Integer(num);
      Integer j = new Integer(num);
      // 基本类型转为包装类型
      i = num;
      j = num;
      // 通过静态方法生成一个实例
      i = Integer.valueOf(num);
      j = Integer.valueOf(num);
  }
  ```

  装箱动作是通过 valueOf 方法实现的，也就是说后两个算法是相同的，那结果肯定也是一样的。

  cache 是 IntegerCache 内部类的一个静态数组，容纳的是 -128 到 127 之间的 Integer 对象。通过 valueOf 产生包装对象时，如果 int 参数在 -128 和 127 之间，则直接从整型池中获得对象，不在该范围的 int 类型则通过 new 生成包装对象。

  通过包装类的 valueOf 生成包装实例可以显著提高空间和时间性能。

* 建议 29：优先选择基本类型

  ```java
  public void f(long i) {
      System.out.println("基本类型方法被调用");
  }
  public void f(Long i) {
      System.out.println("包装类型方法被调用");
  }
  main() {
      int i = 5;
      // "基本类型方法被调用"
      client.f(i);
      // "基本类型方法被调用"
      client.f(Integer.valueOf(i));
  }
  ```

  虽然基本类型和包装类型有自动装箱、自动拆箱的功能，但并不影响它们的重载，自动拆箱（装箱）只有在赋值时才会发生，和重载没有关系。

  i 是 int 类型，传递到 f(long i) 是没有任何问题的，编译器会自动把 i 的类型加宽，并将其转变为 long 型，这是基本类型的转换规则。

  i 通过 valueOf 方法包装成一个 Integer 对象，由于没有 f(Integer i) 方法，编译器“聪明”地把 Integer 对象转换成 int，int 自动拓宽为 long，编译结束。

  自动装箱有一个重要的原则，基本类型可以先加宽，再转变成宽类型的包装类型，但不能直接转变成宽类型的包装类型。

  ```java
  public void f(Long i) {}
  main() {
      int i = 5;
      // 编译不通过
      // client.f(i);
      long l = (long)i;
      client.f(l);
  } 
  ```

* 建议 30：不要随便设置随机种子

  在 Java 中，随机数的产生取决于种子，随机数和种子之间的关系遵从以下两个规则：

  种子不同，产生不同的随机数。

  种子相同，即使实例不同也产生相同的随机数。

  Random 类的默认种子（无参构造）是 System.nanoTime() 的返回值，注意这个值是距离某一个固定时间点的纳秒数。

### 第三章 类、对象及方法

* 建议 31：接口中不要存在实现代码

  在接口中声明一个静态常量，其值是一个匿名内部类的实例对象。

  接口是一个契约，不仅仅约束着实现者，同时也是一个保证，保证提供的服务（常量、方法）是稳定的、可靠的，如果把实现代码写到接口中，那接口就绑定了可能变化的因素，这就会导致实现不再稳定和可靠，是随时都可能被抛弃、被更改、被重构的。

* 建议 32：静态变量一定要先声明后赋值

  ```java
  public class Client {
      static {
          i = 100;
      }
      public static int i = 1;
  }
  ```

  静态变量是类加载时被分配到数据区的，它在内存中只有一个拷贝，不会被分配多次，其后的所有赋值操作都是值改变，地址则保持不变。

  我们知道 JVM 初始化变量是先声明空间，然后再赋值的。静态变量是在类初始化时首先被加载的，JVM 会去查找类中所有的静态声明，然后分配空间，注意这时候只是完成了地址空间的分配，还没有赋值，之后 JVM 会根据类中静态赋值（包括静态类赋值和静态块赋值）的先后顺序来执行。

* 建议 33：不要覆写静态方法

  在 Java 中可以通过覆写（Override）来增强或减弱父类的方法或行为，但覆写是针对非静态方法（也叫做实例方法，只有生成实例才能调用的方法）的，不能针对静态方法（static 修饰的方法，也叫做类方法）。

  我们知道一个实例对象有两个类型，表面类型（Apparent Type）和实际类型（Actual Type），表面类型是声明时的类型，实际类型是对象产生时的类型。

  对于非静态方法，它是根据对象的实际类型来执行的。而对于静态方法来说就比较特殊了，首先静态方法不依赖实例对象，它是通过类名访问的；其次，可以通过对象访问静态方法，如果是通过对象调用静态方法，JVM 则会通过对象的表面类型查找到静态方法的入口，继而执行之。

  在子类中构建与父类相同的方法名、输入参数、输出参数、访问权限（权限可以扩大），并且父类、子类都是静态方法，此种行为叫做隐藏（Hide），它与覆写有两点不同：

  表现形式不同。隐藏用于静态方法，覆写用于非静态方法。在代码上的表现是：@Override 注解可以用于覆写，不能用于隐藏。

  职责不同。隐藏的目的是为了抛弃父类静态方法，重现子类方法。而覆写则是将父类的行为增强或减弱，延续父类的职责。

  通过实例对象访问静态方法或静态属性不是好习惯，它给代码带来了“坏味道”。

* 建议 34：构造函数尽量简化

  ```java
  abstract class Server {
      public final static int DEFAULT_PORT = 8090;
      public Server() {
          int port = getPort();
      }
      protected abstract int getPort();
  }
  class SimpleServer extends Server {
      private int port = 100;
      public SimpleServer(int port) {
          this.port = port;
      }
      @Override
      protected int getPort() {
          return Math.random() > 0.5 ? port : DEFAULT_PORT;
      }
  }
  main() {
      new SimpleServer(8080);
  }
  ```

  子类实例化时，会首先初始化父类（注意这里是初始化，可不是生成父类对象），也就是初始化父类的变量，调用父类的构造函数，然后才会初始化子类的变量，调用子类自己的构造函数，最后生成一个实例对象。

* 建议 35：避免在构造函数中初始化其他类

* 建议 36：使用构造代码块精炼程序

  用大括号把多行代码封装在一起，形成一个独立的数据体，实现特定算法的代码集合即为代码块，一般来说代码块是不能单独运行的，必须要有运行体。在 Java 中一共有四种类型的代码块：

  普通代码块：就是在方法后面使用“{}”括起来的代码片段，它不能单独执行，必须通过方法名调用执行。

  静态代码块：在类中使用 static 修饰，并使用“{}”括起来的代码片段，用于静态变量的初始化或对象创建前的环境初始化。

  同步代码块：使用 synchronized 关键字修饰，并使用“{}”括起来的代码片段，它表示同一时间只能有一个线程进入到该方法中，是一种多线程保护机制。

  构造代码块：在类中没有任何的前缀和后缀，并使用“{}”括起来的代码片段。

  构造代码块会在每个构造函数内首先执行（需要注意的是：构造代码块不是在构造函数之前运行的，它依托于构造函数的执行）。

* 建议 37：构造代码块会想你所想

  编译器会把构造代码块插入到每一个构造函数中，但是有一个例外的情况没有说明：如果遇到 this 关键字（也就是构造函数调用自身其他的构造函数时）则不插入构造代码块。

  构造代码块是为了提取构造函数的共同量，减少各个构造函数的代码而产生的，因此，Java 就很聪明地认为把代码块插入到没有 this 方法的构造函数中即可，而调用其他构造函数的则不插入，确保每个构造函数只执行一次构造代码块。

* 建议 38：使用静态内部类提高封装性

  Java 中的嵌套类分为两种：静态内部类和内部类。

  静态内部类有两个优点：加强类的封装性和提高了代码的可读性。

  提高了封装性。从代码位置上来讲，静态内部类放置在外部类内，其代码层意义就是：静态内部类是外部类的子行为或子属性，两者直接保持着一定的关系。

  提高代码的可读性。

  形似内部，神似外部。静态内部类虽然存在于外部类内，而且编译后的类文件名也包含外部类（格式是：外部类+$+内部类），但是它可以脱离外部类存在。

  静态内部类与普通内部类有什么区别呢？

  静态内部类不持有外部类的引用

  在普通内部类中，我们可以直接访问外部类的属性、方法，即使是 private 类型也可以访问，这是因为内部类持有一个外部类的引用，可以自由访问。而静态内部类，则只可以访问外部类的静态方法和静态属性（如果是 private 权限也能访问，这是由其代码位置所决定的），其他则不能访问。

  静态内部类不依赖外部类

  普通内部类与外部类之间是相互依赖的关系，内部类实例不能脱离外部类实例，也就是说它们会同生同死，一起声明，一起被垃圾回收器回收。而静态内部类是可以独立存在的，即使外部类消亡了，静态内部类还是可以存在的。

  普通内部类不能声明 static 的方法和变量

  普通内部类不能声明 static 的方法和变量，常量（也就是 final static 修饰的属性）还是可以的，而静态内部类形似外部类，没有任何限制。

* 建议 39：使用匿名类的构造函数

  `List list = new ArrayList() {};`

  代表的是一个匿名类的声明和赋值，它定义了一个继承于 ArrayList 的匿名类，只是没有任何的覆写方法而已，其代码类似于：

  ```java
  class Sub extends ArrayList {}
  List list = new Sub(); 
  ```

  `List list = new ArrayList() {{}};`

  它的代码类似于：

  ```java
  class Sub extends ArrayList {
      {
          // 构造代码块
      }
  }
  List list = new Sub();
  ```

  初始化块就是匿名类的构造函数。当然，一个类中的构造函数块可以是多个，也就是说可以出现如下代码：`List list = new ArrayList() {{} {} {}};`

* 建议 40：匿名类的构造函数很特殊

  一般类（也就是具有显式名字的类）的所有构造函数默认都是调用父类的无参构造的，而匿名类因为没有名字，只能由构造代码块代替，也就无所谓的有参和无参构造函数了，它在初始化时直接调用了父类的同参数构造，然后再调用了自己的构造代码块。

* 建议 41：让多重继承成为现实

  ```java
  interface Father {
      public int strong();
  }
  interface Mother {
      public int kind();
  }
  class FatherImpl extends Father {
      public int strong() {
          return 8;
      }
  }
  class MotherImpl extends Mother {
      public int kind() {
          return 8;
      }
  }
  class Son extends FatherImpl implements Mother {
      @Override
      public int strong() {
          return super.strong() + 1;
      }
      @Override
      public int kind() {
          return new MotherSpecial().kind();
      }
      // 成员内部类（实例内部类）
      private class MotherSpecial extends MotherImpl {
          @Override
          public int kind() {
              return super.kind() - 1;
          }
      }
  }
  class Daughter extends MotherImpl implements Father {
      @Override
      public int strong() {
          // 匿名内部类
          return new FatherImpl() {
              @Override
              public int strong() {
                  return super.strong() - 2;
              }
          }.strong();
      }
  }
  ```

  内部类可以继承一个与外部类无关的类，保证了内部类的独立性，正是基于这一点，多重继承才会成为可能。

* 建议 42：让工具类不可实例化

  ```java
  public final class UtilsClass {
      private UtilsClass() {
          throw new Error("Don't let anyone instantiate this class.");
      }
  }
  ```

* 建议 43：避免对象的浅拷贝

  浅拷贝规则如下：

  基本类型：如果变量是基本类型，则拷贝其值。

  对象：如果变量是一个实例对象，则拷贝地址引用，也就是说此时新拷贝出的对象与原有对象共享该实例变量，不受访问权限的限制。

  String 字符串：这个比较特殊，拷贝的也是一个地址，是个引用，但是在修改时，它会从字符串池中重新生成新的字符串，原有的字符串对象保持不变，在此处可以认为 String 是一个基本类型。

* 建议 44：推荐使用序列化实现对象的拷贝

  在内存中通过字节流的拷贝来实现，也就是把母对象写到一个字节流中，再从字节流中将其读出来，这样就可以重建一个新对象了，该新对象与母对象之间不存在引用共享的问题，也就相当于深拷贝了一个新对象。

  ```java
  public class CloneUtils {
      public static <T extends Serializable> T clone(T obj) {
          T cloneObj = null;
          try {
              ByteArrayOutputStream baos = new ByteArrayOutputStream();
              ObjectOutputStream oos = new ObjectOutputStream(baos);
              oos.writeObject(obj);
              oos.close();
              ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
              ObjectInputStream ois = new ObjectInputStream(bais);
              cloneObj = ois.readObject();
              ois.close();
          } catch (Exception e) {
              e.printStackTrace();
          }
          return cloneObj;
      }
  }
  ```

  此工具类要求被拷贝的对象必须实现 Serializable 接口，否则是没办法拷贝的。

  采用序列化方式拷贝时还有一个更简单的办法，即使用 Apache 下 commons 工具包中的 SerializationUtils 类，直接使用更加简洁方便。

* 建议 45：覆写 equals 方法时不要识别不出自己

  ```java
  class Person {
      private String name;
      @Override
      public boolean equals(Object obj) {
          if (obj instanceof Person) {
              Person p = (Person) obj;
              return name.equalsIgnoreCase(p.getName());
              // return name.equalsIgnoreCase(p.getName().trim());
          }
          return false;
      }
  }
  ```

  它违背了 equals 方法的自反性原则：对于任何非空引用 x，x.equals(x) 应该返回 true。

* 建议 46：equals 应该考虑 null 值场景

  ```java
  @Override
  public boolean equals(Object obj) {
      if (obj instanceof Person) {
          Person p = (Person) obj;
          if (p.getName() == null || name == null) {
              return false;
          } else {
              return name.equalsIgnoreCase(p.getName());
          }
      }
      return false;
  }
  ```

  覆写 equals 没有遵循对称性原则：对于任何引用 x 和 y 的情形，如果 x.equals(y) 返回 true，那么 y.equals(x) 也应该返回 true。

* 建议 47：在 equals 中使用 getClass 进行类型判断

  传递性原则是指对于实例对象 x、y、z 来说，如果 x.equals(y) 返回 true，y.equals(z) 返回 true，那么 x.equals(z) 也应该返回 true。

  ```java
  @Override
  public boolean equals(Object obj) {
      if (obj != null && obj.getClass() == this.getClass()) {
          Person p = (Person) obj;
          if (p.getName() == null || name == null) {
              return false;
          } else {
              return name.equalsIgnoreCase(p.getName());
          }
      }
      return false;
  }
  ```

  在覆写 equals 时建议使用 getClass 进行类型判断，而不要使用 instanceof。

* 建议 48：覆写 equals 方法必须覆写 hashCode 方法

  HashCodeBuilder 是 org.apache.commons.lang.builder 包下的一个哈希码生成工具。

* 建议 49：推荐覆写 toString 方法

  使用 apache 的 commons 工具包中的 ToStringBuilder 类。

* 建议 50：使用 package-info 类为包服务

  声明友好类和包内访问常量

  ```java
  // package-info.java
  class PkgClass {
      public void test() {}
  }
  class PkgConst {
      static final String PACKAGE_CONST = "";
  }
  ```

  为在包上标注注解提供便利

  比如我们要写一个注解（Annotation），查看一个包下的所有对象，只要把注解标注到 package-info 文件中即可。

  提供包的整体注释说明

  如果是分包开发，也就是说一个包实现了一个业务逻辑或功能点或模块或组件，则该包需要有一个很好的说明文档，说明这个包是做什么用的，版本变迁历史，与其他包的逻辑关系等，package-info 文件的作用在此就发挥出来了，这些都可以直接定义到此文件中，通过 javadoc 生成文档时，会把这些说明作为包文档的首页，让读者更容易对该包有一个整体的认识。

* 建议 51：不要主动进行垃圾回收

  System.gc 要停止所有的响应（Stop the world），才能检查内存中是否有可回收的对象，这对一个应用系统来说风险极大。

