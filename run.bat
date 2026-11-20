@echo off
setlocal
echo ========================================================
echo   Starting InterviewIQ - Smart Mock Interview Platform
echo ========================================================

if not defined JAVA_HOME (
    if exist "C:\Program Files\JetBrains\PyCharm 2026.2.3\jbr" (
        set "JAVA_HOME=C:\Program Files\JetBrains\PyCharm 2026.2.3\jbr"
    )
)

if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_EXE=java"
)

if exist "%~dp0target\interviewiq-1.0.0.jar" (
    "%JAVA_EXE%" -jar "%~dp0target\interviewiq-1.0.0.jar" %*
) else (
    echo Building application executable first...
    call "%~dp0mvnw.cmd" clean package
    "%JAVA_EXE%" -jar "%~dp0target\interviewiq-1.0.0.jar" %*
)

if %ERRORLEVEL% NEQ 0 (
    echo Application exited with code %ERRORLEVEL%.
    pause
)
