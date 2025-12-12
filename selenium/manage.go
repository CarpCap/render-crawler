package selenium

import (
	"fmt"
	"render-crawler/config"
	"time"
)

// 清理掉活跃的浏览器
func StartAutoCleanup() {
	fmt.Println("启动自动清理...")

	go func() {
		ticker := time.NewTicker(time.Duration(config.Cfg.Web.WebCheckInterval) * time.Second)
		defer ticker.Stop() // 只在协程退出时调用

		for range ticker.C {
			//fmt.Println("开始清理 ", time.Now().Format("15:04:05"))
			for _, web := range SelectedKeys() {
				fmt.Println("清理浏览器 ", web.WebDriver)
				web.CloseSelenium()
			}
			//fmt.Println("清理结束 ", time.Now().Format("15:04:05"))
		}

		// 这里永远不会执行到
		fmt.Println("定时器结束")
	}()
}

// 如果超过限制 直接干掉
func verifyFailWeb(web *Web) {
	if web.RequestSum >= config.Cfg.Web.WebCheckRequestSum || web.FailSum >= config.Cfg.Web.WebCheckFailSum {
		web.CloseSeleniumNoSafe()
	}
}
