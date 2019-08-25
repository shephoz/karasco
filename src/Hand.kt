package jp.wozniak.karasco

interface Hand

enum class NormalHand(val point: Int) : Hand {
    TwoPairs(1), ThreeCards(1), Straight(2), FullHouse(2), FourCards(3),
    HighScore(1), Push(2), BlackJack(2), High(1), Low(1),
    Flush(1), ThreeSevens(2)
}

enum class SpecialHand(val bottles: Int) : Hand {
    StraightFlush(1), FiveCards(1),
    AllSeven(2), Perfect(2)
}