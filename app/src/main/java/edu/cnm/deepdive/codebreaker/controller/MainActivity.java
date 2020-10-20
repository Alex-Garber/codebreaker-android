package edu.cnm.deepdive.codebreaker.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

  private MainViewModel viewModel;
  private NavOptions navOptions;
  private NavController navController;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    setupViewModel();
    setupNavigation();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.main_options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    //noinspection SwitchStatementWithTooFewBranches
    switch (item.getItemId()) {
      case R.id.settings:
        navController.navigate(R.id.navigation_settings, null, navOptions);
        break;

      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  private void setupViewModel() {
    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    viewModel.getThrowable().observe(this, (throwable) -> {
      if (throwable != null) {
        Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
      }
    });
  }

  private void setupNavigation(){
    AppBarConfiguration appBarConfiguration =
        new AppBarConfiguration.Builder(R.id.navigation_game, R.id.navigation_settings)
            .build();
    Toolbar toolbar = findViewById(R.id.toolbar);
  setSupportActionBar(toolbar);
    navOptions = new  NavOptions.Builder()
        .setPopUpTo(R.id.navigation_game, false)
        .build();
    navController =  ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment))
        .getNavController();
    NavigationUI.setupActionBarWithNavController(this , navController, appBarConfiguration);



  }

}