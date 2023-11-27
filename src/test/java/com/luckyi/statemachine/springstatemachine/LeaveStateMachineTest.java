package com.luckyi.statemachine.springstatemachine;

import com.luckyi.statemachine.colaStateMachine.Event;
import com.luckyi.statemachine.colaStateMachine.LeaveStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import static com.luckyi.statemachine.colaStateMachine.Event.*;
import static com.luckyi.statemachine.domain.StateMachineConstant.STATE_MACHINE_CONTEXT;

@SpringBootTest(classes = SpringApplication.class)
public class LeaveStateMachineTest {

    @Autowired
    private StateMachine<LeaveStatusEnum, Event> leaveStateMachine;

    @Test
    public void employSubmitRequest(){
        leaveStateMachine.startReactively();
        Message<Event> message = MessageBuilder.withPayload(EMPLOYEE_SUBMIT).setHeader(STATE_MACHINE_CONTEXT,"").build();
    }

}
