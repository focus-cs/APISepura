BATCH_PATH="/Users/mehdi/Documents/Focus/Projects/APISepura/dist/"
BATCH_MAIN="APISepura.jar"
SCIFORMA_URL="https://integration-sepura.sciforma-online.com/sciforma"

ROOT_DIR=$BATCH_PATH
LIB_DIR=$ROOT_DIR/lib

rm -f $LIB_DIR/utilities.jar
rm -f $LIB_DIR/PSClient.jar
rm -f $LIB_DIR/PSClient_en.jar

wget -O $LIB_DIR/utilities.jar $SCIFORMA_URL/utilities.jar
wget -O $LIB_DIR/PSClient_en.jar $SCIFORMA_URL/PSClient_en.jar
wget -O $LIB_DIR/PSClient.jar $SCIFORMA_URL/PSClient.jar

cd $ROOT_DIR

JAVA_ARGS="-showversion"
JAVA_ARGS="$JAVA_ARGS -Xms1024m"
JAVA_ARGS="$JAVA_ARGS -Xmx2048m"
JAVA_ARGS="$JAVA_ARGS -jar"
JAVA_ARGS="$JAVA_ARGS -Dtinylog.configuration=config/tinylog.properties"

java $JAVA_ARGS $BATCH_MAIN
