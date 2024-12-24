package com.smartParking.dao.impl;

import com.smartParking.dao.ParkingSpotDAO;
import com.smartParking.model.ParkingSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ParkingSpotDAOImpl implements ParkingSpotDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for ParkingSpot
    private final RowMapper<ParkingSpot> parkingSpotRowMapper = new RowMapper<>() {
        @Override
        public ParkingSpot mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ParkingSpot(
                    rs.getInt("spot_id"),
                    rs.getInt("lot_id"),
                    rs.getString("spot_number"),
                    rs.getString("type"),
                    rs.getString("status")
            );
        }
    };

    @Override
    public int createParkingSpot(ParkingSpot parkingSpot) {
        String sql = "INSERT INTO parking_spots (lot_id, spot_number, type, status) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, parkingSpot.getLotId(), parkingSpot.getSpotNumber(),
                parkingSpot.getType(), parkingSpot.getStatus());
    }

    @Override
    public Optional<ParkingSpot> getParkingSpotById(int spotId) {
        String sql = "SELECT * FROM parking_spots WHERE spot_id = ?";
        return jdbcTemplate.query(sql, parkingSpotRowMapper, spotId).stream().findFirst();
    }

    @Override
    public List<ParkingSpot> getParkingSpotsByLotId(int lotId) {
        String sql = "SELECT * FROM parking_spots WHERE lot_id = ?";
        return jdbcTemplate.query(sql, parkingSpotRowMapper, lotId);
    }

    @Override
    @Transactional
    public boolean updateParkingSpot(ParkingSpot parkingSpot) {
        String sql = "UPDATE parking_spots SET spot_number = ?, type = ?, status = ? WHERE spot_id = ?";
        return jdbcTemplate.update(sql, parkingSpot.getSpotNumber(), parkingSpot.getType(),
                parkingSpot.getStatus(), parkingSpot.getSpotId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteParkingSpot(int spotId) {
        String sql = "DELETE FROM parking_spots WHERE spot_id = ?";
        return jdbcTemplate.update(sql, spotId) > 0;
    }

    @Transactional
    public boolean reserveSpot(int spotId) {
        // Step 1: Lock the parking spot for update
        String lockSpotSql = "SELECT status FROM parking_spots WHERE spot_id = ? FOR UPDATE";
        String status = jdbcTemplate.queryForObject(lockSpotSql, String.class, spotId);

        if (!"AVAILABLE".equals(status)) {
            throw new IllegalStateException("Parking spot is not available for reservation.");
        }

        // Step 2: Reserve the spot
        String reserveSpotSql = "UPDATE parking_spots SET status = 'RESERVED' WHERE spot_id = ?";
        return jdbcTemplate.update(reserveSpotSql, spotId) > 0;
    }

    @Transactional
    public boolean releaseSpot(int spotId) {
        // Step 1: Lock the parking spot for update
        String lockSpotSql = "SELECT status FROM parking_spots WHERE spot_id = ? FOR UPDATE";
        String status = jdbcTemplate.queryForObject(lockSpotSql, String.class, spotId);

        if (!"RESERVED".equals(status)) {
            throw new IllegalStateException("Parking spot is not reserved, so it cannot be released.");
        }

        // Step 2: Release the spot
        String releaseSpotSql = "UPDATE parking_spots SET status = 'AVAILABLE' WHERE spot_id = ?";
        return jdbcTemplate.update(releaseSpotSql, spotId) > 0;
    }

    @Override
    public List<ParkingSpot> findAvailableSpotsByLocation(String location) {
        String sql = """
        SELECT ps.*
        FROM parking_spots ps
        JOIN parking_lots pl ON ps.lot_id = pl.lot_id
        WHERE pl.location = ? AND ps.status = 'AVAILABLE'
    """;
        return jdbcTemplate.query(sql, parkingSpotRowMapper, location);
    }

}
