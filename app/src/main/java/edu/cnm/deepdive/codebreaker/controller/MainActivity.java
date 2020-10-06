package edu.cnm.deepdive.codebreaker.controller;

import android.content.Intent;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.GuessAdapter;
import edu.cnm.deepdive.codebreaker.model.Code.Guess;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.viewmodel.MainViewModel;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements InputFilter {
  private static final String INVALID_CHAR_PATTERN = String.format("[^%s]" , MainViewModel.pool);
  private ListView guessList;
  private EditText guess;
  private MainViewModel viewModel;
  private int codeLength;

  private static final int[] colorValues =
      {Color.RED, 0xffffa500, Color.YELLOW, Color.GREEN, Color.BLUE, 0xff4b0082, 0xffee82ee};
  private static final Map<Character, Integer> colorMap = buildColorMAp(MainViewModel.pool.toCharArray(), colorValues);

  private GuessAdapter adapter;
  private Button summit;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupViews();
    setupViewModel();
  }

  @Override
  public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int destart,
      int destend) {
    String modifiedSource = source.toString().toUpperCase().replaceAll(INVALID_CHAR_PATTERN, "");
    StringBuilder builder = new StringBuilder(dest);
    builder.replace(destart, destend, modifiedSource);
    if (builder.length() > codeLength){
      modifiedSource
          = modifiedSource.substring(0, modifiedSource.length() - (builder.length() - codeLength));
    }
    int newLength = dest.length() - (destend - destart) +modifiedSource.length();
    summit.setEnabled(newLength == codeLength);
    return modifiedSource;

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.new_game:
        startGame();
        break;
      case R.id.restart_game:
        restartGame();
        break;
      case R.id.settings:
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        break;

      default:
        handled = super.onOptionsItemSelected(item);

    }
    return handled;
  }

  private void setupViewModel() {
      adapter = new GuessAdapter(MainActivity.this, colorMap);
    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    viewModel.getGame().observe(this, (game) -> {
      adapter.clear();
      adapter.addAll(game.getGuesses());
      guessList.setAdapter(adapter);
      guessList.setSelection(adapter.getCount() - 1);
      codeLength= game.getLength();
      guess.setText("");
    });
    viewModel.getSolved().observe(this, solved ->
        findViewById(R.id.guess_controls).setVisibility(solved ? View.INVISIBLE : View.VISIBLE));
    viewModel.getThrowable().observe(this, (throwable) -> {
      if (throwable != null){
        Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
      }
    });
  }

  private void setupViews() {
    guessList = findViewById(R.id.guess_list);
    guess = findViewById(R.id.guess);
    guess.setFilters(new InputFilter[]{this});
    summit = findViewById(R.id.summit);
    summit.setOnClickListener((view) -> recordGuess());
  }

  private void startGame() {
    viewModel.startGame();
  }

  private void recordGuess() {
    viewModel.guess(guess.getText().toString().trim().toUpperCase());

  }

  private void restartGame() {
  viewModel.restartGame();
  }

  private static Map<Character, Integer> buildColorMAp(char[] chars, int[] values){
    Map<Character, Integer> colorMap = new HashMap<>();
    for (int i = 0; i < chars.length; i++) {
      colorMap.put(chars[i], values[i]);
    }
    return colorMap;
  }


}