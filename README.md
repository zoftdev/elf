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

V1.4
- Split activity to separate kafka topic
- Some Fix bug
- Split activity to separate kafka topic 
 - config: http://git.true.th/MSE-DevX/elk-logging-framwork/blob/v14/elf-example/src/main/resources/logback-spring.xml
- Some Fix bug

V1.4.8
- Support ELF_HOSTNAME environment parameter 

example project:
- add logstash tcp

 


## How to use:
- see [ExampleTransaction](http://git.true.th/MSE-DevX/elk-logging-framwork/blob/4850ba5c74d0c7d33dc108d02db538aef990985d/elf-example/src/main/java/com/rmv/mse/microengine/exampleproject/ExampleTransaction.java)
- and [ExampleActivity](http://git.true.th/MSE-DevX/elk-logging-framwork/blob/4850ba5c74d0c7d33dc108d02db538aef990985d/elf-example/src/main/java/com/rmv/mse/microengine/exampleproject/ExampleService.java)

### todo:
- support @LogParam

###help
-  throw elfException (1.4.1) [howto](http://git.true.th/MSE-DevX/elk-logging-framwork/commit/ebc8036529dc36895dc93e91d87d67530e5346a0#diff-1)
-  feature NoLog ถ้า return class ที่implement NoLogInterface จะไม่ทำการ log ลง Elasticsearch [example](http://git.true.th/MSE-DevX/elk-logging-framwork/commit/74731ccf906928a6c566e0e1e161c7f1a19bfa4e#diff-0)
