package com.umc.clearserver.src.noticeBoard;

import com.umc.clearserver.src.noticeBoard.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

@Repository
public class NoticeBoardDao {
    private static JdbcTemplate jdbcTemplate;
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
}
