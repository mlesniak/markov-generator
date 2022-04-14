#!/bin/bash

cat \
  <(echo '#!/bin/sh')\
  <(echo 'exec java -jar $0 "$@"')\
  <(echo 'exit 0') $1 >$2

chmod a+x $2