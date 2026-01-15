package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

  public List<Integer> modern2(List<String> rawScores) {
    return rawScores.stream()
        .map(this::parseIntSafely) // 1. 安全な変換（Optionalを返す）
        .flatMap(Optional::stream) // 2. 存在する値（成功分）だけを抽出して平坦化
        .filter(score -> score >= 0) // 3. 業務ルールの適用
        .toList();
  }

  /** 文字列を数値に変換します。失敗した場合は空のOptionalを返します。 */
  private Optional<Integer> parseIntSafely(String s) {
    try {
      return Optional.of(Integer.parseInt(s));
    } catch (NumberFormatException e) {
      System.err.println("Invalid score: " + s);
      return Optional.empty();
    }
  }

  public List<Integer> modern(List<String> rawScores) {
    return rawScores.stream().map(Integer::parseInt).filter(i -> 0 <= i).toList();
  }
}
