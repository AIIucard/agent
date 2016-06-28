package main.java.agent;

import java.util.Queue;

public class MovementOrder {
	public Queue<Move> moves;

	public enum Move {
		UP, DOWN, LEFT, RIGHT, COLLECT, DROP
	}

	public MovementOrder(Queue<Move> moves) {
		this.moves = moves;
	}

	public Queue<Move> getMoves() {
		return moves;
	}

	@Deprecated
	public void setMoves(Queue<Move> moves) {
		this.moves = moves;
	}
}
