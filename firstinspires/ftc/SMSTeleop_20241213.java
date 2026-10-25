package org.firstinspires.ftc.teamcode;
 import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.Set;
import com.qualcomm.robotcore.hardware.DcMotorEx;
 import com.qualcomm.robotcore.hardware.DcMotor;
 import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

 @TeleOp()
 public class SMSTeleop_20241213 extends LinearOpMode {
 
 //Motor and servo definitions
  DcMotor fl, fr, bl, br, arm;
     Servo wrist, claw;
     
     boolean reset;

     //Holding power to counteract gravity
     final double HOLD_POWER =0;
    final double LOW_POWER_MULTIPLIER = 0.5; // Reduces speed by 50% when in low power mode
   final double CLAW_OPEN_POSITION = 0.8;
 final double CLAW_CLOSED_POSITION = 0.4; //0.5, 0.625
 int targetPosition = 0;
 int armTarget = 0;
 double armPositionFudgeFactor;
 // double armspeed_down = 
 //PIDFController armController = new PIDFController(0.1,0.0,0.0);           


     @Override
     public void runOpMode() {
         // Initialize motors and servos
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        bl = hardwareMap.get(DcMotor.class, "bl");
         br = hardwareMap.get(DcMotor.class, "br");
         arm = hardwareMap.get(DcMotor.class, "arm");
         wrist = hardwareMap.get(Servo.class, "wrist");
         claw= hardwareMap.get(Servo.class, "claw");

// Set motor directions
fl.setDirection(DcMotorEx.Direction.REVERSE);
bl.setDirection(DcMotorEx.Direction.REVERSE);
br.setDirection(DcMotorEx.Direction.FORWARD);
fr.setDirection(DcMotorEx.Direction.FORWARD);
//arm.setDirection(DcMotorEx.Direction.REVERSE);



         // Set motors to brake when at zero power
fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


//Set up encoder mode for arm
arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

// double maxPower = Math.max(Math.abs(flPower), Math.max(Math.abs(frPower), Math.max(Math.abs(blPower), Math.abs(brPower))));
// if (maxPower > 1.0) {
//     flPower /= maxPower;
//     frPower /= maxPower;
//     blPower /= maxPower;
//     brPower /= maxPower;

final double ARM_TICKS_PER_DEGREE =
        28 // number of encoder ticks per rotation of the bare motor
                * 1 / 1 // This is the exact gear ratio of the 50.9:1 Yellow Jacket gearbox
                * 32.0 / 15.0 // This is the external gear reduction, a 15T pinion gear that drives a 32T hub-mount gear
                * 1 / 360.0; // we want ticks per degree, not per rotation
// ARM_TICKS_PER_DEGREE = 57.733;
               //arm.setPower(HOLD_POWER);
    

waitForStart();

        while (opModeIsActive()) {
         
            // Check for low power mode
double powerMultiplier = gamepad1.left_bumper ? LOW_POWER_MULTIPLIER : 1.0;

//             // Drive Code with power adjustment
double rightx = gamepad1.right_stick_x; // strafe left and right
double lefty = gamepad1.left_stick_y;   // forward and backward
double leftx = gamepad1.left_stick_x;

fl.setPower((lefty - leftx - rightx) * powerMultiplier);
fr.setPower((lefty + leftx + rightx) * powerMultiplier);
bl.setPower((lefty + leftx - rightx) * powerMultiplier);
br.setPower((lefty - leftx + rightx) * powerMultiplier);

//Arm control with presets on gamepad2

 // while(true){
 //  int jit = arm.getCurrentPosition();
 //  telemetry.addLine(jit);
 // }
//Manual control with gamepad1 D-pad
 if (gamepad2.y || gamepad1.y) { // INTAKE POSITION
  arm.setTargetPosition(-87);
  arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
  arm.setPower(1);
  telemetry.addLine("INTAKE POSITION");
  wrist.setPosition(0); // 0.6
  }
  
  if (gamepad2.b || gamepad1.b) { //SCORE POSITION
     arm.setTargetPosition(-750);
     arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
     arm.setPower(0.6);
     telemetry.addLine("SCORE POSITION");
     wrist.setPosition(0.3);
  }
  
    if (gamepad2.x || gamepad1.x) { //REST POSITION
     arm.setTargetPosition(-45);
     arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
     sleep(800);
     arm.setPower(.1);
     sleep(300);
     arm.setPower(.12);
     arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
     if(arm.getCurrentPosition() <-25){
      arm.setPower(0.3); 
        telemetry.addLine("REST POSITION");
     }else {
     arm.setPower(0);
    }
    wrist.setPosition(0.3);
    }
  
   if (gamepad2.a || gamepad2.a) { //Level 1 ascent
     arm.setTargetPosition(-380);
     arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       arm.setPower(0.7);
     
     telemetry.addLine("Level 1 ascent");
    }
  if (gamepad2.left_bumper || gamepad1.left_bumper){
   claw.setPosition(CLAW_OPEN_POSITION);
   telemetry.addLine("left bumper");
  }
  
  if (gamepad2.right_bumper || gamepad1.left_bumper){
   claw.setPosition(CLAW_CLOSED_POSITION);
   telemetry.addLine("right bumper");
  }
  
  if (gamepad1.dpad_up || gamepad2.dpad_up){
   arm.setTargetPosition(arm.getCurrentPosition()+50);
  }
  if (gamepad1.dpad_down || gamepad2.dpad_down){
   arm.setTargetPosition(arm.getCurrentPosition()-50);
  }
  if(gamepad2.dpad_left){
     arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
   // arm.setTargetPosition(0);
   // reset = true;
   
  }
  // if (reset && !gamepad2.dpad_left){
  //   arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
  //   arm.setPower(-0.99);
  //   arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
  //   reset = false;
  // }
// arm.setPower(targetPosition2);
// arm.setPower(0.15); // Move up
// //((DcMotorEx) arm).setVelocity(2100);
//  arm.setPower(HOLD_POWER);
// targetPosition = arm.getCurrentPosition(); // Update target position
// telemetry.addLine("Manual Move Up");
                
            // else if (gamepad2.a) {
            //     arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            //     arm.setPower(-0.15); // Move down
            //      arm.setPower(HOLD_POWER);
            //     targetPosition = arm.getCurrentPosition(); // Update target position
            //      telemetry.addLine("Manual Move Down");
                 
         // }
 
 
            // Wrist control
            //  if (gamepad2.dpad_left){
            //    wrist.setPosition(0.5);
            //  }
            //  if (gamepad2.dpad_right) wrist.setPosition(0.7);//specimen scoring position
            //  if (gamepad2.dpad_down) wrist.setPosition(0.3);//position for the grabbing
            // if (gamepad2.dpad_up) wrist.setPosition(0.8);
 
                
 
 
             // Update telemetry
             telemetry.addData("Arm Position", arm.getCurrentPosition());
             telemetry.addData("Target Position", targetPosition);
             telemetry.addData("Low Power Mode", gamepad1.left_bumper ? "Enabled" : "Disabled");
             telemetry.addData("Status", "Running");
             telemetry.addData("fl", fl.getCurrentPosition());
             telemetry.addData("fr", fr.getCurrentPosition());
             telemetry.addData("bl", bl.getCurrentPosition());
             telemetry.addData("br", br.getCurrentPosition());
          telemetry.update();
             

             // telemetry.addData("servo", claw.getPosition());
             // while(true){
             //  sleep(1000);
             // }

             //intakePower = -0.35;
             
           //intake code
        
       
// // arm.setTargetPosition(position)
// 
// //     // Method to move arm to a specific position using encoder
// //     public void moveArmToPosition(int position) {
// //         arm.setTargetPosition(position);
// //         arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
// //         arm.setPower(0.3);  // Set arm power to move to the target position
// //         while (arm.isBusy() && opModeIsActive()) {
// //             telemetry.addData("Moving to position", position);
// //             telemetry.addData("Current position", arm.getCurrentPosition());
// //             telemetry.update();
// //         } ```````````````````
// //         arm.setPower(0);
// //         arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Reset to manual mode after reaching the position
// //     }
//a rm.setTargetPosition(0);
// arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
// arm.setPower(0.5);
}
}
}