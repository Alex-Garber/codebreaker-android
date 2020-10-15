package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class GameRepository {

  private final Context context;
  private final GameDao gameDao;

  public GameRepository(Context context) {
    this.context = context;
    gameDao= CodebreakerDatabase.getInstance().getGameDao();
  }
  public Completable save(Game game){
    return Completable.fromSingle(gameDao.insert(game))
        .subscribeOn(Schedulers.io());

  }
}
