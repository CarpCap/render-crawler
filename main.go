package main

import (
	"render-crawler/config"
	"render-crawler/router"
	"render-crawler/selenium"
)

// @title			rc api
// @version		1.0
// @description	这是一个使用 Gin 框架和 Swag 生成的 API 文档示例。
func main() {

	config.Init()

	selenium.StartAutoCleanup()
	//初始化路由器
	r := router.Router()

	// ⭐️ 解决方案一：直接允许所有来源 (开发环境推荐)
	//corsCfg := cors.DefaultConfig()
	//corsCfg.AllowAllOrigins = true
	//corsCfg.AllowMethods = []string{"POST", "OPTIONS"}
	//r.Use(cors.New(corsCfg))

	// 运行服务器，默认监听 8080 端口
	r.Run(config.Cfg.Listen)

}
