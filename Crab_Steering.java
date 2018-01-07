package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Timon PC on 2018/01/06.
 */

public class Crab_Steering extends Omnidirectional_Steering {

    private int MQ = 0;//MQ=movement quadrant(0-3)
    private int[]x_pair = {1,-1,-1,1};//The FL and BR motors give same vector output, motors cross vect_x line when drawn
    private int[]y_pair = {1,1,-1,-1};//The FR and BL motors give same vector output, motors cross vect_y line when drawn


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

        if (!((!gamepad1.left_bumper && !gamepad1.right_bumper) || (gamepad1.left_bumper && gamepad1.right_bumper)))
        {bumper=true;}else {bumper=false;}

    }
    public void update_stick_status() {
        if (in_x !=0 || in_y !=0) {stick=true;}else{stick=false;}
    }

    public void update_crab_dir() {
        if (in_x != 0) {in_rad=Math.atan(in_x/in_y);}
        else if(in_y>0){in_rad=Math.PI/2;}else {in_rad=-Math.PI/2;}

        vect_rad=in_rad-Math.PI/4;

      MQ = (int)(Math.floor(vect_rad/(Math.PI/2)));

    }

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
        return (Math.sqrt(x*x+y*y)*Math.sqrt(1/(x*x+y*y)));//met sin en cos, is die grootste waarde wat hulle gaan return soms weird, so hier die moet net so bly om by 1 uit te kom
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
        update_crab_dir();
        PM = Stick_vect_size(in_x,in_y);

        DriveFrontRight.setPower(x_pair[MQ]*PM);
        DriveFrontLeft.setPower(y_pair[MQ]*PM);
        DriveBackLeft.setPower(x_pair[MQ]*PM);
        DriveBackRight.setPower(y_pair[MQ]*PM);

    }

    public void MM_Bumper() {
        set_rotation();

        DriveFrontRight.setPower(FR_rot);
        DriveFrontLeft.setPower(FL_rot);
        DriveBackLeft.setPower(BL_rot);
        DriveBackRight.setPower(BR_rot);
    }

    public void MM_StickAndBumper(){
        update_crab_dir();
        PM = Stick_vect_size(in_x,in_y);
        set_rotation();

        DriveFrontRight.setPower(0.5 * (x_pair[MQ]*PM + FR_rot));
        DriveFrontLeft.setPower(0.5 * (y_pair[MQ]*PM + FL_rot));
        DriveBackLeft.setPower(0.5 * (x_pair[MQ]*PM + BL_rot));
        DriveBackRight.setPower(0.5 * (y_pair[MQ]*PM + BR_rot));

    }

    @Override
    public void loop() {
        in_x=gamepad1.left_stick_x;
        in_y=gamepad1.left_stick_y;

        left_bumper=gamepad1.left_bumper;
        right_bumper=gamepad1.right_bumper;

        update_bumper_status();
        update_stick_status();

        if (!bumper && !stick) {MM_Null();}
        if (!bumper && stick) {MM_Stick();}
        if (bumper && !stick) {MM_Bumper();}
        if (bumper && stick) {MM_StickAndBumper();}
    }
}
