package mods

import "github.com/gin-gonic/gin"

type WebReq struct {
	// 请求路径，必须携带http头
	Url string `json:"url" binding:"required" `
	//加载类型
	PageLoadStrategy PageLoadStrategy `json:"pageLoadStrategy" default:"normal"`
	// 代理类型 off cn intl
	ProxyPoolType ProxyPoolType `json:"proxyPoolType"  default:"off"`
	// 等待时间 默认10秒
	WaitTime int `json:"waitTime" binding:"required" default:"10"`
}

// Css定位器
type Css struct {
	WebReq
	// css选择器
	Css string `json:"css" binding:"required"`
}

type Xpath struct {
	WebReq
	// xpath选择器
	Xpath string `json:"xpath" binding:"required" `
}

// BindJSON 通用 JSON 参数绑定函数。
// reqPtr 必须是一个指针类型，指向 Gin 请求期望绑定的结构体。
func BindJSON(c *gin.Context, reqPtr any) bool {
	// 尝试绑定 JSON 数据到 reqPtr
	if err := c.ShouldBindJSON(reqPtr); err != nil {
		// 如果绑定失败，记录错误并返回统一的参数错误响应
		// 推荐返回 HTTP 400 Bad WebReq，而非 500 Internal Server Error
		Fail(c, 400, "请求参数格式或内容错误")
		return false // 绑定失败
	}
	return true // 绑定成功
}
