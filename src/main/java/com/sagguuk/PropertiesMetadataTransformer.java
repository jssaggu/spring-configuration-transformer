package com.sagguuk;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.boot.configurationprocessor.metadata.ConfigurationMetadata;
import org.springframework.boot.configurationprocessor.metadata.ItemMetadata;
import org.springframework.boot.configurationprocessor.metadata.JsonMarshaller;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class converts json file to an output file based on the specified template.
 */
@Mojo(name = "properties-metadata-transformer", defaultPhase = LifecyclePhase.COMPILE)
public class PropertiesMetadataTransformer extends AbstractMojo {

    @Parameter(property = "output-template-file-name")
    private String outputTemplateFileName = "metadata.ftl";

    @Parameter(property = "output-file-name")
    private String outputFileName;

    @Parameter(property = "report-title")
    private String reportTitle = "Spring Properties Report";

    @Parameter(property = "metadata-json-file-name")
    private String metadataJsonFileName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            System.out.println("MetadataJsonFileName: " + metadataJsonFileName);
            buildReportFile(new JsonMarshaller().read(new FileSystemResource(metadataJsonFileName).getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoFailureException(e);
        }
    }

    private void buildReportFile(ConfigurationMetadata metadata) throws IOException, TemplateException {
        Configuration cfg = new Configuration(new Version(2, 3, 27));

        // template is loaded from 'templates' folder under resources
        cfg.setClassForTemplateLoading(PropertiesMetadataTransformer.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.getDefault());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Map<String, Object> input = new HashMap<String, Object>();
        input.put("title", reportTitle);
        input.put("metadata", metadata);
        input.put("PROPERTY", ItemMetadata.ItemType.PROPERTY);

        Template template = cfg.getTemplate(outputTemplateFileName);

        // write output to the console
//        Writer consoleWriter = new OutputStreamWriter(System.out);
//        template.process(input, consoleWriter);

        // write output into a file
        System.out.println("Report file Name: " + outputTemplateFileName);
        Writer fileWriter = new FileWriter(new File(outputFileName));
        try {
            template.process(input, fileWriter);
        } finally {
            fileWriter.close();
        }
    }
}