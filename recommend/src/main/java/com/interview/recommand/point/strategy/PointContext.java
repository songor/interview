package com.interview.recommand.point.strategy;

public class PointContext {

    private PointStrategy strategy;

    public PointContext(String type) {
        switch (type) {
            case "Comment":
                this.strategy = new CommentPointStrategy();
                break;
            case "Sign":
                this.strategy = new SignPointStrategy();
                break;
            case "OrderAmount":
                this.strategy = new OrderAmountPointStrategy();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Double calculatePoint(Double count) {
        return strategy.calculatePoint(count);
    }

}
