package com.umc.clearserver.src.admin;

import com.umc.clearserver.src.admin.model.PostEvaluateReq;
import com.umc.clearserver.src.noticeBoard.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
@Repository
public class AdminDao {
    private static JdbcTemplate jdbcTemplate;
    private final AwsS3Service awsS3Service;

    public AdminDao(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int evaluate(PostEvaluateReq postEvaluateReq) {
        float score = postEvaluateReq.getScore();
        String comments = postEvaluateReq.getComments();
        int id = postEvaluateReq.getId();
        System.out.print(score+"/n"+comments+"/n"+id);
        String modifyEvaluateQuery = "update noticeBoard set score = ? , comments = ?, isWaited = true where id = ?"; // 해당 productIdx를 만족하는 Product를 해당 productName으로 변경한다.
        Object[] modifyEvaluateParams = new Object[]{score, comments, id}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(modifyEvaluateQuery, modifyEvaluateParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }
}
