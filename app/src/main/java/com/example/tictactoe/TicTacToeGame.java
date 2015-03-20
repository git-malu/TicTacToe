package com.example.tictactoe;

import java.util.Random;

/**
 * Created by Lu on 3/19/2015.
 */
public class TicTacToeGame {
//    public static final char OPEN_SPOT = ' ';
    public static final int BOARD_SIZE = 9;
    public static Character mBoard[] = {'1','2','3','4','5','6','7','8','9'};//the board only android can see!

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static char sTurn ='X';// Human starts first
    public static Integer sWin = 0;// Set to 1-tie, 2-human win, or 3-android win
    public static Boolean mGameOver = true;
    private static Random mRand = new Random();
    static Integer mPlayerWon=0,mAndroidWon=0,mTie=0;
    static Integer mTurnCounter = 0;

    public TicTacToeGame() {
        //use singleton instead
    }
    public static void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            mBoard[i] = Character.forDigit(i+1,10);
        }
    }
    public static void setMove(char player, int location) {
        mBoard[location] = player;
    }

//    private void displayBoard()	{
//        System.out.println();
//        System.out.println(mBoard[0] + " | " + mBoard[1] + " | " + mBoard[2]);
//        System.out.println("-----------");
//        System.out.println(mBoard[3] + " | " + mBoard[4] + " | " + mBoard[5]);
//        System.out.println("-----------");
//        System.out.println(mBoard[6] + " | " + mBoard[7] + " | " + mBoard[8]);
//        System.out.println();
//    }

    // Check for a winner.  Return
    //  0 if no winner or tie yet
    //  1 if it's a tie
    //  2 if X won
    //  3 if O won
    //  result is stored in the sWin
    public static int checkForWinner() {

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2]== HUMAN_PLAYER){
                sWin = 2;
                return 2;//human win
            }

            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER){
                sWin = 3;
                return 3;//android win
            }

        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6]== HUMAN_PLAYER){
                sWin = 2;
                return 2;//human win
            }

            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6]== COMPUTER_PLAYER){
                sWin = 3;
                return 3;//android win
            }

        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER)){
            sWin = 2;
            return 2;//human win
        }

        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER)){
            sWin = 3;
            return 3;//android win
        }


        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER){
                sWin = 0;
                return 0;//no one win and continue the game
            }

        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        sWin = 1;
        return 1;//tie
    }

//    void getUserMove()
//    {
//        // Eclipse throws a NullPointerException with Console.readLine
//        // Known bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=122429
//        //Console console = System.console();
//
//        Scanner s = new Scanner(System.in);
//
//        int move = -1;
//
//        while (move == -1) {
//            try {
//                System.out.print("Enter your move: ");
//                move = s.nextInt();
//
//                while (move < 1 || move > BOARD_SIZE ||
//                        mBoard[move-1] == HUMAN_PLAYER || mBoard[move-1] == COMPUTER_PLAYER) {
//
//                    if (move < 1 || move > BOARD_SIZE)
//                        System.out.println("Please enter a move between 1 and " + BOARD_SIZE + ".");
//                    else
//                        System.out.println("That space is occupied.  Please choose another space.");
//
//                    System.out.print("Enter your move: ");
//                    move = s.nextInt();
//                }
//            }
//            catch (InputMismatchException ex) {
//                System.out.println("Please enter a number between 1 and " + BOARD_SIZE + ".");
//                s.next();  // Get next line so we start fresh
//                move = -1;
//            }
//        }
//
//        mBoard[move-1] = HUMAN_PLAYER;
//    }

//result is kept in the mBoard
    public static int getComputerMove()
    {
        int move;

        // First see if there's a move O can make to win
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;//move
                //check if win
                if (checkForWinner() == 3) {//if android win,not undo it
                    System.out.println("Computer is moving to " + (i + 1));
                    return i;
                }
                else
                    mBoard[i] = curr;//otherwise undo it
            }
        }

        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];   // Save the current number
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER;
                    System.out.println("Computer is moving to " + (i + 1));
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }

        // Generate random move
        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);

        System.out.println("Computer is moving to " + (move + 1));

        mBoard[move] = COMPUTER_PLAYER;
        return move;

    }

}
