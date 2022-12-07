#include "Arduino.h"

#define echoPin 26 // GPIO 26 of Esp32
#define trigPin 25 // Trigger a 12-as lábhoz
#define LEDPin 2 // Arduino beépített led 13-as láb

double maximumRange = 200.0; // max distance to measure
double minimumRange = 0.0; // min distance to measure

// determines how big of a percentage difference from the 
//previous distance is tolerable
double normalLowerBound = 75.0; 
double normalUpperBound = 125.0;
// for starting value set the middle of the measuring range
double prevDisctance = (minimumRange + maximumRange) / 2;

int bufferSize = 20;
int distanceBound = 5;
double buffer[20];

long duration = 0;
double distance = 0; //measured distance
double filteredDistance = 0; // filtered distance 
double finalDistance = 0; // average of filtered values between bounds
/*
measures the distance between the HC-SR04 sensor and the 
object in front
*/
double measureDistance() {
  // clearing the trigPin (Low) for 2us
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // generate ultraSound => set trigPin (Hih) for 10 us then sets back
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  // readin travel time of ultrasound
  // measures the time between low ->high pin mode
  // from that calculates the distance knowing the speed of sound wave (340 m/s)
  duration = pulseIn(echoPin, HIGH);
  return static_cast < double > (duration) * 0.034 / 2;
  
}
/*
if measured distance is outside of max and minRange sets
distance to the nearest limit
*/
void validateDistance() {
  distance = distance > maximumRange ? maximumRange : distance;
  distance = distance < minimumRange ? minimumRange : distance;
}
/*
based on deviation % calculates the filtered distance
if deviation % between normal ranges nothing happens,
othervise round up / down the number based on deviation % 
*/
void calcDistanceFilter(double prevDistance, double deviationPrc) {
  if (deviationPrc >= normalLowerBound) {
    if (deviationPrc <= normalUpperBound) {
      filteredDistance = distance;
    } else {
      filteredDistance = distance * ((prevDistance / distance) + 0.25);
    }
  } else {
    filteredDistance = distance / ((distance / prevDistance) + 0.25);
  }
}
/*
returnd the filtered data to be stored in the buffer
calculates deviation % based on previous measured distance
with this transient faulty errors consequences can be minimized even more
sets current distance to previous one
*/
double saveDistance() {
  distance = measureDistance();
  validateDistance();
  double deviationPrc = distance / prevDisctance * 100;
  calcDistanceFilter(prevDisctance, deviationPrc);
  prevDisctance = distance;
  return filteredDistance;
}

// Swaps the contents the contents of two pointers
void swap(double * a, double * b) {
  int tmp = * a;
  * a = * b;
  * b = tmp;
}

// Create a sub-array for elements "<=" and ">" the pivot
// Takes an array, left index, and pivot index as arguments
int partition(double * array, int left, int pivot) {
  // Start with a sub-array that is empty
  int low = left - 1;

  // Compare all numbers before the pivot
  int high = pivot - 1;

  // Go over all elements in sub-array
  for (int j = left; j <= high; j++) {
    // Does this element go in the "<=" sub-array?
    if (array[j] <= array[pivot]) {
      // Move over marker of sub-array
      low++;

      // Swap the element into position
      swap( & array[low], & array[j]);
    }
  }
  // Move the pivot into the correct position
  swap( & array[low + 1], & array[pivot]);

  // Return the index of the element in the correct place
  return low + 1;
}

// Recursive function that partitions the array into "<="/">"
// sub-arrays, and calls quicksort on them
void quicksort(double * array, int left, int pivot) {
  // Recursively called until only a single element left
  if (left < pivot) {
    // Partition the array into "<="/">" sub-arrays
    int new_pivot = partition(array, left, pivot);

    // Sort the sub-arrays via recursive calls
    // Sort the "<=" sub-array
    quicksort(array, left, new_pivot - 1);
    // Sort the ">" sub-array
    quicksort(array, new_pivot + 1, pivot);
  }
}
/*
sorts the buffer, so lower and upperbound noises can be distinguised
*/
void sortBuffer() {
  quicksort(buffer, 0, bufferSize - 1);
}

/*
sums up the numbers outside of given bounds, then averages it
lower and upperbound distanes in the buffer might be noises so for 
sensor faulty filtering purposes we do not include them in the average
hence making it more precise
([5]-[15] elements are good)
*/
void calcFinalDistance() {
  double sum = 0;
  for (int sample = distanceBound; sample < bufferSize - distanceBound; sample++) {
    sum += buffer[sample];
  }
  finalDistance = sum / (bufferSize - (2 * distanceBound));
}
/*
sends distance data through serial port for other SW components
*/
void sendDistance() {
  if (finalDistance >= maximumRange || finalDistance <= minimumRange) {
    // turns on the pin, indicating that finalDistance is not in range
    digitalWrite(LEDPin, HIGH);
  }
//   Serial.print("Final distance: ");
//   Serial.print(finalDistance);
//   Serial.print("  Buffer: ");
//   for (size_t i = 0; i < bufferSize; i++) {
//     Serial.print(buffer[i]);
//     Serial.print(" ");
//   }
//   Serial.print("\n");
    Serial.println(finalDistance);
}

void setup() {
  Serial.begin(9600); // setting bitrate of serial port
  // trigPin, echoPin required for operating the HC-SR04 distance sensor
  pinMode(trigPin, OUTPUT); // pin for sending out the ultrasound wave
  pinMode(echoPin, INPUT); // pin for reveiving the ultrasound wave
  pinMode(LEDPin, OUTPUT); // pin for LED signaling out of bounds distance
  // needed to let the microcontroller and development inicialize
  delayMicroseconds(200000);
}

void loop() {

  // Filling the buffer, needed to calculate accurate distance
  for (int sample = 0; sample < bufferSize; sample++) {
    buffer[sample] = saveDistance();
    delayMicroseconds(5000);
  }
  sortBuffer();
  calcFinalDistance();
  sendDistance();
  digitalWrite(LEDPin, LOW);
}