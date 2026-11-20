# PowerShell Launcher for InterviewIQ
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "  Launching InterviewIQ - Smart Mock Interview Platform" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan

if (-not $env:JAVA_HOME) {
    if (Test-Path "C:\Program Files\JetBrains\PyCharm 2026.2.3\jbr") {
        $env:JAVA_HOME = "C:\Program Files\JetBrains\PyCharm 2026.2.3\jbr"
    }
}

$javaExe = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME "bin\java.exe" } else { "java" }

$jarPath = Join-Path $PSScriptRoot "target\interviewiq-1.0.0.jar"
if (-not (Test-Path $jarPath)) {
    Write-Host "JAR not found. Building package first..." -ForegroundColor Yellow
    & (Join-Path $PSScriptRoot "mvnw.cmd") package
}

Write-Host "Starting InterviewIQ GUI desktop application..." -ForegroundColor Green
& $javaExe -jar $jarPath
