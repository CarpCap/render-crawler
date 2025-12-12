package selenium

import (
	"errors"
	"fmt"
	"github.com/PuerkitoBio/goquery"
	"github.com/tebeka/selenium"
	"log"
	"strings"
	"time"
)

type LocateType string

const (
	CSS   LocateType = "css"
	XPATH LocateType = "xpath"
	TIME  LocateType = "time"
)

type Locate interface {
	pageSource() (string, error)
}

type Html struct {
	wd              selenium.WebDriver
	locateValue     string
	pageLoadTimeout int
}

type Css struct{ Html }
type Xpath struct{ Html }
type Time struct{ Html }

func (c Css) pageSource() (string, error) {
	log.Println("css pageSource")
	for i := 0; i < c.pageLoadTimeout; i++ {
		// 1. 获取页面源代码
		page, err := c.wd.PageSource()
		if err != nil {
			// 其他异常打印并继续
			fmt.Printf("css 获取页面源码错误: %v\n", err)
			continue
		}

		// 2. 解析 HTML
		doc, err := goquery.NewDocumentFromReader(strings.NewReader(page))
		if err != nil {
			fmt.Printf("解析 HTML 错误: %v\n", err)
			continue
		}

		// 3. 使用 CSS 选择器查找元素
		if doc.Find(c.locateValue).Length() > 0 {
			// 元素存在，返回页面源码
			return page, nil
		}

		// 4. 等待 1 秒后重试
		time.Sleep(1 * time.Second)
	}

	// 超时返回错误
	return "", errors.New("CSS页面加载超时，未找到指定元素")
}

func (x Xpath) pageSource() (string, error) {
	// 重试 pageLoadTimeout 次
	for i := 0; i < x.pageLoadTimeout; i++ {
		// 1. 查找元素
		elements, err := x.wd.FindElements(selenium.ByXPATH, x.locateValue)
		if err != nil {
			// 其他异常，打印并继续重试
			fmt.Printf("xpath查找元素异常: %v\n", err)
			continue
		}

		// 2. 如果找到元素，返回页面源码
		if len(elements) > 0 {
			pageSource, err := x.wd.PageSource()
			if err != nil {
				return "", fmt.Errorf("xpath获取页面源码失败: %v", err)
			}
			return pageSource, nil
		}

		// 3. 等待1秒后继续重试
		time.Sleep(1 * time.Second)
	}

	// 超时返回错误
	return "", errors.New("Xpath页面加载超时")
}

func (t Time) pageSource() (string, error) {
	// 记录日志，表示开始执行 pageSource 方法
	log.Println("time pageSource")

	// 创建一个带缓冲的通道，容量为1
	// 这个通道用于接收 goroutine 的执行结果
	// 结构体包含两个字段：s (string类型，页面源码) 和 e (error类型，错误信息)
	ch := make(chan struct {
		s string
		e error
	}, 1)

	// 使用 goroutine 异步执行获取页面源码的操作
	// goroutine 是 Go 的轻量级线程，这里用于防止主线程被阻塞
	go func() {
		// 在 goroutine 中调用 WebDriver 的 PageSource 方法
		s, e := t.wd.PageSource()
		// 将结果（页面源码和错误）发送到通道中
		// 注意：这里使用了匿名结构体字面量 {s string; e error}{s, e}
		ch <- struct {
			s string
			e error
		}{s, e}
	}()

	// 使用 select 语句等待多个通道操作
	// select 会阻塞，直到其中一个 case 可以执行
	select {
	// case 1: 从通道 ch 接收到结果（goroutine 执行完成）
	case r := <-ch:
		// 检查是否有错误
		if r.e != nil {
			// 如果有错误，返回空字符串和错误信息
			return "", r.e
		}
		// 如果没有错误，返回页面源码
		return r.s, nil

	// case 2: 超时情况（10秒后 time.After 通道会收到值）
	case <-time.After(10 * time.Second):
		// 10秒内没有从 ch 通道收到结果，返回超时错误
		return "", fmt.Errorf("Time页面加载超时")
	}

}

func PageSource(url string, wd selenium.WebDriver, locateType LocateType, value string, pageLoadTimeout int) (string, error) {
	// 导航到指定 URL
	if err := wd.Get(url); err != nil {
		log.Printf("导航到 %s 失败: %v", url, err)
		return "", err
	}
	log.Printf("成功导航到 %s", url)

	html := Html{wd: wd, locateValue: value, pageLoadTimeout: pageLoadTimeout}
	var locate Locate
	switch locateType {
	case CSS:
		locate = Css{html}
		break
	case XPATH:
		locate = Xpath{html}
		break
	case TIME:
		locate = Time{html}
		break
	default:
		return "", errors.New("locate type error")
	}
	return locate.pageSource()
}
