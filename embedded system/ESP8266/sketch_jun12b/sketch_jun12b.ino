#include<SoftwareSerial.h> 
#include "ESP8266WiFi.h"
#include "ESP8266HTTPClient.h"
// RX TX
SoftwareSerial s(0,2);

const char* ssid = "bsnl279079"; 
const char* password = "happyislucky"; 

const String web_link = "https://nomadic-bedrock-229208.appspot.com/head/";

bool connection = false;

void setup() {
  Serial.begin(115200);
    Serial.println(WiFi.localIP());// Print the IP address
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED){
          delay(1); // required to connect donot know why
          Serial.println("connecting");
    }
    if (WiFi.status() == WL_CONNECTED) {
       Serial.println("WiFi connected");
       connection = true;
    }
    Serial.println(WiFi.localIP());// Print the IP address
  s.begin(115200);
}

int old_sig = 5;

void loop() {
  HTTPClient http;
  http.begin(web_link ,"6F 5C 0D B9 7C 14 82 39 D1 DF A3 4D 97 F4 2B 3F E2 BE 22 78"); 
  int httpCode = http.GET();
  String payload = http.getString();
  int sig = payload.toInt();
  Serial.print("payload : ");
  Serial.println(payload);
  Serial.println(sig);
  if (old_sig != sig) {
      s.write(sig);
      old_sig = sig;
  }
  http.end();
}
