package com.smartParking.dao.impl;

import com.smartParking.dao.ReservationDAO;
import com.smartParking.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDAOImpl implements ReservationDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = new RowMapper<>() {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Reservation(
                    rs.getInt("reservation_id"),
                    rs.getInt("user_id"),
                    rs.getInt("spot_id"),
                    rs.getTimestamp("start_time").toLocalDateTime(),
                    rs.getTimestamp("end_time").toLocalDateTime(),
                    rs.getString("status"),
                    rs.getBigDecimal("penalty"),
                    rs.getBigDecimal("cost")
            );
        }
    };

    @Override
    @Transactional
    public int createReservation(Reservation reservation) {
        // Step 1: Lock the parking spot for update
       // String lockSpotSql = "SELECT status FROM parking_spots WHERE spot_id = ? FOR UPDATE";
       // String status = jdbcTemplate.queryForObject(lockSpotSql, String.class, reservation.getSpotId());

        String reservationSql = "INSERT INTO reservations (user_id, spot_id, start_time, end_time, status, penalty, cost) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(reservationSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, reservation.getUserId());
            ps.setInt(2, reservation.getSpotId());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(reservation.getStartTime()));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(reservation.getEndTime()));
            ps.setString(5, reservation.getStatus());
            ps.setBigDecimal(6, reservation.getPenalty());
            ps.setBigDecimal(7, reservation.getCost());
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    @Override
    public Optional<Reservation> getReservationById(int reservationId) {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, reservationId).stream().findFirst();
    }

    @Override
    public List<Reservation> getReservationsByUserId(int userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, userId);
    }

    @Override
    public List<Reservation> getReservationsBySpotId(int spotId) {
        String sql = "SELECT * FROM reservations WHERE spot_id = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, spotId);
    }

    @Override
    @Transactional
    public boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE reservations SET start_time = ?, end_time = ?, status = ?, penalty = ?, cost = ? WHERE reservation_id = ?";
        return jdbcTemplate.update(sql,
                java.sql.Timestamp.valueOf(reservation.getStartTime()),
                java.sql.Timestamp.valueOf(reservation.getEndTime()),
                reservation.getStatus(),
                reservation.getPenalty(),
                reservation.getCost(),
                reservation.getReservationId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteReservation(int reservationId) {
        // Retrieve the reservation to get the spot ID
        Optional<Reservation> reservationOpt = getReservationById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new IllegalStateException("Reservation not found.");
        }

        // Free up the parking spot
        String updateSpotSql = "UPDATE parking_spots SET status = 'AVAILABLE' WHERE spot_id = ?";
        jdbcTemplate.update(updateSpotSql, reservationOpt.get().getSpotId());

        // Delete the reservation
        String deleteReservationSql = "DELETE FROM reservations WHERE reservation_id = ?";
        return jdbcTemplate.update(deleteReservationSql, reservationId) > 0;
    }

    @Override
    @Transactional
    public boolean isSpotAvailableForDuration(int spotId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE spot_id = ? AND " +
                "(start_time < ? AND end_time > ?) AND " +
                "status  = 'ACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, spotId, endTime, startTime);
        return count == 0;
    }


    @Override
    public List<Reservation> getExpiredReservations(LocalDateTime currentTime) {
        String sql = """
        SELECT * FROM reservations
        WHERE end_time < ? AND status = 'ACTIVE'
    """;
        return jdbcTemplate.query(sql, reservationRowMapper, currentTime);
    }
    @Override
    public List<Reservation> getReservationsByStatus(String status) {
        String sql = """
        SELECT * FROM reservations
        WHERE status = ?
    """;
        return jdbcTemplate.query(sql, reservationRowMapper, status);
    }

    @Override
    public List<Reservation> getReservationsBySpotStatus(String status) {
        String sql = """
        SELECT * FROM reservations
        WHERE spot_id IN (SELECT spot_id FROM parking_spots WHERE status = ?)
    """;
        return jdbcTemplate.query(sql, reservationRowMapper, status);
    }

    @Override
    public List<Reservation> getStartedReservations(LocalDateTime currentTime) {
        String sql = """
        SELECT * FROM reservations
        WHERE start_time < ? AND status = 'ACTIVE'
    """;
        return jdbcTemplate.query(sql, reservationRowMapper, currentTime);
    }
}
