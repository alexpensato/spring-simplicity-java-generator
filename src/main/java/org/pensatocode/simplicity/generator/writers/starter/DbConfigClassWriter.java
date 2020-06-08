package org.pensatocode.simplicity.generator.writers.starter;

import lombok.extern.log4j.Log4j2;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.components.Starter;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.VelocityUtil;
import org.pensatocode.simplicity.generator.writers.FileSourceWriter;

import java.io.File;

@Log4j2
public class DbConfigClassWriter implements FileSourceWriter {

    private final VelocityEngine velocityEngine;
    private final DirectoryService dirService;
    private final Starter starterProps;
    private final String vmFileName;

    public DbConfigClassWriter(VelocityEngine velocityEngine, DirectoryService dirService, Starter starterProps) {
        this.velocityEngine = velocityEngine;
        this.dirService = dirService;
        this.starterProps = starterProps;
        this.vmFileName = "starter/db-config-class.vm";
    }

    public boolean generateFileSource() {
        // create config dir
        String dirAbsolutePath = dirService.getRootPackageDir().getAbsolutePath()
                + File.separator
                + GeneratorUtil.DB_CONFIG_DIR_NAME;
        if (! dirService.createDir(dirAbsolutePath) ){
            return false;
        }
        // create file path
        String fileAbsolutePath = dirService.getRootPackageDir().getAbsolutePath()
                + File.separator
                + GeneratorUtil.DB_CONFIG_DIR_NAME
                + File.separator
                + GeneratorUtil.DB_CONFIG_CLASS_NAME;
        // create the template
        VelocityContext velocityContext = new VelocityContext();
        String dbConfigPackage = starterProps.getPackageGroup() + GeneratorUtil.DOT + GeneratorUtil.DB_CONFIG_DIR_NAME;
        velocityContext.put("packageName", dbConfigPackage);
        Template template = velocityEngine.getTemplate(vmFileName);
        // write the file
        return VelocityUtil.writeFile(fileAbsolutePath, template, velocityContext);
    }

    @Override
    public String getVmFileName() {
        return vmFileName;
    }
}
