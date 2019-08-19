## oauth2-demo
### 概述
使用oauth2保护你的应用，可以分为简易的分为三个步骤

* 配置资源服务器
* 配置认证服务器
* 配置spring security

前两点是oauth2的主体内容，spring security oauth2是建立在spring security基础之上的，所以有一些体系是公用的。

oauth2根据使用场景不同，分成了4种模式

* 授权码模式（authorization code）
* 简化模式（implicit）
* 密码模式（resource owner password credentials）
* 客户端模式（client credentials）

本例使用密码模式（以下简称password模式）和客户端模式（以下简称client模式）。
### 例子
例子使用的postman，对应文件在postman目录下
#### password模式获取token
passwordTypeGetToken
(https://github.com//master/png/passwordTypeGetToken.png)
#### client模式获取token
clientTypeGetToken
(https://github.com//master/png/clientTypeGetToken.png)
#### 资源访问权限
因为使用的是1.5X的springboot，所以application.yml中配置了
```
security:
  oauth2:
    resource:
      filter-order: 3
 ```
原理是将资源过滤器链提升到springsecurity的过滤器链之前，否则，/order/{id}的认证和鉴权将会被 springSecurityFilterChain 拦截，不被oauth2相关的 过滤器链处理，你应当了解一个 springSecurity 的设计：一次请求，只会被 FilterProxyChain 中的最多一个过滤器链处理。

对权限具体的应用可以参考如下
```
                    //不限制使用权限
//                    .antMatchers("/product/**").anonymous()
                    //限制必须满足hasScope('select')权限才能访问
                    .antMatchers("/product/**").access("#oauth2.hasScope('select')")
                    //配置order访问控制，只要认证就可以访问
                    .antMatchers("/order/**").authenticated(); 
```