module com.ramolete.betterdialog {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    //requires com.ramolete.betterdialog;

    // Keep this if you have classes in the top package that other modules need
    exports com.ramolete.betterdialog;

    // <<< ADD THIS LINE >>>
    // Exports the package containing BattleUI to the world (unqualified export)
    exports com.ramolete.betterdialog.rpgbattle;


    opens com.ramolete.betterdialog to javafx.fxml;
    exports com.ramolete.betterdialog.trash;
}