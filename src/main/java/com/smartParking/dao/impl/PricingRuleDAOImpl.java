package com.smartParking.dao.impl;

import com.smartParking.dao.PricingRuleDAO;
import com.smartParking.model.PricingRule;
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
public class PricingRuleDAOImpl implements PricingRuleDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for PricingRule
    private final RowMapper<PricingRule> pricingRuleRowMapper = new RowMapper<>() {
        @Override
        public PricingRule mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PricingRule(
                    rs.getInt("rule_id"),
                    rs.getInt("lot_id"),
                    rs.getTime("start_time").toLocalTime(),
                    rs.getTime("end_time").toLocalTime(),
                    rs.getBigDecimal("price_per_hour")
            );
        }
    };

    @Override
    public int createPricingRule(PricingRule pricingRule) {
        String sql = "INSERT INTO pricing_rules (lot_id, start_time, end_time, price_per_hour) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pricingRule.getLotId());
            ps.setTime(2, java.sql.Time.valueOf(pricingRule.getStartTime()));
            ps.setTime(3, java.sql.Time.valueOf(pricingRule.getEndTime()));
            ps.setBigDecimal(4, pricingRule.getPricePerHour());
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    @Override
    public Optional<PricingRule> getPricingRuleById(int ruleId) {
        String sql = "SELECT * FROM pricing_rules WHERE rule_id = ?";
        return jdbcTemplate.query(sql, pricingRuleRowMapper, ruleId).stream().findFirst();
    }

    @Override
    public List<PricingRule> getPricingRulesByLotId(int lotId) {
        String sql = "SELECT * FROM pricing_rules WHERE lot_id = ?";
        return jdbcTemplate.query(sql, pricingRuleRowMapper, lotId);
    }

    @Override
    public boolean updatePricingRule(PricingRule pricingRule) {
        String sql = "UPDATE pricing_rules SET start_time = ?, end_time = ?, price_per_hour = ? WHERE rule_id = ?";
        return jdbcTemplate.update(sql,
                java.sql.Time.valueOf(pricingRule.getStartTime()),
                java.sql.Time.valueOf(pricingRule.getEndTime()),
                pricingRule.getPricePerHour(),
                pricingRule.getRuleId()) > 0;
    }

    @Override
    public boolean deletePricingRule(int ruleId) {
        String sql = "DELETE FROM pricing_rules WHERE rule_id = ?";
        return jdbcTemplate.update(sql, ruleId) > 0;
    }
}
