# ===========================
# Go 跨平台构建脚本 build.ps1
# ===========================

# 设置控制台输出编码为 UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "开始构建 Linux amd64 版本..."

# 设置环境变量（仅当前脚本生效）
$env:CGO_ENABLED = "0"
$env:GOOS = "linux"
$env:GOARCH = "amd64"

# 输出目录
$out = "build"
if (!(Test-Path $out)) {
    New-Item -ItemType Directory -Path $out | Out-Null
}

# 编译
go build -o "$out/render-crawler" .

Write-Host "构建成功：$out/render-crawler"
