package edu.cnm.deepdive.codebreaker;

import android.app.Application;
import android.util.Log;

public class CodebreakerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(getClass().getName(), "Testing logging");
  }
}
