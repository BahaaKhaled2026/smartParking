package com.smartParking.dao.impl;

import com.smartParking.dao.ParkingLotDAO;
import com.smartParking.model.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ParkingLotDAOImpl implements ParkingLotDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for ParkingLot
    private final RowMapper<ParkingLot> parkingLotRowMapper = new RowMapper<>() {
        @Override
        public ParkingLot mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ParkingLot(
                    rs.getInt("lot_id"),
                    rs.getString("name"),
                    rs.getBigDecimal("longitude"),
                    rs.getBigDecimal("latitude"),
                    rs.getInt("capacity"),
                    rs.getBigDecimal("total_revenue"),
                    rs.getBigDecimal("total_penalty"),
                    rs.getTimestamp("created_at").toLocalDateTime() ,
                    rs.getInt("manager_id")
            );
        }
    };

    @Override
    public int createParkingLot(ParkingLot parkingLot) {
        String sql = "INSERT INTO parking_lots (name, longitude, latitude, capacity, total_revenue, total_penalty , manager_id) VALUES (?, ?, ?, ?, ?, ? , ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, parkingLot.getName());
            ps.setBigDecimal(2, parkingLot.getLongitude());
            ps.setBigDecimal(3, parkingLot.getLatitude());
            ps.setInt(4, parkingLot.getCapacity());
            ps.setBigDecimal(5, parkingLot.getTotalRevenue());
            ps.setBigDecimal(6, parkingLot.getTotalPenalty());
            ps.setInt(7, parkingLot.getManagerId());
            return ps;
        }, keyHolder);
        parkingLot.setLotId(keyHolder.getKey().intValue());
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    @Override
    public Optional<ParkingLot> getParkingLotById(int lotId) {
        String sql = "SELECT * FROM parking_lots WHERE lot_id = ?";
        return jdbcTemplate.query(sql, parkingLotRowMapper, lotId).stream().findFirst();
    }

    @Override
    public List<ParkingLot> getAllParkingLots() {
        String sql = "SELECT * FROM parking_lots";
        return jdbcTemplate.query(sql, parkingLotRowMapper);
    }

    @Override
    public boolean updateParkingLot(ParkingLot parkingLot) {
        String sql = "UPDATE parking_lots SET name = ?, longitude = ?, latitude = ?, capacity = ?, total_revenue = ?, total_penalty = ?  WHERE lot_id = ?";
        return jdbcTemplate.update(sql, parkingLot.getName(), parkingLot.getLongitude(), parkingLot.getLatitude(), parkingLot.getCapacity(), parkingLot.getTotalRevenue() , parkingLot.getTotalPenalty() , parkingLot.getLotId()) > 0;
    }

    @Override
    public boolean deleteParkingLot(int lotId) {
        String sql = "DELETE FROM parking_lots WHERE lot_id = ?";
        return jdbcTemplate.update(sql, lotId) > 0;
    }

    @Override
    @Transactional
    public void updateTotalRevenue(int lotId, BigDecimal cost) {
        String sql = "UPDATE parking_lots SET total_revenue = total_revenue + ? WHERE lot_id = ?";
        jdbcTemplate.update(sql, cost, lotId);
    }


    @Override
    @Transactional
    public void updateTotalPenalty(int lotId, BigDecimal penalty) {
        String sql = "UPDATE parking_lots SET total_penalty = total_penalty + ? WHERE lot_id = ?";
        jdbcTemplate.update(sql, penalty, lotId);
    }

    @Override
    public List<ParkingLot> getAllLotsOrderedByRevenue(){
        String sql = "SELECT * FROM parking_lots Order By total_revenue DESC" ;
        return jdbcTemplate.query(sql,parkingLotRowMapper) ;
    }

    @Override
    public List<ParkingLot> getParkingLotsByManagerId(int managerId) {
        String sql = "SELECT * FROM parking_lots WHERE manager_id = ?";
        return jdbcTemplate.query(sql, parkingLotRowMapper, managerId);
    }
}
