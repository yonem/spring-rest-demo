package jp.ne.yonem.restful.infrastructure.lesson.memento;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameProgressWorkflowServiceTest {

  @InjectMocks private GameProgressWorkflowService sut;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: セーブポイントを作成し、状態を変更した後にロードすると元の状態に復元されること")
    void test01() {
      var originator = new GameOriginator();
      originator.changeState(100, 1); // 初期状態

      sut.saveCheckpoint(originator);

      originator.changeState(50, 2);
      assertThat(originator.getHp()).isEqualTo(50);
      assertThat(originator.getStage()).isEqualTo(2);

      sut.loadLastCheckpoint(originator);

      assertThat(originator.getHp()).isEqualTo(100);
      assertThat(originator.getStage()).isEqualTo(1);
    }

    @Test
    @DisplayName("正常系: セーブデータが存在しない状態でロードを要求しても、例外が発生せず現在の状態が維持されること")
    void test02() {
      // 準備
      var originator = new GameOriginator();
      originator.changeState(80, 5);

      sut.loadLastCheckpoint(originator);

      assertThat(originator.getHp()).isEqualTo(80);
      assertThat(originator.getStage()).isEqualTo(5);
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: セーブ時にOriginatorがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.saveCheckpoint(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("originator must not be null");
    }

    @Test
    @DisplayName("異常系: ロード時にOriginatorがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.loadLastCheckpoint(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("originator must not be null");
    }
  }
}
