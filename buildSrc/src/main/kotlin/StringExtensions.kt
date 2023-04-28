import java.util.concurrent.TimeUnit

fun String.execute(): Unit {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        val stdIn = proc.inputStream.bufferedReader().readText()
        val stdOut = proc.errorStream.bufferedReader().readText()
        System.out.println("out> $stdIn\nerr> $stdOut")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
