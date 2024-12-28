package com.smartParking.dao.impl;

import com.smartParking.model.LotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LotManagerDAOImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<LotManager> topUserRowMapper = new RowMapper<>() {
        @Override
        public LotManager mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LotManager(
                    rs.getInt("lot_id"),
                    rs.getBigDecimal("longitude"),
                    rs.getBigDecimal("latitude"),
                    rs.getBigDecimal("total_revenue"),
                    rs.getBigDecimal("total_penalty"),
                    rs.getInt("capacity"),
                    rs.getInt("occupied")
            );
        }
    };
    public List<LotManager> getLots(int id){
        String sql = "SELECT parking_lots.lot_id , parking_lots.longitude , parking_lots.latitude , " +
        "parking_lots.total_revenue , parking_lots.total_penalty , parking_lots.capacity ,t.occupied " +
        "from (SELECT lot_id, COUNT(*) as occupied FROM parking_spots where status = 'OCCUPIED' " +
        "GROUP BY lot_id) t INNER JOIN parking_lots ON t.lot_id = parking_lots.lot_id AND parking_lots.manager_id = ? ";

        return jdbcTemplate.query(sql, topUserRowMapper , id);
    }

    public LotManager getLotById(int id){
        String sql = "SELECT parking_lots.lot_id , parking_lots.longitude , parking_lots.latitude , " +
        "parking_lots.total_revenue , parking_lots.total_penalty , parking_lots.capacity ,t.occupied " +
        "from (SELECT lot_id, COUNT(*) as occupied FROM parking_spots where status = 'OCCUPIED' " +
        "GROUP BY lot_id) t INNER JOIN parking_lots ON t.lot_id = parking_lots.lot_id AND parking_lots.lot_id = ? ";

        return jdbcTemplate.queryForObject(sql, topUserRowMapper , id);
    }
}
