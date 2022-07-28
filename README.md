<p align="center">
  <img width="200" src="https://i.imgur.com/T5gDld9.jpg" alt="no"/>
</p>
<p align="center">
   prosper-engine
</p>
<p align="center">
  A framework to build a simple civilisation simulation engine
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


