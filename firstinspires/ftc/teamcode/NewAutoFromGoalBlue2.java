package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Autoblue2", group = "Match")
public class NewAutoFromGoalBlue2 extends LinearOpMode {  

    // Drive Motors
    private DcMotorEx leftFront, rightFront, leftBack, rightBack;

    // Mechanisms
    private DcMotorEx intake, shooterL, shooterR, transfer;
    private Servo blocker;

    // Constants
    final double UNBLOCK = 0.20;
    final double BLOCK   = 0.00;

    // Encoder constants
    static final double TICKS_PER_REV     = 537.7;   // GoBilda 312 RPM
    static final double WHEEL_DIAMETER_MM = 96;      // 96mm mecanum
    static final double MM_PER_INCH       = 25.4;
    static final double WHEEL_CIRCUM      = WHEEL_DIAMETER_MM * Math.PI;
    static final double TICKS_PER_INCH    = TICKS_PER_REV / (WHEEL_CIRCUM / MM_PER_INCH);

    //@Override
    public void runOpMode() {
        ini();

        telemetry.addLine("Encoder Auto READY");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {

            // Drive forward 24 inches
            driveInches(2, 0.6);

            //  Turn right 45 degrees (adjust as needed)
            turnDegreesRight(45, 0.5);

            //  Shoot 3 artifacts
            shoot3();

            //  Drive forward 18 inches
            driveInches(6, 0.6);

            brake();
        }
    }

    // ------------------------------------------------
    //  ENCODER DRIVE
    // ------------------------------------------------
    void driveInches(double inches, double power) {
        int move = (int)(inches * TICKS_PER_INCH);

        leftFront.setTargetPosition(leftFront.getCurrentPosition() + move);
        leftBack.setTargetPosition(leftBack.getCurrentPosition() + move);
        rightFront.setTargetPosition(rightFront.getCurrentPosition() + move);
        rightBack.setTargetPosition(rightBack.getCurrentPosition() + move);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while (opModeIsActive() &&
              (leftFront.isBusy() || leftBack.isBusy() ||
               rightFront.isBusy() || rightBack.isBusy())) {
            idle();
        }

        brake();
        resetDriveEncoders();
    }

    // ------------------------------------------------
    //  ENCODER TURN RIGHT
    // ------------------------------------------------
    void turnDegreesRight(double degrees, double power) {

        // Approx. conversion for a 18" wheelbase robot
        double inches = degrees / 10.0;   // <-- adjust to tune turn accuracy

        int move = (int)(inches * TICKS_PER_INCH);

        // Right turn = left side forward, right side backward
        leftFront.setTargetPosition(leftFront.getCurrentPosition() + move);
        leftBack.setTargetPosition(leftBack.getCurrentPosition() + move);
        rightFront.setTargetPosition(rightFront.getCurrentPosition() - move);
        rightBack.setTargetPosition(rightBack.getCurrentPosition() - move);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while (opModeIsActive() &&
              (leftFront.isBusy() || leftBack.isBusy() ||
               rightFront.isBusy() || rightBack.isBusy())) {
            idle();
        }

        brake();
        resetDriveEncoders();
    }

    // ------------------------------------------------
    //  SHOOT 3 ARTIFACTS
    // ------------------------------------------------
    void shoot3() {

        shooterL.setPower(1);
        shooterR.setPower(1);
        sleep(600); // spin up

        for (int i = 0; i < 3; i++) {
            blocker.setPosition(UNBLOCK);
            transfer.setPower(1);
            intake.setPower(1);
            sleep(230);

            blocker.setPosition(BLOCK);
            sleep(200);
        }

        shooterL.setPower(0);
        shooterR.setPower(0);
        intake.setPower(0);
        transfer.setPower(0);
    }

    // ------------------------------------------------
    // UTILS
    // ------------------------------------------------
    void brake() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    void resetDriveEncoders() {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // ------------------------------------------------
    // INITIALIZATION
    // ------------------------------------------------
    public void ini() {

        leftFront  = hardwareMap.get(DcMotorEx.class, "motorLeftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "motorRightFront");
        leftBack   = hardwareMap.get(DcMotorEx.class, "motorLeftBack");
        rightBack  = hardwareMap.get(DcMotorEx.class, "motorRightBack");

        intake   = hardwareMap.get(DcMotorEx.class, "motorIntake");
        transfer = hardwareMap.get(DcMotorEx.class, "Transfermotor");
        shooterL = hardwareMap.get(DcMotorEx.class, "motorTurretLeft");
        shooterR = hardwareMap.get(DcMotorEx.class, "motorTurretRight");

        blocker = hardwareMap.get(Servo.class, "servoHold");
        blocker.setPosition(BLOCK);

        // Directions
        leftFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightBack.setDirection(DcMotorEx.Direction.REVERSE);

        shooterL.setDirection(DcMotorEx.Direction.FORWARD);
        shooterR.setDirection(DcMotorEx.Direction.REVERSE);

        // Behavior
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        resetDriveEncoders();
    }
}
