package jp.wozniak.karasco

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.routing

@KtorExperimentalLocationsAPI
@Location("/karasco")
data class Score(val score: Double)

data class Response(val hands: Set<Hand>, val points: Int, val bottles: Int)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    install(Locations)
    routing {
        get<Score> { score ->
            val hands = calculate(score.score)
            val points = hands.filterIsInstance<NormalHand>().sumBy(NormalHand::point)
            val bottles = hands.filterIsInstance<SpecialHand>().sumBy(SpecialHand::bottles)
            call.respond(Response(hands, points, bottles))
        }
    }
}

