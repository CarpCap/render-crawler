package mods

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

// JsonResult 定义了统一的 HTTP 响应结构
type JsonResult struct {
	Code int         `json:"code"` // 业务错误码，200 表示成功
	Msg  string      `json:"msg"`  // 消息，用于描述结果
	Data interface{} `json:"data"` // 返回的数据
}

// Success 成功响应
func Success(c *gin.Context, data interface{}) {
	c.JSON(http.StatusOK, JsonResult{
		Code: 200,
		Msg:  "Success",
		Data: data,
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
