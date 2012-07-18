/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nypl.mssa.quickmd;

import java.io.*;

/**
 *
 * @author dm
 */
public class QuickMD {
    private File file;
    private RandomAccessFile raf;
    QuickMD(String filein) throws IOException, InterruptedException{
        file = new File(filein);
        //System.out.println(file.exists());
        getSHA1();
        getSize();
        getFS();
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
        if(line == null){
            if(checkHFS())
                System.out.println("File System: HFS");
            else
                System.out.println("File System: not determined");
        }
        else{
            System.out.println("File System: " + line);
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        
        String s = args[0];
        //String s = "/Volumes/Staging/Imaging_Workflow/B.Needs_Metadata/M1126/M1126-0042.001";
        QuickMD q = new QuickMD(s); 
    }

    private boolean checkHFS() throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        raf = new RandomAccessFile(file, "r");
        raf.seek(Integer.parseInt("400", 16));
        sb.append(Integer.toHexString(raf.read()));
        raf.seek(Integer.parseInt("401", 16));
        sb.append(Integer.toHexString(raf.read()));
        if(sb.toString().equals("4244"))
            return true;
        else
            return false;
    }

}

