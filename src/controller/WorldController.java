package controller;



import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import service.Service;
import service.ServiceFromJson;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.input.MouseButton;

public class WorldController {
	@FXML
	private ImageView itemView,itemView2,itemView3, itemView4,position, directionView;
	@FXML
	private ToggleButton buttonViewUp,buttonViewDown,buttonViewLeft,buttonViewRight;
	@FXML 
	private ScrollPane scrollPane;
	private String item,item2,item3,item4;
	private PerspectiveCamera perCamera; 
	private Sphere sphereView;
	private ServiceFromJson test;
	/*
	 * Avoid to see something strange
	 */
	private int  maxHDegree;
	private Integer axisX, axisY, preX ;
	private HashMap<String, Double> Degrees;
	private Service service;
	
	public WorldController(){
		item = "1";
		item2 = "1";
		item3 = "1";
		item4 = "1";
		test = new ServiceFromJson();
		service  = test.fromProperties();
		maxHDegree = 90;
		axisX = 100;
		axisY = 100;
		preX = 0;
		Degrees = new HashMap<>();
		// initialize the Vertical and Horizontal degrees
		Degrees.put("V", 0.0);
		Degrees.put("H", 0.0);
	}
	

	public void inisialize() throws IOException{
		itemView.setImage(service.getItemPicture(2));
		itemView2.setImage(service.getItemPicture(3));
		itemView3.setImage(service.getItemPicture(4));
		itemView4.setImage(service.getItemPicture(1));
		directionView.setImage(new Image("arrow.png"));
		
		// Inisialize the scroll window(map)
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		AnchorPane anchor = (AnchorPane) scrollPane.getContent();
		ObservableList<Node> nodeList =  anchor.getChildren();
		ImageView mapview = (ImageView) nodeList.get(0);
		mapview.setImage(new Image("map.png"));
		position = (ImageView) nodeList.get(1);
		position.setImage(new Image("position.png"));
		
		
		//Set direction visiable
		if (service.getPanoPicture(axisX, axisY+1) == null){
	    	buttonViewUp.setVisible(false);
	    }
		if (service.getPanoPicture(axisX, axisY-1) == null){
	    	buttonViewDown.setVisible(false);
	    }
		if (service.getPanoPicture(axisX-1, axisY) == null){
	    	buttonViewLeft.setVisible(false);
	    }
		if (service.getPanoPicture(axisX+1, axisY) == null){
	    	buttonViewRight.setVisible(false);
	    }
		
	}
	
	// Create a 3D picture
	public SubScene createSubScene() throws Exception{
		perCamera = new PerspectiveCamera(true);
	    perCamera.setNearClip(0.1);
	    perCamera.setFarClip(8000.0);
	    perCamera.setFieldOfView(70);
	    sphereView = new Sphere(600);
	    sphereView.setCullFace(CullFace.NONE);
		PhongMaterial material = new PhongMaterial();
	    //material.setDiffuseMap(new Image(getClass().getResource("hill.jpg").toExternalForm()));
		Image image = service.getPanoPicture(axisX, axisY);
		//material.setDiffuseMap(picture.getPanoPicture(axisX, axisY));
		material.setDiffuseMap(image);
	    sphereView.setMaterial(material);
	    perCamera.getTransforms().addAll (new Rotate(-180, Rotate.Y_AXIS));
	    Group root3D = new Group(perCamera,sphereView,new AmbientLight(Color.WHITE));

	    SubScene scene = new SubScene(root3D, 800, 400, true, SceneAntialiasing.BALANCED);
	    scene.setCamera(perCamera);
	    return scene;
	}
	
	
	/**
	 * Move in the 3D picture
	 * @param event
	 */
	public void mouseRead(MouseEvent event) {
		preX = (int) event.getSceneX();
	}
	
    public void mouseDrag(MouseEvent event) { 
    	double x = event.getX();
    	
    	// Move left
    	if (x > preX){
	    	
	    	if (Degrees.get("H") > -maxHDegree){
	    		perCamera.getTransforms().add(new Rotate(-1, Rotate.Y_AXIS));
	    		Degrees.put("H",Degrees.get("H")-1);
	    		position.setRotate(position.getRotate()-1);
	    	}
    	}
    	//Move right
    	else{
    		
	    	if (Degrees.get("H") < maxHDegree){
	    		perCamera.getTransforms().add(new Rotate(1, Rotate.Y_AXIS));
	    		Degrees.put("H",Degrees.get("H")+1);
	    		position.setRotate(position.getRotate()+1);
	    	}
	    }
     }
    
 
    
    //Change the pictures
    public void showDirection(MouseEvent event) throws Exception{
    	
    	double arrowX = event.getSceneX();
    	double arrowY = event.getSceneY();
    	directionView.setLayoutX(arrowX-10);
    	directionView.setLayoutY(arrowY-10);
    	directionView.setRotationAxis(Rotate.X_AXIS);
    	directionView.setRotate(-7.0/20.0*arrowY + 120.0);
    	directionView.setFitHeight(-0.1*arrowY + 30);
    	directionView.setFitWidth(-0.1*arrowY + 30);
    	
    }
    
    public void moveForward(ActionEvent event) throws Exception{
    	axisY += 1;
    	PhongMaterial material = new PhongMaterial();
	    material.setDiffuseMap(service.getPanoPicture(axisX, axisY));
	    sphereView.setMaterial(material);
	    position.setLayoutY(position.getLayoutY()-40);
	    scrollPane.setVvalue(scrollPane.getVvalue()-0.2);
	    
	    if (service.getPanoPicture(axisX, axisY+1) == null){
	    	buttonViewUp.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX-1, axisY) == null){
	    	buttonViewLeft.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX+1, axisY) == null){
	    	buttonViewRight.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX, axisY-1) != null){
	    	buttonViewDown.setVisible(true);
	    }
	    if (service.getPanoPicture(axisX+1, axisY) != null){
	    	buttonViewRight.setVisible(true);
	    }
	    if (service.getPanoPicture(axisX-1, axisY) != null){
	    	buttonViewLeft.setVisible(true);
	    }
	    
	    judgeItem(item,itemView);
	    judgeItem(item2,itemView2);
	    judgeItem(item3,itemView3);
	    judgeItem(item4,itemView4);
    }
    
    public void moveBackward(ActionEvent event) throws Exception{
    	axisY -= 1;
    	PhongMaterial material = new PhongMaterial();
	    material.setDiffuseMap(service.getPanoPicture(axisX, axisY));
	    position.setLayoutY(position.getLayoutY()+40);
	    sphereView.setMaterial(material);
	    scrollPane.setVvalue(scrollPane.getVvalue()+0.2);
	    
	    if (service.getPanoPicture(axisX, axisY-1) == null){
	    	buttonViewDown.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX-1, axisY) == null){
	    	buttonViewLeft.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX+1, axisY) == null){
	    	buttonViewRight.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX, axisY+1) != null){
	    	buttonViewUp.setVisible(true);
	    }
	    if (service.getPanoPicture(axisX+1, axisY) != null){
	    	buttonViewRight.setVisible(true);
	    }
	    if (service.getPanoPicture(axisX-1, axisY) != null){
	    	buttonViewLeft.setVisible(true);
	    }
	    
	    judgeItem(item,itemView);
	    judgeItem(item2,itemView2);
	    judgeItem(item3,itemView3);
	    judgeItem(item4,itemView4);
	    
    }
    public void turnLeft(ActionEvent event) throws Exception{
    	axisX -= 1;
    	PhongMaterial material = new PhongMaterial();
    	position.setLayoutX(position.getLayoutX()-40);
	    material.setDiffuseMap(service.getPanoPicture(axisX, axisY));
	    sphereView.setMaterial(material);
	    
	    
	    if (service.getPanoPicture(axisX-1, axisY) == null){
	    	buttonViewLeft.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX, axisY-1) == null){
	    	buttonViewDown.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX, axisY+1) == null){
	    	buttonViewUp.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX+1, axisY) != null){
	    	buttonViewRight.setVisible(true);
	    }
	    if (service.getPanoPicture(axisX, axisY+1) != null){
	    	buttonViewUp.setVisible(true);
	    }
	    if (service.getPanoPicture(axisX, axisY-1) != null){
	    	buttonViewDown.setVisible(true);
	    }
    }
    public void turnRight(ActionEvent event) throws Exception{
    	axisX += 1;
    	PhongMaterial material = new PhongMaterial();
	    material.setDiffuseMap(service.getPanoPicture(axisX, axisY));
	    sphereView.setMaterial(material);
	    position.setLayoutX(position.getLayoutX()+40);
	    
	    if (service.getPanoPicture(axisX+1, axisY) == null){
	    	buttonViewRight.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX, axisY-1) == null){
	    	buttonViewDown.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX, axisY+1) == null){
	    	buttonViewUp.setVisible(false);
	    }
	    if (service.getPanoPicture(axisX-1, axisY) != null){
	    	buttonViewLeft.setVisible(true);
	    }
	    if (service.getPanoPicture(axisX, axisY+1) != null){
	    	buttonViewUp.setVisible(true);
	    }
	    if (service.getPanoPicture(axisX, axisY-1) != null){
	    	buttonViewDown.setVisible(true);
	    }
    }
    
    //Play with the Items
    public void dragItem(MouseEvent event) {
    	double itemX = event.getX();
    	double itemY = event.getY();
    	
    	if (itemX > -20 ){
    		itemView.setX(0);
        	itemView.setY(0);
        	itemView.setScaleX(1);
        	itemView.setScaleY(1);
        	item = "1";
    	}
    	else{
    		itemView.setX(itemX-20);
        	itemView.setY(itemY-20);
        	itemView.setScaleX(2);
        	itemView.setScaleY(2);
        	item = Integer.toString(axisX) + Integer.toString(axisY);
    	}
    }
    public void dragItem2(MouseEvent event) {
    	double itemX = event.getX();
    	double itemY = event.getY();
    	
    	if (itemX > -20 ){
    		itemView2.setX(0);
        	itemView2.setY(0);
        	itemView2.setScaleX(1);
        	itemView2.setScaleY(1);
        	item2 = "1";
    	}
    	else{
    		itemView2.setX(itemX-20);
        	itemView2.setY(itemY-20);
        	itemView2.setScaleX(2);
        	itemView2.setScaleY(2);
        	item2 = Integer.toString(axisX) + Integer.toString(axisY);
    	}
    }
    public void dragItem3(MouseEvent event) {
    	double itemX = event.getX();
    	double itemY = event.getY();
    	
    	if (itemX > -20 ){
    		itemView3.setX(0);
        	itemView3.setY(0);
        	itemView3.setScaleX(1);
        	itemView3.setScaleY(1);
        	item3 = "1";
    	}
    	else{
    		itemView3.setX(itemX-20);
        	itemView3.setY(itemY-20);
        	itemView3.setScaleX(2);
        	itemView3.setScaleY(2);
        	item3 = Integer.toString(axisX) + Integer.toString(axisY);
    	}
    }
    public void dragItem4(MouseEvent event) {
    	double itemX = event.getX();
    	double itemY = event.getY();
    	
    	if (itemX > -20 ){
    		itemView4.setX(0);
        	itemView4.setY(0);
        	itemView4.setScaleX(1);
        	itemView4.setScaleY(1);
        	item4 = "1";
    	}
    	else{
    		itemView4.setX(itemX-20);
        	itemView4.setY(itemY-20);
        	itemView4.setScaleX(2);
        	itemView4.setScaleY(2);
        	item4 = Integer.toString(axisX) + Integer.toString(axisY);
    	}
    }
    
    public void pickAll(ActionEvent event){
    	
     	itemView.setX(0);
    	itemView.setY(0);
    	itemView.setScaleX(1);
    	itemView.setScaleY(1);
    	itemView.setVisible(true);
    	
    	itemView2.setX(0);
    	itemView2.setY(0);
    	itemView2.setScaleX(1);
    	itemView2.setScaleY(1);
    	itemView2.setVisible(true);
    	
    	itemView3.setX(0);
    	itemView3.setY(0);
    	itemView3.setScaleX(1);
    	itemView3.setScaleY(1);
    	itemView3.setVisible(true);
    	
    	itemView4.setX(0);
    	itemView4.setY(0);
    	itemView4.setScaleX(1);
    	itemView4.setScaleY(1);
    	itemView4.setVisible(true);
    }
    
    private void judgeItem(String str, ImageView im){
    	if (str != "1"){
	    	if (str.equals(Integer.toString(axisX) + Integer.toString(axisY))){
	    		im.setVisible(true);
	    	}
	    	else
	    	{
	    		im.setVisible(false);
	    	}
	    }
	    else{
	    	im.setVisible(true);
	    }
    }
    
}
