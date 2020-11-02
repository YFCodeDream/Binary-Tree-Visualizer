package sample.view;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/11/2 10:17
 */
public class UpdateLogViewController {
    private Stage dialogStage;
    @FXML
    private void handleGotIt() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.getIcons().add(new Image("file:resources/images/tree-icon.png"));
    }
}
