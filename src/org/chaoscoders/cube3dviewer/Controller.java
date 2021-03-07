package org.chaoscoders.cube3dviewer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Optional;

public class Controller {

    @FXML
    private Label sensLabel;

    @FXML
    private Slider sensSlider;

    @FXML
    private ListView<String> cubeListView;

    private HashMap<String, Cube> cubeMap;

    @FXML
    public void onClickAdd(){
        //Namen müssen eindeutig sein

        String cubeName = "";
        String cubeCode = "";

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Cube Dialog");
        dialog.setHeaderText("Please enter your cube code and a name.");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField cubeNameTextField = new TextField();
        cubeNameTextField.setPromptText("Cubename");
        TextField cubeCodeTextField = new TextField();
        cubeCodeTextField.setPromptText("Cubecode");

        grid.add(new Label("Cubename:"), 0, 0);
        grid.add(cubeNameTextField, 1, 0);
        grid.add(new Label("Cubecode:"), 0, 1);
        grid.add(cubeCodeTextField, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        cubeNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(cubeNameTextField.getText(), cubeCodeTextField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent()){
            cubeName = cubeNameTextField.getText();
            cubeCode = cubeCodeTextField.getText();
            Cube cube = new Cube(cubeCode);
            if(!cubeMap.containsKey(cubeName)){

                if(cube.isValid()){
                    this.cubeMap.put(cubeName, cube);
                    this.cubeListView.getItems().add(cubeName);
                    this.loadCube(cube);
                }else{
                    Alert alert = new Alert(AlertType.ERROR, "Wrong Cube Code!");
                    alert.show();
                }


            }else{
                Alert alert = new Alert(AlertType.ERROR, "Cube Name already in use!");
                alert.show();
            }
        }
    }

    @FXML
    public void onClickRemove(){
        if(cubeListView.getSelectionModel().getSelectedItem() != null){
            String selection = cubeListView.getSelectionModel().getSelectedItem();
            Alert confirm = new Alert(AlertType.CONFIRMATION,
                    "Are you sure you want to remove \"" + selection + "\" from the list?");
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.get() == ButtonType.OK){
                cubeListView.getItems().remove(selection);
                cubeMap.remove(selection);
            }
        }
    }

    @FXML
    public void onClickListView(){
        if(cubeListView.getSelectionModel().getSelectedItem() != null){
            loadCube(cubeMap.get(this.cubeListView.getSelectionModel().getSelectedItem()));
        }
    }

    @FXML
    private void initialize(){
        this.cubeMap = new HashMap<>();
        sensSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                Main.sens = newValue.doubleValue();
                sensLabel.textProperty().setValue("Rotate Sensitivity: " + newValue.doubleValue());
            }
        });
        /*Cube standardCube = new Cube("0,0,0,0,0,0,0,0,0;1,1,1,1,1,1,1,1,1;2,2,2,2,2,2,2,2,2;3,3,3,3,3,3,3,3,3;4,4,4,4,4,4,4,4,4;5,5,5,5,5,5,5,5,5");
        cubeMap.put("Solved Cube", standardCube);
        cubeListView.getItems().add("Solved Cube");
        loadCube(standardCube);*/
    }

    private void loadCube(Cube cube){
        Main.faces = cube.getBoxFaces().clone();
        Main.intFaces = cube.getFaces().clone();
        Main.loadCube();
    }

}
