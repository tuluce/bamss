set -e
export REDIS_URL="redis://h:p01ca6a87847af99192832aac38981599e7e7abab79f62cd663c0fa50484aa75f@ec2-54-229-183-122.eu-west-1.compute.amazonaws.com:13899"
./gradlew build
java -jar build/libs/bamss-keygen-0.0.1-SNAPSHOT.jar
