package com.smartParking.dao.impl;

import com.smartParking.dao.ParkingLotDAO;
import com.smartParking.model.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
                    rs.getString("location"),
                    rs.getInt("capacity"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    };

    @Override
    public int createParkingLot(ParkingLot parkingLot) {
        String sql = "INSERT INTO parking_lots (name, location, capacity) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, parkingLot.getName());
            ps.setString(2, parkingLot.getLocation());
            ps.setInt(3, parkingLot.getCapacity());
            return ps;
        }, keyHolder);

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
        String sql = "UPDATE parking_lots SET name = ?, location = ?, capacity = ? WHERE lot_id = ?";
        return jdbcTemplate.update(sql, parkingLot.getName(), parkingLot.getLocation(), parkingLot.getCapacity(), parkingLot.getLotId()) > 0;
    }

    @Override
    public boolean deleteParkingLot(int lotId) {
        String sql = "DELETE FROM parking_lots WHERE lot_id = ?";
        return jdbcTemplate.update(sql, lotId) > 0;
    }
}
