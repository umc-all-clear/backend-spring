package com.umc.clearserver.src.friend;

import com.umc.clearserver.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.umc.clearserver.src.friend.model.*;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.util.List;
import com.umc.clearserver.src.utils.Pair;

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
        int u1 = patchFriendReq.getUser1();
        int u2 = patchFriendReq.getUser2();
        int friendId = jdbcTemplate.queryForObject(
                "SELECT id from friend where user1 = ? and user2 = ?", Integer.class, u1, u2);
        String deleteFriendQuery = "update friend set state = ? where id = ?"; // 해당 id를 만족하는 Friend를 해당 state으로 변경한다.
        Object[] deleteFriendParams = new Object[]{0, friendId}; // 주입될 값들(state, id) 순

        return this.jdbcTemplate.update(deleteFriendQuery, deleteFriendParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

    public List<GetFriendRankingRes> getFriendRank(Integer userId, Integer year, Integer month){
        int nextMonth = month+1;

        String prevMonth = "\'" + year + "-" + month + "-1 0:0:0'";
        String postMonth = "\'" + year + "-" + nextMonth + "-1 0:0:0'";

        //우선 친구 목록을 불러옵니다.
        String getFriendListQuery ="SELECT DISTINCT user2, email FROM friend, user where user.id=friend.user2 and friend.user1 = ?;";
        List<Pair> friendList = this.jdbcTemplate.query(getFriendListQuery,
                (rs, rowNum) -> new Pair(
                        rs.getInt("user2"),
                        rs.getString("email"),
                        0
                )
                , userId);

        //친구들 중 noticeBoard에 게시글을 올린 사람을 불러와 평점순으로 내림차순 정렬합니다.
        String getUserRankQuery = "SELECT writer, email ,AVG(score)\n"+
                "From noticeBoard, user\n" +
                "where" + prevMonth + "< noticeBoard.createdAt and noticeBoard.createdAt <" + postMonth+ "and noticeBoard.writer = user.id\n" +
                "group by writer\n" +
                "HAVING writer IN(SELECT user2 FROM friend WHERE user1 = ?) \n" +
                "ORDER BY AVG(score) DESC";
        System.out.println(getUserRankQuery);
        List<GetFriendRankingRes> getFriendRankingResList =  this.jdbcTemplate.query(getUserRankQuery,
                (rs, rowNum) -> new GetFriendRankingRes(
                        rs.getInt("writer"),
                        rs.getString("email"),
                        rs.getDouble("AVG(score)")
                ), userId);

        //이제 게시글을 안올렸지만 친구목록에 있는 사람들을 뒤에 붙입니다.
        for(int i=0; i<getFriendRankingResList.size(); i++)
        {
            for(int j=0; j<friendList.size(); j++)
            {
                if(getFriendRankingResList.get(i).getFriendIndex() == friendList.get(j).getFirst())
                {
                    friendList.get(j).setThird(1);
                    continue;
                }
            }
        }
        for(int i=0; i<friendList.size(); i++)
        {
            if(friendList.get(i).getThird()==0)
            {
                GetFriendRankingRes tempRank = new GetFriendRankingRes(friendList.get(i).getFirst(), friendList.get(i).getSecond(), 0.0);
                getFriendRankingResList.add(tempRank);
            }
        }

        return getFriendRankingResList;
    }

    // 해당 userIdx를 갖는 유저조회
    public List<GetFriendRelationRes> getFriendRelation(String user1, String user2) {
        System.out.println("========친구 관계를 조회합니다========");
        System.out.println("user1 이메일: " + user1);
        System.out.println("user1 이메일: " + user2);
        System.out.println("========친구 관계를 조회 끝=========");

        String isUserExist = "select COUNT(state) from user where email=?";
        int isUserExistCnt = this.jdbcTemplate.queryForObject(isUserExist, Integer.class ,user2);

        if(isUserExistCnt == 0)
        {
            return (List<GetFriendRelationRes>) new GetFriendRelationRes(-1, "user2NotExist!", "user2NotExist!", -1);
        }

        String getFriendRelationQuery = "select COUNT(state) from friend where user1 = (SELECT id FROM user WHERE email=?) and user2 = (SELECT id FROM user WHERE email=?);"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        Object[] getFriendRelationParams = new Object[]{user1, user2};

        String getFriendInfoQuery = "SELECT nickname, email, id FROM user WHERE email = ?";
        int friendState = this.jdbcTemplate.queryForObject(getFriendRelationQuery, Integer.class, getFriendRelationParams);

        return this.jdbcTemplate.query(getFriendInfoQuery,
                (rs, rowNum) -> new GetFriendRelationRes(
                        friendState,
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getInt("id")
                ), user2);
    }


    public int createFriend(CreateFriendReq createFriendReq) {
        String createFriendQuery = "insert into friend(user1, user2, isAccepted,state) VALUES(?,?,?,?)"; // 실행될 동적 쿼리문

        List<GetUserRes> user1 = getUsersByEmail(createFriendReq.getUser1());
        List<GetUserRes> user2 = getUsersByEmail(createFriendReq.getUser2());

        int user1Idx = user1.get(0).getUserIdx();
        int user2Idx = user2.get(0).getUserIdx();

        if(user1Idx > user2Idx){
            int tmp = user1Idx;
            user1Idx = user2Idx;
            user2Idx = tmp;
        }

        CreateFriendRes createFriendRes = new CreateFriendRes(user1Idx,user2Idx,1,1);

        Object[] createFriendParams = new Object[]{createFriendRes.getUser1(), createFriendRes.getUser2(), 1, 1}; // 동적 쿼리의 ?부분에 주입될 값

        return this.jdbcTemplate.update(createFriendQuery, createFriendParams);
    }


}