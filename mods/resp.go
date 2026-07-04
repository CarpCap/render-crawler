package mods

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

// JsonResult 统一的 HTTP 响应结构
type JsonResult struct {
	Code       int         `json:"code"`                 // 业务码，200 表示成功
	Msg        string      `json:"msg"`                  // 结果消息
	Data       interface{} `json:"data"`                 // HTML 页面源码
	Screenshot string      `json:"screenshot,omitempty"` // base64 PNG 截图，未请求时为空
}

// Success 成功响应
func Success(c *gin.Context, data interface{}) {
	c.JSON(http.StatusOK, JsonResult{
		Code: 200,
		Msg:  "Success",
		Data: data,
	})
}

// SuccessWithScreenshot 成功响应（含截图）
func SuccessWithScreenshot(c *gin.Context, html string, screenshot string) {
	c.JSON(http.StatusOK, JsonResult{
		Code:       200,
		Msg:        "Success",
		Data:       html,
		Screenshot: screenshot,
	})
}

// Fail 失败响应（通常用于业务错误）
func Fail(c *gin.Context, code int, msg string) {
	c.JSON(http.StatusOK, JsonResult{ // 即使是业务失败，HTTP状态码也可以是 200，由 Code 字段区分
		Code: code,
		Msg:  msg,
		Data: nil,
	})
}

// InternalError 服务器内部错误（HTTP 500 错误）
func InternalError(c *gin.Context, msg string) {
	c.JSON(http.StatusInternalServerError, JsonResult{
		Code: 500, // 或自定义一个内部错误码
		Msg:  msg,
		Data: nil,
	})
}
