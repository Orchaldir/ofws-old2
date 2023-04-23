package ofws.ecs

import ofws.ecs.storage.ComponentStorage

data class QueryEntry1<T>(val entity: Entity, val component: T)
data class QueryEntry2<A, B>(val entity: Entity, val component0: A, val component1: B)

data class Query1<T>(
    private var entities: MutableList<Entity>,
    private val storage: ComponentStorage<T>,
) : Iterator<QueryEntry1<T>> {
    override fun hasNext() = entities.isNotEmpty()

    override fun next(): QueryEntry1<T> {
        val entity = entities.removeFirst()
        val component: T = storage.getOrThrow(entity)
        return QueryEntry1(entity, component)
    }
}

data class Query2<A, B>(
    private var entities: MutableList<Entity>,
    private val storageA: ComponentStorage<A>,
    private val storageB: ComponentStorage<B>,
) : Iterator<QueryEntry2<A, B>> {
    override fun hasNext() = entities.isNotEmpty()

    override fun next(): QueryEntry2<A, B> {
        val entity = entities.removeFirst()
        val a: A = storageA.getOrThrow(entity)
        val b: B = storageB.getOrThrow(entity)
        return QueryEntry2(entity, a, b)
    }
}