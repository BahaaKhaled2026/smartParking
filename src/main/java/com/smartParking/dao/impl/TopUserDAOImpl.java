package com.smartParking.dao.impl;
import com.smartParking.model.TopUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TopUserDAOImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<TopUser> topUserRowMapper = new RowMapper<>() {
        @Override
        public TopUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TopUser(
                    rs.getInt("count"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("license_plate"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    };

    public List<TopUser> getTopUsers(){
        String sql = "SELECT t1.count , users.username ,users.email , users.license_plate  , users.created_at from (SELECT user_id, COUNT(*) as count" +
                " FROM reservations" +
                " where status = 'COMPLETED'" +
                " GROUP BY user_id) t1 INNER JOIN users ON t1.user_id = users.user_id ORDER BY count DESC";

        return jdbcTemplate.query(sql, topUserRowMapper);
    }

}
