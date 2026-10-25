package org.firstinspires.ftc.teamcode.M12;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class AdvancedAutonWithIMU extends LinearOpMode {
    //IMU SETUP ------------------------------------------------------------------
    IMU imu;

    public String alliance = "None";
    public String side = "None";
    public String placement = "None"; //this is the variable for which spot the robot should score
    public int waitTime = 0;
    
    private ElapsedTime runtime = new ElapsedTime();
    
    static final double COUNTS_PER_MOTOR_REV = 1100; // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0; // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES = 4.0; // For figuring circumference
    static final double COUNTS_PER_INCH = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
    (WHEEL_DIAMETER_INCHES * 3.1415)) / 3.5625;

    DcMotorEx fl, fr, bl, br, arm;
    Servo claw, wrist;

    // Constants for servo positions and movement times
    // final double CLAW_OPEN_POSITION = 0.3;
    // final double CLAW_CLOSED_POSITION = 0.05;
    // final double WRIST_POSITION_DPAD_LEFT = 0.3;
    // final double WRIST_POSITION_DPAD_UP = 0.8;
    // final double DRIVE_POWER = 0.4;
    //  final int FORWARD_TIME_MS = 450;
    // final int FORWARD1_TIME_MS = 950;
    //  final int FORWARD12_TIME_MS = 1100;
    // final int FORWARD2_TIME_MS = 800;
    // final int FORWARD3_TIME_MS = 900;
    //  final int ARM_START_POSITION = 0;
    // final int BACKUP_TIME_MS = 950;
    // final int BACKUP1_TIME_MS =1100;
    // final int STRAFE_TIME_MS = 760;
    // final int STRAFERIGHT_TIME_MS = 50;
    // final int STRAFE1_TIME_MS = 430; 
    // final int STRAFE2_TIME_MS = 450;
    // final int STRAFE3_TIME_MS = 450;
    // final int TURN_TIME_MS = 445;

    @Override
        public void runOpMode() {
            //IMU SETUP ------------------------------------------------------------------
            imu = hardwareMap.get(IMU.class, "imu");
            imu.resetYaw();

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
    
            // Set zero power behavior
            fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            
            fl.setTargetPosition(0);
            fr.setTargetPosition(0);
            bl.setTargetPosition(0);
            br.setTargetPosition(0);
            fl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            
            // // Set up encoder mode for arm
            // arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            // arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // claw.setPosition(CLAW_CLOSED_POSITION);
            // wrist.setPosition(WRIST_POSITION_DPAD_LEFT);
 
         while (!opModeIsActive() && !isStopRequested()) {
          //Running pipeline
          if (gamepad1.x) {
            alliance = "Blue";
          } else if (gamepad1.b) {
            alliance = "Red";
          }
          
          if (gamepad1.start){ //reset imu to zero
            //odo.resetPosAndIMU();
            telemetry.addData("Resetting IMU","");
            telemetry.update();
          }
          
          if (gamepad1.right_bumper) {
            side = "Basket";
          } 
          else if (gamepad1.left_bumper)
          {
            side = "Loser Zone";
          }
          
          if(gamepad1.dpad_up)
          {
            waitTime = waitTime + 1;
            sleep(500);
          }
          else if (gamepad1.dpad_down)
          {
            waitTime = waitTime - 1;
            sleep(500);
          }   
          
          telemetry.addData("--SMS 2024 Push Auto--", true);
          telemetry.addData("alliance: ", alliance);
          telemetry.addData("side", side);
          telemetry.addData("WaitTime: ", waitTime);
          //telemetry.addData("Status", odo.getDeviceStatus());
          telemetry.addData("Imu", getAngle());
          telemetry.addData("fl", fl.getCurrentPosition());
          telemetry.addData("fr", fr.getCurrentPosition());
          telemetry.addData("bl", bl.getCurrentPosition());
          telemetry.addData("br", br.getCurrentPosition());
          telemetry.update();
          telemetry.addData("Press X for Blue alliance", true);
          telemetry.addData("Press B for Red alliance", true);
          telemetry.addData("Press A for Grabbing Stack", true);
          telemetry.addData("Press Y for NOT Grabbing Stack", true);
          telemetry.update();
         }
    
        if (opModeIsActive()) {
          strafe(0.5, 24, -25.0, 4.0); //strafe right 0.5 motor power, 24 inches, -25 degrees to correct drift, timeout if stalled for more than 4 seconds
          drive(0.5, 55, -25.0,1.5); //drive forward 0.7 motor power, 45 inches, -25 degrees to counteract drift, 1.5 second timeout
          sleep(1000);
          
          strafe(0.5, 24, -25.0, 4.0); //strafe right 24 inches
          sleep(1000);
          
          drive(0.5, -60,-25,4.0); //go back 45 inches
          sleep(1000);
          
          drive(0.5,65,83,1.5); //drive in place 0 inches and turn 90 degrees** might not be 90***
          sleep(1000);
          
                    strafe(0.5, 20, -25.0, 4.0); //strafe right 24 inches
                    sleep(1000);
                             drive(0.5, -60,-25,4.0); //go back 45 inches
                              sleep(1000);
          

        }
    }
    
  public void drive(double speed, double inches, double angle, double timeoutS) {
    // Ensure that the OpMode is still active
    if (opModeIsActive()) {
      fl.setTargetPosition(fl.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH) + (int)((getAngle() + angle) * 6.5));
      fr.setTargetPosition(fr.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH) - (int)((getAngle() + angle) * 6.5));
      bl.setTargetPosition(bl.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH) + (int)((getAngle() + angle) * 6.5));
      br.setTargetPosition(br.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH) - (int)((getAngle() + angle) * 6.5));
  
      // reset the timeout time and start motion.
      runtime.reset();
  
      while (opModeIsActive() &&
        (runtime.seconds() < timeoutS) &&
        (fl.isBusy() && fr.isBusy() && bl.isBusy() && br.isBusy())) {
  
        double realSpeed = Math.min(runtime.seconds() * speed * 2, speed);
  
        fl.setPower(realSpeed);
        bl.setPower(realSpeed);
        fr.setPower(realSpeed);
        br.setPower(realSpeed);
  
        // Display it for the driver.
        telemetry.addData("Running to", " %7d :%7d", fl.getTargetPosition(), fr.getTargetPosition());
        telemetry.addData("Currently at", " at %7d :%7d", fl.getCurrentPosition(), fr.getCurrentPosition());
        telemetry.addData("Imu", getAngle());
        telemetry.update();
      }
  
      // Stop all motion;
      fl.setPower(0);
      bl.setPower(0);
      fr.setPower(0);
      br.setPower(0);
  
      sleep(300); // optional pause after each move.
    }
  }

  public void strafe(double speed, double inches, double angle, double timeoutS) {
    // Ensure that the OpMode is still active
    if (opModeIsActive()) {
  
      // Determine new target position, and pass to motor controller
      fl.setTargetPosition((fl.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH)) + (int)((getAngle() + angle) * 6.5));
      bl.setTargetPosition((bl.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH)) + (int)((getAngle() + angle) * 6.5));
      fr.setTargetPosition((fr.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH)) - (int)((getAngle() + angle) * 6.5));
      br.setTargetPosition((br.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH)) - (int)((getAngle() + angle) * 6.5));
  
      // reset the timeout time and start motion.
      runtime.reset();
      fl.setPower(speed);
      bl.setPower(speed);
      fr.setPower(speed);
      br.setPower(speed);
  
      while (opModeIsActive() &&
        (runtime.seconds() < timeoutS) &&
        (fl.isBusy() && fr.isBusy())) {
  
        // Display it for the driver.
        telemetry.addData("Running to", " %7d :%7d", fl.getTargetPosition(), fr.getTargetPosition());
        telemetry.addData("Currently at", " at %7d :%7d", fl.getCurrentPosition(), fr.getCurrentPosition());
        telemetry.addData("Imu", getAngle());
        telemetry.update();
      }
  
      // Stop all motion;
      fl.setPower(0);
      bl.setPower(0);
      fr.setPower(0);
      br.setPower(0);
  
      sleep(300); // optional pause after each move.
      
      telemetry.addData("Imu", getAngle());
      telemetry.update();
    }
  }
   public double getAngle() {
    return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
  }
}
