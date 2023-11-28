package com.luckyi.statemachine.cola;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import com.luckyi.statemachine.SpringApplication;
import com.luckyi.statemachine.domain.LeaveStatusEnum;
import com.luckyi.statemachine.domain.Event;
import com.luckyi.statemachine.domain.LeaveContext;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author luckyi
 */
@SpringBootTest(classes = SpringApplication.class)
public class LeaveRequestTest {

    @Resource(name = "stateMachine")
    private StateMachine stateMachineInstance;


    @DisplayName("员工提交请假申请单")
    @Test
    public void employSubmitRequest(){

        StateMachine<LeaveStatusEnum, Event, LeaveContext> stateMachine = StateMachineFactory.get("leaveStateMachineId");
        LeaveContext context = new LeaveContext();

        LeaveStatusEnum state=stateMachine.fireEvent(LeaveStatusEnum.LEAVE_SUBMIT, Event.EMPLOYEE_SUBMIT,context);
        String plantUML = stateMachine.generatePlantUML();
        System.out.println(plantUML);
        // stateMachine.showStateMachine();
        Assertions.assertEquals(LeaveStatusEnum.LEAVE_SUBMIT.getCode(),state.getCode());

    }

    @DisplayName("部门主管审批通过")
    @Test
    public void leaderAuditPass(){

        StateMachine<LeaveStatusEnum, Event, LeaveContext> stateMachine = StateMachineFactory.get("leaveStateMachineId");
        LeaveContext context = new LeaveContext();
        //主管审批通过
        context.setIdea(0);
        LeaveStatusEnum state=stateMachine.fireEvent(LeaveStatusEnum.LEAVE_SUBMIT, Event.DIRECT_LEADER_AUDIT,context);
        Assertions.assertEquals(LeaveStatusEnum.LEADER_AUDIT_PASS.getCode(),state.getCode());
    }

    @DisplayName("部门主管审批不通过")
    @Test
    public void leaderAuditNotPass(){

        StateMachine<LeaveStatusEnum, Event, LeaveContext> stateMachine = StateMachineFactory.get("leaveStateMachineId");
        LeaveContext context = new LeaveContext();
        //主管审批不通过
        context.setIdea(1);
        LeaveStatusEnum state=stateMachine.fireEvent(LeaveStatusEnum.LEAVE_SUBMIT, Event.DIRECT_LEADER_AUDIT,context);
        Assertions.assertEquals(LeaveStatusEnum.LEADER_AUDIT_REFUSE.getCode(),state.getCode());
    }


    @DisplayName("HR审批通过")
    @Test
    public void hrAuditPass(){

        StateMachine<LeaveStatusEnum, Event, LeaveContext> stateMachine = StateMachineFactory.get("leaveStateMachineId");
        LeaveContext context = new LeaveContext();
        //HR通过
        context.setIdea(0);
        LeaveStatusEnum state=stateMachine.fireEvent(LeaveStatusEnum.LEADER_AUDIT_PASS, Event.HR_AUDIT,context);
        Assertions.assertEquals(LeaveStatusEnum.HR_PASS.getCode(),state.getCode());
    }

    @DisplayName("HR审批不通过")
    @Test
    public void hrAuditNotPass(){

        StateMachine<LeaveStatusEnum, Event, LeaveContext> stateMachine = StateMachineFactory.get("leaveStateMachineId");
        LeaveContext context = new LeaveContext();
        //HR审批不通过
        context.setIdea(1);
        LeaveStatusEnum state=stateMachine.fireEvent(LeaveStatusEnum.LEADER_AUDIT_PASS, Event.HR_AUDIT,context);
        Assertions.assertEquals(LeaveStatusEnum.HR_REFUSE.getCode(),state.getCode());
    }




}
