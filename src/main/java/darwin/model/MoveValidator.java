package darwin.model;

public interface MoveValidator {
    boolean canMoveTo(Vector2d position);
    Vector2d correctPosition(Vector2d position);
}
