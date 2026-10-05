import java.io.File
import java.io.PrintStream
import java.util.Scanner

/**
 * Основная точка входа в эмулятор оболочки (Этап 2).
 */
fun main(args: Array<String>) {
    System.setOut(PrintStream(System.out, true))

    if (args.size < 2) {
        println("Ошибка: Передайте аргументы [Путь к VFS] [Путь к скрипту]")
        return
    }

    val vfsPath = args[0]
    val scriptPath = args[1]
    val vfsName = "my_vfs"

    println("Конфигурация эмулятора")
    println("Путь к VFS: $vfsPath")
    println("Путь к стартовому скрипту: $scriptPath")
    println("------------------------------\n")

    if (!runStartupScript(scriptPath, vfsName)) {
        println("Выполнение стартового скрипта прервано из-за ошибки.")
        return
    }

    runRepl(vfsName)
}

/**
 * Выполняет команды из стартового скрипта.
 * @return true, если все команды выполнены успешно, false при ошибке.
 */
fun runStartupScript(scriptPath: String, vfsName: String): Boolean {
    val scriptFile = File(scriptPath)
    if (!scriptFile.exists()) {
        println("Ошибка: Стартовый скрипт не найден по пути $scriptPath")
        return false
    }

    for (line in scriptFile.readLines()) {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith("//")) {
            continue
        }

        println("$vfsName> $trimmed")

        val expandedInput = expandEnvVariables(trimmed)
        val tokens = expandedInput.split("\\s+".toRegex())
        val command = tokens[0]
        val cmdArgs = tokens.drop(1)

        if (!executeCommand(command, cmdArgs)) {
            return false
        }
    }
    return true
}


/**
 * Запускает интерактивный режим REPL.
 */
fun runRepl(vfsName: String) {
    val scanner = Scanner(System.`in`)
    while (true) {
        print("$vfsName> ")
        if (!scanner.hasNextLine()) break

        val input = scanner.nextLine().trim()
        if (input.isEmpty()) continue

        val expandedInput = expandEnvVariables(input)
        val tokens = expandedInput.split("\\s+".toRegex())
        val command = tokens[0]
        val args = tokens.drop(1)

        if (!executeCommand(command, args)) {
            break
        }
    }
}

/**
 * Ищет в строке переменные окружения ($ИМЯ) и заменяет их на реальные значения.
 */
fun expandEnvVariables(input: String): String {
    var result = input
    val regex = "\\$([A-Za-z0-9_]+)".toRegex()

    regex.findAll(input).forEach { matchResult ->
        val varName = matchResult.groupValues[1]
        val envValue = System.getenv(varName) ?: ""
        result = result.replace("$$varName", envValue)
    }
    return result
}

/**
 * Обрабатывает и выполняет введенную команду.
 * @return false, если произошла ошибка или вызвана exit, иначе true.
 */
fun executeCommand(command: String, args: List<String>): Boolean {
    when (command) {
        "exit" -> return false
        "ls", "cd" -> {
            println("Вызвана заглушка команды $command. Аргументы: $args")
        }
        else -> {
            println("Ошибка: команда $command не найдена")
            return false
        }
    }
    return true
}
