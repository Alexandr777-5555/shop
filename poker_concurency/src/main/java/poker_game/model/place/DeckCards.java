package poker_game.model.place;

import poker_game.model.place.cards.Deck;

public class DeckCards implements Deck {

    // колода карт
    private  String[] deck ;


    public DeckCards() {
        deck=new String[]{
                "2d", "3d", "4d", "5d", "6d", "7d", "8d", "9d", "Td", "Jd", "Qd", "Kd", "Ad",
                "2c", "3c", "4c", "5c", "6c", "7c", "8c", "9c", "Tc", "Jc", "Qc", "Kc", "Ac",
                "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "Ts", "Js", "Qs", "Ks", "As",
                "2h", "3h", "4h", "5h", "6h", "7h", "8h", "9h", "Th", "Jh", "Qh", "Kh", "Ah"};
    }



    @Override
    public void shuffle() {

    }

    @Override
    public void print() {

    }
}
