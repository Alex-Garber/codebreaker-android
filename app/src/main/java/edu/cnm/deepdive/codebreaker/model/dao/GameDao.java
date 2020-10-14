package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface GameDao {

@Insert
Single<Long> insert(Game game);
@Insert
Single<List<Long>> insert (Game...games);
@Insert
Single<List<Long>> insert (Collection<Game> games);


@Delete
  Single<Integer> delete (Game game);
  Single<Integer> delete (Game... game);
  Single<Integer> delete (Collection<Game> game);

}
