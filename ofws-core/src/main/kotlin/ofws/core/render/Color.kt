package ofws.core.render

data class Color(
    val red: UByte,
    val green: UByte,
    val blue: UByte,
) {

    companion object {
        val BLACK = Color(0u, 0u, 0u)
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

    /**
     * Interpolates linearly with another color.
     */
    fun lerp(other: Color, factor: Double) =
        Color(lerp(red, other.red, factor), lerp(green, other.green, factor), lerp(blue, other.blue, factor))

    private fun lerp(start: UByte, end: UByte, factor: Double): UByte {
        if (factor > 1.0) {
            return end
        }

        if (end >= start) {
            val diff = (end - start).toDouble()
            return (start + (diff * factor).toUInt()).toUByte()
        }

        val diff = (start - end).toDouble()

        return (start - (diff * factor).toUInt()).toUByte()
    }
}