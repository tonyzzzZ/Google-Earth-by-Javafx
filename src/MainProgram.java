
import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.WorldController;
import javafx.fxml.FXMLLoader;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.collections.ObservableList;

public class MainProgram extends Application {

	public void start(Stage stage) throws Exception {
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			AnchorPane page = (AnchorPane) fxmlLoader.load(this.getClass().getResourceAsStream("WorldViewer.fxml"));
			WorldController controller = (WorldController) fxmlLoader.getController();
			SubScene subscene = controller.createSubScene();
			controller.inisialize();
			
			Group group = new Group();
			group.getChildren().add(subscene);
			group.getChildren().add(page);
			Scene scene = new Scene(group);
			scene.getStylesheets().add("MyStyle.css");
			
			stage.setTitle("Tony's toy");
			stage.setScene(scene);
			stage.show();
        
		} catch ( IOException ex) {
		   Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		   System.exit(1);
		}
	}
	
    public static void main(String args[]) {
     	launch(args);
     	System.exit(0);
    }
}
