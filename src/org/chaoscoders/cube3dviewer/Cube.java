package org.chaoscoders.cube3dviewer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Cube {


    // C0,C1,C2,C3,C4,C5,C6,C7,C8; 6-mal

    private Box[][] boxFaces;
    private int[][] faces;
    private boolean isValid;

    public Cube(String cubeCode){

        faces = new int[6][9];
        boxFaces = new Box[6][9];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                boxFaces[i][j] = new Box(10, 10, 10);
                boxFaces[i][j].setMaterial(new PhongMaterial(Color.PURPLE));
            }
        }

        try{
            int index = 0;
            for (String s : cubeCode.split(";")){
                for (int i = 0; i < 9; i++) {
                    faces[index][i] = Integer.parseInt(s.split(",")[i]);
                    Material mat = Main.mats[faces[index][i]];
                    boxFaces[index][i].setMaterial(mat);
                }
                index++;
            }
            this.isValid = true;
        }catch (Exception e){
            this.isValid = false;
            e.printStackTrace();
        }


    }

    public Box[][] getBoxFaces() {
        return boxFaces;
    }

    public boolean isValid(){
        return isValid;
    }

    public int[][] getFaces() {
        return faces;
    }
}
