package org.pensatocode.simplicity.generator.writers.starter;

import lombok.extern.log4j.Log4j2;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.components.ProjectConfig;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.util.ComponentBinder;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.VelocityUtil;
import org.pensatocode.simplicity.generator.writers.FileSourceWriter;

import java.io.File;

@Log4j2
public class BuildGradleWriter implements FileSourceWriter {

    private final VelocityEngine velocityEngine;
    private final DirectoryService dirService;
    private final ProjectConfig projectConfigProps;
    private final String vmFileName;

    public BuildGradleWriter(VelocityEngine velocityEngine, DirectoryService dirService) {
        this.velocityEngine = velocityEngine;
        this.dirService = dirService;
        this.projectConfigProps = ComponentBinder.getProjectConfig();
        this.vmFileName = "starter/build-gradle.vm";
    }

    public boolean generateFileSource() {
        // create build.gradle.kts path
        String fileAbsolutePath = dirService.getProjectDir().getAbsolutePath()
                + File.separator
                + GeneratorUtil.BUILD_GRADLE_FILE_NAME;
        // create the template
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName", projectConfigProps.getPackageGroup());
        Template template = velocityEngine.getTemplate(vmFileName);
        // write the file
        return VelocityUtil.writeFile(fileAbsolutePath, template, velocityContext);
    }

    @Override
    public String getVmFileName() {
        return vmFileName;
    }
}
