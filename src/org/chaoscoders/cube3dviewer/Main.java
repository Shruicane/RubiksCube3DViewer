package org.chaoscoders.cube3dviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import javafx.scene.Scene;

import java.io.IOException;

public class Main extends Application {

    public static Box[][] faces;
    public static int[][] intFaces;
    public static Material[] mats;
    public static double sens;

    public static Group root;
    public static Group wFaces;
    public static Group rFaces;
    public static Group bFaces;
    public static Group oFaces;
    public static Group gFaces;
    public static Group yFaces;
    public static Box baseCube;

    private static double size;
    private static double offset;
    private static double centerDistance;

    private static BorderPane pane;
    private static Scene mainScene;
    private static SubScene subScene;
    private static Rotate rotateX;
    private static Rotate rotateY;

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;


    @Override
    public void start(Stage primaryStage) throws Exception{
        initMembers();
        createBaseCube();
        initRoot();
        initBorderPane();
        loadCube();
        loadLeftPane();
        mainScene = new Scene(pane, 1920, 1080, false);
        loadCamera();
        initPrimaryStage(primaryStage);
    }

    public static void loadCube(){

        //create faces
        //yellow & white
        wFaces = generateFace(0,100, 100, 10);
        yFaces = generateFace(5,100, 100, 10);
        wFaces.setTranslateZ(-centerDistance);
        yFaces.setTranslateZ(centerDistance);

        //red & orange
        rFaces = generateFace(1,10, 100, 100);
        oFaces = generateFace(3,10, 100, 100);
        rFaces.setTranslateX(-centerDistance);
        oFaces.setTranslateX(centerDistance);


        //blue & green
        bFaces = generateFace(2, 100, 10, 100);
        gFaces = generateFace(4, 100, 10, 100);
        bFaces.setTranslateY(centerDistance);
        gFaces.setTranslateY(-centerDistance);

        fixFaceRotation();

        //delete Nodes and them back to the scene. Magic!
        root.getChildren().clear();
        root.getChildren().addAll(wFaces, rFaces, bFaces, oFaces, gFaces, yFaces, baseCube, new AmbientLight());
    }

    private void initMembers(){
        sens = 1;
        faces = new Box[6][9];
        mats = new Material[6];
        mats[0] = new PhongMaterial(Color.WHITE);
        mats[1] = new PhongMaterial(Color.RED);
        mats[2] = new PhongMaterial(Color.BLUE);
        mats[3] = new PhongMaterial(Color.ORANGE);
        mats[4] = new PhongMaterial(Color.GREEN);
        mats[5] = new PhongMaterial(Color.YELLOW);

        Cube standardCube = new Cube("0,0,0,0,0,0,0,0,0;1,1,1,1,1,1,1,1,1;2,2,2,2,2,2,2,2,2;3,3,3,3,3,3,3,3,3;4,4,4,4,4,4,4,4,4;5,5,5,5,5,5,5,5,5");
        intFaces = standardCube.getFaces();

        size = 100;
        offset = size/15;
        centerDistance = ( (size * 3) + (offset * 4) ) / 2;
    }

    private void initPrimaryStage(Stage primaryStage){
        primaryStage.setTitle("Cube Viewer");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/RubiksCubeIcon.png")));
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void loadCamera(){
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);
        mainScene.setCamera(camera);
    }

    private void createBaseCube(){
        baseCube = new Box((size + offset) * 3 + offset, (size + offset) * 3 + offset, (size + offset) * 3 + offset);
        baseCube.setMaterial(new PhongMaterial(Color.BLACK));
        baseCube.setDrawMode(DrawMode.FILL);
    }

    private void loadLeftPane() throws IOException {
        VBox left = FXMLLoader.load(Main.class.getResource("/SidePanel.fxml"));
        left.prefHeightProperty().bind(pane.heightProperty());
        left.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, new Insets(0))));
        pane.setLeft(left);
    }

    private void initRoot(){
        root = new Group();
        root.setTranslateX(500);
        root.setTranslateY(500);
        root.setTranslateZ(0);
        rotateX = new Rotate(30, 0, 0, 0, Rotate.X_AXIS);
        rotateY = new Rotate(20, 0, 0, 0, Rotate.Y_AXIS);
        root.getTransforms().addAll(rotateX, rotateY);
    }

    private void initBorderPane(){
        pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, new Insets(0))));
        subScene = new SubScene(root, 1100, 1080, true, SceneAntialiasing.BALANCED);
        initRotator();
        Main.pane.setCenter(subScene);
    }

    private void initRotator(){
        subScene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        subScene.setOnMouseDragged(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle()-((mousePosY - mouseOldY) / 2 * sens));
            rotateY.setAngle(rotateY.getAngle()+((mousePosX - mouseOldX) / 2 * sens));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });
    }

    private static Group generateFace(int i, int width, int height, int depth) {
        Group face = new Group();
        for (int j = 0; j < 9; j++) {
            faces[i][j] = new Box(width, height, depth);
            faces[i][j].setMaterial(mats[intFaces[i][j]]);
            int indexX = j%3;
            int indexY = j/3;

            switch (i){
                case 0:
                case 5:
                    faces[i][j].setTranslateX((-1 + indexX) * ((offset + size)));
                    faces[i][j].setTranslateY((-1 + indexY) * ((offset + size)));
                    break;
                case 1:
                case 3:
                    faces[i][j].setTranslateY((-1 + indexX) * ((offset + size)));
                    faces[i][j].setTranslateZ((-1 + indexY) * ((offset + size)));
                    break;
                case 2:
                case 4:
                    faces[i][j].setTranslateX((-1 + indexX) * ((offset + size)));
                    faces[i][j].setTranslateZ((-1 + indexY) * ((offset + size)));
                    break;
            }
            face.getChildren().add(faces[i][j]);
        }
        return face;
    }

    private static void fixFaceRotation(){
        //Rotations der einzelnen Seiten anpassen um mit den Indexen aus dem Schema übereinzustimmen
        yFaces.setRotationAxis(Rotate.Y_AXIS);
        yFaces.setRotate(180);
        gFaces.setRotationAxis(Rotate.Z_AXIS);
        gFaces.setRotate(180);

        //180 z und 90 x
        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxBox.setAngle(180);
        rzBox.setAngle(180);
        oFaces.getTransforms().addAll(rxBox, ryBox, rzBox);

        bFaces.setRotationAxis(Rotate.Y_AXIS);
        bFaces.setRotate(180);
        rFaces.setRotationAxis(Rotate.X_AXIS);
        rFaces.setRotate(180);
        wFaces.setRotationAxis(Rotate.Z_AXIS);
        wFaces.setRotate(180);
    }

    public static void main(String[] args) {
        launch(args);
    }
}