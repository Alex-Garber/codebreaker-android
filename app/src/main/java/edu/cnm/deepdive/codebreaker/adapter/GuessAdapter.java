package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemGuessBinding;
import edu.cnm.deepdive.codebreaker.model.Code.Guess;
import java.util.ArrayList;
import java.util.Map;


public class GuessAdapter extends ArrayAdapter<Guess> {
  private final Map<Character, Integer> colorValueMap;
  private final LayoutInflater inflater;
  private final Map<Character, String> colorLabelMap;

  public GuessAdapter(@NonNull Context context,
      Map<Character, Integer> colorValueMap,
      Map<Character, String> colorLabelMap) {

    super(context, R.layout.item_guess, new ArrayList<Guess>());
    inflater = LayoutInflater.from(context);

    this.colorValueMap = colorValueMap;
    this.colorLabelMap = colorLabelMap;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ItemGuessBinding binding =(convertView != null) ?
        ItemGuessBinding.bind(convertView)
        : ItemGuessBinding.inflate(inflater, parent, false);
    Guess guess = getItem(position);
    binding.guessNumber.setText(String.valueOf(position+1));
    binding.correct.setText(String.valueOf(guess.getCorrect()));
    binding.close.setText(Integer.toString(guess.getClose()));
    binding.gussContainer.removeAllViews();
    for (char c : guess.getText().toCharArray()){
      ImageView swatch =
          (ImageView) inflater.inflate(R.layout.item_swatch, binding.gussContainer, false);
      swatch.setBackgroundColor(colorValueMap.get(c));
      swatch.setContentDescription(colorLabelMap.get(c));
      swatch.setTooltipText(colorLabelMap.get(c));
      binding.gussContainer.addView(swatch);
    }


    return binding.getRoot();
  }
}

