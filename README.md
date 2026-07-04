# Render Crawler

渲染页面抓取器，可获取 **JS 渲染后的完整网页数据，提供截图功能**

基于 **Selenium** 驱动的浏览器自动化框架，该技术能够高效、完整地获取由 JavaScript (JS) 动态渲染生成的网页数据，网络爬虫。

解决直接发起 HTTP 请求无法加载前端渲染内容的问题。





## 🍕  在线体验

海外服务器，延迟比较高

https://rc.carpcap.com/

## 🌭  功能说明


比如：通过http直接请求baidu
```shell
curl https://www.bilibili.com
```

返回结果大概是这样的，本身因为没有任何请求头以及js执行，本质只是一次get请求，信息量极少。

```
<!DOCTYPE html><html><head>
<!-- Dejavu Release Version 64940-->
<script>
window._BiliGreyResult = {
  method: "direct",
  versionId: "64940",
}
</script><meta charset="UTF-8"><title>验证码_哔哩哔哩</title><meta name="viewport" content="width=device-width,user-scalable=no,initial-scale=1,maximum-scale=1,minimum-scale=1,viewport-fit=cover"><meta name="spm_prefix" content="333.1291"><script type="text/javascript" src="//www.bilibili.com/gentleman/polyfill.js?features=Promise%2CObject.assign%2CString.prototype.includes%2CNumber.isNaN"></script><script>window._riskdata_ = { 'v_voucher': 'voucher_7bc1a50d-7814-48e3-a50b-ad55208484ba' }</script><script type="text/javascript" src="//s1.hdslb.com/bfs/seed/log/report/log-reporter.js"></script><link href="//s1.hdslb.com/bfs/static/jinkela/risk-captcha/css/risk-captcha.0.3bd977140b994afccbcc4d0102d33b9577e89cc0.css" rel="stylesheet"></head><body><div id="biliMainHeader"></div><div id="risk-captcha-app"></div><script src="//s1.hdslb.com/bfs/seed/jinkela/risk-captcha-sdk/CaptchaLoader.js"></script><script type="text/javascript" src="//s1.hdslb.com/bfs/static/jinkela/risk-captcha/1.risk-captcha.3bd977140b994afccbcc4d0102d33b9577e89cc0.js"></script><script type="text/javascript" src="//s1.hdslb.com/bfs/static/jinkela/risk-captcha/risk-captcha.3bd977140b994afccbcc4d0102d33b9577e89cc0.js"></script></body></html>
```

但如果通过 Render Crawler 

<img src="docs/img1.png" width="500" style="border: 2px solid #ddd; border-radius: 8px;">


---

## 🐳  快速搭建

docker-compose.yml
```docker
version: '3.3'
services:
  render-crawler:
    image: carpcap/render-crawler
    container_name: render-crawler
    environment:
      # Node 容器可以同时处理 6 个会话
      - SE_NODE_MAX_SESSIONS=6
      # 客户端请求新会话的最大等待时间
      # - SE_NODE_SESSION_TIMEOUT=300
    shm_size: 2g
    ports:
      - "10034:10034"
      # - "4444:4444" # 控制端口
      # - "5900:5900" # nvc端口
      # - "7900:7900" # 浏览器端口 默认密码secret
```

访问地址  http://127.0.0.1:10034

swagger http://127.0.0.1:10034/swagger/index.html


## 🚀 代理

代码中内置了 **两套代理池**，用以区分代理策略：

proxy_pool.go

* **CnProxyPool** —— 国内代理池
* **IntlProxyPool** —— 国外代理池

你需要自行实现其中的 `replenish` 方法（项目内已提供示例）。

---

## 🔧 自行编译

项目提供了 `build.ps1` go打包脚本  `Dockerfile`，你可以在修改代码后重新构建镜像：


```cmd
.\build.ps1
```

```bash
docker build -t render-crawler .
```


---

