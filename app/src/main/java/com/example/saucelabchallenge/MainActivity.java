package com.example.saucelabchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void startPrintAllService(View view) {
    SolutionService.startActionPrintServices(this);
  }

  public void checkBatteryStatus(View view) {
    SolutionService.startActionCheckBatteryStatus(this);
  }
}
