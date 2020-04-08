# Log4j

## Gradle dependencies

```groovy
dependencies {
    // for JSON and YAML configs
    compile group: 'com.fasterxml.jackson.core', name: 'jackson-core', version: '2.10.3'
    compile group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.10.3'

    compile group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.13.1'
    compile group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.13.1'
}
```

## Appender

- An output destination is called an Appender. 
- They exist for console, files, remote socket servers, Apache Flume, JMS, remote UNIX Syslog daemons, and various database APIs.
- More than one appender can be attached to a logger.
- An appender can be added to a Logger by calling the `addLoggerAppender` method of the current Configuration.
- _Each enabled logging request for a given logger will be forwarded to all the appenders in that Logger's LoggerConfig as well as the Appenders of the LoggerConfig's parents.

## Layout

- Customize the output format by associatng a Layout with an Appender.
- The Layout is responsible for formatting the LogEvent according to the user's wishes, whereas an appender takes care of sending the formatted output to its destination. The `PatternLayout`, part of the standard log4j distribution, lets the user specify the output format according to conversion patterns similar to the C language printf function.

    `"%r [%t] %-5p %c - %m%n"`

    outputs

    `176 [main] INFO  org.foo.Bar - Located nearest gas station.`

- The first field is the number of miliseconds elapsed since the start of the program. The second field is the thread making the log request. The third field is the level. The fourth field is the name of the logger associated with the log request. The text after the `-` is the message.

- Log4j comes with different Layouts for various use cases such as JSON, XML, HTML, and Syslog. Other appenders such as the database connectors fill in specified fields instead of a particular textual layout.

# Log4j 2 API

The Log4j 2 API provides the interface that applications should code to.

### Hello World
```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloWorld {
    private static final Logger logger = LogManager.getLogger("HelloWorld");
    public static void main(String[] args) {
        logger.info("Hello, World!");
    }
}
```

- First a Logger with the name `HelloWorld` is obtained from the LogManager. Next the logger is used to write the message. The message will only be written if configured to allow informational messages.

```
logger.debug("His name is {} {}.", user.getFirst(), user.getLast());
```

### Formatting Parameters

- To use a formatter logger, you must call one of the LogManager `getFormatterLogger` methods. 
- Then `%s %,d` can be used in the format string which is the first arg of the log methods.
- Alternatively, you can all the `logger.printf(Level.INFO, "formatstr %s", obj.toString())` method. 

### JSON config

```json
{
  "configuration": {
    "status": "error",
    "name": "JSONConfigDemo",
    "appenders": {
      "Console": {
        "name": "console",
          "target": "SYSTEM_OUT",
        "PatternLayout": {
          "pattern": "%d [%t] %-10c %-5level: %msg%n%throwable"
        }
      },
      "File": {
        "name": "file",
        "fileName": "logs/json.log",
        "append": "false",
        "PatternLayout": {
          "pattern": "%d [%t] %-10c %-5level: %msg%n%throwable"
        }
      }
    },
    "loggers": {
      "logger": {
        "name": "myapp",
        "level": "trace",
        "AppenderRef": {
          "ref": "file"
        }
      },
      "root": {
        "level": "trace",
        "AppenderRef": {
          "ref": "console"
        }
      }
    }
  }
}
```

### Properties config with rolling files in hidden user folder and console logger
```properties
status = error
dest = err
name = PropertiesConfig

property.home = ${sys:user.home}

appender.console.type = Console
appender.console.name = stdout
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = %d [%t] %-10c %-5level: %msg%n%throwable

appender.rolling.type = RollingFile
appender.rolling.name = rollingfile
appender.rolling.fileName = ${home}/.myapp/applog.log
appender.rolling.filePattern = ${home}/.myapp/archived-%d{MM-dd-yy-HH-mm-ss}-%i.zip
appender.rolling.layout.type = PatternLayout
appender.rolling.layout.pattern = %d [%t] %-10c %-5level: %msg%n%throwable
appender.rolling.policies.type = Policies
appender.rolling.policies.size.type = SizeBasedTriggeringPolicy
appender.rolling.policies.size.size = 5MB
appender.rolling.strategy.type = DefaultRolloverStrategy
appender.rolling.strategy.max = 5

logger.rolling.name = myapp
logger.rolling.level = trace
logger.rolling.appenderRef.rolling.ref = rollingfile

rootLogger.level = trace
rootLogger.appenderRef.stdout.ref = stdout
```