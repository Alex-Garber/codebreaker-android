package edu.cnm.deepdive.codebreaker.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.GuessAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentGameBinding;
import edu.cnm.deepdive.codebreaker.viewmodel.MainViewModel;
import java.util.HashMap;
import java.util.Map;

public class GameFragment extends Fragment implements InputFilter{


  private static final int[] colorValues =
      {Color.RED, 0xffffa500, Color.YELLOW, Color.GREEN, Color.BLUE, 0xff4b0082, 0xffee82ee};
  private static final Map<Character, Integer> colorMap = buildColorMAp(MainViewModel.pool.toCharArray(), colorValues);
  private static final String INVALID_CHAR_PATTERN = String.format("[^%s]" , MainViewModel.pool);




  private MainViewModel viewModel;
  private int codeLength;
  private GuessAdapter adapter;
  private FragmentGameBinding binding;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentGameBinding.inflate(getLayoutInflater());
    setupViews();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
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
    binding.summit.setEnabled(newLength == codeLength);
    return modifiedSource;

  }


  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.game_options, menu);
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
       default:
        handled = super.onOptionsItemSelected(item);

    }
    return handled;
  }

  private void setupViewModel() {
    FragmentActivity activity = getActivity();
    //noinspection ConstantConditions
    adapter = new GuessAdapter(activity, colorMap);
    viewModel = new ViewModelProvider(activity).get(MainViewModel.class);
    LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
    viewModel.getGame().observe(lifecycleOwner, (game) -> {
      adapter.clear();
      adapter.addAll(game.getGuesses());
      binding.guessList.setAdapter(adapter);
      binding.guessList.setSelection(adapter.getCount() - 1);
      codeLength= game.getLength();
      binding.guess.setText("");
    });
    viewModel.getSolved().observe(lifecycleOwner, solved ->
        binding.guessControls.setVisibility(solved ? View.INVISIBLE : View.VISIBLE));
  }

  private void setupViews() {
    binding.guess.setFilters(new InputFilter[]{this});
    binding.summit.setOnClickListener((view) -> recordGuess());
  }

  private void startGame() {
    viewModel.startGame();
  }

  private void recordGuess() {
    viewModel.guess(binding.guess.getText().toString().trim().toUpperCase());

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
