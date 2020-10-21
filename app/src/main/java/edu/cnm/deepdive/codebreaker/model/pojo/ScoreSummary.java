package edu.cnm.deepdive.codebreaker.model.pojo;

import androidx.room.ColumnInfo;

public class ScoreSummary {

  @ColumnInfo(name= "code_length")
  private int codeLength;
@ColumnInfo(name = "code_length")
  private double averageGuessCount;

  public int getCodeLength() {
    return codeLength;
  }

  public void setCodeLength(int codeLength) {
    this.codeLength = codeLength;
  }

  public double getAverageGuessCount() {
    return averageGuessCount;
  }

  public void setAverageGuessCount(double averageGuessCount) {
    this.averageGuessCount = averageGuessCount;
  }

}
