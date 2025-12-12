// router/router.go
package router

import (
	"net/http"
	_ "render-crawler/docs"
	"render-crawler/handler"

	"embed" // 引入内置的 embed 包
	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
)

//go:embed index.html
var content embed.FS

// Router 创建并配置 Gin 路由器
func Router() *gin.Engine {
	// 使用默认的 Gin 配置，包含 Logger 和 Recovery 中间件
	r := gin.Default()
	r.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	htmlContent, err := content.ReadFile("index.html")

	r.GET("/", func(c *gin.Context) {
		// 读取嵌入的 HTML 文件内容
		if err != nil {
			c.String(http.StatusInternalServerError, "Failed to read index.html")
			return
		}
		// 设置 Content-Type 并返回内容
		c.Data(http.StatusOK, "text/html; charset=utf-8", htmlContent)
	})

	api := r.Group("web")
	api.POST("/css", handler.Css)
	api.POST("/xpath", handler.Xpath)
	api.POST("/time", handler.Time)
	return r
}
