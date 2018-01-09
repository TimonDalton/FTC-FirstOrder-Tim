package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp

public class TeleOp extends {




    DcMotor DriveFrontLeft;
    DcMotor DriveFrontRight;
    DcMotor DriveBackLeft;
    DcMotor DriveBackRight;

    boolean toggle = false;
    boolean pressed = false;

    double in_rad=0;
    double vect_rad =0;
    double vect_x = 0;
    double vect_y = 0;

    boolean left_bumper = false;
    boolean right_bumper = false;

    double FR_rot =0;
    double FL_rot =0;
    double BL_rot =0;
    double BR_rot =0;

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

    public void update_bumper_status(){
        /* Code wat ek gebruik het vr debugging
        boolean both_true;
        boolean both_false;
        if (left_bumper == true && right_bumper == true) {both_true = true;}else{both_true = false;}
        if (left_bumper == false && right_bumper == false) {both_false = true;}else{both_false = false;}



        if (!(both_false == true || both_true == true))
        {bumper=true;}else {bumper=false;}

        */
        if (!((!gamepad1.left_bumper&& !gamepad1.right_bumper) || (gamepad1.left_bumper && gamepad1.right_bumper)))
        {bumper=true;}else {bumper=false;}

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

    public void set_rotation() {
        if (!bumper) {
            FR_rot =0;
            FL_rot =0;
            BL_rot =0;
            BR_rot =0;
        }else if(gamepad1.left_bumper) {
            FR_rot = 1;
            FL_rot = -1;
            BL_rot = -1;
            BR_rot = 1;
        }else {
            FR_rot = -1;
            FL_rot = 1;
            BL_rot = 1;
            BR_rot = -1;
        }

    }

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

    public void MM_Bumper() {
        set_rotation();

        DriveFrontRight.setPower(FR_rot);
        DriveFrontLeft.setPower(FL_rot);
        DriveBackLeft.setPower(BL_rot);
        DriveBackRight.setPower(BR_rot);
    }

    public void MM_StickAndBumper(){
        update_motor_vect();
        PM = Stick_vect_size(gameapad1.leftstick_x,gameapad1.leftstick_y);
        set_rotation();

        DriveFrontRight.setPower(0.5 * (vect_x*PM + FR_rot));
        DriveFrontLeft.setPower(0.5 * (vect_y*PM + FL_rot));
        DriveBackLeft.setPower(0.5 * (vect_x*PM + BL_rot));
        DriveBackRight.setPower(0.5 * (vect_y*PM + BR_rot));

    }

    public void Omni_loop() {

        update_bumper_status();
        update_stick_status();

        if (bumper==false && stick==false) {MM_Null();}
        if (bumper==false && stick==true) {MM_Stick();}
        if (bumper==true && stick==false) {MM_Bumper();}
        if (bumper==true && stick==true) {MM_StickAndBumper();}
    }
//Crab--->==================================================================================================

    int MQ = 0;//MQ=movement quadrant(0-3)
    int[]x_pair = {1,-1,-1,1};//The FL and BR motors give same vector output, motors cross vect_x line when drawn
    int[]y_pair = {1,1,-1,-1};//The FR and BL motors give same vector output, motors cross vect_y line when drawn


    public void update_crab_dir() {
    if (gameapad1.leftstick_x != 0) {in_rad=Math.atan(gameapad1.leftstick_x/gameapad1.leftstick_y);}
    else if(gameapad1.leftstick_y>0){in_rad=Math.PI/2;}else {in_rad=-Math.PI/2;}

    vect_rad=in_rad-Math.PI/4;

    MQ = (int)(Math.floor(vect_rad/(Math.PI/2)));


}




    public void Crab_MM_Null() {
        DriveFrontRight.setPower(0);
        DriveFrontLeft.setPower(0);
        DriveBackLeft.setPower(0);
        DriveBackRight.setPower(0);
    }

    public void Crab_MM_Stick() {
        update_crab_dir();
        PM = Stick_vect_size(gameapad1.leftstick_x,gameapad1.leftstick_y);

        DriveFrontRight.setPower(x_pair[MQ]*PM);
        DriveFrontLeft.setPower(y_pair[MQ]*PM);
        DriveBackLeft.setPower(x_pair[MQ]*PM);
        DriveBackRight.setPower(y_pair[MQ]*PM);

    }

    public void Crab_MM_Bumper() {
        set_rotation();

        DriveFrontRight.setPower(FR_rot);
        DriveFrontLeft.setPower(FL_rot);
        DriveBackLeft.setPower(BL_rot);
        DriveBackRight.setPower(BR_rot);
    }

    public void Crab_MM_StickAndBumper(){
        update_crab_dir();
        PM = Stick_vect_size(gameapad1.leftstick_x,gameapad1.leftstick_y);
        set_rotation();

        DriveFrontRight.setPower(0.5 * (x_pair[MQ]*PM + FR_rot));
        DriveFrontLeft.setPower(0.5 * (y_pair[MQ]*PM + FL_rot));
        DriveBackLeft.setPower(0.5 * (x_pair[MQ]*PM + BL_rot));
        DriveBackRight.setPower(0.5 * (y_pair[MQ]*PM + BR_rot));

    }


    public void Crab_loop() {
        gameapad1.leftstick_x=gamepad1.left_stick_x;
        gameapad1.leftstick_y=gamepad1.left_stick_y;

        left_bumper=gamepad1.left_bumper;
        right_bumper=gamepad1.right_bumper;

        update_bumper_status();
        update_stick_status();

        if (!bumper && !stick) {Crab_MM_Null();}
        if (!bumper && stick) {Crab_MM_Stick();}
        if (bumper && !stick) {Crab_MM_Bumper();}
        if (bumper && stick) {Crab_MM_StickAndBumper();}
    }
}


    @Override
    public void loop() {
        while (!toggle){Crab_loop();}
        while (toggle){Omni_loop();}
    //below code stops constant spamming of toggle
        if (!pressed&&gamepad1.left_stick_button) {
            if (toggle) {toggle = false;} else {toggle = true;}
            pressed = true;
        }else if(!gamepad1.left_stick_button){pressed = false;}

    }
}


