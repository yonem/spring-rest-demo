package jp.ne.yonem.restful.infrastructure.persistence.mapper;

import java.util.List;
import java.util.Optional;
import jp.ne.yonem.restful.infrastructure.persistence.record.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

  /**
   * 新しいユーザーを挿入します。
   *
   * @param user 挿入するUserオブジェクト
   * @return 挿入された行数
   */
  int insert(User user);

  /**
   * IDに基づいてユーザーを検索します。
   *
   * @param id ユーザーID
   * @return 該当するUserオブジェクト、見つからない場合はnull
   */
  Optional<User> findById(@Param("id") Integer id);

  /**
   * メールアドレスに基づいてユーザーを検索します。
   *
   * @param email メールアドレス
   * @return 該当するUserオブジェクト、見つからない場合はnull
   */
  Optional<User> findByEmail(@Param("email") String email);

  /**
   * すべてのユーザーを取得します。
   *
   * @return Userオブジェクトのリスト
   */
  Optional<List<User>> findAllUsers();

  /**
   * ユーザー情報を更新します。
   *
   * @param user 更新するUserオブジェクト
   * @return 更新された行数
   */
  int updateUser(User user);

  /**
   * IDに基づいてユーザーを削除します。
   *
   * @param id 削除するユーザーID
   * @return 削除された行数
   */
  int deleteUser(@Param("id") Integer id);
}
