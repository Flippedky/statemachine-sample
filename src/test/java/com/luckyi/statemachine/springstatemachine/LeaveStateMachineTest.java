package com.luckyi.statemachine.springstatemachine;

import com.luckyi.statemachine.domain.Event;
import com.luckyi.statemachine.domain.LeaveContext;
import com.luckyi.statemachine.domain.LeaveStatusEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.state.State;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.luckyi.statemachine.domain.Event.*;
import static com.luckyi.statemachine.domain.LeaveStatusEnum.*;
import static com.luckyi.statemachine.domain.StateMachineConstant.STATE_MACHINE_CONTEXT;

@SpringBootTest
public class LeaveStateMachineTest {

    @Resource
    private StateMachine<LeaveStatusEnum, Event> leaveStateMachine;

    @Test
    public void employSubmitRequest(){
        leaveStateMachine.startReactively();
        LeaveContext leaveContext = new LeaveContext(0, "");
        Message<Event> message = MessageBuilder.withPayload(DIRECT_LEADER_AUDIT).setHeader(STATE_MACHINE_CONTEXT,leaveContext).build();
        Flux<StateMachineEventResult<LeaveStatusEnum, Event>> resultFlux = leaveStateMachine.sendEvent(Mono.just(message));
        resultFlux.subscribe(result -> {
            State<LeaveStatusEnum, Event> state = result.getRegion().getState();
            LeaveStatusEnum status = state.getId();
            Assertions.assertEquals(LEAVE_SUBMIT,status);
        });
    }

}
