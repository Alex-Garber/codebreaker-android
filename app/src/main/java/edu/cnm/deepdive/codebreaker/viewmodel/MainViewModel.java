package edu.cnm.deepdive.codebreaker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.codebreaker.model.Code.Guess;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.IllegalGuessCharacterException;
import edu.cnm.deepdive.codebreaker.model.IllegalGuessLengthException;
import java.security.SecureRandom;
import java.util.Random;

public class MainViewModel extends AndroidViewModel {

  public static final String pool = "ROYGBIV";
  public static final int CODE_LENGTH = 4;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Guess> guess;
  private final MutableLiveData<Boolean> solved;
  private final MutableLiveData<Throwable> throwable;
  private final Random rng;


  public MainViewModel(@NonNull Application application) {
    super(application);
    game = new MutableLiveData<>();
    guess = new MutableLiveData<>();
    solved = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    rng = new SecureRandom();
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

  public void startGame(){
    throwable.setValue(null);
    guess.setValue(null);
    solved.setValue(false);
    Game game = new Game(pool, CODE_LENGTH, rng);
    this.game.setValue(game);

  }
  public void restartGame() {
    throwable.setValue(null);
    guess.setValue(null);
    solved.setValue(false);
    //noinspection ConstantConditions
    game.getValue().restart();
    game.setValue(game.getValue());
  }
  public void guess(String text) {
    throwable.setValue(null);
    //noinspection ConstantConditions
    try {
      Game game = this.game.getValue();
      Guess guess = game.guess(text);
      this.guess.setValue(guess);
      this.game.setValue(game);
      solved.setValue(guess.getCorrect() == game.getLength());
    } catch (IllegalGuessLengthException | IllegalGuessCharacterException e) {
      throwable.setValue(e);
    }

  }


}
