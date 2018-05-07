# ELF : ELK Logging Framework

Use @TransactionLog and @ActivityLog to capture log to ELK

## Example
```java
    @TransactionLog
    public TransactionResult example_hello_world() {
        //do something
        return TransactionResult.SUCCESS;
    }
```


## Change log
V1.0

- support message setting
- add method field
- add logstash tcp

V1.4
- Split activity to separate kafka topic 
 - config: http://git.true.th/MSE-DevX/elk-logging-framwork/blob/v14/elf-example/src/main/resources/logback-spring.xml
- Some Fix bug

 


## How to use:
- see [ExampleTransaction](http://git.true.th/MSE-DevX/elk-logging-framwork/blob/4850ba5c74d0c7d33dc108d02db538aef990985d/elf-example/src/main/java/com/rmv/mse/microengine/exampleproject/ExampleTransaction.java)
- and [ExampleActivity](http://git.true.th/MSE-DevX/elk-logging-framwork/blob/4850ba5c74d0c7d33dc108d02db538aef990985d/elf-example/src/main/java/com/rmv/mse/microengine/exampleproject/ExampleService.java)

### todo:
- support @LogParam