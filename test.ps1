# test.ps1
# 建议保存为 UTF-8 with BOM 或 Unicode

$ErrorActionPreference = "Stop"

# 强制控制台使用 UTF-8
chcp 65001 > $null
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "==> 步骤1：执行 build.ps1"
& .\build.ps1

Write-Host "==> 步骤2：构建 Docker 镜像"
docker build -t carpcap/render-crawler .

Write-Host "==> 步骤3：停止容器"
docker compose down

Write-Host "==> 步骤4：启动容器"
docker compose up -d

Write-Host "==> 执行完成"