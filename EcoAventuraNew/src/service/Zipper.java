/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import gui.ZippingFrame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Alexandru
 */
public class Zipper {
    private List<File> queue;
    private String outPath;
    private String source;
    
    public Zipper (String outPath, String source) {
        this.outPath = outPath;
        this.source = source;
        queue = new ArrayList<>();
    }
    
    public Zipper (File file, String outPath, String source) {
        this.outPath = outPath;
        this.source = source;
        queue = new ArrayList<>();
        queue.add(file);
    }
    
    public Zipper (File [] files, String outPath, String source) {
        this.outPath = outPath;
        this.source = source;
        queue = new ArrayList<>();
        for (int i = 0; i < files.length; ++i) {
            queue.add(files[i]);
        }
    }

    public List<File> getQueue() {
        return queue;
    }

    public void setQueue(List<File> queue) {
        this.queue = queue;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }
    
    public void add (File f) {
        queue.add(f);
    }
    
    public void zip (ZippingFrame frame) throws Exception {
        byte [] buffer = new byte[1024];
        FileOutputStream fos = null;
        FileInputStream in = null;
        ZipOutputStream zos = null;
        
        //LoadingFrame.getInstance().setProgress(0);
        long totalSize = 0;
        long actualSize = 0;
        for (File f : queue) {
            totalSize += f.length();
        }
        
        fos = new FileOutputStream(outPath);
        zos = new ZipOutputStream(fos);
        
        System.out.println("Start zipping to : " + outPath);
        
        for (File f : queue) {
            System.out.println("Adding : " + f.getName());
            frame.updateList("Se adauga : " + f.getName());
            ZipEntry ze = new ZipEntry(f.getCanonicalPath().substring(source.length()+1));
            zos.putNextEntry(ze);            
            
            try {
                in = new FileInputStream(f);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                    actualSize += len;
                    frame.updateBar((int) (actualSize * 100 / totalSize));
                }
            } catch (Exception e) {} 
            finally {
                in.close();
            }               
            zos.closeEntry();
        }
        
        zos.close();
        System.out.println("Done");
    }
}
