package sample;

import javafx.scene.image.Image;

public abstract class Unit {
    private String name;
    private Image image;
    private String status;

    public Unit(String name, Image image, String status){
        setName(name);
        setImage(image);
        setStatus(status);
    }

    public void setName(String name){
        this.name = name;
    }
    public void setImage(Image image){
        this.image = image;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public Image getImage(){
        return image;
    }
    public String getName(){
        return name;
    }
   public  String getStatus(){
        return status;
    }
}
