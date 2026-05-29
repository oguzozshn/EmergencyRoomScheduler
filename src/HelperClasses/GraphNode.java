package HelperClasses;
import Model.Room;

public class GraphNode {
    private GraphNode parent;
    private Room room;

    public GraphNode(Room room, GraphNode parent) {
        this.room = room;
        this.parent = null;
    }
}
