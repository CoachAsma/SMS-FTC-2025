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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


// IMU specific imports

// AprilTag specific imports


/**
 * {@link TeleOpPreviewEvent} is a basic TeleOp OpMode for the DECODE 2025-2026 season.
 * This OpMode implements Field-Centric Mecanum drive and auxiliary controls, including a
 * quadratic function for fly-by launch velocity calculation.
 * Adapted from SWYFT-Itkan Ri3d demonstration code
 */
@TeleOp(name = "Preview Event TeleOp", group = "Match")
//@Disabled
public class v6MatchTeleOp extends LinearOpMode {


    private ElapsedTime runtime = new ElapsedTime();

    // Drive Motors
    private DcMotorEx motorLeftFront = null;
    private DcMotorEx motorRightFront = null;
    private DcMotorEx motorLeftBack = null;
    private DcMotorEx motorRightBack = null;

    // Mechanism Motors
    private DcMotorEx motorIntake = null;
    private DcMotorEx motorTurretLeft = null; // Using DcMotorEx to get velocity
    private DcMotorEx motorTurretRight = null;
    //private DcMotorEx motorLift = null;
    private DcMotorEx Transfermotor = null;
    private Servo servoHold = null;


    // Servos
    //private Servo servoHold = null;

    // --- State Variables for Toggles ---
    boolean flywheelOn = false;
    boolean a_button_previously_pressed = false;

    // --- CONTROL CONSTANTS ---
    // Bang-Bang Controller Constants
    static final double BANG_BANG_TARGET_VELOCITY = 1500.0; // Target speed in ticks per second
    static final double FLYWHEEL_FULL_POWER = 0.5; // Power to apply when below target speed

    // Other Constants
    static final double INTAKE_POWER = 1.0;
   // static final double CLIMB_POWER = 1.0;

    String survoStatus = "open";

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        // --- HARDWARE MAPPING ---
        motorLeftFront = hardwareMap.get(DcMotorEx.class, "motorLeftFront");
        motorRightFront = hardwareMap.get(DcMotorEx.class, "motorRightFront");
        motorLeftBack   = hardwareMap.get(DcMotorEx.class, "motorLeftBack");
        motorRightBack = hardwareMap.get(DcMotorEx.class, "motorRightBack");

        Transfermotor    = hardwareMap.get(DcMotorEx.class, "Transfermotor");
        motorIntake     = hardwareMap.get(DcMotorEx.class, "motorIntake");
        motorTurretLeft = hardwareMap.get(DcMotorEx.class, "motorTurretLeft"); // Mapped as DcMotorEx to read encoder
        motorTurretRight   = hardwareMap.get(DcMotorEx.class, "motorTurretRight");
        //motorLift = hardwareMap.get(DcMotorEx.class, "motorLift");
        //servoHold = hardwareMap.get(Servo.class, "servoHold");

        // --- MOTOR DIRECTION ---
        motorLeftFront.setDirection(DcMotorEx.Direction.FORWARD);
        motorLeftBack.setDirection(DcMotorEx.Direction.FORWARD);
        motorRightFront.setDirection(DcMotorEx.Direction.FORWARD);
        motorRightBack.setDirection(DcMotorEx.Direction.FORWARD);

        motorTurretRight.setDirection(DcMotorEx.Direction.REVERSE);
        motorTurretLeft.setDirection(DcMotorEx.Direction.FORWARD);
        motorIntake.setDirection(DcMotorEx.Direction.FORWARD);
        Transfermotor.setDirection(DcMotorEx.Direction.FORWARD);
        //motorLift.setDirection(DcMotorEx.Direction.FORWARD);
        servoHold = hardwareMap.get(Servo.class, "servoHold");


        // --- MOTOR BEHAVIOR ---
        // Drivetrain and Climber set to BRAKE
        motorLeftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorRightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorLeftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorRightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        //motorLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Intake and Shooters set to FLOAT (Coast)
        motorIntake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        motorTurretLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        motorTurretRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        Transfermotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        // --- ENCODER SETUP FOR SHOOTER ---
        motorTurretRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorTurretLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // Set other motors to run without encoders
        motorLeftFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motorRightFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motorLeftBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motorRightBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        motorIntake.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motorTurretRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motorTurretLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        //motorLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        servoHold.setDirection(Servo.Direction.FORWARD);
        servoHold.setPosition(0.7);

        telemetry.addData("Status", "Initialized. Ready to run!");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
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

            motorLeftFront.setPower(frontLeftPower);
            motorLeftBack.setPower(backLeftPower);
            motorRightFront.setPower(frontRightPower);
            motorRightBack.setPower(backRightPower);

            //----------------------------//
            // INTAKE AND SHOOTING LOGIC  //
            //----------------------------//

            if (gamepad2.x){
                 servoHold.setPosition(0);
                survoStatus = "open";
            }

            if (gamepad2.right_trigger > 0.1 && gamepad2.b) {
                motorIntake.setPower(-INTAKE_POWER);
                Transfermotor.setPower(0.5);

            } else if (gamepad2.left_trigger > 0.1) {
                motorIntake.setPower(INTAKE_POWER);
                Transfermotor.setPower(-0.5);

            } else {
                motorIntake.setPower(0);
                Transfermotor.setPower(0);

            }


            //-------------------//
            // FLYWHEEL LAUNCHER //
            //-------------------//
            if (gamepad2.a && !a_button_previously_pressed) {
                flywheelOn = !flywheelOn;
            }
            a_button_previously_pressed = gamepad2.a;

            if (flywheelOn) {
                // Get the current velocity from the encoded motor
                double currentVelocity = motorTurretLeft.getVelocity();

                // Bang-Bang Control Logic
                if (currentVelocity < BANG_BANG_TARGET_VELOCITY) {
                    // If speed is too low, turn motors to full power
                    motorTurretLeft.setPower(FLYWHEEL_FULL_POWER);
                    motorTurretRight.setPower(FLYWHEEL_FULL_POWER);
                } else {
                    // If speed is at or above target, turn motors off (coast)
                    motorTurretLeft.setPower(0);
                    motorTurretRight.setPower(0);
                }
            } else {
                motorTurretLeft.setPower(0);
                motorTurretRight.setPower(0);
            }

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
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("--- Launcher ---", "");
            telemetry.addData("Flywheel Status", flywheelOn ? "ON" : "OFF");
            telemetry.addData("Target Velocity", BANG_BANG_TARGET_VELOCITY);

            telemetry.addData("Actual Velocity", "%.2f", motorTurretLeft.getVelocity());
            telemetry.addData("Launcher Power", "%.2f", motorTurretLeft.getPower());
            // BATTERY VOLTAGE
            telemetry.update();

            telemetry.update();
        }
    }
}