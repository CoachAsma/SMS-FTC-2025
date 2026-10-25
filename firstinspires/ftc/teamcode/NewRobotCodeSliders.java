package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Slider Preset Control", group = "Linear Opmode")
public class NewRobotCodeSliders extends LinearOpMode {

    // Declare hardware components
 DcMotor sliderMotor;

    // Preset positions for the slider (encoder counter)
    private static final int PRESET_MID = -50;
    private static final int PRESET_HIGH = 2000;

    @Override
    public void runOpMode() {
        // Initialize hardware
        sliderMotor = hardwareMap.get(DcMotor.class, "slider_motor");

        // Set motor direction and zero power behavior
        sliderMotor.setDirection(DcMotor.Direction.FORWARD);
        sliderMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reset and initialize encoder
        sliderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Check for button presses to move to preset positions
         if (gamepad2.y || gamepad1.y) { // INTAKE POSITION
  sliderMotor.setTargetPosition(10);
  sleep(500);
  sliderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
  sliderMotor.setPower(0.5);
  telemetry.addLine("intake position");
  }else if (gamepad1.b) {
                moveToPosition(PRESET_MID);
  }
            // Telemetry for debugging
            telemetry.addData("Slider Position", sliderMotor.getCurrentPosition());
            telemetry.update();
        }

        // Stop the motor at the end
        sliderMotor.setPower(0);
    }

    // Method to move the slider to a specified position
    private void moveToPosition(int targetPosition) {
        sliderMotor.setTargetPosition(targetPosition);
        sliderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sliderMotor.setPower(0.5); // Adjust speed as needed

        while (opModeIsActive() && sliderMotor.isBusy()) {
            // Wait until the motor reaches the target position
            telemetry.addData("Target Position", targetPosition);
            telemetry.addData("Current Position", sliderMotor.getCurrentPosition());
            telemetry.update();
        }

        // Stop the motor and reset to run using encoder mode
        sliderMotor.setPower(0);
        sliderMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}