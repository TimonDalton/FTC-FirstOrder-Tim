package org.firstinspires.ftc.teamcode;

/**
 * Created by Timon PC on 09/01/2018.
 */


public class Selector_Code {

    boolean pressed = false;
    boolean left_selected = true;
    boolean right_selected = false;

    void Select(){

        //Select left setting
        if (!pressed&&gamepad1.dpad_left) {
            if (!left_selected) {left_selected = true;}
            pressed = true;
        }else if(!gamepad1.dpad_left&&!gamepad1.dpad_right){pressed = false;}
        //Select right setting
        if (!pressed&&gamepad1.dpad_right) {
            if (!right_selected) {right_selected = true;}
            pressed = true;
        }else if(!gamepad1.dpad_left&&!gamepad1.dpad_right){pressed = false;}

    }

}

//use if(){} with above mentioned left/right var to run specified code in loop
