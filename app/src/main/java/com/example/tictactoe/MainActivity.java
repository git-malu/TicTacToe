package com.example.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private String[] mDifficulty = {"","","",""};
    // Buttons making up the board
    private Button[] mBoardButtons;
    // Various text displayed
    private TextView mInfoTextView;
    private TextView mScore;
    // Restart Button
    private Button startButton;
    // Game Over
    SharedPreferences mPref;
    SharedPreferences.Editor mPrefEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDifficulty[1] = getString(R.string.easy);
        mDifficulty[2] = getString(R.string.normal);
        mDifficulty[3] = getString(R.string.undefeated);
        mPref = getSharedPreferences("TicTacToe",MODE_PRIVATE);
        mPrefEditor = mPref.edit();
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
        if(TicTacToeGame.sTurnCounter !=0){
            Log.d("saved?", "yes");
            setListeners();
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
        TicTacToeGame.sInfo = mInfoTextView.getText().toString();//save the info to TicTacToe
        TicTacToeGame.sInfoColor = mInfoTextView.getTextColors();
    }

    private void setListeners(){
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));//set up the listeners.
        }
    }
    private void startNewGame() {
        TicTacToeGame.sGameOver = false;
//---Reset all buttons
        setListeners();
        TicTacToeGame.sTurnCounter++;
        if(TicTacToeGame.sTurnCounter %2==0){
            TicTacToeGame.clearBoard();
            int move = TicTacToeGame.getComputerMove(Integer.valueOf(TicTacToeGame.sDifficulty));
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);//set android move
            mInfoTextView.setText(getString(R.string.its_your_turn_x));
            mInfoTextView.setTextColor(Color.rgb(0, 0, 0));
        }else{
            TicTacToeGame.clearBoard();
            mInfoTextView.setText(getString(R.string.you_go_first));
            mInfoTextView.setTextColor(Color.rgb(0, 200, 0));
        }
//---TextView-Human goes first
        readUserSettings();
        displayScoreBoard();
    }
//
    private void infoDisplayOnResume(int winner){
        //resume the info TextView
        mInfoTextView.setText(TicTacToeGame.sInfo);
        mInfoTextView.setTextColor(TicTacToeGame.sInfoColor);
        if (winner == 0) {
            mInfoTextView.setText(getString(R.string.its_your_turn_x));
        } else if (winner == 1) {
            mInfoTextView.setText(getString(R.string.its_a_tie));
        } else if (winner == 2) {
            mInfoTextView.setText(getString(R.string.you_won));
        } else {
            mInfoTextView.setText(getString(R.string.android_won));
        }
        //resume the score textView
        displayScoreBoard();
    }

    //---Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        @Override
        public void onClick(View v) {
            if (TicTacToeGame.sGameOver == false) {
                if (mBoardButtons[location].isEnabled()) {
                    //player move
                    TicTacToeGame.setMove(TicTacToeGame.HUMAN_PLAYER, location);
                    setMove(TicTacToeGame.HUMAN_PLAYER, location);//X,2

                    //--- If no winner yet, let the computer make a move
                    int winner = TicTacToeGame.checkForWinner();
                    //android move
                    if (winner == 0) {
                        mInfoTextView.setText(getString(R.string.its_androids_turn));
                        int move = TicTacToeGame.getComputerMove(Integer.valueOf(TicTacToeGame.sDifficulty));
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
                        TicTacToeGame.sGameOver = true;
                        TicTacToeGame.mTie++;
                        saveHighestScore(TicTacToeGame.sPlayerWon);
                    } else if (winner == 2) {
                        mInfoTextView.setTextColor(Color.rgb(0, 200, 0));
                        mInfoTextView.setText(getString(R.string.you_won));
                        TicTacToeGame.sGameOver = true;
                        TicTacToeGame.sPlayerWon++;
                        saveHighestScore(TicTacToeGame.sPlayerWon);

                    } else {
                        mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
                        mInfoTextView.setText(getString(R.string.android_won));
                        TicTacToeGame.sGameOver = true;
                        TicTacToeGame.sAndroidWon++;
                        saveHighestScore(TicTacToeGame.sPlayerWon);
                    }
                }
            }
            //score board
            displayScoreBoard();
        }
    }
    private void displayScoreBoard(){
        mScore.setText(getString(R.string.android_win)+TicTacToeGame.sPlayerWon.toString()+"\n"
                +getString(R.string.player_win)+TicTacToeGame.sAndroidWon.toString()+"\n"
                +getString(R.string.tie)+TicTacToeGame.mTie.toString()+"\n"
                +getString(R.string.highest_score)+mPref.getInt("highest_score",0)+"\n"
                +getString(R.string.difficulty)+mDifficulty[Integer.valueOf(TicTacToeGame.sDifficulty)]);
    }
    private void saveHighestScore(Integer i){
        if(mPref.getInt("highest_score",0)<i){
            mPrefEditor.putInt("highest_score", i);
            mPrefEditor.commit();
        }
    }

    private void setMove(char player, int location) {
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, 1);
            return true;
        }else if(id == R.id.clean_highest_score){
            mPrefEditor.putInt("highest_score",0).commit();
            displayScoreBoard();
        }

        return super.onOptionsItemSelected(item);
    }

    private void readUserSettings(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        TicTacToeGame.sDifficulty = pref.getString("AI","2");
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                readUserSettings();
                displayScoreBoard();
                break;
        }
    }
}

