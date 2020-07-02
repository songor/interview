package com.interview.recommand.point.dataobject;

import java.util.Date;

public class PointLogDO {

    /**
     * primary id
     */
    private Integer id;

    /**
     * user id
     */
    private String userId;

    /**
     * the number of points
     */
    private Double count;

    /**
     * points validity, unit is month
     */
    private Integer validity;

    /**
     * record channel that obtain or consume points
     * 0 - earn points for shopping
     * 1 - comment to get points
     * 2 - sign to earn points
     */
    private Integer channel;

    /**
     * 0 - obtain
     * 1 - consume
     */
    private Integer type;

    /**
     * channel primary id
     */
    private String channelId;

    /**
     * point deadline
     */
    private Date deadline;

    /**
     * last modified date
     */
    private Date modified;

    /**
     * has this point been used
     * 0 - unused
     * 1 - used
     */
    private Integer used;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }
}
