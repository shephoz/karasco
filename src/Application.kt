package jp.wozniak.karasco

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            val hands = calculate(77.007)
            val points = hands.filterIsInstance<NormalHand>().sumBy(NormalHand::point)
            val bottles = hands.filterIsInstance<SpecialHand>().sumBy(SpecialHand::bottles)

            val display = StringBuilder()
            display.append("$hands\n")
            if (points > 0) display.append("$points points!\n")
            if (bottles > 0) display.append("$bottles bottles!\n")

            call.respondText(display.toString(), ContentType.Text.Plain)
        }
    }
}

