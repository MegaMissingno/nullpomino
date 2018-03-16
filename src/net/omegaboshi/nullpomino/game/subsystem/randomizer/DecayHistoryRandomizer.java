package net.omegaboshi.nullpomino.game.subsystem.randomizer;

import mu.nu.nullpo.game.component.Piece;

//Variation on history randomizer that checks fewer pieces each reroll.
//Rejects the first piece if it was in the last seven, second piece if it was in the last six, third if it was in the last five...
//After eight rerolls, accepts anything.

public class DecayHistoryRandomizer extends Randomizer {

	int[] history;
	int id;

	boolean firstPiece;

	public DecayHistoryRandomizer() {
		super();
	}

	public DecayHistoryRandomizer(boolean[] pieceEnable, long seed) {
		super(pieceEnable, seed);

	}

	public void init() {
		firstPiece = true;
                //May want to abstract this for varying history lengths
                history = new int[] {Piece.PIECE_S, Piece.PIECE_Z, Piece.PIECE_S, Piece.PIECE_Z, Piece.PIECE_S, Piece.PIECE_Z, Piece.PIECE_S};
	}

	public int next() {
		if (firstPiece && !isPieceSZOOnly()) {
			do {
				id = r.nextInt(pieces.length);
			} while (pieces[id] == Piece.PIECE_O || pieces[id] == Piece.PIECE_Z || pieces[id] == Piece.PIECE_S);
			firstPiece = false;
		} else {
                        /*System.out.print("History: ");
                        for (int h = 0; h < history.length; h++) {
                            System.out.print(Piece.PIECE_NAMES[history[h]]);
                        }
                        System.out.println();*/
                        id = r.nextInt(pieces.length);
                        //System.out.println("Roll 0: " + Piece.PIECE_NAMES[id]);
			for (int i = history.length; i > 0 ; i--) {
				boolean repeat = false;
                                for (int k = 0; k < i; k++) {
                                    if (pieces[id] == history[k]) {
                                        repeat = true;
                                        //System.out.println("Rejected " + Piece.PIECE_NAMES[id]);
                                        break;
                                    }
                                }
                                if (!repeat) {
                                    break;
                                }
                                id = r.nextInt(pieces.length);
                                //System.out.println("Roll " + (8-i) + ": " + Piece.PIECE_NAMES[id]);
			}
		}
		for (int i = history.length - 1; i > 0; i--) {
			history[i] = history[i-1];
		}
		history[0] = pieces[id];
                //System.out.println("Accepted " + Piece.PIECE_NAMES[id]);
		return pieces[id];
	}
}
