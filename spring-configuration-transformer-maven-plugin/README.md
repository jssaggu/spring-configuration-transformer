# Spring Configuration Transformer Maven Plugin

This simple plugin can be used to transform Spring Configurations to a user-friendly html/markdown file.

## How to use?

### Prerequisite
Please ensure your Spring (SpringBoot ) application is able to generate configuration metadata json file. 
See [Spring Configuration Metadata](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html) page for more information. 

### Plugin Configuration

Add the following plugin to your project's `pom.xml` file. The plugin will be executed after code has been compiled and metadata file has been generated.

```yaml
<plugins>
    <plugin>
        <groupId>com.sagguuk</groupId>
        <artifactId>spring-configuration-transformer-maven-plugin</artifactId>
        <!-- Update below to latest available version. For example: 0.0.1-SNAPSHOT-->
        <version>some-latest-version</version>
        
        <executions>
            <execution>
                <goals>
                    <goal>spring-configuration-transformer</goal>
                </goals>
            </execution>
        </executions>
        
        <configuration>
            <metadataJsonFileName>${project.build.directory}/classes/META-INF/spring-configuration-metadata.json</metadataJsonFileName>
            <outputFileName>docs/my-app-properties.html</outputFileName>
            <reportTitle>Title of your report</reportTitle>
        </configuration>
    
    </plugin>
</plugins>
```

### Configuration
* **metadataJsonFileName**
The plugin comes with default template to generate html outputs. But you can customise and provide the location of your template file in this variable.
* **outputFileName** Name of the output file name. You can either provide absolute path or file will be created in the root folder. Feel free to use maven variables to build the path.
* **reportTile** Title for the generated report
