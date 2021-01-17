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

***What Does the Spring Bean Lifecycle Look Like?***

Spring framework provides following **4 ways for controlling life cycle events** of a bean:

`InitializingBean` and `DisposableBean` callback interfaces

*Aware interfaces for specific behavior

Custom `init()` and `destroy()` methods in bean configuration file

`@PostConstruct` and `@PreDestroy` annotations

[***An Intro to the Spring DispatcherServlet***](https://www.baeldung.com/spring-dispatcherservlet)

Simply put, in the *Front Controller* design pattern*,* a single controller is **responsible for directing incoming *HttpRequests* to all of an application's other controllers and handlers**.

Spring's *DispatcherServlet* implements this pattern and is, therefore, responsible for correctly coordinating the *HttpRequests* to their right handlers.