package org.firstinspires.ftc.teamcode.M12;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "SMSAutoBlue", group = "match")
public class SMSAutonomous_20241213 extends LinearOpMode {

    DcMotorEx fl, fr, bl, br, arm;
    Servo claw, wrist;

    // Constants for servo positions and movement times
    // final double CLAW_OPEN_POSITION = 0.3;
    // final double CLAW_CLOSED_POSITION = 0.05;
    // final double WRIST_POSITION_DPAD_LEFT = 0.3;
    //     final double WRIST_POSITION_DPAD_UP = 0.8;
    final double DRIVE_POWER = 0.5;
     final int FORWARD_TIME_MS = 450;
    final int FORWARD1_TIME_MS = 950;
     final int FORWARD12_TIME_MS = 1100;
    final int FORWARD2_TIME_MS = 800;
    final int FORWARD3_TIME_MS = 900;
     final int ARM_START_POSITION = 0;
    final int BACKUP_TIME_MS = 950;
    final int BACKUP1_TIME_MS =1100;
    final int STRAFE_TIME_MS = 650;
    final int STRAFERIGHT_TIME_MS = 50;
    final int STRAFE1_TIME_MS = 410; 
    final int STRAFE2_TIME_MS = 450;
    final int STRAFE3_TIME_MS = 450;
    final int TURN_TIME_MS = 445;

    @Override
        public void runOpMode() {
            // Initialize motors and servos
            fl = hardwareMap.get(DcMotorEx.class, "fl");
            fr = hardwareMap.get(DcMotorEx.class, "fr");
            bl = hardwareMap.get(DcMotorEx.class, "bl");
            br = hardwareMap.get(DcMotorEx.class, "br");
            arm = hardwareMap.get(DcMotorEx.class, "arm");
            claw = hardwareMap.get(Servo.class, "claw");
            wrist = hardwareMap.get(Servo.class, "wrist");
    
            // Set motor directions
            fl.setDirection(DcMotorEx.Direction.REVERSE);
            bl.setDirection(DcMotorEx.Direction.REVERSE);
            br.setDirection(DcMotorEx.Direction.FORWARD);
            fr.setDirection(DcMotorEx.Direction.FORWARD);
            // arm.setDirection(DcMotorEx.Direction.REVERSE);
    
            // Set zero power behavior
            fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    
            // // Set up encoder mode for arm
            // arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            // arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // claw.setPosition(CLAW_CLOSED_POSITION);
            // wrist.setPosition(WRIST_POSITION_DPAD_LEFT);
    
            waitForStart();
    
    
        if (opModeIsActive()) {
                // Step 1: Drive forward
                strafeRight(DRIVE_POWER,STRAFERIGHT_TIME_MS );
                sleep(500);
                
                
            if (opModeIsActive()) {
                // Step 1: Drive forward
                driveBackward(DRIVE_POWER, FORWARD_TIME_MS);
                sleep(500);
        
                // Step 4: Strafe left
                strafeLeft(DRIVE_POWER, STRAFE_TIME_MS);
                sleep(500);
      //turnRight(DRIVE_POWER * 0.3, 200); // Reduced power and short duration for a slight left turn
                
                // Step 5: Move forward
                driveBackward(DRIVE_POWER, FORWARD1_TIME_MS);
                sleep(500);
    
                // Step 5.1: Adjust alignment by turning left slightly
             
    
                // Step 6: Strafe left again
                strafeLeft(DRIVE_POWER, STRAFE1_TIME_MS);
                sleep(500);
      //turnRight(DRIVE_POWER * 0.3, 360); // Reduced power and short duration for a slight left turn
               
                // Step 7: Move forward
                driveForward(DRIVE_POWER, BACKUP1_TIME_MS);
    //turnRight(DRIVE_POWER * 0.3, 360); // Reduced power and short duration for a slight left turn
                sleep(500);
                // Step 8: Move backward
                driveBackward(DRIVE_POWER, FORWARD1_TIME_MS);
                sleep(500);
    
                // Step 9: Strafe right
             strafeLeft(DRIVE_POWER, STRAFE3_TIME_MS);
                sleep(500);
    //turnRight(DRIVE_POWER * 0.3, 360); // Reduced power and short duration for a slight left turn
              
                // Step 10: Move forward
                driveForward(DRIVE_POWER, BACKUP1_TIME_MS);
                sleep(500);
    
                // Step 11: Move backward
                driveBackward(DRIVE_POWER, FORWARD12_TIME_MS);
                sleep(500);
    
                // Step 12: Strafe right
                strafeLeft(DRIVE_POWER, STRAFE1_TIME_MS);
                sleep(500);
    //turnRight(DRIVE_POWER * 0.3, 360); // Reduced power and short duration for a slight left turn
               
                // Step 13: Move forward
                driveForward(DRIVE_POWER, BACKUP1_TIME_MS);
                sleep(500);
    
                // Step 14: Move backward
                driveBackward(DRIVE_POWER, FORWARD3_TIME_MS);
                sleep(500);
    
                // Step 15: Turn right
                turnLeft(DRIVE_POWER, TURN_TIME_MS);
                sleep(500);
    
    
                // Step 17: Final forward movement
                driveBackward(DRIVE_POWER, FORWARD2_TIME_MS);
    
                // Stop all motors at the end
                stopMotors();
            }
        }
    }
    // Method to drive forward for a specified time
    private void driveForward(double power, int timeMs) {
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
        sleep(timeMs); // Run motors for the specified time
        stopMotors();
    }

    // Method to drive backward for a specified time
    private void driveBackward(double power, int timeMs) {
        fl.setPower(-power);
        fr.setPower(-power);
        bl.setPower(-power);
        br.setPower(-power);
        sleep(timeMs); // Run motors for the specified time
        stopMotors();
    }

    // Method to strafe right for a specified time
    private void strafeLeft(double power, int timeMs) {
        fl.setPower(-power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(-power);
        sleep(timeMs); // Run motors for the specified time
        stopMotors();
    }
    
        // Method to strafe right for a specified time
    private void strafeRight(double power, int timeMs) {
        fl.setPower(power);
        fr.setPower(-power);
        bl.setPower(-power);
        br.setPower(power);
        sleep(timeMs); // Run motors for the specified time
        stopMotors();
    }

    // Method to turn right for a specified time
    private void turnLeft(double power, int timeMs) {
        fl.setPower(power);
        fr.setPower(-power);
        bl.setPower(power);
        br.setPower(-power);
        sleep(timeMs); // Run motors for the specified time
        stopMotors();
    }



    // Method to stop all motors
    private void stopMotors() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }
}
