package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp

public class TeleOp extends OpMode {

    DcMotor DriveFrontLeft;
    DcMotor DriveFrontRight;
    DcMotor DriveBackLeft;
    DcMotor DriveBackRight;

    boolean toggle = false;
    boolean pressed = false;


    Omnidirectional_Steering Omni = new Omnidirectional_Steering();
    Crab_Steering Crab = new Crab_Steering();

    public void init() {
        DriveFrontRight = hardwareMap.dcMotor.get("DriveFrontRight");
        DriveFrontLeft = hardwareMap.dcMotor.get("DriveFrontLeft");
        DriveBackLeft = hardwareMap.dcMotor.get("DriveBackLeft");
        DriveBackRight = hardwareMap.dcMotor.get("DriveBackRight");
        DriveFrontRight.setDirection(DcMotor.Direction.REVERSE);
        DriveBackRight.setDirection(DcMotor.Direction.REVERSE);
        DriveFrontLeft.setPower(0);
        DriveFrontRight.setPower(0);
        DriveBackLeft.setPower(0);
        DriveBackRight.setPower(0);
    }


    @Override
    public void loop() {
        while (!toggle){Crab.loop();}
        while (toggle){Omni.loop();}
    //below code stops constant spamming of toggle
        if (!pressed&&gamepad1.left_stick_button) {
            if (toggle) {toggle = false;} else {toggle = true;}
            pressed = true;
        }else if(!gamepad1.left_stick_button){pressed = false;}

    }
}


