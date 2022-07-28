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

##### Syntax Structure

###### Context Mutation

- [context]: on what the mutation should be executed onto
- [type]: the mutation type
- [target]: the target of the mutation
- [operation]: how the target should be mutated
- [argument]: the argument value for the specific operaton

```
logic <my_logic_name> {
    <context>::<type>(<target>).<operation>(<argument>); 
}
```


