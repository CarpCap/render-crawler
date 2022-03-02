#	 基于selenium技术的爬虫服务

##	功能

抓取html渲染后的数据

##	设计





##	直接使用公库docker

docker run -di -p 10023:10023 --name scrawler caiquan/selenium-crawler



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

