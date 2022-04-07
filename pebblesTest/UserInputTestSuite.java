import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({PlayerInputTest.class, BagInputTest.class})
public class UserInputTestSuite {
    @Rule
    public static MockPebbleGame testGame = new MockPebbleGame();
}