package com.interview.recommand.point.strategy;

public class CommentPointStrategy implements PointStrategy {

    @Override
    public Double calculatePoint(Double count) {
        return count * 5;
    }

}
