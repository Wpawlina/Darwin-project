package darwin.model.map;

import darwin.util.Boundary;
import darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

public class BounderyTest {

    @Test
    public void contains()
    {
        Boundary boundary=new Boundary(new Vector2d(0,0),new Vector2d(10,10));
        assert boundary.contains(new Vector2d(5,5));
        assert boundary.contains(new Vector2d(0,0));
        assert boundary.contains(new Vector2d(10,10));
        assert !boundary.contains(new Vector2d(11,11));
    }

    @Test
    public void size()
    {
        Boundary boundary=new Boundary(new Vector2d(0,0),new Vector2d(10,10));
        assert boundary.size()==121;
    }

    @Test
    public void generateSpaces()
    {
        Boundary boundary=new Boundary(new Vector2d(0,0),new Vector2d(2,2));
        assert boundary.generateSpaces().size()==boundary.size();
    }

    @Test
    public void generateArraySpaces()
    {
        Boundary boundary=new Boundary(new Vector2d(0,0),new Vector2d(2,2));
        assert boundary.generateArraySpaces().size()== boundary.size();
    }

}
