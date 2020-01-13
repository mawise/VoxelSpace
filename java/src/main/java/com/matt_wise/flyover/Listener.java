package com.matt_wise.flyover;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicInteger;

public class Listener implements KeyListener {
    private AtomicInteger speed = new AtomicInteger(0); //manipulated by 'w'/'s' keys
    private AtomicInteger rotation = new AtomicInteger(0);//manipulated by 'a'/'d' keys

    public int getSpeed(){
        return speed.get();
    }

    public double getRotation(){
        return rotation.get();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W){
            speed.set(1);
        } else if (keyCode == KeyEvent.VK_S){
            speed.set(-1);
        } else if (keyCode == KeyEvent.VK_A){
            rotation.set(1);
        } else if (keyCode == KeyEvent.VK_D){
            rotation.set(-1);
        } else {
            //System.out.println("Unrecognized key: '" + pressedKey + "'");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W){
            speed.set(0);
        } else if (keyCode == KeyEvent.VK_S){
            speed.set(0);
        } else if (keyCode == KeyEvent.VK_A){
            rotation.set(0);
        } else if (keyCode == KeyEvent.VK_D) {
            speed.set(0);
        } else {
            //System.out.println("Unrecognized key: '" + pressedKey + "'");
        }
    }
}
