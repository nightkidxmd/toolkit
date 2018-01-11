package com.dadatoolkit.compressutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

/**
 * TODO need to modify
 * @author maoda.xu@samsung.com
 *
 */
public class FileCompressHelper {

    private String mFilePath;
    private OnCompressListener L;
    public FileCompressHelper(String tarFilePath) {
        mFilePath = tarFilePath;
    }


    /**
     * Decompress .tgz or .tar
     * @param l
     */

    public void decompress(OnCompressListener l){
        File f = null;
        L = l;
        if(mFilePath.endsWith(".tgz")){
            f = deCompressTGZFile(mFilePath);
        }else{
            f = new File(mFilePath);
        }
        if(f != null && f.exists()){
            deCompressTARFile(f);
        }
    }


    private File deCompressTGZFile(String filepath){
        File f = new File(filepath);
        FileOutputStream out = null;
        GZIPInputStream gzIn = null;
        File outFile = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(f);
            BufferedInputStream in = new BufferedInputStream(fin);
            outFile = new File("tmp.tar");
            if(outFile.exists()){
                outFile.delete();
            }
            out = new FileOutputStream(outFile);
            gzIn = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int n = -1;
            while((n = gzIn.read(buffer))!= -1){
                out.write(buffer,0,n);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(out != null)
                try {
                    out.close(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (fin != null){
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return outFile;
    }


    private void deCompressTARFile(File file){
        TarArchiveInputStream is = null;
        File root = new File("feature"+File.separator);
        if(root.exists()){
            File[] files = root.listFiles();
            for(File f:files){
                f.delete();
            }
        }

        root.mkdir();

        try {
            is = new TarArchiveInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {

            while (true) {
                TarArchiveEntry entry;
                entry = is.getNextTarEntry();
                if (entry == null) {
                    break;
                }

                if(!entry.isDirectory()){
                    FileOutputStream os = null;
                    String[] temp = entry.getName().split("/");
                    String fileName = temp[temp.length-1];
                    File f = new File(root.getAbsoluteFile()+File.separator+fileName);
                    if (!f.exists()) {
                        f.createNewFile();

                    }
                    try{
                        os = new FileOutputStream(f);
                        byte[] buffer = new byte[1024];
                        int n = -1;
                        while((n = is.read(buffer))!= -1){
                            os.write(buffer,0,n);
                        }
                    }catch(IOException e){
                        e.printStackTrace();

                    }finally{
                        if (os != null){
                            os.close();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(L != null){
            L.onCompressDone();
        }
    }


    public interface OnCompressListener{
        public void onCompressDone();
    }

    
    public void decompressZipFile(String filepath,String outpath){
    	File zipFile = new File(filepath);
    	ZipArchiveInputStream is = null;
        File root = new File(outpath+File.separator);
        if(root.exists()){
            File[] files = root.listFiles();
            for(File f:files){
                f.delete();
            }
        }
        
        root.mkdir();
        
        try {
            is = new ZipArchiveInputStream(new FileInputStream(zipFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {

            while (true) {
                ZipArchiveEntry entry;
                entry = is.getNextZipEntry();
                if (entry == null) {
                    break;
                }

                if(!entry.isDirectory()){
                    FileOutputStream os = null;
                    String[] temp = entry.getName().split("/");
                    String fileName = temp[temp.length-1];
                    File f = new File(root.getAbsoluteFile()+File.separator+fileName);
                    if (!f.exists()) {
                        f.createNewFile();

                    }
                    try{
                        os = new FileOutputStream(f);
                        byte[] buffer = new byte[1024];
                        int n = -1;
                        while((n = is.read(buffer))!= -1){
                            os.write(buffer,0,n);
                        }
                    }catch(IOException e){
                        e.printStackTrace();

                    }finally{
                        if (os != null){
                            os.close();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(L != null){
            L.onCompressDone();
        }
    }
}
