package ofws.math

data class Range(val min: Int = 1, val max: Int = Int.MAX_VALUE) {

    init {
        requireGreater(min, 1, "min")
        requireGreater(max, min, "max")
    }

}