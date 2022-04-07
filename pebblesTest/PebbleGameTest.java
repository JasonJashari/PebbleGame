import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PebbleGameTest {
    private MockPebbleGame testGame = new MockPebbleGame();

    @Before
    public void setUp(){
        testGame.setNoOfPlayers(1);
        testGame.setBags();
    }

    @After
    public void tearDown(){
        testGame.setGameOver(true);
        testGame.setNoOfPlayers(0);
        testGame.bagA.clear();
        testGame.bagB.clear();
        testGame.bagC.clear();
        testGame.bagX.clear();
        testGame.bagY.clear();
        testGame.bagZ.clear();
    }

    @Test
    public void testNoOfPlayers() {
        assertEquals(1, testGame.getNoOfPlayers());
    }

    @Test
    public void testDrawFromBlackBag() {
        MockPebbleGame.Player player = testGame.new Player();
        assertEquals(40, testGame.bagX.size());
        assertEquals(40, testGame.bagY.size());
        assertEquals(40, testGame.bagZ.size());
        Pebble p = testGame.drawFromBlackBag(player);
        ArrayList<Pebble> discardToBag = player.getNextWhiteBagDiscard();
        if (discardToBag == testGame.bagA) {
            assertEquals(39, testGame.bagX.size());
            assertEquals(40, testGame.bagY.size());
            assertEquals(40, testGame.bagZ.size());
        } else if (discardToBag == testGame.bagB) {
            assertEquals(40, testGame.bagX.size());
            assertEquals(39, testGame.bagY.size());
            assertEquals(40, testGame.bagZ.size());
        } else {
            assertEquals(40, testGame.bagX.size());
            assertEquals(40, testGame.bagY.size());
            assertEquals(39, testGame.bagZ.size());
        }
    }

    @Test
    public void startingDraw() {
        assertEquals(40, testGame.bagX.size());
        Pebble p = testGame.startingDraw("X");
        assertEquals(39, testGame.bagX.size());
    }

    @Test
    public void initialiseBag() {
    }

    @Test
    public void initialiseThreads() {
        // testGame.setNoOfPlayers(4);
        // testGame.initialiseThreads();
        // test threads exist
    }

    @Test
    public void testPlayerHand() {
        MockPebbleGame.Player player = testGame.new Player();
        player.generateStartingHand();

        // test player hand is 10
        assertEquals(10, player.getHand().size());

        // test player hand is pebbles
        for(int i=0; i<player.getHand().size(); i++){
            assertTrue(player.getHand().get(i) instanceof Pebble);
        }

    }

    @Test
    public void testChoosePebble() {
        MockPebbleGame.Player player = testGame.new Player();
        player.generateStartingHand();
        Pebble p = player.choosePebble();
        assertTrue(p instanceof Pebble);
        assertTrue(player.getHand().contains(p));
    }

    @Test
    public void testCheckWon() {
        MockPebbleGame.Player player = testGame.new Player();
        ArrayList<Pebble> hand = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            hand.add(new Pebble(15));;
        }
        player.setHand(hand);
        player.checkWon();
        assertFalse(testGame.isGameOver());

        hand.clear();
        for (int i = 0; i < 10; i++) {
            hand.add(new Pebble(10));;
        }
        player.setHand(hand);
        player.checkWon();
        assertTrue(testGame.isGameOver());
    }

    @Test
    public void testDiscardPebble() {
        MockPebbleGame.Player player = testGame.new Player();
        player.generateStartingHand();
        ArrayList<Pebble> whiteBag = player.getNextWhiteBagDiscard();
        int playerHandSizeBefore = player.getHand().size();
        int bagSizeBefore = whiteBag.size();
        Pebble pebbleDiscarded = player.discardPebble();
        int playerHandSizeAfter = player.getHand().size();
        int bagSizeAfter = whiteBag.size();

        // test invariance
        assertEquals(playerHandSizeBefore-1, playerHandSizeAfter);
        assertEquals(bagSizeBefore+1, bagSizeAfter);

        // test pebble exists in white bag
        assertTrue(whiteBag.contains(pebbleDiscarded));

        // test pebble does not exist in player hand
        assertFalse(player.getHand().contains(pebbleDiscarded));
    }

}