package ofws.math


/**
 * A [Size1d] defines a size along one dimension.
 */
@JvmInline
value class Size1d(
    /**
     * The size. Must be greater than 0.
     */
    val size: Int,
) {
    init {
        requireGreater(size, 1, "size")
    }

    companion object {
        val ONE = Size1d(1)
        val TWO = Size1d(2)
        val THREE = Size1d(3)
        val FOUR = Size1d(4)
        val FIVE = Size1d(5)
    }

}