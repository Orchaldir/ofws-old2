package ofws.math.fov

import mu.KotlinLogging
import ofws.math.*

private val logger = KotlinLogging.logger {}

class ShadowCasting : FovAlgorithm {

    private enum class Status {
        UNDEFINED,
        BLOCKING,
        CLEAR;
    }

    override fun calculateVisibleCells(config: FovConfig): Set<Position> {
        val visibleCells = mutableSetOf(config.position)

        val top = Slope(1, 1)
        val bottom = Slope(1, 0)

        Octant.values().forEach {
            processOctant(config, visibleCells, it, 1, top, bottom)
        }

        return visibleCells
    }

    private fun processOctant(
        config: FovConfig,
        visibleCells: MutableSet<Position>,
        octant: Octant,
        startX: Int,
        parentTop: Slope,
        parentBottom: Slope
    ) {
        val originX = config.mapSize.getX(config.position)
        val originY = config.mapSize.getY(config.position)
        var top = parentTop
        var bottom = parentBottom
        logger.info("$octant startX=$startX top=$top bottom=$bottom")

        for (localX in startX..config.range) {
            val topY = top.calculateTopX(localX)
            val bottomY = bottom.calculateBottomX(localX)

            logger.info("x=$localX topY=$topY bottomY=$bottomY")

            var status = Status.UNDEFINED

            for (localY in topY downTo bottomY) {
                val (x, y) = octant.getGlobal(originX, originY, localX, localY)

                val index = config.mapSize.getPosition(x, y)
                visibleCells.add(index)

                val isBlocking = config.isBlocking(index)

                logger.info("  x=$localX y=$localY isBlocking=$isBlocking previous=$status")

                if (isBlocking && status == Status.CLEAR) {
                    // create a slope above the current blocker
                    val newBottom = createSlopeThroughTopLeft(localX, localY)

                    if (localY == bottomY) {
                        bottom = newBottom
                        break
                    } else processOctant(
                        config, visibleCells, octant,
                        localX + 1,
                        top,
                        newBottom
                    )
                } else if (status == Status.BLOCKING) {
                    // create a slope below the previous blocker
                    top = createSlopeThroughTopRight(localX, localY)
                }

                status = updateStatus(isBlocking)
            }

            if (status != Status.CLEAR) {
                break
            }
        }
    }

    private fun updateStatus(isBlocking: Boolean) = if (isBlocking) Status.BLOCKING else Status.CLEAR
}