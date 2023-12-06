package com.luckyi.statemachine.statePattern;

import com.luckyi.statemachine.statedesignpattern.Context;
import com.luckyi.statemachine.statedesignpattern.TurnOffAction;
import com.luckyi.statemachine.statedesignpattern.TurnOnAction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 状态模式测试用例
 *
 * @author luckyi
 */
public class StatePatternDemo {

    @DisplayName("状态模式测试用例-开灯")
    @Test
    public void turnOn() {
        Context context = new Context(new TurnOnAction());
        context.doAction();
    }

    @DisplayName("状态模式测试用例-关灯")
    @Test
    public void turnOff() {
        Context context = new Context(new TurnOffAction());
        context.doAction();
    }
}
