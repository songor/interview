package com.interview.recommand.point.service;

import com.interview.recommand.point.strategy.PointContext;

public class MockCommentServiceImpl {

    public Double getPoint(String userId) {
        int count = getTotalCommentByUserId(userId);
        return new PointContext("Comment").calculatePoint((double) count);
    }

    private Integer getTotalCommentByUserId(String userId) {
        return 10;
    }

}
