#!/bin/bash

set -euo pipefail

mvn verify -Dsource.skip=true -Denforcer.skip=true -Danimal.sniffer.skip=true -Dmaven.test.skip=true -B -e -V
