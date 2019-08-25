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