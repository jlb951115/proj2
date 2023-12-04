package byog.Core;
import static org.junit.Assert.*;

import byog.TileEngine.TETile;
import org.junit.Test;

public class TestWorld {
    @Test
    public void TestWorld() {
        TETile[][] T1 = new Game().playWithInputString("n4956869837922692650s");
        TETile[][] T2 = new Game().playWithInputString("n48209753395010873s");
        assertNotEquals(T1, T2);
    }
}
