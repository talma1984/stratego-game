package com.company;


import java.util.Scanner;

import static com.company.BoardTileType.*;

public class MainGame {



    public static final String Rules = "\n"+
            "                           game rules: \n"+
            "Each team gets 15 soldiers 4 bombs and 1 flag.\n the goal is to capture you're enemy's flag.\n"+
            "If the rank of the attacker and the defender is the same, the attacker gets to live.\n"+
            "you can move only 1 tile in all 9 directions\n"+
            "to print the board again press P\n"+
            "(1) Flag x1)\n"+
            "(2)spy x1, Kill only the major, loses to everyone else.\n"+
            "(3)private x5, Loses to everyone excepts to other privates.\n"+
            "(4)bomb squad x3, Kills  2,3 and bombs\n"+
            "(5)sergeant x3, Kills 2,3,4\n"+
            "(6) lieutenant x2, Kills 2,3,4,5\n"+
            "(7)major x1, kills 2,3,4,5,6,7\n"+
            "(8)bomb x4, Kills everyone except the bomb squadmajor x1, Kills 02,03,04,05. Loses to spy. \n"+
            "(X) ENEMY\n"+
            "(.) empty territory\n" ;

    String p1;
    String p2;

    BoardTile[][] board = new BoardTile[6][10];

    PlayerType currentPlayer = PlayerType.P1;

    public MainGame() {
        p1 = getPlayer();                      //     regist player1
        p2 = getPlayer();                      //     regist player2
        printWelcomMessage();                  //     print message
        waitForGameToStart();                  //     wait for game to start
        initializeGame();                      //     start game
        while (!isGameWon()) {                  //     check for win
            printBoard( currentPlayer );
            Action action = getPlayerAction( currentPlayer );      //     else show corent player board and wait for input
            updateBoard( action );                                //     perform action
            currentPlayer = getNextPlayer();                     //     end turn
        }
        printBoard( currentPlayer );                               //     return to check for win
        printWinMessage();
    }

    private PlayerType getNextPlayer() {
        if ( currentPlayer == PlayerType.P1 ) {
            return PlayerType.P2;
        }
        return PlayerType.P1;
    }

    private void updateBoard(Action action) {               // move piece \ perform attack
        int fromRow = action.from[0];
        int fromCol = action.from[1];
        int toRow = action.to[0];
        int toCol = action.to[1];
        BoardTile fromTile = board[fromRow][fromCol];
        BoardTile toTile = board[toRow][toCol];


         if ( fromTile.content == BoardTileType.EMPTY ) {
            System.out.println( "this tile is empty" );//        make sure from tyle  not empty
            getPlayerAction( currentPlayer );
        } else if ( fromTile.content == BoardTileType.BOMB ) {  // if you try to move a bomb
            System.out.println( "bombs can't move" );
            getPlayerAction( currentPlayer );
        } else if ( fromTile.content == BoardTileType.SPY && toTile.content == BoardTileType.MAJOR ) {// only  spy can kill the major
            System.out.println(" a spy killed your major!");
            toTile.content = fromTile.content;
            toTile.owner = fromTile.owner;
        } else if ( fromTile.content == BoardTileType.BOMB_SQUAD && toTile.content == BoardTileType.BOMB ) { //bomb squad can defuse bombs!!
            System.out.println("the bomb squad difuse the bomb");
            toTile.content = fromTile.content;
            toTile.owner = fromTile.owner;
        } else if ( fromTile.content.compareTo( toTile.content ) >= 0 ) { // greater  compare uses as a - operator... meaning its like doing bomb-private

            toTile.content = fromTile.content;
            toTile.owner = fromTile.owner;
        } else if ( toRow >= fromRow+2 || toCol >= fromCol+2 || toRow <= fromRow-2 || toCol <= fromCol-2 ) { //     make sure the distans is okay
            System.out.println( "the distanse between the tile and the target is too big!" );
            getPlayerAction( currentPlayer );
        } else {
            fromTile.content = EMPTY;    //clears the last tile
        }
        fromTile.content = EMPTY;    //clears the last tile

    }

    private Action getPlayerAction(PlayerType corentPlayer) {

        System.out.println( "it is "+corentPlayer+"'s turn" );  // get player action from input, validate it and return only valid action (from & to  board coordinate)
        int[] from;
        int[] to;
        System.out.println( "select a tile" );

        do {
            from = getcordinet();
        } while (!validateFromCordinate( from ));
        System.out.println( "select target" );
        do {
            to = getcordinet();
        } while (!validatetoCordinate( to ));

        Action action = new Action();

        action.from = from;
        action.to = to;
        return action;
    }

    private boolean validatetoCordinate(int[] to) {
        int toRow = to[0];
        int toCol = to[1];

        BoardTile toTile = board[toRow][toCol];
          if ( toRow < 0 || toRow > 5 || toCol < 0 || toCol > 9 ) { //     make sure its iside the board
            System.out.println( "the destenation is outside the board" );
            getPlayerAction( currentPlayer );
        }

        return true;
    }

    private boolean validateFromCordinate(int[] from) {
        int fromRow = from[0];
        int fromCol = from[1];

        BoardTile fromTile = board[fromRow][fromCol];
        if ( fromTile.owner != currentPlayer ) { //        make sure from tyle  corent player
            System.out.println( " its not yours" );
            getPlayerAction( currentPlayer );
        } else if ( fromRow < 0 || fromRow > 5 || fromCol < 0 || fromCol > 9 ) {//make sure from tyle exist
            System.out.println( "its not in the board" );
            getPlayerAction( currentPlayer );
        }

            return true;
    }


    private int[] getcordinet() {// get string of "7,9" split it to array of strings ["7","9"] then convert string to int array of [7,9]

        System.out.println("select a row(0-5)  and then column(0-9)");
        Scanner raw = new Scanner( System.in );
        Scanner col  = new Scanner( System.in );
        int num1 = raw.nextInt();
        int num2 = col.nextInt();
        int[] cordinets = {num1, num2,};

        return cordinets;
    }


    private void printBoard(PlayerType corentPlayer) {               // print the board for a specific player

        for (int row = 0; row < board.length; row++) {
            System.out.print( row );
            for (int col = 0; col < board[0].length; col++) {
                if ( board[row][col].content == EMPTY ) {
                    System.out.print( "|| "+". " );
                } else {
                    if ( board[row][col].owner == corentPlayer ) {//            need to print the content()one note
                        System.out.print( "|| "+convertStringToInt( board[row][col].content.toString())+" ");
                    } else {                                    // print all enemy pieces as X
                        System.out.print( "|| "+"x " );
                    }
                }
            }
            System.out.println();
        }
        System.out.print( " " );
        for (int col = 0; col < board[0].length; col++) {
            System.out.print("|| "+ col+" ");
        }
        System.out.println();
    }

    private int convertStringToInt(String s) {

        int tile = 0;

        if ( s == FLAG.toString() ) {
            tile += 1;
            return tile;
        } else if ( s == SPY.toString() ) {
            tile += 2;
        } else if ( s == PRIVATE.toString() ) {
            tile += 3;
        } else if ( s == BOMB_SQUAD.toString() ) {
            tile += 4;
        } else if ( s == SERGEANT.toString() ) {
            tile += 5;
        } else if ( s == LIEUTENANT.toString() ) {
            tile += 6;
        } else if ( s == MAJOR.toString() ) {
            tile += 7;
        } else if ( s == BOMB.toString() ) {
            tile += 8;
        }else {

        }
return tile;
    }



        private void printWinMessage() {
            System.out.println("Congratilations! "+currentPlayer+" Has won!");
        }


        private boolean isGameWon() {                 // check if the game has won (whether one of the flags is missing)
            int flags = 0;
            for (int row = 0; row < board.length ; row++) {
                for (int col = 0; col < board[0].length; col++){
                    if (board[row][col].content == FLAG){
                        flags++;
                    }
                }
            }
            if ( flags != 2){

                return true;
            }else {
                return false;
            }


    }


    private void initializeGame() {                  // set up board
        for (int row = 0; row < board.length ; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col] = new BoardTile();
            }
        }
        board[0][3].content = BOMB;               //    the board of player1
        board[0][3].owner = PlayerType.P1;
        board[1][3].content = BOMB;
        board[1][3].owner = PlayerType.P1;
        board[0][0].content = BOMB;
        board[0][0].owner = PlayerType.P1;
        board[1][0].content = BOMB;
        board[1][0].owner = PlayerType.P1;
        board[0][8].content = FLAG;
        board[0][8].owner = PlayerType.P1;
        board[1][8].content = SPY;
        board[1][8].owner = PlayerType.P1;
        board[0][4].content = PRIVATE;
        board[0][4].owner = PlayerType.P1;
        board[1][4].content = PRIVATE;
        board[1][4].owner = PlayerType.P1;
        board[0][9].content = PRIVATE;
        board[0][9].owner = PlayerType.P1;
        board[1][9].content = PRIVATE;
        board[1][9].owner = PlayerType.P1;
        board[0][1].content = PRIVATE;
        board[0][1].owner = PlayerType.P1;
        board[1][1].content = BOMB_SQUAD;
        board[1][1].owner = PlayerType.P1;
        board[0][7].content = BOMB_SQUAD;
        board[0][7].owner = PlayerType.P1;
        board[1][7].content = BOMB_SQUAD;
        board[1][7].owner = PlayerType.P1;
        board[0][2].content = SERGEANT;
        board[0][2].owner = PlayerType.P1;
        board[1][2].content = SERGEANT;
        board[1][2].owner = PlayerType.P1;
        board[0][5].content = SERGEANT;
        board[0][5].owner = PlayerType.P1;
        board[1][5].content = LIEUTENANT;
        board[1][5].owner = PlayerType.P1;
        board[0][6].content = LIEUTENANT;
        board[0][6].owner = PlayerType.P1;
        board[1][6].content = MAJOR;
        board[1][6].owner = PlayerType.P1;

        board[4][4].content = BOMB;               //    the board of player2
        board[4][4].owner = PlayerType.P2;
        board[5][4].content = BOMB;
        board[5][4].owner = PlayerType.P2;
        board[4][5].content = BOMB;
        board[4][5].owner = PlayerType.P2;
        board[5][5].content = BOMB;
        board[5][5].owner = PlayerType.P2;
        board[4][1].content = FLAG;
        board[4][1].owner = PlayerType.P2;
        board[5][1].content = SPY;
        board[5][1].owner = PlayerType.P2;
        board[4][9].content = PRIVATE;
        board[4][9].owner = PlayerType.P2;
        board[5][9].content = PRIVATE;
        board[5][9].owner = PlayerType.P2;
        board[4][2].content = PRIVATE;
        board[4][2].owner = PlayerType.P2;
        board[5][2].content = PRIVATE;
        board[5][2].owner = PlayerType.P2;
        board[4][8].content = PRIVATE;
        board[4][8].owner = PlayerType.P2;
        board[5][8].content = BOMB_SQUAD;
        board[5][8].owner = PlayerType.P2;
        board[4][3].content = BOMB_SQUAD;
        board[4][3].owner = PlayerType.P2;
        board[5][3].content = BOMB_SQUAD;
        board[5][3].owner = PlayerType.P2;
        board[4][7].content = SERGEANT;
        board[4][7].owner = PlayerType.P2;
        board[5][7].content = SERGEANT;
        board[5][7].owner = PlayerType.P2;
        board[4][6].content = SERGEANT;
        board[4][6].owner = PlayerType.P2;
        board[5][6].content = LIEUTENANT;
        board[5][6].owner = PlayerType.P2;
        board[4][0].content = LIEUTENANT;
        board[4][0].owner = PlayerType.P2;
        board[5][0].content = MAJOR;
        board[5][0].owner = PlayerType.P2;


    }


    private void waitForGameToStart() {                   // wait for any input

        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.println("press 1 to start ");

            input = scanner.nextLine();
        }while (!input.equals("1"));

    }


    private void printWelcomMessage() {

        System.out.println("Welcome  "+ p1 + " And  " + p2+" to:\n          ||STRATEGO||");  // print rules of the game (could use p1 & p2 names)
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printRulls();
    }

    private void printRulls( ) {
        System.out.print(Rules);
    }



    private String getPlayer() {
        System.out.println("please enter your name"); // function returns a name from users input
        Scanner name = new Scanner(System.in);

        return name.nextLine();
    }
}
