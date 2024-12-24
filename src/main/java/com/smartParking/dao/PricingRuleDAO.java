package com.smartParking.dao;

import com.smartParking.model.PricingRule;
import java.util.List;
import java.util.Optional;

public interface PricingRuleDAO {
    int createPricingRule(PricingRule pricingRule); // Returns the generated ID
    Optional<PricingRule> getPricingRuleById(int ruleId);
    List<PricingRule> getPricingRulesByLotId(int lotId);
    boolean updatePricingRule(PricingRule pricingRule);
    boolean deletePricingRule(int ruleId);
}
