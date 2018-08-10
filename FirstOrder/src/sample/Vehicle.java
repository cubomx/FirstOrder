package sample;

import javafx.scene.image.Image;


public class Vehicle extends Unit implements IAssignedPilot{
    private String asignToPilot;
    Vehicle(String name, Image image, String status, String asignToPilot) {
        super(name, image, status);
        setAssignToPilot(asignToPilot);
    }

    public String getAsignToPilot(){
        return this.asignToPilot;
    }

    @Override
    public void setAssignToPilot(String asignToPilot) {
        this.asignToPilot = asignToPilot;
    }

}
