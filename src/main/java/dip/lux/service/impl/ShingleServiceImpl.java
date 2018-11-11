package dip.lux.service.impl;

import dip.lux.service.ShingleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ShingleServiceImpl implements ShingleService {
    private static final String[] STOP_WORDS_RU = {"это", "как", "так", "над", "для", "что", "кто", "она", "таблица", "рисунок", "рис", "таб", "табл"};

    private static final int SHINGLE_LEN = 2;


    @Override
    public String canonize(String fileContent) {
        fileContent = fileContent.toLowerCase();
        fileContent = fileContent.replaceAll("[^\\p{L}\\p{Nd}]+|\\s", " ");
        fileContent = fileContent.replaceAll("\n", " ");
        fileContent = fileContent.replaceAll("[0-9]", " ");
        fileContent = fileContent.replaceAll("[A-Za-z]", " ");
        fileContent = fileContent.replaceAll(" +", " ");

        for (String stopWord : STOP_WORDS_RU) {
            fileContent = fileContent.replace(" " + stopWord + " ", " ");
        }

        for(String word: fileContent.split(" ")){
            if(word.length() > 0 && word.length() < 3){
                fileContent = fileContent.replace(" " + word + " ", " ");
            }
        }

        return fileContent;
    }

    @Override
    public ArrayList<Integer> generateShingle(String text) {
        ArrayList<Integer> shingles = new ArrayList<>();
        String str = canonize(text);
        String[] words = str.split(" ");
        int shinglesNumber = words.length - SHINGLE_LEN;

        //Create all shingles
        for (int i = 0; i <= shinglesNumber; i++) {
            String shingle = "";

            //Create one shingle
            for (int j = 0; j < SHINGLE_LEN; j++) {
                shingle = shingle + words[i+j] + " ";
            }

            shingles.add(shingle.hashCode());
        }

        return shingles;
    }

    @Override
    public double compare(ArrayList<Integer> firstShingle, ArrayList<Integer> secondShingle) {
        if (firstShingle == null || secondShingle == null) return 0.0;

        int textShingles1Number = firstShingle.size();
        int textShingles2Number = secondShingle.size();

        double similarShinglesNumber = 0;

        for (Integer aFirstShingle : firstShingle) {
            for (Integer aSecondShingle : secondShingle) {
                if (aFirstShingle.equals(aSecondShingle)) similarShinglesNumber++;
            }
        }

        return ((similarShinglesNumber / ((textShingles1Number + textShingles2Number) / 2.0)) * 100);
    }
}
