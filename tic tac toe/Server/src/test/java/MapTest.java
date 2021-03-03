import org.junit.Test;
import ru.itis.game.Game;


public class MapTest {
    @Test
    public void testWin1(){
        Game game = new Game(new int[][]{{1,1,1},{0,0,0},{0,0,0}});
        assert game.checkWin() == 1;
    }

    @Test
    public void testWin2(){
        Game game = new Game(new int[][]{{1,0,0},{1,0,0},{1,0,0}});
        assert game.checkWin() == 1;
    }

    @Test
    public void testWin3(){
        Game game = new Game(new int[][]{{1,0,0},{0,1,0},{0,0,1}});
        assert game.checkWin() == 1;
    }

    @Test
    public void testWin4(){
        Game game = new Game(new int[][]{{0,0,1},{0,1,0},{1,0,0}});
        assert game.checkWin() == 1;
    }

    @Test
    public void testWin5(){
        Game game = new Game(new int[][]{{1,0,0},{0,0,0},{1,0,0}});
        assert game.checkWin() == 0;
    }

    @Test
    public void testWin6(){
        Game game = new Game(new int[][]{{-1,-1,-1},{0,0,0},{0,0,0}});
        assert game.checkWin() == -1;
    }

    @Test
    public void testWin7(){
        Game game = new Game(new int[][]{{1,-1,1},{0,0,0},{0,0,0}});
        assert game.checkWin() == 0;
    }
}
