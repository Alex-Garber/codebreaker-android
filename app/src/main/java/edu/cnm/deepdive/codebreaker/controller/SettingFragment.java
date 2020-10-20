package edu.cnm.deepdive.codebreaker.controller;


import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import edu.cnm.deepdive.codebreaker.R;

public class SettingFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onViewCreated(@NonNull  View view, @Nullable   Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ActionBar actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
  }
}
