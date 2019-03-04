// This file is automatically generated by the Open Roberta Lab.
#undef max
#undef min
#define _SENSEBOX_INCLUDES
#include <NEPODefs.h>
#include "SenseBoxMCU.h"
#include <SPI.h>
#include <SD.h>
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <senseBoxIO.h>
    
unsigned long _time = millis();

double datum;
int _led_R2 = 8;
BMX055 _bmx055_A;

int _getValueFromBmx(int axis, int mode) {
    int _x_axis;
    int _y_axis;
    int _z_axis;
    switch (mode) {
        case 1:
            _bmx055_A.getAcceleration(&_x_axis, &_y_axis, &_z_axis);
            break;
        case 2:
            _bmx055_A.getRotation(&_x_axis, &_y_axis, &_z_axis);
            break;
        case 3:
            _bmx055_A.getMagnet(&_x_axis, &_y_axis, &_z_axis);
            break;
    }
    switch (axis) {
        case 1:
            return _x_axis;
        case 2:
            return _y_axis;
        case 3:
            return _z_axis;
    }
}

File _dataFile;
char* _expression = "111111111111111111111112";
#define OLED_RESET 4
Adafruit_SSD1306 _display_L(OLED_RESET);
int _led_R1 = 7;

void setup()
{
    Serial.begin(9600); 
    pinMode(_led_R2, OUTPUT);
    _bmx055_A.begin();
    SD.begin(28);
    _dataFile = SD.open("test.txt", FILE_WRITE);
    _dataFile.close();
    senseBoxIO.powerI2C(true);
    delay(2000);
    _display_L.begin(SSD1306_SWITCHCAPVCC, 0x3D);
    _display_L.display();
    delay(100);
    _display_L.clearDisplay();
    pinMode(_led_R1, OUTPUT);
    datum = 0;
}

void loop()
{
    datum = sqrt(_randomIntegerInRange(1, 100));
    _dataFile = SD.open("test.txt", FILE_WRITE);
    _dataFile.print(_expression);
    _dataFile.print(" : ");
    _dataFile.println(datum);
    _dataFile.close();
    _display_L.setCursor(0, 0);
    _display_L.setTextSize(1);
    _display_L.setTextColor(WHITE, BLACK);
    _display_L.println("written to file: ");
    _display_L.display();
    
    _display_L.setCursor(0, 5);
    _display_L.setTextSize(1);
    _display_L.setTextColor(WHITE, BLACK);
    _display_L.println(datum);
    _display_L.display();
    
    _display_L.clearDisplay();
}