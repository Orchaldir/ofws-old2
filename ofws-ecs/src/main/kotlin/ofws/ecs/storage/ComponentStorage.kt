package ofws.ecs.storage

import ofws.ecs.Entity

interface ComponentStorage<T> {
    fun getType(): String
    fun has(entity: Entity): Boolean
    operator fun get(entity: Entity): T?
    fun getOrThrow(entity: Entity): T
    fun getList(entities: List<Entity>): List<T>
    fun getAll(): Collection<T>
    fun getIds(): Set<Entity>
    fun updateAndRemove(updated: Map<Entity, T> = emptyMap(), removed: Set<Entity> = emptySet()): ComponentStorage<T>
}