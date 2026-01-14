package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.ArrayList;
import java.util.List;

public class ParseScores {

  public List<Integer> legacy(List<String> rawScores) {
    List<Integer> scores = new ArrayList<>();
    for (String s : rawScores) {
      try {
        int score = Integer.parseInt(s);
        if (score >= 0) {
          scores.add(score);
        }
      } catch (NumberFormatException e) {
        // 不正な文字はスキップ
        System.err.println("Invalid score: " + s);
      }
    }
    return scores;
  }
}
