package poker_game.model.thinks.impl;

import poker_game.model.thinks.Think;

/**
 * реализация думать по схеме C
 */

public class ThinkC implements Think {
    @Override
    public int bet(int rang) {
        if(rang<1) throw new IllegalArgumentException("Unknown rang");
        int bet = 0;
        if (rang <= 12) {
            bet = 100;
        }
        if (rang > 12) {
            bet = 200;
        }
        return bet;
    }
}
