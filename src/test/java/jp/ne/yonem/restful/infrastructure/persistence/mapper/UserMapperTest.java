package jp.ne.yonem.restful.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;

import jp.ne.yonem.restful.infrastructure.persistence.record.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@MybatisTest
@Transactional
public class UserMapperTest {
  @Autowired private UserMapper sut;

  @Test
  @DisplayName("ユーザ登録と検索")
  void test01() {
    var user = new User(null, "testuser_insert", "insert@example.com", "password123", 1, null);
    var inserted = sut.insert(user);

    var act = sut.findById(user.getId());
    assertEquals(1, inserted);
    assertNotNull(act);
    assertEquals(user.getId(), act.getId());
    assertEquals(user.getEmail(), act.getEmail());
    assertEquals(user.getUserName(), act.getUserName());
    assertEquals(user.getRoles(), act.getRoles());
    assertNotNull(act.getCreatedAt());
  }

  @Test
  @DisplayName("ユーザー名によるユーザー検索")
  void test02() {
    var user = new User(null, "testuser_insert", "insert@example.com", "password123", 1, null);
    var inserted = sut.insert(user);

    // 存在するユーザー名で検索
    var act = sut.findByEmail("insert@example.com");
    assertEquals(1, inserted);
    assertNotNull(act);
    assertEquals(user.getId(), act.getId());
    assertEquals(user.getEmail(), act.getEmail());
    assertEquals(user.getUserName(), act.getUserName());
    assertEquals(user.getRoles(), act.getRoles());
    assertNotNull(act.getCreatedAt());

    // 存在しないユーザー名で検索
    var notFoundUser = sut.findByEmail("nonexistent_user@example.com");
    assertNull(notFoundUser);
  }

  //
  //  /** 全ユーザー取得テスト。 複数のユーザーを挿入し、すべてが取得されることを確認します。 */
  //  @Test
  //  void testFindAllUsers() {
  //    // テスト用のユーザーを複数挿入
  //    User user1 = new User(null, "user1", "user1@example.com", "pass1", 1, OffsetDateTime.now());
  //    User user2 = new User(null, "user2", "user2@example.com", "pass2", 2, OffsetDateTime.now());
  //    userMapper.insertUser(user1);
  //    userMapper.insertUser(user2);
  //
  //    List<User> users = userMapper.findAllUsers();
  //
  //    // 挿入したユーザーがリストに含まれていることを確認
  //    assertThat(users).isNotNull();
  //    assertThat(users).hasSizeGreaterThanOrEqualTo(2); // 他のテストからのデータが残っている可能性も考慮
  //    assertThat(users).extracting(User::getUsername).contains("user1", "user2");
  //  }
  //
  //  /** ユーザー情報更新テスト。 ユーザー名とメールアドレスが正しく更新されることを確認します。 */
  //  @Test
  //  void testUpdateUser() {
  //    // テスト用のユーザーを挿入
  //    User user = new User();
  //    user.setUsername("testuser_update");
  //    user.setEmail("update@example.com");
  //    user.setPassword("old_password");
  //    user.setRoles(1);
  //    userMapper.insertUser(user);
  //
  //    // ユーザー情報を更新
  //    user.setUsername("updated_user");
  //    user.setEmail("updated@example.com");
  //    user.setPassword("new_password"); // パスワードも更新可能
  //    user.setRoles(2);
  //    int result = userMapper.updateUser(user);
  //
  //    // 更新が成功し、1行が影響を受けたことを確認
  //    assertThat(result).isEqualTo(1);
  //
  //    // 更新されたユーザーを検索して検証
  //    User updatedUser = userMapper.findUserById(user.getId());
  //    assertThat(updatedUser).isNotNull();
  //    assertThat(updatedUser.getUsername()).isEqualTo("updated_user");
  //    assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
  //    assertThat(updatedUser.getRoles()).isEqualTo(2);
  //    // パスワードは通常ハッシュ化されるため、ここでは直接比較しないが、DBに正しく保存されたことを確認
  //    assertThat(updatedUser.getPassword()).isEqualTo("new_password");
  //  }
  //
  //  /** ユーザー削除テスト。 ユーザーが正しくデータベースから削除されることを確認します。 */
  //  @Test
  //  void testDeleteUser() {
  //    // テスト用のユーザーを挿入
  //    User user = new User();
  //    user.setUsername("testuser_delete");
  //    user.setEmail("delete@example.com");
  //    user.setPassword("password123");
  //    user.setRoles(1);
  //    userMapper.insertUser(user);
  //
  //    // ユーザーを削除
  //    int result = userMapper.deleteUser(user.getId());
  //
  //    // 削除が成功し、1行が影響を受けたことを確認
  //    assertThat(result).isEqualTo(1);
  //
  //    // 削除されたユーザーが検索できないことを確認
  //    User deletedUser = userMapper.findUserById(user.getId());
  //    assertThat(deletedUser).isNull();
  //  }
}
