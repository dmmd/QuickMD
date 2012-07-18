/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nypl.mssa.quickmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author dm
 */
public class QuickMD {
    private File file;
    QuickMD(String filein) throws IOException, InterruptedException{
        file = new File(filein);
        //System.out.println(file.exists());
        getSHA1();
        getSize();
        getFS();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        
        String s = args[0];
        QuickMD q = new QuickMD(s); 
    }

    private void getSHA1() throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("openssl dgst -sha1 " + file);
        p.waitFor();
        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
        String line=reader.readLine(); 
        System.out.println("Sha1: " + line.substring(line.length() - 40));
        
        
    }

    private void getSize() throws IOException, InterruptedException {
        System.out.println("Size: " + file.length());
    }

    private void getFS() throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("/usr/local/bin/fsstat -t " + file.getAbsolutePath());
        p.waitFor();
        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
        String line=reader.readLine(); 
        System.out.println("File System: " + line);
    }
    
}
