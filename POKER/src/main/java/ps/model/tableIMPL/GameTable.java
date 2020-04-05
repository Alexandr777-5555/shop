package ps.model.tableIMPL;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ps.model.Deck;
import ps.model.Table;
import ps.model.base.Player;
import ps.model.deckIMPL.CardsDeck;
import ps.model.evulator.HandEvaluator;
import ps.qualifiers.DeskAnnotation;
import ps.service.PlayerService;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * реализует функциональность игрового стола
 */

public class GameTable implements Table {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @DeskAnnotation
    private Deck deck;
    private List<Player> playerList;
    private boolean win;
    @Autowired
    private PlayerService service;


    public GameTable(List<Player> playerList) {
        this.playerList = Objects.requireNonNull(playerList, "null");
    }

    @Override
    public void dealCards(Deck deck) {
        for (Player player : playerList) {
            player.setHand(deck.getCardsPlayer(player.getId()));
        }
    }

    @Override
    //   @PostConstruct
    public void game() {
        while (isWin() == false) {  // до победы
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            int bank = 0;
            init();
            deck.shuffle(); // перемешаем колоду
            dealCards(deck); // присваиваем игрокам карманные карты
            printHandsPlayer(); // распечатаем руки у игроков ранг и ставки
            for (Player player : playerList) {
                bank += player.chipsInPot(player.bet());
            }
            log.info("Общий банк равен" + bank);
            streetFlop(deck);
            streetTern(deck);
            streetRiver(deck);
            System.out.println();
            allStreets(deck);
            determinationHandsPLayer();
            int winner = winner();
            log.info("победил игрок" + winner);
            chipsToWinner(winner, bank);
            printAmountChips();
            checkGameOver();
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            if (checkWinner() == true) {// условие победы
                win = true;
                log.info("Выиграл игру игрок ");
                printAmountChips();
                winnerWrite();
            }
            destroy();
        }
    }


    private void winnerWrite() {
        for (Player player : playerList) {
            service.save(player);
        }
    }


    /**
     * раздать флоп на игровой стол ( 3 карты)
     *
     * @param deck колода карт
     */
    public void streetFlop(Deck deck) {
        if (deck == null) {
            throw new SecurityException("Missing deck");
        }
        log.info("Карты флоп :" + deck.getFlop());
    }


    /**
     * раздать терн на игровой стол (1 карта)
     *
     * @param deck
     */
    public void streetTern(Deck deck) {
        if (deck == null) {
            throw new SecurityException("Missing deck");
        }
        log.info("Карты терн :" + deck.getTern()); // раздать терн
    }


    /**
     * раздать ривер на игровой стол (1 карта)
     *
     * @param deck
     */
    public void streetRiver(Deck deck) {
        if (deck == null) {
            throw new SecurityException("Missing deck");
        }
        log.info("Карты ривер :" + deck.getRiver()); // раздать ривер
    }


    public void allStreets(Deck deck) {
        if (deck == null) {
            throw new SecurityException("Missing deck");
        }
        log.info("ОБЩИЕ КАРТЫ : " + deck.commonCards());
    }

    /**
     * проверка остатка фишек у игрока
     */
    public void checkGameOver() {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getChips() == 0) {
                playerList.remove(i);
            }
        }
    }

    @Override
    public Player getWinPlayer() {
        if (playerList.size() == 1) {
            return playerList.get(0);
        }
// TODO вместо null надо что то возвращать ! это неправильно!!!
        return null;
    }


    /**
     * проверка есть победитель в игре
     *
     * @return
     */
    private boolean checkWinner() {
        if (playerList.size() == 1) {
            return true;
        }
        return false;
    }


    /**
     * отдать фишки победителю
     *
     * @param id
     * @param bank
     */
    private void chipsToWinner(int id, int bank) {
        if ((id < 1) && (bank < 0)) throw new IllegalArgumentException("bad");
        for (Player player : playerList) {
            if (player.getId() == id) {
                player.setChips(player.getChips() + bank);
            }
        }
    }


    /**
     * определение силы рук у игроков
     */
    private void determinationHandsPLayer() {
        for (Player player : playerList) {
            player.setStrongHand(0);
            String handPlayer = allHandPlayer(deck, player.getId());
            int strongHand = HandEvaluator.rankHand(CardsDeck.valueOf(handPlayer));
            player.setStrongHand(strongHand);
        }
    }


    public String allHandPlayer(Deck deck, int id) {
        return deck.allHandPlayer(id);
    }


    /**
     * определяем победителя раздачи
     */
    private int winner() {
        int id = -1;
        List<Integer> players = new ArrayList<>();
        for (Player player : playerList) {
            players.add(player.getStrongHand());
        }
        Integer max = Collections.max(players); // у кого самая сильная рука
        findMaxHand(max);
        id = findWinnerId();
        return id;
    }


    /**
     * @return id победителя в раздаче
     */
    private int findWinnerId() {
        Random random = ThreadLocalRandom.current();
        List<Integer> integers = new ArrayList<>();
        int id = 0;
        int countWin = 0;
        for (Player player : playerList) {
            if (player.isWin() == true) {
                id = player.getId();
                log.info("победитель в раздаче " + player.getId());
                countWin++;
                integers.add(id);
            }
        }
        if (countWin > 1) {
            log.info("победителей " + countWin);
            log.info("поэтому определяем случайным образом");
            id = integers.get(random.nextInt(integers.size()));
        }
        return id;
    }

    /**
     * метод определяет сильнешего игрока в раздаче
     *
     * @param max
     */
    private void findMaxHand(int max) {
        if (max < 0) throw new IllegalArgumentException("unknown number: " + max);
        for (Player player : playerList) {
            player.setWin(false);
            if (player.getStrongHand() == max) {
                player.setWin(true);
            }
        }
    }

    private void init() {
        deck = new CardsDeck(); // init
    }

    private void destroy() {
        if (deck != null) {
            System.out.println(deck.toString());
            deck = null;
        }
    }

    private void printHandsPlayer() {
        for (Player player : playerList) {
            log.info("рука " + player.getHand() + " у игрока " + player.getId() + " ранг " + player.rangHand() + "ставка " + player.bet());
        }
    }


    private void printAmountChips() {
        for (Player player : playerList) {
            log.info("у игрока " + player.getId() + " остаток фишек : " + player.getChips());

        }
    }


    private boolean isWin() {
        return win;
    }

    private void setWin(boolean win) {
        this.win = win;
    }


}
