package HelperClasses;
import Model.Room;

public class GraphNode {
    public Room room;
    public GraphNode nextEdge;

    public GraphNode(Room room) {
        this.room = room;
        this.nextEdge = null;
    }
}