package Controller;

import Model.HexGen;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Stage stage;
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    private HexGen hexGenerator;
    private Alert alert;

    @FXML TextField imagePath;
    @FXML TextField desPath;
    @FXML ProgressBar progressBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        directoryChooser = new DirectoryChooser();
        hexGenerator = new HexGen();
        imagePath.setText("");
        desPath.setText("");
        progressBar.progressProperty().bind(hexGenerator.doubleProperty);
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }

    public void exit(ActionEvent event){
        Platform.exit();
    }

    public void chooseFile(ActionEvent event){
        File file = fileChooser.showOpenDialog(stage);
        if(file!=null){
            imagePath.setText(file.getAbsolutePath());
            hexGenerator.setImageFile(file);
        }else{
            alert = new Alert(Alert.AlertType.ERROR, "File is invalid!");
            alert.show();
        }

    }

    public void chooseDestination(ActionEvent event){
        String path = "";
        File selected = directoryChooser.showDialog(stage);
        if(selected!=null){
            path=selected.getAbsolutePath()+"\\hex";
            desPath.setText(path);
            hexGenerator.setHexPath(path);
        }else{
            alert = new Alert(Alert.AlertType.ERROR, "Path is invalid!");
            alert.show();
        }
    }

    public void generateHexFile(ActionEvent event){
        if(desPath.getText().isEmpty() || imagePath.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR, "Please fill all the fields.");
            alert.show();
        }else{
            try{
                Task task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception{
                        hexGenerator.process();
                        return null;
                    }
                };
                task.setOnSucceeded(val -> {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Hex File Generated!");
                    alert.show();
                });
                task.setOnFailed(val -> {
                    alert = new Alert(Alert.AlertType.ERROR, task.getException().toString());
                    alert.show();
                });
                new Thread(task).start();


            }catch (Exception e){
                alert = new Alert(Alert.AlertType.ERROR, e.toString());
                alert.show();
            }
        }

    }
}
