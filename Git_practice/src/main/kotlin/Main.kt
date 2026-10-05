import java.util.Scanner

/**
 * Основная точка входа в эмулятор оболочки (Этап 1).
 */
fun main() {
    val scanner = Scanner(System.`in`)
    val vfsName = "my_vfs"

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
 * Ищет в строке переменные окружения (\$ИМЯ) и заменяет их на реальные значения.
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
 * @return false, если нужно выйти из приложения (команда exit), иначе true.
 */
fun executeCommand(command: String, args: List<String>): Boolean {
    when (command) {
        "exit" -> return false
        "ls", "cd" -> {
            println("Вызвана заглушка команды $command. Аргументы: $args")
        }
        else -> {
            println("Ошибка: команда $command не найдена")
        }
    }
    return true
}
