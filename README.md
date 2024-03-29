# 使用到的技术
1. Elasticsearch 6.2.3, 用于存储用户数据
2. spring-cloud Greenwich zuul用于网关拦截
3. spring boot 2.1.4后台框架
4. 用户登录使用RSA加密，生成token存储在header中

----

## 使用zuul作为网关。
在TestFilter中使用pre，来拦截请求。用户登录信息在header和redis中。所有被zuul拦截的请求都在TestFilter中过滤。

## 使用HandlerInterceptorAdapter作为拦截器。
不经过zuul拦截器的，例如登录请求是被HandlerInterceptorAdapter拦截器拦截。

## 用户登录
用户登录使用RSA加密方式。
1. 首先前端获取公钥，后台产生公钥私钥对，并将公钥私钥存储到redis中，并将公钥返回前端。
2. 前端公钥加密用户密码，将加密后的密码和公钥传递给服务器。
3. 服务器拿到公钥和密码后，从redis中拿到私钥，并拿私钥对密码进行加密，与es中的密码进行认证。
4. 如果认证成功生成token保存在redis中。
5. 前端将获取token，并存放在header中。

## 自定义注释
@Authorization,在Controller的方法上使用此注解,该方法在映射时会检查用户是否登录
CurrentUser,在Controller的方法参数中使用此注解，该方法在映射时会注入当前登录的User对象
