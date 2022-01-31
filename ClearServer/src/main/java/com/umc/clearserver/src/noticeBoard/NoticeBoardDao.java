package com.umc.clearserver.src.noticeBoard;

import com.umc.clearserver.src.noticeBoard.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class NoticeBoardDao {
    private static JdbcTemplate jdbcTemplate;
    private final AwsS3Service awsS3Service;

    public NoticeBoardDao(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PostNoticeBoardRes postNoticeBoardRes(String beforePicUrl, String afterPicUrl, String userID){
        String getUserIdQuery = "SELECT id from user where email=?";
        String userEmail = userID;
        int ID = this.jdbcTemplate.queryForObject(getUserIdQuery, int.class, userEmail);
        String insertNoticeBoardQuery = "INSERT INTO noticeBoard(beforePic, afterPic, writer, isWaited) VALUES(?, ?, ? ,?)";
        Object[] insertNoticeBoardParams = new Object[]{beforePicUrl, afterPicUrl, ID, 0};
        int result = this.jdbcTemplate.update(insertNoticeBoardQuery, insertNoticeBoardParams);

        if(result == 0) return new PostNoticeBoardRes("Failed", -1, "err", "err", "err", true);
        else return new PostNoticeBoardRes("Success", ID, userEmail, beforePicUrl, afterPicUrl, true);
    }

    public List<SearchClearNoticeBoardRes> viewNoticeBoard(SearchClearNoticeBoardReq searchClearNoticeBoardReq, String email){
        String getUserIdQuery = "SELECT id from user where email=?";
        String historyTime = "\'" +searchClearNoticeBoardReq.getYear()+"-" + searchClearNoticeBoardReq.getMonth() + "-" + searchClearNoticeBoardReq.getDay() +" "+23+":"+59+":00\'";

        String userEmail = email;
        int ID = this.jdbcTemplate.queryForObject(getUserIdQuery, int.class, userEmail);

        String getNoticeBoardQuery =  String.format("SELECT id, score, contents, comments, beforePic, afterPic From noticeBoard where writer = %d AND createdAt < %s", ID, historyTime);
        //Object[] getNoticeBoardQueryParam = new Object[]{ID, historyTime};
        System.out.println("=======================");
        System.out.println("유저 " + email + "의 " + historyTime+"이전 게시물을 조회합니다.");
        System.out.println("Executed Query: "+getNoticeBoardQuery);
        System.out.println("=======================");
        return this.jdbcTemplate.query(getNoticeBoardQuery,
                (rs, rowNum) -> new SearchClearNoticeBoardRes(
                        rs.getInt("id"),
                        userEmail,
                        rs.getDouble("score"),
                        rs.getString("contents"),
                        rs.getString("comments"),
                        rs.getString("beforePic"),
                        rs.getString("afterPic")
                ));
    }

    public DeleteNoticeBoardRes deleteNoticeBoard(int idx, String email){
//        String getBeforPicQuery =  String.format("SELECT id, score, contents, comments, beforePic, afterPic From noticeBoard where id=?", idx);
//        System.out.println(searchClearNoticeBoardRes.get(0).getAfterPicUrl());
        String deleteBeforeFileName = awsS3Service.deleteFile("moduclear", "testUser/2e076100-cff9-43cf-9397-2d41248e0981kitty.jpeg");
        String deleteAfterFileName = awsS3Service.deleteFile("moduclear", "testUser/9d773de6-b4fc-49d5-be5c-0e03e0c6bb41쌀국수.jpeg");
        String deleteQuery ="DELETE from noticeBoard where id = ?;";
        int res = this.jdbcTemplate.update(deleteQuery, idx);
        if(res==1){
            return new DeleteNoticeBoardRes("failed", -1, "null", "null");
        }
        else{
            return new DeleteNoticeBoardRes("failed", -1, "null", "null");
        }
    }
}
