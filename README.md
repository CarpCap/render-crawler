
# 介绍

基于 Selenium 的动态渲染爬虫，帮你获取 JS 渲染后的完整网页数据。



##	代理
代码中内置了2套代理池，用于区分代理策略

需要自行实现其中的replenish方法（有案例）

AomesticProxyPoolc.lass 国内代理池

AbroadProxyPool.class 国外代理池


## 自行编译
提供了Dockerfile文件修改代码后可以重新编译镜像


##	直接使用公库docker

docker run -di -p 10023:10023 --name scrawler carpcap/render-crawler:1.0.0



##	访问Swagger

http://127.0.0.1:10023/swagger-ui.html



url值需要完整携带 http头

如：

```{
{
 "css": "#i_cecream > div.bili-header.large-header > div.bili-header__channel",
 "url": "http://www.bilibili.com"
}
```

