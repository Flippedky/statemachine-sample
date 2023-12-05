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
     * 审批内容
     */
    private String approvalContent;

    public LeaveContext() {
    }

    public LeaveContext(Integer idea, String approvalContent) {
        this.idea = idea;
        this.approvalContent = approvalContent;
    }

    public Integer getIdea() {
        return idea;
    }

    public void setIdea(Integer idea) {
        this.idea = idea;
    }

    public String getApprovalContent() {
        return approvalContent;
    }

    public void setApprovalContent(String approvalContent) {
        this.approvalContent = approvalContent;
    }
}
