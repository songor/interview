package com.interview.recommand.point.strategy;

public class OrderAmountPointStrategy implements PointStrategy {

    @Override
    public Double calculatePoint(Double count) {
        if (count < 500) {
            return count * 0.1;
        }
        if (count >= 500 && count < 1000) {
            return count * 0.15;
        }
        if (count >= 1000) {
            return count * 0.20;
        }
        throw new UnsupportedOperationException();
    }

}
