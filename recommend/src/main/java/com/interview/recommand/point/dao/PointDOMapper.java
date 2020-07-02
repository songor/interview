package com.interview.recommand.point.dao;

import com.interview.recommand.point.dataobject.PointLogDO;

import java.util.Date;
import java.util.List;

public interface PointDOMapper {

    /**
     * Get point records by specified user, specified point type, and greater than specified date
     *
     * @param userId
     * @param type
     * @param deadline
     * @return
     */
    List<PointLogDO> selectByUserIdAndTypeAndUsedAndGreaterThanDeadline(String userId, Integer type, Integer used, Date deadline);

    /**
     * Batch update point records to used status.
     *
     * @param list
     */
    void batchUpdateToUsedByPrimaryKey(List<Integer> list);

    /**
     * Insert point record
     *
     * @param record
     * @return
     */
    int insert(PointLogDO record);

}
