#!/usr/bin/env bash
# Download the jars this capstone needs into lib/ (one time, ~7 MB). Needs curl or wget.
# Re-running is safe: jars already present are skipped.
cd "$(dirname "$0")"
mkdir -p lib
get () {   # get <url> <file>
  if [ -s "lib/$2" ]; then echo "  have   $2"; return 0; fi
  echo "  fetch  $2"
  if command -v curl >/dev/null 2>&1; then curl -fL --retry 3 -o "lib/$2" "$1"
  else wget -q -O "lib/$2" "$1"; fi || { echo "FAILED to download $1"; rm -f "lib/$2"; exit 1; }
}
get "https://repo1.maven.org/maven2/org/apache/tomcat/embed/tomcat-embed-core/11.0.26/tomcat-embed-core-11.0.26.jar" tomcat-embed-core-11.0.26.jar
get "https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-annotations-api/11.0.26/tomcat-annotations-api-11.0.26.jar" tomcat-annotations-api-11.0.26.jar
get "https://repo1.maven.org/maven2/com/h2database/h2/2.3.232/h2-2.3.232.jar" h2-2.3.232.jar
echo "Done. Jars are in lib/ - now run ./run.sh"
