package ofws.math

data class Size(
    val x: Int,
    val y: Int
) {

    val cells: Int
        get() = x * y

}