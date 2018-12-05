package com.cayzerok.game

import java.net.SocketAddress
import java.util.*

data class User(
    var UUID: UUID?,
    var address: SocketAddress?
)

val users = Array(8) {User(null,null)}