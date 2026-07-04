# Render Crawler — AGENTS.md

## 项目概述

基于 **Selenium** 驱动的浏览器自动化框架，通过 REST API 获取 **JavaScript 渲染后** 的完整网页数据。
解决直接发起 HTTP 请求无法加载前端渲染内容的问题。

**技术栈：** Go 1.24、Gin、Selenium WebDriver、Chrome standalone、Viper、Swaggo

---

## 项目结构

```
render-crawler/
├── main.go                  # 入口：初始化配置、启动清理协程、注册路由
├── config.toml              # 默认配置文件
├── app.conf                 # Supervisor 进程管理配置（Docker 内部用）
├── config/
│   └── config.go            # Viper 加载 TOML，支持 --config 参数
├── handler/
│   └── web_handler.go       # HTTP 处理器，解析请求调用 selenium
├── router/
│   ├── router.go            # Gin 路由：首页、Swagger、/web/css、/web/xpath、/web/time
│   └── index.html           # 嵌入式 API 测试页面
├── mods/
│   ├── req.go               # 请求模型：WebReq、Css、Xpath
│   ├── resp.go              # 统一响应：JsonResult、Success、Fail
│   └── web.go               # 枚举：ProxyPoolType、PageLoadStrategy
├── selenium/
│   ├── web.go               # Web 结构体：WebDriver 创建、页面获取、状态管理
│   ├── manage.go            # 空闲实例自动清理
│   ├── selector.go          # 实例注册表：注册/注销/查找/清理
│   ├── html.go              # 定位策略引擎：CSS / XPath / Time
│   └── proxy_pool.go        # 代理池框架：国内/国际双池
├── docs/                    # Swagger 文档（swaggo 自动生成）
├── Dockerfile
├── docker-compose.yml
├── build.ps1                # 交叉编译 Linux amd64
└── go.mod / go.sum
```

---

## 架构说明

### 请求数据流

```
客户端 POST 请求 → handler → 查找空闲浏览器实例 → (未找到则创建)
→ Web.GetPageSource → wd.Get(url) → 定位策略等待 → 返回 HTML → 封装响应
```

### 浏览器实例管理

- **Web 结构体** 封装 Chrome WebDriver，附带请求计数、失败计数、最后活跃时间、状态锁
- **selector.go** 维护全局注册表，按代理类型和页面加载策略查找可用实例
- **manage.go** 后台协程定期清理空闲超时的实例
- 失败或请求超标的实例自动关闭
- 通过 `sync.Mutex` + `atomic.Bool` 保证并发安全

### 三种定位策略

| 策略 | 路由 | 说明 |
|------|------|------|
| CSS | `/web/css` | 轮询等待 CSS 选择器匹配到元素 |
| XPath | `/web/xpath` | 轮询等待 XPath 找到元素 |
| Time | `/web/time` | goroutine + select 实现固定超时等待 |

### 代理系统

内置 CnProxyPool（国内）和 IntlProxyPool（国际），需自行实现 `Replenish()` 补充代理 IP。

### 页面加载策略

| 值 | 说明 |
|----|------|
| `none` | HTML 下载完成后立即获取 |
| `eager` | 等待 DOMContentLoaded |
| `normal` | 等待所有资源加载完成（默认） |

---

## API

### POST `/web/css`

```json
{"url":"...", "css":"#app", "waitTime":10, "pageLoadStrategy":"normal", "proxyPoolType":"off"}
```

### POST `/web/xpath`

```json
{"url":"...", "xpath":"//*[@id=\"app\"]", "waitTime":10, "pageLoadStrategy":"normal", "proxyPoolType":"off"}
```

### POST `/web/time`

```json
{"url":"...", "waitTime":10, "pageLoadStrategy":"normal", "proxyPoolType":"off"}
```

### 统一响应

```json
{"code":200, "msg":"Success", "data":"<!DOCTYPE html>..."}
```

---

## 开发

```bash
# 直接运行
go run .

# 指定配置
go run . --config=config.toml

# 交叉编译 Linux
.\build.ps1
```

启动后访问 `http://127.0.0.1:10034/` 使用内置测试工具，或访问 `/swagger/index.html`。

---

## 部署

Docker Compose：

```yaml
services:
  render-crawler:
    image: carpcap/render-crawler
    container_name: render-crawler
    environment:
      - SE_NODE_MAX_SESSIONS=6
    shm_size: 2g
    ports:
      - "10034:10034"
```

手动构建：`.\build.ps1` → `docker build -t render-crawler .`

---

## 配置项

| 项 | 默认值 | 说明 |
|---|---|---|
| `listen` | `:10034` | 监听地址 |
| `selenium_path` | `http://127.0.0.1:4444/wd/hub` | WebDriver 地址 |
| `web_check_request_sum` | `60` | 单个实例最大请求数 |
| `web_check_fail_sum` | `2` | 单个实例最大失败数 |
| `web_check_interval` | `10` | 清理检查间隔（秒） |
| `web_check_idle` | `60` | 空闲阈值（秒） |
| `headless` | `true` | 无头模式 |

---

## 注意事项

- Docker 需设置 `shm_size: 2g`，否则 Chrome 可能因共享内存不足 crash
- 需自行实现代理池的 `Replenish()` 接入代理供应商
- 图片加载默认禁用，可在 `initWeb()` 中调整
