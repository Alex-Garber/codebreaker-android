package edu.cnm.deepdive.codebreaker.model.dao;

import android.renderscript.Sampler.Value;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
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
@Delete
  Single<Integer> delete (Game... game);
  @Delete
  Single<Integer> delete (Collection<Game> game);

  @Update
  Single<Integer> update(Game game);

  @Update
  Single<Integer> update(Game... games);

  @Update
  Single<Integer> update(Collection<Game> games);

  @Query(value = "SELECT * FROM Game ORDER BY code_length DESC, guess_count ASC")
  LiveData<List<Game>> selectAll();

  @Query("SELECT * FROM Game WHERE code_length = :codeLength ORDER BY guess_count ASC LIMIT :recordCount")
  LiveData<List<Game>> selectBest(int codeLength, int recordCount);
}
