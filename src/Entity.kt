

interface Entity {
    fun eval(): Entity
    override fun toString(): String
}

/*
/*
class Environment : Entity {
    override fun eval(env: Environment): Entity {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}

 */

class Number : Entity {
    override fun eval(): Entity {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}

class CellReference : Entity {
    override fun eval(): Entity {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}


class Function(val function: (MutableList<Entity>, Int) -> Entity) : Entity {
    override fun eval(): Entity {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }


}

 */