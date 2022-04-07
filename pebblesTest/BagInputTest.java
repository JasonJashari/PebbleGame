import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class BagInputTest {
    // returns true if file location for bag is valid

    @Before
    public void setUp(){
        UserInputTestSuite.testGame.setNoOfPlayers(5);
    }

    @After
    public void tearDown(){
        UserInputTestSuite.testGame.setNoOfPlayers(0);
    }

    @Test
    public void testUserBagInput(){
        assertTrue(UserInputTestSuite.testGame.getUserBagInput(0,"testBag.txt"));
    }

    @Test
    public void testUserBagInputFileNotFound(){
        assertFalse(UserInputTestSuite.testGame.getUserBagInput(0,"doesNotExist.txt"));
    }

    @Test
    public void testUserBagInputWrongFormat(){
        assertFalse(UserInputTestSuite.testGame.getUserBagInput(0,"myPic.png"));
    }

    @Test
    public void testUserBagInputNotFile(){
        assertFalse(UserInputTestSuite.testGame.getUserBagInput(0,"sentence"));
    }

    @Test
    public void testUserBagInputEmpty(){
        assertFalse(UserInputTestSuite.testGame.getUserBagInput(0, ""));
    }

    @Test
    public void testUserBagInputSmallBag(){
        assertFalse(UserInputTestSuite.testGame.getUserBagInput(0,"tinyBag.csv"));
    }

}