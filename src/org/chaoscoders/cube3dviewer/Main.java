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

    private static double xRot = 0;
    private static double yRot = 0;
    private static double zRot = 0;

    private static double mousePosX;
    private static double mousePosY;
    private static double mouseOldX;
    private static double mouseOldY;

    public static Material[] mats;
    public static Group root;
    public static Group wFaces;
    public static Group rFaces;
    public static Group bFaces;
    public static Group oFaces;
    public static Group gFaces;
    public static Group yFaces;
    public static Box baseCube;

    private static double size = 100;
    private static double offset = size/15;
    private static double centerDistance = ( (size * 3) + (offset * 4) ) / 2;

    private static BorderPane pane;
    private static Scene mainScene;
    private static SubScene subScene;
    private static Rotate rotateX;
    private static Rotate rotateY;

    public static Box[][] faces;
    public static int[][] intFaces;

    public static double sens = 1;

    @Override
    public void start(Stage primaryStage) throws Exception{

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

        createBaseCube();
        initRoot();

        initBorderPane();
        loadCube();
        loadLeftPane();

        //Creating a scene object
        mainScene = new Scene(pane, 1920, 1080, false);
        loadCamera();
        initPrimaryStage(primaryStage);

    }

    private static void initPrimaryStage(Stage primaryStage){
        primaryStage.setTitle("Cube Viewer");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/RubiksCubeIcon.png")));
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private static void loadCamera(){
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);
        mainScene.setCamera(camera);
    }

    private static void createBaseCube(){
        baseCube = new Box((size + offset) * 3 + offset, (size + offset) * 3 + offset, (size + offset) * 3 + offset);
        baseCube.setMaterial(new PhongMaterial(Color.BLACK));
        baseCube.setDrawMode(DrawMode.FILL);
    }

    private static void loadLeftPane() throws IOException {
        VBox left = FXMLLoader.load(Main.class.getResource("/SidePanel.fxml"));
        left.prefHeightProperty().bind(pane.heightProperty());
        left.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, new Insets(0))));
        pane.setLeft(left);
    }

    private static void initRoot(){
        root = new Group();
        root.setTranslateX(500);
        root.setTranslateY(500);
        root.setTranslateZ(0);
        rotateX = new Rotate(30, 0, 0, 0, Rotate.X_AXIS);
        rotateY = new Rotate(20, 0, 0, 0, Rotate.Y_AXIS);
        root.getTransforms().addAll(rotateX, rotateY);
    }

    private static void initBorderPane(){
        pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, new Insets(0))));
        subScene = new SubScene(root, 1100, 1080, true, SceneAntialiasing.BALANCED);
        Main.pane.setCenter(subScene);
    }

    public static void loadCube(){
        //create faces

        //yellow & white
        wFaces = new Group();
        yFaces = new Group();

        int i = 0;
        for (int j = 0; j < 9; j++) {
            faces[i][j] = new Box(100, 100, 10);
            faces[i][j].setMaterial(mats[intFaces[i][j]]);
            int indexX = j%3;
            int indexY = j/3;
            faces[i][j].setTranslateX((-1 + indexX) * ((offset + size)));
            faces[i][j].setTranslateY((-1 + indexY) * ((offset + size)));
            wFaces.getChildren().add(faces[i][j]);
        }

        wFaces.setTranslateZ(-centerDistance);

        i = 5;
        for (int j = 0; j < 9; j++) {
            faces[i][j] = new Box(100, 100, 10);
            faces[i][j].setMaterial(mats[intFaces[i][j]]);
            int indexX = j%3;
            int indexY = j/3;
            faces[i][j].setTranslateX((-1 + indexX) * ((offset + size)));
            faces[i][j].setTranslateY((-1 + indexY) * ((offset + size)));
            yFaces.getChildren().add(faces[i][j]);
        }

        yFaces.setTranslateZ(centerDistance);

        //red & orange
        rFaces = new Group();
        oFaces = new Group();

        i = 1;
        for (int j = 0; j < 9; j++) {
            faces[i][j] = new Box(10, 100, 100);
            faces[i][j].setMaterial(mats[intFaces[i][j]]);
            int indexX = j%3;
            int indexY = j/3;
            faces[i][j].setTranslateY((-1 + indexX) * ((offset + size)));
            faces[i][j].setTranslateZ((-1 + indexY) * ((offset + size)));
            rFaces.getChildren().add(faces[i][j]);
        }

        rFaces.setTranslateX(-centerDistance);

        i = 3;
        for (int j = 0; j < 9; j++) {
            faces[i][j] = new Box(10, 100, 100);
            faces[i][j].setMaterial(mats[intFaces[i][j]]);
            int indexX = j%3;
            int indexY = j/3;
            faces[i][j].setTranslateY((-1 + indexX) * ((offset + size)));
            faces[i][j].setTranslateZ((-1 + indexY) * ((offset + size)));
            oFaces.getChildren().add(faces[i][j]);
        }

        oFaces.setTranslateX(centerDistance);


        //blue & green
        bFaces = new Group();
        gFaces = new Group();

        i = 2;
        for (int j = 0; j < 9; j++) {
            faces[i][j] = new Box(100, 10, 100);
            faces[i][j].setMaterial(mats[intFaces[i][j]]);
            int indexX = j%3;
            int indexY = j/3;
            faces[i][j].setTranslateX((-1 + indexX) * ((offset + size)));
            faces[i][j].setTranslateZ((-1 + indexY) * ((offset + size)));
            bFaces.getChildren().add(faces[i][j]);
        }

        bFaces.setTranslateY(centerDistance);

        i = 4;
        for (int j = 0; j < 9; j++) {
            faces[i][j] = new Box(100, 10, 100);
            faces[i][j].setMaterial(mats[intFaces[i][j]]);
            int indexX = j%3;
            int indexY = j/3;
            faces[i][j].setTranslateX((-1 + indexX) * ((offset + size)));
            faces[i][j].setTranslateZ((-1 + indexY) * ((offset + size)));
            gFaces.getChildren().add(faces[i][j]);
        }

        gFaces.setTranslateY(-centerDistance);

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

        fixFaceRotation();

        //Nodes löschen und dann neu hinzufügen. Magic!
        root.getChildren().clear();
        root.getChildren().addAll(wFaces, rFaces, bFaces, oFaces, gFaces, yFaces, baseCube, new AmbientLight());

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

        //180 y
        bFaces.setRotationAxis(Rotate.Y_AXIS);
        bFaces.setRotate(180);

        //180 x
        rFaces.setRotationAxis(Rotate.X_AXIS);
        rFaces.setRotate(180);

        //180 z
        wFaces.setRotationAxis(Rotate.Z_AXIS);
        wFaces.setRotate(180);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

