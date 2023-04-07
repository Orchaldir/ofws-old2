package ofws.core.render

data class Color(
    val red: UByte,
    val green: UByte,
    val blue: UByte,
) {
    fun getRedDouble() = red.toDouble() / 255.0
    fun getGreenDouble() = green.toDouble() / 255.0
    fun getBlueDouble() = blue.toDouble() / 255.0
}