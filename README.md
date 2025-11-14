# 介绍

基于 **Selenium** 的动态渲染爬虫，可获取 **JS 渲染后的完整网页数据**，解决直接发起 HTTP 请求无法加载前端渲染内容的问题。

---

## 🚀 代理

代码中内置了 **两套代理池**，用以区分代理策略：

* **AomesticProxyPool.class** —— 国内代理池
* **AbroadProxyPool.class** —— 国外代理池

你需要自行实现其中的 `replenish` 方法（项目内已提供示例）。

---

## 🔧 自行编译

项目提供了 `Dockerfile`，你可以在修改代码后重新构建镜像：

```bash
docker build -t render-crawler .
```

---

## 🐳 使用公共 Docker 镜像

直接运行容器：

```bash
docker run -di -p 10023:10023 --name render-crawler carpcap/render-crawler
```

或使用 **docker-compose**：

```yaml
version: '3.8'

services:
  render-crawler:
    image: carpcap/render-crawler
    container_name: render-crawler
    ports:
      - "10023:10023"
```

---

## 📘 Swagger 文档访问

在浏览器访问：

```
http://127.0.0.1:10023/swagger-ui.html
```

> **注意：请求中的 `url` 参数必须携带完整的 `http/https` 协议头。**

示例：

```json
{
  "css": "#i_cecream",
  "url": "https://www.bilibili.com"
}
```
