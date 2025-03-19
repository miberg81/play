
1. Line Continuation:
# CMD uses caret (^)
gcc -I"%JAVA_HOME%\include" ^
-I"%JAVA_HOME%\include\win32" ^
# Bash uses backslash (\)
gcc -I"$JAVA_HOME/include" \
-I"$JAVA_HOME/include/win32" \

2. Environment Variables:
# CMD uses %VARIABLE%%JAVA_HOME%
# Bash uses $VARIABLE$JAVA_HOME

3. Path Separators:
# CMD typically uses backslashes (\)
-I"%JAVA_HOME%\include\win32"
# Bash uses forward slashes (/)-I"$JAVA_HOME/include/win32"