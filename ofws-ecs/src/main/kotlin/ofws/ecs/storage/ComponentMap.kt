package ofws.ecs.storage

import ofws.ecs.Entity

data class ComponentMap<T>(
    private val type: String,
    private val componentMap: Map<Entity, T> = mapOf(),
) : ComponentStorage<T> {

    override fun getType() = type

    override fun has(entity: Entity) = componentMap.containsKey(entity)

    override fun get(entity: Entity) = componentMap[entity]

    override fun getOrThrow(entity: Entity): T {
        return componentMap[entity] ?: throw NoSuchElementException("Entity $entity has no $type!")
    }

    override fun getList(entities: List<Entity>) = entities.map { getOrThrow(it) }

    override fun getAll(): Collection<T> = componentMap.values

    override fun getIds() = componentMap.keys

    override fun updateAndRemove(updated: Map<Entity, T>, removed: Set<Entity>): ComponentStorage<T> {
        val newComponentMap = componentMap + updated - removed
        return copy(componentMap = newComponentMap)
    }
}