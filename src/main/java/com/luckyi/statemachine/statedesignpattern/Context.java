package com.luckyi.statemachine.statedesignpattern;

/**
 * @ClassName Context
 * @Description TODO
 * @author luckyi
 */
public class Context {

    private SwitchState state;

    public Context(SwitchState state){
        this.state=state;
    }

    public void doAction(){
        state.handle();
    }
}
