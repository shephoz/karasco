package jp.wozniak.karasco

fun main() {
    val hands = calculate(77.007)
    println(hands)
    val points = hands.filterIsInstance<NormalHand>().sumBy { it.point }
    val bottles = hands.filterIsInstance<SpecialHand>().sumBy { it.bottles }
    if (points > 0) println("$points points!")
    if (bottles > 0) println("$bottles bottles!")
}

fun calculate(score: Int): Set<Hand> {
    val digits: List<Int> = score.toString().toList().map { it.toString().toInt() }

    val hands = mutableSetOf<Hand>()
    hands.sumCheck(digits)
    hands.occurrenceCheck(digits)
    hands.continuousCheck(digits)
    hands.straightCheck(digits)

    return hands
}

fun MutableSet<Hand>.sumCheck(digits: List<Int>) {
    val hands = this
    val sum: Int = digits.sum()

    when (sum) {
        in 0..17 -> {
            hands.add(NormalHand.Low)
        }
        21 -> {
            hands.add(NormalHand.BlackJack)
        }
        in 37..45 -> {
            hands.add(NormalHand.High)
        }
    }
}

fun MutableSet<Hand>.occurrenceCheck(digits: List<Int>) {
    val hands = this
    val occurrences =
        digits.groupingBy { it }.eachCount().map { Occurrence(it.key, it.value) }.sortedByDescending { it.times }
    println(occurrences)

    when (occurrences[0].times) {
        5 -> {
            if (occurrences[0].digit == 7) {
                hands.add(SpecialHand.AllSeven)
            } else {
                hands.add(SpecialHand.FiveCards)
            }
        }
        4 -> {
            hands.add(NormalHand.FourCards)
        }
        3 -> {
            when (occurrences[1].times) {
                2 -> hands.add(NormalHand.FullHouse)
                1 -> hands.add(NormalHand.ThreeCards)
            }
        }
        2 -> {
            when (occurrences[1].times) {
                2 -> hands.add(NormalHand.TwoPairs)
            }
        }
    }
}

fun MutableSet<Hand>.continuousCheck(digits: List<Int>) {
    val hands = this
    val continuousOccurrences = mutableListOf<Occurrence>()
    for (digit in digits) {
        val lastOccurrence = continuousOccurrences.lastOrNull()
        if (lastOccurrence != null && digit == lastOccurrence.digit) {
            lastOccurrence.increment()
        } else {
            continuousOccurrences.add(Occurrence(digit, 1))
        }
    }
    continuousOccurrences.sortByDescending { it.times }
    println(continuousOccurrences)

    val mostOccurred = continuousOccurrences[0]
    when (mostOccurred.times) {
        4 -> {
            if (hands.contains(NormalHand.FourCards)) {
                hands.add(NormalHand.Flush)
            }
        }
        3 -> {
            when (continuousOccurrences[1].times) {
                2 -> {
                    if (hands.contains(NormalHand.FullHouse)) {
                        hands.add(NormalHand.Flush)
                    }
                }
                1 -> {
                    if (hands.contains(NormalHand.ThreeCards)) {
                        hands.add(NormalHand.Flush)
                    }

                }
            }
            if (mostOccurred.digit == 7) {
                hands.add(NormalHand.ThreeSevens)
            }
        }
        2 -> {
            when (continuousOccurrences[1].times) {
                2 -> {
                    if (hands.contains(NormalHand.TwoPairs)) {
                        hands.add(NormalHand.Flush)
                    }
                }
            }
        }
    }
}

fun MutableSet<Hand>.straightCheck(digits: List<Int>) {
    val hands = this
    val sequences = (0..9).map { (it..it + 4).toList() }.map { it.map { it % 10 } }
    val flushSequences =
        (0..5).map { (it..it + 4).toList() }.let { seq -> seq.map { it.reversed() }.plus(seq) }
    if (flushSequences.contains(digits)) {
        hands.add(SpecialHand.StraightFlush)
        return
    }
    if (sequences.contains(digits.sorted())) {
        hands.add(NormalHand.Straight)
    }
}

class Occurrence(val digit: Int, var times: Int) {
    fun increment() = this.times++
    override fun toString(): String = "$digit($times)"
}
