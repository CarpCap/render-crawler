package selenium

import (
	"render-crawler/mods"
	"sync"
)

// 代理池
type Queue interface {
	Replenish()
}

type ProxyPool struct {
	name string
	mu   sync.Mutex
	pool []WebProxy
}

// 弹出代理池 如果没有代理了调用replenish补充
func (p *ProxyPool) Pop(queue Queue) *WebProxy {
	p.mu.Lock()
	defer p.mu.Unlock()

	if len(p.pool) <= 0 {
		//尝试补充一次
		queue.Replenish()
		if len(p.pool) <= 0 {
			//如果还是没有代理 返回空
			return nil
		}
	}

	item := p.pool[0]
	p.pool = p.pool[1:]
	return &item
}

type CnProxyPool struct {
	ProxyPool
}

type IntlProxyPool struct {
	ProxyPool
}

// todo 国内代理 补充代理池
func (p *CnProxyPool) Replenish() {
	p.pool = append(p.pool, WebProxy{"127.0.0.1", 8080}, WebProxy{"127.0.0.1", 8081})
}

// todo 国外代理 补充代理池
func (p *IntlProxyPool) Replenish() {
	p.pool = append(p.pool, WebProxy{"127.0.0.2", 8080}, WebProxy{"127.0.0.2", 8081})
}

var cnProxyPool = CnProxyPool{}
var intlProxyPool = IntlProxyPool{}

func GetProxy(proxyType mods.ProxyPoolType) *WebProxy {
	switch proxyType {
	case mods.CN:
		return cnProxyPool.Pop(&cnProxyPool)
	case mods.INTL:
		return intlProxyPool.Pop(&intlProxyPool)
	default:
		return nil
	}

}
