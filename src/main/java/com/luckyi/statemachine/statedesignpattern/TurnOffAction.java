package com.luckyi.statemachine.statedesignpattern;

/**
 * 关灯实现类
 * @author luckyi
 */
public class TurnOffAction implements SwitchState{
    @Override
    public void handle() {
        System.out.println("关灯");
    }
}
