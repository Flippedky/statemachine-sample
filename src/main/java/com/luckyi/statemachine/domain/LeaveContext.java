package com.luckyi.statemachine.domain;

/**
 * 请假流程自定义上下文
 *
 * @author luckyi
 */
public class LeaveContext {

    /**
     * 操作意见<br>
     * 0-同意 1-拒绝
     */
    private Integer idea;

    /**
     * 拒绝原因
     */
    private String refuseReason;

    public LeaveContext() {
    }

    public LeaveContext(Integer idea, String refuseReason) {
        this.idea = idea;
        this.refuseReason = refuseReason;
    }

    public Integer getIdea() {
        return idea;
    }

    public void setIdea(Integer idea) {
        this.idea = idea;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }
}
