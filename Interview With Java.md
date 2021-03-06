# Interview With Java

### Spring

[***Spring Framework Modules***](https://docs.spring.io/spring-framework/docs/5.0.0.RC3/spring-framework-reference/overview.html#overview-modules)

**Core Container**

The *Core Container* consists of the `spring-core`, `spring-beans`, `spring-context`, `spring-context-support`, and `spring-expression` (Spring Expression Language) modules.

The `spring-core` and `spring-beans` modules provide the fundamental parts of the framework, including the IoC and Dependency Injection features.

The *Context* (`spring-context`) module builds on the solid base provided by the [*Core and Beans*](https://docs.spring.io/spring-framework/docs/5.0.0.RC3/spring-framework-reference/core.html#beans-introduction) modules: it is a means to access objects in a framework-style manner that is similar to a JNDI registry.

**Web**

The *Web* layer consists of the `spring-web`, `spring-webmvc` and `spring-websocket` modules.

The `spring-web` module provides basic web-oriented integration features such as multipart file upload functionality and the initialization of the IoC container using Servlet listeners and a web-oriented application context.

The `spring-webmvc` module (also known as the *Web-Servlet* module) contains Spring’s model-view-controller (*MVC*) and REST Web Services implementation for web applications.

[***Comparing Spring AOP and Aspect***](https://www.baeldung.com/spring-aop-vs-aspectj)

**Weaving**

Both AspectJ and Spring AOP uses the different type of weaving which affects their behavior regarding performance and ease of use.

AspectJ makes use of three different types of weaving:

**Compile-time weaving**: The AspectJ compiler takes as input both the source code of our aspect and our application and produces a woven class files as output

**Post-compile weaving**: This is also known as binary weaving. It is used to weave existing class files and JAR files with our aspects

**Load-time weaving**: This is exactly like the former binary weaving, with a difference that weaving is postponed until a class loader loads the class files to the JVM

As AspectJ uses compile time and classload time weaving, **Spring AOP makes use of runtime weaving**.

With runtime weaving, the aspects are woven during the execution of the application using proxies of the targeted object – using either JDK dynamic proxy or CGLIB proxy.

**Performance**

As far as performance is concerned, **compile-time weaving is much faster than runtime weaving**. Spring AOP is a proxy-based framework, so there is the creation of proxies at the time of application startup. Also, there are a few more method invocations per aspect, which affects the performance negatively.

On the other hand, AspectJ weaves the aspects into the main code before the application executes and thus there's no additional runtime overhead, unlike Spring AOP.

[***What Is the Difference Between Beanfactory and Applicationcontext?***](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans)

The `org.springframework.beans` and `org.springframework.context` packages are the basis for Spring Framework’s IoC container. The `BeanFactory` interface provides an advanced configuration mechanism capable of managing any type of object. `ApplicationContext` is a sub-interface of `BeanFactory`. It adds:

- Easier integration with Spring’s AOP features
- Message resource handling (for use in internationalization)
- Event publication
- Application-layer specific contexts such as the `WebApplicationContext` for use in web applications.

The `org.springframework.context.ApplicationContext` interface represents the Spring IoC container and is responsible for instantiating, configuring, and assembling the beans. The container gets its instructions on what objects to instantiate, configure, and assemble by reading configuration metadata. The configuration metadata is represented in XML, Java annotations, or Java code. It lets you express the objects that compose your application and the rich interdependencies between those objects.

[***What Does the Spring Bean Lifecycle Look Like?***]()

Spring framework provides following **4 ways for controlling life cycle events** of a bean:

`InitializingBean` and `DisposableBean` callback interfaces

*Aware interfaces for specific behavior

Custom `init()` and `destroy()` methods in bean configuration file

`@PostConstruct` and `@PreDestroy` annotations

[***An Intro to the Spring DispatcherServlet***](https://www.baeldung.com/spring-dispatcherservlet)

Simply put, in the *Front Controller* design pattern*,* a single controller is **responsible for directing incoming *HttpRequests* to all of an application's other controllers and handlers**.

Spring's *DispatcherServlet* implements this pattern and is, therefore, responsible for correctly coordinating the *HttpRequests* to their right handlers.

[***Intro to Inversion of Control and Dependency Injection with Spring***](https://www.baeldung.com/inversion-control-and-dependency-injection-in-spring)

**What Is Inversion of Control?**

Inversion of Control is a principle in software engineering by which the control of objects or portions of a program is transferred to a container or framework.

By contrast with traditional programming, in which our custom code makes calls to a library, IoC enables a framework to take control of the flow of a program and make calls to our custom code. To enable this, frameworks use abstractions with additional behavior built in. **If we want to add our own behavior, we need to extend the classes of the framework or plugin our own classes.**

**What Is Dependency Injection?**

Dependency injection is a pattern through which to implement IoC, where the control being inverted is the setting of object's dependencies.

The act of connecting objects with other objects, or “injecting” objects into other objects, is done by an assembler rather than by the objects themselves.

**The Spring IoC Container**

In the Spring framework, the IoC container is represented by the interface *ApplicationContext*. The Spring container is responsible for instantiating, configuring and assembling objects known as *beans*, as well as managing their lifecycle.

[***HandlerAdapters in Spring MVC***](https://www.baeldung.com/spring-mvc-handler-adapters#what-is-a-handleradapter)

The servlet doesn't invoke the method directly – it basically serves as a bridge between itself and the handler objects, leading to a loosely coupling design.

**Let the specific processor decouple from the DispatcherServlet in order to comply with the open and close principle**

If we want to add a new type of processor, we must continue to write else if for processing, but the DispatcherServlet does not need to change any code after using the processor adapter, because it only depends on the HandlerAdapter, so that the DispatcherServlet and the specific Handler are uncoupled, they can be developed independently before.

[***A Guide to Spring AbstractRoutingDatasource***](https://www.baeldung.com/spring-abstract-routing-data-source)

```java
public class ClientDataSourceRouter extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return ClientDatabaseContextHolder.getClientDatabase();
    }
}
```

### Spring Boot

***FAT JAR 和 WAR 执行模块——spring-boot-loader***

按照 Java 官方文档的规定，java -jar 命令引导的具体启动类必须配置在 MANIFEST.MF 资源的 Main-Class 属性中：

[Launcher Manifest](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-executable-jar-format.html#executable-jar-launcher-manifest)

MainMethodRunner#run() 方法

```java
public void run() throws Exception {
	Class<?> mainClass = Thread.currentThread().getContextClassLoader().loadClass(this.mainClassName);
	Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
	mainMethod.invoke(null, new Object[] { this.args });
}
```

JarLauncher 实际上是同进程内调用 Start-Class 类的 main(String[]) 方法，并且在启动前准备好 Class Path。

There are three launcher subclasses (`JarLauncher`, `WarLauncher`, and `PropertiesLauncher`). Their purpose is to load resources (`.class` files and so on) from nested jar files or war files in directories (as opposed to those explicitly on the classpath).

[The Executable Jar File Structure](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-executable-jar-format.html#executable-jar-jar-file-structure)

[The Executable War File Structure](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-executable-jar-format.html#executable-jar-war-file-structure)

WEB-INF/lib-provided 目录存放的是 \<scope\>provided\</scope\> 的 JAR 文件（spring-boot-loader、spring-boot-starter-tomcat）。

传统的 Servlet 应用的 Class Path 路径仅关注 WEB-INF/classes 和 WEB-INF/lib 目录，因此，WEB-INF/lib-provided 中的 JAR 将被 Servlet 容器忽略。这样设计的好处在于，打包后的 WAR 文件能够在 Servlet 容器中兼容运行（避免 JAR 冲突）。

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```

***spring-boot-starter-parent 与 spring-boot-dependencies***

\<dependencyManagement\> 导入 spring-boot-dependencies 的方式尽管与 spring-boot-starter-parent 方法同源，然而本方式仅关注 \<dependencyManagement\>，所以 maven-war-plugin 采用的版本为 2.2，[ERROR] Failed to execute goal org.apache.maven.plugins:maven-war-plugin:2.2:war (default-war) on project demo: Error assembling WAR: webxml attribute is required (or pre-existing WEB-INF/web.xml if executing in update mode) -> [Help 1]，因此将 maven-war-plugin:\<maven-war-plugin.version\> 添加到项目的 pom.xml 文件中。

xxx.war 中没有主清单属性：spring-boot-maven-plugin 插件未指定版本；需添加 \<goal\> 为 repackage。

[How does Maven add default plugins to my project?](https://mincong.io/2017/11/07/maven-plugins-understanding/#2-how-does-maven-add-default-plugins-to-my-project)

However, is there any plugin enabled when running any Maven command? In order to check this, we need to check the effective POM. This can be achieved by using `mvn help:effective-pom`.

Actually, all the effective plugins are generated based on [Plugin Bindings for default Lifecycle Reference](http://maven.apache.org/ref/3.6.0/maven-core/default-bindings.html).

That’s why you don’t see them in the project’s POM, but only in the effective POM.

***嵌入式 Servlet Web 容器***

[**Deploy a Spring Boot WAR into a Tomcat Server**](https://www.baeldung.com/spring-boot-war-tomcat-deploy)

We should consider that this new setup makes our Spring Boot application a non-standalone application (if you would like to have it working in standalone mode again, remove the *provided* scope from the tomcat dependency).

[**embedded Tomcat**](https://www.theserverside.com/definition/embedded-Tomcat)

An embedded Tomcat server consists of a single Java web application along with a full Tomcat server distribution, packaged together and compressed into a single JAR, WAR or ZIP file.

One of the most common ways to create an embedded Tomcat file is to use the Maven-Tomcat plugin for a build.

实际上，Tomcat Maven 插件并非嵌入式 Tomcat，仍旧利用了传统 Tomcat 容器部署方式，先将 Web 应用打包为 ROOT.war 文件，然后在 Tomcat 应用启动的过程中，将 ROOT.war 文件解压至 webapps 目录。

反观 Spring Boot 2.0 的实现，它利用嵌入式 Tomcat API 构建为 TomcatWebServer Bean，由 Spring 应用上下文将其引导，其嵌入式 Tomcat 组件的运行（如 Context、Connector 等），以及 ClassLoader 的装载均由 Spring Boot 框架代码实现。

***自动装配***

[**Difference between <context:annotation-config> vs <context:component-scan>**](https://www.baeldung.com/spring-contextannotation-contextcomponentscan)

The \<context:annotation-config\> annotation is mainly used to activate the dependency injection annotations. @Autowired, @Qualifier, @PostConstruct, @PreDestroy, and @Resource are some of the ones that \<context:annotation-config\> can resolve.

Before going further let's point out that we still need to declare beans in the XML. That is because \<context:annotation-config\> activates the annotations only for the beans already registered in the application context.

Moreover, \<context:component-scan\> recognizes bean annotations that \<context:annotation-config\> doesn't detect.

[**Annotation Type Configuration**](https://docs.spring.io/spring-framework/docs/4.0.4.RELEASE/javadoc-api/org/springframework/context/annotation/Configuration.html)

Indicates that a class declares one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.

**Bootstrapping @Configuration classes**

Via AnnotationConfigApplicationContext

Via Spring \<beans\> XML

Via component scanning

[**Understanding Auto-configured Beans**](https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/boot-features-developing-auto-configuration.html#boot-features-understanding-auto-configured-beans)

Under the hood, auto-configuration is implemented with standard `@Configuration` classes. Additional `@Conditional` annotations are used to constrain when the auto-configuration should apply. Usually, auto-configuration classes use `@ConditionalOnClass` and `@ConditionalOnMissingBean` annotations. This ensures that auto-configuration applies only when relevant classes are found and when you have not declared your own `@Configuration`.

You can browse the source code of `spring-boot-autoconfigure` to see the `@Configuration` classes that Spring provides (see the `META-INF/spring.factories` file).

Spring Framework 无法自动装配 @Configuration 类。为此，Spring Boot 1.0 在 Spring Framework 4.0 的基础上，添加了约定配置化导入 @Configuration 类的方式。