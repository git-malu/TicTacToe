package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    // Represents the internal state of the game
//    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button[] mBoardButtons;
    // Various text displayed
    private TextView mInfoTextView;
    private TextView mScore;
    // Restart Button
    private Button startButton;
    // Game Over
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the button array.
        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        //wire up the buttons.
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mScore = (TextView) findViewById(R.id.score);



        //set the board in both classes.
        if(TicTacToeGame.mTurnCounter!=0){
            Log.d("saved?", "yes");
            setListeners();
//            TicTacToeGame.mGameOver = savedInstanceState.getBoolean("GameOver",true);
            Character s;
            for (Integer i=0;i<9;i++){
                s = TicTacToeGame.mBoard[i];
                Log.d("X",s.toString());
                //set Button
                if(s =='X'){//set Button color
                    mBoardButtons[i].setText(s.toString());//set Button Text
                    mBoardButtons[i].setTextColor(Color.rgb(0, 200, 0));
                    mBoardButtons[i].setEnabled(false);
                    Log.d("X","restore X");
                }
                else if (s == 'O'){
                    mBoardButtons[i].setText(s.toString());//set Button Text
                    mBoardButtons[i].setTextColor(Color.rgb(200, 0, 0));
                    mBoardButtons[i].setEnabled(false);
                    Log.d("O","restore O");
                }else{
                    mBoardButtons[i].setText(" ");//set Button Text
                    Log.d("O","restore empty");
                }
//            mBoardButtons[i].setEnabled(savedInstanceState.getBoolean(i.toString()+"b"));//getSaved Button state
            }
            infoDisplayOnResume(TicTacToeGame.sWin);
        }
        else {
            startNewGame();//new game, set onClickListeners
            Log.d("saved?","no");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        for(Integer i=0;i<9;i++){
//            outState.putCharSequence(i.toString(),mBoardButtons[i].getText());
//            outState.putBoolean(i.toString()+"b",mBoardButtons[i].isEnabled());
//        }
//        outState.putCharSequence("info",mInfoTextView.getText());
        TicTacToeGame.mInfo = mInfoTextView.getText().toString();//save the info to TicTacToe
        TicTacToeGame.mInfoColor = mInfoTextView.getTextColors();
    }

    //    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//
//    }
    private void setListeners(){
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));//set up the listeners.
        }
    }
    private void startNewGame() {
        TicTacToeGame.mGameOver = false;
//        TicTacToeGame.clearBoard();
//---Reset all buttons
        setListeners();
        TicTacToeGame.mTurnCounter++;
        if(TicTacToeGame.mTurnCounter%2==0){
            TicTacToeGame.clearBoard();
            int move = TicTacToeGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);//set android move
            mInfoTextView.setText(getString(R.string.its_your_turn_x));
        }else{
            TicTacToeGame.clearBoard();
            mInfoTextView.setText(getString(R.string.you_go_first));
            mInfoTextView.setTextColor(Color.rgb(0, 200, 0));

        }
//---TextView-Human goes first
        mScore.setText("player win:"+TicTacToeGame.mPlayerWon.toString()
                +"Android win:"+TicTacToeGame.mAndroidWon.toString()
                +"Tie:"+TicTacToeGame.mTie.toString());
    }
//
    private void infoDisplayOnResume(int winner){
        //resume the info TextView
        mInfoTextView.setText(TicTacToeGame.mInfo);
        mInfoTextView.setTextColor(TicTacToeGame.mInfoColor);
        if (winner == 0) {
            mInfoTextView.setText(getString(R.string.its_your_turn_x));
        } else if (winner == 1) {
            mInfoTextView.setText(getString(R.string.its_a_tie));
//            TicTacToeGame.mGameOver = true;
        } else if (winner == 2) {
            mInfoTextView.setText(getString(R.string.you_won));
//            TicTacToeGame.mGameOver = true;
        } else {
            mInfoTextView.setText(getString(R.string.android_won));
//            TicTacToeGame.mGameOver = true;
        }
        //resume the score textView
        mScore.setText("player win:"+TicTacToeGame.mPlayerWon.toString()
                +"Android win:"+TicTacToeGame.mAndroidWon.toString()
                +"Tie:"+TicTacToeGame.mTie.toString());
    }

    //---Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        @Override
        public void onClick(View v) {
            if (TicTacToeGame.mGameOver == false) {
                if (mBoardButtons[location].isEnabled()) {
                    //player move
                    TicTacToeGame.setMove(TicTacToeGame.HUMAN_PLAYER, location);
                    setMove(TicTacToeGame.HUMAN_PLAYER, location);//X,2

                    //--- If no winner yet, let the computer make a move
                    int winner = TicTacToeGame.checkForWinner();
                    //android move
                    if (winner == 0) {
                        mInfoTextView.setText(getString(R.string.its_androids_turn));
                        int move = TicTacToeGame.getComputerMove();
                        setMove(TicTacToeGame.COMPUTER_PLAYER, move);//set android move
                        winner = TicTacToeGame.checkForWinner();
                    }
                    //after android move
                    if (winner == 0) {
                        mInfoTextView.setTextColor(Color.rgb(0, 0, 0));
                        mInfoTextView.setText(getString(R.string.its_your_turn_x));
                    } else if (winner == 1) {
                        mInfoTextView.setTextColor(Color.rgb(0, 0, 200));
                        mInfoTextView.setText(getString(R.string.its_a_tie));
                        TicTacToeGame.mGameOver = true;
                        TicTacToeGame.mTie++;
                    } else if (winner == 2) {
                        mInfoTextView.setTextColor(Color.rgb(0, 200, 0));
                        mInfoTextView.setText(getString(R.string.you_won));
                        TicTacToeGame.mGameOver = true;
                        TicTacToeGame.mPlayerWon++;
                    } else {
                        mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
                        mInfoTextView.setText(getString(R.string.android_won));
                        TicTacToeGame.mGameOver = true;
                        TicTacToeGame.mAndroidWon++;
                    }
                }
            }
            mScore.setText("player win:"+TicTacToeGame.mPlayerWon.toString()
                    +"Android win:"+TicTacToeGame.mAndroidWon.toString()
                    +"Tie:"+TicTacToeGame.mTie.toString());
        }
    }

    private void setMove(char player, int location) {
//        TicTacToeGame.setMove(player, location);//set on the mBoard
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //--- OnClickListener for Restart a New Game Button
    public void newGame(View v) {
        startNewGame();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Integer i =getRequestedOrientation();
//        Log.d("orientation", i.toString());
//
//    }
}

