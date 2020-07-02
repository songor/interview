package com.interview.recommand.point.service;

import com.interview.recommand.point.strategy.PointContext;

public class MockSignServiceImpl {

    public Double getPoint(String userId) {
        int count = getSignDayOfMonth(userId);
        return new PointContext("Sign").calculatePoint((double) count);
    }

    private Integer getSignDayOfMonth(String userId) {
        return 31;
    }

}
