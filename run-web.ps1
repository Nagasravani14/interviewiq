# PowerShell Launcher for InterviewIQ on Localhost
Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host "  Starting InterviewIQ on Localhost" -ForegroundColor Green
Write-Host "==================================================================" -ForegroundColor Cyan

if (-not $env:JAVA_HOME) {
    if (Test-Path "C:\Program Files\JetBrains\PyCharm 2026.2.3\jbr") {
        $env:JAVA_HOME = "C:\Program Files\JetBrains\PyCharm 2026.2.3\jbr"
    }
}

$javaExe = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME "bin\java.exe" } else { "java" }
$jarPath = Join-Path $PSScriptRoot "target\interviewiq-1.0.0.jar"

if (-not (Test-Path $jarPath)) {
    Write-Host "Building package..." -ForegroundColor Yellow
    & (Join-Path $PSScriptRoot "mvnw.cmd") clean package -DskipTests
}

Write-Host "Starting InterviewIQ Web Server..." -ForegroundColor Green
Write-Host "The application will automatically launch your browser once started." -ForegroundColor Cyan
& $javaExe -jar $jarPath --web @args
