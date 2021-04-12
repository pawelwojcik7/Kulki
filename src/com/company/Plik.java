package com.company;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Plik {

    File file;
    String lokacja;
    public Plik(String lokacja) throws IOException {
        this.lokacja=lokacja;
        this.file = new File(lokacja);
        if (!file.exists()) {
            file.createNewFile();
        }

        if (file.length() != 0) {
            FileWriter writer = new FileWriter(lokacja);
            writer.write("");

        }
    }

    public void zapisz(String tekst) throws IOException {
        FileWriter writer = new FileWriter(this.lokacja, true);
        writer.append(tekst);
        writer.close();
    }



}
