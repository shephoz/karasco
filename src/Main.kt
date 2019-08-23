fun main() {
    println(calculate(12345))
}

fun calculate(score: Int): Set<Hand> {
    val hands = mutableSetOf<Hand>()
    val digits: List<Int> = score.toString().toList().map { it.toString().toInt() }

    val sum: Int = digits.sum()
    when (sum) {
        in 0..17 -> {
            hands.add(Hand.Low)
        }
        21 -> {
            hands.add(Hand.BlackJack)
        }
        in 37..45 -> {
            hands.add(Hand.High)
        }
    }
    return hands
}

enum class Hand(point: Int) {
    TwoPairs(1), ThreeCards(1), Straight(2), FullHouse(2), FourCards(3),
    HighScore(1), Push(2), BlackJack(2), High(1), Low(1),
    Flash(1), ThreeSevens(2)
}

enum class SpecialHand(bottles: Int) {
    StraightFlash(1), FiveCards(1),
    AllSeven(2), Perfect(2)
}