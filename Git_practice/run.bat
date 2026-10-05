@echo off
rem Компилируем и собираем jar-архив с помощью Gradle
call gradlew.bat shadowJar

rem Запускаем собранный эмулятор (имя jar-файла скорректируем позже)
java -jar build/libs/emulator-all.jar %*
