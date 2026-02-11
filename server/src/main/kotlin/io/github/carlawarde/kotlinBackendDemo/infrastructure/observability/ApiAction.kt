package io.github.carlawarde.kotlinBackendDemo.infrastructure.observability

sealed interface ApiAction {
    val api: String
    val action: String
}

sealed class ReviewAction(override val action: String) : ApiAction {

    override val api: String = "reviews"

    object Create   : ReviewAction("create")
    object Update   : ReviewAction("update")
    object Delete   : ReviewAction("delete")
    object Get      : ReviewAction("get")
    companion object {
        val all = listOf(Create, Update, Delete, Get)
    }
}

sealed class UserAction(override val action: String) : ApiAction {
    override val api: String = "users"

    object Create   : UserAction("create")
    object Update   : UserAction("update")
    object Delete   : UserAction("delete")
    object Get      : UserAction("get")

    companion object {
        val all = listOf(Create, Update, Delete, Get)
    }
}

sealed class GameAction(override val action: String) : ApiAction {
    override val api: String = "games"

    object Create       : GameAction("create")
    object Update       : GameAction("update")
    object Delete       : GameAction("delete")
    object Get          : GameAction("get")

    object Favourite    : GameAction("favourite")
    object Log          : GameAction("log")

    companion object {
        val all = listOf(Create, Update, Delete, Get, Favourite, Log)
    }
}
