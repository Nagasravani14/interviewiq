@echo off
setlocal
if not defined JAVA_HOME (
    if exist "C:\Program Files\JetBrains\PyCharm 2026.2.3\jbr" (
        set "JAVA_HOME=C:\Program Files\JetBrains\PyCharm 2026.2.3\jbr"
    )
)
"%~dp0.tools\apache-maven-3.9.9\bin\mvn.cmd" %*
