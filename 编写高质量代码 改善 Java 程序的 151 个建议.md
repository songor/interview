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

### 第四章 字符串

* 建议 52：推荐使用 String 直接量赋值

  在字符串池中所容纳的都是 String 字符串对象，它的创建机制是这样的：

  创建一个字符串时，首先检查池中是否有字面值相等的字符串，如果有，则不再创建，直接返回池中该对象的引用，若没有则创建之，然后放到池中，并返回新建对象的引用。

  intern 会检查当前的对象在对象池中是否有字面值相同的引用对象，如果有则返回池中对象，如果没有则放置到对象池中，并返回当前对象。

  String 类是一个不可变对象其实有两层意思：一是 String 类是 final 类，不可继承，不可能产生一个 String 的子类；二是 String 类提供的所有方法中，如果有 String 返回值，就会新建一个 String 对象，不对原对象进行修改，这也就保证了原对象是不可改变的。

* 建议 53：注意方法中传递的参数要求

  replaceAll 传递的第一个参数是正则表达式。

* 建议 54：正确使用 String、StringBuilder、StringBuffer

  String 类是不可改变的量，也就是创建后就不能再修改了，即使想通过 String 提供的方法来尝试修改，也是要么创建一个新的字符串对象，要么返回自己。

  StringBuffer 是一个可变字符序列，它与 String 一样，在内存中保存的都是一个有序的字符序列（char 类型的数组），不同点是 StringBuffer 对象的值是可改变的。

  StringBuilder 与 StringBuffer 基本相同，都是可变字符序列，不同点是：StringBuffer 是线程安全的，StringBuilder 是线程不安全的。

  在性能方面，由于 String 类的操作都是产生新的 String 对象，而 StringBuilder 和 StringBuffer 只是一个字符数组的再扩容而已，所以 String 类的操作要远慢于 StringBuilder 和 StringBuffer。

* 建议 55：注意字符串的位置

  Java 对加号的处理机制：在使用加号进行计算的表达式中，只要遇到 String 字符串，则所有的数据都会转换为 String 类型进行拼接，如果是原始类型，则直接拼接，如果是对象，则调用 toString 方法的返回值然后拼接。

* 建议 56：自由选择字符串拼接方法

  对一个字符串进行拼接有三种办法：加号、concat 方法以及 StringBuilder 的 append 方法。

  在字符串拼接方式中，append 方法最快，concat 方法次之，加号最慢。

  "+"方法拼接字符串

  编译器对字符串的加号做了优化，它会使用 StringBuilder 的 append 方法进行追加。

  `str = new StringBuilder(str).append("c").toString();`

  它与纯粹使用 StringBuilder 的 append 方法是不同的：一是每次循环都会创建一个 StringBuilder 对象，二是每次执行完毕都要调用 toString 方法将其转换为字符串——它的执行时间就是耗费在这里了！

  concat 方法拼接字符串

  整体看上去就是一个数组拷贝，虽然在内存中的处理都是原子性操作，速度非常快，不过，每次的 concat 操作都会新创建一个 String 对象，这就是 concat 速度慢下来的真正原因。

  append 方法拼接字符串

  整个 append 方法都在做字符数组处理，加长，然后数组拷贝，这些都是基本的数据处理，没有新建任何对象，所以速度也就最快了！

* 建议 57：推荐在复杂字符串操作中使用正则表达式

* 建议 58：强烈建议使用 UTF 编码

  Java 程序涉及的编码包括两部分：

  Java 文件编码：操作系统默认的格式或 IDE 的设置。

  Class 文件编码：通过 javac 命令生成的后缀名为 .class 的文件是 UTF-8 编码的 UNICODE 文件，这在任何操作系统上都是一样的，只要是 class 文件就会是 UNICODE 格式。需要说明的是，UTF 是 UNICODE 的存储和传输格式，它是为了解决 UNICODE 的高位占用冗余空间而产生的，使用 UTF 编码就标志着字符集使用的是 UNICODE。

* 建议 59：对字符串排序持一种宽容的心态

  Arrays 工具类的默认排序是通过数组元素的 compareTo 方法进行比较的。

  String 类的 compareTo，先取得字符串的字符数组，然后一个一个地比较大小，注意这里是字符比较，也就是 UNICODE 码值的比较。

  ```java
  Comparator c = Collator.getInstance(Locale.CHINA);
  Arrays.sort(strs, c);
  ```

### 第五章 数组和集合

* 建议 60：性能考虑，数组是首选

  遍历计算时要做装箱、拆箱动作。

  基本类型是在栈内存中操作的，而对象则是在堆内存中操作的，栈内存的特点是速度快，容量小，堆内存的特点是速度慢，容量大。

* 建议 61：若有必要，使用变长数组

  在实际开发中，如果确实需要变长的数据集，数组也是在考虑范围之内的，不能因固定长度而将其否定之。

  `Arrays.copyOf(original, newLength);`

* 建议 62：警惕数组的浅拷贝

  通过 copyOf 方法产生的数组是一个浅拷贝，这与序列化的浅拷贝完全相同：基本类型是直接拷贝值，其他都是拷贝引用地址。需要说明的是，数组的 clone 方法也是与此相同的，同样是浅拷贝，而且集合的 clone 方法也都是浅拷贝。

* 建议 63：在明确的场景下，为集合指定初始容量

  每次扩容都是一次数组的拷贝，如果数据量很大，这样的拷贝会非常消耗资源，而且效率非常低下。

* 建议 64：多种最值算法，适时选择

  `Arrays.sort(data.clone());`

  ```java
  List<Integer> dataList = Arrays.asList(data);
  TreeSet<Integer> ts = new TreeSet(dataList);
  ts.lower(ts.last());
  ```

  数组不能剔除重复数据，但 Set 集合却是可以的，而且 Set 的子类 TreeSet 还能自动排序。

* 建议 65：避开基本类型数组转换列表陷阱

  ```java
  // size 1
  int[] data = {1, 2, 3, 4, 5};
  Arrays.asList(data).size();
  // size 5
  Integer[] data = {1, 2, 3, 4, 5};
  Arrays.asList(data).size();
  ```

  asList 方法输入的是一个泛型变长参数，我们知道基本类型是不能泛型化的，也就是说 8 个基本类型不能作为泛型参数，要想作为泛型参数就必须使用其所对应的包装类型。

  在 Java 中，数组是一个对象，它是可以泛型化的，也就是说我们的例子是把一个 int 类型的数组作为了 T 的类型，所以转换后在 List 中就只有一个类型为 int 数组的元素了。

* 建议 66：asList 方法产生的 List 对象不可更改

  asList 返回的是一个长度不可变的列表，数组是多长，转换成的列表也就是多长，换句话说此处的列表只是数组的一个外壳，不再保持列表动态变长的特性。

* 建议 67：不同的列表选择不同的遍历方法

  ```java
  public static int average(List<Integer> list) {
      int sum = 0;
      if (list instanceof RandomAccess) {
          // ArrayList
          for (int i = 0, size = list.size(); i < size; i++) {
              sum += list.get(i);
          }
      } else {
          // LinkedList
          for (int i : list) {
              sum += i;
          }
      }
      return sum / list.size();
  }
  ```

  ArrayList 实现了 RandomAccess 接口，这也就标志着 ArrayList 是一个可以随机存取的列表。

  Java 中的 foreach 语法是 iterator 的变形用法。

  为了使用迭代器就需要强制建立一种互相“知晓”的关系，比如上一个元素可以判断是否有下一个元素，以及下一个元素是什么等关系，这也就是通过 foreach 遍历耗时的原因。

  推荐使用下标方式遍历 => 为了使用迭代器就需要强制建立一种互相“知晓”的关系，这就是通过foreach遍历耗时的原因 -> LinkedList使用迭代器效率更高 => node方法查找指定下标的节点，判断输入的下标与中间值（size >> 1）的关系，小于中间值则从头开始正向搜索，大于中间值则从尾节点反向搜索

* 建议 68：频繁插入和删除时使用 LinkedList

  元素拷贝过程 / 引用指针的变更

  LinkedList 这种顺序存取列表的元素定位方式会折半遍历，这是一个极耗时的操作。而 ArrayList 的修改动作则是数组元素的直接替换，简单高效。

* 建议 69：列表相等只需关心元素数据

  `AbstractList.equals()`

  列表只是一个容器，只要是同一种类型的容器（如 List），不用关心容器的细节差别（如 ArrayList 与 LinkedList），只要确定所有的元素数据相等，那这两个列表就是相等的。

* 建议 70：子列表只是原列表的一个视图

  它返回的 SubList 类也是 AbstractList 的子类，其所有的方法如 get、set、add、remove 等都是在原始列表上的操作，它自身并没有生成一个数组或是链表，也就是子列表只是原列表的一个视图（View），所有的修改动作都反映在了原列表上。

  通过 ArrayList 构造函数创建的 List 对象实际上是新列表，它是通过数组的 copyOf 动作生成的，所生成的列表与原列表之间没有任何关系（虽然是浅拷贝，但元素类型是 String，也就是说元素是深拷贝的）。

* 建议 71：推荐使用 subList 处理局部列表

  `list.subList(fromIndex, toIndex).clear();`

  subList 返回的 List 是原始列表的一个视图，删除这个视图中的所有元素，最终就会反映到原始列表上。

* 建议 72：生成子列表后不要再操作原列表

  ```java
  if (modCount != expectedModCount) {
      throw new ConcurrentModificationException();
  }
  ```

  通过 Collections.unmodifiableList 方法设置列表为只读状态。

  只要生成的子列表多于一个，则任何一个子列表就都不能修改了，否则就会抛出 ConcurrentModificationException。

* 建议 73：使用 Comparator 进行排序

  在 Java 中，要想给数据排序，有两种实现方式，一种是实现 Comparable 接口，一种是实现 Comparator 接口。

  `Collections.sort(List<T> list);`

  `Collections.reverse(List<T> list);`

  `Collections.sort(List<T> list, Comparator<? super T> c);`

  `Collections.sort(List<T> list, Collections.reverseOrder(Comparator<T> cmp));`

  使用 apache 工具类 CompareToBuilder 来简化处理。

  Comparable 接口可以作为实现类的默认排序法，Comparator 接口则是一个类的扩展排序工具。

* 建议 74：不推荐使用 binarySearch 对列表进行检索

  对一个列表进行检索时，我们使用得最多的是 indexOf 方法。

  二分法查找必须要先排序，这是二分法查找的首要条件。

* 建议 75：集合中的元素必须做到 compareTo 和 equals 同步

  indexOf 依赖 equals 方法查找，binarySearch 则依赖 compareTo 方法查找。

  equals 是判断元素是否相等，compareTo 是判断元素在排序中的位置是否相同。

  既然一个是决定排序位置，一个是决定相等，那我们就应该保证当排序位置相同时，其 equals 也相同，否则就会产生逻辑混乱。

* 建议 76：集合运算时使用更优雅的方式

  并集：`list1.addAll(list2);`

  交集：`list1.retainAll(list2);`

  差集：`list1.removeAll(list2);`

  无重复的并集：`list1.addAll(list2.removeAll(list1));`

* 建议 77：使用 shuffle 打乱列表

* 建议 78：减少 HashMap 中元素的数量

  HashMap 比 ArrayList 多了一个层 Entry 的底层对象封装，多占用了内存，并且它的扩容策略是 2 倍长度的递增，同时还会依据阈值判断规则进行判断，因此相对于 ArrayList 来说，他就会出现内存溢出。

* 建议 79：集合中的哈希码不要重复

  HashMap 每次增加元素时都会先计算其哈希码，然后使用 hash 方法再次对 hashCode 进行抽取和统计，同时兼顾哈希码的高位和低位信息产生一个唯一值，也就是说 hashCode 不同，hash 方法返回的值也不同。之后再通过 indexFor 方法与数组长度做一次与运算，即可计算出其在数组中的位置，简单地说，hash 方法和 indexFor 方法就是把哈希码转变成数组的下标。

  `int h; return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);`

  `(n - 1) & hash`

* 建议 80：多线程使用 Vector 或 HashTable

  基本上所有的集合类都有一个叫做快速失败（Fail-Fast）的校验机制，当一个集合在被多个线程修改并访问时，就可能会出现 ConcurrentModificationException 异常，这是为了确保集合方法一致而设置的保护措施，它的实现原理就是我们经常提到的 modCount 修改计数器：如果在读列表时，modCount 发生变化（也就是有其他线程修改）则会抛出 ConcurrentModificationException 异常。这与线程同步是两码事，线程同步是为了保护集合中的数据不被脏读、脏写而设置的。

* 建议 81：非稳定排序推荐使用 List

  SortedSet 接口只是定义了在给集合加入元素时将其进行排序，并不能保证元素修改后的结果，因此 TreeSet 适用于不变量的集合数据排序，比如 String、Integer 等类型，但不适用于可变量的排序，特别是不确定何时元素会发生变化的数据集合。

  对于不变量的排序，例如直接量（也就是 8 个基本类型）、String 类型等，推荐使用 TreeSet，而对于可变量，例如我们自己写的类，可能会在逻辑处理中改变其排序关键值的，则建议使用 List 自行排序。

* 建议 82：由点及面，一叶知秋——集合大家族

  List：ArrayList、LinkedList、Vector、Stack

  Set：EnumSet、HashSet、TreeSet

  Map：TreeMap、HashMap、HashTable、Properties、EnumMap、WeakHashMap

  Queue：ArrayBlockingQueue、PriorityBlockingQueue、LinkedBlockingQueue、PriorityQueue、ArrayDeque、LinkedBlockingDeque、LinkedList

  数组

  工具类：java.util.Arrays、java.util.Collections

  扩展类：Apache 的 commons-collections 扩展包，Google 的 google-collections 扩展包

### 第六章 枚举和注解

* 建议 83：推荐使用枚举定义常量

  枚举的优点主要表现在以下四个方面：

  枚举常量更简单

  枚举常量只需要定义每个枚举项，不需要定义枚举值，而接口常量（或类常量）则必须定义值，否则编译不通过，即使我们不需要关注其值是多少也必须定义。

  枚举常量属于稳态型

  在编译期间限定类型，不允许发生越界的情况。

  枚举具有内置方法

  每个枚举都是 java.lang.Enum 的子类，该基类提供了诸如获得排序值的 ordinal 方法、compareTo 比较方法等，大大简化了常量的访问（通过 values 方法获得所有的枚举项）。

  枚举可以自定义方法

  枚举常量不仅可以定义静态方法，还可以定义非静态方法，而且还能够从根本上杜绝常量类被实例化。

  每个枚举项都是该枚举的一个实例，那我们在枚举中定义的静态方法既可以在类中引用，也可以在实例中引用。

  枚举类型是不能有继承的，也就是说一个枚举常量定义完毕后，除非修改重构，否则无法做扩展，而接口常量和类常量则可以通过继承进行扩展。

* 建议 84：使用构造函数协助描述枚举项

  一般来说，我们经常使用的枚举项只有一个属性，即排序号，其默认值是从 0,1,2...。但是除了排序号，枚举还有一个（或多个）属性：枚举描述，它的含义是通过枚举的构造函数，声明每个枚举项（也就是枚举的实例）必须具有的属性和行为，这是对枚举项的描述或补充，目的是使枚举项表述的意义更加清晰明确。

* 建议 85：小心 switch 带来的空值异常

  Java 的 switch 语句只能判断 byte、short、char、int 类型（JDK7 已经允许使用 String 类型）。

  编译器判断出 switch 语句后的参数是枚举类型，然后就会根据枚举的排序值继续匹配（enum.ordinal()）。enum 变量是 null 值，无法执行 ordinal 方法，于是报空指针异常了。

* 建议 86：在 switch 的 default 代码块中增加 AssertionError 错误

  这样可以保证在增加一个枚举项的情况下，若其他代码未修改，运行期马上就会报错，这样一来就很容易查找到错误，方便立刻排除。

* 建议 87：使用 valueOf 前必须进行校验

  valueOf 方法会把一个 String 类型的名称转变为枚举项，也就是在枚举项中查找出字面值与该参数相等的枚举项。

  valueOf 方法先通过反射从枚举类的常量声明中查找，若找到就直接返回，若找不到则抛出无效参数异常（IllegalArgumentException）。

  ```java
  enum Season {
      Spring, Summer, Autumn, Winter;
      public static boolean contains(String name) {
          Season[] season = values();
          for (Season s : season) {
              if (s.name().equals(name)) {
                  return true;
              }
          }
          return false;
      }
  }
  ```

  Season 枚举具备了静态方法 contains 后，就可以在 valueOf 前判断一下是否包含指定的枚举名称了，若包含则可以通过 valueOf 转换为 Season 枚举，若不包含则不转换。

* 建议 88：用枚举实现工厂方法模式更简洁

  枚举非静态方法实现工厂方法模式

  ```java
  public enum CarFactory {
      FordCar, BuickCar;
      public Car create() {
          switch (this) {
              case FordCar:
                  return new FordCar();
              case BuickCar:
                  return new BuickCar();
              default:
                  throw new AssertionError();
          }
      }
  }
  ```

  通过抽象方法生成产品

  ```java
  public enum CarFactory {
      FordCar {
          @Override
          public Car create() {
              return new FordCar();
          }
      },
      BuickCar {
          @Override
          public Car create() {
              return new BuickCar();
          }
      };
      public abstract Car create();
  }
  ```

  避免错误调用的发生

  一般工厂方法模式中的生产方法接收三种类型的参数：类型参数、String 参数、int 参数，这三种参数都是宽泛的数据类型，很容易产生错误（比如边界问题、null 值问题），而且出现这类错误编译器还不会报警。

  而使用枚举类型的工厂方法模式就不存在该问题了，不需要传递任何参数。

  性能好，使用便捷

  降低类间耦合

  不管生产方法接收的是 Class、String 还是 int 的参数，都会成为客户端类的负担，这些类并不是客户端需要的，而是因为工厂方法的限制必须输入的。这严重违背了迪米特原则，也就是最少知识原则：一个对象应该对其他对象有最少的了解。

* 建议 89：枚举项的数量限制在 64 个以内

  Java 提供了两个枚举集合：EnumSet 和 EnumMap，这两个集合的使用方法都比较简单，EnumSet 表示其元素必须是某一枚举的枚举项，EnumMap 表示 Key 值必须是某一枚举的枚举项。

  `EnumSet<Const> cs = EnumSet.allOf(Const.class);`

  当枚举项数量小于等于 64 时，创建一个 RegularEnumSet 实例对象，大于 64 时则创建一个 JumboEnumSet 实例对象。

  我们知道枚举项的排序值 ordinal 是从 0,1,2... 依次递增的，没有重号，没有跳号，RegularEnumSet 就是利用这一点把每个枚举项的 ordinal 映射到一个 long 类型数字的每个位上的。

  想想看，一个 long 类型的数字包含了所有的枚举项，其效率和性能肯定是非常优秀的。

  我们知道 long 类型是 64 位的，所以 RegularEnumSet 类型也就只能负责枚举项数量不大于 64 的枚举。

  JumboEnumSet 类把枚举项按照 64 个元素一组拆分成了多组，每组都映射到一个 long 类型的数字上，然后该数字再放置到 elements 数组中。

* 建议 90：小心注解继承

  @Inherited，它表示一个注解是否可以自动被继承。

  采用 @Inherited 元注解有利有弊，利的地方是一个注解只要标注到父类，所有的子类都会自动具有与父类相同的注解，整齐、统一而且便于管理，弊的地方是单单阅读子类代码，我们无从知道为何逻辑会被改变，因为子类没有明显标注该注解。

* 建议 91：枚举和注解结合使用威力更大

* 建议 92：注意 @Override 不同版本的区别

  1.5 版中的 @Override 是严格遵守覆写的定义：子类方法与父类方法必须具有相同的方法名、输入参数、输出参数（允许子类缩小）、访问权限（允许子类扩大），父类必须是一个类，不能是一个接口，否则不能算是覆写。而这在 Java 1.6 开放了很多，实现接口的方法也可以加上 @Override 注解了，可以避免粗心大意导致方法名称与接口不一致的情况发生。

### 第七章 泛型和反射

* 建议 93：Java 的泛型是类型擦除的

  Java 的泛型在编译期有效，在运行期被删除，也就是说所有的泛型参数类型在编译后都会被清除掉。

  在编译后所有的泛型类型都会做相应的转化。转换规则如下：

  List\<T\> 擦除后的类型为 List。

  List\<T\>[] 擦除后的类型为 List[]。

  List\<? extends E\>、List\<? super E\> 擦除后的类型为 List\<E\>。

  List\<T extends Serializable & Cloneable\> 擦除后为 List\<Serializable\>。

  泛型的 class 对象是相同的

  `List<String> ls.getClass() == List<Integer> li.getClass()`

  泛型数组初始化时不能声明泛型类型

  `List<String>[] listArray = new List<String>[]`

  instanceof 不允许存在泛型参数

  `list instanceof List<String>`

* 建议 94：不能初始化泛型参数和数组

  泛型类型在编译期被擦除，我们在类初始化时将无法获得泛型的具体参数。

  ```java
  class Foo<T> {
      private T t = new T();
      private T[] tArray = new T[5];
      // 元素加入时向上转型为 Object 类型，取出时向下转型为 E 类型
      private List<T> list = new ArrayList<T>();
  }
  ```

  ```java
  class Foo<T> {
      private T t;
      private T[] tArray;
      public Foo(String className) {
          Class<?> tType = Class.forName(className);
          t = (T) tType.newInstance();
          tArray = (T[]) Array.newInstance(tType, 5);
      }
  }
  ```

  类的成员变量是在类初始化前初始化的，所以要求在初始化前它必须具有明确的类型，否则就只能声明，不能初始化。

* 建议 95：强制声明泛型的实际类型

  通过强制泛型参数类型，我们明确了泛型方法的输入、输出参数类型，问题是我们要在什么时候明确泛型类型呢？一句话：无法从代码中推断出泛型类型的情况下，即可强制声明泛型类型。

  ```java
  class ArrayUtils {
      public static <T> List<T> asList(T...t) {
          List<T> list = new ArrayList<T>();
          Collections.addAll(list, t);
          return list;
      }
  }
  main() {
      List<String> list1 = ArrayUtils.asList("A", "B");
      // 编译器会很“聪明”地推断出最顶层类 Object 就是其泛型类型
      // List<Object> list2 = ArrayUtils.asList();
      List list2 = ArrayUtils.asList();
      // 如果期望 list2 是一个 Integer 类型的列表，而不是 Object 列表，强制声明泛型类型
      // List<Integer> list2 = ArrayUtils.<Integer>asList();
      // 当它发现多个元素的实际类型不一致时就会直接确认泛型类型是 Object
      List list3 = ArrayUtils.asList(1, 2, 3.0);
  }
  ```

* 建议 96：不同的场景使用不同的泛型通配符

  泛型结构只参与“读”操作则限定上界（extends 关键字）

  泛型结构只参与“写”操作则限定下界（super 关键字）

  泛型结构既用作“读”操作又用作“写”操作，使用确定的泛型类型即可

  对于是要限定上界还是限定下界，JDK 的 Collections.copy 方法是一个非常好的例子，它实现了把源列表中的所有元素拷贝到目标列表中对应的索引位置上，代码如下：

  ```java
  public static <T> void copy(List<? super T> dest, List<? extends T> src) {
      for (int i = 0; i < srcSize; i++) {
          dest.set(i, src.get(i));
      }
  }
  ```

* 建议 97：警惕泛型是不能协变和逆变的

  在编程语言的类型框架中，协变和逆变是指宽类型和窄类型在某种情况下（如参数、泛型、返回值）替换或交换的特性，简单地说，协变（覆写、多态）是用一个窄类型替换宽类型，而逆变（重载）则是用一个宽类型替换窄类型。

  泛型不支持协变

  ```java
  // 数组支持协变
  Number[] n = new Integer[10];
  // 编译不通过，泛型不支持协变
  List<Number> ln = new ArrayList<Integer>();
  ```

  Java 为了保证运行期的安全性，必须保证泛型参数类型是固定的，所以它不允许一个泛型参数可以同时包含两种类型，即使是父子类关系也不行。

  使用通配符模拟协变：`List<? extends Number> ln = new ArrayList<Integer>();`

  通配符只是在编码期有效，运行期必须是一个确定类型。

  泛型不支持逆变

  Java 虽然可以允许逆变存在，但在对类型赋值上是不允许逆变的，你不能把一个父类实例对象赋值给一个子类类型变量，泛型自然也不允许此种情况发生了。

* 建议 98：建议采用的顺序是 List\<T\>、List\<?\>、List\<Object\>

* 建议 99：严格限定泛型类型采用多重界限

  `public static <T extends Staff & Passenger> void discount(T t) {}`

  使用 & 符号设定多重边界，指定泛型类型 T 必须是 Staff 和 Passenger 的共有子类型，此时变量 t 就具有了所有限定的方法和属性。

  在 Java 泛型中，可以使用 & 符号关联多个上界并实现多个边界限定，而且只有上界才有此限定，下界没有多重限定的情况。

* 建议 100：数组的真实类型必须是泛型类型的子类型

  ```java
  public static <T> T[] toArray(List<T> list) {
      // 泛型是类型擦除的
      // Object[] t = (Object[])new Object[list.size()];
      T[] t = (T[])new Object[list.size()];
      for (int i = 0, n = list.size(); i < n; i++) {
          t[i] = list.get(i);
      }
      return t;
  }
  main() {
      List<String> list = Arrays.asList("A", "B");
      // ClassCastException
      // for (String str : (String[])toArray(list)) {}
      for (String str : toArray(list)) {}
  }
  ```

  为什么 Object 数组不能向下转型为 String 数组

  数组是一个容器，只有确保容器内的所有元素类型与期望的类型有父子关系时才能转换，Object 数组只能保证数组内的元素是 Object 类型，却不能确保它们都是 String 的父类型或子类，所以类型转换失败。

  其实，要想把一个 Object 数组转换为 String 数组，只有 Object 数组的实际类型是 String 就可以了。

  ```java
  Object[] objArray = {"A", "B"};
  // ClassCastException
  String[] strArray = (String[])objArray;
  
  String[] ss = {"A", "B"};
  Object[] objs = ss;
  String[] strs = (String[])objs;
  ```

  当一个泛型类（特别是泛型集合）转变为泛型数组时，泛型数组的真实类型不能是泛型类型的父类型（比如顶层类 Object），只能是泛型类型的子类型（当然包括自身类型），否则就会出现类型转换异常。

  ```java
  public static <T> T[] toArray(List<T> list, Class<T> tClass) {
      T[] t = (T[])Array.newInstance(tClass, list.size());
      for (int i = 0, n = list.size(); i < n; i++) {
          t[i] = list.get(i);
      }
      return t;
  }
  ```

* 建议 101：注意 Class 类的特殊性

  Java 使用一个元类（MetaClass）来描述加载到内存中的类数据，这就是 Class 类，它是一个描述类的类对象。因为 Class 类是“类中类”，也就有预示着它有很多特殊的地方：

  无构造函数。Java 中的类一般都有构造函数，用于创建实例对象，但是 Class 类却没有构造函数，不能实例化，Class 对象是在加载类时由 Java 虚拟机通过调用类加载器中的 defineClass 方法自动构造的。

  可以描述基本类型。虽然 8 个基本类型在 JVM 中并不是一个对象，它们一般存在于栈内存中，但是 Class 类仍然可以描述它们，例如可以使用 int.class 表示 int 类型的类对象。

  其对象都是单例模式。一个 Class 的实例对象描述一个类，并且只描述一个类，反过来也成立，一个类只有一个 Class 实例对象。

  `String.class.equals(new String().getClass())`

  Class 类是 Java 的反射入口，只有在获得了一个类的描述对象后才能动态地加载、调用，一般获得一个 Class 对象有三种途径：

  类属性方式，如 String.class。

  对象的 getClass 方法，如 new String().getClass()。

  forName 方法加载，如 Class.forName("java.lang.String")

* 建议 102：适时选择 getDeclaredXXX 和 getXXX

  getMethod 方法获得的是所有 public 访问级别的方法，包括从父类继承的方法，而 getDeclaredMethod 获得的是自身类的所有方法，包括公有方法、私有方法等，而且不受限于访问权限。

* 建议 103：反射访问属性或方法时将 Accessible 设置为 true

  Accessible 的属性并不是我们语法层级理解的访问权限，而是指是否更容易获得，是否进行安全检查。

  动态修改一个类或方法或执行方法时都会受 Java 安全体制的制约，而安全的处理是非常消耗资源的（性能非常低），因此对于运行期要执行的方法或要修改的属性就提供了 Accessible 可选项：由开发者决定是否要逃避安全体系的检查。

  当然了，由于取消了安全检查，也可以运行 private 方法、访问 private 私有属性了。

* 建议 104：使用 forName 动态加载类文件

  动态加载（Dynamic Loading）是指在程序运行时加载需要的类库文件，对 Java 程序来说，一般情况下，一个类文件在启动时或首次初始化时会被加载到内存中，而反射则可以在运行时再决定是否要加载一个类。

  一个类文件只有在被加载到内存中后才可能生成实例对象，也就是说一个对象的生成必然会经过以下两个步骤：加载到内存中生成 Class 的实例对象；通过 new 关键字生成实例对象。

  动态加载的意义在于：加载一个类即表示要初始化该类的 static 变量，特别是 static 代码块，在这里我们可以做大量的工作，比如注册自己，初始化环境等。

  需要说明的是，forName 只是把一个类加载到内存中，并不保证由此产生一个实例对象，也不会执行任何方法，之所以会初始化 static 代码，那是由类加载机制所决定的。

* 建议 105：动态加载不适合数组

  如果 forName 要加载一个类，那它首先必须是一个类——8 个基本类型排除在外，它们不是一个具体的类；其次，它必须具有可追索的类路径，否则就会报 ClassNotFoundException。

  在 Java 中，数组是一个非常特殊的类，虽然它是一个类，但没有定义类路径。

  `Class.forName("[Ljava.lang.String;");`

  `Class.forName("[J");`

  只是把一个 String 类型的数组类和 long 类型的数组类加载到了内存中（如果内存中没有该类的话），并不能通过 newInstance 方法生成一个实例对象，因为它没有定义数组的长度，在 Java 中数组是定长的，没有长度的数组是不允许存在的。

  `String[] strs = (String[])Array.newInstance(String.class, 8);`

* 建议 106：动态代理可以使代理模式更加灵活

* 建议 107：使用反射增加装饰模式的普适性

* 建议 108：反射让模板方法模式更强大

  模板方法模式的定义是：定义一个操作中的算法骨架，将一些步骤延迟到子类中，使子类不改变一个算法的结构即可重定义该算法的某些特定步骤。

  在一般的模板方法模式中，抽象模板需要定义一系列的基本方法，一般都是 protected 访问级别的，并且是抽象方法，这标志着子类必须实现这些基本方法，这对子类来说既是一个约束也是一个负担。但是使用了反射后，不需要定义任何抽象方法，只需定义一个基本方法鉴别器即可加载符合规则的基本方法。

  ```java
  private boolean isInitDataMethod(Method m) {
      return m.getName().startsWith("init")
          && Modifier.isPublic(m.getModifiers())
          && m.getReturnType().equals(Void.TYPE)
          && !m.isVarArgs()
          && !Modifier.isAbstract(m.getModifiers());
  }
  ```

  Junit 4 之前要求测试的方法名必须是以 test 开头的，并且无返回值、无参数，而且被 public 修饰，其实现的原理与此非常相似。

* 建议 109：不需要太多关注反射效率

### 第八章 异常

* 建议 110：提倡异常封装

  提高系统的友好性：可以把异常的阅读者分为两类，开发人员和用户。开发人员查找问题，需要打印出堆栈信息，而用户则需要了解具体的业务原因。

  提高系统的可维护性：对异常进行分类处理，并进行封装输出。

  解决 Java 异常机制自身缺陷：Java 中的异常一次只能抛出一个，使用自行封装的异常。

* 建议 111：采用异常链传递异常

  ```java
  public class IOException extends Exception {
      public IOException() { super(); }
      public IOException(String message) { super(message); }
      public IOException(String message, Throwable cause) { super(message, cause); }
      // 上一个层级的异常可以通过异常链进行传递
      public IOException(Throwable cause) { super(cause); }
  }
  ```

  异常需要封装和传递，我们在进行系统开发时不要“吞噬”异常，也不要“赤裸裸”地抛出异常，封装后再抛出，或者通过异常链传递，可以达到系统更健壮、友好的目的。

* 建议 112：受检异常尽可能转化为非受检异常

  受检异常是正常逻辑的一种补偿处理手段，特别是对可靠性要求比较高的系统来说，在某些条件下必须抛出受检异常以便由程序进行补偿处理。

  受检异常确实有不足的地方：

  受检异常使接口声明脆弱：这里产生了两个问题，一是异常是主逻辑的补充逻辑，修改一个补充逻辑，就会导致主逻辑也被修改，也就是出现了实现类“逆影响”接口的情景，我们知道实现类是不稳定的，而接口是稳定的，一旦定义了异常，则增加了接口的不稳定性，这是对面向对象设计的严重亵渎；二是实现的类变更最终会影响到调用者，破坏了封装性，这也是迪米特法则所不能容忍的。

  受检异常使代码的可读性降低：一个方法增加了受检异常，则必须有一个调用者对异常进行处理。

  受检异常增加了开发工作量：异常需要封装和传递。

  受检异常提出的是“法律下的自由”，必须遵守异常的约定才能自由编写代码。非受检异常则是“协约性质的自由”，你必须告诉我你要抛出什么异常，否则不会处理。

  当受检异常威胁到系统的安全性、稳定性、可靠性、正确性时，不能转换为非受检异常。

* 建议 113：不要在 finally 块中处理返回值

  覆盖了 try 代码块中的 return 返回值

  屏蔽异常

  finally 是用来做异常的收尾处理的，一旦加上了 return 语句就会让程序的复杂度陡然提升，而且会产生一些隐蔽性非常高的错误。

* 建议 114：不要在构造函数中抛出异常

  构造函数抛出错误是程序员无法处理的

  构造函数不应该抛出非受检异常

  加重了上层代码编写者的负担；后续代码不会执行。

  构造函数尽可能不要抛出受检异常

  导致子类代码膨胀：子类的无参构造函数不能省略，原因是父类的无参构造函数抛出了 IOException 异常，子类的无参构造函数默认调用的是父类的构造函数，所以子类的无参构造函数也必须抛出 IOException 或其父类。

  违背了里氏替换原则：父类出现的地方子类就可以出现，而且将父类替换为子类也不会产生任何异常。Sub 的构造函数抛出了 Exception 异常，它比父类的构造函数抛出的异常范围要宽，必须增加新的 catch 块才能解决。

  子类构造函数扩展受限：父类构造函数抛出异常会让子类构造函数的灵活性大大降低。构造函数 Sub 中没有把 super() 放在第一句话中，想把父类的异常重新包装后再抛出是不可行的。

* 建议 115：使用 Throwable 获得栈信息

  JVM 在创建一个 Throwable 类及其子类时会把当前线程的栈信息记录下来，以便在输出异常时准确定位异常原因。

  在出现异常时（或主动声明一个 Throwable 对象时），JVM 会通过 fillInStackTrace 方法记录下栈帧信息，然后生成一个 Throwable 对象，这样我们就可以知道类间的调用顺序、方法名称及当前行号等了。

  获得栈信息可以对调用者进行判断，然后决定不同的输出。

* 建议 116：异常只为异常服务

  异常只能用在非正常的情况下，不能成为正常情况的主逻辑。

  ```java
  try {
      Enum.valueOf(c, name);
  } catch (IllegalArgumentException e) {
      
  }
  ```

  异常判断降低了系统性能。

  降低了代码的可读性，只有详细了解 valueOf 方法的人才能读懂这样的代码。

  隐藏了运行期可能产生的错误，catch 到异常，但没有做任何处理。

* 建议 117：多使用异常，把性能问题放一边

  这样具备完整例外情景的逻辑就具备了 OO 的味道，任何一个事物的处理都可能产生非预期结果，问题是需要以何种手段来处理，如果不使用异常就需要依靠返回值的不同来进行处理了，这严重失去了面向对象的风格。

