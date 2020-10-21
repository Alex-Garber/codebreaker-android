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
import io.reactivex.disposables.Disposable;
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
        .doAfterSuccess((game) -> {
          guess.postValue(null);
          solved.postValue(false);
          timestamp = new Date();
          previousGussCount = 0;
        })
        .subscribe(
            game::postValue,
            throwable::postValue
        )
    );
  }

  public void restartGame() {
    Game game = this.game.getValue();
    @SuppressWarnings("ConstantConditions")
    int guessCount = game.getGuessCount();
    throwable.setValue(null);
    pending.add(
        repository.restartGame(game)
            .doOnComplete(() -> {
              guess.postValue(null);
              solved.postValue(false);
              previousGussCount += guessCount;
            })
            .subscribe(

                () ->    this.game.setValue(game),
                throwable:: postValue
            )
    );
  }

  public void guess(String text) {
      Game game = this.game.getValue();
    throwable.setValue(null);
    Disposable disposable = repository.guess(game, text)
        .doAfterSuccess((guess) -> {
          this.game.postValue(game);
          //noinspection ConstantConditions
          if (guess.getCorrect() == game.getLength()) {
            solved.postValue(true);
            save(game);
          }
        })
        .subscribe(
            guess::postValue,
            throwable::postValue
        );
    pending.add(disposable);
  }

  private void save(Game game) {
    pending.add(
        repository.save(game, timestamp, previousGussCount)
            .subscribe(
                () -> {},
                throwable::postValue
            )
    );
  }


  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();

  }

}
