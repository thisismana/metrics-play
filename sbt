#!/usr/bin/env bash

declare -a residual_args
declare -a java_args

# read sbt version from project/build.properties
if [ -z "$SBT_VERSION" ] ; then
  SBT_VERSION=$(sed -ne 's/sbt.version=//p' project/build.properties)
fi

JAR=$HOME/.sbt/sbt-launch-$SBT_VERSION.jar

mkdir -p $HOME/.sbt
mkdir -p $HOME/.sbt/boot

# download the sbt-launch.jar, if required
if [ ! -r $JAR ] ; then
  URL="http://central.maven.org/maven2/org/scala-sbt/sbt-launch/$SBT_VERSION/sbt-launch-$SBT_VERSION.jar"
  echo "Downloading sbt $SBT_VERSION from $URL"
  wget -q -O "$JAR" $URL

  if [ ! -r $JAR ] ; then
    echo "Unable to download file."
  fi
fi
# logging
dlog () {
  [[ $debug ]] && echo "$@"
}
# sbt arguments
addResidual () {
  dlog "[residual] arg = '$1'"
  residual_args=( "${residual_args[@]}" "$1" )
}
# jvm arguments
addJava () {
  dlog "[addJava] arg = '$1'"
  java_args=( "${java_args[@]}" "$1" )
}
# checks for required arguments, e.g. -jvm-debug <port>
require_arg () {
  local type="$1"
  local opt="$2"
  local arg="$3"
  if [[ -z "$arg" ]] || [[ "${arg:0:1}" == "-" ]]; then
    echo "$opt requires <$type> argument"
    exit 1
  fi
}
addDebugger () {
  addJava "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$1"
}
is_function_defined() {
  declare -f "$1" > /dev/null
}
process_args () {
  while [[ $# -gt 0 ]]; do
    case "$1" in
      -d|-debug) debug=1 && shift ;;
     -jvm-debug) require_arg port "$1" "$2" && addDebugger $2 && shift 2 ;;
          "-D*") addJava "$1" && shift ;;
            -J*) addJava "${1:2}" && shift ;;
              *) addResidual "$1" && shift ;;
    esac
  done

  is_function_defined process_my_args && {
    myargs=("${residual_args[@]}")
    residual_args=()
    process_my_args "${myargs[@]}"
  }
}

process_args "$@"

addJava "-Dsun.net.inetaddr.ttl=60"
addJava "-Duser.timezone=Europe/Berlin"
addJava "-Xms1024m"
addJava "-Xmx2048m"
addJava "-XX:ReservedCodeCacheSize=256m"
addJava "-XX:MaxMetaspaceSize=512m"

set -x
exec java \
    "${java_args[@]}" \
    -noverify \
    -Dfile.encoding=UTF8 \
    -Dsbt.boot.directory=$HOME/.sbt/boot \
    -Xmx1024M -Xss1M \
    -XX:+CMSClassUnloadingEnabled \
    -jar $JAR "${residual_args[@]}"
