package ofws.ecs

import ofws.ecs.storage.ComponentStorage
import kotlin.reflect.KClass

data class EcsState(
    private val entities: Set<Entity> = emptySet(),
    private val storageMap: Map<String, ComponentStorage<*>> = emptyMap(),
    private val dataMap: Map<String, Any> = emptyMap()
) {

    fun <T> getStorage(kClass: KClass<*>): ComponentStorage<T>? {
        val type = getType(kClass)
        val storage = storageMap[type]

        @Suppress("UNCHECKED_CAST")
        return storage as ComponentStorage<T>?
    }

    inline fun <reified T : Any> getStorage(): ComponentStorage<T>? = getStorage(T::class)

    fun <T> getStorageOrThrow(kClass: KClass<*>): ComponentStorage<T> {
        return getStorage(kClass) ?: throw NoSuchElementException("No storage for type '${getType(kClass)}'!")
    }

    inline fun <reified T : Any> getStorageOrThrow(): ComponentStorage<T> = getStorageOrThrow(T::class)

    fun <T> getData(kClass: KClass<*>): T? {
        val type = getType(kClass)
        val data = dataMap[type]

        @Suppress("UNCHECKED_CAST")
        return data as T?
    }

    inline fun <reified T : Any> getData(): T? = getData(T::class)

    fun copy(
        updatedStorage: List<ComponentStorage<*>> = emptyList(),
        updatedData: List<Any> = emptyList()
    ): EcsState {
        val newStorageMap = if (updatedStorage.isEmpty()) {
            storageMap
        } else {
            storageMap + updatedStorage.map { it.getType() to it }
        }

        val newDataMap = if (updatedData.isEmpty()) {
            dataMap
        } else {
            dataMap + updatedData.associateBy { getType(it::class) }
        }

        return EcsState(entities, newStorageMap, newDataMap)
    }

    fun <T> query1(classT: KClass<*>): Query1<T> {
        val storage: ComponentStorage<T> = getStorageOrThrow(classT)

        return Query1(storage.getIds().toMutableList(), storage)
    }

    fun <A, B> query2(classA: KClass<*>, classB: KClass<*>): Query2<A, B> {
        val storageA: ComponentStorage<A> = getStorageOrThrow(classA)
        val storageB: ComponentStorage<B> = getStorageOrThrow(classB)
        val sharedEntities: Set<Entity> = entities.intersect(storageA.getIds()).intersect(storageB.getIds())

        return Query2(sharedEntities.toMutableList(), storageA, storageB)
    }

    inline fun <reified T : Any> query1(): Query1<T> = query1(T::class)

    inline fun <reified A : Any, reified B : Any> query2(): Query2<A, B> = query2(A::class, B::class)

}