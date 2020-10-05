package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.model.Code.Guess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GuessAdapter extends ArrayAdapter<Guess> {
  private static final char[] colorCharacters = {'R', 'O', 'Y', 'G', 'B', 'V'};
  private static final int[] colorValues = {Color.RED, 0xffffa500, Color.YELLOW, Color.GREEN, Color.BLUE, 0xffee82ee};
  private final Map<Character, Integer> colorMap;
  private final LayoutInflater inflater;

  public GuessAdapter(@NonNull Context context) {

    super(context, R.layout.item_guess, new ArrayList<Guess>());
    inflater = LayoutInflater.from(context);
    colorMap = new HashMap<>();
    for (int i = 0; i < colorCharacters.length; i++) {
      colorMap.put(colorCharacters[i], colorValues[i]);
    }
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    View layout = (convertView != null)
        ? convertView
        : inflater.inflate(R.layout.item_guess, parent, false);
    Guess guess = getItem(position);
    TextView guessNumber = layout.findViewById(R.id.guess_number);
    TextView correct = layout.findViewById(R.id.correct);
    TextView close = layout.findViewById(R.id.close);
    guessNumber.setText(String.valueOf(position+1));
    correct.setText(String.valueOf(guess.getCorrect()));
    close.setText(Integer.toString(guess.getClose()));
    LinearLayout guessContainer = layout.findViewById(R.id.guss_container);
    guessContainer.removeAllViews();
    for (char c : guess.getText().toCharArray()){
      ImageView swatch = (ImageView) inflater.inflate(R.layout.item_swatch, guessContainer, false);
      swatch.setBackgroundColor(colorMap.get(c));
      swatch.setContentDescription(String.valueOf(c));
      guessContainer.addView(swatch);
    }


    return layout;
  }
}

