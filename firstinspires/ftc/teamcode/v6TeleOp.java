/*
 * MIT License
 *
 * Copyright (c) 2024 ParkCircus Productions; All Rights Reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.Match.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.VoltageSensor; // Use ControlHub for easy access since SDK 8.0+



// IMU specific imports

// AprilTag specific imports


/**
 * {@link TeleOpPreviewEvent} is a basic TeleOp OpMode for the DECODE 2025-2026 season.
 * This OpMode implements Field-Centric Mecanum drive and auxiliary controls, including a
 * quadratic function for fly-by launch velocity calculation.
 * Adapted from SWYFT-Itkan Ri3d demonstration code
 */
@TeleOp(name = "newCode", group = "Match")
//@Disabled
public class v6TeleOp extends LinearOpMode {


    private ElapsedTime runtime = new ElapsedTime();

    // Drive Motors
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


    // Servos
    //private Servo servoHold = null;
    final double UNBLOCK = 0.5;
    final double BLOCK = 0.8;
   // static final double CLIMB_POWER = 1.0;

    String survoStatus = "open";

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        // --- HARDWARE MAPPING ---
        ini();
        telemetry.update();

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            blocker.setPosition(0.1);
            telemetry.addLine("I am hereeee");
            telemetry.update();
            //----------------//
            // MECANUM DRIVE  //
            //----------------//
            double rx = gamepad1.right_stick_x;
            double x =  gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (-y + x +rx) / denominator;
            double backRightPower = (-y - x + rx) / denominator;

            leftFront.setPower(frontLeftPower);
           leftBack.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightBack.setPower(backRightPower);

            //----------------------------//
            // INTAKE AND SHOOTING LOGIC  //
            //----------------------------//

            if (gamepad1.right_trigger > 0.1) 
                intake.setPower(-0.75);
            else if (gamepad1.left_trigger > 0.1) 
                intake.setPower(0.75);
             else 
                intake.setPower(0);
             int targetV = 1500;
             boolean shootingPressed = false;
             if(gamepad1.x){
                 targetV = 1500;
                 shootingPressed = true;
             }
             if(gamepad1.a){
                 targetV = 1600;
                  shootingPressed = true;
             }
             if(gamepad1.b){
                targetV = 1700;
                 shootingPressed = true;
             }
             shooterL.setVelocity(targetV);
             shooterR.setVelocity(targetV);
             double error = shooterL.getVelocity() - targetV;
             if(shootingPressed && Math.abs(error) < 30){
                 blocker.setPosition(UNBLOCK);
                 transfer.setPower(1);
             }
             else{
                 blocker.setPosition(BLOCK);
             }
            //----------------- --//
            // FLYWHEEL LAUNCHER //
            //-------------------//
            

            //----------------//
            //     BASE LIFT  //
            /*/----------------//
            /if (gamepad2.dpad_up) {
                motorLift.setPower(CLIMB_POWER);
            } else if (gamepad2.dpad_down) {
                motorLift.setPower(-CLIMB_POWER);
            } else {
                motorLift.setPower(0);
            }
*/
            //----------------//
            //   TELEMETRY    //
            //----------------//
        
         
            telemetry.addData("Actual Velocity", "%.2f", shooterL.getVelocity());
            // BATTERY VOLTAGE
            telemetry.update();

            telemetry.update();
        }
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
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
        rightBack.setDirection(DcMotorEx.Direction.FORWARD);

        shooterR.setDirection(DcMotorEx.Direction.REVERSE);
        shooterL.setDirection(DcMotorEx.Direction.FORWARD);
        intake.setDirection(DcMotorEx.Direction.FORWARD);
        transfer.setDirection(DcMotorEx.Direction.FORWARD);
        //motorLift.setDirection(DcMotorEx.Direction.FORWARD);
        blocker = hardwareMap.get(Servo.class, "servoHold");


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
        shooterR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        //motorLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        blocker.setDirection(Servo.Direction.REVERSE);
        //blocker.setPosition(0.7);
    }
}