package ofws.ecs

import ofws.ecs.storage.ComponentMap
import ofws.ecs.storage.ComponentStorage
import kotlin.reflect.KClass

data class EntityBuilder(
    val entity: Entity,
    private val storageMap: MutableMap<String, ComponentStorage<*>> = mutableMapOf(),
) {

    fun <T> add(kClass: KClass<*>, component: T): EntityBuilder {
        val type = getType(kClass)

        @Suppress("UNCHECKED_CAST")
        val storage = storageMap[type] as ComponentStorage<T>? ?: ComponentMap(type)
        storageMap[type] = storage.updateAndRemove(mapOf(entity to component))

        return this
    }

    inline fun <reified T : Any> add(component: T) = add(T::class, component)

}