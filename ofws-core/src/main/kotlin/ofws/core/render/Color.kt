package ofws.core.render

data class Color(
    val red: UByte,
    val green: UByte,
    val blue: UByte,
) {

    companion object {
        val BLUE = Color(0u, 0u, 255u)
        val GREEN = Color(0u, 255u, 0u)
        val PINK = Color(255u, 192u, 203u)
        val RED = Color(255u, 0u, 0u)
        val WHITE = Color(255u, 255u, 255u)
        val YELLOW = Color(255u, 255u, 0u)
    }

    fun getRedDouble() = red.toDouble() / 255.0
    fun getGreenDouble() = green.toDouble() / 255.0
    fun getBlueDouble() = blue.toDouble() / 255.0
}