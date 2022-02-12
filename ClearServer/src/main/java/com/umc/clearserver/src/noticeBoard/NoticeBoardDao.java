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

    public PostNoticeBoardRes postNoticeBoardRes(String beforePicUrl, String afterPicUrl, String userID, String userContent){
        String getUserIdQuery = "SELECT id from user where email=?";
        String userEmail = userID;
        int ID = this.jdbcTemplate.queryForObject(getUserIdQuery, int.class, userEmail);
        String insertNoticeBoardQuery = "INSERT INTO noticeBoard(beforePic, afterPic, writer, contents,isWaited) VALUES(?, ?, ?, ?, ?)";
        Object[] insertNoticeBoardParams = new Object[]{beforePicUrl, afterPicUrl, ID, userContent, 0};
        int result = this.jdbcTemplate.update(insertNoticeBoardQuery, insertNoticeBoardParams);


        if(result == 0) return new PostNoticeBoardRes("Failed", -1, "err", "err", "err", "err", true);
        else return new PostNoticeBoardRes("Success", ID, userEmail, userContent, beforePicUrl, afterPicUrl, true);
    }

    public List<SearchClearNoticeBoardRes> viewNoticeBoard(SearchClearNoticeBoardReq searchClearNoticeBoardReq, String email){
        String getUserIdQuery = "SELECT id from user where email=?";
        int nextMonth = searchClearNoticeBoardReq.getMonth()+1;
        String historyTime_start = "\'" +searchClearNoticeBoardReq.getYear()+"-" + searchClearNoticeBoardReq.getMonth() + "-1 0:0:0\'";
        String historyTime_end = "\'" +searchClearNoticeBoardReq.getYear()+"-" + nextMonth + "-1 0:0:0\'";
        System.out.println(historyTime_start);
        System.out.println(historyTime_end);
        String userEmail = email;
        int ID = this.jdbcTemplate.queryForObject(getUserIdQuery, int.class, userEmail);

        String getNoticeBoardQuery =  String.format("SELECT * From noticeBoard where writer = %d AND createdAt > %s AND createdAt < %s", ID, historyTime_start, historyTime_end);
        //Object[] getNoticeBoardQueryParam = new Object[]{ID, historyTime};
        System.out.println("=======================");
        System.out.println("유저 " + email + "의 " + historyTime_start + "이후, " + historyTime_end+"이전 게시물을 조회합니다.");
        System.out.println("Executed Query: "+getNoticeBoardQuery);
        System.out.println("=======================");
        return this.jdbcTemplate.query(getNoticeBoardQuery,
                (rs, rowNum) -> new SearchClearNoticeBoardRes(
                        rs.getInt("id"),
                        rs.getString("cleanedAt"),
                        userEmail,
                        rs.getDouble("score"),
                        rs.getString("contents"),
                        rs.getString("comments"),
                        rs.getString("beforePic"),
                        rs.getString("afterPic"),
                        rs.getBoolean("isWaited")
                ));
    }

    public DeleteNoticeBoardRes deleteNoticeBoard(int idx, String email){
        String beforePicFileName = null, afterPicFileName = null;
        String getBeforPicQuery =  String.format("SELECT * From noticeBoard where id=?", idx);
        SearchClearNoticeBoardRes searchClearNoticeBoardRes;
        searchClearNoticeBoardRes = this.jdbcTemplate.queryForObject(getBeforPicQuery,
                (rs, rowNum) -> new SearchClearNoticeBoardRes(
                        rs.getInt("id"),
                        rs.getString("cleanedAt"),
                        email,
                        rs.getDouble("score"),
                        rs.getString("contents"),
                        rs.getString("comments"),
                        rs.getString("beforePic"),
                        rs.getString("afterPic"),
                        rs.getBoolean("isWaited")
                ), idx);

        String[] beforeUrlSplit = searchClearNoticeBoardRes.getBeforePicUrl().split("/");
        String[] afterUrlSplit = searchClearNoticeBoardRes.getAfterPicUrl().split("/");
        for(int i=0; i< beforeUrlSplit.length; i++)
        {
            if(beforeUrlSplit[i].equals(email)) beforePicFileName = beforeUrlSplit[i+1];
        }
        for(int i=0; i< afterUrlSplit.length; i++)
        {
            if(afterUrlSplit[i].equals(email)) afterPicFileName = afterUrlSplit[i+1];
        }
        System.out.println(beforePicFileName);
        System.out.println(afterPicFileName);
        String deleteBeforeFileName = awsS3Service.deleteFile("moduclear", email+"/"+beforePicFileName);
        String deleteAfterFileName = awsS3Service.deleteFile("moduclear", email+"/"+afterPicFileName);
        int res = 1;
        String deleteQuery ="DELETE from noticeBoard where id = ?;";
        //int res = this.jdbcTemplate.update(deleteQuery, idx);
        if(res==1){
            return new DeleteNoticeBoardRes("success", idx, deleteBeforeFileName, deleteAfterFileName);
        }
        else{
            return new DeleteNoticeBoardRes("failed", -1, "null", "null");
        }

    }

}
