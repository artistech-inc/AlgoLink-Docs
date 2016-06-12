/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artistech.utils.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author matta
 */
public final class Logging {

    private Logging() {
    }

    public static void initLogging() {
        try {
            //init logger
            File f = java.io.File.createTempFile("logging", ".propterties");
            try {
                String prop_file = Logging.class.getCanonicalName().replace('.', '/') + ".properties";
                java.util.Enumeration<URL> resources = Logging.class.getClassLoader()
                        .getResources(prop_file);
                if (resources.hasMoreElements()) {
                    URL to_use = resources.nextElement();
                    try (FileOutputStream fis = new FileOutputStream(f)) {
                        IOUtils.copy(to_use.openStream(), fis);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (f.exists()) {
                try (FileInputStream fis = new FileInputStream(f)) {
                    LogManager.getLogManager().readConfiguration(fis);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException | SecurityException ex) {
                    Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
