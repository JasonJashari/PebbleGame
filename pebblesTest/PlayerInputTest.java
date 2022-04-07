import org.junit.Test;

import static org.junit.Assert.*;


public class PlayerInputTest {
    // returns requiredPlayerInput, threads run if false

    @Test
    public void testUserPlayerInputInt(){
        assertFalse(UserInputTestSuite.testGame.getUserPlayerInput("7"));
    }

    @Test
    public void testUserPlayerInputFloat(){
        assertTrue(UserInputTestSuite.testGame.getUserPlayerInput("7.64"));
    }

    @Test
    public void testUserPlayerInputAlphabetical(){
        assertTrue(UserInputTestSuite.testGame.getUserPlayerInput("abc"));
    }

    @Test
    public void testUserPlayerInputEmpty(){
        assertTrue(UserInputTestSuite.testGame.getUserPlayerInput(""));
    }

    @Test
    public void testUserPlayerInputNegativeInt(){
        assertTrue(UserInputTestSuite.testGame.getUserPlayerInput("-3"));
    }

}
