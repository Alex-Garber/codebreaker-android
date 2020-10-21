package edu.cnm.deepdive.codebreaker.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.codebreaker.model.dao.ScoreDao;
import edu.cnm.deepdive.codebreaker.model.entity.Score;
import edu.cnm.deepdive.codebreaker.service.CodebreakerDatabase.Converters;
import java.util.Date;

@Database(entities = {Score.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CodebreakerDatabase extends RoomDatabase {
  // Name of file that is our data base
  private static final String DB_NAME = "codebreaker";

  // Must generate a setter for context
  private static Application context;

  public static void setContext(Application context) {
    CodebreakerDatabase.context = context;
  }
  public static CodebreakerDatabase getInstance(){
    return InstanceHolder.INSTANCE;

  }
  // creates a way to get instance of GameDao
  public abstract ScoreDao getScoreDao();
// Builder builds what INSTANCE holds ie the database
  private static class InstanceHolder {
    private static final CodebreakerDatabase INSTANCE =
        Room.databaseBuilder(context,CodebreakerDatabase.class, DB_NAME)
        .build();
  }
// date obj to long
  public static class Converters {
    @TypeConverter
    // date obj to long
    public static Long dateToLong(Date value) {
      return (value != null) ? value.getTime() : null;
    }

  @TypeConverter
  // long to date
  public static Date longToDate(Long value) {
    return (value != null) ? new Date(value) : null;
  }

  }

}
