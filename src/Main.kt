fun main() {
    println(calculate(12345))
}

fun calculate(score: Int): Set<Hand> {
    val hands = mutableSetOf<Hand>()
    val digits: List<Int> = score.toString().toList().map { it.toString().toInt() }

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

    val occurrences = digits.groupingBy { it }.eachCount().toList().sortedBy { -it.second }
    println(occurrences)
    when (occurrences[0].second) {
        4 -> {
            hands.add(NormalHand.FourCards)
        }
        3 -> {
            when (occurrences[1].second) {
                2 -> hands.add(NormalHand.FullHouse)
                1 -> hands.add(NormalHand.ThreeCards)
            }
        }
        2 -> {
            when (occurrences[1].second) {
                2 -> hands.add(NormalHand.TwoPairs)
            }
        }
    }

    val sequentialOccurrences = mutableListOf<Pair<Int, Int>>() // Pair(digit, occurrence)
    for (digit in digits) {
        val lastOccurrence = sequentialOccurrences.lastOrNull()
        if (lastOccurrence != null && digit == lastOccurrence.first) {
            sequentialOccurrences.set(
                sequentialOccurrences.lastIndex,
                Pair(lastOccurrence.first, lastOccurrence.second + 1)
            )
        } else {
            sequentialOccurrences.add(Pair(digit, 1))
        }
    }

    sequentialOccurrences.sortBy { -it.second * 10 + it.first } //まず出現回数の多い順、その中でdigitの昇順
    println(sequentialOccurrences)
    val mostOccurred = sequentialOccurrences[0]
    when (mostOccurred.second) {
        5 -> {
            if (mostOccurred.first == 7) {
                hands.add(SpecialHand.AllSeven)
            } else {
                hands.add(SpecialHand.FiveCards)
            }
        }
        4 -> {
            if (hands.contains(NormalHand.FourCards)) {
                hands.add(NormalHand.Flash)
            }
        }
        3 -> {
            when (sequentialOccurrences[1].second) {
                2 -> {
                    if (hands.contains(NormalHand.FullHouse)) {
                        hands.add(NormalHand.Flash)
                    }
                }
                1 -> {
                    if (hands.contains(NormalHand.ThreeCards)) {
                        hands.add(NormalHand.Flash)
                    }

                }
            }
            if (mostOccurred.first == 7) {
                hands.add(NormalHand.ThreeSevens)
            }
        }
        2 -> {
            when (sequentialOccurrences[1].second) {
                2 -> {
                    if (hands.contains(NormalHand.TwoPairs)) {
                        hands.add(NormalHand.Flash)
                    }
                }
            }
        }
    }

    return hands
}

interface Hand

enum class NormalHand(point: Int) : Hand {
    TwoPairs(1), ThreeCards(1), Straight(2), FullHouse(2), FourCards(3),
    HighScore(1), Push(2), BlackJack(2), High(1), Low(1),
    Flash(1), ThreeSevens(2)
}

enum class SpecialHand(bottles: Int) : Hand {
    StraightFlash(1), FiveCards(1),
    AllSeven(2), Perfect(2)
}