package nl.hsac.fitnesse.fixture.util;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.template.Configuration;

import java.io.*;
import java.util.Properties;

public class DynamicResourceHelper {
    final private String propertyloc = "/myproperties.txt";
    Configuration localConfig;

    public DynamicResourceHelper(){}; // Standard Constructor
    public DynamicResourceHelper(Configuration freemarkerConfig){
        localConfig = freemarkerConfig;
    }

    /**
     * used when init of DynamicResourceHelper with Configuration
     * @return config with DynamicPath
     */
    public Configuration invokeDynamicPathConfig(){
        applyDynamicResourcePath();
        return localConfig;
    }
    // used when defaultConstructor init of DynamicResourceHelper

    /**
     *
     * @param config
     * @return
     */
    public Configuration invokeDynamicPathwithConfig(Configuration config){
        localConfig = config;
        return invokeDynamicPathConfig();
    }

    /**
     * Check for Multi or Single case
     */
    void applyDynamicResourcePath(){
        // absolute Path only
        // loaded from my.properties File
        String path = getDynamicPathFromProperty();

        if(path.contains(",")){
            setMultiTemplateFolder(path);
        } else {
            setTemplateFolder(path);
        }
    }

    /**
     * Multi Sources Case
     * path from "applyDynamicResourcePath()" resolution
     * @param path
     */
    void setMultiTemplateFolder(String path){
        String paths[] = path.split(",");
        FileTemplateLoader loaders[] = new FileTemplateLoader[paths.length];
        for(int i = 0; i < paths.length; i++){
            try {
                loaders[i] = new FileTemplateLoader(new File(paths[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        localConfig.setTemplateLoader(mtl);
    }

    /**
     * Single Source Case
     * Absolute Path only
     * path from "applyDynamicResourcePath()" resolution
     * @param path
     */
    void setTemplateFolder(String path){
        // pom.xml Line 58 <freemarkertemplatepath>${freemarkertemplate.path}</freemarkertemplatepath>
        // fixme: "$" as marker for the default Location
        if( path.contains("$") ) {
            localConfig.setClassForTemplateLoading(getClass(), "/templates/");
        } else {
            // Sets the new path to the templates
            File baseDir = new File(path);
            try {
                localConfig.setDirectoryForTemplateLoading(baseDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String getDynamicPathFromProperty() {
        String resultString = "";
        InputStream is;
        java.util.Properties p = null;

        // properties-maven-plugin - Writes Properties in my.properties file in src/main/resources/
        // Maven Pfad ist: <outputFile>src/main/resources/myproperties.txt</outputFile>
        // example: compile dependency:copy-dependencies exec:exec -Dfreemarkertemplate.path=C:/Users/simonow/Documents/AdessoProjekte/hsac-fitnesse-fixtures/wiki/FitNesseRoot/files/templates
        // Found in 561 - 567 in pom.xml
        File tmpfile = new File("../src/main/resources" + propertyloc); // fixme: not well tested Path, could break. Better Solution?

        if (tmpfile.exists()) {
            // set new path
            try {
                is = new FileInputStream(tmpfile);
                p = new Properties();
                p.load(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultString = p.getProperty("freemarkertemplatepath");
        } else {
            // or set marker for the default path
            resultString = "$";
        }
        return resultString;
    }
}
