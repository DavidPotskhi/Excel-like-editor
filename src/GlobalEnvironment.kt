class GlobalEnvironment : Environment {

    val bindings = hashMapOf<String, (MutableList<Entity>, Int) -> Entity>()

    init {
        TODO("Initialise bindings for the global environment")
    }

    override fun get(name: String): (MutableList<Entity>, Int) -> Entity {
        TODO("Not yet implemented")
    }

    override fun set(name: String, value: (MutableList<Entity>, Int) -> Entity) {
        TODO("Not yet implemented")
    }
}