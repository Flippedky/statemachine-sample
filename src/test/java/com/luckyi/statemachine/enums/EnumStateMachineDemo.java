package com.luckyi.statemachine.enums;

import com.luckyi.statemachine.enumstate.LeaveRequestStateEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 枚举状态机<br>
 *
 * @author luckyi
 */
public class EnumStateMachineDemo {

    //一开始是员工提交审批
    LeaveRequestStateEnum state = LeaveRequestStateEnum.Submitted;

    @Test
    public void execute() {

        //员工提交审批完，状态发生流转，流转到下一个状态，变成Team Leader
        state = state.nextState();
        assertEquals(LeaveRequestStateEnum.Escalated, state);

        //Team Leader审批完成，完成状态流转，流转到部门经理
        state = state.nextState();
        assertEquals(LeaveRequestStateEnum.Approved, state);
        //部门经理是最终审批环节，下一个状态还是他自己
        state = state.nextState();
        assertEquals(LeaveRequestStateEnum.Approved, state);
    }
}
