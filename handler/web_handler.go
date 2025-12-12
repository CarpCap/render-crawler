package handler

import (
	"github.com/gin-gonic/gin"
	"render-crawler-go/mods"
	"render-crawler-go/selenium"
)

// @Summary	Css定位
// @Tags selenium
// @Accept	json
// @Produce json
// @Param body body mods.Css true " "
// @Router	/web/css [post]
func Css(c *gin.Context) {
	var req mods.Css
	if !mods.BindJSON(c, &req) {
		return // 绑定失败，直接返回
	}

	result, err := call(req.Url, req.ProxyPoolType, req.Css, selenium.CSS, req.WebReq.PageLoadStrategy, req.WebReq.WaitTime)

	if err != nil {
		mods.Fail(c, 500, err.Error())
		return
	}

	mods.Success(c, result)

}

// @Summary	Xpath定位
// @Tags selenium
// @Accept	json
// @Produce json
// @Param body body mods.Xpath true " "
// @Router	/web/xpath [post]
func Xpath(c *gin.Context) {
	var req mods.Xpath
	if !mods.BindJSON(c, &req) {
		return // 绑定失败，直接返回
	}

	result, err := call(req.Url, req.ProxyPoolType, req.Xpath, selenium.XPATH, req.WebReq.PageLoadStrategy, req.WebReq.WaitTime)

	if err != nil {
		mods.Fail(c, 500, err.Error())
		return
	}

	mods.Success(c, result)
}

// @Summary	Time
// @Tags selenium
// @Accept	json
// @Produce json
// @Param body body mods.WebReq true " "
// @Router	/web/time [post]
func Time(c *gin.Context) {
	var req mods.WebReq
	if !mods.BindJSON(c, &req) {
		return // 绑定失败，直接返回
	}

	result, err := call(req.Url, req.ProxyPoolType, "", selenium.TIME, req.PageLoadStrategy, req.WaitTime)

	if err != nil {
		mods.Fail(c, 500, err.Error())
		return
	}

	mods.Success(c, result)
}

func call(url string, proxyType mods.ProxyPoolType, locateValue string, locateType selenium.LocateType, strategy mods.PageLoadStrategy, pageLoadTimeout int) (string, error) {
	web := selenium.GetAvailableSelenium(proxyType, strategy)

	if web == nil {
		web = selenium.CreateSelenium(proxyType, strategy, proxyType)

	}

	return web.GetPageSource(url, locateValue, locateType, pageLoadTimeout)

}
