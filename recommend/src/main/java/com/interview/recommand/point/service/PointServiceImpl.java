package com.interview.recommand.point.service;

import com.interview.recommand.point.dao.PointDOMapper;
import com.interview.recommand.point.dataobject.PointLogDO;
import com.interview.recommand.point.model.PointLogModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PointServiceImpl {

    /**
     * @Autowired
     */
    private PointDOMapper pointDOMapper;

    /**
     * Insert point record into the database
     */
    public void earnPoints() {
    }

    /**
     * User story 2 - 购物消费积分
     * Two sql statements need to be placed in the same transaction(@Transactional).
     * (1) Insert a record of consume points
     * (2) Reset the status of point records that have been consumed
     *
     * @param pointLogModelList
     * @param channel
     * @param channelId
     */
    public void consumePoints(List<PointLogModel> pointLogModelList, Integer channel, String channelId) {
        String userId = pointLogModelList.stream().findFirst().map(PointLogModel::getUserId).get();
        double count = pointLogModelList.stream().mapToDouble(PointLogModel::getCount).sum();
        PointLogDO pointLogDO = new PointLogDO();
        pointLogDO.setUserId(userId);
        pointLogDO.setCount(count);
        pointLogDO.setChannel(channel);
        pointLogDO.setType(1);
        pointLogDO.setChannelId(channelId);
        pointLogDO.setModified(new Date());

        List<Integer> ids = pointLogModelList.stream().map(PointLogModel::getId).collect(Collectors.toList());

        pointDOMapper.batchUpdateToUsedByPrimaryKey(ids);
        pointDOMapper.insert(pointLogDO);
    }

    /**
     * User story 3 - 积分总额
     *
     * @param userId
     * @return
     */
    public Double getTotalPoints(String userId) {
        List<PointLogModel> pointLogDOList = getPointRecords(userId);
        return pointLogDOList.stream().mapToDouble(PointLogModel::getCount).sum();
    }

    /**
     * User story 3 - 积分有效期
     *
     * @param userId
     * @return
     */
    public List<PointLogModel> getPointRecords(String userId) {
        List<PointLogDO> pointLogDOList = pointDOMapper
                .selectByUserIdAndTypeAndUsedAndGreaterThanDeadline(userId, 0, 0, new Date());
        List<PointLogModel> result = new ArrayList<>(pointLogDOList.size());
        pointLogDOList.stream().forEach(pointLogDO -> {
            PointLogModel model = new PointLogModel();
            // tracking point records
            model.setId(pointLogDO.getId());
            model.setUserId(pointLogDO.getUserId());
            // number of points
            model.setCount(pointLogDO.getCount());
            // validity of point
            model.setValidity(pointLogDO.getValidity());
            // deadline of point
            model.setDeadline(pointLogDO.getDeadline());
        });
        return result;
    }

    /**
     * User story 3 - 积分消费记录
     *
     * @param userId
     * @return
     */
    public List<PointLogModel> getPointConsumeRecords(String userId) {
        List<PointLogDO> pointLogDOList = pointDOMapper
                .selectByUserIdAndTypeAndUsedAndGreaterThanDeadline(userId, 1, null, null);
        List<PointLogModel> result = new ArrayList<>(pointLogDOList.size());
        pointLogDOList.stream().forEach(pointLogDO -> {
            PointLogModel model = new PointLogModel();
            // tracking point records
            model.setId(pointLogDO.getId());
            model.setUserId(pointLogDO.getUserId());
            // count to spent points
            model.setCount(pointLogDO.getCount());
            // time to spent points
            model.setModified(pointLogDO.getModified());
            result.add(model);
        });
        return result;
    }

}
