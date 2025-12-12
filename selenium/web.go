package selenium

import (
	"errors"
	"github.com/tebeka/selenium"
	"log"
	"render-crawler-go/config"
	"render-crawler-go/mods"
	"strconv"
	"sync"
	"sync/atomic"
	"time"
)

type WebProxy struct {

	// 代理主机地址
	Host string

	// 代理端口
	Port int
}

// Web 浏览器实例及其状态管理
type Web struct {
	WebProxy

	// 浏览器实例
	// 在 Go 中，WebDriver 可能是 selenium.WebDriver 接口类型
	WebDriver selenium.WebDriver

	// 锁 (对应 Java 的 ReentrantLock，Go 中通常使用 sync.Mutex)
	// 如果需要精确控制锁定/解锁，可以使用 *sync.Mutex
	Mutex sync.RWMutex

	// 页面加载策略
	PageLoadStrategy mods.PageLoadStrategy

	// 浏览器实例请求次数。
	// 在并发场景下，应使用 sync/atomic 提供的原子操作。
	RequestSum int32

	// 失败次数
	// 同样建议使用 int32 并通过原子操作更新
	FailSum int32

	// 最后一次活跃时间
	// 推荐使用 time.Time 类型而不是 Long (毫秒数)
	LastActiveTime int64

	// 浏览器状态
	// True 代表正在执行任务中
	// 使用 sync/atomic 提供的 AtomicBool 或 int32 模拟
	Status atomic.Bool

	// 代理类型
	ProxyType mods.ProxyPoolType
}

func CreateSelenium(proxyType mods.ProxyPoolType, pageLoadStrategy mods.PageLoadStrategy, pt mods.ProxyPoolType) *Web {
	host := ""
	port := 0
	webProxy := GetProxy(pt)
	if webProxy != nil {
		host = webProxy.Host
		port = webProxy.Port
	}
	return NewSelenium(proxyType, pageLoadStrategy, host, port)
}

func NewSelenium(proxyType mods.ProxyPoolType, pageLoadStrategy mods.PageLoadStrategy, host string, port int) *Web {
	web := Web{
		PageLoadStrategy: pageLoadStrategy,
		ProxyType:        proxyType,
		WebProxy:         WebProxy{Host: host, Port: port},
	}
	web.initWeb()

	return &web
}

// 初始化
func (s *Web) initWeb() {

	chromeArgs := []string{

		// 安全沙盒在 Docker 下会报错，必须关闭
		"--no-sandbox",

		// 避免某些环境下共享内存不足导致 crash（但不是万能）
		"--disable-dev-shm-usage",

		// 避免 GPU 相关错误（Linux 容器基本没 GPU）
		"--disable-gpu",

		// 如果页面一定要显示图片，则开启
		"--blink-settings=imagesEnabled=false",

		// 禁用 Viz Display 合成器，降低 Chrome headless 的内存使用
		"--disable-features=VizDisplayCompositor",

		// 提升稳定性：限制渲染线程数
		"--disable-background-networking",
		"--disable-background-timer-throttling",
		"--disable-backgrounding-occluded-windows",
		"--disable-breakpad",
		"--disable-component-extensions-with-background-pages",

		// 防止 DevTools 端口冲突
		"--remote-debugging-port=0",
	}

	if config.Cfg.Web.Headless {
		chromeArgs = append(chromeArgs, "--headless=new")
	}

	//添加proxy
	if len(s.Host) > 0 {
		log.Println("添加代理,", s.Host+":"+strconv.Itoa(s.Port))
		chromeArgs = append(chromeArgs, "--proxy-server=http://"+s.Host+":"+strconv.Itoa(s.Port))
	}

	caps := selenium.Capabilities{
		"browserName": "chrome",
		// 注意！新版 Web 的键名
		"goog:chromeOptions": map[string]interface{}{
			"args": chromeArgs,
		},
		"pageLoadStrategy": string(s.PageLoadStrategy),
	}

	wd, err := selenium.NewRemote(caps, config.Cfg.SeleniumPath)
	if err != nil {
		log.Fatalf("无法连接到远程 Web Hub (%s): %v", config.Cfg.SeleniumPath, err)
	}
	s.WebDriver = wd
	log.Println("成功连接到远程 Chrome WebDriver.")

	s.LastActiveTime = time.Now().Unix()
	Register(s)

}

func (s *Web) CloseSeleniumNoSafe() {

	log.Println("关闭浏览器实例")
	Unregister(s)

	if s.WebDriver == nil {
		log.Println("浏览器早被关闭了")
		return
	}

	err := s.WebDriver.Quit()
	if err != nil {
		log.Println("关闭浏览器 异常 {}", err)
		return
	}

	s.WebDriver = nil
}

// 销毁
func (s *Web) CloseSelenium() {
	s.Mutex.Lock()

	if !s.Status.CompareAndSwap(false, true) {
		return
	}

	defer func() {
		s.Status.Store(false)
		s.Mutex.Unlock()
	}()
	s.CloseSeleniumNoSafe()

}

// 获取页面
func (s *Web) GetPageSource(url string, localValue string, locateType LocateType, pageLoadTimeout int) (string, error) {
	s.Mutex.Lock()

	if !s.Status.CompareAndSwap(false, true) {
		return "", errors.New("系统异常")
	}

	defer func() {
		s.Status.Store(false)
		s.Mutex.Unlock()

	}()

	//如果为空代表关闭
	if s.WebDriver == nil {
		//创建一个新的
		s.initWeb()
	}

	//策略模式获取内容
	result, err := PageSource(url, s.WebDriver, locateType, localValue, pageLoadTimeout)
	if err != nil {
		s.FailSum++
		verifyFailWeb(s)
	}

	s.RequestSum++
	s.LastActiveTime = time.Now().Unix()

	return result, err
}
