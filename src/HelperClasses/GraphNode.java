package HelperClasses;
import Model.Room;

public class GraphNode {
    public GraphNode edge;
    private Room room;


    public GraphNode(Room room, GraphNode edge) {
        this.room = room;
        this.edge = null;
    }
}
