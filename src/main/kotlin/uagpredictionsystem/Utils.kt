package uagpredictionsystem

import java.text.Normalizer


sealed class Either<out L, out R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    data class Right<out R>(val value: R) : Either<Nothing, R>()
}

fun replaceAccentedCharacters(text: String): String {
    val normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD)
    return normalizedText
        .replace("[^\\p{ASCII}]".toRegex(), "")
        .replace("[^A-Za-z0-9 ]".toRegex(), "")
        .replace("\\s+".toRegex(), " ")
}

