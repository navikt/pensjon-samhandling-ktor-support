package no.nav.pensjon.samhandling.env

internal fun Map<String, String>.getVal(key: String) = this[key] ?: throw MissingEnvironmentVariable("""$key, is not found in environment""")

internal fun Map<String, String>.getVal(key: String, defaultValue: String) = this.getOrDefault(key, defaultValue)

internal fun Map<String, String>.verifyEnvironmentVariables(keys: List<String>) {
    val missingKeys = keys.filter { this[it] == null }
    if (missingKeys.isNotEmpty()) throw MissingEnvironmentVariables(missingKeys.joinToString(", ", "Missing env keys: "))
}

internal class MissingEnvironmentVariable(message: String) : RuntimeException(message)

internal class MissingEnvironmentVariables(message: String) : RuntimeException(message)