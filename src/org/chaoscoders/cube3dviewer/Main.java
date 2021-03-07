package org.chaoscoders.cube3dviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import javafx.scene.Scene;

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
        Material mat_black = new PhongMaterial(Color.BLACK);

        Cube standardCube = new Cube("0,0,0,0,0,0,0,0,0;1,1,1,1,1,1,1,1,1;2,2,2,2,2,2,2,2,2;3,3,3,3,3,3,3,3,3;4,4,4,4,4,4,4,4,4;5,5,5,5,5,5,5,5,5");
        intFaces = standardCube.getFaces();

        size = 100;
        offset = size/15;
        centerDistance = ( (size * 3) + (offset * 4) ) / 2;

        //Drawing a Box


        baseCube = new Box((size + offset) * 3 + offset, (size + offset) * 3 + offset, (size + offset) * 3 + offset);
        baseCube.setMaterial(mat_black);
        baseCube.setDrawMode(DrawMode.FILL);
        root = new Group();
        pane = new BorderPane();
        subScene = new SubScene(root, 1100, 1080, true, SceneAntialiasing.BALANCED);
        Main.pane.setCenter(subScene);
        loadCube();


        //Creating a Group object


        root.setTranslateX(500);
        root.setTranslateY(500);
        root.setTranslateZ(0);

        //matrixRotateNode(root, 0, 0, 0);

        //Setting the position of the group

        VBox left = FXMLLoader.load(getClass().getResource("/SidePanel.fxml"));
        left.prefHeightProperty().bind(pane.heightProperty());





        rotateX = new Rotate(30, 0, 0, 0, Rotate.X_AXIS);
        rotateY = new Rotate(20, 0, 0, 0, Rotate.Y_AXIS);
        root.getTransforms().addAll(rotateX, rotateY);

        pane.setLeft(left);

        pane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, new Insets(0))));
        left.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, new Insets(0))));
        left.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, new Insets(0))));


        //Creating a scene object
        Scene scene = new Scene(pane, 1920, 1080, false);

        //Setting camera
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);


        scene.setCamera(camera);

        //Setting title to the Stage
        primaryStage.setTitle("Cube Viewer");

        //Adding scene to the stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();
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

        //Nodes löschen und dann neu hinzufügen. Magic!
        root.getChildren().clear();
        root.getChildren().addAll(wFaces, rFaces, bFaces, oFaces, gFaces, yFaces, baseCube, new AmbientLight());

    }

    public static void main(String[] args) {
        launch(args);
    }
}

