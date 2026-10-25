/*
Copyright 2025 FIRST Tech Challenge Team 27468

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains a minimal example of a Linear "OpMode". An OpMode is a 'program' that runs
 * in either the autonomous or the TeleOp period of an FTC match. The names of OpModes appear on
 * the menu of the FTC Driver Station. When an selection is made from the menu, the corresponding
 * OpMode class is instantiated on the Robot Controller and executed.
 *
 * Remove the @Disabled annotation on the next line or two (if present) to add this OpMode to the
 * Driver Station OpMode list, or add a @Disabled annotation to prevent this OpMode from being
 * added to the Driver Station.
 */
@Autonomous(name = "motortest", group = "Match")
public class motortest extends LinearOpMode {

     private DcMotorEx leftFront = null;
    private DcMotorEx rightFront = null;
    private DcMotorEx leftBack = null;
    private DcMotorEx rightBack = null;

    // Mechanism Motors
    private DcMotorEx intake = null;
    private DcMotorEx shooterL= null; // Using DcMotorEx to get velocity
    private DcMotorEx shooterR = null;
    //private DcMotorEx motorLift = null;
    private DcMotorEx transfer= null;
    private Servo blocker = null;
    private Servo targetfound = null;

    private com.qualcomm.hardware.limelightvision.Limelight3A limelight;
    // Servos
    //private Servo servoHold = null;
    final double UNBLOCK = 0.2;
    final double BLOCK = 0;
   // static final double CLIMB_POWER = 1.0;

    @Override
    public void runOpMode() {
        ini();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        telemetry.addData("velocity L", shooterL.getVelocity());
            telemetry.addData("velocity R", shooterR.getVelocity());
            telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        if(opModeIsActive()){
          // wheels(0.9,0.5);
      /*    intake(3.5,-1);
            transfer(3.5,-1);
            //shootNew(1500);
          */
//sleep(1000);
           targetfound.setPosition(0.5);

        }
        // run until the end of the match (driver presses STOP)
    }
    void autoAlign(){
         LLResult result = limelight.getLatestResult();
            double ta = 0, tx = 0, ty = 0 ;
            boolean hasTarget = result != null && result.isValid();
            if (hasTarget) {
                tx = result.getTx();
            }
            int dir = 1;
            if(tx < 0) dir = -1;
            while(Math.abs(tx) > 3){
            leftFront.setPower(0.2*dir);
            leftBack.setPower(0.2*dir);
            rightFront.setPower(-0.2*dir);
            rightBack.setPower(-0.2*dir);
             result = limelight.getLatestResult();
             hasTarget = result != null && result.isValid();
            if (hasTarget) {
                tx = result.getTx();
            }
            }
            brake();
    }
    void shoot(int vel){
        transfer.setPower(1);
        intake.setPower(1);
        sleep(500);
        while(shooterR.getVelocity() < vel){
            shooterL.setPower(1);
            shooterR.setPower(1);
        }
        while(shooterR.getVelocity() > vel){
            shooterL.setPower(0);
            shooterR.setPower(0);
        }  
        transfer.setPower(1);
        intake.setPower(1);
        transfer.setPower(-1);
        sleep(100);
        blocker.setPosition(UNBLOCK);
        transfer.setPower(1);
        sleep(200);
        blocker.setPosition(BLOCK);
        brake();
    }
    void wheels (double seconds,double dr){
            leftFront.setPower(dr);
            leftBack.setPower(dr);
            rightFront.setPower(dr);
            rightBack.setPower(dr);
            sleep((int)(seconds * 1000));
            brake();
    }
    void intake(double seconds,double dr){
          intake.setPower(dr); 
         /* transfer.setPower(dr);
            sleep((int)(seconds * 1000));
            brake();*/
    }
    void transfer(double seconds,double dr){
          //intake.setPower(dr); 
          transfer.setPower(dr);
            sleep((int)(seconds * 1000));
            brake();
    }
      void recieve(double seconds,double dr){
            leftFront.setPower(-dr);
            leftBack.setPower(-dr);
            rightFront.setPower(-dr);
            rightBack.setPower(-dr);
            transfer.setPower(1);
            intake.setPower(1);
            sleep((int)(seconds * 1000));
            brake();
      }
            
    void strafe(double seconds,int dr){
          leftFront.setPower(-dr);
            leftBack.setPower(dr);
            rightFront.setPower(dr);
            rightBack.setPower(-dr);
            sleep((int)(seconds * 1000));
            brake();
    }
    void brake(){
        leftFront.setPower(0); leftBack.setPower(0);
            rightFront.setPower(0); rightBack.setPower(0);
            shooterR.setPower(0); shooterL.setPower(0);
            intake.setPower(0); transfer.setPower(0);
            sleep(500);
    }
    
     void shootNew(int vel){
        blocker.setPosition(BLOCK);
        transfer.setPower(1);
        intake.setPower(1);
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() < 3){
            double current = shooterR.getVelocity();
            if(current < vel){
                shooterL.setPower(1);
                shooterR.setPower(1);
            }
            else{
                shooterL.setPower(0);
                shooterR.setPower(0);
            }
            if(vel - current < 30){
                blocker.setPosition(UNBLOCK);
            }
        }
        transfer.setPower(0);
        shooterL.setPower(0);
        shooterR.setPower(0);
        blocker.setPosition(BLOCK);
        // intake.setPower(0);

    }
    public void ini(){
        leftFront = hardwareMap.get(DcMotorEx.class, "motorLeftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "motorRightFront");
        leftBack   = hardwareMap.get(DcMotorEx.class, "motorLeftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "motorRightBack");

        transfer   = hardwareMap.get(DcMotorEx.class, "Transfermotor");
        intake    = hardwareMap.get(DcMotorEx.class, "motorIntake");
        shooterL = hardwareMap.get(DcMotorEx.class, "motorTurretLeft"); // Mapped as DcMotorEx to read encoder
        shooterR   = hardwareMap.get(DcMotorEx.class, "motorTurretRight");
        //motorLift = hardwareMap.get(DcMotorEx.class, "motorLift");
        //servoHold = hardwareMap.get(Servo.class, "servoHold");

        // --- MOTOR DIRECTION ---
        leftFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightBack.setDirection(DcMotorEx.Direction.REVERSE);

        shooterR.setDirection(DcMotorEx.Direction.REVERSE);
        shooterL.setDirection(DcMotorEx.Direction.FORWARD);
        intake.setDirection(DcMotorEx.Direction.FORWARD);
        transfer.setDirection(DcMotorEx.Direction.FORWARD);
        //motorLift.setDirection(DcMotorEx.Direction.FORWARD);
        blocker = hardwareMap.get(Servo.class, "servoHold");
        targetfound = hardwareMap.get(Servo.class, "TargetFound");
        limelight = hardwareMap.get(com.qualcomm.hardware.limelightvision.Limelight3A.class, "limelight");
        limelight.setPollRateHz(10);
        limelight.start();
        limelight.pipelineSwitch(0);

        // --- MOTOR BEHAVIOR ---
        // Drivetrain and Climber set to BRAKE
        leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        //motorLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Intake and Shooters set to FLOAT (Coast)
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        transfer.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        // --- ENCODER SETUP FOR SHOOTER ---
        shooterL.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        shooterR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // Set other motors to run without encoders
        leftFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        intake.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterR.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shooterL.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        //motorLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        blocker.setDirection(Servo.Direction.FORWARD);
            targetfound.setDirection(Servo.Direction.FORWARD);
       blocker.setPosition(0.3);
               targetfound.setPosition(0);
               

    }
}
