package edu.cnm.deepdive.codebreaker.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.model.Code.Guess;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.IllegalGuessCharacterException;
import edu.cnm.deepdive.codebreaker.model.IllegalGuessLengthException;
import edu.cnm.deepdive.codebreaker.model.entity.Score;
import edu.cnm.deepdive.codebreaker.service.GameRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  public static final String pool = "ROYGBIV";

  private final MutableLiveData<Game> game;
  private final MutableLiveData<Guess> guess;
  private final MutableLiveData<Boolean> solved;
  private final MutableLiveData<Throwable> throwable;
  private final Random rng;
  private final String codeLengthPrefKey;
  private final int codeLengthPrefDefault;
  private final SharedPreferences preferences;
  private final GameRepository repository;
  private final CompositeDisposable pending;

  private Date timestamp;
  private int previousGussCount;

  public MainViewModel(@NonNull Application application) {
    super(application);
    repository = new GameRepository(application);
    game = new MutableLiveData<>();
    guess = new MutableLiveData<>();
    solved = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    rng = new SecureRandom();
    codeLengthPrefKey = application.getString(R.string.code_length_pref_key);
    codeLengthPrefDefault =
        application.getResources().getInteger(R.integer.code_length_pref_default);
    preferences = PreferenceManager.getDefaultSharedPreferences(application);
    preferences.registerOnSharedPreferenceChangeListener((prefs, key) -> {
      if (key.equals(codeLengthPrefKey)) {
        startGame();
      }
    });
    pending = new CompositeDisposable();
    startGame();
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Guess> getGuess() {
    return guess;
  }

  public MutableLiveData<Boolean> getSolved() {
    return solved;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void startGame() {
    throwable.setValue(null);
    int codeLength = preferences.getInt(codeLengthPrefKey, codeLengthPrefDefault);
    pending.add(

    repository.newGame(pool, codeLength, rng)
        .subscribe(
            (game -> {
              guess.setValue(null);
              solved.setValue(false);
              timestamp = new Date();
              previousGussCount = 0;
              this.game.setValue(game);

            },
            throwable::postValue
        )
    );
  }

  public void restartGame() {
    throwable.setValue(null);
    guess.setValue(null);
    solved.setValue(false);
    Game game = this.game.getValue();
    //noinspection ConstantConditions
    previousGussCount += game.getGuessCount();
    game.restart();
    this.game.setValue(game);
  }

  public void guess(String text) {
    throwable.setValue(null);
    //noinspection ConstantConditions
    try {
      Game game = this.game.getValue();
      Guess guess = game.guess(text);
      this.guess.setValue(guess);
      this.game.setValue(game);
      if (guess.getCorrect() == game.getLength()) {
        solved.setValue(true);
        save(game);
      }

    } catch (IllegalGuessLengthException | IllegalGuessCharacterException e) {
      throwable.setValue(e);
    }

  }

  private void save(Game game) {
    Score score =
        new Score();
    score.setCodeLength(game.getLength());
    score.setTimestamp(timestamp);
    score.setGuessCount(game.getGuessCount() + previousGussCount);
    pending.add(
        repository.save(score)
            .subscribe(
                () -> {
                },
                throwable::postValue
            )

    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();

  }

}
