package mods

type ProxyPoolType string

const (
	OFF  ProxyPoolType = "off"
	CN   ProxyPoolType = "cn"
	INTL ProxyPoolType = "intl"
)

type PageLoadStrategy string

const (
	// 当html下载完成之后，重复去获取半解析状态的html 跟定位符匹配
	NONE PageLoadStrategy = "none"
	// 要等待整个dom树加载完成，即DOMContentLoaded这个事件完成，仅对html的内容进行下载解析
	EAGER PageLoadStrategy = "eager"
	// 默认 即正常情况下，selenium会等待整个界面加载完成（指对html和子资源的下载与解析,如JS文件，图片等，不包括ajax）
	NORMAL PageLoadStrategy = "normal"
)
