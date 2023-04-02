package ofws.ecs

import ofws.ecs.storage.ComponentMap
import ofws.ecs.storage.ComponentStorage
import kotlin.reflect.KClass

data class EcsBuilder(
    private var entity: Entity = Entity(0),
    private val entities: MutableSet<Entity> = mutableSetOf(),
    private val storageMap: MutableMap<String, ComponentStorage<*>> = mutableMapOf(),
    private val dataMap: MutableMap<String, Any> = mutableMapOf(),
) {
    fun build(): EcsState {
        return EcsState(entities, storageMap, dataMap)
    }

    fun <T> registerComponent(kClass: KClass<*>) {
        val type = getType(kClass)
        storageMap[type] = ComponentMap<T>(type, mapOf())
    }

    inline fun <reified T : Any> registerComponent() = registerComponent<T>(T::class)

    // component

    fun createEntity(): EntityBuilder {
        val builder = EntityBuilder(entity, storageMap)
        entities.add(entity)
        entity = entity.next()
        return builder
    }

    // data

    fun <T : Any> addData(kClass: KClass<*>, data: T) {
        val type = getType(kClass)
        dataMap[type] = data
    }

    inline fun <reified T : Any> addData(data: T) = addData(T::class, data)

}