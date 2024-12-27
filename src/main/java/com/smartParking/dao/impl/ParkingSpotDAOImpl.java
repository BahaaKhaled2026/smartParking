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
        // First, lock the row for update
        String selectSql = "SELECT spot_id FROM parking_spots WHERE spot_id = ? FOR UPDATE";
        jdbcTemplate.queryForObject(selectSql, Integer.class, parkingSpot.getSpotId());

        // Now, update the parking spot
        String updateSql = "UPDATE parking_spots SET spot_number = ?, type = ?, status = ? WHERE spot_id = ?";
        return jdbcTemplate.update(updateSql, parkingSpot.getSpotNumber(), parkingSpot.getType(),
                parkingSpot.getStatus(), parkingSpot.getSpotId()) > 0;
    }


    @Override
    @Transactional
    public boolean deleteParkingSpot(int spotId) {
        String sql = "DELETE FROM parking_spots WHERE spot_id = ?";
        return jdbcTemplate.update(sql, spotId) > 0;
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

    @Override
    public int getLotIdBySpotId(int spotId) {
        String sql = "SELECT lot_id FROM parking_spots WHERE spot_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, spotId);
    }
}
