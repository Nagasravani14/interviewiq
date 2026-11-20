@echo off
setlocal
echo ==================================================================
echo   Starting InterviewIQ on Localhost
echo ==================================================================

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

if not exist "%~dp0target\interviewiq-1.0.0.jar" (
    echo Building package first...
    call "%~dp0mvnw.cmd" clean package -DskipTests
)

echo Starting InterviewIQ Web Server...
echo The application will automatically launch your browser once started.
"%JAVA_EXE%" -jar "%~dp0target\interviewiq-1.0.0.jar" --web %*
