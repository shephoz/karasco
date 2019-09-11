package jp.wozniak.karasco

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respondText
import io.ktor.routing.routing

@KtorExperimentalLocationsAPI
@Location("/karasco")
data class Score(val score: Double)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations)
    routing {
        get<Score> { score ->
            val hands = calculate(score.score)
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

