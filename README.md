## Java状态机多种实现实例
> 状态机(State Machine) 是现实事物运行规则抽象而成的一个数学模型，它描述了状态序列之间流转的关系及条件。在复杂多状态流转情况下，使用状态机来表达状态的流转，会使语义会更加清晰，会增强代码的可读性和可维护性。
一个状态机可以具有有限个特定的状态，它通常根据输入，从一个状态转移到另一个状态，不过也可能存在瞬时状态，而一旦任务完成，状态机就会立刻离开瞬时状态。每个状态根据不同的前置条件，会从当前状态流转至下一个状态。

#### 状态机要素：

- 现态：是指当前所处的状态。
- 条件：又称为“事件”，当一个条件被满足，将会触发一个动作，或者执行一次状态的迁移。
- 动作：条件满足后执行的动作。动作执行完毕后，可以迁移到新的状态，也可以仍旧保持原状态。动作不是必需的，当条件满足后，也可以不执行任何动作，直接迁移到新状态。
- 次态：条件满足后要迁往的新状态。“次态”是相对于“现态”而言的，“次态”一旦被激活，就转变成新的“现态”了。

### 示例

- [Enum枚举实现状态机](src/main/java/com/luckyi/statemachine/enumstate)
- [状态模式实现状态机](src/main/java/com/luckyi/statemachine/statedesignpattern)
- [spring-statemachine状态机](src/main/java/com/luckyi/statemachine/springstatemachine)
- [cola-statemachine状态机](src/main/java/com/luckyi/statemachine/colastatemachine)

### 参考文章

- [实现一个状态机引擎cola-statemachine](https://blog.csdn.net/significantfrank/article/details/104996419)
- [spring-statemachine 状态机自定义持久化入库](https://blog.csdn.net/sjy_2010/article/details/133862831)
- [基于reactorAPI的spring-statemachine的状态机实战](https://juejin.cn/post/7240006787947331642)
- [状态机的技术选型](https://blog.csdn.net/zhangfenshi123/article/details/127852110)
