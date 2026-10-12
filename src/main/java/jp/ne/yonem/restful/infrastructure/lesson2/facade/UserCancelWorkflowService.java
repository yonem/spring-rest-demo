package jp.ne.yonem.restful.infrastructure.lesson2.facade;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Facadeパターンを利用して退会処理を実行するサービスです。 */
@Service
public class UserCancelWorkflowService {

  private final UserDeactivationFacade deactivationFacade;

  public UserCancelWorkflowService(UserDeactivationFacade deactivationFacade) {
    this.deactivationFacade =
        Objects.requireNonNull(deactivationFacade, "deactivationFacade must not be null");
  }

  /**
   * 指定されたユーザーの退会処理を実行します。
   *
   * @param userId ユーザーID
   * @return 処理結果
   */
  public String execute(String userId) {
    var safeUserId = Objects.requireNonNull(userId, "userId must not be null");
    return deactivationFacade.processDeactivation(safeUserId);
  }
}
