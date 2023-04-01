package ofws.ecs

import ofws.ecs.storage.ComponentStorage

data class QueryEntry2<A, B>(val entity: Entity, val component0: A, val component1: B)

class Query2<A, B>(
    private var entities: MutableList<Int>,
    private val storageA: ComponentStorage<A>,
    private val storageB: ComponentStorage<B>,
) : Iterator<QueryEntry2<A, B>> {
    override fun hasNext() = entities.isNotEmpty()

    override fun next(): QueryEntry2<A, B> {
        val entity = Entity(entities.removeFirst())
        val a: A = storageA.getOrThrow(entity)
        val b: B = storageB.getOrThrow(entity)
        return QueryEntry2(entity, a, b)
    }
}