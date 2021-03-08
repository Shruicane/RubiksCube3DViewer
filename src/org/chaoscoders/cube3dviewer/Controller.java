package org.chaoscoders.cube3dviewer;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
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
        Optional<Pair<String, String>> result = this.getDialogResult();
        if (result.isPresent()){
            String cubeName = result.get().getKey();
            String cubeCode = result.get().getValue();
            Cube cube = new Cube(cubeCode);
            if(!cubeMap.containsKey(cubeName)){

                if(cube.isValid()){
                    this.cubeMap.put(cubeName, cube);
                    this.cubeListView.getItems().add(cubeName);
                    this.loadCube(cube);

                    this.cubeListView.getSelectionModel().select(this.cubeMap.size() - 1);
                }else{
                    Alert alert = new Alert(AlertType.ERROR, "Invalid Cube Code!");
                    alert.show();
                }
            }else{
                Alert alert = new Alert(AlertType.ERROR, "Cube Name already in use!");
                alert.show();
            }
        }
    }

    private Optional<Pair<String, String>> getDialogResult(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Cube Dialog");
        dialog.setHeaderText("Please enter your cube code and a name.");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

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

        Node buttonOK = dialog.getDialogPane().lookupButton(ButtonType.OK);
        buttonOK.setDisable(true);

        cubeNameTextField.textProperty().addListener((observable, oldValue, newValue) -> buttonOK.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(grid);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(cubeNameTextField.getText(), cubeCodeTextField.getText());
            }
            return null;
        });
        return dialog.showAndWait();
    }

    @FXML
    public void onClickRemove(){
        if(cubeListView.getSelectionModel().getSelectedItem() != null){
            int index = cubeListView.getSelectionModel().getSelectedIndex();
            String selection = cubeListView.getSelectionModel().getSelectedItem();
            Alert confirm = new Alert(AlertType.CONFIRMATION,
                    "Are you sure you want to remove \"" + selection + "\" from the list?");
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                cubeListView.getItems().remove(selection);
                cubeMap.remove(selection);
                if(index - 1 >= 0){
                    //fix selection
                    cubeListView.getSelectionModel().select(index - 1);
                    onClickListView();
                }
                //show solved Cube if List is empty
                if(cubeMap.size() < 1){
                    loadCube(new Cube("0,0,0,0,0,0,0,0,0;1,1,1,1,1,1,1,1,1;2,2,2,2,2,2,2,2,2;3,3,3,3,3,3,3,3,3;4,4,4,4,4,4,4,4,4;5,5,5,5,5,5,5,5,5"));
                }
            }
        }
    }

    @FXML
    public void onClickListView(){
        if(cubeListView.getSelectionModel().getSelectedItem() != null){
            loadCube(cubeMap.get(this.cubeListView.getSelectionModel().getSelectedItem()));
        }
    }

    int index = 0;


    @FXML
    private void initialize(){
        this.cubeMap = new HashMap<>();
        sensSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            Main.sens = newValue.doubleValue();
            sensLabel.textProperty().setValue("Rotate Sensitivity: " + newValue.doubleValue());
        });
        applyCellFactory();
    }

    private void loadCube(Cube cube){
        Main.faces = cube.getBoxFaces().clone();
        Main.intFaces = cube.getFaces().clone();
        Main.loadCube();
    }

    private void applyCellFactory(){
        index = 0;

        cubeListView.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<String>() {

                @Override
                protected void updateItem(String t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        setText(t);
                    }
                }
            };

            ContextMenu contextMenu = new ContextMenu();
            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Copy Cube Code"));
            editItem.setOnAction(event -> {
                final ClipboardContent content = new ClipboardContent();
                content.putString(this.cubeMap.get(this.cubeListView.getSelectionModel().getSelectedItem()).getCubeCode());
                Clipboard.getSystemClipboard().setContent(content);
            });
            contextMenu.getItems().addAll(editItem);

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });

            return cell;
        });
    }

}