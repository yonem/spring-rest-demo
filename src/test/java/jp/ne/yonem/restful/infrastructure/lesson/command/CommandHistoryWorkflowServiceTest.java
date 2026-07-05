package jp.ne.yonem.restful.infrastructure.lesson.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandHistoryWorkflowServiceTest {

  @InjectMocks private CommandHistoryWorkflowService sut;

  @Mock private EditorCommand command;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: コマンドを実行した際、オブジェクトのexecuteが呼び出され、undoは呼ばれないこと")
    void test01() {
      // 実行
      sut.executeCommand(command);

      // 検証
      verify(command, times(1)).execute();
      verify(command, never()).undo();
    }

    @Test
    @DisplayName("正常系: 履歴が存在する状態でUndoを実行した際、コマンドのundoが呼び出されること")
    void test02() {
      // 準備: 一度実行してスタックに積む
      sut.executeCommand(command);
      reset(command); // カウントをリセット

      // 実行: Undoを要求
      sut.undoLastCommand();

      // 検証
      verify(command, times(1)).undo();
      verify(command, never()).execute();
    }

    @Test
    @DisplayName("正常系: 履歴が空の状態でUndoを実行しても、例外が発生せず何も処理されないこと")
    void test03() {
      // 実行: 空の状態でUndo
      sut.undoLastCommand();

      // 検証: モックは一切触られていないこと
      verifyNoInteractions(command);
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 実行するコマンドがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.executeCommand(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("command must not be null");
    }
  }
}
