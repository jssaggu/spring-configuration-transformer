package com.sagguuk;


import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * This class converts json file to an output file based on the specified template.
 *
 * @author jasvinder.saggu
 */
@Mojo(name = "spring-configuration-transformer", defaultPhase = LifecyclePhase.COMPILE)
public class SpringConfigurationTransformerMojo extends AbstractMojo {

    private static String DEFAULT_OUTPUT_TEMPLATE_FILE = "spring-configuration-transformer-html.ftl";

    @Parameter(property = "output-template-file-name")
    private String outputTemplateFileName;

    @Parameter(property = "output-file-name")
    private String outputFileName;

    @Parameter(property = "report-title")
    private String reportTitle = "Spring Configuration Report";

    @Parameter(property = "metadata-json-file-name")
    private String metadataJsonFileName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            getLog().info("Metadata Json File Name: " + metadataJsonFileName);
            getLog().info("Output Report Name: " + outputFileName);

            buildReportFile(new JsonMarshaller().read(new FileSystemResource(metadataJsonFileName).getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoFailureException(e);
        }
    }

    private void buildReportFile(ConfigurationMetadata metadata) throws IOException, TemplateException {
        Configuration configuration = new Configuration(new Version(2, 3, 31));

        if (isNull(outputTemplateFileName) || outputTemplateFileName.trim().length() < 1) {
            getLog().info("No Output Template Name specified. Using plugin provided template.");
            outputTemplateFileName = DEFAULT_OUTPUT_TEMPLATE_FILE;
            configuration.setClassForTemplateLoading(SpringConfigurationTransformerMojo.class, "/templates");
        } else {
            getLog().info("Output Template Name: " + outputTemplateFileName);
            final FileTemplateLoader externalTemplateLoader = new FileTemplateLoader(Paths.get("/templates").getParent().toFile());
            configuration.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{externalTemplateLoader}));
        }

        configuration.setDefaultEncoding("UTF-8");
        configuration.setLocale(Locale.getDefault());
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Map<String, Object> templateInput = new HashMap<String, Object>();
        templateInput.put("title", reportTitle);
        templateInput.put("metadata", metadata);
        templateInput.put("PROPERTY", ItemMetadata.ItemType.PROPERTY);

        Template template = configuration.getTemplate(outputTemplateFileName);

        // write output into a file
        Writer fileWriter = new FileWriter(new File(outputFileName));

        try {
            getLog().debug("Writing file..." + outputFileName);
            template.process(templateInput, fileWriter);
            getLog().debug("Writing file done..." + outputFileName);

            getLog().info("Spring Configuration Transformation Done");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileWriter.close();
        }
    }
}