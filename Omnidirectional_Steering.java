package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp

public class Omnidirectional_Steering extends OpMode {

    DcMotor DriveFrontLeft;
    DcMotor DriveFrontRight;
    DcMotor DriveBackLeft;
    DcMotor DriveBackRight;

    double in_rad=0;
    double vect_rad =0;
    double vect_x = 0;
    double vect_y = 0;

    boolean bumper = false;
    boolean stick = false;

    double PM=0;//power multiplier(aka speed control)


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

    public void update_stick_status() {
        if (gameapad1.leftstick_x !=0 || gameapad1.leftstick_y !=0) {stick=true;}else{stick=false;}
    }

    public void update_motor_vect() {
        if (gameapad1.leftstick_x != 0) {in_rad=Math.atan(gameapad1.leftstick_y/gameapad1.leftstick_x);}
        else if(gameapad1.leftstick_y>0){in_rad=Math.PI/2;}else {in_rad=-Math.PI/2;}

        vect_rad=in_rad-Math.PI/4;
        vect_x=Math.cos(vect_rad);
        vect_y=Math.sin(vect_rad);
    }//<3

      /*Minder compiler werk weergawe:
      if (gameapad1.leftstick_x != 0) {vect_rad=Math.atan(gameapad1.leftstick_x/gameapad1.leftstick_y)-Math.PI/4;}
         else if(gameapad1.leftstick_y>0){vect_rad=Math.PI/4}else {vect_rad=-Math.PI/(4/3)}

         vect_x=Math.cos(vect_rad);
         vect_y=Math.sin(vect_rad);
       */



    public double Stick_vect_size(double x,double y) {
        return (Math.sqrt(x*x+y*y));//met sin en cos, is die grootste waarde wat hulle gaan return soms weird, so hier die moet net so bly om by 1 uit te kom
    }

    //MM staan vir motor method
    //PM staan vir power multiplier
    public void MM_Null() {
        DriveFrontRight.setPower(0);
        DriveFrontLeft.setPower(0);
        DriveBackLeft.setPower(0);
        DriveBackRight.setPower(0);
    }

    public void MM_Stick() {
        update_motor_vect();
        PM = Stick_vect_size(gameapad1.leftstick_x,gameapad1.leftstick_y);

        DriveFrontRight.setPower(vect_x*PM);
        DriveFrontLeft.setPower(vect_y*PM);
        DriveBackLeft.setPower(vect_x*PM);
        DriveBackRight.setPower(vect_y*PM);

    }


    @Override
    public void loop() {

        update_stick_status();

        if (stick==false) {MM_Null();}
        if (stick==true) {MM_Stick();}

    }
}