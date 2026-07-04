package handler

import (
	"github.com/gin-gonic/gin"
	"render-crawler/mods"
	"render-crawler/selenium"
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
		return
	}

	result, err := call(req.Url, req.ProxyPoolType, req.Css, selenium.CSS, req.WebReq.PageLoadStrategy, req.WebReq.WaitTime, req.WebReq.Screenshot)

	if err != nil {
		mods.Fail(c, 500, err.Error())
		return
	}

	writeResult(c, result, req.WebReq.Screenshot)
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
		return
	}

	result, err := call(req.Url, req.ProxyPoolType, req.Xpath, selenium.XPATH, req.WebReq.PageLoadStrategy, req.WebReq.WaitTime, req.WebReq.Screenshot)

	if err != nil {
		mods.Fail(c, 500, err.Error())
		return
	}

	writeResult(c, result, req.WebReq.Screenshot)
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
		return
	}

	result, err := call(req.Url, req.ProxyPoolType, "", selenium.TIME, req.PageLoadStrategy, req.WaitTime, req.Screenshot)

	if err != nil {
		mods.Fail(c, 500, err.Error())
		return
	}

	writeResult(c, result, req.Screenshot)
}

func call(url string, proxyType mods.ProxyPoolType, locateValue string, locateType selenium.LocateType, strategy mods.PageLoadStrategy, pageLoadTimeout int, screenshot bool) (*mods.PageResult, error) {
	web := selenium.GetAvailableSelenium(proxyType, strategy)

	if web == nil {
		web = selenium.CreateSelenium(proxyType, strategy, proxyType)
	}

	html, screenData, err := web.GetPageSource(url, locateValue, locateType, pageLoadTimeout, screenshot)
	if err != nil {
		return nil, err
	}

	return &mods.PageResult{
		HTML:       html,
		Screenshot: screenData,
	}, nil
}

// writeResult 根据是否截图选择返回格式
// Data 始终为 HTML 源码；截图数据放在顶层 screenshot 字段
func writeResult(c *gin.Context, result *mods.PageResult, screenshot bool) {
	if screenshot {
		mods.SuccessWithScreenshot(c, result.HTML, result.Screenshot)
	} else {
		mods.Success(c, result.HTML)
	}
}
