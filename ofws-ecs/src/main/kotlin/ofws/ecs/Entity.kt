package ofws.ecs

@JvmInline
value class Entity(private val id: Int) {

    fun next() = Entity(id + 1)

}