package dip.lux.service;

import java.util.ArrayList;

public interface ShingleService {
    String canonize(String fileContent);

    ArrayList<Integer> generateShingle(String str);

    double compare(ArrayList<Integer> firstShingle, ArrayList<Integer> secondShingle);
}
