package myapp;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class ConfigureLogger {
    public static void configure() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        LayoutComponentBuilder standard = builder.newLayout("PatternLayout");
        standard.addAttribute("pattern", "%d [%t] %-10c %-5level: %msg%n%throwable");

        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        console.add(standard);
        builder.add(console);  // add console appender

        AppenderComponentBuilder file = builder.newAppender("file", "File");
        file.addAttribute("fileName", "logs/logging.log");
        file.addAttribute("append", "false");
        file.add(standard);
        builder.add(file);  // add file appender
        
        builder.add(builder.newRootLogger(Level.TRACE)
            .add(builder.newAppenderRef("stdout"))
            .add(builder.newAppenderRef("file")));
        
        
        Configurator.initialize(builder.build());
    }

}