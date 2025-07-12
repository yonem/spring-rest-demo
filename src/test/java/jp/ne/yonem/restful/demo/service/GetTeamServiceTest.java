package jp.ne.yonem.restful.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import jp.ne.yonem.restful.demo.entity.Team;
import jp.ne.yonem.restful.demo.mapper.TeamMapper;
import org.apache.ibatis.javassist.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTeamServiceTest {
  @Mock private TeamMapper mapper;
  @InjectMocks private GetTeamService sut;

  @Test
  @DisplayName("正常系: チーム取得")
  void test01() throws NotFoundException {
    when(mapper.findById(any())).thenReturn(new Team(1, null, List.of()));

    var act = sut.execute(1);
    assertEquals(1, act.getId());
    verify(mapper, times(1)).findById(any());
  }

  @Test
  @DisplayName("異常系: チーム取得: 存在しないチーム")
  void test02() {
    when(mapper.findById(any())).thenReturn(null);

    var thrown = assertThrows(NotFoundException.class, () -> sut.execute(1));
    assertEquals("Team is not found.", thrown.getMessage());
    verify(mapper, times(1)).findById(any());
  }
}
