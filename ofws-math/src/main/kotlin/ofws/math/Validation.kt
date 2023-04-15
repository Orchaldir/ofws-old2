package ofws.math

fun requireGreater(value: Int, minValue: Int, name: String): Int {
    require(value >= minValue) { "Value '$name' requires $value >= $minValue!" }
    return value
}