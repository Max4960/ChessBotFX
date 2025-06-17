package model;

import javafx.scene.layout.GridPane;

public class ProgressBar {
    public static void print(long c, long t) {
        final int width = 50;
        if (t == 0) {
            return;
        }

        long percentage = (c*100)/t;
        long progress = (c*width)/t;

        StringBuilder sb = new StringBuilder();
        sb.append('\r');
        sb.append('[');
        for (int i = 0; i < width; i++) {
            if (i <= progress) {
                sb.append('|');
            } else {
                sb.append(' ');
            }
        }
        sb.append("] " +  percentage + "%");
        System.out.print(sb.toString());
        if (c == t) {
            System.out.println();
        }
    }
}
