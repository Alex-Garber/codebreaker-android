package edu.cnm.deepdive.codebreaker;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

  private ListView guessList;
  private EditText guess;
  private Button summit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    guessList = findViewById(R.id.guess_list);
    guess = findViewById(R.id.guess);
    summit = findViewById(R.id.summit);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Toast.makeText(this, "So far so good", Toast.LENGTH_LONG).show();
  }
}