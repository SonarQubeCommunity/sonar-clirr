#!/bin/bash

set -euo pipefail

function installTravisTools {
  mkdir ~/.local
  curl -sSL https://github.com/SonarSource/travis-utils/tarball/v28 | tar zx --strip-components 1 -C ~/.local
  source ~/.local/bin/install
}

mvn install -B -e -V

# execute integration tests

# enable xvfb for selenium tests
installTravisTools
start_xvfb

cd its
mvn -Dsonar.runtimeVersion="LTS" -Dmaven.test.redirectTestOutputToFile=false verify
