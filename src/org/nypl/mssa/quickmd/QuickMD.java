/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nypl.mssa.quickmd;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author dm
 */
public class QuickMD {
    private File file;
    private RandomAccessFile raf;
    QuickMD(String filein) throws IOException, InterruptedException, NoSuchAlgorithmException{
        file = new File(filein);
        //System.out.println(file.exists());
        System.out.println("QuickMD v1.0\n");
        getSHA1();
        getSize();
        getFS();
        System.out.println("\n");
    }
    


    private void getSHA1() throws IOException, InterruptedException, NoSuchAlgorithmException {
        /*
        Process p = Runtime.getRuntime().exec("openssl dgst -sha1 " + file);
        p.waitFor();
        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
        String line=reader.readLine(); 
        */
        
        System.out.println(calculateHash());
        
        
    }

    private void getSize() throws IOException, InterruptedException {
        System.out.println("Size: \t\t" + file.length());
    }

    private void getFS() throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("/usr/local/bin/fsstat -t " + file.getAbsolutePath());
        p.waitFor();
        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
        String line=reader.readLine();
        if(line == null){
            if(checkHFS())
                System.out.println("File System: \tHFS");
            else
                System.out.println("File System: \tnot determined");
        }
        else{
            System.out.println("File System:\t" + line);
        }
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
    
    private String calculateHash() throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        InputStream is = new FileInputStream(file);				
	byte[] buffer = new byte[1024];
	
        int read = 0;
        
        while((read = is.read(buffer)) > 0) {
            messageDigest.update(buffer, 0, read);
        }		
        
        byte[] sum = messageDigest.digest();
        
        BigInteger bigInt = new BigInteger(1, sum);
        String output = bigInt.toString(16);
	return ("SHA1: \t\t" + output);
    }
        
    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException{
        
        String s = args[0];
        //String s = "/Volumes/Staging/Imaging_Workflow/B.Needs_Metadata/M1126/M1126-0042.001";
        QuickMD q = new QuickMD(s); 
    }

}

