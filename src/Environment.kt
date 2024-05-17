interface Environment {

    operator fun get(name: String): (MutableList<Entity>, Int) -> Entity
    operator fun set(name: String, value: (MutableList<Entity>, Int) -> Entity)
}



