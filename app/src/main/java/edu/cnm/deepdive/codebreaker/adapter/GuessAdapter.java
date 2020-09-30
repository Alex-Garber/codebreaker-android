package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import edu.cnm.deepdive.codebreaker.model.Code.Guess;
import java.util.ArrayList;
import java.util.List;

public class GuessAdapter extends ArrayAdapter<Guess> {


  public GuessAdapter(@NonNull Context context) {
    super(context, resource, new ArrayList<Guess>());
  }
}

