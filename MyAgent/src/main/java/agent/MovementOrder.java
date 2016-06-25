package main.java.agent;

import java.util.Queue;

import main.java.map.MapLocation;

public class MovementOrder {
	public MapLocation destination;
	public Queue<String> moves;
	public boolean isFinished;

	public enum Move {
		UP, DOWN, LEFT, RIGHT, COLLECT, DROP
	}

	public MovementOrder(MapLocation destination, Queue<String> moves) {
		this.destination = destination;
		this.moves = moves;
	}

	public MapLocation getDestination() {
		return destination;
	}

	@Deprecated
	public void setDestination(MapLocation destination) {
		this.destination = destination;
	}

	public Queue<String> getMoves() {
		return moves;
	}

	@Deprecated
	public void setMoves(Queue<String> moves) {
		this.moves = moves;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
}
