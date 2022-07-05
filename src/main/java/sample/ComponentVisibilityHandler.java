package sample;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ComponentVisibilityHandler {

    public void editEntryVisibility () {

    }
    public void showPwdGenerateMenu(AnchorPane apPwd, AnchorPane entryPane, Button btnEdit, AnchorPane apBottomTable) {
        apPwd.setVisible(true);
        apPwd.setDisable(false);
        entryPane.setDisable(true);
        entryPane.setVisible(false);
        btnEdit.setDisable(true);
        btnEdit.setVisible(false);
        apBottomTable.setVisible(false);
    }
}
