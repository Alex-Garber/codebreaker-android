package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.SwatchSpinnerDropdownItemBinding;
import java.util.Map;

public class CodeCharacterAdapter extends ArrayAdapter<Character> {

  private final Map<Character, Integer> colorValueMap;
  private final Map<Character, String> colorLabelMap;
  private final Spinner spinner;
  private final LayoutInflater inflater;


  public CodeCharacterAdapter(Context context, Map<Character, Integer> colorValueMap,
      Map<Character, String> colorLabelMap, Character[] codes, Spinner spinner) {
    super(context, R.layout.swatch_spinner_dropdown_item, codes);
    this.colorValueMap = colorValueMap;
    this.colorLabelMap = colorLabelMap;
    inflater = LayoutInflater.from(context);
    this.spinner = spinner;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    SwatchSpinnerDropdownItemBinding binding = (convertView != null)
        ? SwatchSpinnerDropdownItemBinding.bind(convertView)
        : SwatchSpinnerDropdownItemBinding.inflate(inflater);
    Character c = getItem(position);
    binding.swatch.setBackgroundColor(colorValueMap.get(c));
    String label = colorLabelMap.get(c);
    binding.swatch.setContentDescription(label);
    binding.label.setText(label);
    binding.label.setVisibility(
        (position == spinner.getSelectedItemPosition()) ? View.GONE : View.VISIBLE);
    return binding.getRoot();
  }
}
