<p align="center">
  <img width="200" src="https://avatars.githubusercontent.com/u/110696897?s=400&u=0dad9954062c5d9482b8ee47b2540b824688bd40&v=4" alt="no"/>
</p>
<h3 align="center">
   Goulash Engine
</h3>
<p align="center">
  Behavior State Machine wrapped into a Simulation as a Service
</p>

## Script Syntax

##### Syntax Structure

- [context]: The target of the statement
- [::] mutation operator
- [type]: the mutation type
- [target]: the target of the mutation
- [operation]: how the target should be mutated
- [argument]: the argument value for the operation

```
logic <my_logic_name> {
    <context>::<type>(<target>).<operation>(<argument>); 
}
```

###### Simple Examples

```
logic mylogic {
    // increase all actors urge to eat by 5 per tick
    actors::urge(eat).plus(5); 
}
```

```
logic mylogic {
    // decrease all actors urge to eat by 5 per tick
    actors::urge(eat).minus(5); 
}
```

```
logic mylogic {
    // set all actors urge to eat to 50 per tick
    actors::urge(eat).set(50); 
}
```


