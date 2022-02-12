package com.umc.clearserver.src.friend;

import com.umc.clearserver.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.umc.clearserver.src.friend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FriendDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from user where email = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    public List<GetUserRes> getUsers() {
        String getUsersQuery = "select * from user"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password")) // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
        ); // 복수개의 회원정보들을 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보)의 결과 반환(동적쿼리가 아니므로 Parmas부분이 없음)
    }

    public List<GetUserRes> getUsersByEmail(String email) {
        String getUsersByNicknameQuery = "select * from user where email =?"; // 해당 이메일을 만족하는 유저를 조회하는 쿼리문
        String getUsersByNicknameParams = email;
        return this.jdbcTemplate.query(getUsersByNicknameQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUsersByNicknameParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    public int deleteFriend(PatchFriendReq patchFriendReq) {
        /*
        String deleteProductQuery = "update Friend set state = ? where user1 = ? and user2 = ? "; // 해당 productIdx를 만족하는 Product를 해당 productName으로 변경한다.
        Object[] deleteProductParams = new Object[]{"inactive", patchFriendReq.getUser1(), patchFriendReq.getUser2()}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(deleteProductQuery, deleteProductParams);
        */
        int u1 = patchFriendReq.getUser1();
        int u2 = patchFriendReq.getUser2();
        int friendId = jdbcTemplate.queryForObject(
                "SELECT id from friend where user1 = ? and user2 = ?", Integer.class, u1, u2);
        String deleteFriendQuery = "update friend set state = ? where id = ?"; // 해당 id를 만족하는 Friend를 해당 state으로 변경한다.
        Object[] deleteFriendParams = new Object[]{0, friendId}; // 주입될 값들(state, id) 순

        return this.jdbcTemplate.update(deleteFriendQuery, deleteFriendParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)

    }

    // User 테이블에 존재하는 전체 유저들의 정보 조회
    public List<GetFriendRes> getFriends() {
        String getFriendsQuery = "select * from friend"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getFriendsQuery,
                (rs, rowNum) -> new GetFriendRes(
                        rs.getInt("id"),
                        rs.getInt("user1"),
                        rs.getInt("user2")
                ) // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
        ); // 복수개의 회원정보들을 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보)의 결과 반환(동적쿼리가 아니므로 Parmas부분이 없음)
    }

    // 해당 id를 갖는 유저조회
    public GetFriendRes getFriend(int id) {
        String getFriendQuery = "select * from friend where id = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        int getFriendParams = id;
        return this.jdbcTemplate.queryForObject(getFriendQuery,
                (rs, rowNum) -> new GetFriendRes(
                        rs.getInt("id"),
                        rs.getInt("user1"),
                        rs.getInt("user2")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getFriendParams); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 해당 user1을 갖는 유저들의 정보 조회
    public List<GetFriendRes> getFriendsByUserId(Integer userId) {
        String getFrinedsByUserIdQuery = "select * from friend where user1 =?"; // 해당 이메일을 만족하는 유저를 조회하는 쿼리문
        Integer getFriendsByUserIdParams = userId;
        return this.jdbcTemplate.query(getFrinedsByUserIdQuery,
                (rs, rowNum) -> new GetFriendRes(
                        rs.getInt("id"),
                        rs.getInt("user1"),
                        rs.getInt("user2")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getFriendsByUserIdParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }
}
