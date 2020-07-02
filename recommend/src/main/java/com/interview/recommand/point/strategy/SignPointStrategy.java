package com.interview.recommand.point.strategy;

import java.util.Calendar;
import java.util.Date;

public class SignPointStrategy implements PointStrategy {

    @Override
    public Double calculatePoint(Double count) {
        int dayOfMonth = getDayOfMonth();
        if (dayOfMonth == count) {
            return count * 20;
        } else {
            return count * 10;
        }
    }

    private int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
