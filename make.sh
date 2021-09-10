#!/bin/bash
# ---------------------------------------------------------------------------
mkdir dist > /dev/null 2>&1
# ---------------------------------------------------------------------------
echo
echo Building huge file sorter application...
echo version 0.0.1
echo
# ---------------------------------------------------------------------------
chmod a+x ./mvnw
./mvnw clean package --show-version -Dorg.slf4j.simpleLogger.showDateTime=true "-Dorg.slf4j.simpleLogger.dateTimeFormat=yyyy-MM-dd HH:mm:ss"
if [ $? -ne 0 ]; then
  echo "Build failed. Check log above for more details."
  exit $?
fi
# ---------------------------------------------------------------------------
cp -f target/Huge-file-sorter-0.0.1-jar-with-dependencies.jar dist
cp -f conf/hfs.sh dist
chmod a+x dist/hfs.sh
rm -rf target/
# ---------------------------------------------------------------------------
echo
echo "...build was successful"
