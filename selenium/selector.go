package selenium

import (
	"fmt"
	"render-crawler-go/config"
	"render-crawler-go/mods"
	"sync"
	"time"
)

// selectedKey 存储所有注册的 Selenium 实例
// 键是 Selenium.ID，值是 *Selenium
var selectedKey = make(map[string]*Web)

// 读写锁，保护 selectedKey 的并发访问
var selectorMutex sync.RWMutex

// Register 注册一个 Selenium 实例
// 对应 Java 的 register(Selenium selenium)
func Register(selenium *Web) {
	// 加写锁，保护 map 的修改操作
	selectorMutex.Lock()
	defer selectorMutex.Unlock()

	// 使用 ID 作为 key 注册
	selectedKey[selenium.WebDriver.SessionID()] = selenium
}

// Unregister 注销一个 Selenium 实例
// 对应 Java 的 unRegister(Selenium selenium)
func Unregister(selenium *Web) {
	// 加写锁，保护 map 的修改操作
	selectorMutex.Lock()
	defer selectorMutex.Unlock()

	// 从 map 中删除
	delete(selectedKey, selenium.WebDriver.SessionID())
}

// GetAvailableSelenium 获取可用的 Selenium 实例
// 对应 Java 的 getAvailableSelenium(ProxyPoolType proxyType, PageLoadStrategy pageLoadType)
func GetAvailableSelenium(proxyType mods.ProxyPoolType, pageLoadType mods.PageLoadStrategy) *Web {
	// 加读锁，保护 map 的读取操作
	selectorMutex.RLock()
	defer selectorMutex.RUnlock()

	for _, web := range selectedKey {
		// 检查条件：ProxyPoolType 匹配, Status 为 false (空闲), PageLoadStrategy 匹配
		if web.ProxyType == proxyType && !web.Status.Load() && web.PageLoadStrategy == pageLoadType {
			fmt.Println("返回web ", web.WebDriver.SessionID(), web.Status.Load())

			// 返回第一个匹配的可用实例
			return web
		}
	}
	return nil // 未找到可用实例
}

// SelectedKeys 活跃性低的 selenium 集合
// 对应 Java 的 selectedKeys()
func SelectedKeys() []*Web {
	// 加读锁
	selectorMutex.RLock()
	// 注意：在函数退出前释放读锁
	defer selectorMutex.RUnlock()

	// 计算活跃时间阈值 (当前时间 - 活跃判定时间)

	inactiveSet := make([]*Web, 0)

	for _, web := range selectedKey {
		// 判定条件：实例最后使用时间 < 活跃时间阈值
		if config.Cfg.Web.WebCheckIdle <= (time.Now().Unix() - web.LastActiveTime) {
			inactiveSet = append(inactiveSet, web)
		}
	}
	return inactiveSet
}
