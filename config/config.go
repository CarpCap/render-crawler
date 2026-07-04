package config

import (
	"fmt"
	"github.com/spf13/pflag"
	"log"

	"github.com/spf13/viper"
)

type Config struct {
	Listen       string `mapstructure:"listen"`
	SeleniumPath string `mapstructure:"selenium_path"`

	Web struct {
		WebCheckRequestSum int32 `mapstructure:"web_check_request_sum"`
		WebCheckFailSum    int32 `mapstructure:"web_check_fail_sum"`
		WebCheckInterval   int32 `mapstructure:"web_check_interval"`
		WebCheckIdle       int64 `mapstructure:"web_check_idle"`
		Headless           bool  `mapstructure:"headless"`
		WebLoadImg         bool  `mapstructure:"web_load_img"`
	} `mapstructure:"web"`
}

var Cfg *Config

// --config=./config.toml 指定配置文件
// Init 函数添加了命令行参数逻辑
func Init() {

	// 1. 定义命令行参数变量
	var cfgFile string

	// 定义命令行标志：
	pflag.StringVarP(&cfgFile, "config", "c", "", "指定配置文件路径，用于覆盖默认配置")

	// 解析命令行参数
	pflag.Parse()

	// 2. 根据命令行参数决定 Viper 的配置方式
	if cfgFile != "" {
		// --- 场景 A: 部署覆盖模式 (最高优先级) ---

		// 命令行指定了文件，Viper 只加载这一个文件，忽略所有搜索路径
		viper.SetConfigFile(cfgFile)
		fmt.Printf("✅ 启用部署覆盖模式，尝试加载文件: %s\n", cfgFile)

	} else {
		// --- 场景 B: 默认搜索模式 (次优先级) ---

		fmt.Println("🔍 启用默认搜索模式，查找 config.toml...")

		// 设置配置文件的名称和类型
		viper.SetConfigName("config") // 查找文件名为 config
		viper.SetConfigType("toml")   // 文件类型为 toml

		// 配置文件搜索路径（按顺序查找）
		viper.AddConfigPath(".")   // 1. 程序运行的当前路径
		viper.AddConfigPath("../") // 2. go run config/config.go 情况下

		// 您还可以添加生产环境的绝对路径作为默认搜索路径
		// viper.AddConfigPath("/etc/yourapp/")
	}

	// 3. 读取配置文件
	if err := viper.ReadInConfig(); err != nil {
		if _, ok := err.(viper.ConfigFileNotFoundError); ok && cfgFile == "" {
			// 如果是默认搜索模式下找不到文件，打印警告但不退出
			log.Printf("⚠️ 警告: 默认配置文件 config.toml 未找到，将使用默认值或环境变量。")
			// 此时可以继续，使用程序内置的零值
		} else {
			// 无论是自定义文件找不到还是解析错误，都视为致命错误
			log.Fatalf("❌ 读取配置文件失败: %v", err)
		}
	} else {
		fmt.Printf("✨ 配置文件加载成功，使用的文件路径: %s\n", viper.ConfigFileUsed())
	}

	// 4. 解析配置到结构体
	Cfg = new(Config)
	if err := viper.Unmarshal(Cfg); err != nil {
		log.Fatalf("❌ 解析配置失败: %v", err)
	}

}
