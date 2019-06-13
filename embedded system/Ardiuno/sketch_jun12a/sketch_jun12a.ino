#include<SoftwareSerial.h>
// RX TX
SoftwareSerial s(3,2);

int data;

void setup() {
    s.begin(115200);
    Serial.begin(115200);
}

void loop() {
  if (s.available()) {
      data = s.read();
      Serial.println(data);
  }
}
